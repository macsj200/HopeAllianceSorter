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

	public VerifyTextField(int width){
		super(width);
		
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
			Double.valueOf(getText());
			legit = true;
		} catch (NumberFormatException e){
			legit = false;
		}
		return legit;
	}

	public VerifyTextField(){
		this(8);
	}
}
