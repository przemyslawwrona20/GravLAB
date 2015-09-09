package geo.geolab.gravimetry;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class GravStanowisko {

	/** Gradient pionowy */
	public static final double GRADIENT = 0.3086;

	// ------------------------- DANE Z POMIARÓW -------------------------
	private String nazwa;
	private B b;
	private L l;
	private H h;
	private GregorianCalendar data;
	private Double odczyt;
	private Double wysokosc;
	private Double luni = new Double(0.0);
	private Double blad;

	// ------------------------- DANE OBLICZONE -------------------------
	public GravStanowisko(String nazwa, GregorianCalendar data, double odczyt,
			double wysokosc, Double luni, Double blad) {
		super();
		this.nazwa = nazwa;
		this.data = data;
		this.odczyt = odczyt;
		this.wysokosc = wysokosc;
		this.luni = luni;
		this.blad = blad;
	}

	public GravStanowisko(String nazwa, B b, L l, H h, String day,
			String month, String year, String hour, String minutes,
			String seconds, String odczyt, String wysokosc, String blad) {
		super();
		this.nazwa = nazwa;
		this.b = b;
		this.l = l;
		this.h = h;

		data = new GregorianCalendar(Integer.parseInt(year),
				Integer.parseInt(month), Integer.parseInt(day),
				Integer.parseInt(hour), Integer.parseInt(minutes),
				Integer.parseInt(seconds));

		odczyt = odczyt.replace(",", ".");
		this.odczyt = Double.parseDouble(odczyt);

		wysokosc = wysokosc.replace(",", ".");
		this.wysokosc = Double.parseDouble(wysokosc);

		blad = blad.replace(",", ".");
		this.blad = Double.parseDouble(blad);

	}

	@Override
	public String toString() {
		return "GravStanowisko [nazwa=" + nazwa + ", odczyt=" + odczyt
				+ ", wysokosc=" + wysokosc + ", luni=" + luni + ", blad="
				+ blad + "]";
	}

	public String getNazwa() {
		return nazwa;
	}

	public void setNazwa(String nazwa) {
		this.nazwa = nazwa;
	}

	public B getB() {
		return b;
	}

	public void setB(B b) {
		this.b = b;
	}

	public L getL() {
		return l;
	}

	public void setL(L l) {
		this.l = l;
	}

	public H getH() {
		return h;
	}

	public void setH(H h) {
		this.h = h;
	}

	public GregorianCalendar getData() {
		return data;
	}

	public void setData(GregorianCalendar data) {
		this.data = data;
	}

	public Double getOdczyt() {
		return odczyt;
	}

	public void setOdczyt(Double odczyt) {
		this.odczyt = odczyt;
	}

	public Double getWysokosc() {
		return wysokosc;
	}

	public void setWysokosc(Double wysokosc) {
		this.wysokosc = wysokosc;
	}

	public Double getLuni() {
		return luni;
	}

	public void setLuni(Double luni) {
		this.luni = luni;
	}

	public Double getBlad() {
		return blad;
	}

	public void setBlad(Double blad) {
		this.blad = blad;
	}

	public String getStringTime(String separator) {
		return Integer.toString(data.get(Calendar.HOUR_OF_DAY)) + separator
				+ Integer.toString(data.get(Calendar.MINUTE)) + separator
				+ Integer.toString(data.get(Calendar.SECOND));
	}

	public String getStringData(String separator) {
		return Integer.toString(data.get(Calendar.DAY_OF_MONTH)) + separator
				+ Integer.toString(data.get(Calendar.MONTH)) + separator
				+ Integer.toString(data.get(Calendar.YEAR));
	}

	public double getPoprawkaGradientu() {
		return wysokosc * 0.01 * GravStanowisko.GRADIENT;
	}

	public double getLuni(int typPoprawki, double wspolczynnikSztywnosci) {
		if (typPoprawki == GravDziennik.WENZEL) {
			Tide tide = new TideWenzel();
			luni = tide.getTide(wspolczynnikSztywnosci, b, l, h, data);
		} else if (typPoprawki == GravDziennik.LONGMAN) {
			Tide tide = new TideWenzel();
			luni = tide.getTide(wspolczynnikSztywnosci, b, l, h, data);
		} else if (typPoprawki == GravDziennik.BASIC) {
			Tide tide = new TideWenzel();
			luni = tide.getTide(wspolczynnikSztywnosci, b, l, h, data);
		} else {
			luni = 0.0;
		}

		return luni;
	}

	@Override
	public boolean equals(Object arg0) {
		GravStanowisko stanowisko = (GravStanowisko) arg0;
		if (stanowisko.getNazwa().equals(this.nazwa))
			return true;
		else
			return false;
	}
}
