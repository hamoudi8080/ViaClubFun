package model;

import java.io.Serializable;

/**
 * A class for creating unavailability objects
 *
 * @author Java Gods
 * @version 1.0
 */
public class Unavailability implements Serializable
{
  private String type;
  private int numberOfGames;
  private Date start, end;

  /**
   * Three-argument constructor
   *
   * @param start         sets the start date of the model.Unavailability
   * @param numberOfGames sets the number of games the model.Unavailability is for
   *                      isAvailable method is set initially to false
   */
  public Unavailability(Date start, int numberOfGames)
  {
    this.type = "Suspended";
    this.start = start.copy();
    this.numberOfGames = numberOfGames;

  }

  /**
   * One-argument constructor
   *
   * @param start sets the start date of the unavailability
   *              type is set to injury
   */
  public Unavailability(Date start)
  {
    this.type = "Injury";
    this.start = start.copy();

    numberOfGames = -1;

  }

  /**
   * Two-argument constructor
   *
   * @param start sets the start of the unvailability
   * @param end   sets the end of the unavailability
   */
  public Unavailability(Date start, Date end)
  {
    this.type = "Injured";
    this.start = start.copy();
    this.end = end.copy();

    numberOfGames = -1;
  }

  /**
   * Gets the type of the unavailability
   *
   * @return the type the unavailability
   */
  public String getType()
  {
    return type;
  }

  /**
   * gets the number of games for the suspension
   *
   * @return the number of games for the suspension
   */
  public int getNumberOfGames()
  {
    return numberOfGames;
  }

  /**
   * Sets the number of games of the suspension
   *
   * @param numberOfGames sets the number of games of the suspension
   */
  public void setNumberOfGames(int numberOfGames)
  {
    this.numberOfGames = numberOfGames;
  }

  /**
   * ends the unavailability and sets the end to the current date
   *
   * @param end sets the end of the unavailability as the current date
   */
  public void setAvailable(Date end)
  {

    this.end = end;
    numberOfGames = 0;

  }

  /**
   * Checks if the unavailability is active
   *
   * @return true if the unavailability is active
   */
  public boolean isActive()
  {
    if (type.equals("Suspended") && numberOfGames > 0)
    {
      return true;
    }

    else
      return type.equals("Injured") && !(end.isBefore(Date.today())) && !end
          .equals(Date.today());
  }

  /**
   * Gets the start date of the unavailability
   *
   * @return the start date of the unavailability
   */
  public Date getStart()
  {
    return start.copy();
  }

  /**
   * Compares two unavailabilities
   *
   * @param obj the object compared with
   * @return true if the object given equals the unavailability
   */
  public boolean equals(Object obj)
  {
    if (!(obj instanceof Unavailability))
    {
      return false;
    }

    Unavailability other = (Unavailability) obj;
    if (end != null)
    {
      return numberOfGames == other.numberOfGames && start.equals(other.start)
          && end.equals(other.end) && type.equals(other.type);
    }
    else
    {
      return numberOfGames == other.numberOfGames && start.equals(other.start)
          && other.end == null && type.equals(other.type);
    }
  }

  /**
   * Returns a string representation of the unavailability
   *
   * @return a string representation of the unavailability
   */
  public String toString()
  {
    String finalString = "";

    if (type.equals("Suspended"))
    {
      finalString = "Type: Suspended; Number of games: " + numberOfGames;
    }
    else if (type.equals("Injured"))
    {
      finalString =
          "Type: Injured; Start Date: " + start + "; End Date: " + end;
    }

    finalString += "; is active: " + isActive();
    return finalString;
  }
}

