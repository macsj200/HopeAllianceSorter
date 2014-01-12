package eyeglassesmain;

import java.util.ArrayList;

public class EyeglassDatabase {
	private ArrayList<Glasses> glasses;

	public EyeglassDatabase(String fileName){
		glasses = Reader.readFile(fileName);
	}
	
	public ArrayList<Glasses> getGlasses(){
		return glasses;
	}

	public ArrayList<Integer> searchRsph(double rsph){
		ArrayList<Integer> hits = new ArrayList<Integer>();
		for(int i = 0; i < glasses.size(); i++){
			if(rsph == 0){
				if(isBetween(glasses.get(i).getRsph(), rsph - 0.25, rsph + 0.25, true)){
					hits.add(glasses.get(i).getNumber());
				}
			}
			else if(rsph > 0){
				if(isBetween(glasses.get(i).getRsph(), rsph - 0.50, rsph, true)){
					hits.add(glasses.get(i).getNumber());
				}
			}
		}
		return hits;
	}

	public ArrayList<Integer> searchRsphAndRcyl(double rsph, double rcyl){
		ArrayList<Integer> hits = new ArrayList<Integer>();
		if(rsph < 0){
			for(int i = 0; i < glasses.size(); i++){
				if(isBetween(rcyl, -1.8, -0.8, true)){
					if(isBetween(glasses.get(i).getRsph(), rsph - 0.25, rsph + 0.5, true)){
						hits.add(glasses.get(i).getNumber());
					}
				}
				else if(rcyl > -0.8){
					if(isBetween(glasses.get(i).getRsph(), rsph, rsph + 0.5, true)){
						hits.add(glasses.get(i).getNumber());
					}
				}
				else if(rcyl < -1.8){
					if(isBetween(glasses.get(i).getRsph(), rsph - .5, rsph + .5, true)){
						hits.add(glasses.get(i).getNumber());
					}
				}
			}
		}
		return hits;
	}
	
	public ArrayList<Integer> searchRcyl(double rcyl){
		ArrayList<Integer> hits = new ArrayList<Integer>();
		for(int i = 0; i < glasses.size(); i++){
			if(isBetween(glasses.get(i).getRcyl(), rcyl, 0, true)){
				hits.add(glasses.get(i).getNumber());
			}
		}
		return hits;
	}
	
	public ArrayList<Integer> searchRaxis(int raxis){
		ArrayList<Integer> hits = new ArrayList<Integer>();
		for(int i = 0; i < glasses.size(); i++){
			//what?
		}
		return hits;
	}
	
	
	public ArrayList<Integer> searchLsph(double lsph){
		ArrayList<Integer> hits = new ArrayList<Integer>();
		for(int i = 0; i < glasses.size(); i++){
			if(lsph == 0){
				if(isBetween(glasses.get(i).getLsph(), lsph - 0.25, lsph + 0.25, true)){
					hits.add(glasses.get(i).getNumber());
				}
			}
			else if(lsph > 0){
				if(isBetween(glasses.get(i).getLsph(), lsph - 0.50, lsph, true)){
					hits.add(glasses.get(i).getNumber());
				}
			}
		}
		return hits;
	}

	public ArrayList<Integer> searchLsphAndLcyl(double lsph, double lcyl){
		ArrayList<Integer> hits = new ArrayList<Integer>();
		if(lsph < 0){
			for(int i = 0; i < glasses.size(); i++){
				if(isBetween(lcyl, -1.8, -0.8, true)){
					if(isBetween(glasses.get(i).getLsph(), lsph - 0.25, lsph + 0.5, true)){
						hits.add(glasses.get(i).getNumber());
					}
				}
				else if(lcyl > -0.8){
					if(isBetween(glasses.get(i).getLsph(), lsph, lsph + 0.5, true)){
						hits.add(glasses.get(i).getNumber());
					}
				}
				else if(lcyl < -1.8){
					if(isBetween(glasses.get(i).getLsph(), lsph - .5, lsph + .5, true)){
						hits.add(glasses.get(i).getNumber());
					}
				}
			}
		}
		return hits;
	}
	
	public ArrayList<Integer> searchLcyl(double lcyl){
		ArrayList<Integer> hits = new ArrayList<Integer>();
		for(int i = 0; i < glasses.size(); i++){
			if(isBetween(glasses.get(i).getLcyl(), lcyl, 0, true)){
				hits.add(glasses.get(i).getNumber());
			}
		}
		return hits;
	}
	
	public ArrayList<Integer> searchLaxis(int laxis){
		ArrayList<Integer> hits = new ArrayList<Integer>();
		for(int i = 0; i < glasses.size(); i++){
			//what?
		}
		return hits;
	}

	public boolean isBetween(double num, double low, double high, boolean inclusive){
		if(inclusive){
			return (num >= low) && (num <= high);
		} else{
			return (num > low) && (num < high);
		}
	}
}
