/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
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
 * StyleBuilder.java
 * ------------
 * (C) Copyright 2002-2005, by Object Refinery Limited.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: JCommon.java,v 1.1 2004/07/15 14:49:46 mungady Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.report.modules.output.table.html;

import org.jfree.util.StringUtils;

public class StyleBuilder
{
  private boolean compact;
  private StringBuffer style;
  private final static String INDENT = "    ";

  public StyleBuilder (final boolean compact)
  {
    this.compact = compact;
    this.style = new StringBuffer();
  }

  public void append (final String key, final String value)
  {
    if (compact == false)
    {
      if (style.length() != 0)
      {
        style.append(StringUtils.getLineSeparator());
      }
      style.append(INDENT);
    }

    style.append(key);
    style.append(": ");
    style.append(value);
    style.append(";");
  }

  public void append (final String key, final String value, final String unit)
  {
    if (compact == false)
    {
      if (style.length() != 0)
      {
        style.append(StringUtils.getLineSeparator());
      }
      style.append(INDENT);
    }

    style.append(key);
    style.append(": ");
    style.append(value);
    style.append(unit);
    style.append(";");
  }

  public String toString ()
  {
    return style.toString();
  }
}
