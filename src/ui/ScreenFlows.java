package ui;

import environment.Board;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import utils.Debug;

/**
 * Same screen IDs as {@code main.Main#setScreen(int)}, for use from FXML controllers without
 * changing {@code Main}. Uses one board instance for transitions that end on screen 3 from this
 * path (see class comment on {@link #boardForMenuPath}).
 */
public final class ScreenFlows {

	/**
	 * Board shown when moving to screen 3 via menu handlers. {@code Main} still owns its own
	 * {@code Board} when {@code setScreen(3)} is used there; unify later if both must match.
	 */
	private static final class BoardForMenuPath {
		static final Board BOARD = new Board();

		static Scene scene() {
			return BOARD.getScene();
		}
	}

	private ScreenFlows() {
	}

	public static void show(Stage stage, int id) {
		Debug.log("'ScreenFlows.show': setting screen " + id);
		String fileName = "";
		switch (id) {
			case 0:
				fileName = "/scenes/startGame.fxml";
				break;
			case 1:
				fileName = "/scenes/playerOneEmpire.fxml";
				break;
			case 2:
				fileName = "/scenes/playerTwoEmpire.fxml";
				break;
			case 3:
				stage.setScene(BoardForMenuPath.scene());
				return;
			case 4:
				fileName = "/scenes/chooseBattlefield.fxml";
				break;
			default:
				Debug.error("Unknown screen id: " + id);
				return;
		}

		try {
			var resource = ScreenFlows.class.getResource(fileName);
			if (resource == null) {
				Debug.error("FXML resource not found: " + fileName);
				return;
			}
			stage.setScene(new Scene(FXMLLoader.load(resource)));
		} catch (IOException e) {
			Debug.error(e);
		}
	}
}
