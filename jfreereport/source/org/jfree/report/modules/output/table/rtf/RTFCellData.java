/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2003, by Simba Management Limited and Contributors.
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
 * ----------------
 * RTFCellData.java
 * ----------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: RTFCellData.java,v 1.1 2003/07/07 22:44:07 taqua Exp $
 *
 * Changes
 * -------
 * 01-Feb-2003 : Initial version
 */
package org.jfree.report.modules.output.table.rtf;

import java.awt.geom.Rectangle2D;

import com.lowagie.text.Cell;
import com.lowagie.text.DocumentException;
import org.jfree.report.modules.output.table.base.TableCellData;

/**
 * The RTF extension of the {@link TableCellData} base class. The cell data implementation contains
 * the cell style for a RTF table cell.
 *
 * @author Thomas Morgner
 */
public abstract class RTFCellData extends TableCellData
{
  /** the cell style. */
  private RTFCellStyle style;

  /**
   * Creates a new RTFCellData with the given bounds and style.
   *
   * @param outerBounds the cell bounds.
   * @param style the cell style.
   * @throws NullPointerException if the style is null.
   */
  public RTFCellData(final Rectangle2D outerBounds, final RTFCellStyle style)
  {
    super(outerBounds);
    if (style == null)
    {
      throw new NullPointerException("Style is null");
    }
    this.style = style;
  }

  /**
   * Gets the cell style for the RTF table cell.
   *
   * @return the cell style.
   */
  public RTFCellStyle getStyle()
  {
    return style;
  }

  /**
   * Creates a iText TableCell with some content in it.
   *
   * @return the cell with the content.
   * @throws DocumentException if there is a problem adding the cell to the document.
   */
  public abstract Cell getCell() throws DocumentException;
}
