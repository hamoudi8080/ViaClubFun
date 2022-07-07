package model;

import java.io.Serializable;
import java.time.LocalTime;

/**
 * A class representing time with hours, minutes and seconds.
 *
 * @author Java Gods
 * @version 1.0
 */
public class Time implements Serializable
{
  private int hour, minute, second;

  /**
   * Three-argument constructor initializing the model.Time.
   *
   * @param h the hour of the day.
   * @param m the minute of the hour.
   * @param s the second of the minute.
   */
  public Time(int h, int m, int s)
  {
    this.hour = h;
    this.minute = m;
    this.second = s;
  }

  /**
   * One-argument constructor initializing the model.Time.
   *
   * @param totalTimeInSeconds sets the time of the day from total seconds.
   */
  public Time(int totalTimeInSeconds)
  {
    hour = totalTimeInSeconds / 3600;
    totalTimeInSeconds = totalTimeInSeconds - hour * 3600;
    minute = totalTimeInSeconds / 60;
    totalTimeInSeconds = totalTimeInSeconds - minute * 60;
    second = totalTimeInSeconds;
  }

  /**
   * Gets the hour of the day
   *
   * @return the hour of the day
   */
  public int getHour()
  {
    return hour;
  }

  /**
   * Gets the minute of the hour
   *
   * @return the minute of the hour
   */
  public int getMinute()
  {
    return minute;
  }

  /**
   * Compares the hour, minute and second of two times
   *
   * @param obj The object to compare this time against.
   * @return true if the given object is equal to this time.
   */
  public boolean equals(Object obj)
  {
    if (!(obj instanceof Time))
    {
      return false;
    }
    Time other = (Time) obj;

    return hour == other.hour && minute == other.minute
        && second == other.second;
  }

  /**
   * Gives a string representation of the object.
   *
   * @return A string representation of the time in the format hh:mm:ss.
   */
  public String toString()
  {
    return String.format("%02d:%02d:%02d", hour, minute, second);
  }
}
