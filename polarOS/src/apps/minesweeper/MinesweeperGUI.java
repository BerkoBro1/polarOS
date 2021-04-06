package apps.minesweeper;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Arrays;
import java.util.Scanner;

import javax.swing.*;

import sys64.*;

public class MinesweeperGUI extends JPanel{

	static JInternalFrame iframe = new JInternalFrame("Minesweeper");
	static JDesktopPane ipane;
	static Scanner input = new Scanner(System.in);
	static int X;
	int xc = X;
	static int Y;
	int yc = 1;
	static int numOfMines;
	int minesNearby;
	String name;
	String posName;
	String xAxisName;
	String yAxisName;
	Boolean np;
	Boolean match;
	Boolean firstClick = true;
	Boolean noneNearby;
	static Boolean rightClick;
	static Boolean leftClick;
	static Boolean middleClick;
	static int[] minesArray;
	static int[] flagArray;
	static int[] pickedArray;
	static JButton[] board;
	JButton winOrLose = new JButton("");
	JPanel panel = new JPanel(new CardLayout(4, 1));
	JPanel grid = new JPanel(new GridLayout(X, Y));
	
	public MinesweeperGUI() {
		//x and y axis' are swapped in java GUI for some reason so now x and y are now swapped.
		MouseListener mouseListener = new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				MinesweeperGUI.rightClick = SwingUtilities.isRightMouseButton(e);
				MinesweeperGUI.leftClick = SwingUtilities.isLeftMouseButton(e);
				MinesweeperGUI.middleClick = SwingUtilities.isMiddleMouseButton(e);
				buttonClicked((JButton) e.getSource());
			}
			public void mouseEntered(MouseEvent e) {}
			public void mouseExited(MouseEvent e) {}
			public void mousePressed(MouseEvent e) {}
			public void mouseReleased(MouseEvent e) {}
		};
		do {
			do {
				//names and adds components for each and every tile/Button
				name = String.valueOf(xc)+"_"+String.valueOf(yc);
				Icon icon = new ImageIcon("root\\apps\\minesweeper\\assets//empty.png");
				JButton name = new JButton(icon);
				name.setBorder(BorderFactory.createEmptyBorder());
				name.addMouseListener(mouseListener);
				name.setPreferredSize(new Dimension(25, 25));
				name.setActionCommand(String.valueOf(xc)+"_"+String.valueOf(yc));
				board[(((X*yc)-(X-1))+(xc-1))-1] = name;
				grid.add(name);
				yc++;
			}while(yc<=Y);
			yc=1;
			xc--;
		}while(xc>0);
		add(grid);
		add(winOrLose);
		winOrLose.setVisible(false);
		winOrLose.addMouseListener(mouseListener);
	}
	
	//creates the JFrame and the mine array a.k.a. where the mines are placed
	public static void start(JDesktopPane pane) {
		ipane = pane;
		iframe = sys64.CreateFrame.create((Y*25)+30, (X*25)+80, 960, 540, "Minesweeper", pane, false, true, 1, false, null, false, null, null);
		MinesweeperGUI window = new MinesweeperGUI();
		iframe.add(window);
		iframe.setVisible(true);
		int i;
		for (i = 0; i < X*Y; i++) {
            flagArray[i] = 0;
			pickedArray[i] = 0;
		}
	}
	
	//A button has been pressed!!!
	@SuppressWarnings("static-access")
	public void buttonClicked(JButton arg0) {
		if(arg0 == winOrLose) {
			iframe.getContentPane().removeAll();
			iframe.dispose();
			new SettingsGUI().start(ipane);
		} else {
			if(firstClick) {
				match = false;
				xc = 1;
				yc = 1;
				//checks for the name of the button by checking every possible name. This will give us the x and y coords (xc and yc).
				//xc and yc were originally for counting (x count) but now are used as coords (x coord)
				do {
					posName = String.valueOf(xc)+"_"+String.valueOf(yc);
					if(arg0.getActionCommand().equals(posName))
						match = true;
					else {
						if(xc>=X) {
							xc=1;
							yc++;
						} else
							xc++;
						if(yc>Y) 
							break;
					}
				}while(!match);
				minesArray = mineArrayCreator(xc, yc);
				firstClick = false;
			}
			match = false;
			xc = 1;
			yc = 1;
			//checks for the name of the button by checking every possible name. This will give us the x and y coords (xc and yc).
			//xc and yc were originally for counting (x count) but now are used as coords (x coord)
			do {
				posName = String.valueOf(xc)+"_"+String.valueOf(yc);
				if(arg0.getActionCommand().equals(posName))
					match = true;
				else {
					if(xc>=X) {
						xc=1;
						yc++;
					} else
						xc++;
					if(yc>Y) 
						break;
				}
			}while(!match);
			if(leftClick) {				
				//Runs Mine checker function
				mineChecker(minesArray, xc, yc);
			} else if(rightClick){
				int location = (((X*yc)-(X-1))+(xc-1))-1;
				if(flagArray[location]==1) {
					flagArray[location]=0;
					pickedArray[location]=0;
					board[location].setIcon(new ImageIcon("root\\apps\\minesweeper\\assets\\empty.png"));
				} else if(pickedArray[location]==0) {
					flagArray[location]=1;
					board[location].setIcon(new ImageIcon("root\\apps\\minesweeper\\assets\\flag.png"));
				}
			} else if(middleClick) {
				int location = (((X*yc)-(X-1))+(xc-1))-1;
				if(minesArray[location]!=1) {
					int[] mines = minesArray;
					boolean noLeft = leftSide(yc);
					boolean noRight = rightSide(yc);
					boolean noTop = top(xc);
					boolean noBottom = bottom(xc);
					int returnValue = 0;
					int flagValue = 0;
					if(!noBottom&&!noLeft) {
						returnValue+=mines[location-X-1];
						flagValue+=flagArray[location-X-1];
					} if(!noBottom) {
						returnValue+=mines[location-1];
						flagValue+=flagArray[location-1];
					} if(!noBottom&&!noRight) {
						returnValue+=mines[location+X-1];
						flagValue+=flagArray[location+X-1];
					} if(!noRight) {
						returnValue+=mines[location+X];
						flagValue+=flagArray[location+X];
					} if(!noRight&&!noTop) {
						returnValue+=mines[location+X+1];
						flagValue+=flagArray[location+X+1];
					} if(!noTop) {
						returnValue+=mines[location+1];
						flagValue+=flagArray[location+1];
					} if(!noTop&&!noLeft) {
						returnValue+=mines[location-X+1];
						flagValue+=flagArray[location-X+1];
					} if(!noLeft) {
						returnValue+=mines[location-X];
						flagValue+=flagArray[location-X];
					}
					if(returnValue==flagValue) {
						if(!noBottom&&!noLeft&&pickedArray[location-X-1]==0&&flagArray[location-X-1]==0)
							mineChecker(mines, xc-1, yc-1);
						if(!noBottom&&pickedArray[location-1]==0&&flagArray[location-1]==0)
							mineChecker(mines, xc-1, yc);
						if(!noBottom&&!noRight&&pickedArray[location+X-1]==0&&flagArray[location+X-1]==0)
							mineChecker(mines, xc-1, yc+1);
						if(!noRight&&pickedArray[location+X]==0&&flagArray[location+X]==0)
							mineChecker(mines, xc, yc+1);
						if(!noTop&&!noRight&&pickedArray[location+X+1]==0&&flagArray[location+X+1]==0)
							mineChecker(mines, xc+1, yc+1);
						if(!noTop&&pickedArray[location+1]==0&&flagArray[location+1]==0)
							mineChecker(mines, xc+1, yc);
						if(!noTop&&!noLeft&&pickedArray[location-X+1]==0&&flagArray[location-X+1]==0)
							mineChecker(mines, xc+1, yc-1);
						if(!noLeft&&pickedArray[location-X]==0&&flagArray[location-X]==0)
							mineChecker(mines, xc, yc-1);
					}
				}
			}
		}
	}
	
	public void changeIcon(JButton btn, String dir) {
		btn.setIcon(new ImageIcon("root\\apps\\minesweeper\\assets\\" + dir));
	}
	
	static String mineSpotName;
	public static int[] mineArrayCreator(int xc, int yc) {
		int[] mines = new int[X*Y];
		int i;
		for (i = 0; i < X*Y; i++) 
            mines[i] = 0;
		int mineCount = numOfMines;
		int location = (((X*yc)-(X-1))+(xc-1))-1;
		do {
			mineSpotName = String.valueOf(mineCount);
			int mineSpotName = (int)(Math.random()*((X*Y)-1));
			if (mines[mineSpotName] != 1 && mineSpotName != location)
				mines[mineSpotName] = 1;
			else 
				mineCount++;
			mineCount--;
		}while(mineCount>0);
		System.out.println();
		return mines; 
	}
	
	public boolean leftSide(int yc) {
		if(yc==1)
			return true;
		else
			return false;
	}
	
	public boolean rightSide(int yc) {
		if(yc==Y)
			return true;
		else
			return false;
	}
	
	public boolean top(int xc) {
		if(xc==X)
			return true;
		else
			return false;
	}
	
	public boolean bottom(int xc) {
		if(xc==1)
			return true;
		else
			return false;
	}

	public void mineChecker(int[] mines, int xc, int yc) {
		boolean noLeft = leftSide(yc);
		boolean noRight = rightSide(yc);
		boolean noTop = top(xc);
		boolean noBottom = bottom(xc);
		np = false;
		int returnValue = 0;
		int location = (((X*yc)-(X-1))+(xc-1))-1;
		if(flagArray[location]==1) {
			returnValue = 10;
			np = true;
		} else if(pickedArray[location]==1) {
			returnValue = 11;
			np = true;
		}else if(mines[location]==1) {
			pickedArray[location]=1;
			returnValue = 9;
			np = true;
		} 
		if(!np) {
			if(!noBottom&&!noLeft)
				returnValue+=mines[location-X-1];
			if(!noBottom)
				returnValue+=mines[location-1];
			if(!noBottom&&!noRight)
				returnValue+=mines[location+X-1];
			if(!noRight)
				returnValue+=mines[location+X];
			if(!noRight&&!noTop)
				returnValue+=mines[location+X+1];
			if(!noTop)
				returnValue+=mines[location+1];
			if(!noTop&&!noLeft)
				returnValue+=mines[location-X+1];
			if(!noLeft)
				returnValue+=mines[location-X];
		}
		pickedArray[location]=1;
		minesNearby = returnValue;
		if(minesNearby == 0) {
			//if the output is 0, then the space is blank and we check the nearby tiles as well
			changeIcon(board[location], "clear.png");
			if(returnValue==0) {
				if(!noBottom&&!noLeft)
					mineChecker(mines, xc-1, yc-1);
				if(!noBottom)
					mineChecker(mines, xc-1, yc);
				if(!noBottom&&!noRight)
					mineChecker(mines, xc-1, yc+1);
				if(!noRight)
					mineChecker(mines, xc, yc+1);
				if(!noTop&&!noRight)
					mineChecker(mines, xc+1, yc+1);
				if(!noTop)
					mineChecker(mines, xc+1, yc);
				if(!noTop&&!noLeft)
					mineChecker(mines, xc+1, yc-1);
				if(!noLeft)
					mineChecker(mines, xc, yc-1);
			}
		} else if(minesNearby == 9) {
			//9 isn't normally possible, so i used it to see if there is a bomb here. This ends the game
			changeIcon(board[location], "bomb.png");
			lose();
		} else if(minesNearby == 10) {
		} else if(minesNearby == 11) {
		} else {
			//This gives the mines their photo
			switch(minesNearby) {
				case 1:
					changeIcon(board[location], "one.png");
					break;
				case 2:
					changeIcon(board[location], "two.png");
					break;
				case 3:
					changeIcon(board[location], "three.png");
					break;
				case 4:
					changeIcon(board[location], "four.png");
					break;
				case 5:
					changeIcon(board[location], "five.png");
					break;
				case 6:
					changeIcon(board[location], "six.png");
					break;
				case 7:
					changeIcon(board[location], "seven.png");
					break;
				case 8:
					changeIcon(board[location], "eight.png");
					break;
			}
		}
		winCheck();
	}
	
	public void winCheck() {
		int i;
		int correct = 0;
		for (i = 0; i < X*Y; i++) 
            correct+=pickedArray[i];
		if(correct==(X*Y)-numOfMines) {
			winOrLose.setVisible(true);
			winOrLose.setText("You Win! Play again?");
			for (i=0; i < X*Y; i++) {
				flagArray[i] = 0;
				pickedArray[i] = 1;
			}
		}
	}
	
	public void lose() {
		winOrLose.setVisible(true);
		winOrLose.setText("You blew up! Play again?");
		yc = 1;
		xc = 0;
		int location;
		int i;
		boolean incomplete = true;
		do {
			if(xc>=X&&yc*xc!=X*Y) {
				xc=1;
				yc++;
				location = (((X*yc)-(X-1))+(xc-1))-1;
				if(minesArray[location] == 1 && flagArray[location] == 0) {
					changeIcon(board[location], "bomb.png");
				} else if(minesArray[location] == 0 && flagArray[location] == 1) {
					changeIcon(board[location], "wrong_flag.png");
				}
			} else if(yc*xc!=X*Y) {
				xc++; 
				location = (((X*yc)-(X-1))+(xc-1))-1;
				if(minesArray[location] == 1 && flagArray[location] == 0) {
					changeIcon(board[location], "bomb.png");
				} else if(minesArray[location] == 0 && flagArray[location] == 1) {
					changeIcon(board[location], "wrong_flag.png");
				}
			}else 
				incomplete = false;
		}while(incomplete);
		for (i=0; i < X*Y; i++) {
			flagArray[i] = 0;
			pickedArray[i] = 1;
		}
	}
}
