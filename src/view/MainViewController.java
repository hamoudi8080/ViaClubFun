package view;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import model.*;

import javafx.fxml.FXML;
import javafx.scene.layout.Region;
import parser.ParserException;
import parser.XmlJsonParser;

/**
 * A user interface that allows displaying and modifying information about players and matches
 *
 * @author JavaGods
 * @version 1.0
 */
public class MainViewController
{
  private Region root;
  private ViaClubModelManager modelManager;
  private ViewHandler viewHandler;

  @FXML private Button playerSearchButton;
  @FXML private Button matchSearchButton;
  @FXML private Button addPlayerButton;
  @FXML private Button addMatchButton;
  @FXML private Button removePlayerButton;
  @FXML private Button removeMatchButton;
  @FXML private Button editPlayerButton;
  @FXML private Button editMatchButton;
  @FXML private Button playerAvailability;
  @FXML private Button exportPlayerListButton;
  @FXML private Button exportMatchButton;

  @FXML private MenuItem exitMenuItem;
  @FXML private MenuItem aboutMenuItem;
  @FXML private MenuItem helpMenuItem;

  @FXML private Tab playerListTab;
  @FXML private Tab matchListTab;

  @FXML private ListView<Player> allPlayersList;
  @FXML private ListView<Match> allMatchesList;

  @FXML private TextField searchPlayersField;
  @FXML private TextField searchMatchesField;

  @FXML private ComboBox<String> playerSearchComboBox;
  @FXML private ComboBox<String> availableComboBox;
  @FXML private ComboBox<String> matchSearchComboBox;
  @FXML private ComboBox<String> matchDateComboBox;

  private XmlJsonParser parser;

  /**
   * Initializes the necessary data in the main view
   *
   * @param viewHandler  links the views
   * @param modelManager single access point for the functionality
   * @param root         the main layout of the view
   */
  public void init(ViewHandler viewHandler, ViaClubModelManager modelManager,
      Region root)
  {
    this.modelManager = modelManager;
    this.root = root;
    this.viewHandler = viewHandler;

    parser = new XmlJsonParser();

    allPlayersList.getSelectionModel().selectedItemProperty()
        .addListener((new MyListListener()));
    allMatchesList.getSelectionModel().selectedItemProperty()
        .addListener(new MyListListener2());
    modelManager.updateSuspensions();

    reset();
  }

  /**
   * Resets the page by updating the displayed data
   */
  public void reset()
  {
    disableMatchButtons();
    disableButtons();
    setAvailableComboBox();
    setMatchDateComboBox();
    setPlayerSearchComboBox();
    setMatchSearchComboBox();
    modelManager.updateBenchedInARow();
    updatePlayerList();
    searchMatchList();
  }

  /**
   * Gets the root of the view
   *
   * @return the root of the view
   */
  public Region getRoot()
  {
    return root;
  }

  /**
   * Main method for handling events in the GUI
   *
   * @param e the event that is targeted
   */
  public void handleActions(ActionEvent e)
  {
    if (e.getSource() == addPlayerButton)
    {
      viewHandler.openView("AddPlayerView");
    }

    else if (e.getSource() == editPlayerButton)
    {
      viewHandler.openView("AddPlayerView");
      viewHandler.getAddPlayerViewController()
          .setFields(allPlayersList.getSelectionModel().getSelectedItem());
    }

    else if (e.getSource() == removePlayerButton)
    {
      Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
          "Are you sure you want to permanently delete this player?",
          ButtonType.YES, ButtonType.NO);
      alert.setTitle("Exit");
      alert.setHeaderText(null);

      alert.showAndWait();

      if (alert.getResult() == ButtonType.YES)
      {
        PlayerList temp = new PlayerList();
        for (int i = 0; i < modelManager.getAllPlayers().size(); i++)
        {
          temp.add(modelManager.getAllPlayers().get(i));
        }
        temp.remove(allPlayersList.getSelectionModel().getSelectedItem());
        modelManager.savePlayers(temp);

        updatePlayerList();
        disableButtons();
      }
    }

    else if (e.getSource() == playerAvailability)
    {
      viewHandler.openView("UnavailabilityView");
      viewHandler.getUnavailabilityViewController()
          .setFields(allPlayersList.getSelectionModel().getSelectedItem());
    }

    else if (e.getSource() == exportPlayerListButton)
    {
      try
      {
        parser.toXml(modelManager.getAllPlayers(),
            "ViaClubFun\\src\\playerList.xml");
      }
      catch (ParserException parserException)
      {
        parserException.printStackTrace();
      }
    }
    else if (e.getSource() == playerSearchComboBox)
    {
      if (playerSearchComboBox.getSelectionModel().getSelectedItem().equals(""))
      {
        searchPlayersField.clear();
        updatePlayerList();
      }
    }

    else if (e.getSource() == playerSearchButton)
    {
      searchPlayersList();
    }
    else if (e.getSource() == availableComboBox)
    {
      updatePlayerList();
    }
    else if (e.getSource() == matchDateComboBox)
    {
      if (matchDateComboBox.getSelectionModel().getSelectedItem() != null)
      {
        searchMatchList();
      }
    }
    else if (e.getSource() == matchSearchButton)
    {
      searchMatchList();
    }

    else if (e.getSource() == matchSearchComboBox)
    {
      if (matchSearchComboBox.getSelectionModel().getSelectedItem() != null)
      {
        searchMatchList();
      }
    }

    else if (e.getSource() == addMatchButton)
    {
      viewHandler.openView("AddMatchView");
    }

    else if (e.getSource() == editMatchButton)
    {
      viewHandler.openView("AddMatchView");
      viewHandler.getAddMatchViewController()
          .setFields(allMatchesList.getSelectionModel().getSelectedItem());
      disableMatchButtons();
    }

    else if (e.getSource() == removeMatchButton)
    {
      Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
          "Are you sure you want to permanently delete this match?",
          ButtonType.YES, ButtonType.NO);
      alert.setTitle("Remove");
      alert.setHeaderText(null);

      alert.showAndWait();

      if (alert.getResult() == ButtonType.YES)
      {
        MatchList temp = modelManager.getAllMatches();
        temp.remove(allMatchesList.getSelectionModel().getSelectedItem());

        modelManager.saveMatches(temp);

        searchMatchList();
        disableMatchButtons();
      }
    }

    else if (e.getSource() == exportMatchButton)
    {
      try
      {
        parser.toXml(allMatchesList.getSelectionModel().getSelectedItem(),
            "ViaClubFun\\src\\match " + allMatchesList.getSelectionModel()
                .getSelectedItem().getDate().getDay() + "-" + allMatchesList
                .getSelectionModel().getSelectedItem().getDate().getMonth()
                + "-" + allMatchesList.getSelectionModel().getSelectedItem()
                .getDate().getYear() + ".xml");
      }
      catch (ParserException parserException)
      {
        parserException.printStackTrace();
      }
    }

    else if (e.getSource() == exitMenuItem)
    {
      Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
          "Do you really want to exit the program?", ButtonType.YES,
          ButtonType.NO);
      alert.setTitle("Exit");
      alert.setHeaderText(null);

      alert.showAndWait();

      if (alert.getResult() == ButtonType.YES)
      {
        System.exit(0);
      }
    }

    else if (e.getSource() == aboutMenuItem)
    {
      Alert alert = new Alert(Alert.AlertType.INFORMATION,
          "Here you can manipulate your players and matches.", ButtonType.OK);
      alert.setTitle("About");
      alert.setHeaderText(null);
      alert.showAndWait();
    }

    else if (e.getSource() == helpMenuItem)
    {
      Alert alert = new Alert(Alert.AlertType.INFORMATION,
          "For client support, please refer to JavaGods.", ButtonType.OK);
      alert.setTitle("Help");
      alert.setHeaderText(null);
      alert.showAndWait();
    }
  }

  /**
   * Disables the edit, remove and unavailability player buttons
   */
  private void disableButtons()
  {
    editPlayerButton.setDisable(true);
    removePlayerButton.setDisable(true);
    playerAvailability.setDisable(true);
  }

  /**
   * Disables the edit and remove match buttons
   */
  private void disableMatchButtons()
  {
    editMatchButton.setDisable(true);
    removeMatchButton.setDisable(true);
    exportMatchButton.setDisable(true);
  }

  /**
   * Sets the items in the player combo box
   */
  private void setAvailableComboBox()
  {
    availableComboBox.getItems().clear();
    availableComboBox.getItems().add("All Players");
    availableComboBox.getItems().add("Available Players");
    availableComboBox.getItems().add("Unavailable players");
    availableComboBox.getSelectionModel().select(0);
  }

  /**
   * Sets the items in the match combo box
   */
  private void setMatchDateComboBox()
  {
    matchDateComboBox.getItems().clear();
    matchDateComboBox.getItems().add("All");
    matchDateComboBox.getItems().add("Today");
    matchDateComboBox.getItems().add("Past");
    matchDateComboBox.getItems().add("Future");
    matchDateComboBox.getSelectionModel().select(0);
  }

  /**
   * Sets the items in the second player combo box
   */
  private void setPlayerSearchComboBox()
  {
    playerSearchComboBox.getItems().clear();
    playerSearchComboBox.getItems().add("Name");
    playerSearchComboBox.getItems().add("Number");
    playerSearchComboBox.getItems().add("Position");
    playerSearchComboBox.getItems().add("");
  }

  /**
   * Sets the items in the second match combo box
   */
  private void setMatchSearchComboBox()
  {
    matchSearchComboBox.getItems().clear();
    matchSearchComboBox.getItems().add("League");
    matchSearchComboBox.getItems().add("Cup");
    matchSearchComboBox.getItems().add("Friendly");
    matchSearchComboBox.getItems().add("Show All");
    matchSearchComboBox.getSelectionModel().select(3);
  }

  /**
   * Searches for players based on the user input
   */
  private void searchPlayersList()
  {
    PlayerList tempList;

    if (availableComboBox.getSelectionModel().getSelectedIndex() == 0)
    {
      tempList = modelManager.getAllPlayers();
    }
    else if (availableComboBox.getSelectionModel().getSelectedIndex() == 1)
    {
      tempList = modelManager.getPlayersAvailable();
    }
    else
    {
      tempList = modelManager.getPlayersUnavailable();
    }

    if (searchPlayersField.getText().equals(""))
    {
      updatePlayerList();
    }

    if (playerSearchComboBox.getSelectionModel().getSelectedItem()
        .equals("Name"))
    {
      PlayerList tempListOutput = modelManager
          .getPlayersByName(searchPlayersField.getText(), tempList);
      allPlayersList.getItems().clear();
      for (int i = 0; i < tempListOutput.size(); i++)
      {
        allPlayersList.getItems().add(tempListOutput.get(i));
      }
    }

    else if (playerSearchComboBox.getSelectionModel().getSelectedItem()
        .equals("Number") && !searchPlayersField.getText().equals(""))
    {
      PlayerList tempListOutput = modelManager
          .getPlayersByNumber(Integer.parseInt(searchPlayersField.getText()),
              tempList);
      allPlayersList.getItems().clear();
      for (int i = 0; i < tempListOutput.size(); i++)
      {
        allPlayersList.getItems().add(tempListOutput.get(i));
      }
    }
    else if (playerSearchComboBox.getSelectionModel().getSelectedItem()
        .equals("Position"))
    {
      PlayerList tempListOutput = modelManager
          .getPlayersByPositions(searchPlayersField.getText(), tempList);
      allPlayersList.getItems().clear();
      for (int i = 0; i < tempListOutput.size(); i++)
      {
        allPlayersList.getItems().add(tempListOutput.get(i));
      }
    }
  }

  /**
   * Searches for matches based on use input
   */
  private void searchMatchList()
  {
    MatchList tempList = new MatchList();
    MatchList tempListOutput;

    switch (matchDateComboBox.getSelectionModel().getSelectedItem())
    {
      case "All":
        tempList = modelManager.getAllMatches();
        break;
      case "Today":
        tempList = modelManager.getAllMatchesToday();
        break;
      case "Past":
        tempList = modelManager.getAllMatchesPast();
        break;
      case "Future":
        tempList = modelManager.getAllFutureMatches();
        break;
    }

    switch (matchSearchComboBox.getSelectionModel().getSelectedItem())
    {
      case "League":
        tempListOutput = modelManager.getTypeMatches("League", tempList);
        allMatchesList.getItems().clear();
        for (int i = 0; i < tempListOutput.size(); i++)
        {
          allMatchesList.getItems().add(tempListOutput.get(i));
        }
        break;
      case "Cup":
        tempListOutput = modelManager.getTypeMatches("Cup", tempList);
        allMatchesList.getItems().clear();
        for (int i = 0; i < tempListOutput.size(); i++)
        {
          allMatchesList.getItems().add(tempListOutput.get(i));
        }
        break;
      case "Friendly":
        tempListOutput = modelManager.getTypeMatches("Friendly", tempList);
        allMatchesList.getItems().clear();
        for (int i = 0; i < tempListOutput.size(); i++)
        {
          allMatchesList.getItems().add(tempListOutput.get(i));
        }
        break;
      default:
        tempListOutput = tempList;
        allMatchesList.getItems().clear();
        for (int i = 0; i < tempListOutput.size(); i++)
        {
          allMatchesList.getItems().add(tempListOutput.get(i));
        }
    }
    if (!searchMatchesField.getText().equals(""))
    {
      tempListOutput = modelManager
          .getMatchesAgainst(searchMatchesField.getText(), tempListOutput);
      allMatchesList.getItems().clear();

      for (int i = 0; i < tempListOutput.size(); i++)
      {
        allMatchesList.getItems().add(tempListOutput.get(i));
      }
    }
  }

  /**
   * Updates the player list displayed based on the search
   */
  private void updatePlayerList()
  {
    if (searchPlayersField.getText().equals(""))
    {
      if (availableComboBox.getSelectionModel().getSelectedIndex() == 0)
      {
        allPlayersList.getItems().clear();
        PlayerList players = modelManager.getAllPlayers();
        for (int i = 0; i < players.size(); i++)
        {
          allPlayersList.getItems().add(players.get(i));
        }
      }

      else if (availableComboBox.getSelectionModel().getSelectedIndex() == 1)
      {
        allPlayersList.getItems().clear();
        PlayerList players = modelManager.getPlayersAvailable();
        for (int i = 0; i < players.size(); i++)
        {
          allPlayersList.getItems().add(players.get(i));
        }
      }

      else if (availableComboBox.getSelectionModel().getSelectedIndex() == 2)
      {
        allPlayersList.getItems().clear();
        PlayerList players = modelManager.getPlayersUnavailable();
        for (int i = 0; i < players.size(); i++)
        {
          allPlayersList.getItems().add(players.get(i));
        }
      }
    }
    else
    {
      searchPlayersList();
    }

  }

  /**
   * Updates the data displayed when changing tabs
   *
   * @param e the targeted event
   */
  public void tabChanged(Event e)
  {
    if (playerListTab.isSelected())
    {
      updatePlayerList();
      disableButtons();
    }

    else if (matchListTab.isSelected())
    {
      searchMatchList();
      disableMatchButtons();
    }
  }

  /**
   * Inner action listener class for enabling player buttons on player selection
   */
  private class MyListListener implements ChangeListener<Player>
  {
    public void changed(ObservableValue<? extends Player> player,
        Player oldPlayer, Player newPlayer)
    {
      editPlayerButton.setDisable(false);
      removePlayerButton.setDisable(false);
      playerAvailability.setDisable(false);
    }
  }

  /**
   * Inner action listener class for enabling match buttons on match selection
   */
  private class MyListListener2 implements ChangeListener<Match>
  {
    public void changed(ObservableValue<? extends Match> match, Match oldMatch,
        Match newMatch)
    {
      editMatchButton.setDisable(false);
      removeMatchButton.setDisable(false);
      exportMatchButton.setDisable(false);
    }
  }
}
