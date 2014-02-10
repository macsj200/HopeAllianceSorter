package eyeglassesmain;

import eyeglassesgui.EyeglassesGui;

public class EyeglassesMain {
	public static final String VERSION = "1.25";

	public static void main(String[] args){
		new EyeglassesGui();
	}

	public static void log(Object o, boolean error){
		if(error){
			System.err.println(o);
		}
		else{
			System.out.println(o);
		}
	}
	
	public static void log(Object o){
		log(o, false);
	}
}
