package geo.geolab.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;

import javax.swing.JOptionPane;

import geo.geolab.gravimetry.Comunicat;
import geo.geolab.gravimetry.GravPrzewyzszenie;
import geo.geolab.gravimetry.GravPunkt;
import geo.geolab.gravimetry.Wyrownanie;
import geo.geolab.gui.messages.PressedWyrownanieDataZapisz;

public class WyrownanieController {
	Controller controller;

	public WyrownanieController(Controller controller) {
		super();
		this.controller = controller;
	}

	public void wyrownaj() {
		controller.getView().wyrownanieNowe();
	}

	public void wyrownajZapiszRaport(PressedWyrownanieDataZapisz message) {

		String raportPath = message.getPathToSave();
		Wyrownanie wyrownanie = message.getWyrownanie();

		File plik = new File(raportPath);

		/** Tworzy nowy plik w którym bêdzie zapisny plik */
		if (!plik.exists()) {
			try {
				plik.createNewFile();
			} catch (IOException e) {
				Comunicat.bladTworzeniaPliku();
				return;
			}
		} else {
			int nadpisac = Comunicat.czyNadpisacPlik();
			if (nadpisac == JOptionPane.NO_OPTION)
				return;
		}

		// Nadpisywanie pliku
		PrintWriter zapis = null;
		try {
			zapis = new PrintWriter(plik);

			zapis.println("WYRÓWNANIE OBSERWACJI POMIARÓW GRAWIMETRYCZNYCH");

			// ZAPISYWANIE PUNKTÓW STA£YCH
			zapis.println("PUNKTY STA£E");
			zapis.print(StringToLeft("lp", 3));
			zapis.print(StringToLeft("nazwa punktu", 14));
			zapis.print(StringToLeft("przyspieszenie", 11));
			zapis.println();

			int i = 0;
			for (GravPunkt punkt : wyrownanie.getPunktyStale()) {
				i++;
				zapis.print(StringToLeft(Integer.toString(i), 3));
				zapis.print(StringToLeft(punkt.getNazwa(), 14));
				zapis.print(StringToLeft(DoubleWithScale(punkt.getValue(), 4),
						11));
				zapis.println();
			}
			zapis.println();

			// ZAPISYWANIE OBSERWACJI
			i = 0;
			zapis.println("OBSERWACJIE");
			zapis.print(StringToLeft("lp", 3));
			zapis.print(StringToLeft("poczatek", 10));
			zapis.print(StringToLeft("koniec", 10));
			zapis.print(StringToLeft("przewyzszenie", 10));
			zapis.print(StringToLeft("blad", 10));
			zapis.println();
			for (GravPrzewyzszenie przewyzszenie : wyrownanie
					.getPrzewyzszenia()) {

				zapis.print(StringToLeft(Integer.toString(i + 1), 3));
				zapis.print(StringToLeft(przewyzszenie.getSource(), 10));
				zapis.print(StringToLeft(przewyzszenie.getDest(), 10));
				zapis.print(StringToLeft(
						DoubleWithScale(przewyzszenie.getValue(), 4), 10));
				zapis.print(StringToLeft(
						DoubleWithScale(przewyzszenie.getBlad(), 4), 10));
				zapis.println();
				i++;
			}

			// // ZAPISYWANIE PÊTLI PIERWSZEJ
			// zapis.println("PIERWSZA PETLA");
			// zapis.print(StringToLeft("lp", 5));
			// zapis.print(StringToLeft("g ref.", 12));
			// zapis.print(StringToLeft("dryft", 10));
			// zapis.print(StringToLeft("g ref. popra.", 15));
			// zapis.print(StringToLeft("delta g", 15));
			// zapis.println();
			//
			// for (int j = 0; j < 4; j++) {
			// zapis.print(StringToLeft(Integer.toString(j + 1), 5));
			// zapis.print(StringToLeft(DoubleWithScale(gRef[j], 4), 12));
			// zapis.print(StringToLeft(
			// DoubleWithScale(dryftPetlaPierwsza[j], 4), 10));
			// zapis.print(StringToLeft(
			// DoubleWithScale(gRefPoprawionePetlaPierwsza[j], 4), 15));
			// zapis.print(StringToLeft(
			// DoubleWithScale(deltaGPetlaPierwsza[j], 4), 15));
			// zapis.println();
			// }
			// zapis.println();

		} catch (FileNotFoundException e) {
			Comunicat.bladStrumieniaNieodnalezionoPliku();
		} finally {
			zapis.close();
		}
	}

	public static String StringToLeft(String name, int size) {
		StringBuilder returnStringBuilder = new StringBuilder();
		returnStringBuilder.append(name);
		for (int i = 0; i < size - name.length(); i++) {
			returnStringBuilder.append(" ");
		}

		return returnStringBuilder.toString();
	}

	public static String DoubleWithScale(Double value, int scale) {
		BigDecimal bigDecimal = new BigDecimal(value).setScale(scale,
				RoundingMode.HALF_EVEN);
		return bigDecimal.toString();
	}

}
