package eyeglassesgui;

import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;

@SuppressWarnings("serial")
public class TextOutputArea extends Box implements GuiWritable {
	private JTextPane outputArea = null;
	private JScrollPane scrollPane = null;
	private StyledDocument doc = null;

	public TextOutputArea() {
		super(BoxLayout.X_AXIS);
		outputArea = new JTextPane();
		outputArea.setEditable(false);
		
		scrollPane = new JScrollPane(outputArea);
		
		scrollPane.setPreferredSize(new Dimension(1000, 400));
		
		doc = outputArea.getStyledDocument();

		add(scrollPane);
	}

	@Override
	public void write(String s) {
		try {
			doc.insertString(doc.getLength(), s, null);
		} catch (BadLocationException e) {
			System.out.println("bad loc");
		}
	}
	
	public void clear(){
		outputArea.setText("");
	}
}
