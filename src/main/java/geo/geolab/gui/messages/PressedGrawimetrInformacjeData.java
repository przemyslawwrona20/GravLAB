package geo.geolab.gui.messages;

public class PressedGrawimetrInformacjeData extends Message {

	private String nazwaGrawimetru;
	private String stalaGrawimetru = null;
	private String[][] tablicaReferencyjna = null;

	public PressedGrawimetrInformacjeData(String nazwaGrawimetru) {
		super("Informacje");
		this.nazwaGrawimetru = nazwaGrawimetru;
	}

	public void add(int wiersz, int kolumna, String value) {
		/** Liczba kolumn jest wiêksza ni¿ 10 */
		if (kolumna >= 10)
			return;

		if (tablicaReferencyjna == null) {
			tablicaReferencyjna = new String[10][3];
		}

		if (tablicaReferencyjna.length - 1 < wiersz) {
			String[][] tablicaReferencyjnaZmiennaPomocnicza = new String[wiersz + 1][3];

			/** Przepisanie tablicy */
			for (int i = 0; i < tablicaReferencyjna.length; i++) {
				for (int j = 0; j < 3; j++) {
					tablicaReferencyjnaZmiennaPomocnicza[i][j] = tablicaReferencyjna[i][j];
				}
			}

			tablicaReferencyjna = tablicaReferencyjnaZmiennaPomocnicza;
		}
		tablicaReferencyjna[wiersz][kolumna] = value;
	}

	public String getNazwaGrawimetru() {
		return nazwaGrawimetru;
	}

	public void setNazwaGrawimetru(String nazwaGrawimetru) {
		this.nazwaGrawimetru = nazwaGrawimetru;
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
