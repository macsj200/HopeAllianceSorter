package eyeglassesgui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class VerifyTextField extends JTextField {
	private boolean legit = true;
	private Color defaultColor;
	private boolean intified;

	public VerifyTextField(int width, boolean Intified){
		super(width);
		
		intified = Intified;
		
		defaultColor = getBackground();

		getDocument().addDocumentListener(new DocumentListener(){

			@Override
			public void changedUpdate(DocumentEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void insertUpdate(DocumentEvent arg0) {
				if(isLegit() || getText().equals("")){
					setBackground(defaultColor);
				} else{
					setBackground(Color.red);
				}
			}

			@Override
			public void removeUpdate(DocumentEvent arg0) {
				if(isLegit() || getText().equals("")){
					setBackground(defaultColor);
				} else{
					setBackground(Color.red);
				}
			}

		});
	}

	public boolean isLegit(){
		try{
			if(!intified){
				Double.valueOf(getText());
			}
			else{
				Integer.valueOf(getText());
			}
			legit = true;
		} catch (NumberFormatException e){
			legit = false;
		}
		return legit;
	}
}
