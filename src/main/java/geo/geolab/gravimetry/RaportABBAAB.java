package geo.geolab.gravimetry;

import java.io.*;

import javax.swing.JOptionPane;

public class RaportABBAAB extends Raport {

	public static final int PETLA_PIERWSZA = 0;
	public static final int PETLA_DRUGA = 2;

	// --------------- PETLA PIERWSZA ---------------
	Double[] dryftPetlaPierwsza;
	Double[] gRefPoprawionePetlaPierwsza;
	Double[] deltaGPetlaPierwsza;

	// --------------- PETLA DRUGA ---------------
	Double[] dryftPetlaDruga;
	Double[] gRefPoprawionePetlaDruga;
	Double[] deltaGPetlaDruga;

	public RaportABBAAB(GravDziennik dziennik, int typPoprawki,
			double wspolczynnikSztywnosciZiemi) {
		super(dziennik, typPoprawki, wspolczynnikSztywnosciZiemi);

		dryftPetlaPierwsza = new Double[4];
		gRefPoprawionePetlaPierwsza = new Double[4];
		deltaGPetlaPierwsza = new Double[4];

		dryftPetlaDruga = new Double[4];
		gRefPoprawionePetlaDruga = new Double[4];
		deltaGPetlaDruga = new Double[4];
	}

	protected Double[] obliczPoprawkiGradientu() {
		int i = 0;
		for (GravStanowisko stanowisko : dziennik.getList()) {
			poprawkiGradientu[i] = stanowisko.getPoprawkaGradientu();
			i++;
		}

		return poprawkiGradientu;
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

	protected Double[] obliczLuni() {
		int i = 0;
		for (GravStanowisko stanowisko : dziennik.getList()) {
			poprawkaLuni[i] = stanowisko.getLuni(typPoprawki,
					wspolczynnikSztywnosciZiemi);
			i++;
		}
		return poprawkaLuni;
	}

	protected Double[] obliczGRef() {
		for (int i = 0; i < dziennik.size(); i++) {
			gRef[i] = odczytPrawidlowy[i] + poprawkiGradientu[i]
					+ poprawkaLuni[i];
		}
		return gRef;
	}

	protected double obliczDryft(int numerPetli) {

		double licznik = (gRef[3 + numerPetli] - gRef[0 + numerPetli])
				* (dziennik.getStanowisko(3 + numerPetli).getData()
						.getTimeInMillis() - dziennik
						.getStanowisko(0 + numerPetli).getData()
						.getTimeInMillis())
				+ (gRef[2 + numerPetli] - gRef[1 + numerPetli])
				* (dziennik.getStanowisko(2 + numerPetli).getData()
						.getTimeInMillis() - dziennik
						.getStanowisko(1 + numerPetli).getData()
						.getTimeInMillis());

		double mianownik = Math.pow(dziennik.getStanowisko(3 + numerPetli)
				.getData().getTimeInMillis()
				- dziennik.getStanowisko(0 + numerPetli).getData()
						.getTimeInMillis(), 2)
				+ Math.pow(dziennik.getStanowisko(2 + numerPetli).getData()
						.getTimeInMillis()
						- dziennik.getStanowisko(1 + numerPetli).getData()
								.getTimeInMillis(), 2);

		/** Oblicza zmiane dryftu na godzine */
		double dryft = licznik / mianownik * 1000 * 3600;

		return dryft;
	}

	protected Double[] obliczDryftPetla(int numerPetli) {
		Double[] returnValue = new Double[4];

		long czasPoczatkowy = dziennik.getStanowisko(0 + numerPetli).getData()
				.getTimeInMillis();

		double dryft = obliczDryft(numerPetli);

		int licznik = 0;
		while (licznik < 4) {
			long czasAktualny = dziennik
					.getStanowisko(0 + numerPetli + licznik).getData()
					.getTimeInMillis();

			double obliczonyDryft = -(czasAktualny - czasPoczatkowy) * dryft
					/ 1000 / 3600;
			returnValue[licznik] = obliczonyDryft;
			licznik++;
		}

		if (numerPetli == PETLA_PIERWSZA)
			dryftPetlaPierwsza = returnValue;
		else if (numerPetli == PETLA_DRUGA)
			dryftPetlaDruga = returnValue;

		return returnValue;
	}

	protected Double[] obliczGRefPoprawione(int nummerPetli) {
		Double[] returnValue = new Double[4];

		Double[] dryftDoRozrzucenia = null;

		if (nummerPetli == PETLA_PIERWSZA) {
			dryftDoRozrzucenia = dryftPetlaPierwsza;
			gRefPoprawionePetlaPierwsza = returnValue;
		}

		else if (nummerPetli == PETLA_DRUGA) {
			dryftDoRozrzucenia = dryftPetlaDruga;
			gRefPoprawionePetlaDruga = returnValue;
		}

		int i = 0;
		for (Double value : dryftDoRozrzucenia) {
			returnValue[i] = value + gRef[i + nummerPetli];
			i++;
		}

		return returnValue;
	}

	protected Double[] obliczDeltaG(int numerPetli) {
		if (numerPetli == PETLA_PIERWSZA) {
			deltaGPetlaPierwsza[3] = 0.0;
			for (int i = 3; i > 0; i--) {
				deltaGPetlaPierwsza[i - 1] = gRefPoprawionePetlaPierwsza[i]
						- gRefPoprawionePetlaPierwsza[i - 1];
			}
			return deltaGPetlaPierwsza;
		}

		else if (numerPetli == PETLA_DRUGA) {
			deltaGPetlaDruga[3] = 0.0;
			for (int i = 3; i > 0; i--) {
				deltaGPetlaDruga[i - 1] = gRefPoprawionePetlaDruga[i]
						- gRefPoprawionePetlaDruga[i - 1];
			}
			return deltaGPetlaDruga;
		}
		return null;
	}

	protected double getDeltaG() {
		double returnValue = deltaGPetlaPierwsza[2] - deltaGPetlaPierwsza[0]
				+ deltaGPetlaDruga[0] - deltaGPetlaDruga[2];
		returnValue /= 4;

		return -returnValue;
	}

	public void oblicz() {
		obliczPoprawkiGradientu();
		obliczOdczytWlasciwy();
		obliczLuni();
		obliczGRef();
		obliczDryftPetla(PETLA_PIERWSZA);
		obliczDryftPetla(PETLA_DRUGA);
		obliczGRefPoprawione(PETLA_PIERWSZA);
		obliczGRefPoprawione(PETLA_DRUGA);
		obliczDeltaG(PETLA_PIERWSZA);
		obliczDeltaG(PETLA_DRUGA);
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
			zapis.println("TYP POPRAWKI: " + this.getTypPoprawki());
			zapis.println("WSPÓ£CZYNNIK SZTYWNOŒCI ZIEMI: "
					+ this.getWspolczynnikSztywnosciZiemi());
			zapis.println();

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
						DoubleWithScale(stanowisko.getBlad(), 4), 8));
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
				zapis.println();
				i++;
			}

			// ZAPISYWANIE DRYFTU
			zapis.println();
			zapis.println("LINIOWY MODEL DRYFTU DLA POMIARU O SCHEMACIE ABBAAB");
			zapis.println();
			zapis.println("DRYFT1 (MGL/GODZ) = "
					+ DoubleWithScale(obliczDryft(PETLA_PIERWSZA), 4));
			zapis.println("DRYFT2 (MGL/GODZ) = "
					+ DoubleWithScale(obliczDryft(PETLA_DRUGA), 4));
			zapis.println();

			// ZAPISYWANIE PÊTLI PIERWSZEJ
			zapis.println("PIERWSZA PETLA");
			zapis.print(StringToLeft("lp", 5));
			zapis.print(StringToLeft("g ref.", 12));
			zapis.print(StringToLeft("dryft", 10));
			zapis.print(StringToLeft("g ref. popra.", 15));
			zapis.print(StringToLeft("delta g", 15));
			zapis.println();

			for (int j = 0; j < 4; j++) {
				zapis.print(StringToLeft(Integer.toString(j + 1), 5));
				zapis.print(StringToLeft(DoubleWithScale(gRef[j], 4), 12));
				zapis.print(StringToLeft(
						DoubleWithScale(dryftPetlaPierwsza[j], 4), 10));
				zapis.print(StringToLeft(
						DoubleWithScale(gRefPoprawionePetlaPierwsza[j], 4), 15));
				zapis.print(StringToLeft(
						DoubleWithScale(deltaGPetlaPierwsza[j], 4), 15));
				zapis.println();
			}
			zapis.println();

			// ZAPISYWANIE PÊTLI DRUGIEJ
			zapis.println("DRUGA PETLA");
			zapis.print(StringToLeft("lp", 5));
			zapis.print(StringToLeft("g ref.", 12));
			zapis.print(StringToLeft("dryft", 10));
			zapis.print(StringToLeft("g ref. popra.", 15));
			zapis.print(StringToLeft("delta g", 15));
			zapis.println();

			for (int j = 0; j < 4; j++) {
				zapis.print(StringToLeft(Integer.toString(j + 1), 5));
				zapis.print(StringToLeft(
						DoubleWithScale(gRef[j + PETLA_DRUGA], 4), 12));
				zapis.print(StringToLeft(
						DoubleWithScale(dryftPetlaDruga[j], 4), 10));
				zapis.print(StringToLeft(
						DoubleWithScale(gRefPoprawionePetlaDruga[j], 4), 15));
				zapis.print(StringToLeft(
						DoubleWithScale(deltaGPetlaDruga[j], 4), 15));
				zapis.println();
			}
			zapis.println();

			// OSTATECZNY WYNIK
			zapis.println("OSTATECZNY WYNIK");
			zapis.println("dg = " + DoubleWithScale(getDeltaG(), 4));
			zapis.println("BLAD = ");

		} catch (FileNotFoundException e) {
			Comunicat.bladStrumieniaNieodnalezionoPliku();
		} finally {
			zapis.close();
		}

	}
}
