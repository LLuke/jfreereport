/**
 * =============================================================
 * JFreeReport : an open source reporting class library for Java
 * =============================================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
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
 * ---------------
 * StringUtil.java
 * ---------------
 * (C)opyright 2000-2002, by Simba Management Limited.
 *
 * $Id: StringUtil.java,v 1.3 2002/11/29 12:07:29 mungady Exp $
 *
 * Changes
 * -------
 */

package com.jrefinery.report.util;

/**
 * String utility functions.
 *
 * @author TM
 */
public class StringUtil
{
  /**
   * Helper functions to query a strings start portion. The comparison is case
   * insensitive.
   *
   * @see String#startsWith
   * @return true, if the string starts with the given startposition.
   */
  public static boolean startsWithIgnoreCase(String base, String start)
  {
    if (base.length() < start.length())
      return false;

    return base.regionMatches(true, 0, start, 0, start.length());
  }

  /**
   * Helper functions to query a strings end portion. The comparison is case
   * insensitive.
   *
   * @see String#endsWith
   * @return true, if the string ends with the given endString.
   */
  public static boolean endsWithIgnoreCase(String base, String end)
  {
    if (base.length() < end.length())
    {
      return false;
    }
    return base.regionMatches(true, base.length() - end.length(), end, 0, end.length());
  }

}
