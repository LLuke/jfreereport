/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
 *
 * (C) Copyright 2000-2002, by Simba Management Limited and Contributors.
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
 * RTFCellStyle.java
 * -------------------
 * (C)opyright 2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: RTFCellStyle.java,v 1.1 2003/02/01 22:10:37 taqua Exp $
 *
 * Changes
 * -------
 * 01-Feb-2003 : Initial version
 */
package com.jrefinery.report.targets.table.rtf;

import com.jrefinery.report.ElementAlignment;
import com.jrefinery.report.targets.FontDefinition;
import com.lowagie.text.Cell;
import com.lowagie.text.Chunk;
import com.lowagie.text.Element;
import com.lowagie.text.Font;

import java.awt.Color;

public class RTFCellStyle
{
  private FontDefinition font;
  private Color fontColor;
  private ElementAlignment verticalAlignment;
  private ElementAlignment horizontalAlignment;

  public RTFCellStyle(FontDefinition font, Color fontColor,
                      ElementAlignment verticalAlignment, ElementAlignment horizontalAlignment)
  {
    if (font == null) throw new NullPointerException("Font");
    if (fontColor == null) throw new NullPointerException("FontColor");
    if (verticalAlignment == null) throw new NullPointerException("VAlign");
    if (horizontalAlignment == null) throw new NullPointerException("HAlign");

    this.font = font;
    this.fontColor = fontColor;
    this.verticalAlignment = verticalAlignment;
    this.horizontalAlignment = horizontalAlignment;
  }

  public FontDefinition getFont()
  {
    return font;
  }

  public Color getFontColor()
  {
    return fontColor;
  }

  public ElementAlignment getVerticalAlignment()
  {
    return verticalAlignment;
  }

  public ElementAlignment getHorizontalAlignment()
  {
    return horizontalAlignment;
  }

  public boolean equals(Object o)
  {
    if (this == o) return true;
    if (!(o instanceof RTFCellStyle))
    {
      return false;
    }

    final RTFCellStyle style = (RTFCellStyle) o;

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

  public int hashCode()
  {
    int result;
    result = font.hashCode();
    result = 29 * result + fontColor.hashCode();
    result = 29 * result + verticalAlignment.hashCode();
    result = 29 * result + horizontalAlignment.hashCode();
    return result;
  }

  public void applyTextStyle (Chunk p)
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

    int family = Font.HELVETICA;
    if (font.isCourier())
    {
      family = Font.COURIER;
    }
    else if (font.isSerif())
    {
      family = Font.TIMES_NEW_ROMAN;
    }
    p.setFont(new Font(family, font.getFontSize(), style, getFontColor()));
  }

  public void applyAlignment (Cell cell)
  {
    if (horizontalAlignment == ElementAlignment.LEFT)
    {
      cell.setHorizontalAlignment(Element.ALIGN_LEFT);
    }
    else if (horizontalAlignment == ElementAlignment.CENTER)
    {
      cell.setHorizontalAlignment(Element.ALIGN_CENTER);
    }
    else if (horizontalAlignment == ElementAlignment.RIGHT)
    {
      cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
    }

    if (verticalAlignment == ElementAlignment.TOP)
    {
      cell.setVerticalAlignment(Element.ALIGN_TOP);
    }
    else if (verticalAlignment == ElementAlignment.MIDDLE)
    {
      cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
    }
    else if (verticalAlignment == ElementAlignment.BOTTOM)
    {
      cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
    }
  }
}
