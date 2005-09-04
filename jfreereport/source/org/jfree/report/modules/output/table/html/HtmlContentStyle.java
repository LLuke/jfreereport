/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2003, by Object Refinery Limited and Contributors.
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
 * Contributor(s):   David Gilbert (for Object Refinery Limited);
 *
 * $Id: HtmlContentStyle.java,v 1.5 2005/02/23 21:05:34 taqua Exp $
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
 * The HtmlCellStyle is used to define the style for the generated HTML-CellStyle. This
 * style directly references to an CSS-StyleDefinition.
 *
 * @author Thomas Morgner
 */
public final class HtmlContentStyle implements HtmlStyle
{
  /**
   * the font definition used for the text.
   */
  private FontDefinition font;

  /**
   * the text color.
   */
  private Color fontColor;

  /**
   * the vertical content alignment.
   */
  private ElementAlignment verticalAlignment;

  /**
   * the horizontal content alignment.
   */
  private ElementAlignment horizontalAlignment;

  /**
   * the cached hashcode.
   */
  private int hashCode;

  //private String name;

  /**
   * Creates a new HTML-StyleDefinition.
   *
   * @param font                the font used to display text.
   * @param fontColor           the text color.
   * @param verticalAlignment   the vertical content alignment.
   * @param horizontalAlignment the horizontal content alignment.
   * @throws NullPointerException if any of the parameters is null.
   */
  public HtmlContentStyle (final FontDefinition font, final Color fontColor,
                           final ElementAlignment verticalAlignment,
                           final ElementAlignment horizontalAlignment)
  {
    if (font == null)
    {
      throw new NullPointerException("FontDefinition is null");
    }
    if (fontColor == null)
    {
      throw new NullPointerException("Color is null");
    }
    if (verticalAlignment == null)
    {
      throw new NullPointerException("VerticalAlignment is null");
    }
    if (horizontalAlignment == null)
    {
      throw new NullPointerException("HorizontalAlignment is null");
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
  public FontDefinition getFont ()
  {
    return font;
  }

  /**
   * Gets the text color for the cell.
   *
   * @return the text color.
   */
  public Color getFontColor ()
  {
    return fontColor;
  }

  /**
   * Returns the vertical element alignment for the content of the cell.
   *
   * @return the vertical alignment.
   */
  public ElementAlignment getVerticalAlignment ()
  {
    return verticalAlignment;
  }

  /**
   * Returns the horizontal element alignment for the content of the cell.
   *
   * @return the horizontal alignment.
   */
  public ElementAlignment getHorizontalAlignment ()
  {
    return horizontalAlignment;
  }

  /**
   * Test, whether an other object equals this HtmlCellStyle.
   *
   * @param o the compared object.
   * @return true, if the given object is a HtmlCellStyle with the same properties defined
   *         as this style.
   */
  public boolean equals (final Object o)
  {
    if (this == o)
    {
      return true;
    }
    if (!(o instanceof HtmlContentStyle))
    {
      return false;
    }

    final HtmlContentStyle style = (HtmlContentStyle) o;

    if (!horizontalAlignment.equals(style.horizontalAlignment))
    {
      return false;
    }
    if (!verticalAlignment.equals(style.verticalAlignment))
    {
      return false;
    }
    if (!font.equals(style.font))
    {
      return false;
    }
    if (!fontColor.equals(style.fontColor))
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
  public int hashCode ()
  {
    if (hashCode == 0)
    {
      int result = font.hashCode();
      result = 29 * result + fontColor.hashCode();
      result = 29 * result + verticalAlignment.hashCode();
      result = 29 * result + horizontalAlignment.hashCode();
      hashCode = result;
    }
    return hashCode;
  }

  /**
   * Translates the font name of the FontDefinition into the HTML-Font name. If the
   * fontdefinition describes a logical font, then the html font name for that logical
   * font is returned.
   *
   * @param font the font definition.
   * @return the translated html font name.
   */
  private String translateFontName (final FontDefinition font)
  {
    if (font.isCourier())
    {
      return "monospaced";
    }
    if (font.isSerif())
    {
      return "serif";
    }
    if (font.isSansSerif())
    {
      return "sans-serif";
    }
    return "'" + font.getFontName() + "'";
  }

  /**
   * Transforms the given HtmlCellStyle into a Cascading StyleSheet definition.
   *
   * @return the generated stylesheet definition.
   */
  public String getCSSString (final boolean compact)
  {
    final FontDefinition font = getFont();
    final StyleBuilder builder = new StyleBuilder(compact);

    builder.append("font-family", translateFontName(font));
    builder.append("font-size", String.valueOf(font.getFontSize()), "pt");

    if (font.isBold())
    {
      builder.append("font-weight", "bold");
    }
    if (font.isItalic())
    {
      builder.append("font-style", "italic");
    }
    if (font.isUnderline() && font.isStrikeThrough())
    {
      builder.append("text-decoration", "underline, line-through");
    }
    else if (font.isUnderline())
    {
      builder.append("text-decoration", "underline");
    }
    else if (font.isStrikeThrough())
    {
      builder.append("text-decoration", "line-through");
    }

    final String colorValue = HtmlStyleCollection.getColorString(getFontColor());
    if (colorValue != null)
    {
      builder.append("color", colorValue);
    }
    // vertical alignment does not work with DIV elements - therefore we use the
    // official way to align block elements:
    //builder.append("vertical-align", translateVerticalAlignment(getVerticalAlignment()));

    if (verticalAlignment == ElementAlignment.BOTTOM)
    {
      builder.append("margin-top", "auto");
      builder.append("margin-bottom", "0");
    }
    else if (verticalAlignment == ElementAlignment.MIDDLE)
    {
      builder.append("margin-top", "auto");
      builder.append("margin-bottom", "auto");
    }
    else
    {
      builder.append("margin-top", "0");
      builder.append("margin-bottom", "auto");
    }

    builder.append("text-align", translateHorizontalAlignment(getHorizontalAlignment()));
    return builder.toString();
  };


  /**
   * Translates the JFreeReport horizontal element alignment into a HTML alignment
   * constant.
   *
   * @param ea the element alignment
   * @return the translated alignment name.
   */
  private String translateHorizontalAlignment (final ElementAlignment ea)
  {
    if (ea == ElementAlignment.RIGHT)
    {
      return "right";
    }
    if (ea == ElementAlignment.CENTER)
    {
      return "center";
    }
    return "left";
  }

}
