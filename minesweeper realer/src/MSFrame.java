import java.util.*;
import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.*;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

@SuppressWarnings({"serial", "unused"})

public class MSFrame extends JFrame {

	private JPanel gridPanel;
	private JPanel topPanel;
	private JPanel bottomPanel;
	private MSButton[][] buttons;
	private Grid grid;
	private JTextField flagCounter;
	private TimerThing timerThing;
	private JTextArea timeDisplay = new JTextArea(4, 10);
	private JTextArea winDisplay = new JTextArea(4, 10);
	
	private int[] bestTimes = new int[4];
	private int[] numWins = new int[4];
	
	private int currDiff;
	
	private static ImageIcon flag = new ImageIcon("/flag.png");
	
	public MSFrame() {
		
		readFromFile("msdata.txt");
		
		addWindowListener(new WindowAdapter() {
			
			@Override
			public void windowClosing(WindowEvent e) {
				saveToFile("msdata.txt"); // save after closing
			}
			
		});
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		
	}
	
	private void displayWindow(int rows, int columns) {
		
		GridBagConstraints gbcFrame = new GridBagConstraints();
		
		GridBagConstraints gbcTopPanel = new GridBagConstraints();
		gbcTopPanel.insets = new Insets(0, 10, 0, 10);
		
		GridBagConstraints gbcBottomPanel = new GridBagConstraints();
		gbcBottomPanel.insets = new Insets(0, 10, 0, 10);
		
		getContentPane().removeAll();
		setLayout(new GridBagLayout());
		revalidate();
		repaint();
		
		//
		
		gridPanel = new JPanel(new GridLayout(rows, columns));
		
		for (MSButton[] B : buttons) {
			for (MSButton b : B) {
				gridPanel.add(b);
			}
		}
		
		gbcFrame.gridx = 0;
		gbcFrame.gridy = 1;
		
		add(gridPanel, gbcFrame);
		
		//
		
		topPanel = new JPanel(new GridBagLayout());
		topPanel.setPreferredSize(new Dimension(columns * 50, 80));
		topPanel.setBackground(new Color(204, 244, 242));
		
		//
		
		flagCounter = new JTextField();
		flagCounter.setEditable(false);
		flagCounter.setFocusable(false);
		flagCounter.setPreferredSize(new Dimension(90, 40));
		flagCounter.setHorizontalAlignment(JTextField.CENTER);
		flagCounter.setText("Flags: 10");
		
		gbcTopPanel.gridx = 0;
		gbcTopPanel.gridy = 0;
		
		topPanel.add(flagCounter, gbcTopPanel);
		
		//
		
		JButton restartButton = new JButton("O");
		restartButton.setFocusable(false);
		restartButton.setPreferredSize(new Dimension(45, 30));
		
		restartButton.addMouseListener(new MouseAdapter() {
			
		    @Override
		    public void mousePressed(MouseEvent e) {
		    	
		    	Timer delay = new Timer(100, new ActionListener() {
		            public void actionPerformed(ActionEvent e) {
		                initializeGame();
		            }
		        });
		    	
		    	delay.setRepeats(false);
		    	delay.start();
		        
		    }
		    
		});
		
		
		gbcTopPanel.gridx = 1;
		gbcTopPanel.gridy = 0;
		
		topPanel.add(restartButton, gbcTopPanel);
		
		//
		
		timerThing = new TimerThing();
		
		gbcTopPanel.gridx = 2;
		gbcTopPanel.gridy = 0;
		
		topPanel.add(timerThing, gbcTopPanel);
		
		//
		
		gbcFrame.gridx = 0;
		gbcFrame.gridy = 0;
		
		add(topPanel, gbcFrame);
		
		//
		
		bottomPanel = new JPanel(new GridBagLayout());
		bottomPanel.setPreferredSize(new Dimension(columns * 50, 100));
		bottomPanel.setBackground(new Color(204, 244, 242));
		
		gbcFrame.gridx = 0;
		gbcFrame.gridy = 2;
		
		add(bottomPanel, gbcFrame);
		
		//
		
		timeDisplay.setEditable(false);
		timeDisplay.setFocusable(false);
		
		gbcBottomPanel.gridx = 0;
		gbcBottomPanel.gridy = 0;
		
		timeDisplay.setText(bestTimes[0] + "\n" + bestTimes[1] + "\n" + bestTimes[2] + "\n" + bestTimes[3]);
		
		bottomPanel.add(timeDisplay, gbcBottomPanel);
		
		//
		
		winDisplay.setEditable(false);
		winDisplay.setFocusable(false);
		
		gbcBottomPanel.gridx = 1;
		gbcBottomPanel.gridy = 0;
		
		winDisplay.setText(numWins[0] + "\n" + numWins[1] + "\n" + numWins[2] + "\n" + numWins[3]);
		
		bottomPanel.add(winDisplay, gbcBottomPanel);
		
		//
		
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
		
	}
	
	private int displayDifficultyMenu() {
		
		String[] difficulties = {"Free", "Easy", "Medium", "Hard", "SILENT"};
		
		int i = JOptionPane.showOptionDialog(
				
				null,
				"Choose difficulty",
				"Difficulty thing",
				JOptionPane.DEFAULT_OPTION,
				JOptionPane.INFORMATION_MESSAGE,
				null,
				difficulties,
				null
						
		);
				
		return i;
		
	}
	
	private void handleButtonClick(MouseEvent e, MSButton b) {
		
		if (!MSButton.gameOver) {
			
			if (SwingUtilities.isLeftMouseButton(e) && !b.isRevealed() && !b.isFlagged()) { // left click only if concealed and unflagged
	        	
	            if (MSButton.firstClick) { // wait until first click to generate array of bombs
	        		
	        		grid.initializeGrid(b.getRow(), b.getColumn()); // initialize the grid, passing in the coordinates of the first button clicked so that the first click is safe
	        		
	        		for (MSButton[] Z : buttons) { // set count attribute for every button
	        			
	        			for (MSButton z : Z) {
	        				
	        				if (grid.getBombGrid()[z.getRow()][z.getColumn()]) {
			        			z.setCount(-1);
			        		}
			        		else {
			        			z.setCount(grid.getCountGrid()[z.getRow()][z.getColumn()]);
			        		}
	        				
	        			}
	        			
	        		}
	        		
	        		MSButton.firstClick = false;
	        		
	        	}
	            
	        	b.reveal();
	        	
	        }
	        else if (SwingUtilities.isRightMouseButton(e) && !b.isRevealed() && !MSButton.firstClick) { // right click only if concealed and not first click
	        	
	        	b.toggleFlag();
	        	flagCounter.setText("Flags: " + MSButton.numFlagsRemaining);
	        	
	        }
	        else if (SwingUtilities.isMiddleMouseButton(e) && b.isRevealed() && b.getCount() != 0) { // middle click only if revealed and is not 0
	        	
	        	MSButton[] neighbors = b.getNeighbors();
	        	int flags = 0;
	        	int remainingNeighbors = 0;
	        	
	        	for (MSButton z : neighbors) {
	        		
	        		if (z != null) {
	        			
	        			if (z.isFlagged()) {
	        				++flags;
	        			}
	        			
	        			++remainingNeighbors;
	        			
	        		}
	        		
	        	}
	        	
	        	if (flags == b.getCount() && remainingNeighbors > b.getCount()) {
	        		
	        		for (MSButton z : neighbors) {
	        			
	        			if (z != null && !z.isFlagged()) {
	        				
	        				z.reveal();
	        				
	        			}
	        			
	        		}
	        		
	        	}
	        	
	        }
	    	
	    	if (MSButton.mineRevealed) {
	    		
	    		System.out.println("You have losed...");
	    		displayEndScreen(false);
	    		MSButton.gameOver = true;
	    		
	    	}
	    	
	    	if (MSButton.numRevealed == (grid.getNumRows() * grid.getNumColumns()) - grid.getNumBombs() && !MSButton.gameOver) {
	    		
	    		System.out.println("You have winned...");
	    		displayEndScreen(true);
	    		MSButton.gameOver = true;
	    		
	    	}
			
		}
		else {
			return;
		}
		
	}
	
	private void displayEndScreen(boolean win) {
		
		timerThing.stop();
		
		if (win) {
			
			switch (currDiff) {
			case 0:
				++numWins[0];
				if (timerThing.getRawTime() < bestTimes[0]) {
					bestTimes[0] = timerThing.getRawTime();
				}
				break;
			case 1:
				++numWins[1];
				if (timerThing.getRawTime() < bestTimes[1]) {
					bestTimes[1] = timerThing.getRawTime();
				}
				break;
			case 2:
				++numWins[2];
				if (timerThing.getRawTime() < bestTimes[2]) {
					bestTimes[2] = timerThing.getRawTime();
				}
				break;
			case 3:
				++numWins[3];
				if (timerThing.getRawTime() < bestTimes[3]) {
					bestTimes[3] = timerThing.getRawTime();
				}
				break;
			}
			
		}
		
	    Timer timer = new Timer(200, e -> {
	    	
	    	timeDisplay.setText(bestTimes[0] + "\n" + bestTimes[1] + "\n" + bestTimes[2] + "\n" + bestTimes[3]);
			winDisplay.setText(numWins[0] + "\n" + numWins[1] + "\n" + numWins[2] + "\n" + numWins[3]);
	    	
	        int i = JOptionPane.showConfirmDialog(
	        		
	            null,
	            win ? 
	            "You Have Winned. Play Again?\nTime Elapsed: " + timerThing.getTime() + "\nBest Time: " + String.format("%02d:%02d", (bestTimes[currDiff] / 60), (bestTimes[currDiff] % 60))
	            :
	            "You Have Losed. Play Again?",
	            "Game Over",
	            JOptionPane.YES_NO_OPTION
	            
	        );

	        if (i == JOptionPane.YES_OPTION) {
	            initializeGame();
	        } else {
	        	saveToFile("msdata.txt");
	            System.exit(0);
	        }
	        
	    });
	    
	    timer.setRepeats(false);
	    timer.start();
	    
	}
	
	public void initializeGame() {
		
		MSButton.initializeFreshGame();
		
		int diff = displayDifficultyMenu();
		currDiff = diff;
		
		switch (diff) {
		case 0:
			grid = new Grid(6, 6, 4);
			MSButton.numFlagsRemaining = 4;
			setTitle("Hello Bro (Free)");
			break;
		case 1:
			grid = new Grid(9, 9, 10);
			MSButton.numFlagsRemaining = 10;
			setTitle("Hello Bro (Easy)");
			break;
		case 2:
			grid = new Grid(16, 16, 40);
			MSButton.numFlagsRemaining = 40;
			setTitle("Hello Bro (Medium)");
			break;
		case 3:
			grid = new Grid(16, 30, 70);
			MSButton.numFlagsRemaining = 70;
			setTitle("Hello Bro (Hard)");
			break;
		case 4:
			saveToFile("msdata.txt");
			JOptionPane.showMessageDialog(null, "LMAO...", "LMAO", JOptionPane.INFORMATION_MESSAGE);
			System.exit(0);
			break;
		default:
			System.exit(0);
		}
		
        buttons = new MSButton[grid.getNumRows()][grid.getNumColumns()];
        MSButton.initializeButtonsArrayRef(buttons);
        
        for (int i = 0; i < buttons.length; ++i) {
        	
        	for (int j = 0; j < buttons[i].length; ++j) {
        		
        		buttons[i][j] = new MSButton("", i, j, 0);
        		
        	}
        	
        }
        
        for (MSButton[] B : buttons) {
        	
        	for (MSButton b : B) {
        		
        		b.addMouseListener(new MouseAdapter() {
        			
        		    @Override
        		    public void mousePressed(MouseEvent e) {
        		        handleButtonClick(e, b);
        		    }
        		});
        		
        	}
        	
        }
        
        displayWindow(grid.getNumRows(), grid.getNumColumns());
        
        timerThing.start();
		
	}
	
	private void saveToFile(String fileName) {
	    
		FileWriter f = null;
		PrintWriter w = null;
	    
	    try {
	    	
	    	f = new FileWriter(fileName);
	    	w = new PrintWriter(f);
	    	
	    	for (int i = 0; i < 4; ++i) {
	    		w.println(bestTimes[i]);
	    	}
	    	
	    	for (int i = 0; i < 4; ++i) {
	    		w.print(numWins[i]);
	    		if (i != 3) {
	    			w.println();
	    		}
	    	}
	  
	    }
	    catch (IOException e) {
	    	System.out.println("Could not write to the file......");
	    	e.printStackTrace();
	    }
	    finally {
	    	if (w != null) {
	    		w.close();
	    	}
	    }
	    
	}
	
	private void readFromFile(String fileName) {
	    
	    FileInputStream f = null;
	    Scanner scnr = null;
	    
	    try {
	      
	    	f = new FileInputStream(fileName);
	    	scnr = new Scanner(f);
	      
	    	for (int i = 0; i < 4; ++i) {
	    		bestTimes[i] = Integer.parseInt(scnr.nextLine());
	    	}
	    	
	    	for (int i = 0; i < 4; ++i) {
	    		numWins[i] = Integer.parseInt(scnr.nextLine());
	    	}
	      
	    }
	    catch (IOException e) {
	    	System.out.println("Something went wrong....");
	    	e.printStackTrace();
	    }
	    
	}
	
}
