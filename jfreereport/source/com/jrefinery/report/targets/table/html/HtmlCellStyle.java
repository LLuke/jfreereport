/**
 * Date: Jan 18, 2003
 * Time: 7:59:00 PM
 *
 * $Id: HtmlCellStyle.java,v 1.4 2003/01/27 03:17:43 taqua Exp $
 */
package com.jrefinery.report.targets.table.html;

import com.jrefinery.report.ElementAlignment;
import com.jrefinery.report.targets.FontDefinition;
import com.jrefinery.report.util.Log;

import java.awt.Color;

public class HtmlCellStyle
{
  private FontDefinition font;
  private Color fontColor;
  private ElementAlignment verticalAlignment;
  private ElementAlignment horizontalAlignment;

  public HtmlCellStyle(FontDefinition font, Color fontColor, ElementAlignment verticalAlignment, ElementAlignment horizontalAlignment)
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
    if (!(o instanceof HtmlCellStyle))
    {
      Log.debug ("Not the same class");
      return false;
    }

    final HtmlCellStyle style = (HtmlCellStyle) o;

    if (!font.equals(style.font))
    {
      Log.debug ("Not the same font");
      return false;
    }
    if (!fontColor.equals(style.fontColor))
    {
      Log.debug ("Not the same fontcolor");
      return false;
    }
    if (!horizontalAlignment.equals(style.horizontalAlignment))
    {
      Log.debug ("Not the same horizontal alignment");
      return false;
    }
    if (!verticalAlignment.equals(style.verticalAlignment))
    {
      Log.debug ("Not the same vertical alignment");
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
}
