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
 * ------------------------
 * DecimalFormatFilter.java
 * ------------------------
 * (C)opyright 2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Object Refinery Limited);
 *
 * $Id: DecimalFormatFilter.java,v 1.7 2005/09/07 14:25:10 taqua Exp $
 *
 * ChangeLog
 * ---------
 * 21-May-2002 : Initial version
 * 06-Jun-2002 : Documentation updated
 * 08-Aug-2002 : Removed unused imports
 * 10-Dec-2002 : Updated Javadocs (DG);
 *
 */

package org.jfree.report.filter;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.Format;
import java.util.Locale;

import org.jfree.report.ReportDefinition;

/**
 * A filter that formats the numeric value from a data source to a string representation
 * using the decimal number system as base.
 * <p/>
 * This filter will format java.lang.Number objects using a java.text.DecimalFormat to
 * create the string representation for the date obtained from the datasource.
 * <p/>
 * If the object read from the datasource is no date, the NullValue defined by
 * setNullValue(Object) is returned.
 *
 * @author Thomas Morgner
 * @see java.text.NumberFormat
 * @see java.lang.Number
 */
public class DecimalFormatFilter extends NumberFormatFilter
{
  /** The report definition registered to this connectable. */
  private ReportDefinition reportDefinition;
  /** The last locale used to convert numbers. */
  private Locale lastLocale;
  /** A flag indicating whether this filter should try to detect locales changes. */
  private boolean keepState;

  /**
   * DefaultConstructor, this object is initialized using a DecimalFormat with the default
   * pattern for this locale.
   */
  public DecimalFormatFilter ()
  {
    setFormatter(new DecimalFormat());
  }

  /**
   * Returns the format for the filter. The DecimalFormatParser has only DecimalFormat
   * objects assigned.
   *
   * @return the formatter.
   *
   * @throws NullPointerException if the given format is null
   */
  public DecimalFormat getDecimalFormat ()
  {
    return (DecimalFormat) getFormatter();
  }

  /**
   * Sets the format for the filter.
   *
   * @param format the format.
   * @throws NullPointerException if the given format is null
   */
  public void setDecimalFormat (final DecimalFormat format)
  {
    setFormatter(format);
  }

  /**
   * Sets the format for the filter. If the given format is no Decimal format, a
   * ClassCastException is thrown
   *
   * @param format the format.
   * @throws NullPointerException if the given format is null
   * @throws ClassCastException   if the format is no decimal format
   */
  public void setFormatter (final Format format)
  {
    final DecimalFormat dfmt = (DecimalFormat) format;
    super.setFormatter(dfmt);
  }

  /**
   * Synthesizes a pattern string that represents the current state of this Format
   * object.
   *
   * @return the pattern string of the format object contained in this filter.
   */
  public String getFormatString ()
  {
    return getDecimalFormat().toPattern();
  }

  /**
   * Applies a format string to the internal <code>DecimalFormat</code> instance.
   *
   * @param format the format string.
   */
  public void setFormatString (final String format)
  {
    getDecimalFormat().applyPattern(format);
  }

  /**
   * Synthesizes a localized pattern string that represents the current state of this
   * Format object.
   *
   * @return the localized pattern string of the format-object.
   */
  public String getLocalizedFormatString ()
  {
    return getDecimalFormat().toLocalizedPattern();
  }

  /**
   * Applies a localised format string to the internal <code>DecimalFormat</code>
   * instance.
   *
   * @param format the format string.
   */
  public void setLocalizedFormatString (final String format)
  {
    getDecimalFormat().applyLocalizedPattern(format);
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
        getDecimalFormat().setDecimalFormatSymbols(new DecimalFormatSymbols(locale));
      }
    }
    return super.getValue();
  }

  /**
   * Connects the connectable to the given report definition.
   *
   * @param reportDefinition the reportDefinition for this report connectable.
   * @throws IllegalStateException if this instance is already connected to a
   * report definition.
   */
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

  /**
   * Disconnects the connectable from the given report definition.
   *
   * @param reportDefinition the reportDefinition for this report connectable.
   * @throws IllegalStateException if this instance is already connected to a
   * report definition.
   */
  public void unregisterReportDefinition (final ReportDefinition reportDefinition)
  {
    if (this.reportDefinition != reportDefinition)
    {
      throw new IllegalStateException("This report definition is not registered.");
    }
    this.reportDefinition = null;
  }
}
