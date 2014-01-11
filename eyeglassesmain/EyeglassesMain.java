package eyeglassesmain;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

public class EyeglassesMain {
	public static void main(String[] args) throws Exception{
		try {
			File file = new File("/Users/mjohansen15/Documents/GuatemalaGlassesFebruary-20142.xls");
			POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(file));
			HSSFWorkbook wb = new HSSFWorkbook(fs);
			HSSFSheet sheet = wb.getSheetAt(0);
			HSSFRow row;
			HSSFRow firstRow;
			HSSFCell cell;

			int rows; // No of rows
			rows = sheet.getPhysicalNumberOfRows();

			int cols = 0; // No of columns
			int tmp = 0;

			// This trick ensures that we get the data properly even if it doesn't start from first few rows
			/*for(int i = 0; i < 10 || i < rows; i++) {
				row = sheet.getRow(i);
				if(row != null) {
					tmp = sheet.getRow(i).getPhysicalNumberOfCells();
					if(tmp > cols) cols = tmp;
				}
			}

			for(int r = 0; r < rows; r++) {
				row = sheet.getRow(r);
				if(row != null) {
					for(int c = 0; c < cols; c++) {
						cell = row.getCell(c);
						if(cell != null) {

						}
					}
				}
			}*/
			
			HashMap<String, String[]> table = new HashMap<String, String[]>();
			
			firstRow = sheet.getRow(0);
			
			int i = 1;
			do{
				row = sheet.getRow(i);
				i = i + 1;
			} while(row != null);
			
		} catch(Exception ioe) {
			ioe.printStackTrace();
		}
	}
}
