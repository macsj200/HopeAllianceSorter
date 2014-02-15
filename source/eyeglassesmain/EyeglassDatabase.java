package eyeglassesmain;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

public class EyeglassDatabase {
	private ArrayList<Glasses> glasses;

	public EyeglassDatabase(File file) throws FormattingException{
		try {
			glasses = Reader.readFile(file);
		} catch (FormattingException e) {
			throw e;
		}
		EyeglassesMain.log("Database read into RAM");
	}

	public ArrayList<Glasses> getGlasses(){
		return glasses;
	}

	/*
	 * 
	 * This is the main search routine for glasses
	 * 
	 * Searches through read-in database and returns that array filtered
	 * 
	 */
	public ArrayList<Glasses> search(double rsph, double rcyl, int raxis, double lsph,
			double lcyl, int laxis){
		ArrayList<Glasses> hits = new ArrayList<Glasses>();

		//Iterate through existing glasses
		for(int i = 0; i < glasses.size(); i++){

			/*If rcyl of glasses is not between rcyl parameter and rcyl parameter + 0.75 filter out*/
			if(!isBetween(glasses.get(i).getRcyl(), rcyl, rcyl + 0.75)){
				continue;
			}

			if(rsph == 0){
				/*if rsph search parameter is zero and glasses rsph is not between rsph parameter +/- 0.25 filter out*/
				if(!isBetween(glasses.get(i).getRsph(), rsph - 0.25, rsph + 0.25)){
					continue;
				}
			}

			if(rsph > 0){
				/*if rsph search parameter is greater than zero and glasses rsph is not between rsph parameter and
				 * rsph parameter - 0.5 filter out
				 */
				if(!isBetween(glasses.get(i).getRsph(), rsph - 0.5, rsph)){
					continue;
				}
			}

			if(rsph < 0){

				if(isBetween(rcyl, -1.8, -0.8)){
					if(!isBetween(glasses.get(i).getRsph(), rsph - 0.25, rsph + 0.5)){
						continue;
					}
				}
				else if(rcyl > -0.8){
					if(!isBetween(glasses.get(i).getRsph(), rsph, rsph + 0.5)){
						continue;
					}
				}
				else if(rcyl < -1.8){
					if(!isBetween(glasses.get(i).getRsph(), rsph - .5, rsph + .5)){
						continue;
					}
				}
			}

			raxis = raxis % 180;

			if(rcyl == 0 || rcyl + 0.25 == 0 || rcyl + 0.5 == 0 || rcyl + 0.75 == 0){
				if((glasses.get(i).getRaxis()) != 0){
					if(raxis + 20 > 180){
						if(!(glasses.get(i).getRaxis() <= raxis + 20 - 180 || glasses.get(i).getRaxis() >= raxis - 20)){
							continue;
						}
					}
					else if(raxis - 20 < 0){
						if(!(glasses.get(i).getRaxis() <= raxis + 20 || glasses.get(i).getRaxis() >= raxis - 20 + 180)){
							continue;
						}
					}
					else{
						if(!isBetween(glasses.get(i).getRaxis(), raxis - 20, raxis + 20)){
							continue;
						}
					}
				}
			}



			if(!isBetween(glasses.get(i).getLcyl(), lcyl, lcyl + 0.75)){
				continue;
			}

			if(lsph == 0){
				if(!isBetween(glasses.get(i).getLsph(), lsph - 0.25, lsph + 0.25)){
					continue;
				}
			}

			if(lsph > 0){
				if(!isBetween(glasses.get(i).getLsph(), lsph - 0.5, lsph)){
					continue;
				}
			}


			if(lsph < 0){

				if(isBetween(lcyl, -1.8, -0.8)){
					if(!isBetween(glasses.get(i).getLsph(), lsph - 0.25, lsph + 0.5)){
						continue;
					}
				}
				else if(lcyl > -0.8){
					if(!isBetween(glasses.get(i).getLsph(), lsph, lsph + 0.5)){
						continue;
					}
				}
				else if(lcyl < -1.8){
					if(!isBetween(glasses.get(i).getLsph(), lsph - .5, lsph + .5)){
						continue;
					}
				}
			}

			laxis = laxis % 180;

			if(lcyl == 0 || lcyl + 0.25 == 0 || lcyl + 0.5 == 0 || lcyl + 0.75 == 0){
				if(glasses.get(i).getLaxis() != 0){
					if(laxis + 20 > 180){
						if(!(glasses.get(i).getLaxis() <= laxis + 20 - 180 || glasses.get(i).getLaxis() >= laxis - 20)){
							continue;
						}
					}
					else if(laxis - 20 < 0){
						if(!(glasses.get(i).getLaxis() <= laxis + 20 || glasses.get(i).getLaxis() >= laxis - 20 + 180)){
							continue;
						}
					}
					else{
						if(!isBetween(glasses.get(i).getLaxis(), laxis - 20, laxis + 20)){
							continue;
						}
					}
				}
			}

			hits.add(glasses.get(i));
		}

		Collections.sort(hits);

		return hits;
	}

	public ArrayList<Glasses> search(String rsph, String rcyl, String raxis, String lsph,
			String lcyl, String laxis){
		return search(Double.valueOf(rsph), Double.valueOf(rcyl), Integer.valueOf(raxis), 
				Double.valueOf(lsph), Double.valueOf(lcyl), Integer.valueOf(laxis));
	}

	public boolean isBetween(double num, double low, double high){
		return (num >= low) && (num <= high);
	}
}
