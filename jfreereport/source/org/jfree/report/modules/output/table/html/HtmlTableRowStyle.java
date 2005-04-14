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
 * $Id: HtmlTableRowStyle.java,v 1.5 2005/04/14 16:37:36 taqua Exp $
 *
 * Changes 
 * -------------------------
 * 09.03.2004 : Initial version
 *  
 */

package org.jfree.report.modules.output.table.html;

import java.awt.Color;

public class HtmlTableRowStyle implements HtmlStyle
{
  private int height;
  private Color background;
  private Color colorTop;
  private Color colorBottom;
  private float borderSizeTop;
  private float borderSizeBottom;

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
    if (colorTop != null)
    {
      styleBuilder.append("border-top", String.valueOf(borderSizeTop), "pt");
      styleBuilder.append("border-top-style", "solid");
      styleBuilder.append("border-top-color", HtmlStyleCollection.getColorString(getColorTop()));
    }

    if (getColorBottom() != null)
    {
      styleBuilder.append("border-bottom", String.valueOf(getBorderSizeBottom()), "pt");
      styleBuilder.append("border-bottom-style", "solid");
      styleBuilder.append("border-bottom-color", HtmlStyleCollection.getColorString(getColorBottom()));
    }

    return styleBuilder.toString();
  }

  public void setBorderTop (final Color top, final float size)
  {
    this.colorTop = top;
    this.borderSizeTop = size;
  }

  public void setBorderBottom (final Color bottom, final float size)
  {
    this.colorBottom = bottom;
    this.borderSizeBottom = size;
  }

  public float getBorderSizeBottom ()
  {
    return borderSizeBottom;
  }

  public float getBorderSizeTop ()
  {
    return borderSizeTop;
  }

  public Color getColorBottom ()
  {
    return colorBottom;
  }

  public Color getColorTop ()
  {
    return colorTop;
  }

  public int getHeight ()
  {
    return height;
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

    if (borderSizeBottom != htmlTableRowStyle.borderSizeBottom)
    {
      return false;
    }
    if (borderSizeTop != htmlTableRowStyle.borderSizeTop)
    {
      return false;
    }
    if (height != htmlTableRowStyle.height)
    {
      return false;
    }
    if (background != null ? !background.equals(htmlTableRowStyle.background) : htmlTableRowStyle.background != null)
    {
      return false;
    }
    if (colorBottom != null ? !colorBottom.equals(htmlTableRowStyle.colorBottom) : htmlTableRowStyle.colorBottom != null)
    {
      return false;
    }
    if (colorTop != null ? !colorTop.equals(htmlTableRowStyle.colorTop) : htmlTableRowStyle.colorTop != null)
    {
      return false;
    }

    return true;
  }

  public int hashCode ()
  {
    int result;
    result = height;
    result = 29 * result + (background != null ? background.hashCode() : 0);
    result = 29 * result + (colorTop != null ? colorTop.hashCode() : 0);
    result = 29 * result + (colorBottom != null ? colorBottom.hashCode() : 0);
    result = 29 * result + borderSizeTop != +0.0f ? Float.floatToIntBits(borderSizeTop) : 0;
    result = 29 * result + borderSizeBottom != +0.0f ? Float.floatToIntBits(borderSizeBottom) : 0;
    return result;
  }
}
