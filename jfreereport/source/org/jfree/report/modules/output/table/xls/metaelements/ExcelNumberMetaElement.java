/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
 *
 * (C) Copyright 2000-2004, by Object Refinery Limited and Contributors.
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
 * ------------------------------
 * ExcelNumberMetaElement.java
 * ------------------------------
 * (C)opyright 2004, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Object Refinery Limited);
 *
 * $Id: ExcelNumberMetaElement.java,v 1.4 2005/02/05 18:35:20 taqua Exp $
 *
 * Changes 
 * -------------------------
 * Mar 15, 2004 : Initial version
 *  
 */

package org.jfree.report.modules.output.table.xls.metaelements;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.jfree.report.modules.output.table.base.RawContent;
import org.jfree.report.style.ElementStyleSheet;

public class ExcelNumberMetaElement extends ExcelMetaElement
{
  public ExcelNumberMetaElement (final RawContent elementContent,
                                 final ElementStyleSheet style)
  {
    super(elementContent, style);
  }

  public void applyValue (final HSSFCell cell)
  {
    final RawContent rc = (RawContent) getContent();
    final Number number = (Number) rc.getContent();
    cell.setCellValue(number.doubleValue());

  }
}
