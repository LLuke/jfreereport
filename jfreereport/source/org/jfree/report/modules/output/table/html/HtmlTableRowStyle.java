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
 * $Id: HtmlTableRowStyle.java,v 1.7 2005/05/31 20:37:25 taqua Exp $
 *
 * Changes 
 * -------------------------
 * 09.03.2004 : Initial version
 *  
 */

package org.jfree.report.modules.output.table.html;

import java.awt.Color;
import java.awt.Stroke;

import org.jfree.util.ObjectUtilities;
import org.jfree.report.util.StrokeUtility;

public class HtmlTableRowStyle implements HtmlStyle
{
  private int height;
  private Color background;
  private Color colorTop;
  private Color colorBottom;
  private Stroke borderStrokeTop;
  private Stroke borderStrokeBottom;
  private boolean tableRowBorderDefinition;

  public HtmlTableRowStyle (final int height,
                            final Color background,
                            final boolean tableRowBorderDefinition)
  {
    this.height = height;
    this.background = background;
    this.tableRowBorderDefinition = tableRowBorderDefinition;
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
    if (tableRowBorderDefinition)
    {
      if (colorTop != null)
      {
        styleBuilder.append("border-top-width",
                String.valueOf(StrokeUtility.getStrokeWidth(borderStrokeTop)),
                "pt");
        styleBuilder.append("border-top-style",
                HtmlTableCellStyle.translateStrokeStyle(borderStrokeTop));
        styleBuilder.append("border-top-color", HtmlStyleCollection.getColorString(getColorTop()));
      }

      if (getColorBottom() != null)
      {
        styleBuilder.append("border-top-width",
                String.valueOf(StrokeUtility.getStrokeWidth(borderStrokeBottom)),
                "pt");
        styleBuilder.append("border-top-style",
                HtmlTableCellStyle.translateStrokeStyle(borderStrokeBottom));
        styleBuilder.append("border-bottom-color", HtmlStyleCollection.getColorString(getColorBottom()));
      }
    }
    return styleBuilder.toString();
  }

  public void setBorderTop (final Color top, final Stroke size)
  {
    this.colorTop = top;
    this.borderStrokeTop = size;
  }

  public void setBorderBottom (final Color bottom, final Stroke size)
  {
    this.colorBottom = bottom;
    this.borderStrokeBottom = size;
  }

  public Stroke getBorderStrokeTop()
  {
    return borderStrokeTop;
  }

  public Stroke getBorderStrokeBottom()
  {
    return borderStrokeBottom;
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

    if (tableRowBorderDefinition)
    {
      // comparison should be against the *mapped* values ...
      if (ObjectUtilities.equal(borderStrokeTop, htmlTableRowStyle.borderStrokeTop) == false)
      {
        return false;
      }
      if (ObjectUtilities.equal(borderStrokeBottom, htmlTableRowStyle.borderStrokeBottom) == false)
      {
        return false;
      }
    }
    if (height != htmlTableRowStyle.height)
    {
      return false;
    }
    if (background != null ? !background.equals(htmlTableRowStyle.background) : htmlTableRowStyle.background != null)
    {
      return false;
    }
    if (tableRowBorderDefinition)
    {
      if (colorBottom != null ? !colorBottom.equals(htmlTableRowStyle.colorBottom) : htmlTableRowStyle.colorBottom != null)
      {
        return false;
      }
      if (colorTop != null ? !colorTop.equals(htmlTableRowStyle.colorTop) : htmlTableRowStyle.colorTop != null)
      {
        return false;
      }
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
    result = 29 * result + (borderStrokeTop != null ? borderStrokeTop.hashCode() : 0);
    result = 29 * result + (borderStrokeBottom != null ? borderStrokeBottom.hashCode() : 0);
    return result;
  }
}
