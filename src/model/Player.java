package model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * A class for creating players with a name, number, positions, unavailabilities, the number of games beched in a row and stats
 *
 * @author Java Gods
 * @version 1.0
 */
public class Player implements Serializable
{
  private String name;
  private int number, benchedInARow;
  private ArrayList<String> positions;
  private ArrayList<Unavailability> unavailabilities;

  /**
   * Two argument constructor for creating a player
   *
   * @param name   sets the name of the player
   * @param number sets the jersey number of the player
   *               sets the positions and the unavailabilities arrayLists.
   */
  public Player(String name, int number)
  {
    this.name = name;
    this.number = number;
    positions = new ArrayList<String>();
    unavailabilities = new ArrayList<Unavailability>();
  }

  /**
   * One argument constructor for creating a player
   *
   * @param name sets the name of the player
   *             number is set to 0,  sets the positions and the unavailabilities arrayLists.
   */
  public Player(String name)
  {
    this.name = name;
    this.number = 0;
    positions = new ArrayList<String>();
    unavailabilities = new ArrayList<Unavailability>();
  }

  /**
   * Returns the name of the player
   *
   * @return the name of the player
   */
  public String getName()
  {
    return name;
  }

  /**
   * Returns the jersey number of the player
   *
   * @return the jersey number of the player
   */
  public int getNumber()
  {
    return number;
  }

  /**
   * sets the jersey number of the player
   *
   * @param number sets the jersey number of the player
   */
  public void setNumber(int number)
  {
    this.number = number;
  }

  /**
   * Adds a position the positions array list
   *
   * @param position the position added to the array list
   */
  public void addPosition(String position)
  {
    positions.add(position);
  }

  /**
   * Gets the trained positions of the player
   *
   * @return the positions of the player
   */
  public ArrayList<String> getPositions()
  {
    return positions;
  }

  /**
   * Checks if the player is currently suspended
   *
   * @return true if of aof the player unavailabilities is active and of type suspended.
   */
  public boolean isSuspended()
  {
    for (int i = 0; i < unavailabilities.size(); i++)
    {
      if (unavailabilities.get(i).isActive() && unavailabilities.get(i)
          .getType().equals("Suspended"))
      {
        return true;
      }
    }
    return false;
  }

  /**
   * Checks if the player is injured
   *
   * @return true if one of the player's unavailabilities is active and of type injured
   */
  public boolean isInjured()
  {
    for (int i = 0; i < unavailabilities.size(); i++)
    {
      if (unavailabilities.get(i).isActive() && unavailabilities.get(i)
          .getType().equals("Injured"))
      {
        return true;
      }
    }
    return false;
  }

  /**
   * Adds a unavailability to the unavailabilities array list
   *
   * @param unavailability is added the the unavaliabilities array list
   */
  public void addUnavailability(Unavailability unavailability)
  {
    unavailabilities.add(unavailability);
  }

  /**
   * Gets the history of the player's unavailabilities
   *
   * @return all unavailability objects from unavailabilites array list
   */
  public ArrayList<Unavailability> getAllUnavailabilities()
  {
    return unavailabilities;
  }

  /**
   * Sets the number of games benched in a row
   *
   * @param games sets the number of games benched in a row
   */
  public void setBenchedInARow(int games)
  {
    benchedInARow = games;
  }

  /**
   * Compares the name, the number, the positions and the unavailabilities of two players
   *
   * @param obj the object to compare with
   * @return true if a give object is equal to this player
   */
  public boolean equals(Object obj)
  {
    if (!(obj instanceof Player))
    {
      return false;
    }
    else
    {
      Player other = (Player) obj;
      return name.equals(other.name) && number == other.number && positions
          .equals(other.positions) && unavailabilities
          .equals(other.unavailabilities);
    }
  }

  /**
   * Returns a string representation of the player
   *
   * @return a string representation of the player in the format Name: name Number: number Positions positions Status: status Games benched in a row: benched in a row
   */
  public String toString()
  {
    String returnStr = "Name: " + name + " Number: " + number + " Positions: ";
    for (int i = 0; i < positions.size(); i++)
    {
      if (i + 1 != positions.size())
      {
        returnStr += positions.get(i) + ", ";
      }
      else
      {
        returnStr += positions.get(i);
      }
    }

    if (isInjured())
    {
      returnStr += " Status: Injured";
    }
    else if (isSuspended())
    {
      returnStr += " Status: Suspended";
    }
    else
    {
      returnStr += " Status: Available";
    }
    returnStr += " Games benched in a row: " + benchedInARow;
    return returnStr;
  }
}

