package view;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Region;
import model.*;

import java.util.ArrayList;

/**
 * A class for controlling the unavailability view
 *
 * @author JavaGods
 * @version 1.0
 */
public class UnavailabilityViewController
{
  private Region root;
  private ViaClubModelManager modelManager;
  private ViewHandler viewHandler;

  @FXML private Button addSuspensionButton;
  @FXML private Button addInjuryButton;
  @FXML private Button removeButton;
  @FXML private Button saveButton;
  @FXML private Button cancelButton;
  @FXML private Button forceAvailableButton;

  @FXML private MenuItem exitMenuItem;
  @FXML private MenuItem aboutMenuItem;
  @FXML private MenuItem helpMenuItem;

  @FXML private ListView<Unavailability> unavailabilityListView;
  @FXML private DatePicker fromDatePicker;
  @FXML private DatePicker toDatePicker;
  @FXML private ComboBox<Integer> numberOfGamesBox;
  @FXML private TextField nameField;

  private Player player;
  private ArrayList<Unavailability> tempUnavailabilities;

  /**
   * Initializes the necessary data in the view
   *
   * @param viewHandler links the views
   * @param modelManager a single access point for the functionality
   * @param root the main layout of the window
   */
  public void init(ViewHandler viewHandler, ViaClubModelManager modelManager,
      Region root)
  {
    this.modelManager = modelManager;
    this.root = root;
    this.viewHandler = viewHandler;
    unavailabilityListView.getSelectionModel().selectedItemProperty()
        .addListener((new MyListListener()));
    tempUnavailabilities = new ArrayList<Unavailability>();
    reset();
  }

  /**
   *Resets the page and updates the displayed data
   */
  public void reset()
  {
    setNumberOfGamesBox();
    player = null;
    disableButtons();
    toDatePicker.setValue(null);
    fromDatePicker.setValue(null);
    numberOfGamesBox.setValue(null);
    tempUnavailabilities.clear();
    unavailabilityListView.getItems().clear();
    setUnavailabilityList();
  }

  /**
   * Disables the remove, and injury adn add suspension buttons
   */
  public void disableButtons()
  {
    removeButton.setDisable(true);
    addInjuryButton.setDisable(true);
    addSuspensionButton.setDisable(true);
  }

  /**
   * gets the root of the view
   * @return the root of the view
   */
  public Region getRoot()
  {
    return root;
  }

  /**
   * Main method for handling events in the GUI
   *
   * @param e the targeted event
   */
  public void handleActions(ActionEvent e)
  {
    if (e.getSource() == saveButton)
    {

      player.getAllUnavailabilities().clear();
      for (int i = 0; i < tempUnavailabilities.size(); i++)
      {
        player.addUnavailability(tempUnavailabilities.get(i));
      }
      PlayerList tempPlayerList = modelManager.getAllPlayers();
      int index = tempPlayerList.getIndex(player.getName(), player.getNumber());
      tempPlayerList.set(index, player);

      modelManager.savePlayers(tempPlayerList);

      viewHandler.openView("MainView");
      viewHandler.getMainViewController().reset();
    }
    else if (e.getSource() == cancelButton)
    {
      viewHandler.openView("MainView");
      viewHandler.getMainViewController().reset();
    }

    else if (e.getSource() == addSuspensionButton)
    {
      tempUnavailabilities.add(new Unavailability(Date.today(),
          numberOfGamesBox.getSelectionModel().getSelectedItem()));
      updateUnavailabilityListView();

    }

    else if (e.getSource() == addInjuryButton)
    {
        Date tempStart = new Date(fromDatePicker.getValue().getDayOfMonth(),
            fromDatePicker.getValue().getMonthValue(),
            fromDatePicker.getValue().getYear());

        Date tempEnd = new Date(toDatePicker.getValue().getDayOfMonth(),
            toDatePicker.getValue().getMonthValue(),
            toDatePicker.getValue().getYear());
        if (tempStart != null && tempEnd != null && tempStart.isBefore(tempEnd))
        {
          tempUnavailabilities.add(new Unavailability(tempStart, tempEnd));
          unavailabilityListView.getItems()
              .add(new Unavailability(tempStart, tempEnd));
        }
        else
        {
          Alert alert = new Alert(Alert.AlertType.INFORMATION,
              "The start date should be before the end date.", ButtonType.OK);
          alert.setTitle("Exit");
          alert.setHeaderText(null);

          alert.showAndWait();
          if (alert.getResult() == ButtonType.OK)
          {
            fromDatePicker.setValue(null);
            toDatePicker.setValue(null);
          }
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
          "Here you can add, remove and see the history of your player's unavailabilities.",
          ButtonType.OK);
      alert.setTitle("About");
      alert.setHeaderText(null);
      alert.showAndWait();
    }

    else if (e.getSource() == helpMenuItem)
    {
      Alert alert = new Alert(Alert.AlertType.INFORMATION,
          "For client support, please refer to JavaGods.", ButtonType.OK);
      alert.setTitle("About");
      alert.setHeaderText(null);
      alert.showAndWait();
    }

    else if (e.getSource() == removeButton)
    {

      tempUnavailabilities.remove(unavailabilityListView.getSelectionModel().getSelectedItem());
      updateUnavailabilityListView();
    }

    else if (e.getSource() == forceAvailableButton)
    {
      if (tempUnavailabilities != null)
      {
        for (int i = 0; i < tempUnavailabilities.size(); i++)
        {
          if (tempUnavailabilities.get(i).isActive())
          {
            tempUnavailabilities.get(i).setAvailable(Date.today());
          }
        }
      }
      updateUnavailabilityListView();
    }
    else if (e.getSource() == numberOfGamesBox)
    {
      addSuspensionButton.setDisable(false);
    }
    else if (e.getSource() == fromDatePicker)
    {
      makeAddInjuryButtonAvailable();
    }
    else if (e.getSource() == toDatePicker)
    {
      makeAddInjuryButtonAvailable();
    }
  }

  /**
   * Sets the fields with the preloaded data from the player
   *
   * @param player  sets the player the data is taken from
   */
  public void setFields(Player player)
  {
    reset();

    nameField.setText(player.getName());
    if (player.getAllUnavailabilities().size() > 0)
    {
      unavailabilityListView.getItems().clear();
      for (int i = 0; i < player.getAllUnavailabilities().size(); i++)
      {
        unavailabilityListView.getItems()
            .add(player.getAllUnavailabilities().get(i));

      }
      tempUnavailabilities.clear();
      for (int i = 0; i < unavailabilityListView.getItems().size(); i++)
      {
        tempUnavailabilities.add(unavailabilityListView.getItems().get(i));
      }
    }
    this.player = player;
  }

  private void makeAddInjuryButtonAvailable()
  {
    if (fromDatePicker.getValue()!=null && toDatePicker.getValue()!=null)
    {
      addInjuryButton.setDisable(false);
    }
  }

  /**
   * Sets the numbers in the suspension combo box
   */
  public void setNumberOfGamesBox()
  {
    for (int i = 0; i < 10; i++)
    {
      numberOfGamesBox.getItems().add(i + 1);
    }
  }

  /**
   * Updates the unavailability list
   */
  public void updateUnavailabilityListView()
  {
    if (modelManager != null && player != null)
    {
      unavailabilityListView.getItems().clear();
      for (int i = 0; i < tempUnavailabilities.size(); i++)
      {
        unavailabilityListView.getItems().add(tempUnavailabilities.get(i));
      }
    }
  }

  /**
   * Sets the unavailability list
   */
  public void setUnavailabilityList()
  {
    if (modelManager != null && player != null)
    {

      unavailabilityListView.getItems().clear();
      tempUnavailabilities = player.getAllUnavailabilities();
      for (int i = 0; i < tempUnavailabilities.size(); i++)
      {
        unavailabilityListView.getItems().add(tempUnavailabilities.get(i));
      }
    }
  }

  /**
   * Inner action listener class for enabling the remove button on unavailability selection
   */
  private class MyListListener implements ChangeListener<Unavailability>
  {
    public void changed(
        ObservableValue<? extends Unavailability> unavailability,
        Unavailability oldUnavailability, Unavailability newUnavailability)
    {
      removeButton.setDisable(false);
    }
  }
}
