package eyeglassesmain;

import javax.swing.JOptionPane;

import eyeglassesgui.EyeglassesGui;

public class EyeglassesMain {
	public static final String VERSION = "2.41";

	public static void main(String[] args){
		new EyeglassesGui();
	}

	public static void log(Object o, boolean error){
		if(error){
			JOptionPane.showMessageDialog(null, o,"Error", JOptionPane.ERROR_MESSAGE);
			
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
