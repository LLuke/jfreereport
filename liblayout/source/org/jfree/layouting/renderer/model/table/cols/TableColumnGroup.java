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
 * TableColumnGroup.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: TableColumnGroup.java,v 1.1 2006/07/20 17:53:50 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.layouting.renderer.model.table.cols;

import java.util.ArrayList;

import org.jfree.layouting.renderer.border.Border;

/**
 * A table column group contains one or more table columns. The table column
 * group is a normalized element.
 * <p/>
 * A column group may defined a shared background for all columns. The column
 * group may define a minimum width. If the contained cells do not use all of
 * that granted width, they get some extra padding.
 * <p/>
 * As Mozilla does not take the width of a colgroup into account, we will
 * neither.
 *
 * @author Thomas Morgner
 */
public class TableColumnGroup
{
  private ArrayList tableColumns;
  private Border border;
  private boolean incrementalModeSupported;

  public TableColumnGroup(final Border border)
  {
    this.border = border;
    this.tableColumns = new ArrayList();
    this.incrementalModeSupported = true;
  }

  public TableColumnGroup()
  {
    this(Border.createEmptyBorder());
  }

  public void addColumn(TableColumn column)
  {
    this.tableColumns.add(column);
  }

  public Border getBorder()
  {
    return border;
  }

  public void validate(final long computedParentWidth)
  {
    if (tableColumns.size() == 0)
    {
      return;
    }

    for (int i = 0; i < tableColumns.size(); i++)
    {
      final TableColumn column = (TableColumn) tableColumns.get(i);
      final long columnWidth = column.getDefinedWidth().resolve(computedParentWidth);
      column.setInitialWidth(columnWidth);

      if (incrementalModeSupported)
      {
        if (column.isAutoWidth())
        {
          incrementalModeSupported = false;
          break;
        }
      }
    }


  }

  public int getColumnCount()
  {
    return tableColumns.size();
  }

  public boolean isIncrementalModeSupported()
  {
    return incrementalModeSupported;
  }

  public TableColumn getColumn(final int pos)
  {
    return (TableColumn) tableColumns.get(pos);
  }

  public boolean isFirstColumn(TableColumn col)
  {
    if (tableColumns.size() == 0)
    {
      return false;
    }
    return tableColumns.get(0) == col;
  }

  public boolean isLastColumn(TableColumn col)
  {
    if (tableColumns.size() == 0)
    {
      return false;
    }
    return tableColumns.get(tableColumns.size() - 1) == col;
  }
}
