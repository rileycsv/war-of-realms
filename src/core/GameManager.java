package core;

/**
 * A global tracker for the current state of the game.
 * Holds references to the players and dictates whose turn it is.
 */
public class GameManager {
	public static Player[] players = { new Player("Dummy 1"), new Player("Dummy 2") }; 
	private static int activePlayer = -1;

	public static int getActivePlayer() {
	    return activePlayer;
	}
	
	public static void setActivePlayer(int id) {
	    // Now you can add safety checks!
	    if (id >= 0 && id < players.length) {
	        activePlayer = id;
	    }
	}
}
