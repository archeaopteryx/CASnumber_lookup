package xlsxReadin.xlsxReader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
//import java.util.concurrent.TimeUnit;

import javax.swing.JOptionPane;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * Program to automate looking up chemical names from CAS numbers in
 * an excel sheet formula
 * 
 * Uses the database.xlsx sheet as a database
 * First checks the 'database' for the CAS. If the CAS
 * isn't found, then the URL for the NIST web book page is sent and 
 * the document title retrieved. If the page isn't contained on the 
 * NIST site, then 'thegoodscentcomapany' is checked. Assuming the 
 * CAS is found, it is added to the database file. Finally, the excel
 * sheet with the formula has the chemical names filled in.
 *
 */
public class App 
{
	private int skipRows, casCol, outCol, sheetNum;
	private File file;
	private static final int N_BUCKETS = 50;

	public App(int skipRows, int casCol, int outCol, int sheetNum, File file) {
		this.skipRows = skipRows;
		this.casCol = casCol;
		this.outCol = outCol;
		this.sheetNum = sheetNum;
		this.file = file;
		run();
	}
	
	public void run() {
		try {
			ArrayList<String> casNumList = casList();
			ArrayList<String> namesList = lookup(casNumList);
			output(namesList);
		}catch (IOException ex) {
			System.out.println(ex.getMessage());
			throw new RuntimeException(ex.getMessage());
		}
	}
	
	private ArrayList<String> casList() throws IOException{
		ArrayList<String> casNums = new ArrayList<String>();
		FileInputStream fis = new FileInputStream(file);
		XSSFWorkbook wb = new XSSFWorkbook(fis);
		try {
			XSSFSheet sheet = wb.getSheetAt(sheetNum);
			for (int i = skipRows; i<=sheet.getLastRowNum(); i++) {
				try {
					Cell cell = sheet.getRow(i).getCell(casCol);
					casNums.add(cell.getStringCellValue());
				}catch(NullPointerException npe){
					casNums.add("");
				}				
			}
		}catch(IllegalArgumentException e) {
			JOptionPane.showMessageDialog(null, "Sheet does not exist! Exiting...");
			System.exit(0);
		}

		wb.close();
		fis.close();
		return casNums;
	}
	
	private ArrayList<String> lookup (ArrayList<String> casNums) throws IOException {
		ArrayList<String> chemNames = new ArrayList<String>();
		//String dbFile = "../introduction/database.xlsx";
		String dbFile = "database.xlsx";
		FileInputStream fis = new FileInputStream(dbFile);
		XSSFWorkbook wb = new XSSFWorkbook(fis);
		XSSFSheet sheet = wb.getSheetAt(0);
		XSSFSheet nameSheet = wb.getSheetAt(1);
		for(String cas : casNums) {
			boolean found = false;
			if(cas.length() == 0) {
				chemNames.add("");
				found = true;
			}
			else {
				int bucket = getBucket(cas);
				Row row = sheet.getRow(bucket);
				if(row.getCell(1)!=null) {
					int lastCell = row.getLastCellNum();
					for(int i=0; i<lastCell; i++) {
						Cell cell = row.getCell(i);
						if (cas.equals(cell.getStringCellValue())) {
							chemNames.add(nameSheet.getRow(bucket).getCell(i).getStringCellValue());
							found = true;
							break;
						}
					}
				}
				if (!found) {
					String name = nistLookup(cas);
					if (name.equals("Registry Number Not Found")) {
						name = fragLookup(cas);
					}
					int index = row.getLastCellNum();
					row.createCell(index).setCellValue(cas);
					Row nameCellRow = nameSheet.getRow(bucket);
					nameCellRow.createCell(index).setCellValue(name);
					chemNames.add(name);
				}
			}
		}
		FileOutputStream fileOut = new FileOutputStream(dbFile);
		wb.write(fileOut);
		wb.close();
		fis.close();
		return chemNames;
	}
	
	private void output(ArrayList<String> names) throws IOException {
		FileInputStream fis = new FileInputStream(file);
		XSSFWorkbook wb = new XSSFWorkbook(fis);
		XSSFSheet sheet = wb.getSheetAt(sheetNum);
		int index = 0;
		for (int i = skipRows; i<sheet.getLastRowNum()+skipRows; i++) {
			Row row = sheet.getRow(i);
			try {
				if (row != null) row.createCell(outCol).setCellValue(names.get(index));
				index++;
			}catch (NullPointerException npe) {
				index++;
			}
			}
		FileOutputStream fileOut = new FileOutputStream(file);
		wb.write(fileOut);
		wb.close();
		fis.close();
		String msg = "Finished!";
			
		JOptionPane.showMessageDialog(null, msg);
	}
	
	private int getBucket(String cas) {
		String base = "";
		for(int i=0; i<cas.length(); i++) {
			if(cas.charAt(i)=='-' && base.length()>0) break;
			base += cas.charAt(i);
		}
		if(base.length()==0) {
			JOptionPane.showMessageDialog(null, "problem assigning bucket to cas number"+cas);
			System.exit(0);
		}
		int value = (int) Integer.parseInt(base);
		int bucket = value % N_BUCKETS;
		return bucket;
	}
	
	private String nistLookup(String cas) throws IOException {
		String urlBase = "https://webbook.nist.gov/cgi/cbook.cgi?ID=";
		String urlSuffix = "&Units=SI"; 
		String url = urlBase+cas+urlSuffix;
		Document doc = Jsoup.connect(url).get();
		String title = doc.title();
		return title; 
	}
	
	private String fragLookup(String cas) throws IOException {
		String urlBase = "http://thegoodscentscompany.com/search3.php?qName=";
		String urlSuffix = "&submit.x=0&submit.y=0";
		String url = urlBase+cas+urlSuffix;
		Document doc = Jsoup.connect(url).get();
		String foundCAS = "";
		String name = "";
		try {
			name += doc.selectFirst("a.lstw4").text();
			foundCAS += doc.selectFirst("span.lstw10").text();
		} catch (NullPointerException e) {
			return "";
		}
			
		if (cas.equals(foundCAS)) return name;
		else {
			return "";
		}
	}
	

/*	
	private static void pause() {
		try {
			Thread.sleep(200);
		}
		catch (InterruptedException ex){
			String msg =ex.getMessage();
			JOptionPane.showMessageDialog(null, msg);
			throw new RuntimeException(msg);
		}
	}
*/
}
