package eyeglassesgui;

import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class LabelInputCheckbox extends JPanel{
	private VerifyTextField input;
	private JCheckBox checkBox;
	
	public LabelInputCheckbox(String label, int inputWidth){
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		checkBox = new JCheckBox();
		add(checkBox);
		add(new JLabel(label));
		input = new VerifyTextField(inputWidth);
		
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

	public JTextField getInput() {
		return input;
	}
	
	public void addActionListener(ActionListener al){
		input.addActionListener(al);
	}
	
	public JCheckBox getCheckBox(){
		return checkBox;
	}
}
