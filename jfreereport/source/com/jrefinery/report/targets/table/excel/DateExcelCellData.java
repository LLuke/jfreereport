/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
 *
 * (C) Copyright 2000-2002, by Simba Management Limited and Contributors.
 *
 * This library is free software; you can redistribute it and/or modify it under the terms
 * of the GNU Lesser General Public License as published by the Free Software Foundation;
 * either version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this
 * library; if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 * Boston, MA 02111-1307, USA.
 *
 * -------------------
 * DateExcelCellData.java
 * -------------------
 * (C)opyright 2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Heiko Evermann
 * Contributor(s):   Thomas Morgner;David Gilbert (for Simba Management Limited);
 *
 * $Id: DateExcelCellData.java,v 1.3 2003/02/17 22:01:44 taqua Exp $
 *
 * Changes
 * -------
 * 15-Jan-2003 : Initial version
 */
package com.jrefinery.report.targets.table.excel;

import org.apache.poi.hssf.usermodel.HSSFCell;

import java.awt.geom.Rectangle2D;
import java.util.Date;

public class DateExcelCellData extends ExcelCellData
{
  private Date date;
  private String format;

  public DateExcelCellData(Rectangle2D elementBounds,
                           ExcelDataCellStyle style,
                           Date value, String format)
  {
    super(elementBounds, style);
    this.date = value;
    this.format = format;
  }

  public void applyContent(HSSFCell cell)
  {
    cell.setCellValue(date);
  }

  public boolean isEmpty()
  {
    if (date == null)
      return true;
    else
      return false;
  }
}
