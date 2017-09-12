package conwaysGameOfLife;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

// Class that recreates Conway's Game of Life
public class Life {
	
	Random rand = new Random();
	Scanner scan = new Scanner(System.in);
	
	private static final String liveCell = "o";
	private static final String deadCell = " "; // Should not have to worry about neighbors on borders
	private static final String unCell = "x";
	
	private int xRange;
	private int yRange;
	
	// List of seed live cell coordinates
	private List<List<Integer>> seed = new ArrayList<List<Integer>>();
	
	// List of live and dead coordinates
	public List<List<Integer>> live = new ArrayList<List<Integer>>();
	public List<List<Integer>> dead = new ArrayList<List<Integer>>();
	
	private static int gen; // Represents generation / number of ticks
	
	// Constructor; Initalizes deadList to all valid coordinates
	public Life() {
		
		xRange = 20;
		yRange = 10;
		for(int x = 1; x < (xRange + 1); x++) {
			for(int y = 1; y < (yRange + 1); y++) {
				dead.add(Arrays.asList(x,y));
			}
		}
		
	}
	
	// Initializes seed, liveList, and grid; Confirmation
	public void initialize() {
		
		String confirm = "";
		while(!confirm.equals("Y")) {
			
			confirm = "";
			// Clears previous initialization of seed
			seed.clear();
			// Sends all coordinates in live back to dead
			for(List<Integer> coord: live) {
				if(!dead.contains(coord))
					dead.add(coord);
			}
			// Reset live
			live.clear();
			
			initSeed();
			
			System.out.println("\n\n    Seed");
			displayGrid();
			
			while(!confirm.equals("Y") && !confirm.equals("N")) {
				System.out.print("\n    (Y) or (N)  Confirm: ");
				confirm = scan.nextLine();
			}
			
		}
		
	}
	
	// Initializes seed with coordinates; Assigns to liveList
	private void initSeed() {
		
		// Input for seed coordinates
		int xSeed = 0;
		int ySeed = 0;
		
		System.out.print("\n    Create a Seed.\n\n");
		
		String create = "";
		while(!create.equals("R") && !create.equals("M")) {
			
			System.out.println("  (R) Randomize  (M) Manual Input");
			create = scan.nextLine();
			
			if(create.equals("R")) { // Randomize
				
				int gridArea = xRange * yRange;
				System.out.println("  Grid Area: " + gridArea + "\n"
								 + "  Grid Dimensions: "
								 + xRange + " x " + yRange + "\n");
				
				System.out.print("  Number of seed coordinates to generate: ");
				int generate = scan.nextInt();
				
				for (int toGen = 0; toGen < generate; ) {
					
					xSeed = rand.nextInt(xRange) + 1;
					ySeed = rand.nextInt(yRange) + 1;
					List<Integer> coord = new ArrayList<>(Arrays.asList(xSeed, ySeed));
					
					if(!seed.contains(coord)) {
						seed.add(coord);
						toGen++;
					}
				}
				String consume = scan.nextLine(); // Consumes \n not taken by previous scan.nextInt()
				if(consume.equals("")) {} // Strictly to geet rid of compiler warning
				
			} else if(create.equals("M")) { // Manually input seed
				
				System.out.print("  [Enter] to continue  (E) to end input: ");
				String endInput = scan.nextLine();
				while(!endInput.equals("E")){
					System.out.print("  X: ");
					xSeed = scan.nextInt();
					while(!(xSeed > 0) || !(xSeed <= xRange)) {
						System.out.println("  Invalid x-coordinate.");
						System.out.print("  X: ");
						xSeed = scan.nextInt();
					}
					System.out.print("  Y: ");
					ySeed = scan.nextInt();
					while(!(ySeed > 0) || !(ySeed <= yRange)) {
						System.out.println("  Invalid y-coordinate.");
						System.out.print("  Y: ");
						ySeed = scan.nextInt();
					}
					if(!seed.contains(Arrays.asList(xSeed, ySeed))) {
						seed.add(Arrays.asList(xSeed, ySeed));
						System.out.println("    Seed coordinate inputted.");
					} else {
						System.out.println("    Redundant seed coordinate.");
					}
					System.out.print("\n  [Enter] to continue  (E) to end input: ");
					endInput = scan.nextLine(); // Consumes \n not taken by previous scan.nextInt()
					endInput = scan.nextLine();
					
				}
			}
			
		}
		
		// Transfers seed coordinates to live
		for(List<Integer> coord: seed) {
			live.add(coord);
			dead.remove(coord);
		}
	}
	
	// Displays generation
	public void displayGen(){
		tick();
		gen++;
		System.out.println("\n      Gen " + gen);
		displayGrid();
	}
	
	// Displays grid
	public void displayGrid() {
		
		System.out.println();
		System.out.print("      ");
		for(int x = 1; x < (xRange + 1); x++) {
			System.out.print("-");
		}
		System.out.println();
		for(int y = yRange; y > 0; y--) {
			System.out.print("    | ");
			for(int x = 1; x < (xRange + 1); x++) {
				if(live.contains(Arrays.asList(x,y))) {
					System.out.print(liveCell);
				} else if (dead.contains(Arrays.asList(x,y))){
					System.out.print(deadCell);
				} else {
					System.out.print(unCell);
				}
			}
			System.out.print(" |    \n");
		}
		
		System.out.print("      ");
		for(int x = 1; x < (xRange + 1); x++) {
			System.out.print("-");
		}
		System.out.println();
		
	}
	
	// Where all cell states are checked and decided for the next generation
	private void tick() {
		
		// Temporary lists to transfer cell coordinates to live and die lists
		List<List<Integer>> cellToDie = new ArrayList<List<Integer>>();
		List<List<Integer>> cellToLive = new ArrayList<List<Integer>>();
		
		// Checks live cell conditions
		for(int coord = 0; coord < live.size(); coord++) {
			
			int nbCount = 0; // Neighbor count
			List<Integer> cell = live.get(coord); // Checking this cell's neighbors
			
			if(live.contains(Arrays.asList(cell.get(0), cell.get(1) + 1))) nbCount++;
			if(live.contains(Arrays.asList(cell.get(0), cell.get(1) - 1))) nbCount++;
			if(live.contains(Arrays.asList(cell.get(0) + 1, cell.get(1)))) nbCount++;
			if(live.contains(Arrays.asList(cell.get(0) - 1, cell.get(1)))) nbCount++;
			
			if(live.contains(Arrays.asList(cell.get(0) + 1, cell.get(1) + 1)))
				nbCount++;
			if(live.contains(Arrays.asList(cell.get(0) + 1, cell.get(1) - 1)))
				nbCount++;
			if(live.contains(Arrays.asList(cell.get(0) - 1, cell.get(1) + 1)))
				nbCount++;
			if(live.contains(Arrays.asList(cell.get(0) - 1, cell.get(1) - 1)))
				nbCount++;
			
			// Live cell conditions to execute
			if(nbCount < 2) { // Live cell dies
				if(!cellToDie.contains(cell))
					cellToDie.add(cell);
			} else if(nbCount > 3) { // Live cell dies
				if(!cellToDie.contains(cell))
					cellToDie.add(cell);
			} else if(nbCount == 2 || nbCount == 3) {
				// Live cell lives on
			}
			
		}
		
		// Checks dead cell conditions
		for(int coord = 0; coord < dead.size(); coord++) {
			
			int nbCount = 0; // Neighbor count
			List<Integer> cell = dead.get(coord); // Checking this cell's neighbors
			
			if(live.contains(Arrays.asList(cell.get(0), cell.get(1) + 1))) nbCount++;
			if(live.contains(Arrays.asList(cell.get(0), cell.get(1) - 1))) nbCount++;
			if(live.contains(Arrays.asList(cell.get(0) + 1, cell.get(1)))) nbCount++;
			if(live.contains(Arrays.asList(cell.get(0) - 1, cell.get(1)))) nbCount++;
			
			if(live.contains(Arrays.asList(cell.get(0) + 1, cell.get(1) + 1)))
				nbCount++;
			if(live.contains(Arrays.asList(cell.get(0) + 1, cell.get(1) - 1)))
				nbCount++;
			if(live.contains(Arrays.asList(cell.get(0) - 1, cell.get(1) + 1)))
				nbCount++;
			if(live.contains(Arrays.asList(cell.get(0) - 1, cell.get(1) - 1)))
				nbCount++;
			
			// Dead cell conditions to execute
			if(nbCount == 3) { // Dead cell comes to life
				cellToLive.add(cell);
			}
		
		}
		
		// cellToDie = Live cells to die
		for(List<Integer> cell: cellToDie) {
			if(live.contains(cell))
				live.remove(cell);
			if(!dead.contains(cell))
				dead.add(cell);
		}
		
		// cellToLive = Dead cells to come to life
		for(List<Integer> cell: cellToLive) {
			if(dead.contains(cell))
				dead.remove(cell);
			if(!live.contains(cell))
				live.add(cell);
		}
		
	}
	
}
