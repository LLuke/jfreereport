/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
 *
 * (C) Copyright 2000-2004, by Simba Management Limited and Contributors.
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
 * HtmlTableCellStyle.java
 * ------------------------------
 * (C)opyright 2004, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id$
 *
 * Changes 
 * -------------------------
 * 09.03.2004 : Initial version
 *  
 */

package org.jfree.report.modules.output.table.html;

import org.jfree.report.modules.output.table.base.TableCellBackground;
import java.awt.Color;

public class HtmlTableCellStyle implements HtmlStyle
{
  private TableCellBackground background;
  private String name;
  private int width;

  public HtmlTableCellStyle (final TableCellBackground background,
                             final int width)
  {
    this.background = background;
    this.width = width;
  }

  /**
   * Transforms the TableCellBackground into a Cascading StyleSheet definition.
   *
   * @return the generated stylesheet definition.
   */
  public String getCSSString ()
  {
    final StringBuffer b = new StringBuffer();
    b.append("width: ");
    b.append(width);
    b.append("pt");

    if (background == null)
    {
      return b.toString();
    }

    b.append(";");

    final Color c = background.getColor();
    if (c != null)
    {
      b.append("background-color:");
      b.append(HtmlStyleCollection.getColorString(c));
    }

    if (background.getColorTop() != null)
    {
      if (b.length() != 0)
      {
        b.append(";");
      }
      b.append("border-top: ");
      b.append(background.getBorderSizeTop());
      b.append("pt; border-top-style: solid; border-top-color: ");
      b.append(HtmlStyleCollection.getColorString(background.getColorTop()));
    }

    if (background.getColorBottom() != null)
    {
      if (b.length() != 0)
      {
        b.append(";");
      }
      b.append("border-bottom: ");
      b.append(background.getBorderSizeBottom());
      b.append("pt; border-bottom-style: solid; border-bottom-color: ");
      b.append(HtmlStyleCollection.getColorString(background.getColorBottom()));
    }

    if (background.getColorLeft() != null)
    {
      if (b.length() != 0)
      {
        b.append(";");
      }
      b.append("border-left: ");
      b.append(background.getBorderSizeLeft());
      b.append("pt; border-left-style: solid; border-left-color: ");
      b.append(HtmlStyleCollection.getColorString(background.getColorLeft()));
    }

    if (background.getColorRight() != null)
    {
      if (b.length() != 0)
      {
        b.append(";");
      }
      b.append("border-right: ");
      b.append(background.getBorderSizeRight());
      b.append("pt; border-right-style: solid; border-right-color: ");
      b.append(HtmlStyleCollection.getColorString(background.getColorRight()));
    }

    return b.toString();
  }

  public boolean equals (final Object o)
  {
    if (this == o)
    {
      return true;
    }
    if (!(o instanceof HtmlTableCellStyle))
    {
      return false;
    }

    final HtmlTableCellStyle htmlTableCellStyle = (HtmlTableCellStyle) o;

    if (background != null ? !background.equals(htmlTableCellStyle.background) : htmlTableCellStyle.background != null)
    {
      return false;
    }

    return true;
  }

  public int hashCode ()
  {
    return (background != null ? background.hashCode() : 0);
  }

  public String getName ()
  {
    return name;
  }

  public void setName (final String name)
  {
    this.name = name;
  }
}
