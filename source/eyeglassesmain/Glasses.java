package eyeglassesmain;

public class Glasses {
	private final int number;
	private final double Rsph;
	private final double Rcyl;
	private final int Raxis;
	private final double Lsph;
	private final double Lcyl;
	private final int Laxis;
	private final String frame;
	private final String lens;
	
	public Glasses(int Number, double rsph, double rcyl, int raxis, double lsph,
			double lcyl, int laxis, String frame, String lens) {
		super();
		number = Number;
		Rsph = rsph;
		Rcyl = rcyl;
		Raxis = raxis;
		Lsph = lsph;
		Lcyl = lcyl;
		Laxis = laxis;
		this.frame = frame;
		this.lens = lens;
	}
	
	@Override
	public String toString(){
		return String.format("%d,%f,%f,%d,%f,%f,%d,%s,%s",number, Rsph, Rcyl, Raxis, Rsph, Rcyl, Raxis, frame, lens);
	}
	
	public double getNumber(){
		return number;
	}

	public double getRsph() {
		return Rsph;
	}

	public double getRcyl() {
		return Rcyl;
	}

	public double getRaxis() {
		return Raxis;
	}

	public double getLsph() {
		return Lsph;
	}

	public double getLcyl() {
		return Lcyl;
	}

	public double getLaxis() {
		return Laxis;
	}

	public String getFrame() {
		return frame;
	}

	public String getLens() {
		return lens;
	}
}
