/**
 * Date: Jan 18, 2003
 * Time: 7:59:00 PM
 *
 * $Id: HtmlCellStyle.java,v 1.1 2003/01/18 20:47:36 taqua Exp $
 */
package com.jrefinery.report.targets.table.html;

import com.jrefinery.report.targets.FontDefinition;
import com.jrefinery.report.ElementAlignment;

import java.awt.Color;

public class HtmlCellStyle
{
  private FontDefinition font;
  private Color fontColor;
  private ElementAlignment verticalAlignment;
  private ElementAlignment horizontalAlignment;

  public HtmlCellStyle(FontDefinition font, Color fontColor, ElementAlignment verticalAlignment, ElementAlignment horizontalAlignment)
  {
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
}
