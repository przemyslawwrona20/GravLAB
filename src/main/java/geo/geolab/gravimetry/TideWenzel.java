package geo.geolab.gravimetry;

import static java.lang.Math.atan;
import static java.lang.Math.cos;
import static java.lang.Math.pow;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;
import geo.geolab.comunicat.RaportComunicat;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Calendar;
import java.util.Scanner;
import java.util.StringTokenizer;

public class TideWenzel extends Tide {
	private static final String TDT_UTC = "./wenzel_data_files/TDT_UTC.txt";
	private static final String LICZBY_LOVEA = "./wenzel_data_files/liczbyLovea.txt";
	private static final String WAVE_GROUPS = "./wenzel_data_files/WaveGroups.txt";
	private static final String WENZEL_DAT = "./wenzel_data_files/Wenzel_DAT.txt";

	/**
	 * tablica wsp. grawimetrycznych dla danej szerokoœci
	 */
	private double[] tablica_G = new double[12];

	/**
	 * tablica liczby Love'a "h" dla danej szerokoœci
	 */
	private double[] tablica_h = new double[12];

	/**
	 * tablica liczby Love'a "k" dla danej szerokoœci
	 */
	private double[] tablica_k = new double[12];

	/**
	 * tablica liczby Shidy "l" dla danej szerokoœci
	 */
	private double[] tablica_l = new double[12];

	/**
	 * tablica odchyleñ dla danej szerokoœci
	 */
	private double[] tablica_T = new double[12];

	/**
	 * tablica danych Ksiê¿yca, S³oñca, Jowisza i Wenus
	 */
	private double[] daneAstronomiczne = new double[8];

	/**
	 * tablica prêdkoœci do tablicy daneAstronomiczne[]
	 */
	private double[] tabPredkDanAstr = new double[8];

	/**
	 * tablica wspó³czynników geodezyjnych
	 */
	private double[] tablicaWspGeodez = new double[12]; //

	/**
	 * tablica faz liczona w stopniach
	 */
	private double[] tablicaFazWStopn = new double[12];

	/**
	 * tablica numerów fal
	 */
	private double[] tablicaNumFal = new double[1214];

	/**
	 * tablica amplitud
	 */
	private double[] tablicaAmplitud = new double[1214];

	/**
	 * tablica faz p³ywów w radianach // p³ywów
	 */
	private double[] tablicaFaz = new double[1214];

	/**
	 * tablica czêstotliwoœci w radianach na godzinê
	 */
	private double[] tablicaCzestotl = new double[1214];//

	/**
	 * tablica wspó³czynników amplitud
	 */
	private double[] tablicaPlywow = new double[1214]; //
	int liczbaFal;

	void zerujWszystkieTablice() {
		int i;

		for (i = 0; i < 12; i++) {
			tablica_G[i] = 0.0;
		}
		for (i = 0; i < 12; i++) {
			tablica_h[i] = 0.0;
		}
		for (i = 0; i < 12; i++) {
			tablica_k[i] = 0.0;
		}
		for (i = 0; i < 12; i++) {
			tablica_l[i] = 0.0;
		}
		for (i = 0; i < 12; i++) {
			tablica_T[i] = 0.0;
		}
		for (i = 0; i < 8; i++) {
			daneAstronomiczne[i] = 0.0;
		}
		for (i = 0; i < 8; i++) {
			tabPredkDanAstr[i] = 0.0;
		}
		for (i = 0; i < 12; i++) {
			tablicaWspGeodez[i] = 0.0;
		}
		for (i = 0; i < 12; i++) {
			tablicaFazWStopn[i] = 0.0;
		}
		for (i = 0; i < 1214; i++) {
			tablicaNumFal[i] = 0;
		}
		for (i = 0; i < 1214; i++) {
			tablicaAmplitud[i] = 0.0;
		}
		for (i = 0; i < 1214; i++) {
			tablicaFaz[i] = 0.0;
		}
		for (i = 0; i < 1214; i++) {
			tablicaCzestotl[i] = 0.0;
		}
		for (i = 0; i < 1214; i++) {
			tablicaPlywow[i] = 0.0;
		}
	}

	double dataJulianska(int rok, int miesiac, int dzien, int godzina,
			int minuta, int sekunda) {
		int[] tablicaDOY = { 0, 31, 59, 90, 120, 151, 181, 212, 243, 273, 304,
				334 };
		int iRok, iSkok, iLiczba, iLicznik, temp;
		double DataJulianska, iTemp;
		// --------------------------------------------------------------------------
		DataJulianska = 2415019.5; // dla roku 1900
		iRok = rok - 1900;
		iSkok = iRok / 4;
		iLiczba = iRok * 365 + iSkok;
		temp = iLiczba + tablicaDOY[miesiac - 1] + dzien;
		iTemp = (double) temp;
		DataJulianska = DataJulianska + iTemp
				+ (godzina + (minuta / 60.0) + (sekunda / 3600.0)) / 24.0;
		iLicznik = iSkok * 4;

		if (iRok != iLicznik) {
			return DataJulianska * 1.0;
		}

		if (miesiac > 2) {
			return DataJulianska * 1.0;
		}
		DataJulianska = DataJulianska - 1.0;
		// --------------------------------------------------------------------------
		return DataJulianska * 1.0;
	}

	int liczbaLiniWPliku(File file) {
		Scanner scanner = null;
		try {
			scanner = new Scanner(file);
			int i = 0;
			while (scanner.hasNextLine()) {
				scanner.nextLine();
				i++;
			}
			return i;
		} catch (FileNotFoundException e) {
			return 0;
		} finally {
			scanner.close();
		}
	}

	double czsUTC(double datajulianska) {
		class TDT_UTC {
			public double DataJulianska = 0.0;
			public double TDTminusUTC = 0.0;
		}

		int liczbaLiniWPliku = liczbaLiniWPliku(new File(TDT_UTC));
		TDT_UTC[] tablicaTDT_UTC = new TDT_UTC[liczbaLiniWPliku];

		int z, i, j, k;
		double wynik = 0, tempJD, iZwrot;
		double lowTempJD, hiTempJD, lowTempTDT_UTC, hiTempTDT_UTC;

		// --------------------------------------------------------------------------
		for (z = 0; z < liczbaLiniWPliku; z++) {
			tablicaTDT_UTC[z] = new TDT_UTC();
		}
		// --------------------------------------------------------------------------

		File file = new File(TDT_UTC);
		Scanner scanner = null;
		try {
			scanner = new Scanner(file);
			z = 0;
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				StringTokenizer tokens = new StringTokenizer(line);
				tablicaTDT_UTC[z].DataJulianska = Double.parseDouble(tokens
						.nextToken());
				tablicaTDT_UTC[z].TDTminusUTC = Double.parseDouble(tokens
						.nextToken());
				z++;
			}
			k = z - 1;

		} catch (FileNotFoundException e) {
			RaportComunicat.bladOdczytuPlikuTDT_UTC();
			return 0.0;
		} finally {
			scanner.close();
		}

		// --------------------------------------------------------------------------
		if (datajulianska < tablicaTDT_UTC[0].DataJulianska) {
			wynik = tablicaTDT_UTC[0].TDTminusUTC;
		}

		if ((datajulianska >= tablicaTDT_UTC[0].DataJulianska)
				&& (datajulianska < tablicaTDT_UTC[k].DataJulianska)) {
			i = 1;
			j = i + 1;
			tempJD = tablicaTDT_UTC[1].DataJulianska;
			do {
				lowTempJD = tablicaTDT_UTC[i].DataJulianska;
				lowTempTDT_UTC = tablicaTDT_UTC[i].TDTminusUTC;
				hiTempJD = tablicaTDT_UTC[j].DataJulianska;
				hiTempTDT_UTC = tablicaTDT_UTC[j].TDTminusUTC;
				i = i + 1;
				j = j + 1;
				tempJD = tablicaTDT_UTC[i].DataJulianska;
			} while (datajulianska > tempJD);
			wynik = (hiTempTDT_UTC * (datajulianska - lowTempJD) - lowTempTDT_UTC
					* (datajulianska - hiTempJD))
					/ (hiTempJD - lowTempJD);
		}
		if (datajulianska >= tablicaTDT_UTC[k].DataJulianska) {
			wynik = tablicaTDT_UTC[k].TDTminusUTC;
		}
		// --------------------------------------------------------------------------
		return wynik;
	}

	double liczbyLovea(double szerokoscStopnie, double szerokoscMinuty,
			double szerokoscSekundy, double wysokosc) {
		class LiczbyLovea {
			public double G0 = 0.0;
			public double GP = 0.0;
			public double GM = 0.0;
			public double h0 = 0.0;
			public double hP = 0.0;
			public double hM = 0.0;
			public double k0 = 0.0;
			public double kP = 0.0;
			public double kM = 0.0;
			public double l0 = 0.0;
			public double lP = 0.0;
			public double lM = 0.0;
			public double szerP = 0.0;
			public double szerM;
		}
		LiczbyLovea[] tablicaDanych = new LiczbyLovea[12];
		int i;
		double szerokoscWRadianach, przekrojN, szerokoscGeocentryczna, theta, z, iZwrot;
		// FILE *plik;
		// CString file,tempString;
		// const char* filePath;

		double Pi = 3.141592653589793;
		double a = 6378137.0;
		double e_2 = 0.00669438002290; // mimoœród elipsoidy obrotowej

		// --------------------------------------------------------------------------
		for (i = 0; i < 12; i++) {
			tablicaDanych[i] = new LiczbyLovea();
		}
		// --------------------------------------------------------------------------
		File file = new File(LICZBY_LOVEA);
		Scanner scanner = null;
		try {
			scanner = new Scanner(file);
			i = 0;
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				StringTokenizer tokens = new StringTokenizer(line);

				tablicaDanych[i].G0 = Double.parseDouble(tokens.nextToken());
				tablicaDanych[i].GP = Double.parseDouble(tokens.nextToken());
				tablicaDanych[i].GM = Double.parseDouble(tokens.nextToken());
				tablicaDanych[i].h0 = Double.parseDouble(tokens.nextToken());
				tablicaDanych[i].hP = Double.parseDouble(tokens.nextToken());
				tablicaDanych[i].hM = Double.parseDouble(tokens.nextToken());
				tablicaDanych[i].k0 = Double.parseDouble(tokens.nextToken());
				tablicaDanych[i].kP = Double.parseDouble(tokens.nextToken());
				tablicaDanych[i].kM = Double.parseDouble(tokens.nextToken());
				tablicaDanych[i].l0 = Double.parseDouble(tokens.nextToken());
				tablicaDanych[i].lP = Double.parseDouble(tokens.nextToken());
				tablicaDanych[i].lM = Double.parseDouble(tokens.nextToken());
				tablicaDanych[i].szerP = Double.parseDouble(tokens.nextToken());
				tablicaDanych[i].szerM = Double.parseDouble(tokens.nextToken());

				i++;
			}
			i--;
		} catch (FileNotFoundException e) {
			return 0.0;

		} finally {
			scanner.close();
		}

		// --------------------------------------------------------------------------
		szerokoscWRadianach = (szerokoscStopnie / (180.0 / Pi))
				+ (szerokoscMinuty / (180.0 * 60.0 / Pi))
				+ (szerokoscSekundy / (180.0 * 3600.0 / Pi));
		przekrojN = a / sqrt(1.0 - e_2 * pow(sin(szerokoscWRadianach), 2));
		szerokoscGeocentryczna = (180.0 / Pi)
				* atan(((przekrojN * (1.0 - e_2) + wysokosc) * sin(szerokoscWRadianach))
						/ ((przekrojN + wysokosc) * cos(szerokoscWRadianach)));
		theta = 90.0 - szerokoscGeocentryczna;
		z = cos(theta / (180.0 / Pi));
		// --------------------------------------------------------------------------
		tablicaDanych[0].szerP = 0.335410
				* (35.0 * pow(z, 4) - 30.0 * pow(z, 2) + 3.0)
				/ (3.0 * pow(z, 2) - 1.0);
		tablicaDanych[1].szerP = 0.612372 * (7.0 * pow(z, 2) - 3.0);
		tablicaDanych[2].szerP = 0.866025 * (7.0 * pow(z, 2) - 1.0);
		tablicaDanych[6].szerP = 0.829156 * (9.0 * pow(z, 2) - 1.0);
		tablicaDanych[11].szerP = 0.806226 * (11.0 * pow(z, 2) - 1.0);
		tablicaDanych[0].szerM = 0.894427 / (3.0 * pow(z, 2) - 1.0);
		// --------------------------------------------------------------------------
		for (i = 0; i < 12; i++) {
			tablica_G[i] = tablicaDanych[i].G0 + tablicaDanych[i].GP
					* tablicaDanych[i].szerP + tablicaDanych[i].GM
					* tablicaDanych[i].szerM;
			tablica_h[i] = tablicaDanych[i].h0 + tablicaDanych[i].hP
					* tablicaDanych[i].szerP + tablicaDanych[i].hM
					* tablicaDanych[i].szerM;
			tablica_k[i] = tablicaDanych[i].k0 + tablicaDanych[i].kP
					* tablicaDanych[i].szerP + tablicaDanych[i].kM
					* tablicaDanych[i].szerM;
			tablica_l[i] = tablicaDanych[i].l0 + tablicaDanych[i].lP
					* tablicaDanych[i].szerP + tablicaDanych[i].lM
					* tablicaDanych[i].szerM;
			tablica_T[i] = 1.0 + tablicaDanych[i].k0 - tablicaDanych[i].h0
					+ tablicaDanych[i].szerP
					* (tablicaDanych[i].kP - tablicaDanych[i].hP)
					+ tablicaDanych[i].szerM
					* (tablicaDanych[i].kM - tablicaDanych[i].hM);
		}
		// --------------------------------------------------------------------------
		return 0;
	}

	double obliczeniaAstronomiczne(int model, double dlugoscStopnie,
			double dlugoscMinuty, double dlugoscSekundy, int rok, int miesiac,
			int dzien, int godzina, int minuta, int sekunda) {
		int i;
		double datajulianska, jdUT1900, jdUT2000, TDTminusUTC, jdTDT1990, jdTDT2000;
		double DAL, DALP, DS, DSP, DH, DHP, DDS, DDSP, DDH, DDHP;

		double Pi = 3.141592653589793;
		// --------------------------------------------------------------------------
		datajulianska = dataJulianska(rok, miesiac, dzien, godzina, minuta,
				sekunda);
		jdUT1900 = (datajulianska - 2415020.0) / 36525.0;
		jdUT2000 = (datajulianska - 2451545.0) / 36525.0;
		TDTminusUTC = czsUTC(datajulianska);
		jdTDT1990 = jdUT1900 + TDTminusUTC / 3155760000.0;
		jdTDT2000 = jdUT2000 + TDTminusUTC / 3155760000.0;
		// --------------------------------------------------------------------------
		if (model == 2) {
			DAL = 280.4606184 + 36000.7700536 * jdUT2000 + 0.00038793
					* pow(jdUT2000, 2) - 0.0000000258 * pow(jdUT2000, 3);
			DALP = (36000.7700536 + 2.0 * 0.00038793 * jdUT2000 - 3.0 * 0.0000000258 * pow(
					jdUT2000, 2)) / (24.0 * 36525.0);
			DS = 218.316656 + 481267.881342 * jdTDT2000 - 0.001330
					* pow(jdTDT2000, 2);
			DSP = (481267.881342 - 2.0 * 0.001330 * jdTDT2000)
					/ (24.0 * 36525.0);
			DH = 280.466449 + 36000.769822 * jdTDT2000 + 0.0003036
					* pow(jdTDT2000, 2);
			DHP = (36000.769822 + 2.0 * 0.0003036 * jdTDT2000)
					/ (24.0 * 36525.0);
			DDS = 0.0040 * cos((29.0 + 133.0 * jdTDT2000) * (Pi / 180.0));
			DDSP = (-0.0040 * 133.0 * (Pi / 180.0) * sin((29.0 + 133.0 * jdTDT2000)
					* (Pi / 180.0)))
					/ (24.0 * 36525.0);
			DDH = 0.0018 * cos((159.0 + 19.0 * jdTDT2000) * (Pi / 180.0));
			DDHP = (-0.0018 * 19.0 * (Pi / 180.0) * sin((159.0 + 19.0 * jdTDT2000)
					* (Pi / 180.0)))
					/ (24.0 * 36525.0);
			// ----------------------------------------------------------------------
			daneAstronomiczne[0] = DAL
					- DS
					+ (dlugoscStopnie + dlugoscMinuty / 60.0 + dlugoscSekundy / 3600.0)
					+ (godzina + minuta / 60.0 + sekunda / 3600.0) * 15.0;
			daneAstronomiczne[1] = DS + DDS;
			daneAstronomiczne[2] = DH + DDH;
			daneAstronomiczne[3] = 83.353243 + 4069.013711 * jdTDT2000
					- 0.010324 * pow(jdTDT2000, 2);
			daneAstronomiczne[4] = 234.955444 + 1934.136185 * jdTDT2000
					- 0.002076 * pow(jdTDT2000, 2);
			daneAstronomiczne[5] = 282.937348 + 1.719533 * jdTDT2000
					+ 0.0004597 * pow(jdTDT2000, 2);
			daneAstronomiczne[6] = 248.1 + 32964.47 * jdTDT2000;
			daneAstronomiczne[7] = 81.5 + 22518.44 * jdTDT2000;
			// ----------------------------------------------------------------------
			tabPredkDanAstr[0] = DALP - DSP + 15.0;
			tabPredkDanAstr[1] = DSP + DDSP;
			tabPredkDanAstr[2] = DHP + DDHP;
			tabPredkDanAstr[3] = (4069.013711 - 2.0 * 0.010324 * jdTDT2000)
					/ (24.0 * 36525.0);
			tabPredkDanAstr[4] = (1934.136185 - 2.0 * 0.002076 * jdTDT2000)
					/ (24.0 * 36525.0);
			tabPredkDanAstr[5] = (1.719533 + 2.0 * 0.0004597 * jdTDT2000)
					/ (24.0 * 36525.0);
			tabPredkDanAstr[6] = 32964.47 / (24.0 * 36525.0);
			tabPredkDanAstr[7] = 22518.44 / (24.0 * 36525.0);
			// ----------------------------------------------------------------------
			for (i = 0; i < 8; i++) {
				daneAstronomiczne[i] = fmod(daneAstronomiczne[i], 360.0);
				if (daneAstronomiczne[i] < 0.0) {
					daneAstronomiczne[i] = daneAstronomiczne[i] + 360.0;
				}
			}
		} else {
			daneAstronomiczne[1] = 270.434164 + 481267.8831417 * jdTDT1990
					- 0.0011333 * pow(jdTDT1990, 2) + 0.0000019
					* pow(jdTDT1990, 3);
			daneAstronomiczne[2] = 279.696678 + 36000.768925 * jdTDT1990
					+ 0.0003025 * pow(jdTDT1990, 2);
			daneAstronomiczne[3] = 334.329556 + 4069.0340333 * jdTDT1990
					- 0.010325 * pow(jdTDT1990, 2) - 0.0000125
					* pow(jdTDT1990, 3);
			daneAstronomiczne[4] = 100.816725 + 1934.1420083 * jdTDT1990
					- 0.0020778 * pow(jdTDT1990, 2) - 0.0000022
					* pow(jdTDT1990, 3);
			daneAstronomiczne[5] = 281.220833 + 1.719175 * jdTDT1990
					+ 0.0004528 * pow(jdTDT1990, 2) + 0.0000033
					* pow(jdTDT1990, 3);
			daneAstronomiczne[6] = 248.1 + 32964.47 * jdTDT2000;
			daneAstronomiczne[7] = 81.5 + 22518.44 * jdTDT2000;
			// ----------------------------------------------------------------------
			tabPredkDanAstr[1] = 0.54901652195037 - 2.58575 * pow(10, -9)
					* jdTDT1990 + 6.46 * pow(10, -12) * pow(jdTDT1990, 2);
			tabPredkDanAstr[2] = 0.04106863897444 + 6.902 * pow(10, -10)
					* jdTDT1990;
			tabPredkDanAstr[3] = 0.00464183667960 - 2.355692 * pow(10, -8)
					* jdTDT1990 - 4.278 * pow(10, -11) * pow(jdTDT1990, 2);
			tabPredkDanAstr[4] = 0.00220641342494 - 4.74054 * pow(10, -9)
					* jdTDT1990 - 7.60 * pow(10, -12) * pow(jdTDT1990, 2);
			tabPredkDanAstr[5] = 0.00000196118526 + 1.03303 * pow(10, -9)
					* jdTDT1990 + 1.141 * pow(10, -11) * pow(jdTDT1990, 2);
			tabPredkDanAstr[6] = 32964.47 / (24.0 * 36525.0);
			tabPredkDanAstr[7] = 22518.44 / (24.0 * 36525.0);
			tabPredkDanAstr[0] = tabPredkDanAstr[2] - tabPredkDanAstr[1] + 15.0;
			// ----------------------------------------------------------------------
			for (i = 1; i < 8; i++) {
				daneAstronomiczne[i] = fmod(daneAstronomiczne[i], 360.0);
				if (daneAstronomiczne[i] < 0.0) {
					daneAstronomiczne[i] = daneAstronomiczne[i] + 360.0;
				}
			}
			daneAstronomiczne[0] = daneAstronomiczne[2]
					- daneAstronomiczne[1]
					+ (dlugoscStopnie + dlugoscMinuty / 60.0 + dlugoscSekundy / 3600.0)
					+ (godzina + minuta / 60.0 + sekunda / 3600.0) * 15.0;
			daneAstronomiczne[0] = fmod(daneAstronomiczne[0], 360.0);
			if (daneAstronomiczne[0] < 0.0) {
				daneAstronomiczne[0] = daneAstronomiczne[0] + 360.0;
			}
		}
		// --------------------------------------------------------------------------
		return 0;
	}

	double wspolczynnikiPlywowe(double szerokoscStopnie,
			double szerokoscMinuty, double szerokoscSekundy,
			double dlugoscStopnie, double dlugoscMinuty, double dlugoscSekundy,
			double wysokosc) {
		int i;
		double GMZiemi, szerDegDzies, dlugDegDzies, roDeg, przekrojN, szerokoscGeocentryczna;
		double promienGeocentryczny, DPAR, DMAS, R0, stalaDoodsona, DF, DUMMY, przyspieszenie;
		double[] tablicaSkladowej_X = new double[12], tablicaSkladowej_Z = new double[12];

		double Pi = 3.141592653589793;
		double e_2 = 0.00669438002290; // mimoœród elipsoidy obrotowej
		double a = 6378137.0;
		double GMZiemi1 = 398600.5;

		roDeg = (180.0 / Pi);
		szerDegDzies = szerokoscStopnie + (szerokoscMinuty / 60.0)
				+ (szerokoscSekundy / 3600.0);
		dlugDegDzies = dlugoscStopnie + (dlugoscMinuty / 60.0)
				+ (dlugoscSekundy / 3600.0);
		GMZiemi = GMZiemi1 * pow(10, 9);
		// przyspieszenie normalne
		przyspieszenie = 9.78032677
				* (1.0 + 0.001931851353 * pow(sin(szerDegDzies / roDeg), 2))
				/ sqrt(1.0 - e_2 * pow(sin(szerDegDzies / roDeg), 2)) - 0.3086
				* pow(10, -5) * wysokosc;
		// przekrój normalny w pierwszym wertykale (N)
		przekrojN = a / sqrt(1.0 - e_2 * pow(sin(szerDegDzies / roDeg), 2));
		// szerokosc geocentryczna
		szerokoscGeocentryczna = roDeg
				* atan(((przekrojN * (1.0 - e_2) + wysokosc) * sin(szerDegDzies
						/ roDeg))
						/ ((przekrojN + wysokosc) * cos(szerDegDzies / roDeg)));
		// promieñ geocentryczny
		promienGeocentryczny = sqrt(pow((przekrojN + wysokosc), 2)
				* pow(cos(szerDegDzies / roDeg), 2)
				+ pow((przekrojN * (1.0 - e_2) + wysokosc), 2)
				* pow(sin(szerDegDzies / roDeg), 2));
		// astronomiczne parametry I.A.U 1984
		DPAR = 3422.448;
		DMAS = 1.0 / 0.01230002;
		// sta³a Doodson'a w m^2/s^2
		R0 = a
				* (1.0 - e_2 / 6.0 - 5.0 * pow(e_2, 2) / 72.0 - pow(e_2, 3) * 55.0 / 1296.0);
		stalaDoodsona = pow(R0, 2) * 0.75 * GMZiemi / (pow(a, 3) * DMAS);
		stalaDoodsona = stalaDoodsona * pow((DPAR * (1.0 / roDeg) / 3600.0), 3);
		DF = roDeg * 3.600 * pow(10, -3) / przyspieszenie;
		// obliczenie wspó³czynników liczb Love'a i Shidy
		liczbyLovea(szerokoscStopnie, szerokoscMinuty, szerokoscSekundy,
				wysokosc);
		// obliczenie wspó³czynników geodezyjnych dla potencja³u p³ywowego
		tablicaWspGeodez[0] = stalaDoodsona
				* pow((promienGeocentryczny / R0), 2) * 0.5
				* (1.0 - 3.0 * pow(sin(szerokoscGeocentryczna / roDeg), 2));
		tablicaWspGeodez[1] = stalaDoodsona
				* pow((promienGeocentryczny / R0), 2) * 2.0
				* sin(szerokoscGeocentryczna / roDeg)
				* cos(szerokoscGeocentryczna / roDeg);
		tablicaWspGeodez[2] = stalaDoodsona
				* pow((promienGeocentryczny / R0), 2)
				* pow(cos(szerokoscGeocentryczna / roDeg), 2);
		tablicaWspGeodez[3] = stalaDoodsona
				* pow((promienGeocentryczny / R0), 3) * 1.118033989
				* sin(szerokoscGeocentryczna / roDeg)
				* (3.0 - 5.0 * pow(sin(szerokoscGeocentryczna / roDeg), 2));
		tablicaWspGeodez[4] = stalaDoodsona
				* pow((promienGeocentryczny / R0), 3) * 0.726184378
				* cos(szerokoscGeocentryczna / roDeg)
				* (1.0 - 5.0 * pow(sin(szerokoscGeocentryczna / roDeg), 2));
		tablicaWspGeodez[5] = stalaDoodsona
				* pow((promienGeocentryczny / R0), 3) * 2.598076212
				* sin(szerokoscGeocentryczna / roDeg)
				* pow(cos(szerokoscGeocentryczna / roDeg), 2);
		tablicaWspGeodez[6] = stalaDoodsona
				* pow((promienGeocentryczny / R0), 3)
				* pow(cos(szerokoscGeocentryczna / roDeg), 3);
		tablicaWspGeodez[7] = stalaDoodsona
				* pow((promienGeocentryczny / R0), 4)
				* 0.125000000
				* (3.0 - 30.0 * pow(sin(szerokoscGeocentryczna / roDeg), 2) + 35.0 * pow(
						sin(szerokoscGeocentryczna / roDeg), 4));
		tablicaWspGeodez[8] = stalaDoodsona
				* pow((promienGeocentryczny / R0), 4) * 0.473473091 * 2.0
				* sin(szerokoscGeocentryczna / roDeg)
				* cos(szerokoscGeocentryczna / roDeg)
				* (3.0 - 7.0 * pow(sin(szerokoscGeocentryczna / roDeg), 2));
		tablicaWspGeodez[9] = stalaDoodsona
				* pow((promienGeocentryczny / R0), 4) * 0.777777778
				* pow(cos(szerokoscGeocentryczna / roDeg), 2)
				* (1.0 - 7.0 * pow(sin(szerokoscGeocentryczna / roDeg), 2));
		tablicaWspGeodez[10] = stalaDoodsona
				* pow((promienGeocentryczny / R0), 4) * 3.079201436
				* sin(szerokoscGeocentryczna / roDeg)
				* pow(cos(szerokoscGeocentryczna / roDeg), 3);
		tablicaWspGeodez[11] = stalaDoodsona
				* pow((promienGeocentryczny / R0), 4)
				* pow(cos(szerokoscGeocentryczna / roDeg), 4);
		for (i = 0; i < 12; i++) {
			tablicaFazWStopn[i] = 0.0;
		}
		/*
		 * obliczennie sk³adowych X i Z p³ywowego wektora przyspieszenia
		 * zorientowanego w uk³adzie sferycznym
		 */
		tablicaSkladowej_X[0] = -(stalaDoodsona / R0)
				* (promienGeocentryczny / R0) * pow(10, 9) * 3.0
				* sin(szerokoscGeocentryczna / roDeg)
				* cos(szerokoscGeocentryczna / roDeg);
		tablicaSkladowej_X[1] = (stalaDoodsona / R0)
				* (promienGeocentryczny / R0)
				* pow(10, 9)
				* 2.0
				* (pow(cos(szerokoscGeocentryczna / roDeg), 2) - pow(
						sin(szerokoscGeocentryczna / roDeg), 2));
		tablicaSkladowej_X[2] = -(stalaDoodsona / R0)
				* (promienGeocentryczny / R0) * pow(10, 9) * 2.0
				* sin(szerokoscGeocentryczna / roDeg)
				* cos(szerokoscGeocentryczna / roDeg);
		tablicaSkladowej_X[3] = (stalaDoodsona / R0)
				* pow((promienGeocentryczny / R0), 2) * pow(10, 9)
				* 1.118033989 * cos(szerokoscGeocentryczna / roDeg)
				* (3.0 - 15.0 * pow(sin(szerokoscGeocentryczna / roDeg), 2));
		tablicaSkladowej_X[4] = (stalaDoodsona / R0)
				* pow((promienGeocentryczny / R0), 2) * pow(10, 9)
				* 0.726184378 * sin(szerokoscGeocentryczna / roDeg)
				* (4.0 - 15.0 * pow(cos(szerokoscGeocentryczna / roDeg), 2));
		tablicaSkladowej_X[5] = (stalaDoodsona / R0)
				* pow((promienGeocentryczny / R0), 2) * pow(10, 9)
				* 2.598076212 * cos(szerokoscGeocentryczna / roDeg)
				* (1.0 - 3.0 * pow(sin(szerokoscGeocentryczna / roDeg), 2));
		tablicaSkladowej_X[6] = -(stalaDoodsona / R0)
				* pow((promienGeocentryczny / R0), 2) * pow(10, 9) * 3.0
				* sin(szerokoscGeocentryczna / roDeg)
				* pow(cos(szerokoscGeocentryczna / roDeg), 2);
		tablicaSkladowej_X[7] = -(stalaDoodsona / R0)
				* pow((promienGeocentryczny / R0), 3) * pow(10, 9)
				* 0.125000000 * sin(szerokoscGeocentryczna / roDeg)
				* cos(szerokoscGeocentryczna / roDeg)
				* (60.0 - 140.0 * pow(sin(szerokoscGeocentryczna / roDeg), 2));
		tablicaSkladowej_X[8] = (stalaDoodsona / R0)
				* pow((promienGeocentryczny / R0), 3)
				* pow(10, 9)
				* 0.473473091
				* (6.0 - 54.0 * pow(sin(szerokoscGeocentryczna / roDeg), 2) + 56.0 * pow(
						sin(szerokoscGeocentryczna / roDeg), 4));
		tablicaSkladowej_X[9] = -(stalaDoodsona / R0)
				* pow((promienGeocentryczny / R0), 3)
				* pow(10, 9)
				* 0.777777778
				* cos(szerokoscGeocentryczna / roDeg)
				* (16.0 * sin(szerokoscGeocentryczna / roDeg) - 28.0 * pow(
						sin(szerokoscGeocentryczna / roDeg), 3));
		tablicaSkladowej_X[10] = (stalaDoodsona / R0)
				* pow((promienGeocentryczny / R0), 3) * pow(10, 9)
				* 3.079201436 * pow(cos(szerokoscGeocentryczna / roDeg), 2)
				* (4.0 * pow(cos(szerokoscGeocentryczna / roDeg), 2) - 3.0);
		tablicaSkladowej_X[11] = -(stalaDoodsona / R0)
				* pow((promienGeocentryczny / R0), 3) * pow(10, 9) * 4.0
				* pow(cos(szerokoscGeocentryczna / roDeg), 3)
				* sin(szerokoscGeocentryczna / roDeg);
		// --------------------------------------------------------------------------------
		tablicaSkladowej_Z[0] = (stalaDoodsona / R0)
				* (promienGeocentryczny / R0) * pow(10, 9)
				* (1.0 - 3.0 * pow(sin(szerokoscGeocentryczna / roDeg), 2));
		tablicaSkladowej_Z[1] = 4.0 * (stalaDoodsona / R0)
				* (promienGeocentryczny / R0) * pow(10, 9)
				* sin(szerokoscGeocentryczna / roDeg)
				* cos(szerokoscGeocentryczna / roDeg);
		tablicaSkladowej_Z[2] = 2.0 * (stalaDoodsona / R0)
				* (promienGeocentryczny / R0) * pow(10, 9)
				* pow(cos(szerokoscGeocentryczna / roDeg), 2);
		tablicaSkladowej_Z[3] = 3.0 * (stalaDoodsona / R0)
				* pow((promienGeocentryczny / R0), 2) * pow(10, 9)
				* 1.118033989 * sin(szerokoscGeocentryczna / roDeg)
				* (3.0 - 5.0 * pow(sin(szerokoscGeocentryczna / roDeg), 2));
		tablicaSkladowej_Z[4] = 3.0 * (stalaDoodsona / R0)
				* pow((promienGeocentryczny / R0), 2) * pow(10, 9)
				* 0.726184378 * cos(szerokoscGeocentryczna / roDeg)
				* (1.0 - 5.0 * pow(sin(szerokoscGeocentryczna / roDeg), 2));
		tablicaSkladowej_Z[5] = 3.0 * (stalaDoodsona / R0)
				* pow((promienGeocentryczny / R0), 2) * pow(10, 9)
				* 2.598076212 * sin(szerokoscGeocentryczna / roDeg)
				* pow(cos(szerokoscGeocentryczna / roDeg), 2);
		tablicaSkladowej_Z[6] = 3.0 * (stalaDoodsona / R0)
				* pow((promienGeocentryczny / R0), 2) * pow(10, 9)
				* pow(cos(szerokoscGeocentryczna / roDeg), 3);
		tablicaSkladowej_Z[7] = 4.0
				* (stalaDoodsona / R0)
				* pow((promienGeocentryczny / R0), 3)
				* pow(10, 9)
				* 0.125000000
				* (3.0 - 30.0 * pow(sin(szerokoscGeocentryczna / roDeg), 2) + 35.0 * pow(
						sin(szerokoscGeocentryczna / roDeg), 4));
		tablicaSkladowej_Z[8] = 8.0 * (stalaDoodsona / R0)
				* pow((promienGeocentryczny / R0), 3) * pow(10, 9)
				* 0.473473091 * sin(szerokoscGeocentryczna / roDeg)
				* cos(szerokoscGeocentryczna / roDeg)
				* (3.0 - 7.0 * pow(sin(szerokoscGeocentryczna / roDeg), 2));
		tablicaSkladowej_Z[9] = 4.0 * (stalaDoodsona / R0)
				* pow((promienGeocentryczny / R0), 3) * pow(10, 9)
				* 0.777777778 * pow(cos(szerokoscGeocentryczna / roDeg), 2)
				* (1.0 - 7.0 * pow(sin(szerokoscGeocentryczna / roDeg), 2));
		tablicaSkladowej_Z[10] = 4.0 * (stalaDoodsona / R0)
				* pow((promienGeocentryczny / R0), 3) * pow(10, 9)
				* 3.079201436 * sin(szerokoscGeocentryczna / roDeg)
				* pow(cos(szerokoscGeocentryczna / roDeg), 3);
		tablicaSkladowej_Z[11] = 4.0 * (stalaDoodsona / R0)
				* pow((promienGeocentryczny / R0), 3) * pow(10, 9)
				* pow(cos(szerokoscGeocentryczna / roDeg), 4);
		// -------------------------------------------------------------------------------
		/*
		 * obliczennie sk³adowych X i Z p³ywowego wektora przyspieszenia
		 * zorientowanego w uk³adzie elipsoidalnym
		 */
		for (i = 0; i < 12; i++) {
			DUMMY = (cos(szerDegDzies / roDeg)
					* cos(szerokoscGeocentryczna / roDeg) + sin(szerDegDzies
					/ roDeg)
					* sin(szerokoscGeocentryczna / roDeg))
					* tablicaSkladowej_X[i]
					- (sin(szerDegDzies / roDeg)
							* cos(szerokoscGeocentryczna / roDeg) - cos(szerDegDzies
							/ roDeg)
							* sin(szerokoscGeocentryczna / roDeg))
					* tablicaSkladowej_Z[i];
			tablicaSkladowej_Z[i] = (sin(szerDegDzies / roDeg)
					* cos(szerokoscGeocentryczna / roDeg) - cos(szerDegDzies
					/ roDeg)
					* sin(szerokoscGeocentryczna / roDeg))
					* tablicaSkladowej_X[i]
					+ (cos(szerDegDzies / roDeg)
							* cos(szerokoscGeocentryczna / roDeg) + sin(szerDegDzies
							/ roDeg)
							* sin(szerokoscGeocentryczna / roDeg))
					* tablicaSkladowej_Z[i];
			tablicaSkladowej_X[i] = DUMMY;
		}
		// Obliczenie sk³adowej pionowej (p³yw)
		for (i = 0; i < 12; i++) {
			tablicaWspGeodez[i] = tablicaSkladowej_Z[i];
			tablicaFazWStopn[i] = 180.0;
		}
		/*
		 * doprowadzenie ujemnych wspó³czynników do waroœci bezwzglednej przy
		 * równoczesnym dodaniu do fazy 180 stopni
		 */
		for (i = 0; i < 12; i++) {
			if (tablicaWspGeodez[i] < 0.0) {
				tablicaWspGeodez[i] = -1.0 * tablicaWspGeodez[i];
				tablicaFazWStopn[i] = tablicaFazWStopn[i] + 180.0;
			}
		}
		// ------------------------------------------------------------------------------
		return 0;
	}

	double funkcjaPlywowa(int model, double szerokoscStopnie,
			double szerokoscMinuty, double szerokoscSekundy,
			double dlugoscStopnie, double dlugoscMinuty, double dlugoscSekundy,
			double wysokosc, int rok, int miesiac, int dzien, int godzina,
			int minuta, int sekunda) {
		class PlikDanych {
			public int NRI = 0;
			public int K = 0;
			public int NS1 = 0;
			public int NS2 = 0;
			public int NS3 = 0;
			public int NS4 = 0;
			public int NS5 = 0;
			public int NS6 = 0;
			public int NS7 = 0;
			public int NS8 = 0;
			public int NP = 0;
			public double DHH1 = 0.0;
			public double DHH2 = 0.0;
			public double DHH3 = 0.0;
			// public char CNW[4];
			public String CNW = "";
			public double DHD = 0.0;
			public double DHT = 0.0;
			public double DHTD = 0.0;
			public double DHB = 0.0;
		}

		PlikDanych wierszPlik = new PlikDanych();
		int i, iW, j, m, n, iJ;
		int[] tablica_NS = new int[8];
		double DELTAR, dataJul, DT2000, DSHD, DSHT, DC1, DC2, DC3;
		double[] tablica_DX = new double[3], tablica_DSH = new double[3], tablica_DHH = new double[4], tablica_Delta = new double[12];

		// wspo³czynnik rezonansu dla wsp. grawimetrycznych
		double resFactorGrav = -0.000625;

		// czêstotliwoœæ fali O1 w stopniach na godzinê
		double fecO1 = 13.943036;

		// czêstotliwoœæ drgania FCN w stopniach na godzinê
		double fecFCN = 15.073729;

		double Pi = 3.141592653589793;
		// ------------------------------------------------------------------------------

		DSHD = 0.0;
		DSHT = 0.0;
		wspolczynnikiPlywowe(szerokoscStopnie, szerokoscMinuty,
				szerokoscSekundy, dlugoscStopnie, dlugoscMinuty,
				dlugoscSekundy, wysokosc);
		for (i = 0; i < 12; i++) {
			tablica_Delta[i] = 1.0;
		}
		DELTAR = 0.0;
		for (i = 0; i < 12; i++) {
			tablica_Delta[i] = tablica_G[i];
		}
		DELTAR = resFactorGrav;
		dataJul = dataJulianska(rok, miesiac, dzien, godzina, minuta, sekunda);
		DT2000 = (dataJul - 2451544.0) / 36525.0;
		dataJul = (dataJul - 2415020.0) / 36525.0;
		obliczeniaAstronomiczne(2, dlugoscStopnie, dlugoscMinuty,
				dlugoscSekundy, rok, miesiac, dzien, godzina, minuta, sekunda);

		// wspolczynniki interpolacyjne do modelu CARTWRIGHT-TAYLER-EDDEN (CTE)
		tablica_DX[0] = 0.550376 - 1.173312 * dataJul;
		tablica_DX[1] = 0.306592 + 0.144564 * dataJul;
		tablica_DX[2] = 0.143033 + 1.028749 * dataJul;

		// wspolczynniki DSH
		for (i = 0; i < 3; i++) {
			tablica_DSH[i] = 0.0;
		}
		iW = 0;
		// ===========================================

		File file = new File(WENZEL_DAT);
		Scanner scanner = null;

		try {
			scanner = new Scanner(file);
			while (scanner.hasNext()) {
				String line = scanner.nextLine();

				int index;
				String[] arrayStr = new String[19];

				StringTokenizer tokens = new StringTokenizer(line);
				for (index = 0; index < 19; index++) {
					arrayStr[index] = tokens.nextToken().trim();
				}

				wierszPlik.NRI = Integer.parseInt(arrayStr[0]);
				wierszPlik.K = Integer.parseInt(arrayStr[1]);
				wierszPlik.NS1 = Integer.parseInt(arrayStr[2]);
				wierszPlik.NS2 = Integer.parseInt(arrayStr[3]);
				wierszPlik.NS3 = Integer.parseInt(arrayStr[4]);
				wierszPlik.NS4 = Integer.parseInt(arrayStr[5]);
				wierszPlik.NS5 = Integer.parseInt(arrayStr[6]);
				wierszPlik.NS6 = Integer.parseInt(arrayStr[7]);
				wierszPlik.NS7 = Integer.parseInt(arrayStr[8]);
				wierszPlik.NS8 = Integer.parseInt(arrayStr[9]);
				wierszPlik.NP = Integer.parseInt(arrayStr[10]);

				wierszPlik.DHH1 = Double.parseDouble(arrayStr[11]);
				wierszPlik.DHH2 = Double.parseDouble(arrayStr[12]);
				wierszPlik.DHH3 = Double.parseDouble(arrayStr[13]);

				wierszPlik.CNW = arrayStr[14];

				wierszPlik.DHD = Double.parseDouble(arrayStr[15]);
				wierszPlik.DHT = Double.parseDouble(arrayStr[16]);
				wierszPlik.DHTD = Double.parseDouble(arrayStr[17]);
				wierszPlik.DHB = Double.parseDouble(arrayStr[18]);

				tablica_NS[0] = wierszPlik.NS1;
				tablica_NS[1] = wierszPlik.NS2;
				tablica_NS[2] = wierszPlik.NS3;
				tablica_NS[3] = wierszPlik.NS4;
				tablica_NS[4] = wierszPlik.NS5;
				tablica_NS[5] = wierszPlik.NS6;
				tablica_NS[6] = wierszPlik.NS7;
				tablica_NS[7] = wierszPlik.NS8;
				// --------------------------------------------------------------------
				tablica_DHH[0] = wierszPlik.DHH1;
				tablica_DHH[1] = wierszPlik.DHH2;
				tablica_DHH[2] = wierszPlik.DHH3;
				// --------------------------------------------------------------------
				DSHT = DSHT + wierszPlik.DHT;
				tablica_DHH[3] = 0.0;

				for (m = 0; m < 3; m++) {
					tablica_DSH[m] = tablica_DSH[m] + tablica_DHH[m];
				}
				DSHD = DSHD + wierszPlik.DHD;
				// --------------------------------------------------------------------

				// jeœli zosta³ wybrany model CARTWRIGHT-TAYLER-EDDEN (CTE)
				if (model == 1) {
					for (n = 0; n < 3; n++) {
						tablica_DHH[3] = tablica_DHH[3] + tablica_DHH[n]
								* tablica_DX[n];
					}
				}
				// jeœli zosta³ wybrany model Doodson'a
				if (model == 0) {
					tablica_DHH[3] = wierszPlik.DHD;
				}
				// jeœli zosta³ wybrany model Tamury
				if (model == 2) {
					tablica_DHH[3] = wierszPlik.DHT + wierszPlik.DHTD * DT2000;
				}
				// jeœli zosta³ wybrany model Buellesfeld'a
				if (model == 3) {
					tablica_DHH[3] = wierszPlik.DHB;
				}

				// --------------------------------------------------------------------
				if (fabs(tablica_DHH[3]) >= pow(10, -6)) {
					DC1 = 0.0;
					DC2 = 0.0;
					DC3 = 0.0;
					for (j = 0; j < 8; j++) {
						DC2 = DC2 + tablica_NS[j] * daneAstronomiczne[j];
						DC3 = DC3 + tablica_NS[j] * tabPredkDanAstr[j];
					}

					j = (wierszPlik.K + 1) * wierszPlik.K / 2 - 2
							+ tablica_NS[0];
					iJ = j - 1;
					DC2 = DC2 + tablicaFazWStopn[iJ] + wierszPlik.NP * 90.0;
					DC1 = tablica_DHH[3] * tablicaWspGeodez[iJ];
					tablicaPlywow[iW] = tablica_Delta[iJ];

					if (iJ == 1) {
						tablicaPlywow[iW] = tablica_Delta[iJ] + DELTAR
								* (DC3 - fecO1) / (fecFCN - DC3);
					}
					if (DC1 < 0.0) {
						DC1 = -DC1;
						DC2 = DC2 - 180.0;
					}

					DC2 = fmod(DC2, 360.0);
					while (DC2 < 0.0) {
						DC2 = DC2 + 360.0;
						DC2 = fmod(DC2, 360.0);
					}

					tablicaAmplitud[iW] = DC1;
					tablicaFaz[iW] = DC2 * (Pi / 180.0);
					tablicaCzestotl[iW] = DC3 * (Pi / 180.0);
					tablicaNumFal[iW] = wierszPlik.NRI;
					iW = iW + 1;
				}

			}

			liczbaFal = iW - 1;

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return 0.0;
		} finally {
			scanner.close();
		}

		// ------------------------------------------------------------------------------
		return 0;
	}

	double funkcjaWenzela(int model, double szerokoscStopnie,
			double szerokoscMinuty, double szerokoscSekundy,
			double dlugoscStopnie, double dlugoscMinuty, double dlugoscSekundy,
			double wysokosc, int rok, int miesiac, int dzien, int godzina,
			int minuta, int sekunda) {
		int iG, NAK, NEK, iW;
		int MAXWG = 85;
		int NGR = 14;
		int[] INA = new int[MAXWG], INE = new int[MAXWG];
		double DAM, DBOD = 0, DDC, WYNIK = 0;

		class DaneWG {
			public int NA = 0;
			public int NE = 0;
			public String CNSY;
			public double DG0 = 0.0;
			public double DPHI0 = 0.0;
		}

		DaneWG[] tablicaWG = new DaneWG[MAXWG];
		double Pi = 3.141592653589793;
		// ------------------------------------------------------------------------------
		funkcjaPlywowa(2, szerokoscStopnie, szerokoscMinuty, szerokoscSekundy,
				dlugoscStopnie, dlugoscMinuty, dlugoscSekundy, wysokosc, rok,
				miesiac, dzien, godzina, minuta, sekunda);
		// ------------------------------------------------------------------------------
		for (iG = 0; iG < NGR; iG++) {
			tablicaWG[iG] = new DaneWG();
		}
		// ------------------------------------------------------------------------------
		File file = new File(WAVE_GROUPS);
		Scanner scanner = null;

		try {
			scanner = new Scanner(file);

			while (scanner.hasNext()) {
				for (iG = 0; iG < NGR; iG++) {
					String line = scanner.nextLine();
					StringTokenizer tokens = new StringTokenizer(line);

					tablicaWG[iG].NA = Integer.parseInt(tokens.nextToken());
					tablicaWG[iG].NE = Integer.parseInt(tokens.nextToken());
					tablicaWG[iG].CNSY = tokens.nextToken();
					tablicaWG[iG].DG0 = Double.parseDouble(tokens.nextToken());
					tablicaWG[iG].DPHI0 = Double
							.parseDouble(tokens.nextToken());
					// --------------------------------------------------------------------

					for (iW = 0; iW < liczbaFal + 1; iW++) {
						if (tablicaNumFal[iW] >= tablicaWG[iG].NA) {
							INA[iG] = iW + 1;
							iW = liczbaFal;
						}
					}

					for (iW = 0; iW < liczbaFal + 1; iW++) {
						if (tablicaNumFal[iW] >= tablicaWG[iG].NE) {
							INE[iG] = iW + 1;
							iW = liczbaFal;
						}
					}
					NAK = INA[iG];
					NEK = INE[iG];

					// szukanie g³ównej fali grupy
					DAM = 0.0;
					for (iW = NAK - 1; iW < NEK; iW++) {
						if (tablicaAmplitud[iW] >= DAM) {
							DAM = tablicaAmplitud[iW];
							DBOD = tablicaPlywow[iW];
						}
					}

					// obliczenie obserwowanej amplitudy
					for (iW = NAK - 1; iW < NEK; iW++) {
						tablicaAmplitud[iW] = tablicaAmplitud[iW]
								* tablicaWG[iG].DG0 * tablicaPlywow[iW] / DBOD;
						tablicaFaz[iW] = tablicaFaz[iW] + tablicaWG[iG].DPHI0
								* (Pi / 180.0);
					}
				}
			}

			// ------------------------------------------------------------------------------
			DDC = 0.0;
			for (iG = 0; iG < NGR; iG++) {
				for (iW = tablicaWG[iG].NA - 1; iW < tablicaWG[iG].NE; iW++) {
					DDC = DDC + tablicaAmplitud[iW] * cos(tablicaFaz[iW]);
				}
			}
			WYNIK = DDC / 10.0;

		} catch (FileNotFoundException e) {
			return 0.0;

		} finally {
			scanner.close();
		}

		// ------------------------------------------------------------------------------
		return WYNIK;
	}

	public double getTide(double wspolczynnikSztywnosci, int szerokoscStopnie,
			int szerokoscMinuty, float szerokoscSekundy, int dlugoscStopnie,
			int dlugoscMinuty, float dlugoscSekundy, float Wysokosc, int Rok,
			int Miesiac, int Dzien, int Godzina, int Minuta, int Sekunda) {
		double PoprawkaLunisolarna;
		// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		zerujWszystkieTablice();

		PoprawkaLunisolarna = funkcjaWenzela(2, szerokoscStopnie,
				szerokoscMinuty, szerokoscSekundy, dlugoscStopnie,
				dlugoscMinuty, dlugoscSekundy, Wysokosc, Rok, Miesiac, Dzien,
				Godzina, Minuta, Sekunda)
				* wspolczynnikSztywnosci * (-1) / 1000;
		// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		return PoprawkaLunisolarna;
	}

	@Override
	public double getTide(double wspolczynnikSztywnosci, B b, L l, H h,
			Calendar calendar) {
		return getTide(wspolczynnikSztywnosci, b.getStopnie(), b.getMinuty(),
				(float) b.getSekundy(), l.getStopnie(), l.getMinuty(),
				(float) l.getSekundy(), (float) h.getHeigh(),
				calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
				calendar.get(Calendar.DAY_OF_MONTH),
				calendar.get(Calendar.HOUR_OF_DAY),
				calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND));
	}

	public static void main(String[] args) {
		TideWenzel ti = new TideWenzel();

		int g = 0;
		int m = 0;
		for (int i = 0; i < 143; i++) {
			m = m + 10;
			if (m >= 60) {
				g++;
				m = 0;
			}

			double res1 = ti.getTide(1.2, 52, 13, 56, 21, 0, 31, 100, 2007, 07,
					01, g, m, 0);

			// System.out.println(res1 + "   g " + g + "  m" + m);
			// System.out.println("g " + g + "  m" + m);
			System.out.println(res1);

		}
	}

}
