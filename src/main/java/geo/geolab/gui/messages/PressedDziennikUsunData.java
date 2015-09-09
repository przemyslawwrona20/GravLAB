package geo.geolab.gui.messages;

public class PressedDziennikUsunData extends Message {
	String value;

	public PressedDziennikUsunData(String value) {
		super("Usun");
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
