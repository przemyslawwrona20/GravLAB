package geo.geolab.gui.messages;

public class PressedRaportDodajData extends Message {
	public String getPathToSave() {
		return pathToSave;
	}

	public void setPathToSave(String pathToSave) {
		this.pathToSave = pathToSave;
	}

	private String pathToSave;
	private String nazwaRaportu;
	private String dziennikPomiarowy;
	private String instrumentPomiarowy;

	private int typPoprawki;

	private String obiekt;
	private String obserwator;
	private String sekretarz;

	public String getNazwaRaportu() {
		return nazwaRaportu;
	}

	public void setNazwaRaportu(String nazwaRaportu) {
		this.nazwaRaportu = nazwaRaportu;
	}

	public PressedRaportDodajData() {
		super("dodaj nowy raport");
	}

	public PressedRaportDodajData(String pathToSave, String dziennikPomiarowy,
			String instrumentPomiarowy, int typPoprawki, String nazwaRaportu,
			String obiekt, String obserwator, String sekretarz) {
		super("dodaj nowy raport");
		this.pathToSave = pathToSave;
		this.dziennikPomiarowy = dziennikPomiarowy;
		this.instrumentPomiarowy = instrumentPomiarowy;
		this.typPoprawki = typPoprawki;
		this.nazwaRaportu = nazwaRaportu;
		this.obiekt = obiekt;
		this.obserwator = obserwator;
		this.sekretarz = sekretarz;
	}

	public String getDziennikPomiarowy() {
		return dziennikPomiarowy;
	}

	public void setDziennikPomiarowy(String dziennikPomiarowy) {
		this.dziennikPomiarowy = dziennikPomiarowy;
	}

	public String getInstrumentPomiarowy() {
		return instrumentPomiarowy;
	}

	public void setInstrumentPomiarowy(String instrumentPomiarowy) {
		this.instrumentPomiarowy = instrumentPomiarowy;
	}

	public int getTypPoprawki() {
		return typPoprawki;
	}

	public void setTypPoprawki(int typPoprawki) {
		this.typPoprawki = typPoprawki;
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
