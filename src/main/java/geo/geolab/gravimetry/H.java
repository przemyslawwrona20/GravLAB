package geo.geolab.gravimetry;

public class H {
	private double heigh;

	public H(double heigh) {
		super();
		this.heigh = heigh;
	}

	public H(String heigh) {
		super();
		this.heigh = Double.parseDouble(heigh);
	}

	public double getHeigh() {
		return heigh;
	}

	public void setHeigh(double heigh) {
		this.heigh = heigh;
	}
}
