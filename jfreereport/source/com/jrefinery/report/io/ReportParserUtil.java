/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
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
 * ----------------
 * ReportParserUtil.java
 * ----------------
 * (C)opyright 2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner (taquera@sherito.org);
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: ReportParserUtil.java,v 1.1 2003/04/23 16:26:09 taqua Exp $
 *
 * Changes
 * -------
 * 13.04.2003 : Initial version
 */
package com.jrefinery.report.io;

import com.jrefinery.report.ElementAlignment;
import org.xml.sax.SAXException;

/**
 * A helper class to make parsing the xml files a lot easier.
 */
public class ReportParserUtil
{
  /**
   * Parses a vertical alignment value.
   *
   * @param value  the text to parse.
   * @return the element alignment.
   * @throws SAXException if the alignment value is not recognised.
   */
  public static ElementAlignment parseVerticalElementAlignment(String value) throws SAXException
  {
    if (value.equals("top"))
    {
      return ElementAlignment.TOP;
    }
    if (value.equals("middle"))
    {
      return ElementAlignment.MIDDLE;
    }
    if (value.equals("bottom"))
    {
      return ElementAlignment.BOTTOM;
    }
    throw new SAXException("Invalid vertical alignment");
  }

  /**
   * Parses a horizontal alignment value.
   *
   * @param value  the text to parse.
   * @return the element alignment.
   * @throws SAXException if the alignment value is not recognised.
   */
  public static ElementAlignment parseHorizontalElementAlignment(String value) throws SAXException
  {
    if (value.equals("left"))
    {
      return ElementAlignment.LEFT;
    }
    if (value.equals("center"))
    {
      return ElementAlignment.CENTER;
    }
    if (value.equals("right"))
    {
      return ElementAlignment.RIGHT;
    }
    throw new SAXException("Invalid horizontal alignment");
  }

}
