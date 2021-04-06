package sys64;

import java.awt.*;
import java.awt.event.*;
import java.util.function.Function;

import javax.swing.*;
import javax.swing.event.*;

public class CreateFrame {
	/**
	 * 
	 * @param width       The width of the frame
	 * @param height      The height of the frame
	 * @param xcord       X coordinate at which the frame will be created at
	 * @param ycord       Y coordinate at which the frame will be created at
	 * @param name        Title of the window
	 * @param pane        The original pane passed through the start function
	 * @param resizable   Determines if the window should be resizable
	 * @param minimizable Determines if the window should be minimizable
	 * @param windowNum   ID for window, determined in the shell
	 * @param haveIcon    Determines if the program has an icon
	 * @param iconPath    Sets path for icon, if no icon, set null
	 * @param addToStart  Determines if the program should be added to the start menu
	 * @param startName   Sets name to be displayed in the start menu
	 * @param launchPath  Sets path for the program's class
	 * 
	 *  */
	public static JInternalFrame create(int width, int height, int xcord, int ycord, String name, JDesktopPane pane, Boolean resizable, Boolean minimizable, int windowNum, Boolean haveIcon, String iconPath, Boolean addToStart, String startName, String launchPath) {
		JInternalFrame window = new JInternalFrame(name, resizable, true, resizable, minimizable);
		window.setBounds(xcord, ycord, width, height);
		pane.add(window);
		if(windowNum!=-1)
			sys64.Taskbar.windowsOpen[windowNum] = window;
		if(haveIcon) 
			window.setFrameIcon(new ImageIcon(iconPath));
		if(addToStart)
			AppChecker.checkIfAppAdded(startName, launchPath);
		window.setVisible(true);
		window.addInternalFrameListener(new InternalFrameListener() {


			public void internalFrameIconified(InternalFrameEvent e) {
				window.setVisible(false);
			}

			public void internalFrameDeiconified(InternalFrameEvent e) {
				window.setVisible(true);
			}

			public void internalFrameOpened(InternalFrameEvent e) {}
			public void internalFrameClosing(InternalFrameEvent e) {}
			public void internalFrameClosed(InternalFrameEvent e) {}
			public void internalFrameActivated(InternalFrameEvent e) {}
			public void internalFrameDeactivated(InternalFrameEvent e) {}
				
		});
		return window;
	}
	
}
