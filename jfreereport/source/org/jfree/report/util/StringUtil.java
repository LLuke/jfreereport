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
 * ---------------
 * StringUtil.java
 * ---------------
 * (C)opyright 2002, 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: StringUtil.java,v 1.6 2003/08/31 19:27:59 taqua Exp $
 *
 * Changes
 * -------
 * 10-Dec-2002 : Fixed issues reported by Checkstyle (DG);
 * 25-Jan-2003 : Added URL-Encoding and CSS-Encoding methods to support HTML-Output
 */

package org.jfree.report.util;

/**
 * String utility functions.
 *
 * @author Thomas Morgner
 */
public final class StringUtil
{
  /**
   * Default Constructor.
   */
  private StringUtil()
  {
  }

  /**
   * Helper functions to query a strings start portion. The comparison is case insensitive.
   *
   * @param base  the base string.
   * @param start  the starting text.
   *
   * @return true, if the string starts with the given starting text.
   */
  public static boolean startsWithIgnoreCase(final String base, final String start)
  {
    if (base.length() < start.length())
    {
      return false;
    }
    return base.regionMatches(true, 0, start, 0, start.length());
  }

  /**
   * Helper functions to query a strings end portion. The comparison is case insensitive.
   *
   * @param base  the base string.
   * @param end  the ending text.
   *
   * @return true, if the string ends with the given ending text.
   */
  public static boolean endsWithIgnoreCase(final String base, final String end)
  {
    if (base.length() < end.length())
    {
      return false;
    }
    return base.regionMatches(true, base.length() - end.length(), end, 0, end.length());
  }

  /**
   * Parses the given string and returns the parsed integer value or
   * the given default if the parsing failed.
   *
   * @param value the to be parsed string
   * @param defaultValue the default value
   * @return the parsed string.
   */
  public static int parseInt (String value, int defaultValue)
  {
    if (value == null)
    {
      return defaultValue;
    }
    try
    {
      return Integer.parseInt(value);
    }
    catch (Exception e)
    {
      return defaultValue;
    }
  }

  /**
   * Parses the given string into a boolean value. This returns true, if
   * the string's value is "true".
   * 
   * @param attribute the string that should be parsed.
   * @param defaultValue the default value, in case the string is null.
   * @return the parsed value.
   */
  public static boolean parseBoolean (String attribute, boolean defaultValue)
  {
    if (attribute == null)
    {
      return defaultValue;
    }
    if (attribute.equals("true"))
    {
      return true;
    }
    return false;
  }



}
