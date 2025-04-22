import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
@SuppressWarnings({"serial", "unused"})

public class MSButton extends JButton {

	private int count; // bomb = -1, otherwise amount of neighboring bombs
	private int row;
	private int column;
	private boolean isRevealed = false;
	private boolean isFlagged = false;
	
	public static boolean firstClick;
	public static int numRevealed;
	public static boolean mineRevealed;
	public static boolean gameOver;
	public static MSButton[][] buttons;
	public static int numFlagsRemaining;
	
	public MSButton(String s, int r, int c, int num) {
		
		super(s);
		row = r;
		column = c;
		count = num;
		setFocusable(false);
		setPreferredSize(new Dimension(50, 50));
		
	}
	
	public void setCount(int c) {
		count = c;
	}
	
	public int getCount() {
		return count;
	}
	
	public int getRow() {
		return row;
	}
	
	public int getColumn() {
		return column;
	}
	
	public void reveal() {
		
		if (!isRevealed) {
			
			isRevealed = true;
			setForeground(Color.GRAY);
			setBackground(Color.WHITE);
			setFocusPainted(false);
			setPressedIcon(null);

			if (count == -1) {
				setText("X");
				mineRevealed = true;
				revealAllMines();
			}
			else if (count == 0) {
				setText("");
				this.revealZeros();
			}
			else {
				setText(count + "");
			}
			
			++numRevealed;
			
		}
		
	}
	
	public boolean isRevealed() {
		return isRevealed;
	}
	
	public void toggleFlag() {
		
		if (!isFlagged && numFlagsRemaining > 0) {
			
			isFlagged = true;
			--numFlagsRemaining;
			//setText("F");
			
		}
		else if (isFlagged) {
			
			isFlagged = false;
			++numFlagsRemaining;
			setText("");
			
		}
		
	}
	
	public boolean isFlagged() {
		return isFlagged;
	}
	
	public static void initializeButtonsArrayRef(MSButton[][] ref) {
		buttons = ref;
	}
	
	public static void initializeFreshGame() {
		firstClick = true;
		numRevealed = 0;
		mineRevealed = false;
		gameOver = false;
		numFlagsRemaining = 0;
	}
	
	public MSButton[] getNeighbors() {
		
		MSButton[] neighbors = new MSButton[8];
		
		if (isValid(row - 1, column) && !buttons[row - 1][column].isRevealed) { // add button above
			neighbors[0] = buttons[row - 1][column];
		}
		
		if (isValid(row + 1, column) && !buttons[row + 1][column].isRevealed) { // add button below
			neighbors[1] = buttons[row + 1][column];
		}
		
		if (isValid(row, column - 1) && !buttons[row][column - 1].isRevealed) { // add button to the left
			neighbors[2] = buttons[row][column - 1];
		}
		
		if (isValid(row, column + 1) && !buttons[row][column + 1].isRevealed) { // add button to the right
			neighbors[3] = buttons[row][column + 1];
		}
		
		if (isValid(row - 1, column - 1) && !buttons[row - 1][column - 1].isRevealed) { // add button to the top left
			neighbors[4] = buttons[row - 1][column - 1];
		}
		
		if (isValid(row - 1, column + 1) && !buttons[row - 1][column + 1].isRevealed) { // add button to the top right
			neighbors[5] = buttons[row - 1][column + 1];
		}
		
		if (isValid(row + 1, column - 1) && !buttons[row + 1][column - 1].isRevealed) { // add button to the bottom left
			neighbors[6] = buttons[row + 1][column - 1];
		}
		
		if (isValid(row + 1, column + 1) && !buttons[row + 1][column + 1].isRevealed) { // add button to the bottom right
			neighbors[7] = buttons[row + 1][column + 1];
		}
		
		return neighbors;
		
	}
	
	private boolean isValid(int r, int c) {
	    return r >= 0 && r < buttons.length && c >= 0 && c < buttons[0].length;
	}
	
	private void revealZeros() {
		
		if (count == 0) {
			
			MSButton[] neighbors = this.getNeighbors();
			
			for (MSButton z : neighbors) {
				
				if (z != null) {
					z.reveal();
				}
				
			}
			
		}
		
	}
	
	private static void revealAllMines() {
		
		for (MSButton[] B : MSButton.buttons) {
			
			for (MSButton b : B) {
				
				if (b.getCount() == -1) {
					
					b.isRevealed = true;
					b.setForeground(Color.GRAY);
					b.setBackground(Color.WHITE);
					b.setFocusPainted(false);
					b.setPressedIcon(null);
					b.setText("X");
					
				}
				
			}
			
		}
		
	}
	
	
	
}
