package conwaysGameOfLife;

import java.util.Scanner;

// Starts up the game
public class InitGame {
	
	static Scanner scan = new Scanner(System.in);
	
	public static void main(String[] args) {
		
		// Some context and description
		System.out.println("\n");
		System.out.println("     ----------------------- ");
		System.out.println("    | Conway's Game of Life |");
		System.out.println("     ----------------------- \n");
		System.out.println("  Conway's Game of Life takes place on a grid of cells\n"
						 + "  that may be in either 2 states, dead or alive. Each\n"
						 + "  cell interacts with its 8 neighbors, the cells either\n"
						 + "  vertically, horizontally, or diagonally adjacent to it.\n");
		System.out.println("  The initial or first generation of each game constitutes\n"
						 + "  a seed, and the state of each new generation is reliant\n"
						 + "  on the state of the previous, governed by a few rules.\n\n");
		
		System.out.println("     -------------------- ");
		System.out.println("    | Rules / Conditions |");
		System.out.println("     --------------------\n");
		System.out.println("  Dead Cell:\n\n"
						 + "        There exists exactly 3 neighbors   -> Revives\n");
		System.out.println("  Live Cell:\n\n"
					 	 + "        There exists 2 or 3 live neighbors -> Lives on\n"
						 + "                Less than 3 live neighbors -> Dies\n"
						 + "                More than 3 live neighbors -> Dies\n");
		
		String enter = scan.nextLine();
		System.out.println("    Let's play Conway's Game of Life.");
		enter = scan.nextLine();
		
		Life life = new Life();
		life.initialize();
		
		System.out.println("\n\n  [Enter] Continue  (E) Terminate program");
		enter = scan.nextLine();
		while(!enter.equals("E")) {
			life.displayGen();
			//System.out.print("(E) to terminate program: ");
			enter = scan.nextLine();
		}
		
	}
	
}
