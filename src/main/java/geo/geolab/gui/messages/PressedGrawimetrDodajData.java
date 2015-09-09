package geo.geolab.gui.messages;

public class PressedGrawimetrDodajData extends Message {

	private boolean isStala;
	private boolean isPath;
	private String name = null;
	private String path = null;

	public PressedGrawimetrDodajData() {
		super("Dodaj instrument");
	}

	public PressedGrawimetrDodajData(boolean isStala, Boolean isPath, String name,
			String path) {
		super("Dodja instrument");
		this.isStala = isStala;
		this.isPath = isPath;
		this.name = name;
		this.path = path;
	}

	public boolean isStala() {
		return isStala;
	}

	public void setStala(boolean isStala) {
		this.isStala = isStala;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public boolean isPath() {
		return isPath;
	}

	public void setPath(boolean isPath) {
		this.isPath = isPath;
	}
}
