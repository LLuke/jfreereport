/**
 * Date: Jan 18, 2003
 * Time: 7:23:15 PM
 *
 * $Id$
 */
package com.jrefinery.report.targets.table;

import java.awt.geom.Rectangle2D;

public class TableCellData
{
  /** The position of the outer bounds of the cell */
  private Rectangle2D outerBounds;

  public TableCellData(Rectangle2D outerBounds)
  {
    if (outerBounds == null) throw new NullPointerException("OuterBounds is null");
    this.outerBounds = outerBounds;
  }

  public Rectangle2D getBounds()
  {
    return outerBounds;
  }

}
