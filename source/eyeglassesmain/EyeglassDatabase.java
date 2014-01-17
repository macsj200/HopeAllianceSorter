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
	public ArrayList<Integer> search(double rsph, double rcyl, int raxis, double lsph,
			double lcyl, int laxis){
		ArrayList<Integer> hits = new ArrayList<Integer>();

		for(int i = 0; i < glasses.size(); i++){
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
			else{

				if(!isBetween(glasses.get(i).getRcyl(), rcyl, 0, true)){
					continue;
				}

			}

			raxis = raxis % 180;

			if(!isBetween(glasses.get(i).getRaxis(), raxis - 20, raxis + 20, true)){
				continue;
			}


			if(lsph < 0){

				if(!isBetween(lcyl, -1.8, -0.8, true)){
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
			else{

				if(!isBetween(glasses.get(i).getLcyl(), lcyl, 0, true)){
					continue;
				}
			}

			laxis = laxis % 180;

			if(!isBetween(glasses.get(i).getLaxis(), laxis - 20, laxis + 20, true)){
				continue;
			}
			
			hits.add(glasses.get(i).getNumber());
		}

		return hits;
	}

	public ArrayList<Integer> search(String rsph, String rcyl, String raxis, String lsph,
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
