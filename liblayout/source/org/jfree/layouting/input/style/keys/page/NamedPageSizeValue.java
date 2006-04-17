/**
 * ===========================================
 * LibLayout : a free Java layouting library
 * ===========================================
 *
 * Project Info:  http://www.jfree.org/liblayout/
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
 * NamedPageSizeValue.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id$
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.layouting.input.style.keys.page;

import java.awt.print.PageFormat;

import org.jfree.layouting.input.style.values.CSSConstant;

/**
 * Creation-Date: 30.11.2005, 18:12:45
 *
 * @author Thomas Morgner
 */
public class NamedPageSizeValue extends PageSizeValue
{
  private CSSConstant name;
  private int orientation;

  public NamedPageSizeValue(final CSSConstant name, final int orientation)
  {
    super(checkPageSize(name, orientation));
    this.name = name;
    this.orientation = orientation;
  }

  private static PageSize checkPageSize
          (final CSSConstant name, final int orientation)
  {
    PageSize ps = PageSizeFactory.getInstance().getPageSizeByName
            (name.getCSSText());
    if (ps == null)
    {
      throw new IllegalArgumentException("This is no valid page size");
    }

    if (orientation == PageFormat.PORTRAIT)
    {
      return ps;
    }
    else if (orientation == PageFormat.LANDSCAPE || orientation == PageFormat.REVERSE_LANDSCAPE)
    {
      // rotated by 90 degrees.
      return new PageSize(ps.getHeight(), ps.getWidth());
    }
    else
    {
      throw new IllegalArgumentException("This is no valid page orientation");
    }
  }

  public CSSConstant getName()
  {
    return name;
  }

  public int getOrientation()
  {
    return orientation;
  }

  public String getCSSText()
  {
    StringBuffer b = new StringBuffer();
    b.append(name);
    if (orientation == PageFormat.LANDSCAPE)
    {
      b.append(" landscape");
    }
    else if (orientation == PageFormat.REVERSE_LANDSCAPE)
    {
      b.append(" reverese-landscape");
    }
    else if (orientation == PageFormat.PORTRAIT)
    {
      b.append(" portrait");
    }
    return b.toString();
  }
}
