/**
 * Date: Jan 21, 2003
 * Time: 4:44:58 PM
 *
 * $Id$
 */
package com.jrefinery.report.targets.table.csv;

import com.jrefinery.report.targets.table.TableCellDataFactory;
import com.jrefinery.report.targets.table.TableCellData;
import com.jrefinery.report.Element;

import java.awt.geom.Rectangle2D;

public class CSVCellDataFactory implements TableCellDataFactory
{
  public TableCellData createCellData(Element e, Rectangle2D rect)
  {
    return new CSVCellData(String.valueOf(e.getValue()), rect);
  }
}
