/**
 * Date: Jan 25, 2003
 * Time: 1:46:45 PM
 *
 * $Id: ExcelDataCellStyle.java,v 1.1 2003/01/25 20:38:33 taqua Exp $
 */
package com.jrefinery.report.targets.table.excel;

import com.jrefinery.report.ElementAlignment;
import com.jrefinery.report.targets.FontDefinition;

import java.awt.Color;

public class ExcelDataCellStyle
{
  private boolean wrapText;
  private ElementAlignment horizontalAlignment;
  private ElementAlignment verticalAlignment;
  private FontDefinition fontDefinition;
  private Color textColor;

  public boolean isWrapText()
  {
    return wrapText;
  }

  public void setWrapText(boolean wrapText)
  {
    this.wrapText = wrapText;
  }

  public ElementAlignment getHorizontalAlignment()
  {
    return horizontalAlignment;
  }

  public void setHorizontalAlignment(ElementAlignment horizontalAlignment)
  {
    this.horizontalAlignment = horizontalAlignment;
  }

  public ElementAlignment getVerticalAlignment()
  {
    return verticalAlignment;
  }

  public void setVerticalAlignment(ElementAlignment verticalAlignment)
  {
    this.verticalAlignment = verticalAlignment;
  }

  public FontDefinition getFontDefinition()
  {
    return fontDefinition;
  }

  public void setFontDefinition(FontDefinition fontDefinition)
  {
    this.fontDefinition = fontDefinition;
  }

  public Color getTextColor()
  {
    return textColor;
  }

  public void setTextColor(Color textColor)
  {
    this.textColor = textColor;
  }

  public boolean equals(Object o)
  {
    if (this == o) return true;
    if (!(o instanceof ExcelDataCellStyle)) return false;

    final ExcelDataCellStyle style = (ExcelDataCellStyle) o;

    if (wrapText != style.wrapText) return false;
    if (fontDefinition != null ? !fontDefinition.equals(style.fontDefinition) : style.fontDefinition != null) return false;
    if (horizontalAlignment != null ? !horizontalAlignment.equals(style.horizontalAlignment) : style.horizontalAlignment != null) return false;
    if (textColor != null ? !textColor.equals(style.textColor) : style.textColor != null) return false;
    if (verticalAlignment != null ? !verticalAlignment.equals(style.verticalAlignment) : style.verticalAlignment != null) return false;

    return true;
  }

  public int hashCode()
  {
    int result;
    result = (wrapText ? 1 : 0);
    result = 29 * result + (horizontalAlignment != null ? horizontalAlignment.hashCode() : 0);
    result = 29 * result + (verticalAlignment != null ? verticalAlignment.hashCode() : 0);
    result = 29 * result + (fontDefinition != null ? fontDefinition.hashCode() : 0);
    result = 29 * result + (textColor != null ? textColor.hashCode() : 0);
    return result;
  }
}
