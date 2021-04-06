package sys64;

import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyVetoException;
import java.io.File;
import java.io.IOException;

import javax.swing.*;

import apps.*;

public class Shell extends JPanel {

	static JFrame shell = new JFrame("polarOS");
	
	JDesktopPane desktop = new JDesktopPane();
	JLabel desktopBackground = new JLabel(new ImageIcon("root//sys64//background.png"));
	
	int i;
	String dir;
	
	public Shell() {
		
		setLayout(new BorderLayout());
		
		JPanel background = new JPanel();
		background.setBackground(Color.BLACK);
		background.add(desktopBackground);
		desktop.add(background);
		background.setBounds(0, 0, 1920, 1080);
		add(desktop, BorderLayout.CENTER);

		Box taskbar = sys64.Taskbar.createTaskbar(desktop);
		add(taskbar, BorderLayout.SOUTH);
		
	}
	
	public static void main(String[] args) {
		
		shell.setVisible(true);
		shell.setResizable(false);
		shell.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		shell.add(new Shell());
		shell.setSize(1920, 1080);
		
	}
	
}
