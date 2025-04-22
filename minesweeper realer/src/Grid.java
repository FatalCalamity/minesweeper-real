import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
@SuppressWarnings("unused")

public class Grid {

	private boolean[][] bombGrid;
	private int[][] countGrid;
	private int numRows;
	private int numColumns;
	private int numBombs;
	
	public Grid() { // default grid constructor, 10x10, 25 bombs
		
		numRows = 10;
		numColumns = 10;
		numBombs = 25;
		
	}
	
	public Grid(int rows, int columns) { // grid constructor with 25 bombs and custom size, must be at least of size 64
		
		if ((rows * columns) < 64 || rows < 0 || columns < 0) {
			numRows = 8;
			numColumns = 8;
		}
		else {
			numRows = rows;
			numColumns = columns;
		}
		
		numBombs = 25;
		
	}
	
	public Grid(int rows, int columns, int bombs) { // grid constructor with custom size and custom number of bombs, must be at least 4x4, number of bombs must be at least 1 and at most half the grid size
		
		if (rows < 4 || columns < 4) {
			numRows = 4;
			numColumns = 4;
		}
		else {
			numRows = rows;
			numColumns = columns;
		}
		
		if (bombs < 1) {
			numBombs = 1;
		}
		else if (bombs > (numRows * numColumns) / 2) {
			numBombs = (numRows * numColumns) / 2;
		}
		else {
			numBombs = bombs;
		}
		
	}
	
	public int getNumRows() {
		return numRows;
	}
	
	public int getNumColumns() {
		return numColumns;
	}
	
	public int getNumBombs() {
		return numBombs;
	}
	
	private void createBombGrid(int r, int c) { // creates grid of bombs based on numRows, numColumns, and numBombs, which are all validly set by the constructors - CALLED BY THE FIRST CLICK IN THE GAME, WILL GENERATE A BOMB ARRAY THAT IS ALWAYS SAFE ON THE FIRST CLICK
		
		bombGrid = new boolean[numRows][numColumns];
		
		ArrayList<Integer> randomValues = new ArrayList<Integer>();
		int row, column;
		
		for (int i = 0; i < (numRows * numColumns); ++i) {
			
			if (i == r * numColumns + c || i == (r + 1) * numColumns + c || i == (r - 1) * numColumns + c || i == r * numColumns + c + 1 || i == (r + 1) * numColumns + c + 1 || i == (r - 1) * numColumns + c + 1 || i == r * numColumns + c - 1 || i == (r + 1) * numColumns + c - 1 || i == (r - 1) * numColumns + c - 1) {
				continue;
			}
			
			randomValues.add(i);
			
		}
		
		Collections.shuffle(randomValues);
		
		
		for (int i = 0; i < numBombs; ++i) {
			
			row = randomValues.get(i) / numColumns;
			column = randomValues.get(i) % numColumns;
			
			bombGrid[row][column] = true;
			
		}
		
	}
	
	private void createCountGrid() { // right now this places a -1 on grids with bombs, may change for zybook test cases
		
		countGrid = new int[numRows][numColumns];
		
		for (int i = 0; i < numRows; ++i) {
			
			for (int j = 0; j < numColumns; ++j) {
				
				int count = 0;
				
				if (bombGrid[i][j]) { // if cell contains a bomb, set to -1
					countGrid[i][j] = -1;
				}
				else {
					
					if (i != 0 && bombGrid[i - 1][j]) { // check cell above
						++count;
					}
					
					if (i != (numRows - 1) && bombGrid[i + 1][j]) { // check cell below
						++count;
					}
					
					if (j != 0 && bombGrid[i][j - 1]) { // check cell to the left
						++count;
					}
					
					if (j != (numColumns - 1) && bombGrid[i][j + 1]) { // check cell to the right
						++count;
					}
					
					if (i != 0 && j != 0 && bombGrid[i - 1][j - 1]) { // check cell to the top left
						++count;
					}
					
					if (i != 0 && j != (numColumns - 1) && bombGrid[i - 1][j + 1]) { // check cell to the top left
						++count;
					}
					
					if (i != (numRows - 1) && j != 0 && bombGrid[i + 1][j - 1]) { // check cell to the bottom left
						++count;
					}
					
					if (i != (numRows - 1) && j != (numColumns - 1) && bombGrid[i + 1][j + 1]) { // check cell to the bottom right
						++count;
					}
					
					countGrid[i][j] = count;
					
				}
				
				
			}
			
		}
		
		
	}
	
	public boolean[][] getBombGrid() {
		
		boolean[][] bombGridClone = new boolean[numRows][numColumns];
		
		for (int i = 0; i < numRows; ++i) {
			for (int j = 0; j < numColumns; ++j) {
				bombGridClone[i][j] = bombGrid[i][j];
			}
		}
		
		return bombGridClone;
		
	}
	
	public int[][] getCountGrid() {
		
		int[][] countGridClone = new int[numRows][numColumns];
		
		for (int i = 0; i < numRows; ++i) {
			for (int j = 0; j < numColumns; ++j) {
				countGridClone[i][j] = countGrid[i][j];
			}
		}
		
		return countGridClone;
		
	}
	
	public boolean isBombAtLocation(int row, int column) {
		return bombGrid[row][column];
	}
	
	public int getCountAtLocation(int row, int column) {
		return countGrid[row][column];
	}
	
	public void initializeGrid(int r, int c) {
		
		createBombGrid(r, c);
		createCountGrid();
		
	}

	
	
	
	
}