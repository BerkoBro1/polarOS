package apps.sys64.fileExplorer;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.*;

import apps.sys64.imageViewer.*;
import apps.sys64.textViewer.*;
import sys64.*;

public class FileExplorer extends JPanel {

	static JDesktopPane ipane;
	static JInternalFrame frame;
	
	Box mainContainer = Box.createHorizontalBox();
	Box rightContainer = Box.createVerticalBox();
	JList quickAccessList;
	JScrollPane quickAccess;
	Box addressBox = Box.createHorizontalBox();
	JButton up = new JButton(new ImageIcon("root\\apps\\fileExplorer\\upArrow.png"));
	JTextField addressBar = new JTextField("root", 1);
	JList ls;
	JScrollPane lsDir = new JScrollPane();
	String address = "root";
	
	String[] qadir;
	String[] visible;
	
	Boolean firstCall = true;
	Boolean firstCallII = true;
	
	public FileExplorer() {
		setLayout(new BorderLayout());
		try {
			BufferedReader in = new BufferedReader(new FileReader(new File("root\\apps\\fileExplorer\\quickAccessList.txt")));
			int lines = 0;
			while (in.readLine() != null) lines++;
			qadir = new String[lines];
			visible = new String[lines];
			in.close();
			BufferedReader iin = new BufferedReader(new FileReader(new File("root\\apps\\fileExplorer\\quickAccessList.txt")));
			for(int i = 0; i < lines; i++) {
				qadir[i] = iin.readLine();
				String[] finalDir = qadir[i].split("\\\\");
				visible[i] = finalDir[finalDir.length - 1];
			}
			quickAccessList = new JList(visible);
			iin.close();
			quickAccess = new JScrollPane(quickAccessList, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			quickAccess.setPreferredSize(new Dimension(100, 100));
		} catch (IOException e) { e.printStackTrace(); }
		
		quickAccessList.addListSelectionListener(new ListSelectionListener() {

			public void valueChanged(ListSelectionEvent arg0) {
				if(firstCallII) {
					firstCallII = false;
					int index = ((JList) arg0.getSource()).getSelectedIndex();
					address = qadir[index];
					addressBar.setText(address);
					lsDir();
				} else
					firstCallII = true;
			}
			
		});
		
		lsDir();
		
		addressBar.setBorder(BorderFactory.createEtchedBorder());
		addressBar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				address = addressBar.getText();
				lsDir();
			}
		});
		
		up.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String[] splitAddress = address.split("\\\\");
				if(splitAddress.length!=1 && splitAddress.length!=0) {
					address = splitAddress[0];
					for(int i = 1; i < splitAddress.length - 1; i++)
						address = address + "\\" + splitAddress[i];
					addressBar.setText(address);
					lsDir();
				}
			}
		});
		
		lsDir.setPreferredSize(new Dimension(50, 1080));
		
		up.setPreferredSize(new Dimension(20, 20));
		addressBox.add(up);
		addressBox.add(addressBar);
		
		rightContainer.add(addressBox);
		rightContainer.add(lsDir);
		mainContainer.add(quickAccess);
		mainContainer.add(rightContainer);
		add(mainContainer);
	}
	
	public static void start(JDesktopPane pane) {
		ipane = pane;
		FileExplorer window = new FileExplorer();
		frame = CreateFrame.create(500, 500, 240, 240, "File Explorer", pane, true, true, 0, true, "root\\apps\\fileExplorer\\logo.png", true, "File Explorer", "apps.sys64.fileExplorer.FileExplorer");
		frame.add(window);
	}
	
	public void lsDir() {
		String[] lsA = new File(address).list();
		ls = new JList(lsA);
		if(lsA!=null)	
			lsDir.setViewportView(ls);
		else
			lsDir.setViewportView(new JList());
		ls.addListSelectionListener(new ListSelectionListener() {
			
			public void valueChanged(ListSelectionEvent arg0) {
				if(firstCall) {
					firstCall = false;
					String extentionType = checkExtention(lsA[((JList) arg0.getSource()).getSelectedIndex()]);
					if(extentionType.equals("png"))
						ImageViewer.start(ipane, address + "\\" + lsA[((JList) arg0.getSource()).getSelectedIndex()]);
					else if(extentionType.equals("txt"))
						TextViewer.start(ipane, address + "\\" + lsA[((JList) arg0.getSource()).getSelectedIndex()]);
					else if(extentionType.equals("")) {
						address = address + "\\" + lsA[((JList) arg0.getSource()).getSelectedIndex()];
						addressBar.setText(address);
						lsDir();
					} else
						JOptionPane.showInternalMessageDialog(frame, "The file type ." + extentionType + " is not yet supported by PolarOS ");
				} else 
					firstCall = true;
			}
			
		});
	}
	
	public String checkExtention(String fullName) {
	    String fileName = new File(fullName).getName();
	    int dotIndex = fileName.lastIndexOf('.');
	    return (dotIndex == -1) ? "" : fileName.substring(dotIndex + 1);
	}
}