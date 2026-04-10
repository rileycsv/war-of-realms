package ui;

/**
 * Holds choices made in FXML menus until core types expose setters or the game reads them here.
 */
public final class MenuSelections {

	private static String playerOneEmpire;
	private static String playerTwoEmpire;
	private static String battlefieldDifficulty = "medium";

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

	public static String getBattlefieldDifficulty() {
		return battlefieldDifficulty;
	}

	public static void setBattlefieldDifficulty(String difficulty) {
		if (difficulty != null) {
			battlefieldDifficulty = difficulty;
		}
	}
}
