/**
 * ========================================
 * <libname> : a free Java <foobar> library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/liblayout/
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2005, by Object Refinery Limited and Contributors.
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
 * ---------
 * ExcelExportContext.java
 * ---------
 *
 * Original Author:  Thomas Morgner;
 * Contributors: -;
 *
 * $Id: ExcelExportContext.java,v 1.1 2005/12/10 17:49:12 taqua Exp $
 *
 * Changes
 * -------------------------
 * 09.12.2005 : Initial version
 */
package org.jfree.report.modules.output.table.xls;

import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.jfree.report.modules.output.table.base.SheetLayout;

/**
 * Creation-Date: 09.12.2005, 18:42:30
 *
 * @author Thomas Morgner
 */
public interface ExcelExportContext
{
  public HSSFWorkbook getWorkbook();
  public HSSFPatriarch getPatriarch();
  public HSSFSheet getCurrentSheet();

  public SheetLayout getCurrentLayout();
  public int getLayoutOffset();
  
}
