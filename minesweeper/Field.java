package minesweeper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Field {

	private Scanner scan = new Scanner(System.in);
	private Random rand = new Random();
	
	private String diffMode;
	private String endMsg;
	private int diff_x;
	private int diff_y;
	
	private int totalFields;
	private int numMines;
	private int numToClear;
	private int numGuess;

	private List<List<Integer>> mineLocations = new ArrayList<List<Integer>>();
	private List<List<Integer>> searchedLocations = new ArrayList<List<Integer>>();
	
	private int mine_x;
	private int mine_y;
	
	private boolean gameOver = false;
	private boolean gameWin = false;
	
	// Starts the game
	public void initGame() {
		
		printInstructions();
		
		setDifficulty();
		displayField();
		placeMines();
		playGame();	
		
	}
	
	// Prints instructions screen
	private void printInstructions() {
		
		System.out.println("\n\n    MineSweeper\n\n");
		System.out.println("Objective:\n    Clear the field without setting off a mine\n");
		System.out.println("Instructions:\n"
						 + "    Plug in xy-coordinates to search an area.\n"
				         + "    A search may reveal the following info:\n"
						 + "    (Given that you did not set off a mine)\n");
		System.out.println("      o   No mines are present in any adjacent field\n"
						 + "      1   There exists a mine in an adjacent field\n"
						 + "      2   There exists 2 mines in 2 adjacent fields\n"
						 + "      3   There exists 3 mines in 3 adjacent fields\n"
						 + "      4   There exists 4 mines in 4 adjacent fields\n");
		System.out.println("      .   This is an unsearched field\n"
						 + "      X   Mine located (Unfortunately, this means Game Over)\n");
		System.out.println("  How is your deductive reasoning, MineSweeper?\n");
		String start = scan.nextLine();
		do {
			System.out.println("    --------------\n"
							 + "    | GAME START |\n"
							 + "    --------------\n");
		} while(start == "// This is just lazy me avoiding implementing 3rd party stuff just for a key press");
	}
	
	// Gameplay
	private void playGame() {
		
		// Loops through guessing and displaying board until gameOver or gameWin
		while(gameOver == false && gameWin == false) {
			
			searchField();
			displayField();
			
		}
		
		// Some fun endgame quotes
		if(gameOver == true) {
			switch(diffMode) {
			case "Elementary":
				endMsg = "Sometimes, the questions are complicated,\n"
					   + "        "
					   + "and the answers are simple.";
				break;
			case "'L'-Tier":
				endMsg = "The bells are very loud today.";
				break;
			case "Suicide":
				endMsg = "No matter how gifted, you alone cannot change the world.";
				break;
			}	
		} else /*if(gameWin == true)*/ {
			switch(diffMode) {
			case "Elementary":
				endMsg = "If you use your head, you won't get fat\n"
					   + "        "
					   + "even if you eat sweets.";
				break;
			case "'L'-Tier":
				endMsg = "I have two rules. First. I'm never wrong.\n"
					   + "        "
					   + "Second. If I'm wrong, back to the first rule.";
				break;
			case "Suicide":
				endMsg = "Risking your life and doing something that could \n"
					   + "        "
					   + "easily rob you of your life are exact opposites.";
				break;
			}
		}
		
		// Displays after either winning or losing game
		if(gameOver == true) {
			System.out.println("\n    Game Over\n\n");
			if(numGuess == 1) {
				System.out.println("  You lost your life within " + numGuess + " search\n");
			} else {
				System.out.println("  You lost your life within " + numGuess + " searches\n");
			}
			System.out.println("    Your noble effort will not be forgotten...\n\n");
			System.out.println("    Mode: " + diffMode + "\n\n"
							 + "        " + endMsg + "\n\n");
		} else {
			System.out.println("\n    Success\n\n");
			System.out.println("  You cleared the field within " + numGuess + " searches\n");
			System.out.println("    Your deduction skill is notable.\n\n");
			System.out.println("    Mode: " + diffMode + "\n\n"
							 + "        " + endMsg + "\n\n");
		}
		
	}
	
	// Sets difficulty / Sets size of mine field to clear
	private void setDifficulty() {
		
		System.out.println("(E) Easy    (N) Normal  (D) Difficult ");
		System.out.print("\n    Set difficulty: ");
		String diff = scan.nextLine();
		
		while(!diff.equals("E") && !diff.equals("N") && !diff.equals("D")){
			
			System.out.println("\nInvalid difficulty\n");
			
			diff = scan.nextLine();
			
		}
		
		// Sets field sizes / difficulty mode (needed only for fun end game statements) 
		switch(diff) {
		
		case "E":
			diffMode = "Elementary";
			diff_x = 10;
			diff_y = 6;
			break;
			
		case "N":
			diffMode = "'L'-Tier";
			diff_x = 15;
			diff_y = 9;
			break;
		
		case "D":
			diffMode = "Suicide";
			diff_x = 25;
			diff_y = 15;
			break;
		
		}
		
	}
	
	// Displays field
	private void displayField() {
		
		System.out.println("\n");
		System.out.println("   Search " + (numGuess + 1) + "\n\n");
		
		for(int y = (diff_y - 1); y > 0; y--) {
			
			if(y < 10) {
				System.out.print(y + "  ");
			} else {
				System.out.print(y + " ");
			}
			
			if (numGuess > 0) {
				for(int x = 0; x < diff_x; x++) {
					if(mineLocations.contains(Arrays.asList((x + 1), y)) && searchedLocations.contains(Arrays.asList((x + 1), y))) {
						System.out.print("  X  ");
					} else if(searchedLocations.contains(Arrays.asList((x + 1), y))) {
						checkRisk(x + 1, y);
					} else {
						System.out.print("  .  ");
					}
				}
			} else {
				for(int x = 0; x < diff_x; x++) {
					System.out.print("  .  ");
				}
			}
			
			System.out.println("\n\n");
			
		}
		
		System.out.print("   ");
		
		for(int x = 0; x < diff_x; x++) {	
			
			if(x < 9) {
				System.out.print("  " + (x + 1) + "  ");
			} else {
				System.out.print(" " + (x + 1) + "  ");
			}
			
		}
		
		System.out.println("\n");
		
	}

	// Generates number of mines and places them at random locations on field
	private void placeMines() {
		
		totalFields = diff_x * (diff_y - 1);
		numMines = totalFields / 5;
		numToClear = totalFields - numMines;
		
		for (int minePlaced = 0; minePlaced < numMines; ) {
			
			mine_x = rand.nextInt(diff_x) + 1;
			mine_y = rand.nextInt(diff_y - 1) + 1;
			
			if (!mineLocations.contains(Arrays.asList(mine_x, mine_y))) {
				
				List<Integer> mine = new ArrayList<>(Arrays.asList(mine_x, mine_y));
				mineLocations.add(mine);
				
				minePlaced++;
				
			}
			
		}
		
	}
	
	// Allows for user input of xy-coordinates to search locations
	private void searchField(){
		
		System.out.println("\nGuess a coordinate.\n");
		
		System.out.print("  X: ");
		int x_guess = scan.nextInt();
		while(!(x_guess <= diff_x && x_guess > 0)) {
			System.out.println("\nInvalid x-value\n");
			x_guess = scan.nextInt();
		}
		
		System.out.print("  Y: ");
		int y_guess = scan.nextInt();
		while(!(y_guess <= (diff_y - 1) && y_guess > 0)) {
			System.out.println("\nInvalid y-value\n");
			y_guess = scan.nextInt();
		}
		
		List<Integer> search = new ArrayList<>(Arrays.asList(x_guess, y_guess));
		numGuess++;
		
		if(mineLocations.contains(Arrays.asList(x_guess, y_guess))) {
			searchedLocations.add(search);
			gameOver = true;
		} else {
			if(!searchedLocations.contains(Arrays.asList(x_guess, y_guess))) {
				searchedLocations.add(search);
			}
		}
		
	}
	
	// Checks surrounding fields and gives number of adjacent mines, if any
	private void checkRisk(int x, int y) {
		
		int xCheck = x;
		int yCheck = y;

		if(xCheck == 1) {
			if(yCheck == 1) {
				if(mineLocations.contains(Arrays.asList(xCheck, yCheck + 1)) &&
						mineLocations.contains(Arrays.asList(xCheck + 1, yCheck))) {
					System.out.print("  2  ");
				} else if(mineLocations.contains(Arrays.asList(xCheck, yCheck + 1))) {
					System.out.print("  1  ");
				} else if(mineLocations.contains(Arrays.asList(xCheck + 1, yCheck))) {
					System.out.print("  1  ");
				} else {
					System.out.print("  o  ");
				}
			} else {
				if(mineLocations.contains(Arrays.asList(xCheck, yCheck + 1)) &&
						mineLocations.contains(Arrays.asList(xCheck + 1, yCheck)) &&
						mineLocations.contains(Arrays.asList(xCheck, yCheck - 1))){
					System.out.print("  3  ");
				} else if(mineLocations.contains(Arrays.asList(xCheck, yCheck + 1)) &&
						mineLocations.contains(Arrays.asList(xCheck, yCheck - 1))) {
					System.out.print("  2  ");
				} else if(mineLocations.contains(Arrays.asList(xCheck, yCheck + 1)) &&
						mineLocations.contains(Arrays.asList(xCheck + 1, yCheck))) {
					System.out.print("  2  ");
				} else if(mineLocations.contains(Arrays.asList(xCheck + 1, yCheck)) &&
						mineLocations.contains(Arrays.asList(xCheck, yCheck  - 1))) {
					System.out.print("  2  ");
				} else if(mineLocations.contains(Arrays.asList(xCheck, yCheck + 1))) {
					System.out.print("  1  ");
				} else if(mineLocations.contains(Arrays.asList(xCheck + 1, yCheck))) {
					System.out.print("  1  ");
				} else if(mineLocations.contains(Arrays.asList(xCheck, yCheck - 1))) {
					System.out.print("  1  ");
				} else {
					System.out.print("  o  ");
				}
			}
		} else if(xCheck > 1) {
			if(yCheck == 1) {
				if(mineLocations.contains(Arrays.asList(xCheck - 1, yCheck)) &&
						mineLocations.contains(Arrays.asList(xCheck, yCheck + 1)) &&
						mineLocations.contains(Arrays.asList(xCheck + 1, yCheck))){
					System.out.print("  3  ");
				} else if (mineLocations.contains(Arrays.asList(xCheck - 1, yCheck)) &&
						mineLocations.contains(Arrays.asList(xCheck + 1, yCheck))) {
					System.out.print("  2  ");
				} else if(mineLocations.contains(Arrays.asList(xCheck - 1, yCheck)) &&
						mineLocations.contains(Arrays.asList(xCheck, yCheck + 1))) {
					System.out.print("  2  ");
				} else if(mineLocations.contains(Arrays.asList(xCheck, yCheck + 1)) &&
						mineLocations.contains(Arrays.asList(xCheck + 1, yCheck))){
					System.out.print("  2  ");
				} else if(mineLocations.contains(Arrays.asList(xCheck - 1, yCheck))) {
					System.out.print("  1  ");
				} else if(mineLocations.contains(Arrays.asList(xCheck, yCheck + 1))) {
					System.out.print("  1  ");
				} else if(mineLocations.contains(Arrays.asList(xCheck + 1, yCheck))) {
					System.out.print("  1  ");
				} else {
					System.out.print("  o  ");
				}
			} else {
				if(mineLocations.contains(Arrays.asList(xCheck, yCheck + 1)) &&
						mineLocations.contains(Arrays.asList(xCheck + 1, yCheck)) &&
						mineLocations.contains(Arrays.asList(xCheck, yCheck - 1)) &&
						mineLocations.contains(Arrays.asList(xCheck - 1, yCheck))){
					System.out.print("  4  ");
				} else if(mineLocations.contains(Arrays.asList(xCheck, yCheck + 1)) &&
						mineLocations.contains(Arrays.asList(xCheck + 1, yCheck)) &&
						mineLocations.contains(Arrays.asList(xCheck, yCheck - 1))){
					System.out.print("  3  ");
				} else if(mineLocations.contains(Arrays.asList(xCheck + 1, yCheck)) &&
					mineLocations.contains(Arrays.asList(xCheck, yCheck - 1)) &&
						mineLocations.contains(Arrays.asList(xCheck - 1, yCheck))) {
					System.out.print("  3  ");
				} else if(mineLocations.contains(Arrays.asList(xCheck, yCheck - 1)) &&
						mineLocations.contains(Arrays.asList(xCheck - 1, yCheck)) &&
						mineLocations.contains(Arrays.asList(xCheck, yCheck + 1))) {
					System.out.print("  3  ");
				} else if(mineLocations.contains(Arrays.asList(xCheck - 1, yCheck)) &&
						mineLocations.contains(Arrays.asList(xCheck, yCheck + 1)) &&
						mineLocations.contains(Arrays.asList(xCheck + 1, yCheck))) {
					System.out.print("  3  ");
				} else if(mineLocations.contains(Arrays.asList(xCheck, yCheck + 1)) &&
						mineLocations.contains(Arrays.asList(xCheck, yCheck - 1))) {
					System.out.print("  2  ");
				} else if(mineLocations.contains(Arrays.asList(xCheck - 1, yCheck)) &&
						mineLocations.contains(Arrays.asList(xCheck + 1, yCheck))) {
					System.out.print("  2  ");
				} else if(mineLocations.contains(Arrays.asList(xCheck, yCheck + 1)) &&
						mineLocations.contains(Arrays.asList(xCheck + 1, yCheck))){
					System.out.print("  2  ");
				} else if(mineLocations.contains(Arrays.asList(xCheck + 1, yCheck)) &&
						mineLocations.contains(Arrays.asList(xCheck, yCheck - 1))) {
					System.out.print("  2  ");
				} else if(mineLocations.contains(Arrays.asList(xCheck - 1, yCheck)) &&
						mineLocations.contains(Arrays.asList(xCheck, yCheck + 1))) {
					System.out.print("  2  ");
				} else if(mineLocations.contains(Arrays.asList(xCheck, yCheck + 1))) {
					System.out.print("  1  ");
				} else if(mineLocations.contains(Arrays.asList(xCheck + 1, yCheck))) {
					System.out.print("  1  ");
				} else if(mineLocations.contains(Arrays.asList(xCheck, yCheck - 1))) {
					System.out.print("  1  ");
				} else if(mineLocations.contains(Arrays.asList(xCheck - 1, yCheck))) {
					System.out.print("  1  ");
				} else {
					System.out.print("  o  ");
				}
			}
		}

		// If all mine-free locations are cleared/searched then gameWin
		if(searchedLocations.size() == numToClear) {
			gameWin = true;
		}
		
	}
		
}

