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
 * --------------------
 * RTFTextCellData.java
 * --------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: RTFTextCellData.java,v 1.3 2003/08/24 15:06:10 taqua Exp $
 *
 * Changes
 * -------
 * 25-Jan-2003 : Initial version
 */
package org.jfree.report.modules.output.table.rtf;

import java.awt.geom.Rectangle2D;

import com.lowagie.text.Cell;
import com.lowagie.text.Chunk;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Paragraph;

/**
 * A wrapper for text content within the generated RTF Table.
 *
 * @author Thomas Morgner
 */
public class RTFTextCellData extends RTFCellData
{
  /** the text content that should be printed within the cell. */
  private String value;

  /**
   * Creates a new RTFTextCellData for the given content.
   *
   * @param outerBounds the cell bounds.
   * @param value the text content.
   * @param style the style definition for the cell.
   */
  public RTFTextCellData(final Rectangle2D outerBounds, final String value,
                         final RTFTextCellStyle style)
  {
    super(outerBounds, style);
    if (value == null)
    {
      throw new NullPointerException();
    }
    this.value = value;
  }

  /**
   * Creates a iText TableCell with text content in it.
   *
   * @return the cell with the content.
   * @throws DocumentException if the cell could not be created.
   */
  public Cell getCell()
      throws DocumentException
  {
    final Cell cell = new Cell();
    cell.setBorderWidth(0);

    final Chunk chunk = new Chunk(value);
    final RTFTextCellStyle style = (RTFTextCellStyle) getStyle();
    style.applyTextStyle(chunk);
    final Paragraph paragraph = new Paragraph();
    paragraph.add(chunk);
    cell.addElement(paragraph);
    return cell;
  }

  /**
   * Gets a flag, which indicates whether this cell contains background definitions.
   *
   * @return false, as this is no background cell.
   */
  public boolean isBackground()
  {
    return false;
  }
}
