package geo.geolab.gravimetry;

import geo.geolab.comunicat.RaportComunicat;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;

public class GravDziennik {
	public static final String NAZWA = "Nazwa";
	public static final String OBIEKT = "Obiekt";
	public static final String OBSERWATOR = "Obserwator";
	public static final String SEKRETARZ = "Sekretarz";

	public static final int ERROR = 0;
	public static final int ABBAAB = 1;
	public static final int ABCDEA = 2;

	public static final int WENZEL = 0;
	public static final int LONGMAN = 1;
	public static final int BASIC = 2;

	protected List<GravStanowisko> list = new ArrayList<GravStanowisko>();
	protected GravInstrument grawimetr;

	public void openFromTXT(String path) {
		File file = new File(path);
		try {
			Scanner scanner = new Scanner(file);

			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				if (line.contains("@") || line.contains("#"))
					continue;
				StringTokenizer stringTokenizer = new StringTokenizer(line);

				if (stringTokenizer.countTokens() < 1)
					continue;

				String point = stringTokenizer.nextToken();

				String bStopnie = stringTokenizer.nextToken();
				String bMinuty = stringTokenizer.nextToken();
				String bSekundy = stringTokenizer.nextToken();

				String lStopnie = stringTokenizer.nextToken();
				String lMinuty = stringTokenizer.nextToken();
				String lSekundy = stringTokenizer.nextToken();

				String h = stringTokenizer.nextToken();

				String day = stringTokenizer.nextToken();
				String month = stringTokenizer.nextToken();
				String year = stringTokenizer.nextToken();
				String hour = stringTokenizer.nextToken();
				String minutes = stringTokenizer.nextToken();
				String seconds = stringTokenizer.nextToken();

				String odczyt = stringTokenizer.nextToken();
				String instrumentHeight = stringTokenizer.nextToken();
				String blad = stringTokenizer.nextToken();

				GravStanowisko gravStanowisko = new GravStanowisko(point,
						new B(bStopnie, bMinuty, bSekundy), new L(lStopnie,
								lMinuty, lSekundy), new H(h), day, month, year,
						hour, minutes, seconds, odczyt, instrumentHeight, blad);
				this.list.add(gravStanowisko);
			}
			scanner.close();

		} catch (FileNotFoundException e) {
			RaportComunicat.bladNierozpoznanoDziennika();
		}
	}

	public int rozpoznajSchemat() {

		// Sprawdzanie czy jest to schemat ABBAAB
		if (list.size() == 6 && list.get(0).equals(list.get(3))
				&& list.get(0).equals(list.get(4))
				&& list.get(1).equals(list.get(2))
				&& list.get(1).equals(list.get(5))
				&& !list.get(0).equals(list.get(1))) {

			return ABBAAB;
		}

		// Sprawdzanie czy jest to ABCDEA
		if (list.get(0).equals(list.get(list.size() - 1))) {
			boolean isABCDEA = true;

			for (int i = 0; i < list.size() - 1; i++) {
				for (int j = 0; j < list.size() - 1; j++) {
					if (i != j && list.get(i).equals(list.get(j))) {
						isABCDEA = false;
					}
				}
			}
			if (isABCDEA)
				return ABCDEA;
		}
		return ERROR;
	}

	public GravStanowisko getStanowisko(int index) {
		return list.get(index);
	}

	public int size() {
		return list.size();
	}

	public List<GravStanowisko> getList() {
		return list;
	}

	public void setList(List<GravStanowisko> list) {
		this.list = list;
	}

	public GravInstrument getGrawimetr() {
		return grawimetr;
	}

	public void setGrawimetr(GravInstrument grawimetr) {
		this.grawimetr = grawimetr;
	}

	public void display() {
		for (GravStanowisko stanowisko : list) {
			System.out.println(stanowisko.toString());
		}
	}

}
