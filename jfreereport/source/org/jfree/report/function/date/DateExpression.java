/**
 * ========================================
 * <libname> : a free Java <foobar> library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/liblayout/
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2005, by Object Refinery Limited and Contributors.
 *
 * This library is free software; you can redistribute it and/or modify it under the terms
 * of the GNU Lesser General Public License as published by the Free Software Foundation;
 * either version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this
 * library; if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 * Boston, MA 02111-1307, USA.
 *
 * ---------
 * DateExpression.java
 * ---------
 *
 * Original Author:  Thomas Morgner;
 * Contributors: -;
 *
 * $Id: DateExpression.java,v 1.1 2006/01/20 19:50:52 taqua Exp $
 *
 * Changes
 * -------------------------
 * 20.01.2006 : Initial version
 */
package org.jfree.report.function.date;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.jfree.report.ResourceBundleFactory;
import org.jfree.report.function.AbstractExpression;

/**
 * Creation-Date: 20.01.2006, 18:50:12
 *
 * @author Thomas Morgner
 */
public class DateExpression extends AbstractExpression
{
  private Integer month;
  private Integer year;
  private Integer hour;
  private Integer minute;
  private Integer second;
  private Integer milliSecond;
  private Long time;
  private Integer dayOfWeek;
  private Integer dayOfYear;
  private Integer dayOfMonth;
  private Integer dayOfWeekInMonth;
  private TimeZone timeZone;

  public DateExpression()
  {
  }

  public TimeZone getTimeZone()
  {
    return timeZone;
  }

  public void setTimeZone(final TimeZone timeZone)
  {
    this.timeZone = timeZone;
  }

  public Integer getMonth()
  {
    return month;
  }

  public void setMonth(final Integer month)
  {
    this.month = month;
  }

  public Integer getDay()
  {
    return dayOfMonth;
  }

  public void setDay(final Integer day)
  {
    this.dayOfMonth = day;
  }

  public Integer getYear()
  {
    return year;
  }

  public void setYear(final Integer year)
  {
    this.year = year;
  }

  public Integer getHour()
  {
    return hour;
  }

  public void setHour(final Integer hour)
  {
    this.hour = hour;
  }

  public Integer getMinute()
  {
    return minute;
  }

  public void setMinute(final Integer minute)
  {
    this.minute = minute;
  }

  public Integer getSecond()
  {
    return second;
  }

  public void setSecond(final Integer second)
  {
    this.second = second;
  }

  public Integer getMilliSecond()
  {
    return milliSecond;
  }

  public void setMilliSecond(final Integer milliSecond)
  {
    this.milliSecond = milliSecond;
  }

  public Long getTime()
  {
    return time;
  }

  public void setTime(final Long time)
  {
    this.time = time;
  }

  public Integer getDayOfWeek()
  {
    return dayOfWeek;
  }

  public void setDayOfWeek(final Integer dayOfWeek)
  {
    this.dayOfWeek = dayOfWeek;
  }

  public Integer getDayOfYear()
  {
    return dayOfYear;
  }

  public void setDayOfYear(final Integer dayOfYear)
  {
    this.dayOfYear = dayOfYear;
  }

  public Integer getDayOfMonth()
  {
    return dayOfMonth;
  }

  public void setDayOfMonth(final Integer dayOfMonth)
  {
    this.dayOfMonth = dayOfMonth;
  }

  public Integer getDayOfWeekInMonth()
  {
    return dayOfWeekInMonth;
  }

  public void setDayOfWeekInMonth(final Integer dayOfWeekInMonth)
  {
    this.dayOfWeekInMonth = dayOfWeekInMonth;
  }

  /**
   * Return the current expression value. <P> The value depends (obviously) on
   * the expression implementation.
   *
   * @return the value of the function.
   */
  public Object getValue()
  {
    Calendar calendar = getCalendar();
    configureCalendar(calendar);
    return calendar.getTime();
  }

  protected void configureCalendar(final Calendar calendar) {
    if (time != null)
    {
      calendar.setTime(new Date(time.longValue()));
    }
    else
    {
      if (month != null)
      {
        calendar.set(Calendar.MONTH, month.intValue());
      }
      if (dayOfMonth != null)
      {
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth.intValue());
      }
      if (year != null)
      {
        calendar.set(Calendar.YEAR, year.intValue());
      }
      if (hour != null)
      {
        calendar.set(Calendar.HOUR, hour.intValue());
      }
      if (minute != null)
      {
        calendar.set(Calendar.MINUTE, minute.intValue());
      }
      if (second != null)
      {
        calendar.set(Calendar.SECOND, second.intValue());
      }
      if (milliSecond != null)
      {
        calendar.set(Calendar.MILLISECOND, milliSecond.intValue());
      }
      if (dayOfWeek != null)
      {
        calendar.set(Calendar.DAY_OF_WEEK, dayOfWeek.intValue());
      }
      if (dayOfYear != null)
      {
        calendar.set(Calendar.DAY_OF_YEAR, dayOfYear.intValue());
      }
      if (dayOfWeekInMonth != null)
      {
        calendar.set(Calendar.DAY_OF_WEEK_IN_MONTH, dayOfWeekInMonth.intValue());
      }
      if (timeZone != null)
      {
        calendar.setTimeZone(getTimeZone());
      }
    }
  }

  protected Calendar getCalendar()
  {
    ResourceBundleFactory rf = getResourceBundleFactory();
    return Calendar.getInstance(rf.getLocale());
  }
}
