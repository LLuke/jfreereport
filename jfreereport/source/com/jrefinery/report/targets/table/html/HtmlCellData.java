/**
 * Date: Jan 18, 2003
 * Time: 7:56:22 PM
 *
 * $Id$
 */
package com.jrefinery.report.targets.table.html;

import com.jrefinery.report.targets.table.TableCellData;

import java.awt.geom.Rectangle2D;

public class HtmlCellData extends TableCellData
{
  private String value;
  private HtmlCellStyle style;

  public HtmlCellData(Rectangle2D outerBounds, String value, HtmlCellStyle style)
  {
    super(outerBounds);
    this.value = value;
    this.style = style;
  }

  public String getValue()
  {
    return value;
  }

  public HtmlCellStyle getStyle()
  {
    return style;
  }
}
