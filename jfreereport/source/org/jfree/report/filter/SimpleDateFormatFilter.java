/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2003, by Simba Management Limited and Contributors.
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
 * SimpleDateFormatFilter.java
 * ---------------------------
 * (C)opyright 2000-2003, by Simba Management Limited.
 *
 * $Id: SimpleDateFormatFilter.java,v 1.1 2003/07/07 22:44:04 taqua Exp $
 *
 * Changes
 * -------
 */
package org.jfree.report.filter;

import java.text.Format;
import java.text.SimpleDateFormat;

/**
 * A filter that creates string from dates. This filter will format java.util.
 * Date objects using a java.text.SimpleDateFormat to create the string representation for
 * the date obtained from the datasource.
 * <p>
 * If the object read from the datasource is no date, the NullValue defined by
 * setNullValue(Object) is returned.
 * <p>
 * This implementation uses a SimpleDateFormat and grants more control over the parsing
 * results.
 *
 * @see java.text.SimpleDateFormat
 *
 * @author Thomas Morgner
 */
public class SimpleDateFormatFilter extends DateFormatFilter
{
  /**
   * DefaultConstructor.
   */
  public SimpleDateFormatFilter()
  {
    setFormatter(new SimpleDateFormat());
  }

  /**
   * Returns the SimpleDateFormat object used in this filter.
   *
   * @return The date format object.
   */
  public SimpleDateFormat getSimpleDateFormat()
  {
    return (SimpleDateFormat) getFormatter();
  }

  /**
   * Sets the date format for the filter.
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
   * @return the format string for the used DateFormat.
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
    if (format == null)
    {
      throw new NullPointerException();
    }
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
