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
import javax.swing.JLabel;
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
	private LabelInput Rsph;
	private LabelInput Rcyl;
	private LabelInput Raxis;
	private LabelInput Lsph;
	private LabelInput Lcyl;
	private LabelInput Laxis;
	private JButton searchButton;
	private TextOutputArea results;
	private EyeglassDatabase database;
	private ArrayList<Integer> resultList;
	private JLabel numberOfResultsLabel;
	private JPanel outputPanel;

	public EyeglassesGui(){
		File configFile = new File(".config.properties");
		prop = new Properties();

		fileChooser = new JFileChooser();
		try {
			prop.load(new FileInputStream(configFile));
			System.out.println("Config file exists, reading");
			System.out.println("Starting in " + prop.getProperty("dir"));
			fileChooser.setCurrentDirectory(new File(prop.getProperty("dir")));
		} catch (FileNotFoundException e) {
			System.out.println("Couldn't find config file.  Loading default");
		} catch (IOException e) {
			System.out.println("Couldn't load config file.  Loading default");
		} finally{
			file = promptForFile();
			System.out.println("Sucessfully opened file");
		}

		if(file == null){
			System.out.println("File selection failed");
			JOptionPane.showMessageDialog(this, "Couldn't open file.  Exiting.","Error", JOptionPane.ERROR_MESSAGE);
			System.exit(-1);
		}

		System.out.println("Writing last directory to config: " + file.getParent());
		prop.setProperty("dir", file.getParent());


		try {
			prop.store(new FileOutputStream(".config.properties"), null);
			System.out.println("Stored config");
		} catch (FileNotFoundException e) {
			System.out.println("Couldn't save config");
		} catch (IOException e) {
			System.out.println("Couldn't save config");
		}

		setLayout(new FlowLayout());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		this.setPreferredSize(new Dimension(420,520));

		initAndAddComponents();
		pack();
		setVisible(true);

		(new Thread(new Runnable(){
			public void run(){
				long time = System.currentTimeMillis();
				database = new EyeglassDatabase(file);
				time = System.currentTimeMillis() - time;
				System.out.println("read database in " + time + "ms");

				SwingUtilities.invokeLater(new Runnable(){
					public void run(){
						searchButton.setEnabled(true);
					}
				});
			}
		})).start();

	}

	public class Searcher implements Runnable{
		public void run(){
			SwingUtilities.invokeLater(new Runnable(){
				public void run(){
					searchButton.setEnabled(false);
				}
			});
			boolean worked = false;
			
			long time;
			try{
				time = System.currentTimeMillis();
				resultList = searchAndCompare();
				time = System.currentTimeMillis() - time;
				System.out.println("Search completed in " + time + "ms." + "  Results: " + resultList.size());
				worked = true;
			} catch (NumberFormatException e){
				System.out.println("Couldn't process search parameters.");
				JOptionPane.showMessageDialog(null, "Couldn't process search parameters.","Error", JOptionPane.ERROR_MESSAGE);
			}

			if(worked){
				SwingUtilities.invokeLater(new Runnable(){
					public void run(){
						results.clear();
						writeArrayList(resultList);
						numberOfResultsLabel.setText("Number of results: " + resultList.size());
						searchButton.setEnabled(true);
					}
				});
			} 
			else{
				SwingUtilities.invokeLater(new Runnable(){
					public void run(){
						results.clear();
						results.write("Error");
						numberOfResultsLabel.setText("Number of results: N/A");
						searchButton.setEnabled(true);
					}
				});
			}
		}
	}

	private void initAndAddComponents(){
		searchButton = new JButton("Search");
		searchButton.setEnabled(false);
		
		numberOfResultsLabel = new JLabel("Number of results: N/A");
		
		outputPanel = new JPanel();
		outputPanel.setLayout(new FlowLayout());
		
		SearcherListener listener = new SearcherListener();

		searchButton.addActionListener(listener);

		leftInputPanel = new JPanel();
		leftInputPanel.setLayout(new BoxLayout(leftInputPanel, BoxLayout.Y_AXIS));

		rightInputPanel = new JPanel();
		rightInputPanel.setLayout(new BoxLayout(rightInputPanel, BoxLayout.Y_AXIS));

		inputPanel = new JPanel();
		inputPanel.setLayout(new BoxLayout(inputPanel,BoxLayout.X_AXIS));

		int width = 8;

		Rsph = new LabelInput("Rsph: ", width, false);
		Rcyl = new LabelInput("Rcyl: ", width, false);
		Raxis = new LabelInput("Raxis: ", width, true);
		Lsph = new LabelInput("Lsph: ", width, false);
		Lcyl = new LabelInput("Lcyl: ", width, false);
		Laxis = new LabelInput("Laxis: ", width, true);

		Rsph.addActionListener(listener);
		Rcyl.addActionListener(listener);
		Raxis.addActionListener(listener);
		Lsph.addActionListener(listener);
		Lcyl.addActionListener(listener);
		Laxis.addActionListener(listener);

		results = new TextOutputArea(20, 20);

		rightInputPanel.add(Rsph);
		rightInputPanel.add(Rcyl);
		rightInputPanel.add(Raxis);
		leftInputPanel.add(Lsph);
		leftInputPanel.add(Lcyl);
		leftInputPanel.add(Laxis);

		inputPanel.add(leftInputPanel);
		inputPanel.add(rightInputPanel);
		
		outputPanel.add(results);
		outputPanel.add(numberOfResultsLabel);

		getContentPane().add(inputPanel);
		getContentPane().add(outputPanel);
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

		try{
			if(!Rsph.getInput().getText().equals("") && !Rcyl.getInput().getText().equals("")){
				RsphAndRcylResults = database.searchRsphAndRcyl(Double.valueOf(Rsph.getInput().getText()), Double.valueOf(Rcyl.getInput().getText()));
			}
			else if (!Rsph.getInput().getText().equals("")){
				RsphResults = database.searchRsph(Double.valueOf(Rsph.getInput().getText()));
			}
			else if (!Rcyl.getInput().getText().equals("")){
				RcylResults = database.searchRcyl(Double.valueOf(Rcyl.getInput().getText()));
			}

			if(!Raxis.getInput().getText().equals("")){
				RaxisResults = database.searchRaxis(Integer.valueOf(Raxis.getInput().getText()));
			}


			if(!Lsph.getInput().getText().equals("") && !Lcyl.getInput().getText().equals("")){
				LsphAndLcylResults = database.searchLsphAndLcyl(Double.valueOf(Lsph.getInput().getText()), Double.valueOf(Lcyl.getInput().getText()));
			}
			else if (!Lsph.getInput().getText().equals("")){
				LsphResults = database.searchLsph(Double.valueOf(Lsph.getInput().getText()));
			}
			else if (!Lcyl.getInput().getText().equals("")){
				LcylResults = database.searchLcyl(Double.valueOf(Lcyl.getInput().getText()));
			}

			if(!Laxis.getInput().getText().equals("")){
				LaxisResults = database.searchLaxis(Integer.valueOf(Laxis.getInput().getText()));
			}
		} catch (NumberFormatException e){
			throw e;
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

	public class SearcherListener implements ActionListener{
		public void actionPerformed(ActionEvent ae){
			System.out.println("Start search");
			(new Thread(new Searcher())).start();
		}
	}
}