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
 * HtmlTableCellStyle.java
 * ------------------------------
 * (C)opyright 2004, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Object Refinery Limited);
 *
 * $Id: HtmlTableCellStyle.java,v 1.3 2005/01/25 00:13:44 taqua Exp $
 *
 * Changes 
 * -------------------------
 * 09-Mar-2004 : Initial version
 *  
 */

package org.jfree.report.modules.output.table.html;

import java.awt.Color;

import org.jfree.report.modules.output.table.base.TableCellBackground;

/**
 * Encapsulates a background definition for a &lt;td&gt; element. For layout
 * purposes, the font size is define to be 1pt. The div element included in
 * the table cell will correct that to a reasonable value if needed, and the
 * &amp;nbsp; entity (required to prevent empty cells if not using CSS) will
 * be as small as possible.
 * <p>
 * Using a font-size of zero makes the cell empty and causes problems with
 * the defined borders. 
 *
 * @author Thomas Morgner
 */
public class HtmlTableCellStyle implements HtmlStyle
{
  private TableCellBackground background;

  public HtmlTableCellStyle(final TableCellBackground background)
  {
    this.background = background;
  }

  /**
   * Transforms the TableCellBackground into a Cascading StyleSheet definition.
   *
   * @return the generated stylesheet definition.
   */
  public String getCSSString(final boolean compact)
  {
    if (background == null)
    {
      return "font-size: 1pt";
    }

    final StyleBuilder b = new StyleBuilder(compact);
    b.append("font-size", "1pt");

    final Color c = background.getColor();
    if (c != null)
    {
      b.append("background-color", HtmlStyleCollection.getColorString(c));
    }

    if (background.getColorTop() != null)
    {
      b.append("border-top", String.valueOf(background.getBorderSizeTop()),
              "pt");
      b.append("border-top-style", "solid");
      b.append("border-top-color", HtmlStyleCollection.getColorString(
              background.getColorTop()));
    }

    if (background.getColorBottom() != null)
    {
      b.append("border-bottom", String.valueOf(
              background.getBorderSizeBottom()), "pt");
      b.append("border-bottom-style", "solid");
      b.append("border-bottom-color", HtmlStyleCollection.getColorString(
              background.getColorBottom()));
    }

    if (background.getColorLeft() != null)
    {
      b.append("border-left", String.valueOf(background.getBorderSizeLeft()),
              "pt");
      b.append("border-left-style", "solid");
      b.append("border-left-color", HtmlStyleCollection.getColorString(
              background.getColorLeft()));
    }

    if (background.getColorRight() != null)
    {
      b.append("border-right", String.valueOf(background.getBorderSizeRight()),
              "pt");
      b.append("border-right-style", "solid");
      b.append("border-right-color", HtmlStyleCollection.getColorString(
              background.getColorRight()));
    }
    return b.toString();
  }

  public boolean equals(final Object o)
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

    return !(background != null ? !background.equals(
            htmlTableCellStyle.background) : htmlTableCellStyle.background != null);

  }

  public int hashCode()
  {
    return (background != null ? background.hashCode() : 0);
  }
}
