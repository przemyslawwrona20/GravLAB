package geo.geolab.gui.messages;

public class PressedDziennikInformacjeData extends Message {

	private String nazwaDziennika;
	private String nazwaObiektu, obserwator, sekretarz;
	private String[][] dziennikTablica;
	private int liczbaWierszy = 13;

	public PressedDziennikInformacjeData(String nazwaGrawimetru) {
		super("Informacje");
		this.nazwaDziennika = nazwaGrawimetru;
	}

	public String getNazwaDziennika() {
		return nazwaDziennika;
	}

	public void setNazwaDziennika(String nazwaGrawimetru) {
		this.nazwaDziennika = nazwaGrawimetru;
	}

	public String getNazwaObiektu() {
		return nazwaObiektu;
	}

	public void setNazwaObiektu(String nazwaObiektu) {
		this.nazwaObiektu = nazwaObiektu;
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

	public String[][] getDziennikTablica() {
		return dziennikTablica;
	}

	public void setDziennikTablica(String[][] dziennikTablica) {
		this.dziennikTablica = dziennikTablica;
	}

	public void add(int wiersz, int kolumna, String value) {
		/** Liczba kolumn jest wiêksza ni¿ 13 */
		if (kolumna > liczbaWierszy - 1)
			return;

		if (dziennikTablica == null) {
			dziennikTablica = new String[10][liczbaWierszy];
		}

		if (dziennikTablica.length - 1 < wiersz) {
			String[][] tablicaReferencyjnaZmiennaPomocnicza = new String[wiersz + 1][liczbaWierszy];

			/** Przepisanie tablicy */
			for (int i = 0; i < dziennikTablica.length; i++) {
				for (int j = 0; j < 3; j++) {
					tablicaReferencyjnaZmiennaPomocnicza[i][j] = dziennikTablica[i][j];
				}
			}

			/** Zmiana referencji tablicy */
			dziennikTablica = tablicaReferencyjnaZmiennaPomocnicza;
		}
		dziennikTablica[wiersz][kolumna] = value;
	}
}
