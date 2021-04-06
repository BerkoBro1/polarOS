package apps.encryptedTextEditor;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

public class FileBrowser extends JPanel implements ActionListener{
	JLabel label = new JLabel("File List: ");
	JButton newFile = new JButton("New File");
	JButton open = new JButton("Open");
	JButton delete = new JButton("Delete");
	JTextField newFileTF = new JTextField(10);
	ButtonGroup bg;
	File directory;
	String dire;
	public FileBrowser(String dir) {
		dire = dir;
		directory = new File(dir);
		directory.mkdir();
		JPanel fileList = new JPanel(new GridLayout(directory.listFiles().length + 4, 1));
		fileList.add(label);
		bg = new ButtonGroup();
		for(File file : directory.listFiles()) {
			JRadioButton radio = new JRadioButton(file.getName());
			radio.setActionCommand(file.getName());
			bg.add(radio);
			fileList.add(radio);
		}
		setHiddenAttrib(new File(dir));
		JPanel newP = new JPanel();
		newP.add(newFileTF);
		newP.add(newFile);
		newFile.addActionListener(this);
		open.addActionListener(this);
		delete.addActionListener(this);
		fileList.add(open);
		fileList.add(delete);
		fileList.add(newP);
		add(fileList);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		Login login = (Login) getParent();
		if(e.getSource() == open) {
			login.add(new Editor(dire + "\\" + bg.getSelection().getActionCommand()), "editor");
			login.cl.show(login, "editor");
		}
		if(e.getSource() == newFile) {
			String file = dire + "\\" + newFileTF.getText() + ".txt";
			if(newFileTF.getText().length() > 0 && !(new File(file).exists())) {
				login.add(new Editor(file), "editor");
				login.cl.show(login, "editor");
			}
		}
		if(e.getSource() == delete) {
			try {
				Files.deleteIfExists(Paths.get(dire + "\\" + bg.getSelection().getActionCommand()));
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		}
	}
	
	private static void setHiddenAttrib(File file) {
		try {
			// execute attribute command to set hide attribute
			Process p = Runtime.getRuntime().exec("attrib +H " + file.getPath());
			// for removing hide attribute
			//Process p = Runtime.getRuntime().exec("attrib -H " + file.getPath());
			p.waitFor(); 
			if(file.isHidden()) {
				//System.out.println(file.getName() + " hidden attribute is set to true");
			} else {
				System.out.println(file.getName() + " hidden attribute not set to true");
		 	}
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
		    e.printStackTrace();
		}
	}
	
}
