package sys64;

import java.io.*;

public class AppChecker {
	
	public static String[] checkApps() {
		
		
		
		return null;
	}
	
	public static void checkIfAppAdded(String name, String classPath) {
		
		try {
			File appList = new File("root\\sys64\\taskbar\\appList.txt");
			File appMethodList = new File("root\\sys64\\taskbar\\appMethodList.txt");
			BufferedReader read = new BufferedReader(new FileReader(appList));
			String line;
			do
				line = read.readLine();
			while(line!=null && !line.equals(name));
			read.close();
			if(line==null) {
				BufferedWriter write = new BufferedWriter(new FileWriter(appList, true));
				BufferedWriter writee = new BufferedWriter(new FileWriter(appMethodList, true));
				write.write("\n"+name);
				write.close();
				writee.write("\n"+classPath);
				writee.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
}
