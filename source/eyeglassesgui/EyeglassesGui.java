package eyeglassesgui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import eyeglassesmain.EyeglassDatabase;

public class EyeglassesGui extends JFrame{
	private InputAndLabel Rsph;
	private InputAndLabel Rcyl;
	private InputAndLabel Raxis;
	private InputAndLabel Lsph;
	private InputAndLabel Lcyl;
	private InputAndLabel Laxis;
	private JButton searchButton;
	private TextOutputArea results;
	private EyeglassDatabase database;
	private ArrayList<Integer> hits;
	
	public EyeglassesGui(EyeglassDatabase database){
		this.database = database;
		
		setLayout(new FlowLayout());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		this.setPreferredSize(new Dimension(800,200));
		
		initAndAddComponents();
		pack();
		setVisible(true);
	}
	
	private void initAndAddComponents(){
		searchButton = new JButton("Search");
		
		searchButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				if(!(isEmpty(Rsph.getInput()))){
					hits = database.searchRsph((Double.valueOf(Rsph.getInput().getText())));
					for(int i = 0; i < hits.size(); i++){
						results.write(hits.get(i).toString() + "\n");
					}
				}
			}
		});
		
		int width = 8;
		
		Rsph = new InputAndLabel("Rsph: ", width);
		Rcyl = new InputAndLabel("Rcyl: ", width);
		Raxis = new InputAndLabel("Raxis: ", width);
		Lsph = new InputAndLabel("Lsph: ", width);
		Lcyl = new InputAndLabel("Lcyl: ", width);
		Laxis = new InputAndLabel("Laxis: ", width);
		
		results = new TextOutputArea(20, 20);
		
		getContentPane().add(Rsph);
		getContentPane().add(Rcyl);
		getContentPane().add(Raxis);
		getContentPane().add(Lsph);
		getContentPane().add(Lcyl);
		getContentPane().add(Laxis);
		getContentPane().add(results);
		getContentPane().add(searchButton);
	}
	
	public boolean isEmpty(JTextField area){
		return area.getText().equals("");
	}
}
