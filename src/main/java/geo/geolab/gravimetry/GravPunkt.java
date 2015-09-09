package geo.geolab.gravimetry;

public class GravPunkt {
	private String nazwa;
	private Double value;

	public GravPunkt(String nazwa, Double value) {
		super();
		this.nazwa = nazwa;
		this.value = value;
	}

	public String getNazwa() {
		return nazwa;
	}

	public void setNazwa(String nazwa) {
		this.nazwa = nazwa;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	@Override
	public boolean equals(Object arg0) {
		GravPunkt gravPunkt = (GravPunkt) arg0;

		return this.nazwa.equals(gravPunkt.getNazwa());
	}

}
