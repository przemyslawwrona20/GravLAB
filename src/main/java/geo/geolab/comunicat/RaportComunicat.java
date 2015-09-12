package geo.geolab.comunicat;

import javax.swing.JOptionPane;

public class RaportComunicat {

	public static void bladRozpoznaniaSchematuPomiaru() {
		String title = "B��d rozpoznania schematu";
		String komunikat = "B��d rozpoznania schematu \n\n "
				+ "Mo�liwe problemy \n"
				+ "1)	W wskazanym pliku konstrukcja punkt�w pomiarowych \n "
				+ "nie nale�y do jednego z schemat�w ABBAAB lub ABCDE ...A";

		JOptionPane.showMessageDialog(null, komunikat, title,
				JOptionPane.ERROR_MESSAGE);
	}

	public static void bladNieZaznaczonoDziennika() {
		String title = "B��d Nie zaznaczono dziennika";
		String komunikat = "B��d rozpoznania schematu \n\n "
				+ "Mo�liwe problemy \n" + "1)	Nie zaznaczono dziennika";

		JOptionPane.showMessageDialog(null, komunikat, title,
				JOptionPane.ERROR_MESSAGE);
	}

	public static void bladNieZaznaczonoInstrumentu() {
		String title = "B��d Nie zaznaczono instrumentu";
		String komunikat = "B��d rozpoznania schematu \n\n "
				+ "Mo�liwe problemy \n" + "1)	Nie zaznaczono instrumentu";

		JOptionPane.showMessageDialog(null, komunikat, title,
				JOptionPane.ERROR_MESSAGE);
	}

	public static void bladNierozpoznanoDziennika() {
		String title = "Nie rozpoznano dziennika";
		String komunikat = "Dziennika zostal skonstruowany w niepoprawny spos�b \n\n "
				+ "Mo�liwe problemy \n"
				+ "1)	Nie zgadza si� liczba token�w w dzienniku";

		JOptionPane.showMessageDialog(null, komunikat, title,
				JOptionPane.ERROR_MESSAGE);
	}
	
	public static void bladOdczytuPlikuTDT_UTC () {
		String title = "B��d odczytu pliku";
		String komunikat = "Nie odczytano pliku TDT_UTC.txt \n\n "
				+ "Mo�liwe problemy \n"
				+ "1)	Nie zgadza si� liczba token�w w dzienniku";

		JOptionPane.showMessageDialog(null, komunikat, title,
				JOptionPane.ERROR_MESSAGE);
	}

}
