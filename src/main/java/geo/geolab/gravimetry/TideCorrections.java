package geo.geolab.gravimetry;

import static java.lang.Math.*;
import geo.geolab.comunicat.RaportComunicat;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Scanner;
import java.util.StringTokenizer;

public class TideCorrections {
	private static final String TDT_UTC = "./wenzel_data_files/TDT_UTC.txt";
	private static final String LICZBY_LOVEA = "./wenzel_data_files/liczbyLovea.txt";
	private static final String WAVE_GROUPS = "./wenzel_data_files/WaveGroups.txt";
	private static final String WENZEL_DAT = "./wenzel_data_files/Wenzel_DAT.txt";

	double szerokosc, dlugosc, ekliptyka;

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

	double fabs(double val) {
		return abs(val);
	}

	double trunc(double val) {
		if (val > 0)
			return floor(val);
		else
			return ceil(val);
	}

	double frac(double val) {
		if (val > 0)
			return val - floor(val);
		else
			throw new ArithmeticException("Value mniejsza od 0");
	}

	double fmod(double val, double div) {
		int calos = (int) (val / div);
		return (double) (val - calos * div);
	}

	double ATan2(double yl, double xl) {
		double wynik;
		double Pi = 3.141592653589793;

		if (xl == 0) {
			if (yl > 0) {
				wynik = Pi / 2.0;
			} else {
				wynik = Pi * (3.0 / 2.0);
			}
		} else {
			wynik = atan(yl / xl);

			if (xl > 0) {
				if (yl < 0) {
					wynik = 2.0 * Pi + atan(yl / xl);
				}
			} else {
				wynik = Pi + atan(yl / xl);
			}
		}

		return wynik;
	}

	// Wiadomo o co chodzi :-)
	double StopnieNaRadiany(int deg, int min, float sek) {
		double KatWRadianach;
		double Pi = 3.141592653589793;

		KatWRadianach = (deg / (180 / Pi)) + (min / (180 * 60 / Pi))
				+ (sek / (180 * 3600 / Pi));

		return KatWRadianach;
	}

	// Przeliczenia miêdzy uk³adem równikowym a ekliptycznym
	double PrzeliczeniaTrygonometriiSferycznej(double ALat, double ALong,
			double AEcl) {
		double s, cc, cs, atn;
		double Pi = 3.141592653589793;

		szerokosc = 0;
		dlugosc = 0;
		ekliptyka = 0;

		szerokosc = ALat;
		dlugosc = ALong;
		ekliptyka = AEcl;
		s = cos(ekliptyka) * sin(szerokosc) + sin(ekliptyka) * cos(szerokosc)
				* sin(dlugosc);
		cc = cos(szerokosc) * cos(dlugosc);
		cs = -sin(szerokosc) * sin(ekliptyka) + cos(szerokosc) * cos(ekliptyka)
				* sin(dlugosc);
		atn = atan(sqrt((s * s) / (1 - s * s)));

		if (s < 0) {
			szerokosc = -atn;
		} else {
			szerokosc = atn;
		}
		atn = atan(cs / cc);

		if (cc < 0) {
			dlugosc = atn + Pi;
		} else {
			dlugosc = atn;
		}

		return 0;
	}

	// Œmieszna nazwa ale dotyczy doprowadzania k¹ta do przedzia³u (-Pi;Pi>
	double ZakresPlusMinusPi(double zmienna) {
		double Pi = 3.141592653589793;

		if (zmienna > (2 * Pi)) {
			zmienna = zmienna - trunc(fabs(zmienna) / (2.0 * Pi)) * (2.0 * Pi);
		}

		if (zmienna < (2 * Pi)) {
			zmienna = zmienna + trunc(fabs(zmienna) / (2.0 * Pi)) * (2.0 * Pi);
		}

		if (zmienna > 0) {
			while (zmienna > Pi) {
				zmienna = zmienna - (2 * Pi);
			}
		} else {
			while (zmienna < -Pi) {
				zmienna = zmienna + (2 * Pi);
			}
		}

		return zmienna;
	}

	// /\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\
	// \//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\/

	double Plywy(double wysokosc, double szerokoscWradianach,
			double dlugoscWradianach, int rok, int miesiac, int dzien,
			int godzina, int minuta, int sekunda) {
		int[] TablicaDniWMiesiacu = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31,
				30, 31 };
		int i, j;
		double RoSekundowe, RoStopniowe, GMMoon, GMSun;
		double DayShare, T1900, T2000, StarTime;
		double l, l1, F, D, LGreat, M, G, e, v;
		double EclSlop, Old, Blunder, CosZ;
		double MoonLong, MoonLat, SunLong, SunLat;
		double MoonDist, SunDist, Radius;
		double OstatecznyWynik;

		double Pi = 3.141592653589793;
		double a = 6378137.0;
		double ek = 0.05489972;
		double ms = 0.0748040;
		double rev = 1296000.0;

		RoSekundowe = (180.0 * 3600.0) / Pi;
		RoStopniowe = 180.0 / Pi;
		GMMoon = 6.6742 * pow(10, -8) * 7.3537 * pow(10, 25);
		GMSun = 6.6742 * pow(10, -8) * 1.993 * pow(10, 33);
		// ************************************************************************
		if (rok < 100) {
			rok = rok + 1900;
		}
		i = 0;
		T1900 = -0.5;
		j = 1900;

		while (j < rok) {
			if (i == 4) {
				i = 0;
			}
			T1900 = T1900 + 365;

			if (i == 0) {
				T1900 = T1900 + 1;
			}
			i = i + 1;
			j = j + 1;
		}
		j = 1;

		while (j < miesiac) { // bo c++ numeruje argumenty w tablicy od 0 a nie
								// od 1
			T1900 = T1900 + TablicaDniWMiesiacu[j - 1];
			j = j + 1;
		}

		if ((miesiac >= 3) && (i == 4)) {
			T1900 = T1900 + 1;
		}
		T1900 = (T1900 + dzien - 1) / 36525;
		// ************************************************************************
		StarTime = 6 * 3600.0 + 41 * 60.0 + 50.548 + 8640184.813 * (T1900 - 1);
		DayShare = 3600.0 * godzina + 60.0 * minuta + sekunda + 0.8
				* (rok - 1980) + 50.5;
		StarTime = (StarTime + DayShare * 1.002738) * 2 * Pi / 86400
				+ dlugoscWradianach;
		T1900 = T1900 + DayShare / (86400.0 * 36525.0);
		T2000 = T1900 - 1;
		EclSlop = (84381.448 - 46.8150 * T2000) * (1 / RoSekundowe);
		// ************************************************************************
		l = (296.0 * 3600 + 6 * 60 + 16.59 + 1717915856.79 * T1900 + 33.09 * pow(
				T1900, 2)) * (1 / RoSekundowe);
		l1 = (358.0 * 3600 + 28 * 60 + 33.0 + 129596579.1 * T1900 - 0.54 * pow(
				T1900, 2)) * (1 / RoSekundowe);
		F = (11.0 * 3600 + 15 * 60 + 3.2 + 1739527290.54 * T1900 - 11.56 * pow(
				T1900, 2)) * (1 / RoSekundowe);
		D = (350.0 * 3600 + 44 * 60 + 14.95 + 1602961611.18 * T1900 - 5.17 * pow(
				T1900, 2)) * (1 / RoSekundowe);
		LGreat = (270.0 * 3600 + 26 * 60 + 2.99 + 1732564379.31 * T1900 - 4.08 * pow(
				T1900, 2)) * (1 / RoSekundowe);
		// ************************************************************************
		Blunder = LGreat
				+ (6.289 * sin(l) - 1.274 * sin(l - 2 * D) + 0.658 * sin(2 * D)
						+ 0.214 * sin(2 * l) - 0.186 * sin(l1) - 0.114
						* sin(2 * F) - 0.059 * sin(2 * l - 2 * D) - 0.057
						* sin(l + l1 - 2 * D) + 0.053 * sin(l + 2 * D) - 0.046
						* sin(l1 - 2 * D) + 0.041 * sin(l - l1) - 0.035
						* sin(D) - 0.03 * sin(l + l1)) * (1 / RoStopniowe);
		MoonLong = ZakresPlusMinusPi(Blunder);
		// ************************************************************************
		MoonLat = (5.128 * sin(F) + 0.281 * sin(l + F) - 0.278 * sin(F - l)
				- 0.173 * sin(F - 2 * D) + 0.055 * sin(F + 2 * D - l) - 0.046
				* sin(l + F - 2 * D) + 0.033 * sin(F + 2 * D))
				* (1 / RoStopniowe);
		MoonDist = (384404 * pow(10, 5))
				/ (1 + 0.0545 * cos(l) + 0.01 * cos(l - 2 * D) + 0.0082
						* cos(2 * D) + 0.003 * cos(2 * l) + 0.0009
						* cos(l + 2 * D) + 0.0006 * cos(l1 - 2 * D) + 0.0004
						* cos(l + l1 - 2 * D) + 0.0003 * cos(l - l1));
		PrzeliczeniaTrygonometriiSferycznej(MoonLat, MoonLong, EclSlop);
		MoonLat = szerokosc;
		MoonLong = dlugosc;
		EclSlop = ekliptyka;
		// ************************************************************************
		M = (1287099.804 + (99 * 360.0 * 3600.0 + 1292581.224) * T2000 - 0.577 * pow(
				T2000, 2)) * (1 / RoSekundowe);
		G = (1018578.046 + 6190.046 * T2000 + 1.666 * pow(T2000, 2))
				* (1 / RoSekundowe);
		e = 0.01673 - 0.0000419 * (T2000 + 0.5);
		M = ZakresPlusMinusPi(M);
		Blunder = M;
		Old = 0;

		while (fabs(Blunder - Old) > pow(10, -12)) {
			Old = Blunder;
			Blunder = M + e * sin(Blunder);
		}
		SunDist = 1.496 * pow(10, 13) * (1 - e * cos(Blunder));
		v = atan((sqrt(1 - (e * e)) * sin(Blunder) / (cos(Blunder) - e)));

		if (fabs(v - Blunder) > (Pi / 8)) {
			v = v - Pi;
		}
		SunLat = 0;
		SunLong = ZakresPlusMinusPi(v + G);
		// ************************************************************************
		PrzeliczeniaTrygonometriiSferycznej(SunLat, SunLong, EclSlop);
		SunLat = szerokosc;
		SunLong = dlugosc;
		EclSlop = ekliptyka;
		// ************************************************************************
		Radius = wysokosc * 100 + 6.378137 * pow(10, 8)
				* (0.99832707 + 0.00167664 * cos(2 * szerokoscWradianach));
		CosZ = sin(szerokoscWradianach) * sin(SunLat)
				+ cos(szerokoscWradianach) * cos(SunLat)
				* cos(StarTime - SunLong);
		Blunder = GMSun / SunDist * Radius / SunDist * (3 * pow(CosZ, 2) - 1)
				/ SunDist;
		CosZ = sin(szerokoscWradianach) * sin(MoonLat)
				+ cos(szerokoscWradianach) * cos(MoonLat)
				* cos(StarTime - MoonLong);
		// ************************************************************************
		Blunder = Blunder
				+ GMMoon
				* Radius
				* (3 * pow(CosZ, 2) - 1 + 3 * Radius
						* (5 * pow(CosZ, 3) - 3 * CosZ) / (2 * MoonDist))
				/ pow(MoonDist, 3);
		OstatecznyWynik = -Blunder * 1.2 * pow(10, 6);

		return OstatecznyWynik;
	}

	double PoprawkaPlywowaMetodaUproszczona(double wspolczynnikSztywnosci,
			int szerokoscStopnie, int szerokoscMinuty, float szerokoscSekundy,
			int dlugoscStopnie, int dlugoscMinuty, float dlugoscSekundy,
			float Wysokosc, int Rok, int Miesiac, int Dzien, int Godzina,
			int Minuta, int Sekunda) {
		double SzerokoscWRadianach, DlugoscWRadianach, PoprawkaLunisolarna;
		// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		SzerokoscWRadianach = StopnieNaRadiany(szerokoscStopnie,
				szerokoscMinuty, szerokoscSekundy);
		DlugoscWRadianach = StopnieNaRadiany(dlugoscStopnie, dlugoscMinuty,
				dlugoscSekundy);
		PoprawkaLunisolarna = -(Plywy(Wysokosc, SzerokoscWRadianach,
				DlugoscWRadianach, Rok, Miesiac, Dzien, Godzina, Minuta,
				Sekunda) * wspolczynnikSztywnosci) / 1.2;
		// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		return PoprawkaLunisolarna;
	}

	// /\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\
	// \//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\/

	double PlywyLongmana(int szer_deg, int szer_min, float szer_sek,
			int dlug_deg, int dlug_min, float dlug_sek, float Height, int Year,
			int Month, int Day, int Hour, int Minute, int Second) {
		double wynik, Longitude, Latitude;
		double a1t = 6378137;
		double a2 = 1.495 * pow(10, 11);
		double a3 = 0.05489972;
		double a4 = 0.074804;
		double a5 = 0.40931461617;
		double a6 = 0.00000667;
		double a7 = 7.3537 * pow(10, 25);
		double a8 = 1.993 * pow(10, 33);
		double a9 = 384402000;
		double a10 = 0.089797190013;
		double f1t = 0.006738;
		double f2 = 0.01675104;
		double f3 = 0.0000418;
		double f4 = 0.000000126;
		double f5t = 4.90822946677;
		double f6t = 0.03000526417;
		double f7t = 0.00000790246;
		double f8t = 0.00000005818;
		double f9t = 4.72002344;
		double f10 = 8399.7093;
		double f11 = 0.00004406956;
		double f12 = 0.00000003297;
		double f13 = 5.83512472;
		double f14 = 71.0180093;
		double f15 = 0.00018054461;
		double f16 = 0.00000021817;
		double f17 = 4.88162793402;
		double f18 = 628.331950978;
		double f19 = 0.00000527962;
		double f20 = 0.261799387794;
		double f21 = 4.52358857;
		double f22 = 33.757153;
		double f23 = 0.00003674888;
		double f24 = 0.00000003878;
		double a13, a14, a15, a16, a17, a18, a19, a20, a21, a22, a23, a24, a25, a26 = 0, a27, a28, a29;
		double a30, a31, a32, a33, a34, a35, a36, a37, a38, a39, a40, a41, a42, a43, a44, a45, a46;
		double a11, a12, ja2, ja3, j3, j4, j5, j6, j8, l1, l2, J, j1, j2;
		double temp, iTemp;

		double Pi = 3.141592653589793;

		j8 = Hour + (Minute / 100.0) + (Second / 6000.0);
		j2 = 310836960.0;
		Latitude = szer_deg + (szer_min / 60.0) + (szer_sek / 3600.0);
		l1 = (Latitude / 180.0) * Pi;
		Longitude = dlug_deg + (dlug_min / 60.0) + (dlug_sek / 3600.0);
		l2 = (Longitude / 180.0) * Pi;
		j3 = Month;
		j4 = Day;
		j5 = Year;

		if (j3 <= 2) {
			j3 = j3 + 12;
			j5 = j5 - 1;
		}
		j3 = j3 + 1;
		j6 = trunc(365.25 * j5 + 0.01);
		j6 = j6 - trunc((j5 / 100.0) + 0.01);
		j6 = j6 + trunc((j5 / 400.0) + 0.01);
		j6 = j6 + trunc(30.6001 * j3 + 0.01);
		j6 = j6 + j4 - 478164.0;
		j6 = j6 * 1440.0;
		j1 = j6 - j2;

		if ((j8 >= 0) && (j8 <= 12)) {
			j1 = j1 - 1440.0
					+ (trunc(12 + j8 + 0.001) * 60 + frac(12 + j8) * 100.0);
		}

		if ((j8 > 12) && (j8 < 24)) {
			j1 = j1 + trunc(j8 - 12 + 0.001) * 60 + frac(j8 - 12) * 100.0;
		}
		a11 = 1.0 / (a9 * (1.0 - pow(a3, 2)));
		a12 = sin(l1);
		a13 = cos(l1);
		J = j1 / 52596000.0;
		ja2 = pow(J, 2);
		ja3 = pow(J, 3);
		a14 = sqrt(1.0 / (1.0 + f1t * pow(a12, 2)));
		a15 = a14 * a1t + Height * 100.0;
		a16 = f2 - f3 * J - f4 * ja2;
		a17 = 1.0 / (a2 * (1.0 - pow(a16, 2)));
		a18 = f5t + f6t * J + f7t * ja2 + f8t * ja3;
		a19 = f9t + f10 * J + f11 * ja2 + f12 * ja3;
		a20 = f13 + f14 * J - f15 * ja2 - f16 * ja3;
		a21 = f17 + f18 * J + f19 * ja2;
		a22 = 1.0 / a2 + a17 * a16 * cos(a21 - a18);
		a23 = 1.0 / a9 + a11 * a3 * cos(a19 - a20) + a11 * pow(a3, 2)
				* cos(2.0 * (a19 - a20));
		a24 = 15.0 / 8.0 * a11 * a4 * a3 * cos(a19 - 2 * a21 + a20) + a11
				* pow(a4, 2) * cos(2.0 * (a19 - a21));
		a23 = a23 + a24;
		a25 = (j1 - trunc(j1 / 1440.0 + 0.001) * 1440.0) / 60.0 * f20;

		if (l2 < 0) {
			temp = a25 + l2;

			if (temp > 0) {
				a26 = a25 + l2;
			}

			if (temp == 0) {
				a26 = 0.0;
			}

			if (temp < 0) {
				a26 = a25 + l2 - 2.0 * Pi;
			}
		}

		if (l2 == 0) {
			a26 = a25 + l2;
		}

		if (l2 > 0) {
			a26 = a25 + l2 - 2.0 * Pi;
		}
		iTemp = a25 + l2 - 2.0 * Pi;

		{
			if (iTemp < 0) {
				a26 = a25 + l2;
			}

			if (iTemp == 0) {
				a26 = 0.0;
			}

			if (iTemp > 0) {
				a26 = a25 + l2 - 2.0 * Pi;
			}
		}
		a27 = a26 + a21;
		a28 = a21 + 2.0 * a16 * sin(a21 - a18);
		a29 = f21 - f22 * J + f23 * ja2 + f24 * ja3;
		a30 = cos(a5) * cos(a10) - sin(a5) * sin(a10) * cos(a29);
		a31 = sqrt(1.0 - pow(a30, 2));
		a32 = ATan2(a31, a30);
		a33 = sin(a10) * sin(a29) / a31;
		a34 = sqrt(1.0 - pow(a33, 2));
		a35 = ATan2(a33, a34);
		a36 = a26 + a21 - a35;
		a37 = cos(a29) * a34 + sin(a29) * a33 * cos(a5);
		a38 = sin(a5) * sin(a29) / a31;
		a39 = 2.0 * atan(a38 / (1.0 + a37));
		a40 = a29 - a39;
		a41 = a19 - a40;
		a42 = a41 + 2.0 * a3 * sin(a19 - a20) + 1.25 * pow(a3, 2)
				* sin(2.0 * (a19 - a20));
		a43 = 3.75 * a4 * a3 * sin(a19 - 2.0 * a21 + a20) + (11.0 / 8.0)
				* pow(a4, 2) * sin(2.0 * (a19 - a21));
		a42 = a42 + a43;
		a43 = a12 * sin(a5) * sin(a28);
		a44 = a13
				* (pow(cos(a5 / 2.0), 2) * cos(a28 - a27) + pow(sin(a5 / 2.0),
						2) * cos(a28 + a27));
		a43 = a43 + a44;
		a44 = a12 * a31 * sin(a42);
		a45 = a13
				* (pow(cos(a32 / 2.0), 2) * cos(a42 - a36) + pow(
						sin(a32 / 2.0), 2) * cos(a42 + a36));
		a44 = a44 + a45;
		a45 = a6 * a8 * a15 * pow(a22, 3) * (3.0 * pow(a43, 2) - 1.0);
		a46 = a6
				* a7
				* (a15 * pow(a23, 3) * (3.0 * pow(a44, 2) - 1.0) + 1.5
						* pow(a15, 2) * pow(a23, 4)
						* (5.0 * pow(a44, 3) - 3.0 * a44));
		wynik = (a45 + a46);

		return wynik;
	}

	double PoprawkaPlywowaMetodaLongmana(double wspolczynnikSztywnosci,
			int szerokoscStopnie, int szerokoscMinuty, float szerokoscSekundy,
			int dlugoscStopnie, int dlugoscMinuty, float dlugoscSekundy,
			float Wysokosc, int Rok, int Miesiac, int Dzien, int Godzina,
			int Minuta, int Sekunda) {
		double PoprawkaLunisolarna;
		// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		PoprawkaLunisolarna = PlywyLongmana(szerokoscStopnie, szerokoscMinuty,
				szerokoscSekundy, dlugoscStopnie, dlugoscMinuty,
				dlugoscSekundy, Wysokosc, Rok, Miesiac, Dzien, Godzina, Minuta,
				Sekunda)
				* wspolczynnikSztywnosci;
		// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		return PoprawkaLunisolarna;
	}

	// /\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\
	// \//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\/

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

	// //******************************************************************************
	int liczbaLiniWPliku(File file) {
		Scanner scanner = null;
		try {
			scanner = new Scanner(file);
			int i = 0;
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
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

	double PoprawkaPlywowaMetodaWenzela(double wspolczynnikSztywnosci,
			int szerokoscStopnie, int szerokoscMinuty, float szerokoscSekundy,
			int dlugoscStopnie, int dlugoscMinuty, float dlugoscSekundy,
			float Wysokosc, int Rok, int Miesiac, int Dzien, int Godzina,
			int Minuta, int Sekunda) {
		double PoprawkaLunisolarna;
		// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		zerujWszystkieTablice();

		PoprawkaLunisolarna = funkcjaWenzela(2, szerokoscStopnie,
				szerokoscMinuty, szerokoscSekundy, dlugoscStopnie,
				dlugoscMinuty, dlugoscSekundy, Wysokosc, Rok, Miesiac, Dzien,
				Godzina, Minuta, Sekunda)
				* wspolczynnikSztywnosci * (-1);
		// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		return PoprawkaLunisolarna;
	}

	// ------------------------------------------------------------------------------
	double obliczPoprawkeLunosolarna(int metOblPoprPlyw, double wspSztywZiemi,
			int degSzer, int minSzer, float sekSzer, int degDlug, int minDlug,
			float sekDlug, float wys, Calendar czas_t) {
		double result;

		if (metOblPoprPlyw == 0) {
			result = PoprawkaPlywowaMetodaWenzela(wspSztywZiemi, degSzer,
					minSzer, sekSzer, degDlug, minDlug, sekDlug, wys,
					czas_t.get(Calendar.YEAR), czas_t.get(Calendar.MONTH),
					czas_t.get(Calendar.DAY_OF_MONTH),
					czas_t.get(Calendar.HOUR), czas_t.get(Calendar.MINUTE),
					czas_t.get(Calendar.SECOND));
			// result = zaokraglijDouble(result, 5);
		}

		else if (metOblPoprPlyw == 1) {
			result = PoprawkaPlywowaMetodaLongmana(wspSztywZiemi, degSzer,
					minSzer, sekSzer, degDlug, minDlug, sekDlug, wys,
					czas_t.get(Calendar.YEAR), czas_t.get(Calendar.MONTH),
					czas_t.get(Calendar.DAY_OF_MONTH),
					czas_t.get(Calendar.HOUR), czas_t.get(Calendar.MINUTE),
					czas_t.get(Calendar.SECOND));
			// result = zaokraglijDouble(result, 5);
		}

		else if (metOblPoprPlyw == 2) {
			result = PoprawkaPlywowaMetodaUproszczona(wspSztywZiemi, degSzer,
					minSzer, sekSzer, degDlug, minDlug, sekDlug, wys,
					czas_t.get(Calendar.YEAR), czas_t.get(Calendar.MONTH),
					czas_t.get(Calendar.DAY_OF_MONTH),
					czas_t.get(Calendar.HOUR), czas_t.get(Calendar.MINUTE),
					czas_t.get(Calendar.SECOND));

			// result = zaokraglijDouble(result,5);
		}

		else {
			result = 0.0;
		}

		return result;
	}

	public static void main(String[] args) {
		TideCorrections ti = new TideCorrections();

		int g = 0;
		int m = 0;
		for (int i = 0; i < 1440; i++) {
			m++;
			if (m >= 60) {
				g++;
				m = 0;
			}

			Calendar cal = new GregorianCalendar(2006, 10, 10, g, m, 0);

			double res1 = ti.obliczPoprawkeLunosolarna(0, 1.40, 52, 48, 32, 22,
					10, 0, 100, cal);
			double res2 = ti.obliczPoprawkeLunosolarna(1, 1.40, 52, 48, 32, 22,
					10, 0, 100, cal);
			double res3 = ti.obliczPoprawkeLunosolarna(2, 1.40, 52, 48, 32, 22,
					10, 0, 100, cal);

			System.out.println(res1 + "   " + res2 + "    " + res3 + "   " + g
					+ " " + m);

		}

	}
}
