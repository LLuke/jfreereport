/**
 * Date: Jan 18, 2003
 * Time: 7:59:00 PM
 *
 * $Id$
 */
package com.jrefinery.report.targets.table.html;

import java.awt.Font;
import java.awt.Color;

public class HtmlCellStyle
{
  private Font font;
  private Color fontColor;

  public HtmlCellStyle(Font font, Color fontColor)
  {
    this.font = font;
    this.fontColor = fontColor;
  }

  public Font getFont()
  {
    return font;
  }

  public Color getFontColor()
  {
    return fontColor;
  }
}
