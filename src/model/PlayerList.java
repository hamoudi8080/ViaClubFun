package model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * A class representing a list of players
 * @author Java Gods
 * @version 1.0
 */
public class PlayerList implements Serializable
{
  private ArrayList<Player> players;

  /**
   * No-argument constructor initializing the player list
   */
  public PlayerList()
  {
    this.players = new ArrayList<Player>();
  }

  /**
   * Adds a player to the player's list
   * @param player the player added to the list
   */
  public void add(Player player)
  {
    players.add(player);
  }

  /**
   * Removes a player from the list
   * @param player the player removed form the list
   */
  public void remove(Player player)
  {
    players.remove(player);
  }

  /**
   * Gets the size of the player list
   * @return the size of the player list
   */

  public int size()
  {
    return players.size();
  }

  /**
   * Gets the player at a specific position in the list
   * @param index the position in the list
   * @return the player at the specified index in the list
   */
  public Player get(int index)
  {
    if (index < players.size())
    {
      return players.get(index);
    }
    else
    {
      return null;
    }
  }

  /**
   * Gets a player from the list by the name
   * @param name is searched for in the player list
   * @return a player if it has the same name
   */
  public Player get(String name)
  {
    for (int i = 0; i < players.size(); i++)
    {
      Player temp = players.get(i);

      if (temp.getName().equals(name))
      {
        return temp;
      }
    }
    return null;
  }

  /**
   * Checks if a player is contained in the player list
   *
   * @param player sets the player which is checked if is contained in the list
   * @return  true if the player is inside the list
   */

  public boolean contains(Player player){
    return players.contains(player);
  }

  /**
   * Sets the player at a specific index in the player list
   *
   * @param index the position in the list
   * @param player the player which is set to the position
   */
  public void set(int index, Player player)
  {
    players.set(index, player);
  }

  /**
   *Gets the index of the player that has a specific name and number
   *
   * @param name the name of the player looked for
   * @param number the number of the player looked for
   * @return the position of the player with the specified name and number in the player list
   */
  public int getIndex(String name, int number)
  {
    for (int i = 0; i < players.size(); i++)
    {
      Player temp = players.get(i);

      if (temp.getName().equals(name) && temp.getNumber() == number)
      {
        return i;
      }
    }
    return -1;
  }

  /**
   * Clears the player list
   */
  public void clear()
  {
    players.clear();
  }

  /**
   * Compares two player lists
   *
   * @param obj the object compared with
   * @return true if the given object is equal to the player list
   */
  public boolean equals(Object obj)
  {
    if (!(obj instanceof PlayerList))
    {
      return false;
    }

    PlayerList other = (PlayerList) obj;
    return players.equals(other.players);
  }

  /**
   * A string representation of the player list
   *
   * @return the string representation of the player list
   */
  public String toString()
  {
    String returnStr = "";
    for (int i = 0; i < players.size(); i++)
    {
      Player temp = players.get(i);

      returnStr += temp + "\n";
    }
    return returnStr;
  }
}
