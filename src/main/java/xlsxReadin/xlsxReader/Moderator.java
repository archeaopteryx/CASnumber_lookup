package xlsxReadin.xlsxReader;

import java.io.File;

public class Moderator {
	
	public void run() {
		InputGUI input = new InputGUI();
		int skipRows = input.getSkip();
		int casCol = ColNameToInt(input.getCas());
		int outCol = ColNameToInt(input.getOut());
		int sheet = input.getSheet()-1;
		File file = input.getFile();
		if(file== null) System.exit(0);
		else {
			new App(skipRows, casCol, outCol, sheet, file);
		}
	}
	
	private int ColNameToInt(String colName) {
		String name = colName.toUpperCase();
		int length = name.length();
		if(length==1) {
			return (int) name.charAt(0)-'A';
		}
		else {
			int total = 0;
			for(int i=0; i<length-1; i++) {
				total += ((name.charAt(i)+1-'A')*26);
			}
			total+=(int) name.charAt(length-1)-'A';
			return total;
		}
	}
	
	public static void main(String[] args) {
		new Moderator().run();
	}

}
