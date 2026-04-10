package ui;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.Stage;

public final class FxStages {

	private FxStages() {
	}

	public static Stage stage(ActionEvent event) {
		return (Stage) ((Node) event.getSource()).getScene().getWindow();
	}
}
