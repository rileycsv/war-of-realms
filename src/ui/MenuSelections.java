package ui;

import environment.Board;
import environment.Boards;

/**
 * Holds choices made in FXML menus until core types expose setters or the game
 * reads them here.
 */
public final class MenuSelections {

	private static String playerOneEmpire;
	private static String playerTwoEmpire;
	private static int battlefieldDifficulty = 1; // 0 = easy, 1 = medium, 2 = hard

	private MenuSelections() {
	}

	public static String getPlayerOneEmpire() {
		return playerOneEmpire;
	}

	public static void setPlayerOneEmpire(String empire) {
		playerOneEmpire = empire;
	}

	public static String getPlayerTwoEmpire() {
		return playerTwoEmpire;
	}

	public static void setPlayerTwoEmpire(String empire) {
		playerTwoEmpire = empire;
	}

	public static int getBattlefieldDifficulty() {
		return battlefieldDifficulty;
	}

	public static void setBattlefieldDifficulty(int difficulty) {
		if (difficulty >= 0 && difficulty <= 2) {
			battlefieldDifficulty = difficulty;
			Boards.setActiveBoard(difficulty);
		}
	}
}
