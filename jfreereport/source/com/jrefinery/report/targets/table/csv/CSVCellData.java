/**
 * Date: Jan 21, 2003
 * Time: 4:45:33 PM
 *
 * $Id$
 */
package com.jrefinery.report.targets.table.csv;

import com.jrefinery.report.targets.table.TableCellData;

import java.awt.geom.Rectangle2D;

public class CSVCellData extends TableCellData
{
  private String value;

  public CSVCellData(String value, Rectangle2D outerBounds)
  {
    super(outerBounds);
    this.value = value;
  }

  public String getValue()
  {
    return value;
  }
}
