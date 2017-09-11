package rockPaperScissorsBattle;

import java.util.*;

public class Scissors {
	
	Scanner scan = new Scanner(System.in);
	Random rand = new Random();
	
	static final String symbol = "x";
	static List<List<Integer>> locations = new ArrayList<List<Integer>>();
	
	// Used to determine whether necessary for prey to run away
	private List<Integer> nearestList = new ArrayList<Integer>();
	private List<List<Integer>> noEscList = new ArrayList<List<Integer>>();
	private List<List<Integer>> restList = new ArrayList<List<Integer>>();
	
	private int numObj;
	private int objPlaced;
	private int initNumObj;
	
	private int endurance = 8; // Sets endurance
	private int restReq = 4; // Sets rest required to recover endurance
	private static int endurCount = 0;
	private static int restCount = 0;
	
	private static int spawnTimer;
	private int numReinforce;
	
	
	protected void setNumObj(int num) {
		numObj = num;
		initNumObj = num;
	}
	
	protected int getNumObj() {
		return numObj;
	}

	// No need for getter
	protected void setNumReinforce(int num) {
		numReinforce = num;
	}
	
	// Randomly generates soldier locations; Called in Simulation.placeFieldObjects
	protected void placeSoldiers() {
		
		for (objPlaced = 0; objPlaced < numObj; ) {
			
			int xLoc = rand.nextInt(Simulation.xField) + 1;
			int yLoc = rand.nextInt(Simulation.yField) + 1;
			
			if (!locations.contains(Arrays.asList(xLoc, yLoc)) &&
				!Simulation.occupied.contains(Arrays.asList(xLoc, yLoc))) {
				
				List<Integer> location = new ArrayList<>(Arrays.asList(xLoc, yLoc));
				locations.add(location);
				Simulation.occupied.add(location);
				
				objPlaced++;
				
			}
		}
	}
	
	// All actions taken for a turn; Called in Simulation.newTurn()
	protected void scissorsAct() {
		
		checkTired(); // Checks if soldier not allowed to move this turn
		reinforce(); // Increments counter for or generates reinforcements
		checkNoEsc(); // Checks if necessary to run; If not no run in escFromPred
		escFromRock(); // If pred is close enough, go in the opposite direction
		getToPaper(); // Chases down prey
		
		// These lists are cleared after each turn
		nearestList.clear(); // Assists noEscList
		noEscList.clear(); // List of rocks that do not need to escape
		restList.clear(); // List of rocks that must rest
		
	}
	
	protected void reinforce() {
		
		spawnTimer++;
		numReinforce = initNumObj/4;
		
		if(spawnTimer >= 50 && Simulation.numMoves <= 100
		&& locations.size() < initNumObj/2 && locations.size() > 0) {
			for (int s = 0; s < numReinforce; ) {
				int xLoc = rand.nextInt(Simulation.xField) + 1;
				int yLoc = rand.nextInt(Simulation.yField) + 1;
				if (!locations.contains(Arrays.asList(xLoc, yLoc)) &&
					!Simulation.occupied.contains(Arrays.asList(xLoc, yLoc))) {
					List<Integer> location = new ArrayList<>(Arrays.asList(xLoc, yLoc));
					locations.add(location);
					Simulation.occupied.add(location);
					s++;			
				}
			}
			System.out.println("        Scissors army called in reinforcements!");
			String enter = scan.nextLine();
			if(enter.equals("// Lazy key press; Want to get rid of the compiler warning")) {}
			spawnTimer = 0;
		}
		
	}
	
	protected void checkTired() {
		
		if(endurCount <= endurance) {
			endurCount++;
		} else {
			if(restCount <= restReq) {
				for(int p = 0; p < locations.size(); p++) {
					restList.add(locations.get(p));
				}
				restCount++;
			} else {
				endurCount = 0;
				restCount = 0;
				restList.clear();
			}
		}
		
	}
	
	private void checkNoEsc(){
		
		findNearestRock();
		// Checks if predator is far away; No need to escape if so for prey
		for(int n = 0; n < nearestList.size(); n++) {
			if(nearestList.get(n) > 7) {
				noEscList.add(locations.get(n));
			}
		}
		
	}
	
	protected void escFromRock() {
		
		List<Integer> predToEsc = new ArrayList<Integer>(findNearestRock());
		
		// Loop through pred prey pairs
		for(int chase = 0; chase < predToEsc.size(); chase++) {
			
			List<Integer> prey = new ArrayList<Integer>(locations.get(chase));
			List<Integer> pred = new ArrayList<Integer>(Rock.locations.get(predToEsc.get(chase)));
			
			if(!noEscList.contains(locations.get(chase)) && !restList.contains(locations.get(chase))) {
				
				// Loop through each coord and make coord change
				for(int coord = 0; coord < 2; coord++) {
					
					int move = pred.get(coord) - prey.get(coord);
					
					// Ensure no overlapping prey field objects
					if(move > 0
							&& !locations.contains((Arrays.asList(locations.get(chase).get(0), locations.get(chase).get(1) - 1)))
							&& !locations.contains((Arrays.asList(locations.get(chase).get(0) - 1, locations.get(chase).get(1))))) { //
						if(!(locations.get(chase).get(coord) - 1 < 1)) {
							locations.get(chase).set(coord, locations.get(chase).get(coord) - 1);
						}
					} else if (move < 0
							&& !locations.contains((Arrays.asList(locations.get(chase).get(0), locations.get(chase).get(1) + 1)))
							&& !locations.contains((Arrays.asList(locations.get(chase).get(0) + 1, locations.get(chase).get(1))))) { //
						if(coord == 0) {
							if(!(locations.get(chase).get(coord) + 1 > Simulation.xField)) {
								locations.get(chase).set(coord, locations.get(chase).get(coord) + 1);
							}
						} else {
							if(!(locations.get(chase).get(coord) + 1 > Simulation.yField)) {
								locations.get(chase).set(coord, locations.get(chase).get(coord) + 1);	
							}
						}
					} // Else no need to move along this axis	
				}
			}
		}
		
	}
	
	// Finds nearest pred
	private List<Integer> findNearestRock() {
		
		List<List<Integer>> predList = new ArrayList<List<Integer>>(findDistsFromRock());
		List<Integer> predIndexList = new ArrayList<Integer>();
		
		for(int dists = 0; dists < predList.size(); dists++) {
			
			int nearest = Simulation.xField;
			int nearIndex = Simulation.xField;
			
			for(int dist = 0; dist < predList.get(dists).size(); dist++) {
				
				if(predList.get(dists).get(dist) < nearest) {
					nearest = predList.get(dists).get(dist);
					nearIndex = dist;
				}
				
			}
			
			if(nearest != Simulation.xField && nearIndex != Simulation.xField) {
				predIndexList.add(nearIndex);
				nearestList.add(nearest); // Used for noEscList
			}
			
		}
		
		return(predIndexList);
		
	}
	
	private List<List<Integer>> findDistsFromRock() {
		
		List<List<Integer>> fromPredDists = new ArrayList<List<Integer>>();
		
		for(int xPrey = 0; xPrey < (locations.size()); xPrey++) {
			
			List<List<Integer>> check2 = new ArrayList<List<Integer>>();
			
			for(int yPrey = 0; yPrey < (locations.get(xPrey).size()); yPrey++) {
				
				List<Integer> check1 = new ArrayList<>();
				int coordPrey = locations.get(xPrey).get(yPrey);
				
				for(int xPred = 0; xPred < Rock.locations.size(); xPred++) {
					
					int coordPred = Rock.locations.get(xPred).get(yPrey);
					int diff = Math.abs(coordPred - coordPrey);
					check1.add(diff);
					
				}
				
				check2.add(check1);
				
			}
			
			// Separated into x and y lists
			List<Integer> xSep = new ArrayList<>(check2.get(0));
			List<Integer> ySep = new ArrayList<>(check2.get(1));
			
			List<Integer> distances = new ArrayList<>();
			
			for(int diff = 0; diff < xSep.size(); diff++) {
				
				int dist = xSep.get(diff) + ySep.get(diff);
				distances.add(dist);
				
			}
			
			fromPredDists.add(distances);
		}
		return(fromPredDists);
	}
	
	protected void getToPaper() {
	
		List<Integer> preyToChase = new ArrayList<Integer>(findNearestPaper());
		
		// Loop through pred prey pairs
		for(int chase = 0; chase < preyToChase.size(); chase++) {
			
			List<Integer> pred = new ArrayList<Integer>(locations.get(chase));
			List<Integer> prey = new ArrayList<Integer>(Paper.locations.get(preyToChase.get(chase)));
			
			if(!restList.contains(locations.get(chase))) {
				
				// Loop through each coord and make coord change
				for(int coord = 0; coord < 2; coord++) {
					
					int move = prey.get(coord) - pred.get(coord);
					
					if(move < 0) {
						if(!(locations.get(chase).get(coord) - 1 < 1)
								&& !locations.contains((Arrays.asList(locations.get(chase).get(0), locations.get(chase).get(1) - 1)))
								&& !locations.contains((Arrays.asList(locations.get(chase).get(0) - 1, locations.get(chase).get(1))))) {
							locations.get(chase).set(coord, locations.get(chase).get(coord) - 1);
						}
					} else if (move > 0) {
						if(coord == 0) {
							if(!(locations.get(chase).get(coord) + 1 > Simulation.xField)
									&& !locations.contains((Arrays.asList(locations.get(chase).get(0), locations.get(chase).get(1) + 1)))
									&& !locations.contains((Arrays.asList(locations.get(chase).get(0) + 1, locations.get(chase).get(1))))) {
								locations.get(chase).set(coord, locations.get(chase).get(coord) + 1);
							}
						} else {
							if(!(locations.get(chase).get(coord) + 1 > Simulation.yField)
									&& !locations.contains((Arrays.asList(locations.get(chase).get(0), locations.get(chase).get(1) + 1)))
									&& !locations.contains((Arrays.asList(locations.get(chase).get(0) + 1, locations.get(chase).get(1))))) {
								locations.get(chase).set(coord, locations.get(chase).get(coord) + 1);	
							}
						}
						
					} // Else no need to move along this axis
				}
			}
		}
	}
	
	// Finds nearest prey
	private List<Integer> findNearestPaper() {
		
		List<List<Integer>> preyList = new ArrayList<List<Integer>>(findDistsFromPaper());
		List<Integer> preyIndexList = new ArrayList<Integer>();
		
		for(int dists = 0; dists < preyList.size(); dists++) {
			
			int nearest = Simulation.xField;
			int nearIndex = Simulation.xField;
			
			for(int dist = 0; dist < preyList.get(dists).size(); dist++) {
				
				if(preyList.get(dists).get(dist) < nearest) {
					nearest = preyList.get(dists).get(dist);
					nearIndex = dist;
				}
				
			}
			
			if(nearest != Simulation.xField && nearIndex != Simulation.xField) {
				preyIndexList.add(nearIndex);
			}
			
		}
		
		return(preyIndexList);
		
	}
	
	private List<List<Integer>> findDistsFromPaper() {
		
		List<List<Integer>> fromPreyDists = new ArrayList<List<Integer>>();
		
		for(int xPred = 0; xPred < (locations.size()); xPred++) {
			
			List<List<Integer>> check2 = new ArrayList<List<Integer>>();
			
			for(int yPred = 0; yPred < (locations.get(xPred).size()); yPred++) {
				
				List<Integer> check1 = new ArrayList<>();
				int coordPred = locations.get(xPred).get(yPred);
				
				for(int xPrey = 0; xPrey < (Paper.locations.size()); xPrey++) {
					
					int coordPrey = Paper.locations.get(xPrey).get(yPred);
					int diff = Math.abs(coordPrey - coordPred);
					check1.add(diff);
					
				}
				
				check2.add(check1);
				
			}
			
			// Separated into x and y lists
			List<Integer> xSep = new ArrayList<>(check2.get(0));
			List<Integer> ySep = new ArrayList<>(check2.get(1));
			
			List<Integer> distances = new ArrayList<>();
			
			for(int diff = 0; diff < xSep.size(); diff++) {
				
				int dist = xSep.get(diff) + ySep.get(diff);
				distances.add(dist);
				
			}
			fromPreyDists.add(distances);
		}
		return(fromPreyDists);
	}
	
	
}

