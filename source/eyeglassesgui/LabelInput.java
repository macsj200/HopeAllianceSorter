package eyeglassesgui;

import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class LabelInput extends JPanel{
	private VerifyTextField input;
	
	public LabelInput(String label, int inputWidth, boolean intify){
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		add(new JLabel(label));
		input = new VerifyTextField(inputWidth, intify);
		
		input.addFocusListener(new FocusListener(){

			@Override
			public void focusGained(FocusEvent arg0) {
				//don't do nothin'
			}

			@Override
			public void focusLost(FocusEvent arg0) {
				//don't do nothin'
			}
			
		});
		
		add(input);
	}

	public VerifyTextField getInput() {
		return input;
	}
	
	public void addActionListener(ActionListener al){
		input.addActionListener(al);
	}
}
