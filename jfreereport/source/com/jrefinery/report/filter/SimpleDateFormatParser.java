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
 * ---------------------------
 * SimpleDateFormatParser.java
 * ---------------------------
 * (C)opyright 2000-2002, by Simba Management Limited.
 *
 * $Id: SimpleDateFormatParser.java,v 1.10 2003/06/27 14:25:17 taqua Exp $
 *
 * Changes
 * -------
 */
package com.jrefinery.report.filter;

import java.text.Format;
import java.text.SimpleDateFormat;

/**
 * Parses a String into a java.util.Date. The string is read from the given datasource
 * and then parsed by the dateformat contained in this FormatParser.
 * <p>
 * <p>
 * If the object read from the datasource is no string, the value is converted to string
 * using String.valueOf (Object)
 * <p>
 * This implementation uses a SimpleDateFormat and grants more control over the parsing
 * results.
 *
 * @see java.text.SimpleDateFormat
 *
 * @author Thomas Morgner
 */
public class SimpleDateFormatParser extends DateFormatParser
{
  /**
   * DefaultConstructor.
   */
  public SimpleDateFormatParser()
  {
    setFormatter(new SimpleDateFormat());
  }


  /**
   * Returns the SimpleDateFormat object used in this parser.
   *
   * @return The date format object.
   */
  public SimpleDateFormat getSimpleDateFormat()
  {
    return (SimpleDateFormat) getFormatter();
  }

  /**
   * Sets the date format for the parser.
   *
   * @param format The format.
   * @throws NullPointerException if the format given is null
   */
  public void setSimpleDateFormat(final SimpleDateFormat format)
  {
    super.setFormatter(format);
  }

  /**
   * Sets the date format for the filter. This narrows the allows formats down to
   * SimpleDateFormat.
   *
   * @param format The format.
   * @throws NullPointerException if the format given is null
   * @throws ClassCastException if the format given is no DateFormat
   */
  public void setFormatter(final Format format)
  {
    final SimpleDateFormat sdfmt = (SimpleDateFormat) format;
    super.setFormatter(sdfmt);
  }

  /**
   * Returns the formatString for this SimpleDateFormat. For a more detailed explaination
   * of SimpleDateFormat formatstrings see java.text.SimpleDateFormat.
   *
   * @see java.text.SimpleDateFormat
   * @return the formatstring used for this DateFormat.
   */
  public String getFormatString()
  {
    return getSimpleDateFormat().toPattern();
  }

  /**
   * defines the formatString for this SimpleDateFormat.
   *
   * @param format the formatString
   * @throws IllegalArgumentException if the string is invalid
   */
  public void setFormatString(final String format)
  {
    getSimpleDateFormat().applyPattern(format);
  }

  /**
   * Returns a localized formatString for this SimpleDateFormat. For a more detailed explaination
   * of SimpleDateFormat formatstrings see java.text.SimpleDateFormat.
   *
   * @see java.text.SimpleDateFormat
   * @return the localized format string.
   */
  public String getLocalizedFormatString()
  {
    return getSimpleDateFormat().toLocalizedPattern();
  }


  /**
   * defines the localized formatString for this SimpleDateFormat.
   *
   * @param format the formatString
   * @throws IllegalArgumentException if the string is invalid
   */
  public void setLocalizedFormatString(final String format)
  {
    getSimpleDateFormat().applyLocalizedPattern(format);
  }

}
