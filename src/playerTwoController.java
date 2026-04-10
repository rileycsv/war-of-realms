import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import ui.FxStages;
import ui.MenuSelections;
import ui.ScreenFlows;

public class playerTwoController {

	private void chooseEmpire(String empire, ActionEvent event) {
		MenuSelections.setPlayerTwoEmpire(empire);
		ScreenFlows.show(FxStages.stage(event), 4);
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
