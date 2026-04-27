import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import ui.MenuSelections;
import ui.ScreenFlows;

public class battlefieldController {

	private void chooseDifficulty(int difficulty, ActionEvent event) {
		MenuSelections.setBattlefieldDifficulty(difficulty);
		ScreenFlows.show(3);
	}

	@FXML
	private void chooseEasy(ActionEvent event) {
		chooseDifficulty(0, event);
	}

	@FXML
	private void chooseMedium(ActionEvent event) {
		chooseDifficulty(1, event);
	}

	@FXML
	private void chooseHard(ActionEvent event) {
		chooseDifficulty(2, event);
	}
}
