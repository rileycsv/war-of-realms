import core.GameManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import ui.ScreenFlows;

public class startGameController {

	@FXML
	private void startGame(ActionEvent event) {
		GameManager.startGame();
		ScreenFlows.show(1);
	}
}
