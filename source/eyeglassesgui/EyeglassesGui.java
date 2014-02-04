package eyeglassesgui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import eyeglassesmain.EyeglassDatabase;
import eyeglassesmain.EyeglassesMain;
import eyeglassesmain.FormattingException;
import eyeglassesmain.Glasses;

@SuppressWarnings("serial")
public class EyeglassesGui extends JFrame{
	private JPanel inputPanel;
	private Box leftInputBox;
	private JFileChooser fileChooser;
	private File file;
	private JButton loadNewFileButton;
	private JPanel filePanel;
	private JLabel fileLabel;
	private BufferedImage logoImage;
	private JPanel logoPanel;
	private Properties prop;
	private Box rightInputBox;
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

		setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		this.setPreferredSize(new Dimension(1050,600));

		initAndAddComponents();
		pack();

		try {
			prop.load(new FileInputStream(configFile));
			System.out.println("Config file exists, reading");

			file = new File(prop.getProperty("filepath"));

			if(file.getParentFile().exists()){
				fileChooser.setCurrentDirectory(file.getParentFile());
			}

		} catch (FileNotFoundException e) {
			System.out.println("Couldn't find config");
		} catch (IOException e) {
			System.out.println("Couldn't read config");
		} finally{
			loadNewFile(file);
		}

		setVisible(true);
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
		logoImage = null;

		try {
			logoImage = ImageIO.read(getClass().getResource("/imgs/colored_logo.jpg"));
		} catch (IOException e) {
			System.out.println("Couldn't find logo file");
		}

		logoPanel = new JPanel();

		if(logoImage != null){
			logoPanel.add(new JLabel(new ImageIcon(logoImage)));
		}

		searchButton = new JButton("Search");
		searchButton.setEnabled(false);

		numberOfResultsLabel = new JLabel("Number of results: N/A");

		outputPanel = new JPanel();
		outputPanel.setLayout(new FlowLayout());

		SearcherListener listener = new SearcherListener();

		searchButton.addActionListener(listener);

		leftInputBox = new Box(BoxLayout.X_AXIS);

		rightInputBox = new Box(BoxLayout.X_AXIS);

		inputPanel = new JPanel();
		inputPanel.setLayout(new BoxLayout(inputPanel,BoxLayout.Y_AXIS));

		int width = 8;

		Rsph = new LabelInput("Rsph: ", width, false);
		Rcyl = new LabelInput("Rcyl: ", width, false);
		Raxis = new LabelInput("Raxis: ", width, true);
		Lsph = new LabelInput("Lsph: ", width, false);
		Lcyl = new LabelInput("Lcyl: ", width, false);
		Laxis = new LabelInput("Laxis: ", width, true);

		Raxis.getInput().getDocument().putProperty("owner", Raxis);
		Laxis.getInput().getDocument().putProperty("owner", Laxis);

		Rsph.addActionListener(listener);
		Rcyl.addActionListener(listener);
		Raxis.addActionListener(listener);
		Lsph.addActionListener(listener);
		Lcyl.addActionListener(listener);
		Laxis.addActionListener(listener);

		addDocumentListenerTo(Rsph.getInput());
		addDocumentListenerTo(Rcyl.getInput());
		addDocumentListenerTo(Raxis.getInput());
		addDocumentListenerTo(Lsph.getInput());
		addDocumentListenerTo(Lcyl.getInput());
		addDocumentListenerTo(Laxis.getInput());

		results = new TextOutputArea(20, 70);

		fileLabel = new JLabel("No file loaded yet");
		loadNewFileButton = new JButton("Load new file");

		loadNewFileButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				loadNewFile(null);
			}

		});

		filePanel = new JPanel();

		filePanel.setLayout(new FlowLayout());

		filePanel.add(fileLabel);
		filePanel.add(loadNewFileButton);

		rightInputBox.add(Rsph);
		rightInputBox.add(Rcyl);
		rightInputBox.add(Raxis);
		leftInputBox.add(Lsph);
		leftInputBox.add(Lcyl);
		leftInputBox.add(Laxis);

		inputPanel.add(rightInputBox);
		inputPanel.add(leftInputBox);

		outputPanel.add(results);
		outputPanel.add(numberOfResultsLabel);

		getContentPane().add(logoPanel);
		getContentPane().add(inputPanel);
		getContentPane().add(outputPanel);
		getContentPane().add(searchButton);
		getContentPane().add(filePanel);
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

	private void loadNewFile(File pFile){
		final File legitFile;
		boolean openFile = true;

		if(pFile == null){
			System.out.println("prompting for file");
			int retVal;

			do{
				retVal = fileChooser.showOpenDialog(this);

				pFile = fileChooser.getSelectedFile();

				if(retVal == JFileChooser.CANCEL_OPTION){
					System.out.println("Cancel selected.");
					openFile = false;
					break;
				}
				else if(retVal != JFileChooser.APPROVE_OPTION){
					System.out.println("File selection failed");
					JOptionPane.showMessageDialog(this, "Couldn't open file.","Error", JOptionPane.ERROR_MESSAGE);
				}
				else{
					System.out.println("Sucessfully opened file");
					openFile = true;
				}

			} while (retVal != JFileChooser.APPROVE_OPTION);
		}


		if(openFile){
			legitFile = pFile;

			(new Thread(new Runnable(){
				@Override
				public void run(){
					SwingUtilities.invokeLater(new Runnable(){
						@Override
						public void run(){
							loadNewFileButton.setEnabled(false);
						}
					});

					try{
						long time = System.currentTimeMillis();

						database = new EyeglassDatabase(legitFile);

						time = System.currentTimeMillis() - time;
						System.out.println("read database in " + time + "ms");
					} catch (org.apache.poi.poifs.filesystem.OfficeXmlFileException e){
						System.out.println("File is not XLS");

						SwingUtilities.invokeLater(new Runnable(){
							@Override
							public void run(){
								searchButton.setEnabled(false);
								loadNewFileButton.setEnabled(true);
								fileLabel.setText("No file loaded yet");
							}
						});

						JOptionPane.showMessageDialog(null, "Couldn't parse file.  Is it a .xls file?","Error", JOptionPane.ERROR_MESSAGE);

						return;
					} catch (FormattingException e) {
						System.out.println("There's a formatting problem on " + e.getMsg());

						SwingUtilities.invokeLater(new Runnable(){
							@Override
							public void run(){
								searchButton.setEnabled(false);
								loadNewFileButton.setEnabled(true);
								fileLabel.setText("No file loaded yet");
							}
						});

						JOptionPane.showMessageDialog(null, "Couldn't parse file.  Error on " + e.getMsg(),"Error", JOptionPane.ERROR_MESSAGE);

						return;
					}

					System.out.println("Writing path to config: " + legitFile.getAbsolutePath());
					prop.setProperty("filepath", legitFile.getAbsolutePath());


					try {
						prop.store(new FileOutputStream(".config.properties"), null);
						System.out.println("Stored config");
					} catch (FileNotFoundException e) {
						System.out.println("Couldn't save config");
					} catch (IOException e) {
						System.out.println("Couldn't save config");
					}

					SwingUtilities.invokeLater(new Runnable(){
						@Override
						public void run(){
							searchButton.setEnabled(true);
							loadNewFileButton.setEnabled(true);
							fileLabel.setText("Loaded " + legitFile.getName());
						}
					});
				}
			})).start();

		} else{
			SwingUtilities.invokeLater(new Runnable(){
				@Override
				public void run(){
					searchButton.setEnabled(false);
					fileLabel.setText("No file loaded yet");
				}
			});
		}
	}


	public class SearcherListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent ae){
			System.out.println("Start search");
			(new Thread(new Searcher())).start();
		}
	}

	private void addDocumentListenerTo(final JTextField textField){
		textField.getDocument().addDocumentListener(new DocumentListener(){

			Color defaultColor = textField.getBackground();

			@Override
			public void changedUpdate(DocumentEvent arg0) {

			}

			@Override
			public void insertUpdate(DocumentEvent arg0) {
				changed(arg0);
			}

			@Override
			public void removeUpdate(DocumentEvent arg0) {
				changed(arg0);
			}

			public void changed(DocumentEvent arg0){
				if(textField.getText().equals("")){
					color(true);
				} else{
					color(verify(arg0));
				}
			}

			public boolean verify(DocumentEvent arg0){
				boolean ret = false;

				try{
					if(arg0.getDocument().getProperty("owner") == Raxis || arg0.getDocument().getProperty("owner") == Laxis){
						Integer.valueOf(textField.getText());
					}
					else{
						Double.valueOf(textField.getText());
					}
					ret = true;
				} catch (NumberFormatException e){
					ret = false;
				}

				return ret;
			}

			public void color(boolean good){
				if(good){
					textField.setBackground(defaultColor);
				} else{
					textField.setBackground(Color.red);
				}
			}
		});
	}
}