package com.internshala.connect4;

import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.util.Duration;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Controller implements Initializable {
	private static final int Columns=7;
	private static final int Rows=6;
	private static final int Circle_Diameter=80;
	private static final String discColor1="24303E";
	private static final String discColor2="4CAA88";

	private static String Player_One="Player One";
	private static String Player_Two="Player Two";
	private boolean isPlayerOneTurn=true;

	private Disc[][] insertedDiscsArray= new Disc[Rows][Columns];

	private boolean isAllowedToInsert=true;

	@FXML
	public GridPane rootGridPane;
	@FXML
	public Pane insertedDiscPane;
	@FXML
	public Label playerNameLabel;
	@FXML
	public TextField playerOneTextField, playerTwoTextField;
	@FXML
	public Button setNamesButton;

	public void createPlayground(){

		setNamesButton.setOnAction(event -> {
			String input1=playerOneTextField.getText();
			String input2=playerTwoTextField.getText();

			Player_One=input1;
			Player_Two=input2;
		});
		Shape rectangleWithHoles=createGameStructuralGrid();
		rootGridPane.add(rectangleWithHoles,0,1);

		List<Rectangle> rectangleList=createClickableColumns();

		for(Rectangle rectangle: rectangleList) {
			rootGridPane.add(rectangle, 0, 1);
		}
	}

	private Shape createGameStructuralGrid(){
		Shape rectangleWithHoles= new Rectangle((Columns+1) * Circle_Diameter, (Rows+1) * Circle_Diameter);

		for(int row=0; row<Rows; row++) {

			for(int col=0; col<Columns; col++){

				Circle circle = new Circle();
				circle.setRadius(Circle_Diameter / 2);
				circle.setCenterX(Circle_Diameter / 2);
				circle.setCenterY(Circle_Diameter / 2);
				circle.setSmooth(true);

				circle.setTranslateX(col * (Circle_Diameter + 5) + Circle_Diameter / 4);
				circle.setTranslateY(row * (Circle_Diameter + 5) + Circle_Diameter / 4);

				rectangleWithHoles = Shape.subtract(rectangleWithHoles, circle);
			}
		}

		rectangleWithHoles.setFill(Color.WHITE);

		return rectangleWithHoles;
	}

	private List<Rectangle> createClickableColumns(){

		List<Rectangle> rectangleList=new ArrayList();
		for(int col=0; col<Columns; col++) {
			Rectangle rectangle = new Rectangle(Circle_Diameter, (Rows + 1) * Circle_Diameter);
			rectangle.setFill(Color.TRANSPARENT);

			rectangle.setTranslateX(col * (Circle_Diameter + 5) + Circle_Diameter / 4);
			rectangle.setOnMouseEntered(event -> rectangle.setFill(Color.valueOf("#EEEEEE26")));
			rectangle.setOnMouseExited(event -> rectangle.setFill(Color.TRANSPARENT));

			final int column=col;
			rectangle.setOnMouseClicked(event -> {

				if(isAllowedToInsert){

					isAllowedToInsert=false;
					insertDisc(new Disc(isPlayerOneTurn), column);
				}

			});
			rectangleList.add(rectangle);
		}
		return rectangleList;
	}

	private void insertDisc(Disc disc,int column){
		int rows=Rows-1;
		while(rows>=0){
			if (getDiscIfPresent(rows,column)== null)
				break;

			rows--;
		}
		if(rows<0){
			return;
		}


     insertedDiscsArray[rows][column]=disc;
     insertedDiscPane.getChildren().add(disc);

     disc.setTranslateX(column * (Circle_Diameter + 5) + Circle_Diameter / 4);

     int current_row=rows;
     TranslateTransition translateTransition=new TranslateTransition(Duration.seconds(0.5), disc);
     translateTransition.setToY(rows * (Circle_Diameter + 5) + Circle_Diameter / 4);

     translateTransition.setOnFinished(event -> {

     	isAllowedToInsert=true;
     	if (gameEnded(current_row, column)){
			gameOver();
			return;
        }
     	isPlayerOneTurn=!isPlayerOneTurn;
     	playerNameLabel.setText(isPlayerOneTurn ? Player_One : Player_Two);
     });
     translateTransition.play();
	}

	private void gameOver() {
      String winner= isPlayerOneTurn ? Player_One : Player_Two;
		System.out.println("Winner is : " + winner);

		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle("Connect 4");
		alert.setHeaderText("The Winner is : " + winner);
		alert.setContentText("Do you want to play again ?");

		ButtonType yesBtn = new ButtonType("Yes");
		ButtonType noBtn = new ButtonType("No, Exit");
		alert.getButtonTypes().setAll(yesBtn , noBtn);


		Platform.runLater(()-> {
			Optional<ButtonType> btnClicked= alert.showAndWait();
			if (btnClicked.isPresent() && btnClicked.get()== yesBtn){
				resetGame();
			} else{
				Platform.exit();
				System.exit(0);
			}
		});

	}

	public void resetGame() {

		insertedDiscPane.getChildren().clear();
		for(int row=0; row<insertedDiscsArray.length; row++){

			for (int col=0; col<insertedDiscsArray[row].length; col++){
				insertedDiscsArray[row][col]=null;
			}
		}

		isPlayerOneTurn=true;
		playerNameLabel.setText(Player_One);

		createPlayground();
	}

	private boolean gameEnded(int rows, int columns){
		List<Point2D> verticalPoints = IntStream.rangeClosed(rows-3 , rows + 3).
											mapToObj(r -> new Point2D(r , columns)).
												collect(Collectors.toList());

		List<Point2D> horizontalPoints = IntStream.rangeClosed(columns-3 , columns + 3).
				mapToObj(col -> new Point2D(rows , col)).
				collect(Collectors.toList());

		Point2D startPoint1= new Point2D(rows-3, columns+3);

		List<Point2D> diagonal1Points = IntStream.rangeClosed(0,6).
										mapToObj(i -> startPoint1.add(i,-i)).
											collect(Collectors.toList());

		Point2D startPoint2= new Point2D(rows-3, columns-3);

		List<Point2D> diagonal2Points = IntStream.rangeClosed(0,6).
				mapToObj(i -> startPoint2.add(i,i)).
				collect(Collectors.toList());

		boolean isEnded= checkCombinations(verticalPoints) || checkCombinations(horizontalPoints)
				|| checkCombinations(diagonal1Points) || checkCombinations(diagonal2Points);


	return isEnded;
	}

	private boolean checkCombinations(List<Point2D> points) {
		int chain=0;
		for (Point2D point: points) {
			int rowIndexForArray=(int) point.getX();
			int colIndexForArray= (int) point.getY();

			Disc disc= getDiscIfPresent(rowIndexForArray,colIndexForArray);
			if(disc!=null && disc.isPlayerOneMove==isPlayerOneTurn){
				chain++;
				if(chain==4) {
					return true;
				}
			} else {
				chain=0;
			}
			
		}

		return false;
	}

	private Disc getDiscIfPresent(int row, int column){

		if(row>=Rows || row<0 || column>=Columns || column<0){
			return null;
		}
		return insertedDiscsArray[row][column];
	}

	private static class Disc extends Circle{
		private final boolean isPlayerOneMove;
		public Disc (boolean isPlayerOneMove){
			this.isPlayerOneMove=isPlayerOneMove;
			setRadius(Circle_Diameter / 2);
			setCenterX(Circle_Diameter / 2);
			setCenterY(Circle_Diameter / 2);
			setFill(isPlayerOneMove ? Color.valueOf(discColor1) : Color.valueOf(discColor2));
		}

	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

	}
}
