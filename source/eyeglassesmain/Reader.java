package eyeglassesmain;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;


public class Reader {
	private static ArrayList<Glasses> glasses = null;

	public static ArrayList<Glasses> readFile(File file) throws FormattingException{
		POIFSFileSystem fs = null;
		HSSFSheet sheet = null;
		HSSFRow row = null;

		try {
			fs = new POIFSFileSystem(new FileInputStream(file));
		} catch (FileNotFoundException e) {
			EyeglassesMain.log("Couldn't find file", true);
		} catch (IOException e) {
			EyeglassesMain.log("Couldn't read file", true);
		}
		HSSFWorkbook wb = null;
		try {
			wb = new HSSFWorkbook(fs);
		} catch (IOException e) {
			EyeglassesMain.log("Couldn't read file", true);
		}

		sheet =  wb.getSheetAt(0);

		glasses = new ArrayList<Glasses>();

		int number;
		double Rsph;
		double Rcyl;
		int Raxis;
		double Lsph;
		double Lcyl;
		int Laxis;
		String frame;
		String lens;

		row = sheet.getRow(1);
		int i = 2;

		int blanks = 0;
		int empties = 0;

		boolean empty = false;

		do{
			try{
				try{
					number = Integer.valueOf(row.getCell(0).toString().split("\\.")[0]);
				} catch (NullPointerException e){
					throw new FormattingException("Error on cell " + 0 + " row " + i);
				}
			} catch (NumberFormatException e){
				blanks = blanks + 1;
				number = 0;
			}
			try{
				try{
					if(row.getCell(1).toString().equals("")){
						empty = true;
					}
					else{
						empty = false;
					}
				} catch (NullPointerException e){
					throw new FormattingException("Error on cell " + 1 + " row " + i);
				}
				Rsph = Double.valueOf(row.getCell(1).toString());
			} catch (NumberFormatException e){
				blanks = blanks + 1;
				Rsph = 0.0;
			}
			try{
				try{
					Rcyl = Double.valueOf(row.getCell(2).toString());
				} catch (NullPointerException e){
					throw new FormattingException("Error on cell " + 2 + " row " + i);
				}
			} catch (NumberFormatException e){
				blanks = blanks + 1;
				Rcyl = 0.0;
			}
			try{
				try{
					Raxis = Integer.valueOf(row.getCell(3).toString().split("\\.")[0]);
				} catch (NullPointerException e){
					throw new FormattingException("Error on cell " + 3 + " row " + i);
				}
			} catch (NumberFormatException e){
				blanks = blanks + 1;
				Raxis = 0;
			}
			try{
				try{
					Lsph = Double.valueOf(row.getCell(4).toString());
				} catch (NullPointerException e){
					throw new FormattingException("Error on cell " + 4 + " row " + i);
				}
			} catch (NumberFormatException e){
				blanks = blanks + 1;
				Lsph = 0.0;
			}
			try{
				try{
					Lcyl = Double.valueOf(row.getCell(5).toString());
				} catch (NullPointerException e){
					throw new FormattingException("Error on cell " + 5 + " row " + i);
				}
			} catch (NumberFormatException e){
				blanks = blanks + 1;
				Lcyl = 0.0;
			}
			try{
				try{
					Laxis = Integer.valueOf(row.getCell(6).toString().split("\\.")[0]);
				} catch (NullPointerException e){
					throw new FormattingException("Error on cell " + 6 + " row " + i);
				}
			} catch (NumberFormatException e){
				blanks = blanks + 1;
				Laxis = 0;
			}

			try{
				frame = row.getCell(7).toString();
			} catch (NullPointerException e){
				throw new FormattingException("Error on cell " + 7 + " row " + i);
			}
			if(frame.equals("")){
				frame = "N/A";
				blanks = blanks + 1;
			}

			try{
				lens = row.getCell(8).toString();
			} catch (NullPointerException e){
				throw new FormattingException("Error on cell " + 8 + " row " + i);
			}
			if(lens.equals("")){
				lens = "N/A";
				blanks = blanks + 1;
			}

			if(!empty){
				glasses.add(new Glasses(number, Rsph, Rcyl, Raxis, Lsph, Lcyl, Laxis, frame, lens));
			} else{
				empties = empties + 1;
			}

			i = i + 1;
			row = sheet.getRow(i);
		} while(row != null);

		EyeglassesMain.log("Blank cells: " + blanks);
		EyeglassesMain.log("Empties: " + empties);

		return glasses;
	}
}
