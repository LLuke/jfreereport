/**
 * Date: Jan 18, 2003
 * Time: 7:25:38 PM
 *
 * $Id: TableCellDataFactory.java,v 1.2 2003/01/25 20:34:11 taqua Exp $
 */
package com.jrefinery.report.targets.table;

import com.jrefinery.report.Element;

import java.awt.geom.Rectangle2D;

public interface TableCellDataFactory
{
  /**
   *
   * @param e
   * @param rect
   * @return null if element type is not supported ...
   */
  public TableCellData createCellData (Element e, Rectangle2D rect);
}
