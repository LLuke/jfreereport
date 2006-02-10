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
 * $Id: HtmlTableCellStyle.java,v 1.10 2006/02/09 22:04:50 taqua Exp $
 *
 * Changes 
 * -------------------------
 * 09-Mar-2004 : Initial version
 *  
 */

package org.jfree.report.modules.output.table.html;

import java.awt.Color;
import java.awt.Stroke;

import org.jfree.report.ElementAlignment;
import org.jfree.report.modules.output.table.base.TableCellBackground;
import org.jfree.report.util.StrokeUtility;
import org.jfree.util.ObjectUtilities;

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
  private ElementAlignment verticalAlignment;

  public HtmlTableCellStyle(final TableCellBackground background,
                            final ElementAlignment verticalAlignment)
  {
    if (verticalAlignment == null)
    {
      throw new NullPointerException();
    }
    this.background = background;
    this.verticalAlignment = verticalAlignment;
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
      return "font-size: 1pt; " +
              "vertical-align:" +
              translateVerticalAlignment(verticalAlignment) + ";";
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
      b.append("border-top-width",
              String.valueOf(StrokeUtility.getStrokeWidth(background.getBorderStrokeTop())),
              "pt");
      b.append("border-top-style", translateStrokeStyle(background.getBorderStrokeTop()));
      b.append("border-top-color", HtmlStyleCollection.getColorString(
              background.getColorTop()));
    }

    if (background.getColorBottom() != null)
    {
      b.append("border-bottom-width",
              String.valueOf(StrokeUtility.getStrokeWidth(background.getBorderStrokeBottom())),
              "pt");
      b.append("border-bottom-style", translateStrokeStyle(background.getBorderStrokeBottom()));
      b.append("border-bottom-color", HtmlStyleCollection.getColorString(
              background.getColorBottom()));
    }

    if (background.getColorLeft() != null)
    {
      b.append("border-left-width",
              String.valueOf(StrokeUtility.getStrokeWidth(background.getBorderStrokeLeft())),
              "pt");
      b.append("border-left-style", translateStrokeStyle(background.getBorderStrokeLeft()));
      b.append("border-left-color", HtmlStyleCollection.getColorString(
              background.getColorLeft()));
    }

    if (background.getColorRight() != null)
    {
      b.append("border-right-width",
              String.valueOf(StrokeUtility.getStrokeWidth(background.getBorderStrokeRight())),
              "pt");
      b.append("border-right-style", translateStrokeStyle(background.getBorderStrokeRight()));
      b.append("border-right-color", HtmlStyleCollection.getColorString(
              background.getColorRight()));
    }
    b.append("vertical-align", translateVerticalAlignment(verticalAlignment));

    return b.toString();
  }


  public static String translateStrokeStyle (Stroke s)
  {
    int style = StrokeUtility.getStrokeType(s);
    switch (style)
    {
      case StrokeUtility.STROKE_DASHED: return "dashed";
      case StrokeUtility.STROKE_DOTTED: return "dotted";
      case StrokeUtility.STROKE_DOT_DASH: return "dot-dash";
      case StrokeUtility.STROKE_DOT_DOT_DASH: return "dot-dot-dash";
      default: return "solid";
    }
  }

  /**
   * Translates the JFreeReport horizontal element alignment into a HTML alignment
   * constant.
   *
   * @param ea the element alignment
   * @return the translated alignment name.
   */
  private String translateVerticalAlignment (final ElementAlignment ea)
  {
    if (ea == ElementAlignment.BOTTOM)
    {
      return "bottom";
    }
    if (ea == ElementAlignment.MIDDLE)
    {
      return "middle";
    }
    return "top";
  }

  public boolean equals(final Object o)
  {
    if (this == o)
    {
      return true;
    }
    if (o == null || getClass() != o.getClass())
    {
      return false;
    }

    final HtmlTableCellStyle that = (HtmlTableCellStyle) o;

    if (ObjectUtilities.equal(background, that.background) == false)
    {
      return false;
    }
    if (verticalAlignment.equals(that.verticalAlignment) == false)
    {
      return false;
    }

    return true;
  }

  public int hashCode()
  {
    int result;
    result = (background != null ? background.hashCode() : 0);
    result = 29 * result + verticalAlignment.hashCode();
    return result;
  }
}
