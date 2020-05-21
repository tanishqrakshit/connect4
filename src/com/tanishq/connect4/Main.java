package com.internshala.connect4;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Main extends Application {

	private Controller controller;

	@Override
	public void start(Stage primaryStage) throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("game.fxml"));
		GridPane rootGridPane = loader.load();

		controller = loader.getController();
		controller.createPlayground();

		MenuBar menuBar = createMenu();
		menuBar.prefWidthProperty().bind(primaryStage.widthProperty());

		Pane menuPane = (Pane) rootGridPane.getChildren().get(0);
		menuPane.getChildren().add(menuBar);

		Scene scene = new Scene(rootGridPane);
		primaryStage.setTitle("Connect Four");
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.show();
	}


	public MenuBar createMenu() {

		//File Menu ....

		javafx.scene.control.Menu fileMenu = new javafx.scene.control.Menu("File");

		javafx.scene.control.MenuItem newGame = new javafx.scene.control.MenuItem("New Game");
		newGame.setOnAction(event -> controller.resetGame());

		javafx.scene.control.MenuItem resetGame = new javafx.scene.control.MenuItem("Reset Game");
		resetGame.setOnAction(event -> controller.resetGame());

		SeparatorMenuItem separatorMenuItem = new SeparatorMenuItem();
		javafx.scene.control.MenuItem exitGame = new MenuItem("Exit Game");
		exitGame.setOnAction(event -> exitGame());

		fileMenu.getItems().addAll(newGame, resetGame, exitGame);

		//Help Menu ....
		javafx.scene.control.Menu helpMenu = new javafx.scene.control.Menu("Help");

		javafx.scene.control.MenuItem aboutGame = new javafx.scene.control.MenuItem("How To Play?");
		aboutGame.setOnAction(event -> aboutConnect4());

		SeparatorMenuItem separatorMenu = new SeparatorMenuItem();
		javafx.scene.control.MenuItem aboutMe = new javafx.scene.control.MenuItem("About Me");
		aboutMe.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				aboutMe();
			}
		});

		helpMenu.getItems().addAll(aboutGame, aboutMe);

		javafx.scene.control.MenuBar menuBar = new javafx.scene.control.MenuBar();
		menuBar.getMenus().addAll(fileMenu, helpMenu);

		return menuBar;
	}

	private void aboutMe() {

		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle("About The Developer");
		alert.setHeaderText("Tanishq Rakshit");
		alert.setContentText("I am currently pursuing Engineering and I love programming. I have made this game through JavaFX. Let's Play !");
		alert.show();
	}

	private void aboutConnect4() {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle("About Connect 4");
		alert.setHeaderText("How To Play");
		alert.setContentText("Connect Four is a two-player connection game in which the players first choose a color and then take turns dropping colored discs from the top into a seven-column, six-row vertically suspended grid. The pieces fall straight down, occupying the next available space within the column. The objective of the game is to be the first to form a horizontal, vertical, or diagonal line of four of one's own discs. Connect Four is a solved game. The first player can always win by playing the right moves.");
		alert.show();
	}

	private void exitGame() {
		Platform.exit();
		System.exit(0);

	}

	private void resetGame() {

	}


	public static void main(String[] args) {
		launch(args);
	}
}
