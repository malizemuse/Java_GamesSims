package rockPaperScissorsBattle;

import java.util.*;

public class Simulation {

	Scanner scan = new Scanner(System.in);
	
	Scissors scissors = new Scissors();
	Paper paper = new Paper();
	Rock rock = new Rock();
	
	protected static final int xField = 145;
	protected static final int yField = 30;
	//private static final int totalField = xField * (yField - 1); // Not needed but leaving totalField here just in case
	
	protected static int numMoves;
	
	static List<List<Integer>> occupied = new ArrayList<List<Integer>>();

	private boolean gameOver = false;
	
	
	// Initializes the game
	public void initGame() {
		
		setSettings();
		placeFieldObjects();
		
		// Loop allows user to keep playing until gameOver
		System.out.println();
		while(gameOver != true) {
			checkScreen();
			System.out.println();
			}
			
	}
	
	// Checks for proper display of end message if gameOver
	public void checkScreen() {
		
		while(gameOver != true) {
			
			if(Paper.locations.isEmpty()) {
				gameOver = true;
				newTurn();
				System.out.println("\n        Victory of the Rocks!\n"
								 + "\n        With no more Paper to stand against them,"
								 + "\n        the Rocks will defeat the remaining Scissors.");
			} else if(Rock.locations.isEmpty()) {
				gameOver = true;
				newTurn();
				System.out.println("\n        Victory of the Scissors!\n"
								 + "\n        With no more Rocks to stand against them,"
								 + "\n        the Scissors will defeat the remaining Paper.");
			} else if(Scissors.locations.isEmpty()) {
				gameOver = true;
				newTurn();	
				System.out.println("\n        Victory of the Paper!\n"
								 + "\n        With no more Scissors to stand against them,"
								 + "\n        the Paper will defeat the remaining Rock.");		
			} else {
				newTurn();
				String enter = scan.nextLine();
				if(enter.equals("// Lazy key press; Want to get rid of the compiler warning")) {}
			}
		}
		
	}
	
	// Displays field, updates eaten soldiers, executes soldier actions
	public void newTurn() {
		
		displayField();
		updateEaten();

		rock.rockAct();
		paper.paperAct();
		scissors.scissorsAct();
		
	}
	
	// Updates eaten soldiers by checking if two soldiers occupy one space
	private void updateEaten() {
		
		// Checks if rock eaten by paper
		for(int pred = 0; pred < Paper.locations.size(); pred++) {
			for(int prey = 0; prey < Rock.locations.size(); prey++) {
				if(Paper.locations.get(pred).equals(Rock.locations.get(prey))) {
					Rock.locations.remove(prey);
				}
			}
		}
		
		// Checks if paper eaten eaten by scissors
		for(int pred = 0; pred < Scissors.locations.size(); pred++) {
			for(int prey = 0; prey < Paper.locations.size(); prey++) {
				if(Scissors.locations.get(pred).equals(Paper.locations.get(prey))) {
					Paper.locations.remove(prey);
				}
			}
		}
		
		// Checks if scissors eaten by rock
		for(int pred = 0; pred < Rock.locations.size(); pred++) {
			for(int prey = 0; prey < Scissors.locations.size(); prey++) {
				if(Rock.locations.get(pred).equals(Scissors.locations.get(prey))) {
					Scissors.locations.remove(prey);
				}
			}
		}
		
	}
	
	// Displays field; Checks field values to display appropriate symbol
	protected void displayField() {
		
		numMoves++;
		// Optional display of moves
		//System.out.println("\n\n        Move " + numMoves);
		
		System.out.print("\n      ");
		for(int x = 0; x < xField; x++) {	
			System.out.print("_");
		}
		System.out.println("\n");
		for(int y = (yField); y > 0; y--) {			
			System.out.print("    | ");
			for(int x = xField; x > 0; x--) {
				if(Rock.locations.contains(Arrays.asList(x, y))) { // Resource
					System.out.print(Rock.symbol);
				} else if(Paper.locations.contains(Arrays.asList(x, y))) { // Prey
					System.out.print(Paper.symbol);
				} else if(Scissors.locations.contains(Arrays.asList(x, y))) { // Predator
					System.out.print(Scissors.symbol);
				} else { // Empty field
					System.out.print(" ");
				}
			}
			System.out.print(" |\n");
		}
		System.out.print("\n      ");
		for(int x = 0; x < xField; x++) {		
			System.out.print("_");
		}
		System.out.println("\n");
		
	}
	
	// Initializes number of soldiers for each nation
	private void setSettings() {
		
		System.out.println("	Number of each field object:\n"
						 + "		( Limit: 20-100 )\n");
		System.out.print("		o  Rock:     ");
		rock.setNumObj(scan.nextInt());
		while(rock.getNumObj() > 100) {
			System.out.print("		Exceeds limit  ->  ");
			rock.setNumObj(scan.nextInt());
		}
		while(rock.getNumObj() < 20) {
			System.out.print("		Invalid number ->  ");
			rock.setNumObj(scan.nextInt());
		}
		System.out.print("		~  Paper:    ");
		paper.setNumObj(scan.nextInt());
		while(paper.getNumObj() > 100) {
			System.out.print("		Exceeds limit  ->  ");
			paper.setNumObj(scan.nextInt());
		}
		while(paper.getNumObj() < 20) {
			System.out.print("		Invalid number ->  ");
			paper.setNumObj(scan.nextInt());
		}
		System.out.print("		x  Scissors: ");
		scissors.setNumObj(scan.nextInt());
		while(scissors.getNumObj() > 100) {
			System.out.print("		Exceeds limit  ->  ");
			scissors.setNumObj(scan.nextInt());
		}
		while(scissors.getNumObj() < 20) {
			System.out.print("		Invalid number ->  ");
			scissors.setNumObj(scan.nextInt());
		}

	}
	
	// Places all nations' randomly-generated soldiers onto the field
	private void placeFieldObjects() {
		
		rock.placeSoldiers();
		paper.placeSoldiers();
		scissors.placeSoldiers();
		
	}
	
	
}

