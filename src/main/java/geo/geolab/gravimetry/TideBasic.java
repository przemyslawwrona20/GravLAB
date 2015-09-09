package geo.geolab.gravimetry;

import static java.lang.Math.*;

import java.util.Calendar;

public class TideBasic extends Tide {

	private double Plywy(double wysokosc, double szerokoscWradianach,
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
		// double a = 6378137.0;
		// double ek = 0.05489972;
		// double ms = 0.0748040;
		// double rev = 1296000.0;

		RoSekundowe = (180.0 * 3600.0) / Pi;
		RoStopniowe = 180.0 / Pi;
		GMMoon = 6.6742 * pow(10, -8) * 7.3537 * pow(10, 25);
		GMSun = 6.6742 * pow(10, -8) * 1.993 * pow(10, 33);

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

	public double getTide(double wspolczynnikSztywnosci, int szerokoscStopnie,
			int szerokoscMinuty, float szerokoscSekundy, int dlugoscStopnie,
			int dlugoscMinuty, float dlugoscSekundy, float Wysokosc, int Rok,
			int Miesiac, int Dzien, int Godzina, int Minuta, int Sekunda) {
		double SzerokoscWRadianach, DlugoscWRadianach, PoprawkaLunisolarna;

		SzerokoscWRadianach = StopnieNaRadiany(szerokoscStopnie,
				szerokoscMinuty, szerokoscSekundy);
		DlugoscWRadianach = StopnieNaRadiany(dlugoscStopnie, dlugoscMinuty,
				dlugoscSekundy);
		PoprawkaLunisolarna = -(Plywy(Wysokosc, SzerokoscWRadianach,
				DlugoscWRadianach, Rok, Miesiac, Dzien, Godzina, Minuta,
				Sekunda) * wspolczynnikSztywnosci);

		return PoprawkaLunisolarna / 1000;
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
		TideBasic ti = new TideBasic();

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

			System.out.println(res1 + "   g " + g + "  m" + m);
			// System.out.println("g " + g + "  m" + m);
			// System.out.println(res1);

		}

	}

}
