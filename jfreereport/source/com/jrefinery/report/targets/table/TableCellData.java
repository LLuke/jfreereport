/**
 * Date: Jan 18, 2003
 * Time: 7:23:15 PM
 *
 * $Id: TableCellData.java,v 1.3 2003/01/25 20:34:11 taqua Exp $
 */
package com.jrefinery.report.targets.table;

import com.jrefinery.report.util.Log;

import java.awt.geom.Rectangle2D;

public abstract class TableCellData
{
  /** The position of the outer bounds of the cell */
  private Rectangle2D outerBounds;
  public Log.SimpleMessage debugChunk;

  public TableCellData(Rectangle2D outerBounds)
  {
    if (outerBounds == null) throw new NullPointerException("OuterBounds is null");
    this.outerBounds = outerBounds;
  }

  public Rectangle2D getBounds()
  {
    return outerBounds;
  }

  public abstract boolean isBackground ();
}
