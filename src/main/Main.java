package main;

import utils.Debug;
import environment.Board;

import java.io.IOException;

import javafx.application.Application;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

public class Main extends Application {
	
	public static Stage primaryStage;
	private Board board = new Board();
	
	@Override
	public void start(Stage stage) {
		// Enable debug
		Debug.enable();
		Debug.setDebugPriority(2);
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
		String fileName = "";
		switch (id) {
			case 0:
			    fileName = "/scenes/startGame.fxml";  // ← add leading /
			    break;
			case 1:
			    fileName = "/scenes/playerOneEmpire.fxml";
			    break;
			case 2:
			    fileName = "/scenes/playerTwoEmpire.fxml";
			    break;
			case 3:
				// Set the scene to the game board
				primaryStage.setScene(board.getScene());
				// Exit the function early since no FXML file needs to be loaded
				return;
			case 4:
			    fileName = "/scenes/chooseBattlefield.fxml";
			    break;
		}
		
		// Load the FXML file from fileName and set it to the primary stage
		try {
		    var resource = getClass().getResource(fileName);
		    if (resource == null) {
		        Debug.error("FXML resource not found: " + fileName);
		        return;
		    }
		    primaryStage.setScene(new Scene(FXMLLoader.load(resource)));
		} catch (IOException e) {
		    Debug.error(e);
		}
	}

	public static void main(String[] args) { launch(args); }
}
