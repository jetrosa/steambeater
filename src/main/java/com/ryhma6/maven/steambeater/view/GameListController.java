package com.ryhma6.maven.steambeater.view;

import java.net.URL;
import java.util.Comparator;
import java.util.ResourceBundle;
import com.ryhma6.maven.steambeater.MainApp;
import com.ryhma6.maven.steambeater.model.SteamAPICalls;
import com.ryhma6.maven.steambeater.model.steamAPI.GameData;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

public class GameListController implements Initializable {

	@FXML
	private ListView<GameData> gameList;

	@FXML
	Button hideStatsButton;

	@FXML
	AnchorPane statsWindow;
	
	@FXML
	Label statLabel;
	
	@FXML
	ComboBox sortingChoice;
	
	@FXML
	TextField searchField;
	
	private MainApp mainApp;
	private final Image IMAGE_TEST = new Image("test.png");

	private Image[] listOfImages = { IMAGE_TEST };
	//private ObservableList<String> games = FXCollections.observableArrayList("Sudoku","Pasianssi", "Minesweeper");
	private ObservableList<GameData> games = SteamAPICalls.getOwnedGames();
	private ObservableList<GameData> ignoredGames = FXCollections.observableArrayList();
	
	FilteredList<GameData> filteredData;
	
	public ObservableList<GameData> getGames() {
		return games;
	}

	public void setGames(ObservableList<GameData> games) {
		this.games = games;
	}

	public void loadGames() {	

		gameList.setCellFactory(param -> new ListCell<GameData>() {
			private Label gameName = new Label();
			private Label timePlayed = new Label();
			private HBox hbox = new HBox();
			private Button ignoreButton = new Button();
			private Button setAsBeaten = new Button();
			private ImageView imageView = new ImageView();

			@Override
			public void updateItem(GameData game, boolean empty) {
				super.updateItem(game, empty);
				if (empty) {
					setText(null);
					setGraphic(null);
				} else {
					ignoreButton.setText("Ignore this game");
					setAsBeaten.setText("Set game as beaten");
					timePlayed.setText("Time played: " + game.getPlaytime_forever() + " hours");
					gameName.setText(game.getName());
					hbox.setSpacing(50);
					hbox.setAlignment(Pos.CENTER_LEFT);
					try {
						imageView.setImage(new Image(game.getImg_logo_url(), true)); // true: load in background
					} catch (Exception e) {
						System.out.println("Loading game img failed (null or invalid url)");
						imageView.setImage(IMAGE_TEST);
					}
					hbox.getChildren().clear();
					hbox.getChildren().addAll(imageView, gameName, timePlayed, ignoreButton);
					setGraphic(hbox);
				}
//				EventHandler<MouseEvent> eventHandler = new EventHandler<MouseEvent>() {
//
//					@Override
//					public void handle(MouseEvent event) {
//						// TODO Auto-generated method stub
//						game.setIgnored(true);
//						if (game.isIgnored()) {
//							FilteredList<GameData> filteredData = new FilteredList<>(names, p -> true);
//							boolean filter = game.isIgnored();
//							if (filter == true) {
//								filteredData.setPredicate(s -> s.isIgnored());
//								System.out.println(game.isIgnored());
//							} else {
//
//							}
//							gameList.setItems(filteredData);
//							ignoredGames.add(game);
//						}
//					}
//
//				};
//				ignoreButton.addEventFilter(MouseEvent.MOUSE_CLICKED, eventHandler);
			}
		});
		filterByName();
	}

	private void hideStats() {
		statsWindow.setManaged(false);
		statsWindow.setVisible(false);
	}

	private void showStats() {
		gameList.maxWidth(250);
		statsWindow.setManaged(true);
		statsWindow.setVisible(true);
	}

	public void setStatsVisibility() {
		System.out.println("Button clicked!");
		boolean visible = statsWindow.isManaged();
		if (visible == true) {
			hideStats();
		} else {
			showStats();
		}
	}

	@FXML
	private void handleMouseClick(MouseEvent arg0) {
		 GameData game = gameList.getSelectionModel().getSelectedItem();
		 statLabel.setText(game.getName()); 
		 showStats();
	}


	/**
	 * Dropdown options to sort gamelist
	 */
	private void initListenerSortGameList(){
		sortingChoice.getSelectionModel().selectedItemProperty().addListener(obs->{
			System.out.println("sorting games");
			//sorting in alphabetical order
			if(sortingChoice.getSelectionModel().getSelectedIndex() == 0) {
				gameList.setItems(filteredData.sorted(Comparator.comparing(GameData::getName)));
			}else if(sortingChoice.getSelectionModel().getSelectedIndex() == 1){
				gameList.setItems(filteredData.sorted(Comparator.comparing(GameData::getPlaytime_forever).reversed()));
			}
		});
		
		sortingChoice.getSelectionModel().select(0);
	}
	

	/**
	 * Filtering gamelist with searchfield
	 */
	private void filterByName() {
		filteredData = new FilteredList<>(games, p -> true);
        
        searchField.textProperty().addListener(obs->{
            String filter = searchField.getText(); 
            if(filter == null || filter.length() == 0) {
                filteredData.setPredicate(s -> true);
            }
            else {
                filteredData.setPredicate(s -> s.getName().toLowerCase().contains(filter.toLowerCase()));
            }
        });
        
        //Wrap the FilteredList in a SortedList. 
        SortedList<GameData> sortedData = new SortedList<>(filteredData);
        
        //Add sorted (and filtered) data to the table.
        gameList.setItems(sortedData);
	}

	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		/*
		 * names = FXCollections.observableArrayList(); GameData g = new GameData();
		 * g.setName("testGame"); names.add(g);
		 */

		loadGames();
		hideStats();
		filterByName();
		
		initListenerSortGameList();
	}
}
