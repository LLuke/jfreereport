/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2003, by Simba Management Limited and Contributors.
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
 * -------------------
 * HtmlCellStyle.java
 * -------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: HtmlCellStyle.java,v 1.3 2003/08/24 15:06:10 taqua Exp $
 *
 * Changes
 * -------
 * 18-Jan-2003 : Initial version
 */
package org.jfree.report.modules.output.table.html;

import java.awt.Color;

import org.jfree.report.ElementAlignment;
import org.jfree.report.style.FontDefinition;

/**
 * The HtmlCellStyle is used to define the style for the generated HTML-CellStyle.
 * This style directly references to an CSS-StyleDefinition.
 *
 * @author Thomas Morgner
 */
public class HtmlCellStyle
{
  /** the font definition used for the text. */
  private FontDefinition font;

  /** the text color. */
  private Color fontColor;

  /** the vertical content alignment. */
  private ElementAlignment verticalAlignment;

  /** the horizontal content alignment. */
  private ElementAlignment horizontalAlignment;

  /**
   * Creates a new HTML-StyleDefinition.
   *
   * @param font the font used to display text.
   * @param fontColor the text color.
   * @param verticalAlignment the vertical content alignment.
   * @param horizontalAlignment the horizontal content alignment.
   * @throws NullPointerException if any of the parameters is null.
   */
  public HtmlCellStyle(final FontDefinition font, final Color fontColor,
                       final ElementAlignment verticalAlignment,
                       final ElementAlignment horizontalAlignment)
  {
    if (font == null)
    {
      throw new NullPointerException("Font");
    }
    if (fontColor == null)
    {
      throw new NullPointerException("FontColor");
    }
    if (verticalAlignment == null)
    {
      throw new NullPointerException("VAlign");
    }
    if (horizontalAlignment == null)
    {
      throw new NullPointerException("HAlign");
    }

    this.font = font;
    this.fontColor = fontColor;
    this.verticalAlignment = verticalAlignment;
    this.horizontalAlignment = horizontalAlignment;
  }

  /**
   * Gets the font style for the cell.
   *
   * @return the defined font.
   */
  public FontDefinition getFont()
  {
    return font;
  }

  /**
   * Gets the text color for the cell.
   *
   * @return the text color.
   */
  public Color getFontColor()
  {
    return fontColor;
  }

  /**
   * Returns the vertical element alignment for the content of the cell.
   *
   * @return the vertical alignment.
   */
  public ElementAlignment getVerticalAlignment()
  {
    return verticalAlignment;
  }

  /**
   * Returns the horizontal element alignment for the content of the cell.
   *
   * @return the horizontal alignment.
   */
  public ElementAlignment getHorizontalAlignment()
  {
    return horizontalAlignment;
  }

  /**
   * Test, whether an other object equals this HtmlCellStyle.
   *
   * @param o the compared object.
   * @return true, if the given object is a HtmlCellStyle with the same properties
   * defined as this style.
   */
  public boolean equals(final Object o)
  {
    if (this == o)
    {
      return true;
    }
    if (!(o instanceof HtmlCellStyle))
    {
      return false;
    }

    final HtmlCellStyle style = (HtmlCellStyle) o;

    if (!font.equals(style.font))
    {
      return false;
    }
    if (!fontColor.equals(style.fontColor))
    {
      return false;
    }
    if (!horizontalAlignment.equals(style.horizontalAlignment))
    {
      return false;
    }
    if (!verticalAlignment.equals(style.verticalAlignment))
    {
      return false;
    }

    return true;
  }

  /**
   * Gets the hashcode for this style definition.
   *
   * @return the hashcode.
   */
  public int hashCode()
  {
    int result;
    result = font.hashCode();
    result = 29 * result + fontColor.hashCode();
    result = 29 * result + verticalAlignment.hashCode();
    result = 29 * result + horizontalAlignment.hashCode();
    return result;
  }
}
