/**
 * Date: Jan 28, 2003
 * Time: 6:02:15 PM
 *
 * $Id$
 */
package com.jrefinery.report.targets.table;

import java.awt.geom.Rectangle2D;

/**
 * A band representation. Bands should create their own x- y- cuts,
 * so that elements from different bands overlay each other (except
 * the bands do, of course).
 */
public class TableBandArea extends TableCellBackground
{
  public TableBandArea(Rectangle2D outerBounds)
  {
    super(outerBounds, null);
  }

  public String toString ()
  {
    return "TableBandArea={bounds=" + getBounds() + "}";
  }
}
