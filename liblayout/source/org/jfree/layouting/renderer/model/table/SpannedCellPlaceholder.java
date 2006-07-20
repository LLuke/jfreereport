/**
 * ===========================================
 * LibLayout : a free Java layouting library
 * ===========================================
 *
 * Project Info:  http://www.jfree.org/liblayout/
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2005, by Object Refinery Limited and Contributors.
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
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * ------------
 * SpannedTableCellRenderBox.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id$
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.layouting.renderer.model.table;

import org.jfree.layouting.renderer.model.SpacerRenderNode;
import org.jfree.layouting.renderer.model.RenderNode;
import org.jfree.layouting.renderer.model.table.cols.TableCell;

/**
 * A place holder for overlaid cells. (Yet another idea stolen from
 * OpenOffice.)
 *
 * @author Thomas Morgner
 */
public class SpannedCellPlaceholder extends SpacerRenderNode
        implements TableCell
{
  private TableCellRenderBox cell;
  private int row;
  private int column;

  public SpannedCellPlaceholder(SpannedCellPlaceholder cell)
  {
    super(0, 0, true);
    this.column = cell.getColSpan();
    this.row = cell.getRowSpan() - 1;
    this.cell = cell.getCell();
  }

  public SpannedCellPlaceholder(TableCellRenderBox cell,
                                int row, int column)
  {
    super(0, 0, true);
    if (cell == null)
    {
      throw new NullPointerException();
    }
    if (column < 1)
    {
      throw new NullPointerException();
    }

    this.cell = cell;
    this.column = column;
    this.row = row;
  }

  public SpannedCellPlaceholder(int rows, int cols)
  {
    this.column = cols;
    this.row = rows;
  }

  /**
   * Cell can be null, if this is a conflict-resolution placeholder.
   *
   * @return
   */
  public TableCellRenderBox getCell()
  {
    return cell;
  }

  public int getColSpan()
  {
    return column;
  }

  public int getRowSpan()
  {
    return row;
  }

  public RenderNode getCellNode()
  {
    return this;
  }
}
