package com.geolab.comunicat;

import javax.swing.JOptionPane;

public class DziennikComunicat {
	public static void bladNiepoprawnaLiczbaTokenow() {
		String title = "B��d odczytu dziennika";
		String komunikat = "B��d odczytu dziennika \n\n "
				+ "Mo�liwe problemy \n"
				+ "1)   Plik z dziennikiem powinien zawiera� 10 token�w lub 17 "
				+ "je�eli podawane s� wsp��dne B, L i H";

		JOptionPane.showMessageDialog(null, komunikat, title,
				JOptionPane.ERROR_MESSAGE);
	}

}
