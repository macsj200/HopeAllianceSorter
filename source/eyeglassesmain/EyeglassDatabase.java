package eyeglassesmain;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

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

		while(searches <= 4 && hits.size() <= 15){
			searches++;
			possibles = new ArrayList<Glasses>();

			for(int i = 0; i < glasses.size(); i++){
				if(!(glasses.get(i).getRcyl() >= rcyl)){
					continue;
				}
				if(!(glasses.get(i).getLcyl() >= lcyl)){
					continue;
				}
				if(!(checkAxis(glasses.get(i).getRaxis(), raxis, 30))){
					continue;
				}
				if(!(checkAxis(glasses.get(i).getLaxis(), laxis, 30))){
					continue;
				}

				possibles.add(glasses.get(i));
			}

			Iterator<Glasses> iter = possibles.iterator();
			
			while(iter.hasNext()){
				Glasses glasses = iter.next();
				
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
						iter.remove();
						continue;
					}
				}
				if(rsph > 0){
					if(!(glasses.getRsph() == rsph || 
							glasses.getRsph() == rsph - margin)){
						iter.remove();
						continue;
					}
				}

				if(lsph < 0){
					if(!(glasses.getLsph() == lsph || 
							glasses.getLsph() == lsph + margin)){
						iter.remove();
						continue;
					}
				}
				if(lsph > 0){
					if(!(glasses.getLsph() == lsph || 
							glasses.getLsph() == lsph - margin)){
						iter.remove();
						continue;
					}
				}
			}



			//hits.addAll(possibles);
			for(Glasses glasses : possibles){
				if(!hits.contains(glasses)){
					hits.add(glasses);
				}
			}
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
