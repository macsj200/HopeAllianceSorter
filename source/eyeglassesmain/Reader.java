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

			int rows; // No of rows
			sheet =  wb.getSheetAt(0);
			rows = sheet.getPhysicalNumberOfRows();

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
			
			boolean empty = false;

			do{
				try{
					number = Integer.valueOf(row.getCell(0).toString().split("\\.")[0]);
				} catch (NumberFormatException e){
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
					Rsph = 0.0;
				}
				try{
					Rcyl = Double.valueOf(row.getCell(2).toString());
				} catch (NumberFormatException e){
					Rcyl = 0.0;
				}
				try{
					Raxis = Integer.valueOf(row.getCell(3).toString().split("\\.")[0]);
				} catch (NumberFormatException e){
					Raxis = 0;
				}
				try{
					Lsph = Double.valueOf(row.getCell(4).toString());
				} catch (NumberFormatException e){
					Lsph = 0.0;
				}
				try{
					Lcyl = Double.valueOf(row.getCell(5).toString());
				} catch (NumberFormatException e){
					Lcyl = 0.0;
				}
				try{
					Laxis = Integer.valueOf(row.getCell(6).toString().split("\\.")[0]);
				} catch (NumberFormatException e){
					Laxis = 0;
				}
				frame = row.getCell(7).toString();
				lens = row.getCell(8).toString();

				if(!empty){
					glasses.add(new Glasses(number, Rsph, Rcyl, Raxis, Lsph, Lcyl, Laxis, frame, lens));
				}

				i = i + 1;
				row = sheet.getRow(i);
			} while(row != null);
		}
		return glasses;
	}
}