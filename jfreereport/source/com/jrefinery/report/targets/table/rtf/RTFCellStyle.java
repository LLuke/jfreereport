/**
 * Date: Feb 1, 2003
 * Time: 8:04:21 PM
 *
 * $Id$
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
