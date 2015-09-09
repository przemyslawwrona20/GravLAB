package geo.geolab.gui.messages;

/** Klasa do pozyskiwania danych o grawimetrze */
public class PressedGrawimetrUsunData extends Message {
	String value;
	String stalaGrawimetru;
	String[][] tablicaReferencyjna;

	public PressedGrawimetrUsunData(String value) {
		super("Usun");
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getStalaGrawimetru() {
		return stalaGrawimetru;
	}

	public void setStalaGrawimetru(String stalaGrawimetru) {
		this.stalaGrawimetru = stalaGrawimetru;
	}

	public String[][] getTablicaReferencyjna() {
		return tablicaReferencyjna;
	}

	public void setTablicaReferencyjna(String[][] tablicaReferencyjna) {
		this.tablicaReferencyjna = tablicaReferencyjna;
	}

}
