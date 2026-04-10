import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import ui.FxStages;
import ui.MenuSelections;
import ui.ScreenFlows;

public class battlefieldController {

	private void chooseDifficulty(String difficulty, ActionEvent event) {
		MenuSelections.setBattlefieldDifficulty(difficulty);
		ScreenFlows.show(FxStages.stage(event), 3);
	}

	@FXML
	private void chooseEasy(ActionEvent event) {
		chooseDifficulty("easy", event);
	}

	@FXML
	private void chooseMedium(ActionEvent event) {
		chooseDifficulty("medium", event);
	}

	@FXML
	private void chooseHard(ActionEvent event) {
		chooseDifficulty("hard", event);
	}
}
