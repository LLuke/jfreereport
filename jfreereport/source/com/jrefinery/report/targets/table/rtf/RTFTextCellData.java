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
 * RTFTextCellData.java
 * -------------------
 * (C)opyright 2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: RTFTextCellData.java,v 1.2 2003/02/02 23:43:53 taqua Exp $
 *
 * Changes
 * -------
 * 25-Jan-2003 : Initial version
 */
package com.jrefinery.report.targets.table.rtf;

import com.lowagie.text.Cell;
import com.lowagie.text.Chunk;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Paragraph;

import java.awt.geom.Rectangle2D;

public class RTFTextCellData extends RTFCellData
{
  private String value;

  public RTFTextCellData(Rectangle2D outerBounds, String value, RTFCellStyle style)
  {
    super(outerBounds, style);
    if (value == null) throw new NullPointerException();
    this.value = value;
  }

  public Cell getCell()
    throws DocumentException
  {
    Cell cell = new Cell();
    cell.setBorderWidth(0);

    Chunk chunk = new Chunk(value);
    getStyle().applyTextStyle(chunk);
    Paragraph paragraph  = new Paragraph();
    paragraph.add(chunk);
    cell.addElement(paragraph);
    return cell;
  }

  public boolean isBackground()
  {
    return false;
  }
}
