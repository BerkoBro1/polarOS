package apps.sys64.textViewer;

import java.awt.*;
import java.awt.event.*;
import java.io.*;

import javax.swing.*;
import javax.swing.event.*;

import sys64.*;

public class TextViewer {
	
	public static void start(JDesktopPane pane, String dir) {
		try {
			File txtFile = new File(dir);
			BufferedReader br = new BufferedReader(new FileReader(txtFile));
			String text = "";
			String exText = br.readLine();
			while(exText!=null) {
				text = text + exText + "\n";
				exText = br.readLine();
			}
			br.close();
			
			String[] splitDir = dir.split("\\\\");
			String name = splitDir[splitDir.length-1];
			
			JInternalFrame frame = CreateFrame.create(700, 500, 0, 0, name, pane, true, false, -1, false, null, false, null, null);
			JPanel panel = new JPanel(new BorderLayout());
			JTextArea textArea = new JTextArea(text);
			JButton save = new JButton("Save");
			save.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					try {
						BufferedWriter bw = new BufferedWriter(new FileWriter(txtFile));
						bw.write(textArea.getText());
						bw.close();
					} catch (IOException e) { e.printStackTrace(); }
				}
			});
			panel.add(save, BorderLayout.NORTH);
			panel.add(textArea);
			frame.add(panel);
			
		} catch(IOException e) { e.printStackTrace(); }
	}
	
}
