import core.GameManager;
import core.Player;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import ui.MenuSelections;
import ui.ScreenFlows;
import utils.Debug;

public class playerOneController {

	private void chooseEmpire(String empire, ActionEvent event) {
		Debug.log(2, "Player 1 chose " + empire);
		MenuSelections.setPlayerOneEmpire(empire);
		GameManager.players[0] = new Player(0, empire);
		ScreenFlows.show(2);
	}

	@FXML
	private void p1Hussites(ActionEvent event) {
		chooseEmpire("Hussites", event);
	}

	@FXML
	private void p1Portuguese(ActionEvent event) {
		chooseEmpire("Portuguese", event);
	}

	@FXML
	private void p1Mongolians(ActionEvent event) {
		chooseEmpire("Mongolians", event);
	}

	@FXML
	private void p1Aztecs(ActionEvent event) {
		chooseEmpire("Aztec", event);
	}

	@FXML
	private void p1English(ActionEvent event) {
		chooseEmpire("English", event);
	}
}
