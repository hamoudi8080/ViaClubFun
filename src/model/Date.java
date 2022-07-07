package model;

import java.io.Serializable;
import java.util.GregorianCalendar;

/**
 * A class representing a Date with a day, month and year.
 *
 * @author Java Gods
 * @version 1.0
 */
public class Date implements Serializable
{
  private int day, month, year;

  /**
   * Three-argument constructor initializing the model.Date.
   *
   * @param day   the day of the month.
   * @param month the month of the year.
   * @param year  the year.
   */
  public Date(int day, int month, int year)
  {
    this.day = day;
    this.month = month;
    this.year = year;
  }

  /**
   * Gets the day of the date
   *
   * @return the day of the date
   */
  public int getDay()
  {
    return day;
  }

  /**
   * Gets the month of the date
   *
   * @return the month of the date
   */
  public int getMonth()
  {
    return month;
  }

  /**
   * Gets the year of the date
   *
   * @return the year of the date
   */
  public int getYear()
  {
    return year;
  }

  /**
   * Checks if a this date is before a second date.
   *
   * @param date2 sets the second date.
   * @return whether or not this date is before date2
   */
  public boolean isBefore(Date date2)
  {
    if (year < date2.year)
    {
      return true;
    }
    else if (year == date2.year)
    {
      if (month < date2.month)
      {
        return true;
      }
      else if (month == date2.month)
      {
        if (day < date2.day)
        {
          return true;
        }
      }
    }
    return false;
  }

  /**
   * Checks if the year is leap.
   *
   * @return true if this year is a leap year, false otherwise.
   */
  private boolean isLeapYear()
  {
    return ((this.year % 4 == 0) && (this.year % 100 != 0)) || (this.year % 400
        == 0);
  }

  /**
   * Gets how many days there are in the month of the date.
   *
   * @return the number of days in the month.
   */
  private int daysInMonth()
  {
    if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8
        || month == 10 || month == 12)
    {
      return 31;
    }
    else if (month == 4 || month == 6 || month == 9 || month == 11)
    {
      return 30;
    }
    else if (month == 2 && isLeapYear())
    {
      return 29;
    }
    else if (month == 2)
    {
      return 28;
    }
    else
    {
      return -1;
    }
  }

  /**
   * Increases the date by one day.
   */
  public void nextDay()
  {
    if (this.day < daysInMonth())
    {
      this.day += 1;
    }
    else
    {
      this.day = 1;
      if (this.month < 12)
      {
        this.month += 1;
      }
      else
      {
        this.month = 1;
        this.year += 1;
      }
    }
  }

  /**
   * Gets the current date.
   *
   * @return Today's date.
   */
  public static Date today()
  {
    GregorianCalendar currentDate = new GregorianCalendar();
    int currentDay = currentDate.get(GregorianCalendar.DATE);
    int currentMonth = currentDate.get(GregorianCalendar.MONTH) + 1;
    int currentYear = currentDate.get(GregorianCalendar.YEAR);
    return new Date(currentDay, currentMonth, currentYear);
  }

  /**
   * Makes a copy of the date
   *
   * @return a copy of the date.
   */
  public Date copy()
  {
    return new Date(day, month, year);
  }

  /**
   * Compares day, month and year of two dates
   *
   * @param obj The object to compare with
   * @return true if the given object to this date
   */
  public boolean equals(Object obj)
  {
    if (!(obj instanceof Date))
    {
      return false;
    }

    Date other = (Date) obj;

    return day == other.day && month == other.month && year == other.year;
  }

  /**
   * Returns a string representation of the date.
   *
   * @return A string representation of the date in the format dd/mm/yyyy;
   */
  public String toString()
  {
    return String.format("%02d/%02d/%04d", day, month, year);
  }
}
