/**
 * Date: Jan 14, 2003
 * Time: 2:53:00 PM
 *
 * $Id: ExcelCellData.java,v 1.1 2003/01/18 20:47:36 taqua Exp $
 */
package com.jrefinery.report.targets.table.excel;

import com.jrefinery.report.targets.table.TableCellData;

import java.awt.geom.Rectangle2D;

public abstract class ExcelCellData extends TableCellData
{
  /**
   * @param elementBounds are calculated outside
   */
  public ExcelCellData(Rectangle2D elementBounds)
  {
    super(elementBounds);
  }

  /**
   * @see java.lang.Object#toString()
   */
  public String toString()
  {
    return "CellData: " + getClass()
        + " outer bounds= "
        + getBounds();
  }

  public abstract boolean isEmpty ();
}
