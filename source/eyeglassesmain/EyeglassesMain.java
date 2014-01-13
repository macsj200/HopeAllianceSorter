package eyeglassesmain;

import eyeglassesgui.EyeglassesGui;

public class EyeglassesMain {
	public static void main(String[] args){
		String fileName = "/Users/mjohansen15/git/HopeAllianceSorter/sample/GuatemalaGlassesFebruary-20142.xls";
		
		EyeglassDatabase database = new EyeglassDatabase(fileName);
		
		new EyeglassesGui(database);
	}
}
