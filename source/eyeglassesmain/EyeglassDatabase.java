package eyeglassesmain;

import java.io.File;
import java.util.ArrayList;

public class EyeglassDatabase {
	private ArrayList<Glasses> glasses;

	public EyeglassDatabase(File file){
		glasses = Reader.readFile(file);
		System.out.println("Database read into RAM");
	}

	public ArrayList<Glasses> getGlasses(){
		return glasses;
	}
	
	public ArrayList<Glasses> search(double rsph, double rcyl, int raxis, double lsph,
			double lcyl, int laxis){
		ArrayList<Glasses> hits = new ArrayList<Glasses>();

		for(int i = 0; i < glasses.size(); i++){
			if(!isBetween(glasses.get(i).getRcyl(), rcyl, 0, true)){
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


			if(raxis > 180){
				raxis = raxis - 180;
			} else if(raxis < 0){
				raxis = raxis + 180;
			} else if(raxis == 0){
				raxis = 180;
			}

			if(!isBetween(glasses.get(i).getRaxis(), raxis - 20, raxis + 20, true)){
				continue;
			}
			
			if(!isBetween(glasses.get(i).getLcyl(), lcyl, 0, true)){
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

			if(laxis > 180){
				laxis = laxis - 180;
			} else if(laxis < 0){
				laxis = laxis + 180;
			} else if(laxis == 0){
				laxis = 180;
			}

			if(!isBetween(glasses.get(i).getLaxis(), laxis - 20, laxis + 20, true)){
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
}
