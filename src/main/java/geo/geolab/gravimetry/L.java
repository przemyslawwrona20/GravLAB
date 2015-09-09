package geo.geolab.gravimetry;

public class L {
	private int stopnie;
	private int minuty;
	private double sekundy;

	public L(int stopnie, int minuty, double sekundy) {
		super();
		this.stopnie = stopnie;
		this.minuty = minuty;
		this.sekundy = sekundy;
	}

	public L(String stopnie, String minuty, String sekundy) {
		super();
		this.stopnie = Integer.parseInt(stopnie);
		this.minuty = Integer.parseInt(minuty);
		this.sekundy = Double.parseDouble(sekundy);
	}

	public double parseToDouble() {
		return stopnie + minuty / 60 + sekundy / 3600;
	}

	public int getStopnie() {
		return stopnie;
	}

	public void setStopnie(int stopnie) {
		this.stopnie = stopnie;
	}

	public int getMinuty() {
		return minuty;
	}

	public void setMinuty(int minuty) {
		this.minuty = minuty;
	}

	public double getSekundy() {
		return sekundy;
	}

	public void setSekundy(double sekundy) {
		this.sekundy = sekundy;
	}

}
