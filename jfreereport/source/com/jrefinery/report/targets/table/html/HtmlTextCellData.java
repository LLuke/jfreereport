/**
 * Date: Jan 25, 2003
 * Time: 5:52:00 AM
 *
 * $Id$
 */
package com.jrefinery.report.targets.table.html;

import java.awt.geom.Rectangle2D;

public class HtmlTextCellData extends HtmlCellData
{
  private String value;

  public HtmlTextCellData(Rectangle2D outerBounds, String value, HtmlCellStyle style)
  {
    super(outerBounds, style);
    if (value == null) throw new NullPointerException();
    this.value = value;
  }

  public String getValue()
  {
    return value;
  }

  public boolean isBackground()
  {
    return false;
  }
}
