/**
 * =============================================================
 * JFreeReport : an open source reporting class library for Java
 * =============================================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
 * Project Lead:  David Gilbert (david.gilbert@object-refinery.com)
 *
 * (C) Copyright 2000-2002, by Simba Management Limited and Contributors.
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
 * -----------------------
 * DecimalFormatFilter.java
 * -----------------------
 * (C)opyright 2000-2002, by Simba Management Limited.
 *
 * ChangeLog
 * ---------
 * 21-May-2002 : Initial version
 * 06-Jun-2002 : Documentation updated
 * 08-Aug-2002 : Removed unused Imports
 */
package com.jrefinery.report.filter;

import java.text.DecimalFormat;
import java.text.Format;

/**
 * A filter that formats the numeric value from a data source to a string representation
 * using the decimal number system as base.
 *
 * This filter will format java.lang.Number objects using a
 * java.text.DecimalFormat to create the string representation for the date obtained
 * from the datasource.
 * <p>
 * If the object read from the datasource is no date, the NullValue defined by
 * setNullValue(Object) is returned.
 *
 * @see java.text.NumberFormat
 * @see java.lang.Number
 */
public class DecimalFormatFilter extends NumberFormatFilter
{
  /**
   * DefaultConstructor, this object is initialized using a DecimalFormat with the
   * default pattern for this locale.
   */
  public DecimalFormatFilter ()
  {
    setFormatter (new DecimalFormat ());
  }

  /**
   * returns the format for the filter. The DecimalFormatParser has only DecimalFormat objects
   * assigned.
   *
   * @param format The format.
   * @throws NullPointerException if the given format is null
   */
  public DecimalFormat getDecimalFormat ()
  {
    return (DecimalFormat) getFormatter ();
  }

  /**
   * Sets the format for the filter.
   *
   * @param format The format.
   * @throws NullPointerException if the given format is null
   */
  public void setDecimalFormat (DecimalFormat format)
  {
    setFormatter (format);
  }

  /**
   * Sets the format for the filter. If the given format is no Decimal format, a
   * ClassCastException is thrown
   *
   * @param format The format.
   * @throws NullPointerException if the given format is null
   * @throws ClassCastException if the format is no decimal format
   */
  public void setFormatter (Format format)
  {
    super.setFormatter ((DecimalFormat) format);
  }

  /**
   * Synthesizes a pattern string that represents the current state
   * of this Format object.
   */
  public String getFormatString ()
  {
    return getDecimalFormat ().toPattern ();
  }

  /**
   * Copied from java.text.DecimalFormat:
   * <p>
   * Apply the given pattern to this Format object. A pattern is a short-hand
   * specification for the various formatting properties. These properties can also be
   * changed individually through the various setter methods.
   * <p>
   * There is no limit to integer digits are set by this routine, since that is the
   * typical end-user desire; use setMaximumInteger if you want to set a real value.
   * For negative numbers, use a second pattern, separated by a semicolon
   *
   * @param format the format string
   */
  public void setFormatString (String format)
  {
    getDecimalFormat ().applyPattern (format);
  }

  /**
   * Synthesizes a localized pattern string that represents the current state
   * of this Format object.
   */
  public String getLocalizedFormatString ()
  {
    return getDecimalFormat ().toLocalizedPattern ();
  }

  /**
   * Copied from java.text.DecimalFormat:
   * <p>
   * Apply the given pattern to this Format object. The pattern is assumed to be in a
   * localized notation. A pattern is a short-hand specification for the various formatting
   * properties. These properties can also be changed individually through the various
   * setter methods.
   * <p>
   * There is no limit to integer digits are set by this routine, since that is the
   * typical end-user desire; use setMaximumInteger if you want to set a real value.
   * For negative numbers, use a second pattern, separated by a semicolon
   *
   * @param the format pattern for this format
   */
  public void setLocalizedFormatString (String format)
  {
    getDecimalFormat ().applyLocalizedPattern (format);
  }
}
