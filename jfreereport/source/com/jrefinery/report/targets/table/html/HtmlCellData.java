/**
 * Date: Jan 18, 2003
 * Time: 7:56:22 PM
 *
 * $Id: HtmlCellData.java,v 1.2 2003/01/25 02:47:10 taqua Exp $
 */
package com.jrefinery.report.targets.table.html;

import com.jrefinery.report.targets.table.TableCellData;

import java.awt.geom.Rectangle2D;

public abstract class HtmlCellData extends TableCellData
{
  private HtmlCellStyle style;

  public HtmlCellData(Rectangle2D outerBounds, HtmlCellStyle style)
  {
    super(outerBounds);
    this.style = style;
  }

  public abstract String getValue();

  public HtmlCellStyle getStyle()
  {
    return style;
  }
}
