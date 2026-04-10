import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import ui.FxStages;
import ui.MenuSelections;
import ui.ScreenFlows;

public class playerOneController {

	private void chooseEmpire(String empire, ActionEvent event) {
		MenuSelections.setPlayerOneEmpire(empire);
		ScreenFlows.show(FxStages.stage(event), 2);
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
