import core.GameManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import ui.FxStages;
import ui.ScreenFlows;

public class startGameController {

	@FXML
	private void startGame(ActionEvent event) {
		GameManager.startGame();
		GameManager.setActivePlayer(0);
		ScreenFlows.show(FxStages.stage(event), 1);
	}
}
