/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
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
 * ------------------------
 * DecimalFormatParser.java
 * ------------------------
 * (C)opyright 2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: DecimalFormatParser.java,v 1.11 2003/06/27 14:25:17 taqua Exp $
 *
 * ChangeLog
 * ---------
 * 21-May-2002 : Initial version
 * 06-Jun-2002 : Documentation updated
 * 08-Aug-2002 : Removed unused Imports
 * 10-Dec-2002 : Updated Javadocs (DG);
 *
 */

package com.jrefinery.report.filter;

import java.text.DecimalFormat;
import java.text.Format;

/**
 * A filter that parses string values from a data source to a number
 * using the decimal numeric system as base.
 *
 * This filter will parse strings using a java.text.DecimalFormat to create the
 * numeric value for the string from the datasource. If the datasource does not
 * return a string, the required string is formed by applying String.valueOf (Object).
 * <p>
 * If the string read from the datasource could not be parsed into a number, the NullValue
 * defined by setNullValue(Object) is returned.
 *
 * @see java.text.NumberFormat
 * @see java.lang.Number
 *
 * @author Thomas Morgner
 */
public class DecimalFormatParser extends NumberFormatParser
{
  /**
   * DefaultConstructor, this object is initialized using a DecimalFormat with the
   * default pattern for this locale.
   */
  public DecimalFormatParser()
  {
    setFormatter(new DecimalFormat());
  }

  /**
   * Returns the format for the filter. The DecimalFormatParser has only DecimalFormat objects
   * assigned.
   *
   * @return the formatter.
   */
  public DecimalFormat getDecimalFormat()
  {
    return (DecimalFormat) getFormatter();
  }

  /**
   * Sets the format for the filter.
   *
   * @param format  the format.
   *
   * @throws NullPointerException if the given format is null.
   */
  public void setDecimalFormat(final DecimalFormat format)
  {
    setFormatter(format);
  }

  /**
   * Sets the format for the filter. If the given format is no Decimal format, a
   * ClassCastException is thrown
   *
   * @param format The format.
   *
   * @throws NullPointerException if the given format is null
   * @throws ClassCastException if the format is no decimal format
   */
  public void setFormatter(final Format format)
  {
    final DecimalFormat dfmt = (DecimalFormat) format;
    super.setFormatter(dfmt);
  }

  /**
   * Synthesizes a pattern string that represents the current state
   * of this Format object.
   *
   * @return the pattern string of the format object contained in this filter.
   */
  public String getFormatString()
  {
    return getDecimalFormat().toPattern();
  }

  /**
   * Applies a format string to the internal <code>DecimalFormat</code> instance.
   *
   * @param format  the format string.
   */
  public void setFormatString(final String format)
  {
    getDecimalFormat().applyPattern(format);
  }

  /**
   * Synthesizes a localized pattern string that represents the current state
   * of this Format object.
   *
   * @return the localized pattern string of the format-object.
   */
  public String getLocalizedFormatString()
  {
    return getDecimalFormat().toLocalizedPattern();
  }

  /**
   * Applies a localised format string to the internal <code>DecimalFormat</code> instance.
   *
   * @param format  the format string.
   */
  public void setLocalizedFormatString(final String format)
  {
    getDecimalFormat().applyLocalizedPattern(format);
  }

}
