/**
 * Date: Jan 18, 2003
 * Time: 7:56:22 PM
 *
 * $Id: HtmlCellData.java,v 1.3 2003/01/25 20:34:12 taqua Exp $
 */
package com.jrefinery.report.targets.table.html;

import com.jrefinery.report.targets.table.TableCellData;

import java.awt.geom.Rectangle2D;
import java.io.PrintWriter;

public abstract class HtmlCellData extends TableCellData
{
  private HtmlCellStyle style;
  private boolean useXHTML;

  public HtmlCellData(Rectangle2D outerBounds, HtmlCellStyle style, boolean useXHTML)
  {
    super(outerBounds);
    this.style = style;
    this.useXHTML = useXHTML;
  }

  public abstract void write(PrintWriter pout, HtmlFilesystem filesystem);

  public HtmlCellStyle getStyle()
  {
    return style;
  }

  public boolean isUseXHTML()
  {
    return useXHTML;
  }
}
