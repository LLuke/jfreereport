/**
 * =========================================
 * LibFormula : a free Java formula library
 * =========================================
 *
 * Project Info:  http://reporting.pentaho.org/libformula/
 *
 * (C) Copyright 2006-2007, by Pentaho Corporation and Contributors.
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
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 *
 * ------------
 * $Id: DateUtil.java,v 1.2 2007/05/08 09:47:09 taqua Exp $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.formula.util;

import java.math.BigDecimal;
import java.sql.Time;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.jfree.formula.DefaultLocalizationContext;
import org.jfree.formula.LocalizationContext;
import org.jfree.formula.typing.Type;
import org.jfree.formula.typing.coretypes.DateType;

/**
 *
 * @author Cedric Pronzato
 *
 */
public class DateUtil
{
  private static final Date ISO8001_TIME = new GregorianCalendar().getTime();

  /**
   * Converts a <code>Date</code> value according to the requested
   * <code>Type</code> to the proper <code>Date</code> subclasses (<code>java.sql.Time</code>,
   * <code>java.sql.Date</code>) if needed. If the requested type is unknown,
   * no conversion takes place and the input date is returned.
   *
   * @param fromDate
   *          The date to convert.
   * @param toType
   *          The requested type of date.
   * @param loosePrecision
   *          if the conversion should keep the original the "date" as is.
   * @return The converted date.
   */
  public static Date normalizeDate(Date fromDate, Type toType)
  {
    if (fromDate == null || toType == null)
    {
      throw new IllegalArgumentException();
    }

    Number serial = toSerialDate(fromDate, null);
    serial = normalizeDate(serial, toType);
    fromDate = toJavaDate(serial, null);
//    final GregorianCalendar gc = new GregorianCalendar();
//    gc.setTime(fromDate);
//    gc.set(GregorianCalendar.MILLISECOND, 0);
//    fromDate = gc.getTime();
    if (toType.isFlagSet(Type.TIME_TYPE))
    {
      return new Time(fromDate.getTime());
    }
    else if (toType.isFlagSet(Type.DATE_TYPE))
    {
      return new java.sql.Date(fromDate.getTime());
    }
    else if (toType.isFlagSet(Type.DATETIME_TYPE))
    {
      return new Date(fromDate.getTime());
    }

    return fromDate;
  }

  public static Number normalizeDate(Number fromSerialDate, Type toType)
  {
    if (fromSerialDate == null || toType == null)
    {
      throw new IllegalArgumentException();
    }

    final BigDecimal o = new BigDecimal(fromSerialDate.doubleValue()).setScale(5, BigDecimal.ROUND_UP);

    if (toType.isFlagSet(Type.TIME_TYPE))
    {
      return o.subtract(new BigDecimal(o.intValue()));
      // only return the decimal part
//      final Double d = new Double(fromSerialDate.doubleValue()
//          - fromSerialDate.intValue());
//      return d;
    }
    else if (toType.isFlagSet(Type.DATE_TYPE))
    {
      return new Integer(fromSerialDate.intValue());
    }
    //datetime (java.util.Date)
    else
    {
      return o;
    }
  }

  public static Date toJavaDate(Number serialDate, LocalizationContext context)
  {
    final Date javaDate = HSSFDateUtil.getJavaDate(serialDate.doubleValue());
    //check for null (error)
    final long l = (javaDate.getTime()/1000)*1000;
    // final GregorianCalendar gc = new GregorianCalendar(context.getTimeZone(),
    // context.getLocale());
    // gc.setTimeInMillis(serialDate.longValue() * MILLISECS_PER_DAY);
    // return gc.getTime();
    return new Date(l);
  }

  public static Number toSerialDate(Date date, LocalizationContext context)
  {
    // final GregorianCalendar gc = new GregorianCalendar(context.getTimeZone(),
    // context.getLocale());
    // gc.setTime(date);
    // final double fraction = (((gc.get(Calendar.HOUR_OF_DAY) * 60 + gc
    // .get(Calendar.MINUTE)) * 60 + gc.get(Calendar.SECOND)) * 1000 + gc
    // .get(Calendar.MILLISECOND))
    // / (double) MILLISECS_PER_DAY;
    // final long timeInMillis = date.getTime();
    // final long days = timeInMillis / MILLISECS_PER_DAY;
    // return new BigDecimal((double) (days) + fraction);
    final double serial = HSSFDateUtil.getExcelDate(date);
    return new Double(serial);
  }

  public static void main(String[] args)
  {
    final DefaultLocalizationContext context = new DefaultLocalizationContext();
    final java.sql.Date createDate = createDate(2006, 05, 01, context);
    final Number serial = toSerialDate(createDate, context);
    System.out.println(createDate);
    System.out.println(serial);
    final Date toJavaDate = toJavaDate(serial, context);
    System.out.println(normalizeDate(toJavaDate, DateType.TYPE));
    System.out.println(toJavaDate);
    System.out.println(HSSFDateUtil.getJavaDate(serial.doubleValue()));
  }
  
  public static Date now(LocalizationContext context)
  {
    final GregorianCalendar gc = new GregorianCalendar(context.getTimeZone(),
        context.getLocale());
    gc.set(Calendar.MILLISECOND, 0);
    
    return gc.getTime();
  }

  public static Date createDateTime(int year, int month, int day, int hour,
      int minute, int second, LocalizationContext context)
  {
    final GregorianCalendar gc = new GregorianCalendar(context.getTimeZone(),
        context.getLocale());
    gc.set(Calendar.DAY_OF_MONTH, day);
    gc.set(Calendar.MONTH, month);
    gc.set(Calendar.YEAR, year);
    gc.set(Calendar.MILLISECOND, 0);
    gc.set(Calendar.HOUR_OF_DAY, hour);
    gc.set(Calendar.MINUTE, minute);
    gc.set(Calendar.SECOND, second);
    return gc.getTime();
  }

  public static Time createTime(int hour, int minute, int second,
      LocalizationContext context)
  {
    final GregorianCalendar gc = new GregorianCalendar(context.getTimeZone(),
        context.getLocale());
    gc.setTime(ISO8001_TIME);
    gc.set(Calendar.MILLISECOND, 0);
    gc.set(Calendar.HOUR_OF_DAY, hour);
    gc.set(Calendar.MINUTE, minute);
    gc.set(Calendar.SECOND, second);
    return new Time(gc.getTime().getTime());
  }

  public static java.sql.Date createDate(int year, int month, int day,
      LocalizationContext context)
  {
    final GregorianCalendar gc = new GregorianCalendar(context.getTimeZone(),
        context.getLocale());
    gc.set(Calendar.DAY_OF_MONTH, day);
    gc.set(Calendar.MONTH, month - 1);
    gc.set(Calendar.YEAR, year);
    gc.set(Calendar.MILLISECOND, 0);
    gc.set(Calendar.HOUR_OF_DAY, 0);
    gc.set(Calendar.MINUTE, 0);
    gc.set(Calendar.SECOND, 0);
    return new java.sql.Date(gc.getTime().getTime());
  }

}
