/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
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
 * ------------------------------
 * RTFTextCellStyle.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: RTFTextCellStyle.java,v 1.4 2003/06/29 16:59:30 taqua Exp $
 *
 * Changes
 * -------------------------
 * 18-Jun-2003 : Initial version
 *
 */

package org.jfree.report.modules.output.table.rtf;

import java.awt.Color;

import com.lowagie.text.Chunk;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.BaseFont;
import org.jfree.report.ElementAlignment;
import org.jfree.report.style.FontDefinition;

/**
 * Encapsulates the style information for text cells in the RTF table.
 *
 * @author Thomas Morgner
 */
public class RTFTextCellStyle extends RTFCellStyle
{
  /** The used font definition. */
  private FontDefinition font;

  /** The basefont used in that cell style. */
  private BaseFont baseFont;

  /** The text color. */
  private Color fontColor;

  /**
   * Creates a new RTFTextCellStyle.
   *
   * @param font the font definition.
   * @param baseFont the baseFont used to represent the font definition in the RTF document.
   * @param fontColor the font color.
   * @param verticalAlignment the vertical text alignment.
   * @param horizontalAlignment the horizontal text alignment.
   * @throws NullPointerException if one of the alignment parameters is null.
   */
  public RTFTextCellStyle(final FontDefinition font, final BaseFont baseFont, final Color fontColor,
                          final ElementAlignment verticalAlignment, final ElementAlignment horizontalAlignment)
  {
    super(verticalAlignment, horizontalAlignment);
    this.font = font;
    this.baseFont = baseFont;
    this.fontColor = fontColor;
  }

  /**
   * Gets the font definition used in the cell.
   *
   * @return the font definition.
   */
  public FontDefinition getFont()
  {
    return font;
  }

  /**
   * Gets the font color for the cell.
   *
   * @return the font color.
   */
  public Color getFontColor()
  {
    return fontColor;
  }

  /**
   * Define the font for the given iText Chunk.
   *
   * @param p the iText chunk, which should be formated.
   */
  public void applyTextStyle(final Chunk p)
  {
    int style = Font.NORMAL;
    if (font.isBold())
    {
      style += Font.BOLD;
    }
    if (font.isItalic())
    {
      style += Font.ITALIC;
    }
    if (font.isStrikeThrough())
    {
      style += Font.STRIKETHRU;
    }
    if (font.isUnderline())
    {
      style += Font.UNDERLINE;
    }
/*
    int family = Font.HELVETICA;
    if (font.isCourier())
    {
      family = Font.COURIER;
    }
    else if (font.isSerif())
    {
      family = Font.TIMES_ROMAN;
    }
*/
    // p.setFont(new Font(family, font.getFontSize(), style, getFontColor()));
    p.setFont(new Font(baseFont, font.getFontSize(), style, getFontColor()));
  }
}
