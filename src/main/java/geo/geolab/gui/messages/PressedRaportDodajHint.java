package geo.geolab.gui.messages;

public class PressedRaportDodajHint extends Message {
	private String nazwaDziennika;
	private String nazwaRaportu = null;
	private String obiekt = null;
	private String obserwator = null;
	private String sekretarz = null;

	public PressedRaportDodajHint(String nazwaDziennika) {
		super("Raport hint");
		this.nazwaDziennika = nazwaDziennika;
	}

	public String getNazwaDziennika() {
		return nazwaDziennika;
	}

	public void setNazwaDziennika(String nazwaDziennika) {
		this.nazwaDziennika = nazwaDziennika;
	}

	public String getNazwaRaportu() {
		return nazwaRaportu;
	}

	public void setNazwaRaportu(String nazwaRaportu) {
		this.nazwaRaportu = nazwaRaportu;
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
}
