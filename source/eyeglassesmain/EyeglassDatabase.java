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

		int searches = 0;

		ArrayList<Glasses> possibles = null;
		ArrayList<Glasses> initialPossibles = null;

		while(searches <= 4 && hits.size() <= 15){
			searches++;
			possibles = new ArrayList<Glasses>();
			initialPossibles = new ArrayList<Glasses>();

			for(int i = 0; i < glasses.size(); i++){
				if(!(glasses.get(i).getRcyl() >= rcyl)){
					continue;
				}
				if(!(glasses.get(i).getLcyl() >= lcyl)){
					continue;
				}
				if(!(checkAxis(glasses.get(i).getRaxis(), raxis, 30) || (glasses.get(i).getRaxis() == 0))){
					continue;
				}
				if(!(checkAxis(glasses.get(i).getLaxis(), laxis, 30) || (glasses.get(i).getLaxis() == 0))){
					continue;
				}

				initialPossibles.add(glasses.get(i));
			}

			for(Glasses glasses : initialPossibles){
				double margin = 0;
				if(searches == 1){
					margin = .25;
				}
				else if(searches == 2){
					margin = .5;
				}
				else if(searches == 3){
					margin = .75;
				}
				else{
					margin = 1;
				}
				
				if(rsph < 0){
					if(!(glasses.getRsph() == rsph || 
							glasses.getRsph() == rsph + margin)){
						continue;
					}
				}
				if(rsph > 0){
					if(!(glasses.getRsph() == rsph || 
							glasses.getRsph() == rsph - margin)){
						continue;
					}
				}

				if(lsph < 0){
					if(!(glasses.getLsph() == lsph || 
							glasses.getLsph() == lsph + margin)){
						continue;
					}
				}
				if(lsph > 0){
					if(!(glasses.getLsph() == lsph || 
							glasses.getLsph() == lsph - margin)){
						continue;
					}
				}

				possibles.add(glasses);
			}



			hits.addAll(possibles);
		}

		Collections.sort(hits);

		return hits;
	}

	public ArrayList<Glasses> search(String rsph, String rcyl, String raxis, String lsph,
			String lcyl, String laxis){
		return search(Double.valueOf(rsph), Double.valueOf(rcyl), Integer.valueOf(raxis), 
				Double.valueOf(lsph), Double.valueOf(lcyl), Integer.valueOf(laxis));
	}

	public boolean checkAxis(int axis1, int axis2, int range){
		boolean isWithinRange = false;
		axis1 = axis1 % 180;
		axis2 = axis2 % 180;

		if(axis2 + range > 180){
			isWithinRange = isWithinRange && 
					(axis1 <= axis2 + range - 180 || axis1 >= axis2 - range);
		}
		else if(axis2 - range < 0){
			isWithinRange = isWithinRange && 
					(axis1 <= axis2 + range || axis1 >= axis2 - range + 180);
		}
		else{
			isWithinRange = isWithinRange &&
					(axis1 >= axis2 - range && axis1 <= axis2 + range);
		}

		return isWithinRange;
	}

	public boolean isBetween(double num, double low, double high){
		return (num >= low) && (num <= high);
	}
}
