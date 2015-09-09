package geo.geolab.gui.messages;

public class PressedRaportUsunData extends Message {
	String nazwaRaportu;

	public PressedRaportUsunData(String nazwaRaportu) {
		super("Usun");
		this.nazwaRaportu = nazwaRaportu;
	}

	public String getValue() {
		return nazwaRaportu;
	}

	public void setValue(String nazwaRaportu) {
		this.nazwaRaportu = nazwaRaportu;
	}

}
