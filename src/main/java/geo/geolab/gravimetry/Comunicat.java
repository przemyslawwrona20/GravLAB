package geo.geolab.gravimetry;

import javax.swing.JOptionPane;

public class Comunicat {

	public static void bladTworzeniaPliku() {
		String title = "B��d tworzenia pliku ";
		String komunikat = "Nie uda�o si� utworzy� pliku z grawimetrem ";

		JOptionPane.showMessageDialog(null, komunikat, title,
				JOptionPane.ERROR_MESSAGE);
	}

	public static int czyNadpisacPlik() {
		String title = "B��d zapisu";
		String komunikat = "Plik o podanej nazwie isnieje. \n"
				+ "Czy nadpisa� plik? ";

		int returnValue = JOptionPane.showConfirmDialog(null, komunikat, title,
				JOptionPane.YES_NO_OPTION);
		return returnValue;
	}

	public static void bladStrumieniaNieodnalezionoPliku() {
		String title = "B��d zapisu";
		String komunikat = "Pr�ba zapisu raportu nie uda�a si� \n \n"
				+ "Mo�liwe problemy: \n" + "1) Plik jest otwarty";

		JOptionPane.showMessageDialog(null, komunikat, title,
				JOptionPane.ERROR_MESSAGE);
	}

}
