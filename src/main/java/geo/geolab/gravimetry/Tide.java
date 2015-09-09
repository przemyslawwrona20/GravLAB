package geo.geolab.gravimetry;

import static java.lang.Math.*;

import java.util.Calendar;

public abstract class Tide {
	protected double szerokosc, dlugosc, ekliptyka;

	protected double ATan2(double yl, double xl) {
		double wynik;
		double Pi = 3.141592653589793;

		// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
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

	protected double StopnieNaRadiany(int deg, int min, float sek) // Wiadomo o
																	// co chodzi
	// :-)
	{
		double KatWRadianach;
		double Pi = 3.141592653589793;

		// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		KatWRadianach = (deg / (180 / Pi)) + (min / (180 * 60 / Pi))
				+ (sek / (180 * 3600 / Pi));

		return KatWRadianach;
	}

	protected double PrzeliczeniaTrygonometriiSferycznej(double ALat,
			double ALong, double AEcl) { // Przeliczenia miêdzy uk³adem
											// równikowym a
											// ekliptycznym
		double s, cc, cs, atn;
		double Pi = 3.141592653589793;

		szerokosc = 0;
		dlugosc = 0;
		ekliptyka = 0;
		// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
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

	protected double ZakresPlusMinusPi(double zmienna) // Œmieszna nazwa ale
														// dotyczy
	// doprowadzania
	{ // k¹ta do przedzia³u (-Pi;Pi>
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

	protected static double trunc(double val) {
		if (val > 0)
			return floor(val);
		else
			return ceil(val);
	}

	protected double fabs(double val) {
		return abs(val);
	}

	protected static double frac(double val) {
		if (val > 0)
			return val - floor(val);
		else
			throw new ArithmeticException("Value mniejsza od 0");
	}

	/* Tylko Wenzel */
	protected double fmod(double val, double div) {
		int calos = (int) (val / div);
		return (double) (val - calos * div);
	}

	public abstract double getTide(double wspolczynnikSztywnosci,
			int szerokoscStopnie, int szerokoscMinuty, float szerokoscSekundy,
			int dlugoscStopnie, int dlugoscMinuty, float dlugoscSekundy,
			float Wysokosc, int Rok, int Miesiac, int Dzien, int Godzina,
			int Minuta, int Sekunda);

	public abstract double getTide(double wspolczynnikSztywnosci, B b, L l,
			H h, Calendar calendar);

}
