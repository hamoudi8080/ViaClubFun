package main;

import model.Player;
import model.PlayerList;
import utils.MyFileHandler;

import java.io.FileNotFoundException;
import java.io.IOException;

public class LoadInitialData
{
   public static void main(String[] args)
   {
      PlayerList players = new PlayerList();
      String[] playerArray = null;

      try
      {
         playerArray = MyFileHandler.readArrayFromTextFile("ViaClubFun\\src\\players.txt");
                      
         for(int i = 0; i<playerArray.length; i++)
         {
            String temp = playerArray[i];
            String[] tempArr = temp.split(",");
            String name = tempArr[0];
            int number = Integer.parseInt(tempArr[1]);


            players.add(new Player(name,number));
         }
         System.out.println(players);
      }
      catch (FileNotFoundException e)
      {
         System.out.println("File was not found, or could not be opened");
      }
     
      try
      {
         MyFileHandler.writeToBinaryFile("ViaClubFun\\src\\players.bin", players);
      }
      catch (FileNotFoundException e)
      {
         System.out.println("Error opening file ");
      }
      catch (IOException e)
      {
         System.out.println("IO Error writing to file ");
      }
      
      System.out.println("Done");
   }
}
