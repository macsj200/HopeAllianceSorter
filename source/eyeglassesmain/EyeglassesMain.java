package eyeglassesmain;

public class EyeglassesMain {
	public static void main(String[] args){
		String fileName = "C:\\Users\\Max\\Documents\\GitHub\\HopeAllianceSorter\\sample\\GuatemalaGlassesFebruary-20142.xls";
		
		System.out.println(Reader.readFile(fileName)[0]);
	}
}
