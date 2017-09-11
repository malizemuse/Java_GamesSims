package rockPaperScissorsBattle;

import java.util.Scanner;

/*
 * Essentially, this is a battlefield rendition of a rock, paper, scissors game
 */
public class Init {
	
	private static Scanner scan = new Scanner(System.in);
	
	public static void main(String[] args) throws InterruptedException {
		
		// Intro Menu
		// To be honest, all this extra description here is just for fun
		// The program is not so complicated that you need to read all this
		System.out.println("\n");
		System.out.println("	 ------------------------------------------------------\n"
						 + "	| The Battle of the Rocks, the Paper, and the Scissors |\n"
						 + "	 ------------------------------------------------------\n\n");
		
		System.out.print("		  (Y) or (N)\n"
					   + "		More information? ");
		
		String enter = scan.nextLine();
		if(enter.equals("Y")) {
			System.out.println("\n\n	 -------------\n"
							 + "	| Nation Info |\n"
							 + "	 ------------- \n\n"
			
							 + "	  Rocks use their brute force to smash the blades of the Scissors\n"
							 + "	  but are easily taken down by the stealth of the Paper.\n\n"
					
							 + "	  Paper opt for stealth, taking out Rocks by suffocation. However,\n"
							 + "	  these tactics do not work against the reflexes of the Scissors.\n\n"
							 
							 + "	  The Scissors slice down the fragile defenses of the Paper\n"
							 + "	  but are no match against the brute strength of the Rock.\n");
			
			enter = scan.nextLine(); // Lazy imitatation of a key press to continue
			
			// Explanation of Battle
			System.out.println("	 ------------------ \n"
							 + "	| Battle Mechanics |\n"
							 + "	 ------------------ \n\n"
					
							 + "   	  Every soldier will prioritize neutralizing an enemy they can apprehend over\n"
							 + "	  escaping encounters with enemies they cannot, within reasonable distance.\n\n"
							 
							 + "	  Because of this, although soldiers are initially spread throughout the\n"
							 + "	  field they will tend to group together throughout time to take down enemies.\n"
							 + "	  This is advantageous.\n\n"
							 
							 + "	  Until a certain point of time in the battle, each nation's soldiers will\n"
							 + "	  call for reinforcements if their numbers get too low. They cannot call\n"
							 + "	  too often, and each nation has a limit of reinforcement numbers.\n\n"
							 
							 + "	  Each nation's soldiers have their own endurance and recovery times.\n\n"
							 + "	    The Rocks have the least endurance but shortest recovery time.\n"
							 + "	    The Scissors have the most but also the longest recovery time.\n"
							 + "	    The Paper are in the middle of the spectrum.\n\n");
							 
							 
			
			enter = scan.nextLine(); // Lazy imitatation of a key press to continue				 
			
			System.out.println("	 ------------------- \n"
							 + "	| Victory Condition |\n"
							 + "	 ------------------- \n\n"
							 
							 + "	  When there are no more troops of one nation, the nation that solely those\n"
							 + "	  troops were able to apprehend is victorious.\n\n\n");
			System.out.println("	Let's start the simulation.");
			
			enter = scan.nextLine(); // Lazy imitatation of a key press to continue
		}
		
		System.out.println("\n");
		System.out.println("	 ------------------ \n"
						 + "	| Simulation Start |\n"
						 + "	 ------------------ \n\n");
		
		// Simulation Start
		Simulation sim = new Simulation();
		sim.initGame();
		
		// Final Words
		enter = scan.nextLine();
		System.out.println("		(...Perhaps, it was not in their best interest to kill the last\n"
						 + "		soldier who could (singlehandedly) defeat the enemy army...)\n\n");
			
	}
	
}
