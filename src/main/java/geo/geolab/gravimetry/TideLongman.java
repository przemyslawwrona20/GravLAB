package geo.geolab.gravimetry;

import static java.lang.Math.*;

import java.util.Calendar;

public class TideLongman extends Tide {
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

	// ********************************************************************************
	public double getTide(double wspolczynnikSztywnosci, int szerokoscStopnie,
			int szerokoscMinuty, float szerokoscSekundy, int dlugoscStopnie,
			int dlugoscMinuty, float dlugoscSekundy, float Wysokosc, int Rok,
			int Miesiac, int Dzien, int Godzina, int Minuta, int Sekunda) {
		double PoprawkaLunisolarna;
		// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		PoprawkaLunisolarna = PlywyLongmana(szerokoscStopnie, szerokoscMinuty,
				szerokoscSekundy, dlugoscStopnie, dlugoscMinuty,
				dlugoscSekundy, Wysokosc, Rok, Miesiac, Dzien, Godzina, Minuta,
				Sekunda)
				* wspolczynnikSztywnosci / 1000;
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
		TideLongman ti = new TideLongman();

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
