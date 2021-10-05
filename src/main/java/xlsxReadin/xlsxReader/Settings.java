package xlsxReadin.xlsxReader;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

public class Settings {
	
	
	static void checkSettingsFile() {
		if(isNix()) {
			checkLinux();
		}
		else if (isWin()) {
			checkWindows();
		}
		else {
			System.out.println("unsupported os");
		}
	}
	
	private static void checkWindows() {
		String settingsDir = System.getenv("APPDATA");
		String filePath = settingsDir+"\\" +FNAME;
		File setFile = new File(filePath);
		String defaultLocation = System.getProperty("user.dir")+ "\\database.xlsx";
		if (setFile.exists()) return;
		else {
			newSettingsFile(filePath, defaultLocation);
		}
	}
	
	private static void checkLinux() {
		String home = System.getProperty("user.home");
		String path = home + LINUX_DIR + "/" + FNAME;
		String defaultLocation = System.getProperty("user.dir")+"/database.xlsx";
		File dir = new File(home+LINUX_DIR);
		File setFile = new File(path);
		if (dir.exists() && setFile.exists()) {
			return;
		}
		else if (dir.exists() && !setFile.exists()) {
			newSettingsFile(path, defaultLocation);
		}
		else {
			new File(home+LINUX_DIR).mkdir();
			newSettingsFile(path, defaultLocation);
		}
	}

	static String getDBLocation() {
		String settingsFilePath = "";
		if (isNix()) {
			settingsFilePath += System.getProperty("user.home")+LINUX_DIR+"/"+ FNAME;
		}
		else if (isWin()) {
			settingsFilePath += System.getenv("APPDATA")+"\\"+FNAME;
		}
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(settingsFilePath));
			String dbLocation = reader.readLine();
			reader.close();
			return dbLocation;
		}catch (IOException ex) {
			System.out.println(ex.toString());
		}
		return null;
	}
	
	static void update(String newPath) {
		String settingsFilePath = "";
		if (isNix()) {
			settingsFilePath += System.getProperty("user.home")+LINUX_DIR+"/"+ FNAME;
		}
		else if (isWin()) {
			settingsFilePath += System.getenv("APPDATA")+"\\" + FNAME;
		}
		try {
			Writer writer = new BufferedWriter(new FileWriter(settingsFilePath, false));
			writer.write(newPath);
			writer.close();
		}catch(IOException ex) {
			System.out.println(ex.toString());
		}
	}
	
	
	private static void newSettingsFile(String path, String dbLocation) {
		try {
			Writer writer = new BufferedWriter(new FileWriter(path));
			writer.write(dbLocation);
			writer.close();
		}catch(IOException ex) {
			System.out.println(ex.toString());
		}
	}
	
	private static boolean isNix() {
		String os = System.getProperty("os.name").toLowerCase();
		if (os.indexOf(NIX)>=0 ||os.indexOf("nux")>=0) {
			return true;
		}
		return false;
	}
	
	private static boolean isWin() {
		String os = System.getProperty("os.name").toLowerCase();
		if (os.indexOf(WIN)>=0) {
			return true;
		}
		return false;
	}
	
	private static final String LINUX_DIR = "/.LookupConfig";
	private static final String FNAME = "settings.txt";
	private static final String NIX = "nix";
	private static final String WIN = "win";

}
