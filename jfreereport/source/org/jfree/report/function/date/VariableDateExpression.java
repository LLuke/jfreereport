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
 * VariableDateExpression.java
 * ---------
 *
 * Original Author:  Thomas Morgner;
 * Contributors: -;
 *
 * $Id: Anchor.java,v 1.3 2005/02/23 21:04:29 taqua Exp $
 *
 * Changes
 * -------------------------
 * 20.01.2006 : Initial version
 */
package org.jfree.report.function.date;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Creation-Date: 20.01.2006, 19:04:24
 *
 * @author Thomas Morgner
 */
public class VariableDateExpression extends DateExpression
{
  private String monthField;
  private String yearField;
  private String hourField;
  private String minuteField;
  private String secondField;
  private String milliSecondField;
  private String timeField;
  private String dayOfWeekField;
  private String dayOfYearField;
  private String dayOfMonthField;
  private String dayOfWeekInMonthField;
  private String timeZoneField;


  public VariableDateExpression()
  {
  }

  public String getMonthField()
  {
    return monthField;
  }

  public void setMonthField(final String monthField)
  {
    this.monthField = monthField;
  }

  public String getYearField()
  {
    return yearField;
  }

  public void setYearField(final String yearField)
  {
    this.yearField = yearField;
  }

  public String getHourField()
  {
    return hourField;
  }

  public void setHourField(final String hourField)
  {
    this.hourField = hourField;
  }

  public String getMinuteField()
  {
    return minuteField;
  }

  public void setMinuteField(final String minuteField)
  {
    this.minuteField = minuteField;
  }

  public String getSecondField()
  {
    return secondField;
  }

  public void setSecondField(final String secondField)
  {
    this.secondField = secondField;
  }

  public String getMilliSecondField()
  {
    return milliSecondField;
  }

  public void setMilliSecondField(final String milliSecondField)
  {
    this.milliSecondField = milliSecondField;
  }

  public String getTimeField()
  {
    return timeField;
  }

  public void setTimeField(final String timeField)
  {
    this.timeField = timeField;
  }

  public String getDayOfWeekField()
  {
    return dayOfWeekField;
  }

  public void setDayOfWeekField(final String dayOfWeekField)
  {
    this.dayOfWeekField = dayOfWeekField;
  }

  public String getDayOfYearField()
  {
    return dayOfYearField;
  }

  public void setDayOfYearField(final String dayOfYearField)
  {
    this.dayOfYearField = dayOfYearField;
  }

  public String getDayOfMonthField()
  {
    return dayOfMonthField;
  }

  public void setDayOfMonthField(final String dayOfMonthField)
  {
    this.dayOfMonthField = dayOfMonthField;
  }

  public String getDayOfWeekInMonthField()
  {
    return dayOfWeekInMonthField;
  }

  public void setDayOfWeekInMonthField(final String dayOfWeekInMonthField)
  {
    this.dayOfWeekInMonthField = dayOfWeekInMonthField;
  }

  public String getTimeZoneField()
  {
    return timeZoneField;
  }

  public void setTimeZoneField(final String timeZoneField)
  {
    this.timeZoneField = timeZoneField;
  }

  protected void configureCalendar(final Calendar calendar)
  {
    // first add the hardcoded values, if any ...
    super.configureCalendar(calendar);
    // then the variable values ..
    if (timeField != null)
    {
      final Object o = getDataRow().get(getTimeZoneField());
      if (o instanceof Number)
      {
        Number n = (Number) o;
        calendar.setTime(new Date (n.longValue()));
      }
      else if (o instanceof Date)
      {
        Date d = (Date) o;
        calendar.setTime(d);
      }
    }
    else
    {
      if (monthField != null)
      {
        trySetField(calendar, Calendar.MONTH, monthField);
      }
      if (dayOfMonthField != null)
      {
        trySetField(calendar, Calendar.DAY_OF_MONTH, dayOfMonthField);
      }
      if (yearField != null)
      {
        trySetField(calendar, Calendar.YEAR, yearField);
      }
      if (hourField != null)
      {
        trySetField(calendar, Calendar.HOUR, hourField);
      }
      if (minuteField != null)
      {
        trySetField(calendar, Calendar.MINUTE, minuteField);
      }
      if (secondField != null)
      {
        trySetField(calendar, Calendar.SECOND, secondField);
      }
      if (milliSecondField != null)
      {
        trySetField(calendar, Calendar.MILLISECOND, milliSecondField);
      }
      if (dayOfWeekField != null)
      {
        trySetField(calendar, Calendar.DAY_OF_WEEK, dayOfWeekField);
      }
      if (dayOfYearField != null)
      {
        trySetField(calendar, Calendar.DAY_OF_YEAR, dayOfYearField);
      }
      if (dayOfWeekInMonthField != null)
      {
        trySetField(calendar, Calendar.DAY_OF_WEEK_IN_MONTH, dayOfWeekInMonthField);
      }
      if (timeZoneField != null)
      {
        final Object o = getDataRow().get(getTimeZoneField());
        if (o instanceof String)
        {
          calendar.setTimeZone(TimeZone.getTimeZone((String) o));
        }
        else if (o instanceof TimeZone)
        {
          calendar.setTimeZone((TimeZone)o);
        }
      }
    }

  }

  private void trySetField(Calendar calendar, int field, String column)
  {
    if (column == null)
    {
      return;
    }
    Object o = getDataRow().get(column);
    if (o instanceof Number == false)
    {
      return;
    }
    Number n = (Number) o;
    calendar.set(field, n.intValue());
  }
}
