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
 * PlaceHolderCell.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: PlaceHolderCell.java,v 1.1 2006/10/17 17:31:57 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.layouting.renderer.model.table.cells;

/**
 * Creation-Date: 10.09.2006, 17:27:54
 *
 * @author Thomas Morgner
 */
public class PlaceHolderCell extends TableCell
{
  private DataCell sourceCell;

  public PlaceHolderCell(final DataCell sourceCell,
                         final int rowSpan,
                         final int colSpan)
  {
    super(rowSpan, colSpan);
    if (sourceCell == null)
    {
      throw new NullPointerException();
    }
    this.sourceCell = sourceCell;
  }

  public DataCell getSourceCell()
  {
    return sourceCell;
  }

  public String toString()
  {
    return "PlaceHolderCell{" +
            "rowSpan=" + getRowSpan() +
            ", colSpan=" + getColSpan() +
            ", sourceCell=" + sourceCell +
            '}';
  }
}
