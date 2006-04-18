/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2005, by Object Refinery Limited and Contributors.
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
 * ------------
 * ConvertToNumberExpression.java
 * ------------
 * (C) Copyright 2002-2005, by Object Refinery Limited.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: ConvertToNumberExpression.java,v 1.5 2006/01/24 18:58:29 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.report.function.numeric;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.Locale;

import org.jfree.report.DataRow;
import org.jfree.report.DataSourceException;
import org.jfree.report.function.AbstractExpression;
import org.jfree.report.function.ExpressionDependencyInfo;

public class ConvertToNumberExpression extends AbstractExpression
{
  private static final String DECIMALFORMAT_DEFAULT_PATTERN =
    "#,###.###################################################" +
    "#########################################################" +
    "#########################################################" +
    "#########################################################" +
    "#########################################################" +
    "#########################################################" +
    "####";

  private static final BigDecimal ZERO = new BigDecimal(0);
  private String field;
  private String format;
  private Locale locale;

  public ConvertToNumberExpression ()
  {
  }

  public String getField ()
  {
    return field;
  }

  public void setField (final String field)
  {
    this.field = field;
  }

  public String getFormat ()
  {
    return format;
  }

  public void setFormat (final String format)
  {
    this.format = format;
  }

  public Locale getLocale()
  {
    return locale;
  }

  public void setLocale(final Locale locale)
  {
    this.locale = locale;
  }

  /**
   * Return the current expression value. <P> The value depends (obviously) on the
   * expression implementation.
   *
   * @return the value of the function.
   */
  public Object getValue () throws DataSourceException
  {
    final DataRow dataRow = getDataRow();
    // get the row directly as a Number
    final Object o = dataRow.get(field);
    // check if that thing is a Number
    if (o instanceof Number)
    {
      return o;
    }

    // get a string and convert
    final String formatString = getFormat();
    try
    {
      final String effectiveFormatString;
      if (formatString == null || formatString.length() == 0)
      {
        // this is a workaround for a bug in JDK 1.5
        effectiveFormatString = DECIMALFORMAT_DEFAULT_PATTERN;
      }
      else
      {
        effectiveFormatString = formatString;
      }


      final DecimalFormat format = new DecimalFormat(effectiveFormatString);
      if (locale != null)
      {
        format.setDecimalFormatSymbols
              (new DecimalFormatSymbols(getLocale()));
      }
      else
      {
        format.setDecimalFormatSymbols
              (new DecimalFormatSymbols(getParentLocale()));
      }
      return format.parse(String.valueOf(o));
    }
    catch (ParseException e)
    {
      return ZERO;
    }
  }

  public void queryDependencyInfo(final ExpressionDependencyInfo info)
  {
    super.queryDependencyInfo(info);
    info.setDependendFields(new String[] { getField()});
  }
  
}