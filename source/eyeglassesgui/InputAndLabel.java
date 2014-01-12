package eyeglassesgui;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class InputAndLabel extends JPanel{
	private JTextField input;
	
	public InputAndLabel(String label, int inputWidth){
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		add(new JLabel(label));
		input = new JTextField(inputWidth);
		add(input);
	}

	public JTextField getInput() {
		return input;
	}
}
