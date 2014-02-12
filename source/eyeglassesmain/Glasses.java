package eyeglassesmain;

public class Glasses implements Comparable{
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
		
		laxis = laxis % 180;

		if(rcyl == 0){
			raxis = 0;
		}

		if(lcyl == 0){
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

	public int compareTo(Glasses glasses) {
		final int BEFORE = 1;
		final int EQUAL = 0;
		final int AFTER = -1;
		
		if(this == glasses){
			return EQUAL;
		}
		
		if(Rsph > glasses.getRsph()){
			return BEFORE;
		}
		else if(Rsph < glasses.getRsph()){
			return AFTER;
		}
		else{
			if(Lsph > glasses.getLsph()){
				return BEFORE;
			}
			else if(Lsph < glasses.getLsph()){
				return AFTER;
			}
			else{
				if(Raxis > glasses.getRaxis()){
					return BEFORE;
				}
				else if(Raxis < glasses.getRaxis()){
					return AFTER;
				}
				else{
					if(Laxis > glasses.getLaxis()){
						return BEFORE;
					}
					else if(Laxis < glasses.getLaxis()){
						return AFTER;
					}
					else{
						if(Rcyl > glasses.getRcyl()){
							return BEFORE;
						}
						else if(Rcyl < glasses.getRcyl()){
							return AFTER;
						}
						else{
							if(Lcyl > glasses.getLcyl()){
								return BEFORE;
							}
							else if(Lcyl < glasses.getLcyl()){
								return AFTER;
							}
							else{
								if(number > glasses.getNumber()){
									return BEFORE;
								}
								else if(number < glasses.getNumber()){
									return AFTER;
								}
								else{
									return EQUAL;
								}
							}
						}
					}
				}
			}
		}
	}

	@Override
	public int compareTo(Object o) {
		return compareTo((Glasses) o);
	}
}
