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
		String title = "B³¹d tworzenia pliku ";
		String komunikat = "Nie uda³o siê utworzyæ pliku z grawimetrem ";

		JOptionPane.showMessageDialog(null, komunikat, title,
				JOptionPane.ERROR_MESSAGE);
	}

	public static void bladStrumieniaNieodnalezionoPliku() {
		String title = "B³¹d zapisu grawimetru";
		String komunikat = "B³ad zapisu Graimetru";

		JOptionPane.showMessageDialog(null, komunikat, title,
				JOptionPane.ERROR_MESSAGE);
	}

	public static int czyNadpisacPlik() {
		String title = "B³¹d zapisu";
		String komunikat = "Plik o podanej nazwie isnieje. \n"
				+ "Czy nadpisaæ plik? ";

		int returnValue = JOptionPane.showConfirmDialog(null, komunikat, title,
				JOptionPane.YES_NO_OPTION);
		return returnValue;
	}

	public static void stalaZawieraNiedozwoloneZnaki() {
		String title = "B³¹d sta³ej grawimetru";
		String komunikat = "Sta³a grawimetru zawiera niedozwolone znaki \n";

		JOptionPane.showMessageDialog(null, komunikat, title,
				JOptionPane.ERROR_MESSAGE);
	}

	public static void bladOdczytuDziennikaReferencyjnego() {
		String title = "B³¹d odczytu dziennika";
		String komunikat = "B³¹d odczytu dziennika referencyjnego (z³a œcie¿ka) \n";

		JOptionPane.showMessageDialog(null, komunikat, title,
				JOptionPane.ERROR_MESSAGE);
	}

	public static void bladKonstrukcjiDziennikaReferencyjnego(int i) {
		String title = "B³¹d konstrukcji dziennika";
		String komunikat = "B³¹d odczytu dziennika referencyjnego \n"
				+ "w lini nr "
				+ i
				+ " jest Ÿle skonstruowana jedna z liczb \n b¹dŸ liczba tokenów jest ró¿na od 3";

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
