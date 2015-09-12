package geo.geolab.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.StringTokenizer;

import geo.geolab.comunicat.DziennikComunicat;
import geo.geolab.gui.messages.PressedDziennikDodajTableData;
import geo.geolab.gui.messages.PressedDziennikImportData;
import geo.geolab.gui.messages.PressedDziennikInformacjeData;
import geo.geolab.gui.messages.PressedDziennikUsunData;

public class DziennikController {
	public static final String NAZWA_RAPORTU = "@nazwa: ";
	public static final String OBIEKT = "@obiekt: ";
	public static final String OBSERWATOR = "@obserwator: ";
	public static final String SEKRETARZ = "@sekretarz: ";

	public static final int SZEROKOSC_ZAPISYWANEJ_KOMORKI = 15;

	Controller controller;

	public DziennikController(Controller controller) {
		super();
		this.controller = controller;
	}

	public void dziennikDodaj() {
		controller.getView().dziennikDodaj();
	}

	public void dziennikUsun() {
		controller.getView().dziennikUsun(getAllName());
	}

	public void dziennikUsun(PressedDziennikUsunData data) {

		String path = Controller.DZIENNIK_PATH + data.getValue() + ".txt";
		File file = new File(path);
		file.canExecute();

		System.out.println("Usuwam " + path);
		file.delete();

		controller.getView().dziennikUsun(getAllName());

	}

	public void importujDziennik() {
		controller.getView().dziennikImport();
	}

	public void importujDziennik(PressedDziennikImportData message) {

		String zapisPath = Controller.DZIENNIK_PATH
				+ message.getNazwaDziennika() + ".txt";

		File file = new File(message.getPath());

		PrintWriter zapis = null;
		Scanner scanner = null;
		try {
			// Tworzenie nowego pliku do zapisu o ile nieistnieje
			// w przeciwnym wypadku nadpisuje
			File nowyPlik = new File(zapisPath);
			if (nowyPlik.exists() == true)
				nowyPlik.createNewFile();

			// Otwieranie strumieni do odzytu i zapisu
			zapis = new PrintWriter(zapisPath);
			scanner = new Scanner(file);

			zapis.println("#Pierwsza linia dziennika");
			zapis.println("@nazwa: " + message.getNazwaDziennika());
			zapis.println("@obiekt: " + message.getObiekt());
			zapis.println("@obserwator: " + message.getObserwator());
			zapis.println("@sekretarz: " + message.getSekretarz());
			zapis.println();

			// Odczyt i zapisywaniekolejnych lini dziennika
			while (scanner.hasNext()) {
				String line = scanner.nextLine();
				StringTokenizer tokens = new StringTokenizer(line);

				if (tokens.countTokens() == 10 || tokens.countTokens() == 17)
					zapis.println(line);
				else {
					DziennikComunicat.bladNiepoprawnaLiczbaTokenow();
					zapis.close();
					nowyPlik.delete();
				}

			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		} finally {
			scanner.close();
			zapis.close();
		}
	}

	public void dodajDziennik(PressedDziennikDodajTableData message) {
		System.out.println("Bêdê zapisywaæ dziennik");

		String zapisPath = Controller.DZIENNIK_PATH
				+ message.getNazwaDziennika() + ".txt";

		PrintWriter zapis = null;

		try {
			// Tworzenie nowego pliku do zapisu o ile nieistnieje
			// w przeciwnym wypadku nadpisuje
			File nowyPlik = new File(zapisPath);
			if (nowyPlik.exists() == true)
				nowyPlik.createNewFile();

			// Otwieranie strumieni do zapisu
			zapis = new PrintWriter(zapisPath);

			zapis.println("#Pierwsza linia dziennika");
			zapis.println("@nazwa: " + message.getNazwaDziennika());
			zapis.println("@obiekt: " + message.getObiekt());
			zapis.println("@obserwator: " + message.getObserwator());
			zapis.println("@sekretarz: " + message.getSekretarz());
			zapis.println();

			// Odczyt i zapisywaniekolejnych lini dziennika
			for (int i = 0; i < message.getDziennik().length; i++) {
				String[] line = message.getDziennik()[i];

				StringBuilder stringBuilder = new StringBuilder();
				for (int j = 0; j < line.length; j++) {
					stringBuilder.append(dopasujDoSzerokosciZapisywanejKomorki(
							message.getDziennik()[i][j],
							SZEROKOSC_ZAPISYWANEJ_KOMORKI));
				}
				zapis.println(stringBuilder.toString());
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		} finally {
			zapis.close();
		}
	}

	public static String[] getAllName() {
		File directory = new File(Controller.DZIENNIK_PATH);
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

	public void setInformacje(PressedDziennikInformacjeData message) {
		String path = Controller.DZIENNIK_PATH + message.getNazwaDziennika()
				+ ".txt";
		File file = new File(path);

		Scanner scanner = null;
		try {
			scanner = new Scanner(file);
			int i = 0;

			while (scanner.hasNext()) {
				String line = scanner.nextLine();

				if (line.contains("#")) {
					continue;
				} else if (line.contains(NAZWA_RAPORTU)) {
					continue;
				} else if (line.contains(OBIEKT)) {
					message.setNazwaObiektu(line.replace(OBIEKT, ""));
					continue;
				} else if (line.contains(OBSERWATOR)) {
					message.setObserwator(line.replace(OBSERWATOR, ""));
					continue;
				} else if (line.contains(SEKRETARZ)) {
					message.setSekretarz(line.replace(SEKRETARZ, ""));
					continue;
				} else {
					StringTokenizer tokens = new StringTokenizer(line);

					if (tokens.countTokens() == 17) {
						String punkt = tokens.nextToken();
						String Bst = tokens.nextToken();
						String Bmin = tokens.nextToken();
						String Bsek = tokens.nextToken();
						String Lst = tokens.nextToken();
						String Lmin = tokens.nextToken();
						String Lsek = tokens.nextToken();
						String H = tokens.nextToken();
						String dzien = tokens.nextToken();
						String miesiac = tokens.nextToken();
						String rok = tokens.nextToken();
						String godzina = tokens.nextToken();
						String minuta = tokens.nextToken();
						String sekunda = tokens.nextToken();
						String odczyt = tokens.nextToken();
						String wysokosc = tokens.nextToken();
						String blad = tokens.nextToken();

						message.add(i, 0, punkt);
						message.add(i, 1, Bst + " " + Bmin + " " + Bsek);
						message.add(i, 2, Lst + " " + Lmin + " " + Lsek);
						message.add(i, 3, H);
						message.add(i, 4, dzien);
						message.add(i, 5, miesiac);
						message.add(i, 6, rok);
						message.add(i, 7, godzina);
						message.add(i, 8, minuta);
						message.add(i, 9, sekunda);
						message.add(i, 10, odczyt);
						message.add(i, 11, wysokosc);
						message.add(i, 12, blad);

						i++;

					} else {
						continue;
					}
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			scanner.close();
		}
	}

	public String dopasujDoSzerokosciZapisywanejKomorki(String str, int size) {
		size--;
		if (size <= str.length())
			return str + " ";
		else {
			for (int i = 0; i < (size - str.length()); i++) {
				str = str + " ";
			}
			return str + " ";
		}
	}
}
