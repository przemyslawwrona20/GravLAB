package geo.geolab.layout;

import java.awt.*;

@SuppressWarnings("serial")
public class GBC extends GridBagConstraints {
	/**
	 * Tworzy obiekt typu GBC z podanymi warto�ciami gridx i gridy oraz
	 * wszystkimi pozosta�ymi parametrami ustawionymi na warto�ci domy�lne.
	 * 
	 * @param gridx
	 *            wsp�rz�dna gridx
	 * @param gridy
	 *            wsp�rz�dna gridy
	 */
	public GBC(int gridx, int gridy) {
		this.gridx = gridx;
		this.gridy = gridy;
	}

	/**
	 * Tworzy obiekt typu GBC z podanymi warto�ciami gridx, gridy, gridwidth i
	 * gridheight oraz wszystkimi pozosta�ymi parametrami ustawionymi na
	 * warto�ci domy�lne.
	 * 
	 * @param gridx
	 *            wsp�rz�dna gridx
	 * @param gridy
	 *            wsp�rz�dna gridy
	 * @param gridwidth
	 *            liczba zajmowanych kom�rek w poziomie
	 * @param gridheight
	 *            liczba zajmowanych kom�rek w pionie
	 */
	public GBC(int gridx, int gridy, int gridwidth, int gridheight) {
		this.gridx = gridx;
		this.gridy = gridy;
		this.gridwidth = gridwidth;
		this.gridheight = gridheight;
	}

	/**
	 * Ustawia parametr anchor.
	 * 
	 * @param anchor
	 *            warto�� parametru anchor
	 * @return this obiekt do dalszej modyfikacji
	 */
	public GBC setAnchor(int anchor) {
		this.anchor = anchor;
		return this;
	}

	/**
	 * Ustawia kierunek zape�niania.
	 * 
	 * @param fill
	 *            kierunek zape�niania
	 * @return this obiekt do dalszej modyfikacji
	 */
	public GBC setFill(int fill) {
		this.fill = fill;
		return this;
	}

	/**
	 * Ustawia parametry weight kom�rek.
	 * 
	 * @param weightx
	 *            parametr weight w poziomie
	 * @param weighty
	 *            parametr weight w pionie
	 * @return this obiekt do dalszej modyfikacji
	 */
	public GBC setWeight(double weightx, double weighty) {
		this.weightx = weightx;
		this.weighty = weighty;
		return this;
	}

	/**
	 * Ustawia dodatkow� pust� przestrze� w kom�rce.
	 * 
	 * @param distance
	 *            dope�nienie we wszystkich kierunkach
	 * @return this obiekt do dalszej modyfikacji
	 */
	public GBC setInsets(int distance) {
		this.insets = new Insets(distance, distance, distance, distance);
		return this;
	}

	/**
	 * Ustawia dope�nienia w kom�rce.
	 * 
	 * @param top
	 *            odst�p od g�rnej kraw�dzi
	 * @param left
	 *            odst�p od lewej kraw�dzi
	 * @param bottom
	 *            odst�p od dolnej kraw�dzi
	 * @param right
	 *            odst�p od prawej kraw�dzi
	 * @return obiekt do dalszej modyfikacji
	 */
	public GBC setInsets(int top, int left, int bottom, int right) {
		this.insets = new Insets(top, left, bottom, right);
		return this;
	}

	/**
	 * Ustawia dope�nienie wewn�trzne.
	 * 
	 * @param ipadx
	 *            dope�nienie wewn�trzne poziome
	 * @param ipady
	 *            dope�nienie wewn�trzne pionowe
	 * @return obiekt do dalszej modyfikacji
	 */
	public GBC setIpad(int ipadx, int ipady) {
		this.ipadx = ipadx;
		this.ipady = ipady;
		return this;
	}
}