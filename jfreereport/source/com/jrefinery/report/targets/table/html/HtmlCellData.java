/**
 * Date: Jan 18, 2003
 * Time: 7:56:22 PM
 *
 * $Id: HtmlCellData.java,v 1.1 2003/01/18 20:47:36 taqua Exp $
 */
package com.jrefinery.report.targets.table.html;

import com.jrefinery.report.targets.table.TableCellData;

import java.awt.geom.Rectangle2D;

public class HtmlCellData extends TableCellData
{
  private String value;
  private HtmlCellStyle style;

  public HtmlCellData(Rectangle2D outerBounds, Object value, HtmlCellStyle style)
  {
    super(outerBounds);
    if (value instanceof String)
    {
      this.value = (String) value;
    }
    else
    {
      // unsupported content is ignored for now ...
      this.value = "";
    }
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
