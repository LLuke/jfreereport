/**
 * Date: Jan 21, 2003
 * Time: 4:44:58 PM
 *
 * $Id: CSVCellDataFactory.java,v 1.3 2003/02/02 23:43:52 taqua Exp $
 */
package com.jrefinery.report.targets.table.csv;

import com.jrefinery.report.Element;
import com.jrefinery.report.targets.table.TableCellData;
import com.jrefinery.report.targets.table.TableCellDataFactory;

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
      return null;
    }
  }
}
