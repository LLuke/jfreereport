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
 * RTFCellData.java
 * -------------------
 * (C)opyright 2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: RTFCellData.java,v 1.1 2003/02/01 22:10:37 taqua Exp $
 *
 * Changes
 * -------
 * 01-Feb-2003 : Initial version
 */
package com.jrefinery.report.targets.table.rtf;

import com.jrefinery.report.targets.table.TableCellData;
import com.lowagie.text.Cell;
import com.lowagie.text.DocumentException;

import java.awt.geom.Rectangle2D;

public abstract class RTFCellData extends TableCellData
{
  private RTFCellStyle style;

  public RTFCellData(Rectangle2D outerBounds, RTFCellStyle style)
  {
    super(outerBounds);
    this.style = style;
  }

  public RTFCellStyle getStyle()
  {
    return style;
  }

  public abstract Cell getCell() throws DocumentException;
}
