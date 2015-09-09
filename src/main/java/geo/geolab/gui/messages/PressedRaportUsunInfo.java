package geo.geolab.gui.messages;

import javax.swing.JTextArea;

public class PressedRaportUsunInfo extends Message {

	private String nazwaRaportu;
	private JTextArea taInformacje;

	public PressedRaportUsunInfo(String nazwaRaportu,
			JTextArea taInformacje) {
		super("Informacje");
		this.nazwaRaportu = nazwaRaportu;
		this.taInformacje = taInformacje;
	}

	public String getNazwaRaportu() {
		return nazwaRaportu;
	}

	public void setNazwaRaportu(String nazwaGrawimetru) {
		this.nazwaRaportu = nazwaGrawimetru;
	}

	public JTextArea getTaInformacje() {
		return taInformacje;
	}

	public void setTaInformacje(JTextArea taInformacje) {
		this.taInformacje = taInformacje;
	}

}
