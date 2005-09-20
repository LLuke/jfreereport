/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2002, by Object Refinery Limited and Contributors.
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
 * ---------------------------
 * SimpleDateFormatParser.java
 * ---------------------------
 * (C)opyright 2000-2002, by Object Refinery Limited.
 *
 * $Id: SimpleDateFormatParser.java,v 1.6 2005/09/07 14:25:10 taqua Exp $
 *
 * Changes
 * -------
 */
package org.jfree.report.filter;

import java.text.DateFormatSymbols;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Locale;

import org.jfree.report.ReportDefinition;

/**
 * Parses a String into a java.util.Date. The string is read from the given datasource and
 * then parsed by the dateformat contained in this FormatParser.
 * <p/>
 * <p/>
 * If the object read from the datasource is no string, the value is converted to string
 * using String.valueOf (Object)
 * <p/>
 * This implementation uses a SimpleDateFormat and grants more control over the parsing
 * results.
 *
 * @author Thomas Morgner
 * @see java.text.SimpleDateFormat
 */
public class SimpleDateFormatParser extends DateFormatParser implements ReportConnectable
{
  /** The report definition registered to this connectable. */
  private ReportDefinition reportDefinition;
  /** The last locale used to convert numbers. */
  private Locale lastLocale;
  /** A flag indicating whether this filter should try to detect locales changes. */
  private boolean keepState;

  /**
   * DefaultConstructor.
   */
  public SimpleDateFormatParser ()
  {
    setFormatter(new SimpleDateFormat());
  }


  /**
   * Returns the SimpleDateFormat object used in this parser.
   *
   * @return The date format object.
   */
  public SimpleDateFormat getSimpleDateFormat ()
  {
    return (SimpleDateFormat) getFormatter();
  }

  /**
   * Sets the date format for the parser.
   *
   * @param format The format.
   * @throws NullPointerException if the format given is null
   */
  public void setSimpleDateFormat (final SimpleDateFormat format)
  {
    super.setFormatter(format);
  }

  /**
   * Sets the date format for the filter. This narrows the allows formats down to
   * SimpleDateFormat.
   *
   * @param format The format.
   * @throws NullPointerException if the format given is null
   * @throws ClassCastException   if the format given is no DateFormat
   */
  public void setFormatter (final Format format)
  {
    final SimpleDateFormat sdfmt = (SimpleDateFormat) format;
    super.setFormatter(sdfmt);
  }

  /**
   * Returns the formatString for this SimpleDateFormat. For a more detailed explaination
   * of SimpleDateFormat formatstrings see java.text.SimpleDateFormat.
   *
   * @return the formatstring used for this DateFormat.
   *
   * @see java.text.SimpleDateFormat
   */
  public String getFormatString ()
  {
    return getSimpleDateFormat().toPattern();
  }

  /**
   * defines the formatString for this SimpleDateFormat.
   *
   * @param format the formatString
   * @throws IllegalArgumentException if the string is invalid
   */
  public void setFormatString (final String format)
  {
    getSimpleDateFormat().applyPattern(format);
  }

  /**
   * Returns a localized formatString for this SimpleDateFormat. For a more detailed
   * explaination of SimpleDateFormat formatstrings see java.text.SimpleDateFormat.
   *
   * @return the localized format string.
   *
   * @see java.text.SimpleDateFormat
   */
  public String getLocalizedFormatString ()
  {
    return getSimpleDateFormat().toLocalizedPattern();
  }


  /**
   * defines the localized formatString for this SimpleDateFormat.
   *
   * @param format the formatString
   * @throws IllegalArgumentException if the string is invalid
   */
  public void setLocalizedFormatString (final String format)
  {
    getSimpleDateFormat().applyLocalizedPattern(format);
  }


  /**
   * Defines, whether the filter should keep its state, if a locale
   * change is detected. This will effectivly disable the locale update.
   *
   * @return true, if the locale should not update the DateSymbols, false otherwise.
   */
  public boolean isKeepState ()
  {
    return keepState;
  }

  /**
   * Defines, whether the filter should keep its state, if a locale
   * change is detected. This will effectivly disable the locale update.
   *
   * @param keepState set to true, if the locale should not update the DateSymbols, false otherwise.
   */
  public void setKeepState (final boolean keepState)
  {
    this.keepState = keepState;
  }

  /**
   * Returns the formatted string. The value is read using the data source given and
   * formated using the formatter of this object. The formating is guaranteed to completly
   * form the object to an string or to return the defined NullValue.
   * <p/>
   * If format, datasource or object are null, the NullValue is returned.
   *
   * @return The formatted value.
   */
  public Object getValue ()
  {
    if (keepState == false && reportDefinition != null)
    {
      final Locale locale = reportDefinition.getResourceBundleFactory().getLocale();
      if (locale != lastLocale)
      {
        lastLocale = locale;
        getSimpleDateFormat().setDateFormatSymbols(new DateFormatSymbols(locale));
      }
    }
    return super.getValue();
  }

  public void registerReportDefinition (final ReportDefinition reportDefinition)
  {
    if (this.reportDefinition != null)
    {
      throw new IllegalStateException("Already connected.");
    }
    if (reportDefinition == null)
    {
      throw new NullPointerException("The given report definition is null");
    }
    this.reportDefinition = reportDefinition;
  }

  public void unregisterReportDefinition (final ReportDefinition reportDefinition)
  {
    if (this.reportDefinition != reportDefinition)
    {
      throw new IllegalStateException("This report definition is not registered.");
    }
    this.reportDefinition = null;
  }
}
