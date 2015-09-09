package geo.geolab.gravimetry;

import java.io.*;

import javax.swing.JOptionPane;

public class RaportABCDEA extends Raport {

	// ------------------------- WARTOSCI POPRAWEK -------------------------
	Double[] dryftPetla;
	Double[] gRefPoprawione;
	Double[] deltaG;

	public RaportABCDEA(GravDziennik dziennik, int typPoprawki,
			double wspolczynnikSztywnosciZiemi) {
		super(dziennik, typPoprawki, wspolczynnikSztywnosciZiemi);
		dryftPetla = new Double[dziennik.size()];
		gRefPoprawione = new Double[dziennik.size()];
		deltaG = new Double[dziennik.size()];
	}

	protected Double[] obliczPoprawkiGradientu() {
		int i = 0;
		for (GravStanowisko stanowisko : dziennik.getList()) {
			poprawkiGradientu[i] = stanowisko.getWysokosc() * GRADIENT / 100;
			i++;
		}
		return poprawkiGradientu;
	}

	protected Double[] obliczLuni() {
		int i = 0;
		for (GravStanowisko stanowisko : dziennik.getList()) {
			poprawkaLuni[i] = stanowisko.getLuni(typPoprawki,
					wspolczynnikSztywnosciZiemi);
			i++;
		}
		return poprawkaLuni;
	}

	protected Double[] obliczOdczytWlasciwy() {
		int i = 0;
		GravInstrument instrumentPomiarowy = dziennik.getGrawimetr();
		for (GravStanowisko stanowisko : dziennik.getList()) {
			odczytPrawidlowy[i] = instrumentPomiarowy
					.getOdczytWlasciwy(stanowisko.getOdczyt());
			i++;
		}
		return odczytPrawidlowy;
	}

	protected Double[] obliczGRef() {
		for (int i = 0; i < dziennik.size(); i++) {
			gRef[i] = odczytPrawidlowy[i] + poprawkiGradientu[i]
					+ poprawkaLuni[i];
		}
		return gRef;
	}

	protected double obliczDryft() {
		GravStanowisko gravStanowiskoFirst = dziennik.getStanowisko(0);
		GravStanowisko gravStanowiskoLast = dziennik.getStanowisko(dziennik
				.getList().size() - 1);

		/** Liczy ró¿nicê czasu w sekundach */
		long deltaTime = (gravStanowiskoFirst.getData().getTimeInMillis() - gravStanowiskoLast
				.getData().getTimeInMillis()) / 1000;

		/** Liczy ró¿nicê wykonanych odczytow */
		double deltagReferencyjne = gRef[0] - gRef[gRef.length - 1];

		/** Liczy zmianê dryftu na godzine */
		double dryft = (deltagReferencyjne * deltaTime)
				/ (deltaTime * deltaTime) * 3600;

		return dryft;
	}

	protected Double[] obliczDryftPetla() {
		GravStanowisko stanowiskoPoczatkowe = dziennik.getStanowisko(0);
		long czasPoczatkowy = stanowiskoPoczatkowe.getData().getTimeInMillis();

		int i = 0;
		double dryft = obliczDryft();
		for (GravStanowisko stanowisko : dziennik.getList()) {
			long czasAktualny = stanowisko.getData().getTimeInMillis();
			double poprawkaDryftu = (czasPoczatkowy - czasAktualny) * dryft
					/ 1000 / 3600;
			dryftPetla[i] = poprawkaDryftu;
			i++;
		}

		return dryftPetla;
	}

	protected Double[] obliczGRefPoprawione() {

		for (int i = 0; i < dziennik.getList().size(); i++) {
			gRefPoprawione[i] = gRef[i] + dryftPetla[i];
		}

		return gRefPoprawione;
	}

	protected Double[] obliczDeltaG() {
		deltaG[dziennik.size() - 1] = 0.0;
		for (int i = (dziennik.size() - 1); i > 0; i--) {
			deltaG[i - 1] = gRefPoprawione[i] - gRefPoprawione[i - 1];
		}

		return deltaG;
	}

	public void oblicz() {
		obliczPoprawkiGradientu();
		obliczLuni();
		obliczOdczytWlasciwy();
		obliczGRef();
		obliczDryftPetla();
		obliczGRefPoprawione();
		obliczDeltaG();
	}

	public void export(String path) {
		File plik = new File(path);

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

			zapis.println("OBIEKT - MIEJSCE POMIARU: ");
			zapis.println("OBSERWATOR: ");
			zapis.println("SEKRETARZ: ");
			zapis.println("INSTRUMENT: " + dziennik.getGrawimetr().getName());
			zapis.println();
			zapis.println("TYP POPRAWKI: " + this.getTypPoprawki());
			zapis.println("WSPÓ£CZYNNIK SZTYWNOŒCI ZIEMI: "
					+ this.getWspolczynnikSztywnosciZiemi());

			zapis.println("");
			zapis.println("ZAPIS DZIENNIKA");

			// ZAPISYWANIE DZIENNIKA
			zapis.println("DZIENNIK POMIAROWY");
			zapis.print(StringToLeft("lp", 3));
			zapis.print(StringToLeft("nazwa punktu", 14));
			zapis.print(StringToLeft("data", 11));
			zapis.print(StringToLeft("h m s", 10));
			zapis.print(StringToLeft("odczyt", 12));
			zapis.print(StringToLeft("wys(cm)", 8));
			zapis.print(StringToLeft("bl.(mgl)", 10));
			zapis.println();

			int i = 0;
			for (GravStanowisko stanowisko : dziennik.getList()) {
				i++;
				zapis.print(StringToLeft(Integer.toString(i), 3));
				zapis.print(StringToLeft(stanowisko.getNazwa(), 14));
				zapis.print(StringToLeft(stanowisko.getStringData(" "), 11));
				zapis.print(StringToLeft(stanowisko.getStringTime(" "), 10));
				zapis.print(StringToLeft(
						DoubleWithScale(stanowisko.getOdczyt(), 4), 12));
				zapis.print(StringToLeft(
						DoubleWithScale(stanowisko.getWysokosc(), 1), 8));
				zapis.print(StringToLeft(
						DoubleWithScale(stanowisko.getBlad(), 4), 10));
				zapis.println();
			}
			zapis.println();

			// ZAPISYWANIE POPRAWEK
			i = 0;
			zapis.println("WPROWADZANE POPRAWKI");
			zapis.print(StringToLeft("lp", 3));
			zapis.print(StringToLeft("gradient(mgl)", 14));
			zapis.print(StringToLeft("luni(mgl)", 14));
			zapis.print(StringToLeft("g ref.(mgl)", 12));
			zapis.println();
			for (GravStanowisko stanowisko : dziennik.getList()) {

				zapis.print(StringToLeft(Integer.toString(i + 1), 3));
				// zapis.print(StringToLeft(stanowisko.getNazwa(), 15));
				zapis.print(StringToLeft(
						DoubleWithScale(stanowisko.getPoprawkaGradientu(), 4),
						14));
				zapis.print(StringToLeft(DoubleWithScale(poprawkaLuni[i], 4),
						14));
				zapis.print(StringToLeft(DoubleWithScale(gRef[i], 4), 12));
				// zapis.print(StringToLeft(stanowisko.getStringTime(" "), 10));
				// zapis.print(StringToLeft(
				// Double.toString(stanowisko.getOdczyt()), 12));
				// zapis.print(StringToLeft(
				// Double.toString(stanowisko.getWysokosc()), 15));
				zapis.println();
				i++;
			}

			// ZAPISYWANIE DRYFTU
			zapis.println();
			zapis.println("LINIOWY MODEL DRYFTU DLA POMIARU METODA PRZEKROJOWA");
			zapis.println();
			zapis.println("DRYFT (MGL/GODZ) = "
					+ DoubleWithScale(obliczDryft(), 4));
			zapis.println();

			// ZAPISYWANIE PÊTLI
			zapis.println("PIERWSZA PETLA");
			zapis.print(StringToLeft("lp", 5));
			zapis.print(StringToLeft("g ref.", 12));
			zapis.print(StringToLeft("dryft", 10));
			zapis.print(StringToLeft("g ref. popra.", 15));
			zapis.print(StringToLeft("delta g", 15));
			zapis.println();

			for (int j = 0; j < dziennik.size(); j++) {
				zapis.print(StringToLeft(Integer.toString(j + 1), 5));
				zapis.print(StringToLeft(DoubleWithScale(gRef[j], 4), 12));
				zapis.print(StringToLeft(DoubleWithScale(dryftPetla[j], 4), 10));
				zapis.print(StringToLeft(DoubleWithScale(gRefPoprawione[j], 4),
						15));
				zapis.print(StringToLeft(DoubleWithScale(deltaG[j], 4), 15));
				zapis.println();
			}
			zapis.println();

			// OSTATECZNY WYNIK
			// zapis.println("OSTATECZNY WYNIK");
			// zapis.println("dg = " + DoubleWithScale(getDeltaG(), 4));
			// zapis.println("BLAD = ");

		} catch (FileNotFoundException e) {
			Comunicat.bladStrumieniaNieodnalezionoPliku();
		} finally {
			zapis.close();
		}

	}

}
