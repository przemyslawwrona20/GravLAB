package geo.geolab.gravimetry;

import java.util.ArrayList;

public class Wyrownanie {
	private ArrayList<GravPunkt> punktyStale;
	private ArrayList<GravPunkt> punktyDoWyrownania;
	private ArrayList<GravPrzewyzszenie> przewyzszenia;
	private Matrix A, L, X, V;

	double m0;

	public Wyrownanie(ArrayList<GravPunkt> punktyStale,
			ArrayList<GravPrzewyzszenie> przewyzszenia) {
		super();
		this.punktyStale = punktyStale;
		this.przewyzszenia = przewyzszenia;
		punktyDoWyrownania = new ArrayList<GravPunkt>();
	}

	public void wyrownaj() {
		znajdzPunktyDoWyrownania();
		stworzMacierzeALStandaryzacja();

		X = ((A.transpose().multiply(A)).inverse()).multiply(
				A.transpose().multiply(L)).multiply(-1.0);
		V = (A.multiply(X)).add(L);

		m0 = Math.sqrt(V.transpose().multiply(V).getValue(0, 0)
				/ (przewyzszenia.size() - punktyDoWyrownania.size()));

		System.out.println();

	}

	private void stworzMacierzeALStandaryzacja() {
		A = new Matrix(przewyzszenia.size(), punktyDoWyrownania.size());
		L = new Matrix(przewyzszenia.size(), 1);

		int i = 0;
		for (GravPrzewyzszenie przewyzszenie : przewyzszenia) {
			int sourceIndex = punktyDoWyrownania.indexOf(new GravPunkt(
					przewyzszenie.getSource(), 0.0));
			int destIndex = punktyDoWyrownania.indexOf(new GravPunkt(
					przewyzszenie.getDest(), 0.0));

			if (sourceIndex > -1)
				A.setValues(i, sourceIndex, -1);

			if (destIndex > -1)
				A.setValues(i, destIndex, 1);

			double sourceHeight = 0.0, destHeight = 0.0;

			if (sourceIndex == -1) {
				sourceIndex = punktyStale.indexOf(new GravPunkt(przewyzszenie
						.getSource(), 0.0));
				sourceHeight = punktyStale.get(sourceIndex).getValue();
			}

			if (destIndex == -1) {
				destIndex = punktyStale.indexOf(new GravPunkt(przewyzszenie
						.getDest(), 0.0));
				destHeight = punktyStale.get(destIndex).getValue();
			}
			L.setValues(i, 0,
					destHeight - sourceHeight - przewyzszenie.getValue());

			A = A.multiplyLine(A, i, 1 / przewyzszenie.getBlad());
			L = L.multiplyLine(L, i, 1 / przewyzszenie.getBlad());

			i++;
		}
	}

	private void znajdzPunktyDoWyrownania() {
		for (int i = 0; i < przewyzszenia.size(); i++) {
			GravPunkt source = new GravPunkt(przewyzszenia.get(i).getSource(),
					null);
			GravPunkt dest = new GravPunkt(przewyzszenia.get(i).getDest(), null);

			if (!punktyStale.contains(source)
					&& !punktyDoWyrownania.contains(source))
				punktyDoWyrownania.add(source);

			if (!punktyStale.contains(dest)
					&& !punktyDoWyrownania.contains(dest))
				punktyDoWyrownania.add(dest);
		}
	}

	public static void main(String[] args) {
		ArrayList<GravPrzewyzszenie> przewyzszenie = new ArrayList<GravPrzewyzszenie>();
		przewyzszenie.add(new GravPrzewyzszenie("1", "4", 12.246, 8));
		przewyzszenie.add(new GravPrzewyzszenie("1", "5", 8.542, 3));
		przewyzszenie.add(new GravPrzewyzszenie("1", "6", 15.699, 5));
		przewyzszenie.add(new GravPrzewyzszenie("2", "3", -6.872, 3));
		przewyzszenie.add(new GravPrzewyzszenie("2", "5", -8.379, 3));
		przewyzszenie.add(new GravPrzewyzszenie("2", "6", -1.220, 4));
		przewyzszenie.add(new GravPrzewyzszenie("2", "7", -6.118, 7));
		przewyzszenie.add(new GravPrzewyzszenie("3", "4", 2.193, 8));
		przewyzszenie.add(new GravPrzewyzszenie("3", "5", -1.507, 3));
		przewyzszenie.add(new GravPrzewyzszenie("3", "7", 0.752, 3));
		przewyzszenie.add(new GravPrzewyzszenie("4", "6", 3.462, 6));
		przewyzszenie.add(new GravPrzewyzszenie("1", "101", 14.132, 7));
		przewyzszenie.add(new GravPrzewyzszenie("5", "103", -1.155, 3));
		przewyzszenie.add(new GravPrzewyzszenie("6", "102", -13.141, 3));

		ArrayList<GravPunkt> punktyStale = new ArrayList<GravPunkt>();
		punktyStale.add(new GravPunkt("101", 123.6548));
		punktyStale.add(new GravPunkt("102", 112.0821));
		punktyStale.add(new GravPunkt("103", 116.9079));

		Wyrownanie wyrownanie = new Wyrownanie(punktyStale, przewyzszenie);
		wyrownanie.wyrownaj();
	}

	public ArrayList<GravPunkt> getPunktyStale() {
		return punktyStale;
	}

	public void setPunktyStale(ArrayList<GravPunkt> punktyStale) {
		this.punktyStale = punktyStale;
	}

	public ArrayList<GravPunkt> getPunktyDoWyrownania() {
		return punktyDoWyrownania;
	}

	public void setPunktyDoWyrownania(ArrayList<GravPunkt> punktyDoWyrownania) {
		this.punktyDoWyrownania = punktyDoWyrownania;
	}

	public ArrayList<GravPrzewyzszenie> getPrzewyzszenia() {
		return przewyzszenia;
	}

	public void setPrzewyzszenia(ArrayList<GravPrzewyzszenie> przewyzszenia) {
		this.przewyzszenia = przewyzszenia;
	}

}
