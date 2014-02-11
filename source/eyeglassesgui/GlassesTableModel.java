package eyeglassesgui;

import java.text.DecimalFormat;
import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import eyeglassesmain.Glasses;

public class GlassesTableModel extends AbstractTableModel{
	private ArrayList<Glasses> glasses;
	private static final String[] columnLabels = {"Number","Rsph","Rcyl","Raxis","Lsph","Lcyl","Laxis","Frame","Lens"};
	private DecimalFormat decimalFormatter;

	public GlassesTableModel(ArrayList<Glasses> glasses){
		this.glasses = glasses;
		decimalFormatter = new DecimalFormat("+#0.00;-#0.00");
	}

	public String getColumnName(int col) {
		return columnLabels[col].toString();
	}

	public int getRowCount() {
		return glasses.size(); 
	}
	public int getColumnCount() { 
		return columnLabels.length; 
	}

	public Object getValueAt(int row, int col) {
		if(col == 0){
			return glasses.get(row).getNumber();
		}
		else if(col == 1){
			return decimalFormatter.format(glasses.get(row).getRsph());
		}
		else if(col == 2){
			return decimalFormatter.format(glasses.get(row).getRcyl());
		}
		else if(col == 3){
			return glasses.get(row).getRaxis();
		}
		else if(col == 4){
			return decimalFormatter.format(glasses.get(row).getLsph());
		}
		else if(col == 5){
			return decimalFormatter.format(glasses.get(row).getLcyl());
		}
		else if(col == 6){
			return glasses.get(row).getLaxis();
		}
		else if(col == 7){
			return glasses.get(row).getFrame();
		}
		else if(col == 8){
			return glasses.get(row).getLens();
		}

		else{
			return null;
		}
	}
	public boolean isCellEditable(int row, int col){ 
		return false; 
	}
	public void setValueAt(Object value, int row, int col) {
		return;
	}
}
