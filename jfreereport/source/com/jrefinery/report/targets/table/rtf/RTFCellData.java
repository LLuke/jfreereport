/**
 * Date: Feb 1, 2003
 * Time: 7:52:04 PM
 *
 * $Id$
 */
package com.jrefinery.report.targets.table.rtf;

import com.jrefinery.report.targets.table.TableCellData;
import com.lowagie.text.Cell;
import com.lowagie.text.DocumentException;

import java.awt.geom.Rectangle2D;

public abstract class RTFCellData extends TableCellData
{
  private RTFCellStyle style;

  public RTFCellData(Rectangle2D outerBounds, RTFCellStyle style)
  {
    super(outerBounds);
    this.style = style;
  }

  public RTFCellStyle getStyle()
  {
    return style;
  }

  public abstract Cell getCell() throws DocumentException;
}
