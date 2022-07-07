package model;

import java.io.Serializable;

/**
 * A class for creating match with a date, a start and a end time, a line-up, a bench, an opponent, a match type and a score
 */
public class Match implements Serializable
{
  private Time startTime, endTime;
  private Date date;
  private PlayerList bench, lineUp;
  private String opponent, matchType;
  private int scoreHomeTeam, scoreOpponent;
  private boolean isAwayGame;

  /**
   * Six-argument constructor for creating a match
   *
   * @param startTime  sets the start time of the match
   * @param endTime    sets the end time of the match
   * @param date       sets the date
   * @param opponent   sets the opponent
   * @param matchType  sets the match type(league, cup or friendly)
   * @param isAwayGame sets whether or not the match is away
   */
  public Match(Time startTime, Time endTime, Date date, String opponent,
      String matchType, boolean isAwayGame)
  {
    this.startTime = startTime;
    this.endTime = endTime;
    this.date = date;
    this.opponent = opponent;
    this.matchType = matchType;
    this.bench = null;
    this.lineUp = null;
    this.scoreHomeTeam = 0;
    this.scoreOpponent = 0;
    this.isAwayGame = isAwayGame;

  }

  /**
   * Gets the score of the home team
   *
   * @return the score of the home team
   */

  public int getScoreHomeTeam()
  {
    return scoreHomeTeam;
  }

  /**
   * Sets the score of the home team
   *
   * @param goals is set as the score of the home team
   */

  public void setScoreHomeTeam(int goals)
  {
    this.scoreHomeTeam = goals;
  }

  /**
   * Gets the score of the opponent team
   *
   * @return the score of the opponent
   */
  public int getScoreOpponent()
  {
    return scoreOpponent;
  }

  /**
   * Sets the score of the opponent
   *
   * @param goals is set as the score of the opponent team
   */
  public void setScoreOpponent(int goals)
  {
    this.scoreOpponent = goals;
  }

  /**
   * A string representation of the match score
   *
   * @return a string representation of the match score in the format: score home team- score opponent
   */
  public String getMatchScore()
  {
    return scoreHomeTeam + " - " + scoreOpponent;
  }

  /**
   * Returns the match type of the match
   *
   * @return the match type
   */
  public String getMatchType()
  {
    return matchType;
  }

  /**
   * Gets the date of the match
   *
   * @return the date of the match
   */
  public Date getDate()
  {
    return date;
  }

  /**
   * Sets the date of the match
   *
   * @param date sets the date of the match
   */
  public void setDate(Date date)
  {
    this.date = date;
  }

  /**
   * Gets the opponent of the match
   *
   * @return the opponent of the match
   */
  public String getOpponent()
  {
    return opponent;
  }


  /**
   * Gets the start of the match
   *
   * @return the start of the match
   */
  public Time getStartTime()
  {
    return startTime;
  }

  /**
   * Gets the end of the match
   *
   * @return the end of the match
   */
  public Time getEndTime()
  {
    return endTime;
  }

  /**
   * Gets if the game is away
   *
   * @return true if the game is away
   */
  public boolean getIsAwayGame()
  {
    return isAwayGame;
  }

  /**
   * Adds a player list as a starting line-up for the match
   *
   * @param lineUp sets the line-up of the match
   */
  public void addLineUp(PlayerList lineUp)
  {
    this.lineUp = lineUp;
  }

  /**
   * gets the starting line-up
   *
   * @return the starting line-up
   */
  public PlayerList getLineUp()
  {
    return lineUp;
  }

  /**
   * Adds a player list as the bench for the match
   *
   * @param bench sets the bench of the match
   */

  public void addBench(PlayerList bench)
  {
    this.bench = bench;
  }

  /**
   * Gets the bench of the match
   *
   * @return the bench of the match
   */
  public PlayerList getBench()
  {
    return bench;
  }

  /**
   * Compares two matches
   *
   * @param obj the object compared to
   * @return true if the given object equals the match
   */
  public boolean equals(Object obj)
  {
    if (!(obj instanceof Match))
    {
      return false;
    }

    Match other = (Match) obj;
    return startTime.equals(other.startTime) && endTime.equals(other.endTime)
        && date.equals(other.date) && lineUp.equals(other.lineUp) && bench
        .equals(other.bench) && opponent.equals(other.opponent) && matchType
        .equals(other.matchType) && scoreHomeTeam == other.scoreHomeTeam
        && scoreOpponent == other.scoreOpponent && isAwayGame == other.isAwayGame;
  }

  /**
   * A representation of the match as a string
   *
   * @return the representation of the match as a string
   */

  public String toString()
  {
    String returnStr =
        matchType + " Match\n" + "Opponent: " + opponent + "\nDate: " + date
            + "\nTime: " + startTime + " - " + endTime + "\nScore: "
            + getMatchScore();

    if (lineUp.size() > 0)
    {
      returnStr += "\nStarting line up:";
      for (int i = 0; i < lineUp.size(); i++)
      {
        returnStr += "\n" + lineUp.get(i);
      }
    }
    if (bench.size() > 0)
    {
      returnStr += "\nBench:";
      for (int i = 0; i < bench.size(); i++)
      {
        returnStr += "\n" + bench.get(i);
      }
      return returnStr;
    }
    return returnStr;
  }
}
