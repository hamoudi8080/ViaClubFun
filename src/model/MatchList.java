package model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * A class for creating match lists
 *
 * @author Java Gods
 * @version 1.0
 */
public class MatchList implements Serializable
{
  private ArrayList<Match> matches;

  /**
   * No-argument constructor
   * initializes the matches list
   */
  public MatchList()
  {
    matches = new ArrayList<Match>();
  }

  /**
   * Add a match to the match list
   *
   * @param match is added to the match list
   */
  public void add(Match match)
  {
    matches.add(match);
  }

  /**
   * Removes a match from the match list
   *
   * @param match is removed from the match list
   */

  public void remove(Match match)
  {
    matches.remove(match);
  }

  /**
   * Return the size of the match list
   *
   * @return returns the size of the match list
   */
  public int size()
  {
    return matches.size();
  }

  /**
   * Gets a match from a specific position in the match list
   *
   * @param index the position of the match in the list
   * @return the match from the specific position in the list
   */
  public Match get(int index)
  {
    if (index < matches.size())
    {
      return matches.get(index);
    }
    else
    {
      return null;
    }
  }

  /**
   * Sets the match in the match list at a specific position
   *
   * @param index the position in the list
   * @param match the match which is set to the position in the list
   */
  public void set(int index, Match match)
  {
    matches.set(index, match);
  }

  /**
   * Gets the index of the match in the list
   *
   * @param startTime  the start time of the match
   * @param endTime    the end time of the match
   * @param date       the date of the match
   * @param opponent   the opponent in the match
   * @param matchType  the match type
   * @param isAwayGame whether or not is away game
   * @return the position of the match in the list
   */
  public int getIndex(Time startTime, Time endTime, Date date, String opponent,
      String matchType, boolean isAwayGame)
  {
    for (int i = 0; i < matches.size(); i++)
    {
      Match temp = matches.get(i);

      if (temp.getStartTime().equals(startTime) && temp.getEndTime()
          .equals(endTime) && temp.getDate().equals(date) && temp.getOpponent()
          .equals(opponent) && temp.getMatchType().equals(matchType)
          && temp.getIsAwayGame() == isAwayGame)
      {
        return i;
      }
    }
    return -1;
  }

  /**
   * A string representation of the match list
   *
   * @return the string representation of the match list
   */
  public String toString()
  {
    String returnStr = "";
    for (int i = 0; i < matches.size(); i++)
    {
      Match temp = matches.get(i);
      returnStr += temp + "\n";
    }
    return returnStr;
  }
}
