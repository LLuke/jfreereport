/**
 * Date: Jan 21, 2003
 * Time: 4:44:58 PM
 *
 * $Id: CSVCellDataFactory.java,v 1.1 2003/01/21 17:11:41 taqua Exp $
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
    Object value = e.getValue();
    if ((value != null) && (value instanceof String))
    {
      return new CSVCellData((String) value, rect);
    }
    else
    {
      return new CSVCellData("", rect);
    }
  }
}
