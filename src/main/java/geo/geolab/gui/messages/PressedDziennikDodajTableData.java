package geo.geolab.gui.messages;

public class PressedDziennikDodajTableData extends Message {
	private String nazwaDziennika;
	private String obiekt;
	private String obserwator;
	private String sekretarz;
	private String[][] dziennik;

	public PressedDziennikDodajTableData(String nazwaDziennika, String obiekt,
			String obserwator, String sekretarz, String[][] dziennik) {
		super("Dodaj dziennik");
		this.nazwaDziennika = nazwaDziennika;
		this.obiekt = obiekt;
		this.obserwator = obserwator;
		this.sekretarz = sekretarz;
		this.dziennik = dziennik;
	}

	public String getNazwaDziennika() {
		return nazwaDziennika;
	}

	public void setNazwaDziennika(String nazwaDziennika) {
		this.nazwaDziennika = nazwaDziennika;
	}

	public String getObiekt() {
		return obiekt;
	}

	public void setObiekt(String obiekt) {
		this.obiekt = obiekt;
	}

	public String getObserwator() {
		return obserwator;
	}

	public void setObserwator(String obserwator) {
		this.obserwator = obserwator;
	}

	public String getSekretarz() {
		return sekretarz;
	}

	public void setSekretarz(String sekretarz) {
		this.sekretarz = sekretarz;
	}

	public String[][] getDziennik() {
		return dziennik;
	}

	public void setDziennik(String[][] dziennik) {
		this.dziennik = dziennik;
	}

}
