/**
 * Date: Jan 18, 2003
 * Time: 7:25:38 PM
 *
 * $Id$
 */
package com.jrefinery.report.targets.table;

import com.jrefinery.report.Element;
import com.jrefinery.report.targets.table.TableCellData;

import java.awt.geom.Rectangle2D;

public interface TableCellDataFactory
{
  public TableCellData createCellData (Element e, Rectangle2D rect);
}
