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

	public static ArrayList<Glasses> readFile(File file){
		if(glasses == null){
			POIFSFileSystem fs = null;
			HSSFSheet sheet = null;
			HSSFRow row = null;

			try {
				fs = new POIFSFileSystem(new FileInputStream(file));
			} catch (FileNotFoundException e) {
				System.out.println("Couldn't find file");
			} catch (IOException e) {
				System.out.println("Couldn't read file");
			}
			HSSFWorkbook wb = null;
			try {
				wb = new HSSFWorkbook(fs);
			} catch (IOException e) {
				System.out.println("Couldn't read file");
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
					number = Integer.valueOf(row.getCell(0).toString().split("\\.")[0]);
				} catch (NumberFormatException e){
					blanks = blanks + 1;
					number = 0;
				}
				try{
					if(row.getCell(1).toString().equals("")){
						empty = true;
					}
					else{
						empty = false;
					}
					Rsph = Double.valueOf(row.getCell(1).toString());
				} catch (NumberFormatException e){
					blanks = blanks + 1;
					Rsph = 0.0;
				}
				try{
					Rcyl = Double.valueOf(row.getCell(2).toString());
				} catch (NumberFormatException e){
					blanks = blanks + 1;
					Rcyl = 0.0;
				}
				try{
					Raxis = Integer.valueOf(row.getCell(3).toString().split("\\.")[0]);
				} catch (NumberFormatException e){
					blanks = blanks + 1;
					Raxis = 0;
				}
				try{
					Lsph = Double.valueOf(row.getCell(4).toString());
				} catch (NumberFormatException e){
					blanks = blanks + 1;
					Lsph = 0.0;
				}
				try{
					Lcyl = Double.valueOf(row.getCell(5).toString());
				} catch (NumberFormatException e){
					blanks = blanks + 1;
					Lcyl = 0.0;
				}
				try{
					Laxis = Integer.valueOf(row.getCell(6).toString().split("\\.")[0]);
				} catch (NumberFormatException e){
					blanks = blanks + 1;
					Laxis = 0;
				}
				
				frame = row.getCell(7).toString();
				if(frame.equals("")){
					frame = "N/A";
					blanks = blanks + 1;
				}
				
				lens = row.getCell(8).toString();
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
			
			System.out.println("Blank cells: " + blanks);
			System.out.println("Empties: " + empties);
		}
		return glasses;
	}
}
