package com.geolab.comunicat;

import javax.swing.JOptionPane;

public class DziennikComunicat {
	public static void bladNiepoprawnaLiczbaTokenow() {
		String title = "B³¹d odczytu dziennika";
		String komunikat = "B³¹d odczytu dziennika \n\n "
				+ "Mo¿liwe problemy \n"
				+ "1)   Plik z dziennikiem powinien zawieraæ 10 tokenów lub 17 "
				+ "je¿eli podawane s¹ wspó³¿êdne B, L i H";

		JOptionPane.showMessageDialog(null, komunikat, title,
				JOptionPane.ERROR_MESSAGE);
	}

}
