package apps.snake;

import javax.swing.*;
import javax.swing.event.*;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.Timer;

import sys64.CreateFrame;

public class Snake extends JPanel {
	
	static JInternalFrame frame;
	
	final int XRES = 20;
	final int YRES = 30;
	int SPEED = 50;
	
	JLabel[] grid = new JLabel[XRES*YRES];
	JPanel screen = new JPanel(new GridLayout(XRES, YRES));
	int[] board = new int[XRES*YRES];
	ArrayList<Integer> snakeArray = new ArrayList<Integer>();
	
	int i;
	
	Color floor = Color.GREEN;
	Color head = Color.GRAY;
	Color body = Color.BLACK;
	Color food = Color.RED;
	
	int dir = 2;
	int prevDir = 2;
	
	int score = 0;
	JLabel scoreLabel = new JLabel("Score: 0", SwingConstants.CENTER);
	
	ArrayList<Integer> addArray = new ArrayList<Integer>();
	
	Boolean firstMove = true;
	Timer timer = new Timer();
	TimerTask timerTask = new TimerTask() {
		@Override
		public void run() {
			if(playing)
				refreshScreen();
			else
				snakeArray.clear();
		}
	};
	static Boolean playing;
	
	public Snake() {
		Action up = new AbstractAction() {
			public void actionPerformed(ActionEvent arg0) {
				System.out.println("preess");
			}
		};
		getInputMap().put(KeyStroke.getKeyStroke("SPACE"), "up");
		getActionMap().put("up", up);

		setFocusable(true);
		for(i=0;i<XRES*YRES;i++) {
			JLabel label = new JLabel("     ");
			grid[i] = label;
			label.setBackground(floor);
			label.setOpaque(true);
			screen.add(label);
			board[i] = 0;
		}
		board[(YRES*3)+5] = 2;
		board[(YRES*3)+4] = 1;
		board[(YRES*3)+3] = 1;
		grid[(YRES*3)+5].setBackground(head);
		grid[(YRES*3)+4].setBackground(body);
		grid[(YRES*3)+3].setBackground(body);
		snakeArray.add((YRES*3)+5);
		snakeArray.add((YRES*3)+4);
		snakeArray.add((YRES*3)+3);
		
		scoreLabel.setBorder(BorderFactory.createLoweredSoftBevelBorder());
		setLayout(new BorderLayout());
		add(scoreLabel, BorderLayout.NORTH);
		add(screen);
		createFood();
		timer.schedule(timerTask, SPEED, SPEED);
	}
	
	public static void start(JDesktopPane pane) {
		frame = CreateFrame.create(466, 381, 960, 540, "Snake", pane, false, true, 2, false, null, true, "Snake", "apps.snake.Snake");
		Snake window = new Snake();
		frame.add(window);
		InternalFrameListener exitListener = new InternalFrameAdapter() {
			@Override
			public void internalFrameClosed(InternalFrameEvent arg0) {
				playing = false;
			}
		};
		frame.addInternalFrameListener(exitListener);
		playing = true;
	}
	
	int location;
	
	public void createFood() {
		Boolean inBody = false;
		do {
			location = (int) (Math.random() * (XRES*YRES));
			if(board[location]==0) 
				inBody = false;
			else
				inBody = true;
		}while(inBody);
		board[location] = 3;
		grid[location].setBackground(food);
		
	}
	
	public void eatFood() {
		scoreLabel.setText("Score: " + ++score);
		addArray.add(snakeArray.size());
		createFood();
	}
	
	public void refreshScreen() {
		prevDir = dir;
		int last = snakeArray.get(snakeArray.size()-1);
		int oneOrTwo = 1;
		board[snakeArray.get(snakeArray.size()-1)] = 0;
		snakeArray.set(snakeArray.size()-1, snakeArray.get(snakeArray.size()-2));
		for(i=0;i<addArray.size()-1;i++) {
			if(addArray.get(i)==0) {
				snakeArray.add(last);
				board[last] = 1;
				oneOrTwo = 2;
				addArray.remove(i);
				break;
			} else {
				addArray.set(i, addArray.get(i) - 1);
			}
		}
		for(i=snakeArray.size()-oneOrTwo;i>0;i--) {
			snakeArray.set(i, snakeArray.get(i-1));
			board[snakeArray.get(i)] = 1;
		}
		switch(dir) {
			case 1:
				if(!(snakeArray.get(0)<YRES) && !checkIfHitBody(-YRES)) {
						snakeArray.set(0, snakeArray.get(0) - YRES);
						board[snakeArray.get(0)] = 2;
				} else {
					died();
				}
				break;
			case 2:
				if(snakeArray.get(0)%YRES!=YRES-1 && !checkIfHitBody(1)) {
					snakeArray.set(0, snakeArray.get(0) + 1);
					board[snakeArray.get(0)] = 2;
				} else { 
					died();
				}
				break;
			case 3:
				if(!(snakeArray.get(0)>YRES*(XRES-1)) && !checkIfHitBody(YRES)) {
					snakeArray.set(0, snakeArray.get(0) + YRES);
					board[snakeArray.get(0)] = 2;
				} else {
					died();
				}
				break;
			case 4:
				if(snakeArray.get(0)%YRES!=0 && !checkIfHitBody(-1)) {
					snakeArray.set(0, snakeArray.get(0) - 1);
					board[snakeArray.get(0)] = 2;
				} else {
					died();
				}
				break;
		}
		for(i=0;i<XRES*YRES;i++) {
			switch(board[i]) {
				case 0:
					grid[i].setBackground(floor);
					break;
				case 1:
					grid[i].setBackground(body);
					break;
				case 2:
					grid[i].setBackground(head);
					break;
				case 3:
					grid[i].setBackground(food);
					break;
			}
		}
		if(!snakeArray.isEmpty())
			if(snakeArray.get(0) == location) 
				eatFood();
	}
	
	public Boolean checkIfHitBody(int dire) {
		for(i=1;i<snakeArray.size()-1;i++)
			if(snakeArray.get(i) == snakeArray.get(0) + dire)
				return true;
		return false;
	}
	
	public void died() {
		snakeArray.clear();
		Object[] options = { "Play Again?", "Quit Game" };
		int input = JOptionPane.showInternalOptionDialog(frame, "You Died! Your score was " + score, "You Died!", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
		if(input == 0) {
			score = 0;
			scoreLabel.setText("Score: 0");
			snakeArray.add((YRES*3)+5);
			snakeArray.add((YRES*3)+4);
			snakeArray.add((YRES*3)+3);
			dir = 2;
			for(i=0;i<XRES*YRES;i++)
				board[i]=0;
			board[(YRES*3)+5] = 2;
			board[(YRES*3)+4] = 1;
			board[(YRES*3)+3] = 1;
			createFood();
		} else if(input == 1) {
			playing = false;
			frame.dispose(); 
		}
	}
	
}