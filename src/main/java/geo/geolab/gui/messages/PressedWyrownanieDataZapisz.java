package geo.geolab.gui.messages;

import geo.geolab.gravimetry.Wyrownanie;

public class PressedWyrownanieDataZapisz extends Message {
	public String getPathToSave() {
		return pathToSave;
	}

	public void setPathToSave(String pathToSave) {
		this.pathToSave = pathToSave;
	}

	private Wyrownanie wyrownanie;
	private String pathToSave;

	public PressedWyrownanieDataZapisz(String name) {
		super(name);
	}

	public PressedWyrownanieDataZapisz(String pathToSave, Wyrownanie wyrownanie) {
		super("Zapisz wyrownanie");
		this.pathToSave = pathToSave;
		this.wyrownanie = wyrownanie;
	}

	public Wyrownanie getWyrownanie() {
		return wyrownanie;
	}

	public void setWyrownanie(Wyrownanie wyrownanie) {
		this.wyrownanie = wyrownanie;
	}

}
