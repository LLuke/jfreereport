/**
 * Date: Jan 25, 2003
 * Time: 5:52:00 AM
 *
 * $Id: HtmlTextCellData.java,v 1.1 2003/01/25 20:38:34 taqua Exp $
 */
package com.jrefinery.report.targets.table.html;

import java.awt.geom.Rectangle2D;
import java.io.PrintWriter;

public class HtmlTextCellData extends HtmlCellData
{
  private String value;

  public HtmlTextCellData(Rectangle2D outerBounds, String value, HtmlCellStyle style, boolean useXHTML)
  {
    super(outerBounds, style, useXHTML);
    if (value == null) throw new NullPointerException();
    this.value = value;
  }

  public void write(PrintWriter pout, HtmlFilesystem filesystem)
  {
    HtmlProducer.printText(pout, value, isUseXHTML());
  }

  public boolean isBackground()
  {
    return false;
  }
}
