package main;

import utils.Debug;
import environment.Board;
import javafx.application.Application;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class Main extends Application {
	
	private Stage primaryStage;
	private Board board = new Board();
	
	@Override
	public void start(Stage stage) {
		// Enable debug
		Debug.enable();
		Debug.println(2, "Debug enabled");

		primaryStage = stage;

		primaryStage.setTitle("War of Realms");
		primaryStage.show();
		
		setScreen(0);
		
		primaryStage.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
			// If debug is enabled and the Escape key is pressed, close the program
			if (event.getCode() == KeyCode.ESCAPE && Debug.isEnabled()) {
				Debug.println(2, "Escape pressed, exiting");
				primaryStage.close();
			}
		});

	}
	
	/**
     * Swaps out the current view scene in the main window.
     * @param id The ID of the screen to display (0 = Active Game Board).
     */
	public void setScreen(int id) {
		Debug.log("'setScreen': setting screen " + id);
		switch (id) {
			case 0:
				primaryStage.setScene(board.getScene());
				break;
		}
		
	}

	public static void main(String[] args) { launch(args); }
}
