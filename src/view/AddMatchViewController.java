package view;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Region;
import model.*;

import java.time.LocalDate;

/**
 * A class for controlling the add match view
 * @author JavaGods
 * @version 1.0
 */

public class AddMatchViewController
{
  private Region root;
  private ViaClubModelManager modelManager;
  private ViewHandler viewHandler;

  @FXML private Button searchButton;
  @FXML private Button addButton;
  @FXML private Button removeButton;
  @FXML private Button saveButton;
  @FXML private Button cancelButton;

  @FXML private DatePicker datePicker;

  @FXML private ComboBox<Integer> startTimeHourBox;
  @FXML private ComboBox<Integer> endTimeHourBox;
  @FXML private ComboBox<Integer> startTimeMinuteBox;
  @FXML private ComboBox<Integer> endTimeMinuteBox;
  @FXML private ComboBox<String> searchBox;
  @FXML private ComboBox<String> matchTypeBox;

  @FXML private TextField opponentField;
  @FXML private TextField searchField;

  @FXML private Spinner<Integer> homeScoreSpinner;
  @FXML private Spinner<Integer> opponentScoreSpinner;

  @FXML private MenuItem exitMenuItem;
  @FXML private MenuItem aboutMenuItem;
  @FXML private MenuItem helpMenuItem;

  @FXML private ListView<Player> allPlayersList;
  @FXML private ListView<Player> lineUpAndBenchList;

  @FXML private ToggleGroup fieldAndBenchGroup;
  @FXML private RadioButton fieldRadio;
  @FXML private RadioButton benchRadio;

  @FXML private ToggleGroup gamePlaceGroup;
  @FXML private RadioButton homeRadio;
  @FXML private RadioButton awayRadio;

  private Match editMatch;
  private boolean gamePlaceBoolean;
  private boolean lineUpListBoolean;
  private PlayerList tempField;
  private PlayerList tempBench;

  /**
   * Initializes the data in the view
   * @param viewHandler links the views
   * @param modelManager single access point for the functionality
   * @param root returns the main layout of the view
   */
  public void init(ViewHandler viewHandler, ViaClubModelManager modelManager,
      Region root)
  {
    this.modelManager = modelManager;
    this.root = root;
    this.viewHandler = viewHandler;

    tempField = new PlayerList();
    tempBench = new PlayerList();

    allPlayersList.getSelectionModel().selectedItemProperty()
        .addListener((new MyListListener()));
    lineUpAndBenchList.getSelectionModel().selectedItemProperty()
        .addListener((new MyListListener2()));

    reset();
  }
  /**
   * Resets the page and updates the displayed data
   */
  public void reset()
  {
    editMatch=null;
    datePicker.setValue(LocalDate.now());
    opponentField.clear();
    tempBench.clear();
    tempField.clear();
    setSearchBox();
    setSpinners();
    setMatchTypeBox();
    updatePlayerList();
    updateTimeBoxes();
    setToggle();
    updateFieldList();
    lineUpListBoolean = true;
  }
  /**
   * Get the root of the view
   * @return the root of the view
   */
  public Region getRoot()
  {
    return root;
  }


  public void handleActions(ActionEvent e)
  {

    if (e.getSource() == searchButton)
    {
      if (!searchField.getText().equals(""))
      {
        searchPlayerList();
      }
    }

    if (e.getSource() == searchBox)
    {
      if (searchBox.getSelectionModel().getSelectedItem().equals(""))
      {
        searchField.clear();
        updatePlayerList();
      }
    }

    else if (e.getSource() == addButton)
    {
      if (lineUpListBoolean)
      {
        tempField.add(allPlayersList.getSelectionModel().getSelectedItem());
        updateFieldList();
      }
      else
      {
        tempBench.add(allPlayersList.getSelectionModel().getSelectedItem());
        updateBenchList();
      }
      updatePlayerList();
      disableButtons();
    }

    else if (e.getSource() == removeButton)
    {
      if (lineUpListBoolean)
      {
        tempField
            .remove(lineUpAndBenchList.getSelectionModel().getSelectedItem());
        updateFieldList();
      }
      else
      {
        tempBench
            .remove(lineUpAndBenchList.getSelectionModel().getSelectedItem());
        updateBenchList();
      }
      updatePlayerList();
      disableButtons();
    }

    else if (e.getSource() == saveButton)
    {
      if (opponentField.getText().equals(""))
      {
        opponentField.setText("Unknown");
      }

      Time stTemp = new Time(
          startTimeHourBox.getSelectionModel().getSelectedItem(),
          startTimeMinuteBox.getSelectionModel().getSelectedItem(), 0);

      Time etTemp = new Time(
          endTimeHourBox.getSelectionModel().getSelectedItem(),
          endTimeMinuteBox.getSelectionModel().getSelectedItem(), 0);

      Date dTemp = new Date(datePicker.getValue().getDayOfMonth(),
          datePicker.getValue().getMonthValue(),
          datePicker.getValue().getYear());

      Match temp = new Match(stTemp, etTemp, dTemp, opponentField.getText(),
          matchTypeBox.getSelectionModel().getSelectedItem(), gamePlaceBoolean);

      temp.addBench(tempBench);
      temp.addLineUp(tempField);

      temp.setScoreHomeTeam(homeScoreSpinner.getValue());
      temp.setScoreOpponent(opponentScoreSpinner.getValue());

      MatchList tempList = modelManager.getAllMatches();

      if (editMatch != null)
      {
        tempList.set(modelManager.getAllMatches()
            .getIndex(editMatch.getStartTime(), editMatch.getEndTime(),
                editMatch.getDate(), editMatch.getOpponent(),
                editMatch.getMatchType(), editMatch.getIsAwayGame()), temp);
      }
      else
      {
        tempList.add(temp);
      }
      modelManager.saveMatches(tempList);
      viewHandler.openView("MainView");
    }

    else if (e.getSource() == cancelButton)

    {
      viewHandler.openView("MainView");
    }

    else if (e.getSource() == awayRadio)

    {
      gamePlaceBoolean = true;
    }

    else if (e.getSource() == homeRadio)

    {
      gamePlaceBoolean = false;
    }

    else if (e.getSource() == fieldRadio)

    {
      lineUpListBoolean = true;
      allPlayersList.getSelectionModel().select(-1);
      updateFieldList();
      disableButtons();
    }

    else if (e.getSource() == benchRadio)

    {
      lineUpListBoolean = false;
      allPlayersList.getSelectionModel().select(-1);
      updateBenchList();
      disableButtons();
    }

    else if (e.getSource() == matchTypeBox)

    {
      if (tempField.size() > 0 || tempBench.size() > 0)
      {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
            "Changing the match type will remove your current Field line up and bench, do you wish to continue?",
            ButtonType.YES, ButtonType.NO);
        alert.setTitle("Confirmation");
        alert.setHeaderText(null);

        alert.showAndWait();

        if (alert.getResult() == ButtonType.YES)
        {
          tempBench.clear();
          tempField.clear();
          updatePlayerList();
          updateBenchList();
          updateFieldList();
        }
      }

      if (matchTypeBox.getSelectionModel().getSelectedItem()!= null)
      {
        updatePlayerList(); // ask allan!!
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
          "Here you can create or edit a match", ButtonType.OK);
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
   * Set the search combo box
   */

  private void setSearchBox()
  {
    searchBox.getItems().clear();
    searchBox.getItems().add("Name");
    searchBox.getItems().add("Number");
    searchBox.getItems().add("Position");
    searchBox.getItems().add("");
  }
  /**
   * Update the time combo box
   */
  private void updateTimeBoxes()
  {
    startTimeHourBox.getItems().clear();
    startTimeMinuteBox.getItems().clear();
    endTimeHourBox.getItems().clear();
    endTimeMinuteBox.getItems().clear();

    for (int i = 0; i < 24; i++)
    {
      startTimeHourBox.getItems().add(i);
      endTimeHourBox.getItems().add(i);
    }
    startTimeHourBox.getSelectionModel().select(12);
    startTimeHourBox.setValue(19);
    endTimeHourBox.setValue(21);

    for (int i = 0; i < 60; i++)
    {
      startTimeMinuteBox.getItems().add(i);
      endTimeMinuteBox.getItems().add(i);
    }
    startTimeMinuteBox.setValue(30);
    endTimeMinuteBox.setValue(30);
  }
  /**
   * Sets the toggle combo box
   */
  private void setToggle()
  {
    fieldRadio.setSelected(true);
    homeRadio.setSelected(true);
  }
  /**
   * Sets match type combo box
   */
  private void setMatchTypeBox()
  {
    matchTypeBox.getItems().clear();
    matchTypeBox.getItems().add("League");
    matchTypeBox.getItems().add("Cup");
    matchTypeBox.getItems().add("Friendly");
    matchTypeBox.getSelectionModel().select(0);
  }
  /**
   * update  the list of the players
   */
  private void updatePlayerList()
  {
    allPlayersList.getItems().clear();

    PlayerList allPlayers = modelManager.getAllPlayers();
    PlayerList available = new PlayerList();
    PlayerList usedPlayers = new PlayerList();

    if (matchTypeBox.getSelectionModel().getSelectedItem().equals("Cup")
        || matchTypeBox.getSelectionModel().getSelectedItem().equals("League"))
    {
      for (int i = 0; i < allPlayers.size(); i++)
      {
        if (!(allPlayers.get(i).isSuspended()) && !(allPlayers.get(i)
            .isInjured()))
        {
          available.add((allPlayers.get(i)));
        }
      }
    }

    else
    {
      for (int i = 0; i < allPlayers.size(); i++)
      {
        if (!(allPlayers.get(i).isInjured()))
        {
          available.add(allPlayers.get(i));
        }
      }
    }

    for (int i = 0; i < available.size(); i++)
    {
      allPlayersList.getItems().add(available.get(i));
    }

    for (int i = 0; i < tempField.size(); i++)
    {
      usedPlayers.add(tempField.get(i));
    }
    for (int i = 0; i < tempBench.size(); i++)
    {
      usedPlayers.add(tempBench.get(i));
    }

    for (int i = 0; i < usedPlayers.size(); i++)
    {
      allPlayersList.getItems().remove(usedPlayers.get(i));
    }

  }
  /**
   * Search the list of players
   */

  private void searchPlayerList()
  {
    PlayerList tempList;
    PlayerList allPlayers = modelManager.getAllPlayers();
    if (matchTypeBox.getSelectionModel().getSelectedIndex() == 2)
    {
      tempList = new PlayerList();
      for (int i = 0; i < allPlayers.size(); i++)
      {
        if (!allPlayers.get(i).isSuspended())
        {
          tempList.add(allPlayers.get(i));
        }
      }
    }
    else
    {
      tempList = modelManager.getPlayersAvailable();
    }

    if (searchBox.getSelectionModel().getSelectedItem().equals("Name"))
    {
      PlayerList tempListOutput = modelManager
          .getPlayersByName(searchField.getText(), tempList);
      allPlayersList.getItems().clear();
      for (int i = 0; i < tempListOutput.size(); i++)
      {
        allPlayersList.getItems().add(tempListOutput.get(i));
      }
    }
    else if (searchBox.getSelectionModel().getSelectedItem().equals("Number")
        && !searchField.getText().equals(""))
    {
      PlayerList tempListOutput = modelManager
          .getPlayersByNumber(Integer.parseInt(searchField.getText()),
              tempList);
      allPlayersList.getItems().clear();
      for (int i = 0; i < tempListOutput.size(); i++)
      {
        allPlayersList.getItems().add(tempListOutput.get(i));
      }

    }
    else if (searchBox.getSelectionModel().getSelectedItem().equals("Position"))
    {
      PlayerList tempListOutput = modelManager
          .getPlayersByPositions(searchField.getText(), tempList);
      allPlayersList.getItems().clear();
      for (int i = 0; i < tempListOutput.size(); i++)
      {
        allPlayersList.getItems().add(tempListOutput.get(i));
      }
    }
  }
  /**
   * update the field list
   */

  private void updateFieldList()
  {
    lineUpAndBenchList.getItems().clear();

    for (int i = 0; i < tempField.size(); i++)
    {
      lineUpAndBenchList.getItems().add(tempField.get(i));
    }
  }
  /**
   * update the list of the players in the bench
   */
  private void updateBenchList()
  {
    lineUpAndBenchList.getItems().clear();

    for (int i = 0; i < tempBench.size(); i++)
    {
      lineUpAndBenchList.getItems().add(tempBench.get(i));
    }
  }
  /**
   * set spinners
   */
  private void setSpinners()
  {
    SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(
        0, 40, 0);
    SpinnerValueFactory<Integer> valueFactory2 = new SpinnerValueFactory.IntegerSpinnerValueFactory(
        0, 40, 0);
    homeScoreSpinner.setValueFactory(valueFactory);
    opponentScoreSpinner.setValueFactory(valueFactory2);
  }
  /**
   *Set the match fields with preloaded data from the match
   * @param match set the file match
   */

  public void setFields(Match match)
  {
    datePicker.setValue(LocalDate
        .of((match.getDate().getYear()), (match.getDate().getMonth()),
            (match.getDate().getDay())));

    matchTypeBox.getSelectionModel().select(match.getMatchType());

    startTimeHourBox.getSelectionModel().select(match.getStartTime().getHour());

    endTimeHourBox.getSelectionModel().select(match.getEndTime().getHour());

    startTimeMinuteBox.getSelectionModel()
        .select(match.getStartTime().getMinute());

    endTimeMinuteBox.getSelectionModel().select(match.getEndTime().getMinute());

    opponentField.setText(match.getOpponent());

    tempField = match.getLineUp();

    tempBench = match.getBench();

    homeScoreSpinner.setValueFactory(
        new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 40,
            match.getScoreHomeTeam()));

    opponentScoreSpinner.setValueFactory(
        new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 40,
            match.getScoreOpponent()));

    updateFieldList();

    if (match.getIsAwayGame())
    {
      awayRadio.setSelected(true);
    }

    editMatch = match;
    updatePlayerList();
  }

  public void checkTime()
  {
    if (startTimeHourBox.getSelectionModel().getSelectedItem() != null
        && startTimeMinuteBox.getSelectionModel().getSelectedItem() != null
        && endTimeHourBox.getSelectionModel().getSelectedItem() != null
        && endTimeMinuteBox.getSelectionModel().getSelectedItem() != null)
    {
      if (startTimeHourBox.getValue() > endTimeHourBox.getValue())
      {
        Alert alert = new Alert(Alert.AlertType.INFORMATION,
            "Start time can't be after end time", ButtonType.OK);
        alert.setTitle("Invalid input");
        alert.setHeaderText(null);
        alert.showAndWait();
        updateTimeBoxes();

      }
      else if (startTimeHourBox.getValue() == endTimeHourBox.getValue()
          && startTimeMinuteBox.getValue() >= endTimeMinuteBox.getValue()){
        Alert alert = new Alert(Alert.AlertType.INFORMATION,
            "Start time can't be after end time", ButtonType.OK);
        alert.setTitle("Invalid input");
        alert.setHeaderText(null);
        alert.showAndWait();
        updateTimeBoxes();
      }
    }
  }
  /**
   * disable buttons,  add and remove
   */

  private void disableButtons()
  {
    addButton.setDisable(true);
    removeButton.setDisable(true);
  }
  /**
   * Inner action listener class for enabling the data of the player in add player button on player selection
   */
  private class MyListListener implements ChangeListener<Player>
  {
    public void changed(ObservableValue<? extends Player> player,
        Player oldPlayer, Player newPlayer)
    {
      if (tempField.size() < 11 && lineUpListBoolean)
      {
        addButton.setDisable(false);
      }
      else if (!lineUpListBoolean && matchTypeBox.getSelectionModel()
          .getSelectedItem().equals("League") && tempBench.size() < 5)
      {
        addButton.setDisable(false);
      }
      else if (!lineUpListBoolean && matchTypeBox.getSelectionModel()
          .getSelectedItem().equals("Cup") && tempBench.size() < 6)
      {
        addButton.setDisable(false);
      }
      else if (!lineUpListBoolean && matchTypeBox.getSelectionModel()
          .getSelectedItem().equals("Friendly"))
      {
        addButton.setDisable(false);
      }
    }
  }
  /**
   * Inner action listener class for enabling the remove button on player selection
   */
  private class MyListListener2 implements ChangeListener<Player>
  {
    public void changed(ObservableValue<? extends Player> player,
        Player oldPlayer, Player newPlayer)
    {
      removeButton.setDisable(false);
    }
  }
}

