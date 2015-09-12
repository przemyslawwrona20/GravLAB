package geo.geolab.comunicat;

import javax.swing.JOptionPane;

public class GravComunicat {
	public static void bladNiezaznaczonoRodzajuGrawimetru() {
		String title = "Nie zaznaczono opcji ";
		String komunikat = "Nie zaznaczono opcji ";

		JOptionPane.showMessageDialog(null, komunikat, title,
				JOptionPane.ERROR_MESSAGE);
	}

	public static void bladTworzeniaPlikuGrawimetr() {
		String title = "B��d tworzenia pliku ";
		String komunikat = "Nie uda�o si� utworzy� pliku z grawimetrem ";

		JOptionPane.showMessageDialog(null, komunikat, title,
				JOptionPane.ERROR_MESSAGE);
	}

	public static void bladStrumieniaNieodnalezionoPliku() {
		String title = "B��d zapisu grawimetru";
		String komunikat = "B�ad zapisu Graimetru";

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

	public static void stalaZawieraNiedozwoloneZnaki() {
		String title = "B��d sta�ej grawimetru";
		String komunikat = "Sta�a grawimetru zawiera niedozwolone znaki \n";

		JOptionPane.showMessageDialog(null, komunikat, title,
				JOptionPane.ERROR_MESSAGE);
	}

	public static void bladOdczytuDziennikaReferencyjnego() {
		String title = "B��d odczytu dziennika";
		String komunikat = "B��d odczytu dziennika referencyjnego (z�a �cie�ka) \n";

		JOptionPane.showMessageDialog(null, komunikat, title,
				JOptionPane.ERROR_MESSAGE);
	}

	public static void bladKonstrukcjiDziennikaReferencyjnego(int i) {
		String title = "B��d konstrukcji dziennika";
		String komunikat = "B��d odczytu dziennika referencyjnego \n"
				+ "w lini nr "
				+ i
				+ " jest �le skonstruowana jedna z liczb \n b�d� liczba token�w jest r�na od 3";

		JOptionPane.showMessageDialog(null, komunikat, title,
				JOptionPane.ERROR_MESSAGE);
	}

	public static void bladNieznanyBlad() {
		String title = "Nieznany blad ";
		String komunikat = "NIeznany blad ";

		JOptionPane.showMessageDialog(null, komunikat, title,
				JOptionPane.ERROR_MESSAGE);
	}
}
