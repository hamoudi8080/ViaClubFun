package view;

import javafx.scene.image.Image;
import model.ViaClubModelManager;

import javafx.application.Application;
import javafx.stage.Stage;

public class StartGUI extends Application
{
  public void start(Stage window) throws Exception
  {
    ViaClubModelManager modelManager = new ViaClubModelManager(
        "ViaClubFun\\src\\players.bin", "ViaClubFun\\src\\matches.bin");
    ViewHandler viewHandler = new ViewHandler(modelManager);
    Image ViaClub = new Image("file:ViaClubFun\\viac_small.png");
    window.getIcons().add(ViaClub);
    viewHandler.start(window);
  }
}
