import core.GameManager;
import core.Player;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import ui.MenuSelections;
import ui.ScreenFlows;
import utils.Debug;

public class playerTwoController {

	private void chooseEmpire(String empire, ActionEvent event) {
		Debug.log(2, "Player 2 chose " + empire);
		MenuSelections.setPlayerTwoEmpire(empire);
		GameManager.players[1] = new Player(1, empire);
		ScreenFlows.show(4);
	}

	@FXML
	private void p2Hussites(ActionEvent event) {
		chooseEmpire("Hussites", event);
	}

	@FXML
	private void p2Portuguese(ActionEvent event) {
		chooseEmpire("Portuguese", event);
	}

	@FXML
	private void p2Mongolians(ActionEvent event) {
		chooseEmpire("Mongolians", event);
	}

	@FXML
	private void p2Aztec(ActionEvent event) {
		chooseEmpire("Aztec", event);
	}

	@FXML
	private void p2English(ActionEvent event) {
		chooseEmpire("English", event);
	}
}
