/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
 *
 * (C) Copyright 2000-2004, by Object Refinery Limited and Contributors.
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
 * ------------------------------
 * HtmlTableRowStyle.java
 * ------------------------------
 * (C)opyright 2004, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Object Refinery Limited);
 *
 * $Id: HtmlTableRowStyle.java,v 1.4 2005/02/23 21:05:34 taqua Exp $
 *
 * Changes 
 * -------------------------
 * 09.03.2004 : Initial version
 *  
 */

package org.jfree.report.modules.output.table.html;

import java.awt.Color;

import org.jfree.util.ObjectUtilities;

public class HtmlTableRowStyle implements HtmlStyle
{
  private int height;
  private Color background;

  public HtmlTableRowStyle (final int height, final Color background)
  {
    this.height = height;
    this.background = background;
  }

  public Color getBackground ()
  {
    return background;
  }

  public String getCSSString (final boolean compact)
  {
    final StyleBuilder styleBuilder = new StyleBuilder(compact);
    styleBuilder.append("height", String.valueOf(height), "pt");
    if (background != null)
    {
      styleBuilder.append("background-color", HtmlStyleCollection.getColorString(background));
    }
    return styleBuilder.toString();
  }

  public boolean equals (final Object o)
  {
    if (this == o)
    {
      return true;
    }
    if (!(o instanceof HtmlTableRowStyle))
    {
      return false;
    }

    final HtmlTableRowStyle htmlTableRowStyle = (HtmlTableRowStyle) o;

    if (height != htmlTableRowStyle.height)
    {
      return false;
    }
    if (ObjectUtilities.equal(background, htmlTableRowStyle.background) == false)
    {
      return false;
    }
    return true;
  }

  public int hashCode ()
  {
    int hashCode = height;
    if (background != null)
    {
      hashCode = 23 * hashCode + background.hashCode();
    }
    return hashCode;
  }
}
