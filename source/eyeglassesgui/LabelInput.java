package eyeglassesgui;

import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class LabelInput extends JPanel{
	private JTextField input;
	private String label;
	
	public LabelInput(String label, int inputWidth, boolean intify){
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		this.label = label;
		add(new JLabel(label));
		input = new JTextField(inputWidth);
		
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
	
	public String getLabel(){
		return label;
	}
	
	public void addActionListener(ActionListener al){
		input.addActionListener(al);
	}
}
