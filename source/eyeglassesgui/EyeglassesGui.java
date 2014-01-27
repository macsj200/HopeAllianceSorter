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
import eyeglassesmain.EyeglassesMain;
import eyeglassesmain.Glasses;

@SuppressWarnings("serial")
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
	private ArrayList<Glasses> resultList;
	private JLabel numberOfResultsLabel;
	private JPanel outputPanel;

	public EyeglassesGui(){
		super("HopeSearch v" + EyeglassesMain.VERSION);
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

			if(file == null){
				System.out.println("File selection failed");
				JOptionPane.showMessageDialog(this, "Couldn't open file.  Exiting.","Error", JOptionPane.ERROR_MESSAGE);
				System.exit(-1);
			}
			else{
				System.out.println("Sucessfully opened file");
			}
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

		this.setPreferredSize(new Dimension(1050,720));

		initAndAddComponents();
		pack();
		setVisible(true);

		(new Thread(new Runnable(){
			@Override
			public void run(){
				long time = System.currentTimeMillis();
				database = new EyeglassDatabase(file);
				time = System.currentTimeMillis() - time;
				System.out.println("read database in " + time + "ms");

				SwingUtilities.invokeLater(new Runnable(){
					@Override
					public void run(){
						searchButton.setEnabled(true);
					}
				});
			}
		})).start();

	}

	public class Searcher implements Runnable{
		@Override
		public void run(){
			SwingUtilities.invokeLater(new Runnable(){
				@Override
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
					@Override
					public void run(){
						results.clear();
						results.write("No.\tRSPH\tRCYL\tRAXIS\tLSPH\tLCYL\tLAXIS\tFRAME\tLENS\n\n");
						writeGlassesList(resultList);
						numberOfResultsLabel.setText("Number of results: " + resultList.size());
						searchButton.setEnabled(true);
					}
				});
			} 
			else{
				SwingUtilities.invokeLater(new Runnable(){
					@Override
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

		results = new TextOutputArea(20, 70);

		rightInputPanel.add(Rsph);
		rightInputPanel.add(Rcyl);
		rightInputPanel.add(Raxis);
		leftInputPanel.add(Lsph);
		leftInputPanel.add(Lcyl);
		leftInputPanel.add(Laxis);

		inputPanel.add(rightInputPanel);
		inputPanel.add(leftInputPanel);

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

	public ArrayList<Glasses> searchAndCompare(){
		return database.search(Rsph.getInput().getText(), Rcyl.getInput().getText(), Raxis.getInput().getText(), 
				Lsph.getInput().getText(), Lcyl.getInput().getText(), Laxis.getInput().getText());
	}

	public void writeGlassesList(ArrayList<Glasses> list){
		for(int i = 0; i < list.size(); i++){
			results.write(list.get(i).toString() + "\n");
		}
	}


	public class SearcherListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent ae){
			System.out.println("Start search");
			(new Thread(new Searcher())).start();
		}
	}
}