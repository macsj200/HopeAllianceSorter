package eyeglassesgui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Properties;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import eyeglassesmain.EyeglassDatabase;

public class EyeglassesGui extends JFrame{
	private JPanel inputPanel;
	private JPanel leftInputPanel;
	private JFileChooser fileChooser;
	private File file;
	private Properties prop;
	private JPanel rightInputPanel;
	private LabelInputCheckbox Rsph;
	private LabelInputCheckbox Rcyl;
	private LabelInputCheckbox Raxis;
	private LabelInputCheckbox Lsph;
	private LabelInputCheckbox Lcyl;
	private LabelInputCheckbox Laxis;
	private JButton searchButton;
	private TextOutputArea results;
	private EyeglassDatabase database;
	private ArrayList<Integer> resultList;

	public EyeglassesGui(){
		File configFile = new File(".config.properties");
		prop = new Properties();

		fileChooser = new JFileChooser();
		try {
			prop.load(new FileInputStream(configFile));

			fileChooser.setCurrentDirectory(new File(prop.getProperty("dir")));
		} catch (FileNotFoundException e) {

		} catch (IOException e) {

		} finally{
			file = promptForFile();
		}

		if(file == null){
			JOptionPane.showMessageDialog(this, "Couldn't open file.  Exiting.","Error", JOptionPane.ERROR_MESSAGE);
			System.exit(-1);
		}

		prop.setProperty("dir", file.getParent());

		try {
			prop.store(new FileOutputStream(".config.properties"), null);
		} catch (FileNotFoundException e) {

		} catch (IOException e) {

		}

		setLayout(new FlowLayout());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		this.setPreferredSize(new Dimension(380,450));

		initAndAddComponents();
		pack();
		setVisible(true);

		(new Thread(new Runnable(){
			public void run(){
				database = new EyeglassDatabase(file);

				SwingUtilities.invokeLater(new Runnable(){
					public void run(){
						searchButton.setEnabled(true);
					}
				});
			}
		})).start();

	}
	
	private class Searcher implements Runnable{
		public void run(){
			SwingUtilities.invokeLater(new Runnable(){
				public void run(){
					searchButton.setEnabled(false);
				}
			});
			resultList = searchAndCompare();
			SwingUtilities.invokeLater(new Runnable(){
				public void run(){
					results.clear();
					writeArrayList(resultList);
					searchButton.setEnabled(true);
				}
			});
		}
	}

	private void initAndAddComponents(){
		searchButton = new JButton("Search");
		searchButton.setEnabled(false);

		searchButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				(new Thread(new Searcher())).start();
			}
		});

		leftInputPanel = new JPanel();
		leftInputPanel.setLayout(new BoxLayout(leftInputPanel, BoxLayout.Y_AXIS));

		rightInputPanel = new JPanel();
		rightInputPanel.setLayout(new BoxLayout(rightInputPanel, BoxLayout.Y_AXIS));

		inputPanel = new JPanel();
		inputPanel.setLayout(new BoxLayout(inputPanel,BoxLayout.X_AXIS));

		int width = 8;

		Rsph = new LabelInputCheckbox("Rsph: ", width);
		Rcyl = new LabelInputCheckbox("Rcyl: ", width);
		Raxis = new LabelInputCheckbox("Raxis: ", width);
		Lsph = new LabelInputCheckbox("Lsph: ", width);
		Lcyl = new LabelInputCheckbox("Lcyl: ", width);
		Laxis = new LabelInputCheckbox("Laxis: ", width);

		results = new TextOutputArea(20, 20);

		rightInputPanel.add(Rsph);
		rightInputPanel.add(Rcyl);
		rightInputPanel.add(Raxis);
		leftInputPanel.add(Lsph);
		leftInputPanel.add(Lcyl);
		leftInputPanel.add(Laxis);

		inputPanel.add(leftInputPanel);
		inputPanel.add(rightInputPanel);

		getContentPane().add(inputPanel);
		getContentPane().add(results);
		getContentPane().add(searchButton);
	}

	public File promptForFile(){
		if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION){
			return fileChooser.getSelectedFile();
		}
		return null;
	}

	public ArrayList<Integer> searchAndCompare(){
		ArrayList<Integer> RsphResults = null;
		ArrayList<Integer> RcylResults = null;
		ArrayList<Integer> RsphAndRcylResults = null;
		ArrayList<Integer> RaxisResults = null;
		ArrayList<Integer> LsphResults = null;
		ArrayList<Integer> LcylResults = null;
		ArrayList<Integer> LsphAndLcylResults = null;
		ArrayList<Integer> LaxisResults = null;
		ArrayList<ArrayList<Integer>> lists = new ArrayList<ArrayList<Integer>>();
		ArrayList<Integer> results = null;

		if(Rsph.getCheckBox().isSelected() && Rcyl.getCheckBox().isSelected()){
			RsphAndRcylResults = database.searchRsphAndRcyl(Double.valueOf(Rsph.getInput().getText()), Double.valueOf(Rcyl.getInput().getText()));
		}
		else if (Rsph.getCheckBox().isSelected()){
			RsphResults = database.searchRsph(Double.valueOf(Rsph.getInput().getText()));
		}
		else if (Rcyl.getCheckBox().isSelected()){
			RcylResults = database.searchRcyl(Double.valueOf(Rcyl.getInput().getText()));
		}

		if(Raxis.getCheckBox().isSelected()){
			RaxisResults = database.searchRaxis(Integer.valueOf(Raxis.getInput().getText()));
		}


		if(Lsph.getCheckBox().isSelected() && Lcyl.getCheckBox().isSelected()){
			LsphAndLcylResults = database.searchLsphAndLcyl(Double.valueOf(Lsph.getInput().getText()), Double.valueOf(Lcyl.getInput().getText()));
		}
		else if (Lsph.getCheckBox().isSelected()){
			LsphResults = database.searchLsph(Double.valueOf(Lsph.getInput().getText()));
		}
		else if (Lcyl.getCheckBox().isSelected()){
			LcylResults = database.searchLcyl(Double.valueOf(Lcyl.getInput().getText()));
		}

		if(Laxis.getCheckBox().isSelected()){
			LaxisResults = database.searchLaxis(Integer.valueOf(Laxis.getInput().getText()));
		}

		lists.add(RsphResults);
		lists.add(RcylResults);
		lists.add(RsphAndRcylResults);
		lists.add(RaxisResults);
		lists.add(LsphResults);
		lists.add(LcylResults);
		lists.add(LsphAndLcylResults);
		lists.add(LaxisResults);

		results = compareArrayLists(lists);

		return results;
	}

	public void writeArrayList(ArrayList<Integer> list){
		for(int i = 0; i < list.size(); i++){
			results.write(list.get(i) + "\n");
		}
	}

	public ArrayList<Integer> compareArrayLists(ArrayList<ArrayList<Integer>> list){
		ArrayList<Integer> results = new ArrayList<Integer>();
		list.removeAll(Collections.singleton(null));

		if(list.size() == 1){
			return list.get(0);
		}

		int indexOfLargest = 0;

		for(int i = 0; i < list.size(); i++){
			if(list.get(i).size() > list.get(indexOfLargest).size()){
				indexOfLargest = i;
			}
		}

		for(int i = 0; i < list.size(); i++){
			if(i == indexOfLargest){
				continue;
			}

			for(int j = 0; j < list.get(indexOfLargest).size(); j++){
				if(list.get(i).contains(list.get(indexOfLargest).get(j))){
					results.add(list.get(indexOfLargest).get(j));
				}
			}
		}

		return results;
	}
}