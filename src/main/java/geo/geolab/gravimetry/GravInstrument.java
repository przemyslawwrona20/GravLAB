package geo.geolab.gravimetry;

import geo.geolab.controller.GravimetrController;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.StringTokenizer;

public class GravInstrument {

	private String name;
	private String path;

	public GravInstrument(String path) {
		super();
		this.path = path;
		Scanner scanner = null;
		try {
			scanner = new Scanner(new File(path));

			while (scanner.hasNext()) {
				String line = scanner.nextLine();

				if (line.contains(GravimetrController.NAZWA_GRAWIMETRU)) {
					name = line.replaceAll(
							GravimetrController.NAZWA_GRAWIMETRU, "").trim();
				}
			}
		} catch (FileNotFoundException e) {
			System.out.println("ERROR");

			e.printStackTrace();
		} finally {
			scanner.close();
		}

	}

	public GravInstrument(String name, String path) {
		super();
		this.name = name;
		this.path = path;
	}

	public double getOdczytWlasciwy(double odczyt) {
		Scanner scanner = null;
		try {
			scanner = new Scanner(new File(path));

			while (scanner.hasNext()) {
				String line = scanner.nextLine();

				if (line.contains("#"))
					continue;

				if (line.contains("@value:")) {
					line = line.replaceAll("@value:", "").trim();
					if (line.equals("null"))
						return getOdczytWlasciwyZTablicyReferencyjnej(odczyt);
					else
						return getOdczytWlasciwyZStalejGrawimetru(odczyt);
				}

			}
		} catch (FileNotFoundException e) {
			System.out.println("ERROR");

			e.printStackTrace();
		} finally {
			scanner.close();
		}

		return 1.0;
	}

	private double getOdczytWlasciwyZStalejGrawimetru(double odczyt) {

		Scanner scanner = null;
		try {
			scanner = new Scanner(new File(path));

			while (scanner.hasNext()) {
				String line = scanner.nextLine();
				if (line.contains("@value:")) {
					line = line.replaceAll("@value:", "").trim();
					return Double.parseDouble(line) * odczyt;
				}
			}
		} catch (FileNotFoundException e) {
			System.out.println("ERROR");

			e.printStackTrace();
		} finally {
			scanner.close();
		}
		return -1.0;
	}

	private double getOdczytWlasciwyZTablicyReferencyjnej(double odczyt) {
		Scanner scanner = null;
		try {
			scanner = new Scanner(new File(path));

			while (scanner.hasNext()) {
				String line = scanner.nextLine();
				if (line.contains("#") || line.contains("@"))
					continue;

				StringTokenizer tokens = new StringTokenizer(line);

				if (tokens.countTokens() == 3) {
					double klucz = Double.parseDouble(tokens.nextToken());
					double wartosc = Double.parseDouble(tokens.nextToken());
					double stala = Double.parseDouble(tokens.nextToken());

					double kluczOdczyt = odczyt - klucz;
					if (kluczOdczyt < 100 && kluczOdczyt > 0) {
						return wartosc + kluczOdczyt * stala;
					}
				}
			}
		} catch (FileNotFoundException e) {
			System.out.println("ERROR");

			e.printStackTrace();
		} finally {
			scanner.close();
		}
		return -1.0;
	}

	public void readFromTXT() {

	}

	// public static ArrayList<GravInstrument> getAll(String path) {
	// ArrayList<GravInstrument> returnList = new ArrayList<GravInstrument>();
	//
	// DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	//
	// try {
	// DocumentBuilder builder = factory.newDocumentBuilder();
	// File file = new File(path);
	// Document doc = builder.parse(file);
	//
	// NodeList nodeList = doc.getElementsByTagName("gravimeter");
	//
	// for (int i = 0; i < nodeList.getLength(); i++) {
	// Node node = nodeList.item(i);
	// Element element = (Element) node;
	//
	// if (element instanceof Element) {
	// GravInstrument gravInstrument = new GravInstrument(
	// element.getAttribute("name"),
	// element.getAttribute("table"));
	// returnList.add(gravInstrument);
	// }
	// }
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
	// }
	//
	// return returnList;
	// }

	// public static GravInstrument get(String name, String path) {
	// DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	//
	// try {
	// DocumentBuilder builder = factory.newDocumentBuilder();
	// File file = new File(path);
	// Document doc = builder.parse(file);
	//
	// NodeList nodeList = doc.getElementsByTagName("gravimeter");
	//
	// for (int i = 0; i < nodeList.getLength(); i++) {
	// Node node = nodeList.item(i);
	// Element element = (Element) node;
	//
	// if (element instanceof Element
	// && element.getAttribute("name").equals(name)) {
	// return new GravInstrument(element.getAttribute("name"),
	// element.getAttribute("table"));
	//
	// }
	// }
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
	// }
	//
	// return null;
	// }

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTablicaKalibracyjnaPath() {
		return path;
	}

	public void setTablicaKalibracyjnaPath(String tablicaKalibracyjnaPath) {
		this.path = tablicaKalibracyjnaPath;
	}

	@Override
	public String toString() {
		return "GravInstrument [name=" + name + ", tablicaKalibracyjnaPath="
				+ path + "]";
	}

}
