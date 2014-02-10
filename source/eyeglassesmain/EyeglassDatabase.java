package eyeglassesmain;

import java.io.File;
import java.util.ArrayList;

public class EyeglassDatabase {
	private ArrayList<Glasses> glasses;

	public EyeglassDatabase(File file) throws FormattingException{
		try {
			glasses = Reader.readFile(file);
		} catch (FormattingException e) {
			throw e;
		}
		System.out.println("Database read into RAM");
	}

	public ArrayList<Glasses> getGlasses(){
		return glasses;
	}

	public ArrayList<Glasses> search(double rsph, double rcyl, int raxis, double lsph,
			double lcyl, int laxis){
		ArrayList<Glasses> hits = new ArrayList<Glasses>();

		for(int i = 0; i < glasses.size(); i++){
			if(!isBetween(glasses.get(i).getRcyl(), rcyl, rcyl + 0.75, true)){
				continue;
			}

			if(rsph == 0){
				if(!isBetween(glasses.get(i).getRsph(), rsph - 0.25, rsph + 0.25, true)){
					continue;
				}
			}

			if(rsph > 0){
				if(!isBetween(glasses.get(i).getRsph(), rsph - 0.5, rsph, true)){
					continue;
				}
			}

			if(rsph < 0){

				if(isBetween(rcyl, -1.8, -0.8, true)){
					if(!isBetween(glasses.get(i).getRsph(), rsph - 0.25, rsph + 0.5, true)){
						continue;
					}
				}
				else if(rcyl > -0.8){
					if(!isBetween(glasses.get(i).getRsph(), rsph, rsph + 0.5, true)){
						continue;
					}
				}
				else if(rcyl < -1.8){
					if(!isBetween(glasses.get(i).getRsph(), rsph - .5, rsph + .5, true)){
						continue;
					}
				}
			}

			raxis = raxis % 180;

			if(rcyl == 0){
				raxis = 0;
			}

			if(!degreeIsBetween(glasses.get(i).getRaxis() + 180, raxis - 20 + 180, raxis + 20 + 180, glasses.get(i).getNumber())){
				continue;
			}

			if(!isBetween(glasses.get(i).getLcyl(), lcyl, lcyl + 0.75, true)){
				continue;
			}

			if(lsph == 0){
				if(!isBetween(glasses.get(i).getLsph(), lsph - 0.25, lsph + 0.25, true)){
					continue;
				}
			}

			if(lsph > 0){
				if(!isBetween(glasses.get(i).getLsph(), lsph - 0.5, lsph, true)){
					continue;
				}
			}


			if(lsph < 0){

				if(isBetween(lcyl, -1.8, -0.8, true)){
					if(!isBetween(glasses.get(i).getLsph(), lsph - 0.25, lsph + 0.5, true)){
						continue;
					}
				}
				else if(lcyl > -0.8){
					if(!isBetween(glasses.get(i).getLsph(), lsph, lsph + 0.5, true)){
						continue;
					}
				}
				else if(lcyl < -1.8){
					if(!isBetween(glasses.get(i).getLsph(), lsph - .5, lsph + .5, true)){
						continue;
					}
				}
			}

			laxis = laxis % 180;

			if(lcyl == 0){
				laxis = 0;
			}

			if(!degreeIsBetween(glasses.get(i).getLaxis() + 180, laxis - 20 + 180, laxis + 20 + 180, glasses.get(i).getNumber())){
				continue;
			}

			hits.add(glasses.get(i));
		}

		return hits;
	}

	public ArrayList<Glasses> search(String rsph, String rcyl, String raxis, String lsph,
			String lcyl, String laxis){
		return search(Double.valueOf(rsph), Double.valueOf(rcyl), Integer.valueOf(raxis), 
				Double.valueOf(lsph), Double.valueOf(lcyl), Integer.valueOf(laxis));
	}

	public boolean isBetween(double num, double low, double high, boolean inclusive){
		if(inclusive){
			return (num >= low) && (num <= high);
		} else{
			return (num > low) && (num < high);
		}
	}

	public boolean degreeIsBetween(int deg, int low, int high, int glassesnum){
		low = low % 180;
		high = high % 180;

		if(low < 0){
			low = low + 180;
		}

		if(high < 0){
			high = high + 180;
		}

		if(high > low){
			if(deg >= low && deg <= high){
				return true;
			} else{
				return false;
			}
		} else{
			if(deg <= high && deg <= low){
				return true;
			} else{
				return false;
			}
		}


	}
}
