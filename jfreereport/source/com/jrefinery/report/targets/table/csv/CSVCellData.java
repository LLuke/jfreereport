/**
 * Date: Jan 21, 2003
 * Time: 4:45:33 PM
 *
 * $Id: CSVCellData.java,v 1.1 2003/01/21 17:11:41 taqua Exp $
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

  public boolean isBackground()
  {
    return false;
  }
}
