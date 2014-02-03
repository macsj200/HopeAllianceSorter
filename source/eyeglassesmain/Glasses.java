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
		
		raxis = raxis % 180;
		
		if(raxis < 0){
			raxis = raxis % 180 + 180;
		}
		
		laxis = laxis % 180;
		
		if(laxis < 0){
			laxis = laxis % 180 + 180;
		}

		if(rcyl == 0 || rcyl + 0.75 == 0){
			raxis = 0;
		}

		if(lcyl == 0 || lcyl + 0.75 == 0){
			laxis = 0;
		}

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
		return String.format("%d\t%.2f\t%.2f\t%d\t%.2f\t%.2f\t%d\t%s\t%s",number, Rsph, Rcyl, Raxis, Lsph, Lcyl, Laxis, frame, lens);
	}

	public int getNumber(){
		return number;
	}

	public double getRsph() {
		return Rsph;
	}

	public double getRcyl() {
		return Rcyl;
	}

	public int getRaxis() {
		return Raxis;
	}

	public double getLsph() {
		return Lsph;
	}

	public double getLcyl() {
		return Lcyl;
	}

	public int getLaxis() {
		return Laxis;
	}

	public String getFrame() {
		return frame;
	}

	public String getLens() {
		return lens;
	}
}
