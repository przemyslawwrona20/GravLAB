package geo.geolab.gravimetry;

import java.math.BigDecimal;
import java.math.RoundingMode;

public abstract class Raport {
	public static double GRADIENT = 0.3086;

	/** Obiekt dziennika pomiarowego */
	protected GravDziennik dziennik;

	// ------------------------- WARTOSCI POPRAWEK -------------------------
	protected Double[] odczytPrawidlowy;
	protected Double[] poprawkiGradientu;
	protected Double[] poprawkaLuni;
	protected Double[] gRef;

	protected int typPoprawki = 0;
	protected double wspolczynnikSztywnosciZiemi = 1.18;

	public Raport(GravDziennik dziennik, int typPoprawki,
			double wspolczynnikSztywnosciZiemi) {
		super();
		this.dziennik = dziennik;
		this.typPoprawki = typPoprawki;
		this.wspolczynnikSztywnosciZiemi = wspolczynnikSztywnosciZiemi;

		odczytPrawidlowy = new Double[dziennik.size()];
		poprawkiGradientu = new Double[dziennik.size()];
		poprawkaLuni = new Double[dziennik.size()];
		gRef = new Double[dziennik.size()];
	}

	protected abstract Double[] obliczPoprawkiGradientu();

	protected abstract Double[] obliczOdczytWlasciwy();

	protected abstract Double[] obliczGRef();

	public abstract void oblicz();

	public abstract void export(String folder);

	public static String StringToLeft(String name, int size) {
		StringBuilder returnStringBuilder = new StringBuilder();
		returnStringBuilder.append(name);
		for (int i = 0; i < size - name.length(); i++) {
			returnStringBuilder.append(" ");
		}

		return returnStringBuilder.toString();
	}

	public static String StringToRight(String name, int size) {
		StringBuilder returnStringBuilder = new StringBuilder();
		for (int i = 0; i < size - name.length(); i++) {
			returnStringBuilder.append(" ");
		}
		returnStringBuilder.append(name);

		return returnStringBuilder.toString();
	}

	public static String DoubleWithScale(Double value, int scale) {
		BigDecimal bigDecimal = new BigDecimal(value).setScale(scale,
				RoundingMode.HALF_EVEN);
		return bigDecimal.toString();
	}

	public int getTypPoprawki() {
		return typPoprawki;
	}

	public void setTypPoprawki(int typPoprawki) {
		this.typPoprawki = typPoprawki;
	}

	public double getWspolczynnikSztywnosciZiemi() {
		return wspolczynnikSztywnosciZiemi;
	}

	public void setWspolczynnikSztywnosciZiemi(double wspolczynnikSztywnosciZiemi) {
		this.wspolczynnikSztywnosciZiemi = wspolczynnikSztywnosciZiemi;
	}

}
