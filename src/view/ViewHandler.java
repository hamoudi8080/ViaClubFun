package view;

import model.ViaClubModelManager;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import java.io.IOException;

public class ViewHandler
{
  private Scene scene;
  private Stage window;
  private MainViewController mainViewController;
  private AddPlayerViewController addPlayerViewController;
  private AddMatchViewController addMatchViewController;
  private UnavailabilityViewController unavailabilityViewController;

  private ViaClubModelManager modelManager;

  public ViewHandler(ViaClubModelManager modelManager) throws IOException
  {
    this.modelManager = modelManager;
    scene = new Scene(new Region());
  }

  public void start(Stage window)
  {
    this.window = window;
    window.setResizable(false);
    openView("MainView");
  }

  public void openView(String id)
  {
    Region root = null;
    switch (id)
    {
      case "MainView":
        root = loadViewMain();
        break;
      case "AddPlayerView":
        root = loadViewAddPlayer();
        break;
      case "AddMatchView":
        root = loadViewAddMatch();
        break;
      case "UnavailabilityView":
        root = loadViewUnavailability();
        break;
    }
    scene.setRoot(root);
    String title = "";

    if (root.getUserData() != null)
    {
      title += root.getUserData();
    }

    window.setTitle(title);
    window.setScene(scene);
    window.setWidth(root.getPrefWidth());
    window.setHeight(root.getPrefHeight());
    window.show();
  }

  private Region loadViewMain()
  {
    if (mainViewController == null)
    {
      try
      {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("MainView.fxml"));
        Region root = loader.load();
        mainViewController = loader.getController();
        mainViewController.init(this,modelManager,root);
      }
      catch (IOException e)
      {
        e.printStackTrace();
      }
    }
    else
    {
      mainViewController.reset();
    }
    return mainViewController.getRoot();
  }

  private Region loadViewAddPlayer()
  {
    if (addPlayerViewController == null)
    {
      try
      {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("AddPlayerView.fxml"));
        Region root = loader.load();
        addPlayerViewController = loader.getController();
        addPlayerViewController.init(this, modelManager, root);
      }
      catch (IOException e)
      {
        e.printStackTrace();
      }
    }
    else
    {
      addPlayerViewController.reset();
    }

    return addPlayerViewController.getRoot();
  }

  private Region loadViewAddMatch()
  {
    if (addMatchViewController == null)
    {
      try
      {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("AddMatchView.fxml"));
        Region root = loader.load();
        addMatchViewController = loader.getController();
        addMatchViewController.init(this, modelManager, root);
      }
      catch (IOException e)
      {
        e.printStackTrace();
      }
    }
    else
    {
      addMatchViewController.reset();
    }

    return addMatchViewController.getRoot();
  }

  private Region loadViewUnavailability()
  {
    if (unavailabilityViewController == null)
    {
      try
      {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("UnavailabilityView.fxml"));
        Region root = loader.load();
        unavailabilityViewController = loader.getController();
        unavailabilityViewController.init(this, modelManager, root);
      }
      catch (IOException e)
      {
        e.printStackTrace();
      }
    }
    else
    {
      unavailabilityViewController.reset();
    }
    return unavailabilityViewController.getRoot();
  }

  public AddPlayerViewController getAddPlayerViewController()
  {
    return addPlayerViewController;
  }
  public UnavailabilityViewController getUnavailabilityViewController(){
    return unavailabilityViewController;
  }

  public AddMatchViewController getAddMatchViewController()
  {
    return addMatchViewController;
  }

  public MainViewController getMainViewController()
  {
    return mainViewController;
  }
}
