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
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
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
	private GlassesTableModel tModel;
	private JFileChooser fileChooser;
	private File file;
	private JTable outputTable;
	private JScrollPane outputScrollPane;
	private JButton loadNewFileButton;
	private JPanel filePanel;
	private JButton clearButton;
	private JLabel fileLabel;
	private BufferedImage logoImage;
	private JPanel logoPanel;
	private Box rightInputBox;
	private LabelInput Rsph;
	private LabelInput Rcyl;
	private LabelInput Raxis;
	private LabelInput Lsph;
	private LabelInput Lcyl;
	private LabelInput Laxis;
	private JButton searchButton;
	private EyeglassDatabase database;
	private ArrayList<Glasses> resultList;
	private JLabel numberOfResultsLabel;

	public EyeglassesGui(){
		super("HopeSearch v" + EyeglassesMain.VERSION);

		fileChooser = new JFileChooser();

		getContentPane().setLayout(new FlowLayout());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		this.setPreferredSize(new Dimension(1050,690));

		initAndAddComponents();
		pack();

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
				EyeglassesMain.log("Search completed in " + time + "ms." + "  Results: " + resultList.size());
				worked = true;
			} catch (NumberFormatException e){
				EyeglassesMain.log("Couldn't process search parameters.", true);			
			}

			if(worked){
				SwingUtilities.invokeLater(new Runnable(){
					@Override
					public void run(){
						tModel = new GlassesTableModel(resultList);
						outputTable.setModel(tModel);
						numberOfResultsLabel.setText("Number of results: " + resultList.size());
						searchButton.setEnabled(true);
					}
				});
			} 
			else{
				SwingUtilities.invokeLater(new Runnable(){
					@Override
					public void run(){
						numberOfResultsLabel.setText("Number of results: N/A");
						outputTable.setModel(new GlassesTableModel(new ArrayList<Glasses>()));
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
			EyeglassesMain.log("Couldn't find logo file", true);
		}

		logoPanel = new JPanel();

		if(logoImage != null){
			logoPanel.add(new JLabel(new ImageIcon(logoImage)));
		}

		searchButton = new JButton("Search");
		searchButton.setEnabled(false);

		numberOfResultsLabel = new JLabel("Number of results: N/A");

		SearcherListener listener = new SearcherListener();

		searchButton.addActionListener(listener);

		leftInputBox = new Box(BoxLayout.X_AXIS);

		rightInputBox = new Box(BoxLayout.X_AXIS);

		inputPanel = new JPanel();
		inputPanel.setLayout(new BoxLayout(inputPanel,BoxLayout.Y_AXIS));

		int width = 8;

		Rsph = new LabelInput("Right sphere: ", width, false);
		Rcyl = new LabelInput("Right cylinder: ", width, false);
		Raxis = new LabelInput("Right axis: ", width, true);
		Lsph = new LabelInput("Left sphere:   ", width, false);
		Lcyl = new LabelInput("Left cylinder:   ", width, false);
		Laxis = new LabelInput("Left axis:   ", width, true);

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

		fileLabel = new JLabel("No file loaded yet");
		loadNewFileButton = new JButton("Load new file");

		loadNewFileButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				loadNewFile(null);
			}

		});
		
		clearButton = new JButton("Clear rx");
		
		clearButton.addActionListener(new ActionListener(){
			
			@Override
			public void actionPerformed(ActionEvent ae){
				Rsph.getInput().setText("");
				Rcyl.getInput().setText("");
				Raxis.getInput().setText("");
				Lsph.getInput().setText("");
				Lcyl.getInput().setText("");
				Laxis.getInput().setText("");
			}
		});

		filePanel = new JPanel();

		filePanel.setLayout(new FlowLayout());

		filePanel.add(fileLabel);
		filePanel.add(loadNewFileButton);

		rightInputBox.add(Rsph);
		rightInputBox.add(Box.createHorizontalStrut(3));
		rightInputBox.add(Rcyl);
		rightInputBox.add(Box.createHorizontalStrut(3));
		rightInputBox.add(Raxis);
		leftInputBox.add(Lsph);
		leftInputBox.add(Box.createHorizontalStrut(3));
		leftInputBox.add(Lcyl);
		leftInputBox.add(Box.createHorizontalStrut(3));
		leftInputBox.add(Laxis);

		inputPanel.add(rightInputBox);
		inputPanel.add(leftInputBox);

		outputTable = new JTable(new GlassesTableModel(new ArrayList<Glasses>()));
		
		outputScrollPane = new JScrollPane(outputTable);
		
		outputScrollPane.setPreferredSize(new Dimension(1000, 400));

		getContentPane().add(logoPanel);
		getContentPane().add(inputPanel);
		getContentPane().add(outputScrollPane);
		getContentPane().add(clearButton);
		getContentPane().add(searchButton);
		getContentPane().add(numberOfResultsLabel);
		getContentPane().add(filePanel);
	}

	public ArrayList<Glasses> searchAndCompare(){
		return database.search(Rsph.getInput().getText(), Rcyl.getInput().getText(), Raxis.getInput().getText(), 
				Lsph.getInput().getText(), Lcyl.getInput().getText(), Laxis.getInput().getText());
	}

	private void loadNewFile(File pFile){
		final File legitFile;
		boolean openFile = true;

		if(pFile == null){
			EyeglassesMain.log("prompting for file");
			int retVal;

			do{
				retVal = fileChooser.showOpenDialog(this);

				pFile = fileChooser.getSelectedFile();

				if(retVal == JFileChooser.CANCEL_OPTION){
					EyeglassesMain.log("Cancel selected.");
					openFile = false;
					break;
				}
				else if(retVal != JFileChooser.APPROVE_OPTION){
					EyeglassesMain.log("File selection failed", true);				
				}
				else{
					EyeglassesMain.log("Sucessfully opened file");
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
						EyeglassesMain.log("read database in " + time + "ms");
					} catch (org.apache.poi.poifs.filesystem.OfficeXmlFileException e){
						EyeglassesMain.log("File is not XLS", true);

						SwingUtilities.invokeLater(new Runnable(){
							@Override
							public void run(){
								searchButton.setEnabled(false);
								loadNewFileButton.setEnabled(true);
								fileLabel.setText("No file loaded yet");
							}
						});

						return;
					} catch (FormattingException e) {
						EyeglassesMain.log("There's a formatting problem on " + e.getMsg(), true);

						SwingUtilities.invokeLater(new Runnable(){
							@Override
							public void run(){
								searchButton.setEnabled(false);
								loadNewFileButton.setEnabled(true);
								fileLabel.setText("No file loaded yet");
							}
						});

						return;
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
			EyeglassesMain.log("Start search");
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