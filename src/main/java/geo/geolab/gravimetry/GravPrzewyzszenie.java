package geo.geolab.gravimetry;

public class GravPrzewyzszenie {
	private String source;
	private String dest;
	private double value;
	private double blad;

	public GravPrzewyzszenie(String source, String dest, double value,
			double blad) {
		super();
		this.source = source;
		this.dest = dest;
		this.value = value;
		this.blad = blad;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getDest() {
		return dest;
	}

	public void setDest(String dest) {
		this.dest = dest;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public double getBlad() {
		return blad;
	}

	public void setBlad(double blad) {
		this.blad = blad;
	}

}
