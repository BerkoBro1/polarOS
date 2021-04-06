package sys64;

import javax.tools.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.plaf.basic.*;

import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.*;

import apps.*;

public class Taskbar {
	
	static Boolean fileExplorerOpen = false;
	static Boolean fileExplorerMinimized = false;
	static Boolean minesweeperOpen = false;
	static Boolean minesweeperMinimized = false;
	static Boolean snakeOpen = false;
	static Boolean snakeMinimized = false;
	
	static Boolean doubleClicProtec = false;
	
	public static JInternalFrame[] windowsOpen = new JInternalFrame[3];
	
	public static Box createTaskbar(JDesktopPane pane) {
		
		Box taskbar = Box.createHorizontalBox();
		JButton minesweeper = new JButton();
		JButton snake = new JButton();
		JButton fileExplorer = new JButton();
		JButton[] taskbarItems = new JButton[3];
		int i;
		
		taskbar.add(createStart(pane));
		
		taskbarItems[0] = fileExplorer;
		fileExplorer.setName("textEditor");
		taskbarItems[1] = minesweeper;
		minesweeper.setName("minesweeper");
		taskbarItems[2] = snake;
		snake.setName("snake");
		snake.setPreferredSize(new Dimension(80, 50));
		taskbar.add(Box.createHorizontalGlue());
		for(i=0;i<taskbarItems.length-1;i++) {
			if(new File("root//apps//"+taskbarItems[i].getName()+"//logo.png").exists())
				taskbarItems[i].setIcon(new ImageIcon("root//apps//"+taskbarItems[i].getName()+"//logo.png"));
			taskbarItems[i].setPreferredSize(new Dimension(80, 50));
			taskbarItems[i].setBackground(Color.DARK_GRAY);
			taskbar.add(taskbarItems[i]);
			taskbar.add(Box.createHorizontalStrut(10));
		}
		if(new File("root//apps//"+taskbarItems[taskbarItems.length-1].getName()+"//logo.png").exists())
			taskbarItems[taskbarItems.length-1].setIcon(new ImageIcon("root//apps//"+taskbarItems[taskbarItems.length-1].getName()+"//logo.png"));
		snake.setBackground(Color.DARK_GRAY);
		//how to minimize window - iframe.setIcon(true);
		
		//ID = 0
		fileExplorer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(!fileExplorerOpen) {
					apps.sys64.fileExplorer.FileExplorer.start(pane); 
					fileExplorerOpen = true;
					fileExplorerMinimized = false;
				}
				else if(fileExplorerOpen) {
						if(!windowsOpen[0].isClosed()) {
							try {
								if(windowsOpen[0].isIcon()) 
									windowsOpen[0].setIcon(false);
								else 
									windowsOpen[0].setIcon(true);
							} catch (PropertyVetoException e) { e.printStackTrace(); }
						} else {
							apps.sys64.fileExplorer.FileExplorer.start(pane); 
							fileExplorerOpen = true;
							fileExplorerMinimized = false;
						}
				}
			}
		});
		
		//ID = 1
		minesweeper.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(!minesweeperOpen) {
					apps.minesweeper.SettingsGUI.start(pane); 
					minesweeperOpen = true;
					minesweeperMinimized = false;
				}
				else if(minesweeperOpen) {
						if(!windowsOpen[1].isClosed()) {
							try {
								if(windowsOpen[1].isIcon()) 
									windowsOpen[1].setIcon(false);
								else 
									windowsOpen[1].setIcon(true);
							} catch (PropertyVetoException e) { e.printStackTrace(); }
						} else {
							apps.minesweeper.SettingsGUI.start(pane); 
							minesweeperOpen = true;
							minesweeperMinimized = false;
						}
				}
			}
		});
		
		//ID = 2
		snake.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				if(!snakeOpen) {
					apps.encryptedTextEditor.Login.start(pane); 
					snakeOpen = true;
					snakeMinimized = false;
				}
				else if(snakeOpen) {
						if(!windowsOpen[2].isClosed()) {
							try {
								if(windowsOpen[2].isIcon()) 
									windowsOpen[2].setIcon(false);
								else 
									windowsOpen[2].setIcon(true);
							} catch (PropertyVetoException e) { e.printStackTrace(); }
						} else {
							apps.encryptedTextEditor.Login.start(pane); 
							snakeOpen = true;
							snakeMinimized = false;
						}
				}
			}		
		});
		taskbar.add(taskbarItems[taskbarItems.length-1]);
		taskbar.add(Box.createHorizontalGlue());
		taskbar.setBorder(BorderFactory.createRaisedBevelBorder());
		taskbar.setOpaque(true);
		taskbar.setBackground(Color.DARK_GRAY);
		
		return taskbar;
	}
	
	public static JButton createStart(JDesktopPane pane) {
		
		JButton start = new JButton(new ImageIcon("root\\sys64\\taskbar\\startIcon.png"));
		start.setBorder(null);
		start.setBackground(Color.DARK_GRAY);
		start.addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent arg0) {
				JInternalFrame startFrame = CreateFrame.create(200, 350, 30, 610, "start", pane, false, false, -1, false, null, false, null, null);
				startFrame.addInternalFrameListener(new InternalFrameListener() {

					public void internalFrameOpened(InternalFrameEvent e) {}
					public void internalFrameClosing(InternalFrameEvent e) {}
					public void internalFrameClosed(InternalFrameEvent e) {}
					public void internalFrameIconified(InternalFrameEvent e) {}
					public void internalFrameDeiconified(InternalFrameEvent e) {}
					public void internalFrameActivated(InternalFrameEvent e) {}

					@Override
					public void internalFrameDeactivated(InternalFrameEvent e) {
						startFrame.doDefaultCloseAction();
					}
					
				});
				
				Box container = Box.createVerticalBox();
				Box appBox = Box.createVerticalBox();
				Box functions = Box.createHorizontalBox();
				
				BufferedReader in;
				try {
					in = new BufferedReader(new FileReader(new File("root\\sys64\\taskbar\\appList.txt")));
					int lines = 0;
					while (in.readLine() != null) lines++;
					in.close();
					BufferedReader iin = new BufferedReader(new FileReader(new File("root\\sys64\\taskbar\\appList.txt")));
					String[] appList = new String[lines];
					for(int i = 0; i < lines; i++) appList[i] = iin.readLine();
					JList jAppList = new JList(appList);
					jAppList.addListSelectionListener(new ListSelectionListener() {
						@Override
						public void valueChanged(ListSelectionEvent arg0) {
							if(!doubleClicProtec) {
								doubleClicProtec = true;
								try {
									int index = ((JList) arg0.getSource()).getSelectedIndex();
									File dirFile = new File("root\\sys64\\taskbar\\appMethodList.txt");
									BufferedReader br = new BufferedReader(new FileReader(dirFile));
									for(int i = 0; i < index; i++)
										br.readLine();
									String fullCmd = br.readLine();
									br.close();
									Method method = (Class.forName(fullCmd)).getDeclaredMethod("start", new Class[] {JDesktopPane.class});
									method.invoke(start, new Object[] {pane});
								} catch (IOException | NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | ClassNotFoundException e1) { e1.printStackTrace(); }
							} else
								doubleClicProtec = false;
						}
					});
					JScrollPane jsp = new JScrollPane(jAppList, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
					appBox.add(jsp);
				} catch (IOException e) { e.printStackTrace(); }
				
				container.add(appBox);
				
				
				container.add(functions);
				
				startFrame.add(container);
				startFrame.setVisible(true);
				BasicInternalFrameUI bi = (BasicInternalFrameUI)startFrame.getUI();
				bi.setNorthPane(null);
			}
		});
		return start;
	}
	
	
	
}
