package geo.geolab.controller;

import java.io.*;
import java.util.*;

import javax.swing.JOptionPane;
//import javax.xml.parsers.DocumentBuilder;
//import javax.xml.parsers.DocumentBuilderFactory;
//import javax.xml.parsers.ParserConfigurationException;
//import javax.xml.transform.OutputKeys;
//import javax.xml.transform.Transformer;
//import javax.xml.transform.TransformerConfigurationException;
//import javax.xml.transform.TransformerException;
//import javax.xml.transform.TransformerFactory;
//import javax.xml.transform.TransformerFactoryConfigurationError;
//import javax.xml.transform.dom.DOMSource;
//import javax.xml.transform.stream.StreamResult;
//
//import org.w3c.dom.Document;
//import org.w3c.dom.Element;
//import org.w3c.dom.NodeList;
//import org.xml.sax.SAXException;

import com.geolab.comunicat.GravComunicat;

import geo.geolab.gui.messages.PressedGrawimetrDodajData;
import geo.geolab.gui.messages.PressedGrawimetrInformacjeData;
import geo.geolab.gui.messages.PressedGrawimetrUsunData;

public class GravimetrController {
	public static final String NAZWA_GRAWIMETRU = "@name: ";
	public static final String VALUE = "@value: ";

	Controller controller;

	public GravimetrController(Controller controller) {
		super();
		this.controller = controller;
	}

	public void dodajGrawimetr() {
		controller.getView().grawimetrDodaj();
	}

	public void dodajGrawimetr(PressedGrawimetrDodajData data) {
		/** Czy zaznaczono RadioButton Sta³a Grawimetru */
		boolean isStala = data.isStala();

		/** Czy zaznaczono RadioButton Dziennik Referencyjny */
		boolean isPath = data.isPath();

		/** Nazwa grawimetru */
		String nazwa = data.getName();

		/**
		 * Sta³a grawimetru lub œciezka do dziennika referencyjnego w zale¿noœci
		 * od zaznaczonej opcji
		 */
		String value = data.getPath();

		/** Miejsce w którym bêdzie zapisany grawimetr */
		String grawimetrSavePath = Controller.GRAV_PATH + nazwa + ".txt";

		/** Tworzy nowy plik w którym bêdzie zapisny plik */
		File plik = new File(grawimetrSavePath);

		/** if plik nie istniej tworzy nowy */
		if (!plik.exists()) {
			try {
				plik.createNewFile();
			} catch (IOException e) {
				GravComunicat.bladTworzeniaPlikuGrawimetr();
			}
		}
		/** Plik ju¿ isnieje czy naspisaæ ? */
		else {
			int nadpisacPlik = GravComunicat.czyNadpisacPlik();

			/** Jeœli nie nadpisywaæ wróæ */
			if (nadpisacPlik == JOptionPane.NO_OPTION)
				return;
		}

		/** Jeœli nie poprawnie stworzono grawimetr zwróæ */
		if (!createNewGrawimetr(isStala, isPath, nazwa, value,
				grawimetrSavePath)) {
			File file = new File(grawimetrSavePath);
			file.delete();
			PressedGrawimetrDodajData message = new PressedGrawimetrDodajData(
					isStala, isPath, nazwa, "");
			controller.getView().grawimetrDodaj(message);
			return;
		}

	}

	public void usunGrawimetr() {
		controller.getView().grawimetrUsun(getAllName());
	}

	public void usunGrawimetr(PressedGrawimetrUsunData data) {
		String path = Controller.GRAV_PATH + data.getValue() + ".txt";
		File file = new File(path);
		file.delete();
		controller.getView().grawimetrUsun(getAllName());
	}

	/** Pobiera informacjie o zaznaczonym grawimetrze z okna GRAWIMETR -> USUÑ */
	public void getInformacjeFromGrawimetr(
			PressedGrawimetrInformacjeData message) {
		String filePath = Controller.GRAV_PATH + message.getNazwaGrawimetru()
				+ ".txt";
		File file = new File(filePath);

		Scanner scanner = null;
		int i = 0;
		try {
			scanner = new Scanner(file);
			while (scanner.hasNext()) {
				String line = scanner.nextLine();

				if (line.contains("#"))
					continue;

				else if (line.contains(VALUE)) {
					line = line.replace(VALUE, "").trim();
					message.setStalaGrawimetru(line);
				}

				else {
					StringTokenizer tokens = new StringTokenizer(line);

					if (tokens.countTokens() != 3)
						continue;

					message.add(i, 0, tokens.nextToken());
					message.add(i, 1, tokens.nextToken());
					message.add(i, 2, tokens.nextToken());

					i++;
				}
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			scanner.close();
		}
	}

	/**
	 * 
	 * @param isStala
	 * @param isPath
	 * @param nazwa
	 * @param value
	 * @return Jesli udalo sie zapisac zwaca sciezke pliku jesli nie
	 */
	private boolean createNewGrawimetr(boolean isStala, boolean isPath,
			String nazwa, String value, String grawimetrSavePath) {

		/**
		 * Jesli zaznaczono ze grawimetr charakteryzuje stala grawimetru i
		 * jednoczesnie jest ona nie poprawna oraz jesli zaznaczono ze grawimetr
		 * charakteryzuje tablica referencyjna i jednoczesnie jest ona nie
		 * poprawna lub dziennik jest ¿le skonstruowany zwraca ponownie
		 * wiadomoœæ z danymi do poprawy
		 */
		if ((isStala && !isCorrectStala(value))
				|| (!isStala && !isCorrectPathFile(value))) {
			// B³¹d wprowadzania danych odpowiednie komunikaty s¹ wymuszane w
			// funkcjiach
			return false;
		}

		/**
		 * Kontrola danych jest poprawna je¿eli wybrano sta³¹ grawimetru jest
		 * zapisywany grawimetr z sta³¹ grawimetru
		 */
		if (isStala) {
			PrintWriter zapis = null;
			try {
				zapis = new PrintWriter(grawimetrSavePath);
				zapis.println("# Dziennik referencyjny");
				zapis.println("# name 		nazwa instrumentu");
				zapis.println("# value		wartosc stalej grawimetru");
				zapis.println("				lub NULL jeœli zdeklarowano tablice referencyjn¹");
				zapis.println("# Miedzy kluczem a znakiem : nie moze byc zadnych");
				zapis.println("# dodatkowych znakow w tym bia³ych znakow");
				zapis.println("");
				zapis.println(GravimetrController.NAZWA_GRAWIMETRU + nazwa);
				zapis.println("@stala: " + isStala);
				zapis.println("@value: " + value.replace(",", "."));
				zapis.println("");

				return true;
			} catch (FileNotFoundException e) {
				GravComunicat.bladStrumieniaNieodnalezionoPliku();
			} finally {
				zapis.close();
			}
		}

		/**
		 * Kontrola danych jest poprawna je¿eli wybrano tablicê referencyjn¹
		 * jest zapisywany grawimetr z œcie¿k¹ do tablicy referencyjnej
		 */
		else if (isPath) {
			PrintWriter zapis = null;
			Scanner scanner = null;
			try {
				zapis = new PrintWriter(grawimetrSavePath);
				zapis.println("# Dziennik referencyjny");
				zapis.println("# name 		nazwa instrumentu");
				zapis.println("# stala 	zawiera informacje czy instrument");
				zapis.println("#		korzysta z tablic referencyjnych");
				zapis.println("# value		wartosc stalej");
				zapis.println("# Miedzy kluczem a znakiem : nie moze byc zadnych");
				zapis.println("# dodatkowych znakow w tym bia³ych znakow");
				zapis.println("");
				zapis.println("@name: " + nazwa);
				zapis.println("@value: null");
				zapis.println("");

				File file = new File(value);
				scanner = new Scanner(file);

				while (scanner.hasNext()) {
					String line = scanner.nextLine();
					zapis.println(line);
				}

				return true;

			} catch (FileNotFoundException e) {
				GravComunicat.bladStrumieniaNieodnalezionoPliku();
				return false;
			} finally {
				scanner.close();
				zapis.close();
			}

		} else {
			GravComunicat.bladNiezaznaczonoRodzajuGrawimetru();
			return false;
		}
		GravComunicat.bladNieznanyBlad();
		return false;
	}

	/**
	 * Metoda sprawdzajaca czy podana stala grawimetru jest poprawna Stala jest
	 * poprawna jesli jest LICZBA i zawiera znak ',' lub '.'
	 * 
	 * @param stala
	 *            stala grawimetru
	 * @return true jesli stala jest poprawna
	 */
	private boolean isCorrectStala(String stala) {
		stala = stala.replace(",", ".");
		if (isLiczba(stala))
			return true;
		else {
			GravComunicat.stalaZawieraNiedozwoloneZnaki();
			return false;
		}

	}

	/**
	 * Sprawdza czy pod podan¹ isnieje plik tekstowy który zawiera poprawnie
	 * skonstuowany dziennik
	 */
	private boolean isCorrectPathFile(String path) {
		File file = new File(path);
		int i = 0;
		Scanner in = null;
		try {
			in = new Scanner(file);

			while (in.hasNext()) {
				String line = in.nextLine();
				StringTokenizer tokens = new StringTokenizer(line);

				/**
				 * Jeœli liczba tokenów jest ró¿na od trzy lub chocia¿ jeden z
				 * tokenów jest ró¿ny od 3
				 */
				if (tokens.countTokens() != 3 || !isLiczba(tokens.nextToken())
						|| !isLiczba(tokens.nextToken())
						|| !isLiczba(tokens.nextToken())) {
					GravComunicat.bladKonstrukcjiDziennikaReferencyjnego(i);
					return false;
				}
				i++;
			}

		} catch (FileNotFoundException e) {
			GravComunicat.bladOdczytuDziennikaReferencyjnego();
			return false;
		} finally {
			in.close();
		}

		return true;
	}

	/**
	 * Metoda sprawdzajaca czy podany tekst jest liczba Tekst jest liczba jesli
	 * zawiera znaki ktore sa liczbami od 0 do 9 oraz separator dziesietny w
	 * postaci znaku "."
	 * 
	 * @param value
	 *            Sprawdzana wartosc
	 * @return true: jesli value jest liczba
	 */
	private boolean isLiczba(String value) {

		for (int i = 0; i < value.length(); i++) {
			int znak = (int) value.charAt(i);

			if ((znak < 46) || (znak == 47) || (znak > 57))
				return false;
		}
		return true;
	}

	public static String[] getAllName() {
		File directory = new File(Controller.GRAV_PATH);
		String[] myFiles = directory.list(new FilenameFilter() {
			public boolean accept(File directory, String fileName) {
				return fileName.endsWith(".txt");
			}
		});

		for (int i = 0; i < myFiles.length; i++) {
			String name = myFiles[i];
			myFiles[i] = name.replaceAll(".txt", "");
		}

		return myFiles;
	}
}

// private void addToXMLFile(GravInstrument gravInstrument, String path) {
// DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//
// try {
// DocumentBuilder builder = factory.newDocumentBuilder();
//
// File file = new File(path);
// Document doc = builder.parse(file);
//
// NodeList nodeList = doc.getElementsByTagName("gravimeters");
// Element rootItem = (Element) nodeList.item(0);
//
// // TWORZENIE WÊZ£A
// Element childElement = doc.createElement("gravimeter");
// childElement.setAttribute("name", gravInstrument.getName());
// childElement.setAttribute("table",
// gravInstrument.getTablicaKalibracyjnaPath());
//
// rootItem.appendChild(childElement);
//
// // ZAPIS DO PLIKU XML
// Transformer transformer = TransformerFactory.newInstance()
// .newTransformer();
// transformer.setParameter(OutputKeys.INDENT, "yes");
// transformer.setParameter(OutputKeys.METHOD, "xml");
//
// transformer.transform(new DOMSource(doc), new StreamResult(
// new FileOutputStream(file)));
//
// } catch (ParserConfigurationException e) {
// // TODO Auto-generated catch block
// e.printStackTrace();
// } catch (SAXException e) {
// // TODO Auto-generated catch block
// e.printStackTrace();
// } catch (IOException e) {
// // TODO Auto-generated catch block
// e.printStackTrace();
// } catch (TransformerConfigurationException e) {
// // TODO Auto-generated catch block
// e.printStackTrace();
// } catch (TransformerFactoryConfigurationError e) {
// // TODO Auto-generated catch block
// e.printStackTrace();
// } catch (TransformerException e) {
// // TODO Auto-generated catch block
// e.printStackTrace();
// }
//
// }
