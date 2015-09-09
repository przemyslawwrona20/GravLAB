package geo.geolab.gui.messages;

public abstract class Message {

	/** Nazwa obiektu */
	private String name;

	/** Opis obiektu */
	private String description;

	/**
	 * 
	 * @param description
	 *            opis obiektu
	 * @param dane
	 *            dane przesylane do kontrolera
	 * @param controller
	 *            nazwa kontrolera obslugujacego przycisk
	 */
	public Message(String name) {
		super();
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

}
