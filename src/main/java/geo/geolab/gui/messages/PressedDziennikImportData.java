package geo.geolab.gui.messages;

public class PressedDziennikImportData extends Message {
	private String nazwaDziennika;
	private String obiekt;
	private String obserwator;
	private String sekretarz;
	private String path;

	public PressedDziennikImportData(String nazwa, String obiekt,
			String obserwator, String sekretarz, String path) {
		super("Import");
		this.nazwaDziennika = nazwa;
		this.obiekt = obiekt;
		this.obserwator = obserwator;
		this.sekretarz = sekretarz;
		this.path = path;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getNazwaDziennika() {
		return nazwaDziennika;
	}

	public void setNazwaDziennika(String nazwa) {
		this.nazwaDziennika = nazwa;
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
