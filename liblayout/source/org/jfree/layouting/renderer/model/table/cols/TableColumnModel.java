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
 * TableColumnModel.java
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
package org.jfree.layouting.renderer.model.table.cols;

import java.util.ArrayList;

import org.jfree.layouting.renderer.model.table.TableRenderBox;

/**
 * Creation-Date: 18.07.2006, 16:46:11
 *
 * @author Thomas Morgner
 */
public class TableColumnModel
{
  private ArrayList columnGroups;
  private ArrayList columns;
  private boolean incrementalModeSupported;
  private boolean validated;
  private TableRenderBox table;

  private long preferredSize;
  private long minimumChunkSize;

  public TableColumnModel(final TableRenderBox table)
  {
    this.table = table;
    this.columns = new ArrayList();
    this.columnGroups = new ArrayList();
    this.incrementalModeSupported = true;
  }

  public void addColumnGroup(TableColumnGroup column)
  {
    columnGroups.add(column);
    validated = false;
  }

  public void addAutoColumn()
  {
    TableColumnGroup autoGroup = new TableColumnGroup();
    autoGroup.addColumn(new TableColumn());
    columnGroups.add(autoGroup);
    validated = false;
  }

  public boolean isIncrementalModeSupported()
  {
    return incrementalModeSupported;
  }

  /**
   * The column count may change over time, when new columnGroups get added.
   *
   * @return
   */
  public int getColumnGroupCount()
  {
    return columnGroups.size();
  }

  public int getColumnCount()
  {
    validate();
    return columns.size();
  }

  public void validate()
  {
    if (validated)
    {
      return;
    }

    columns.clear();
    final long contextWidth = table.getComputedBlockContextWidth();

    for (int i = 0; i < columnGroups.size(); i++)
    {
      final TableColumnGroup node = (TableColumnGroup) columnGroups.get(i);
      node.validate(contextWidth);

      final int count = node.getColumnCount();
      for (int x = 0; x < count; x++)
      {
        final TableColumn column = node.getColumn(x);
        columns.add(column);
      }

      if (incrementalModeSupported &&
              node.isIncrementalModeSupported() == false)
      {
        incrementalModeSupported = false;
      }
    }

    validated = true;
  }


  public void validateSizes()
  {
    int maxColSpan = 0;
    for (int i = 0; i < columns.size(); i++)
    {
      TableColumn column = (TableColumn) columns.get(i);
      final int cs = column.getMaximumColspan();
      if (cs > maxColSpan)
      {
        maxColSpan = cs;
      }
    }

    // first, find out how much space is already used.
    final long[] minChunkSizes = new long[columns.size()];
    final long[] preferredSizes = new long[columns.size()];
    // For each colspan ...
    for (int colspan = 1; colspan <= maxColSpan; colspan += 1)
    {
      for (int colIdx = 0; colIdx < minChunkSizes.length; colIdx++)
      {
        TableColumn column = (TableColumn) columns.get(colIdx);
        final long minimumChunkSize = column.getMinimumChunkSize(colspan);
        final long preferredSize = column.getPreferredSize(colspan);

        distribute(minimumChunkSize, minChunkSizes, colIdx, colspan);
        distribute(preferredSize, preferredSizes, colIdx, colspan);
      }
    }

    for (int i = 0; i < minChunkSizes.length; i++)
    {
      minimumChunkSize += minChunkSizes[i];
      preferredSize += preferredSizes[i];

      TableColumn column = (TableColumn) columns.get(i);
      column.setMinimumChunkSize(minChunkSizes[i]);
      column.setPreferredSize(preferredSizes[i]);
    }

    validated = true;
  }

  public long getPreferredSize()
  {
    return preferredSize;
  }

  public long getMinimumChunkSize()
  {
    return minimumChunkSize;
  }

  private void distribute (long usedSpace, long[] allSpaces,
                           int colIdx, int colspan)
  {
    long usedPrev = 0;
    final int maxSize = Math.min(allSpaces.length, colIdx + colspan);
    for (int i = colIdx; i < maxSize; i++)
    {
      usedPrev += allSpaces[i];
    }

    if (usedSpace <= usedPrev)
    {
      // no need to expand the cells.
      return;
    }

    final long distSpace = (usedSpace - usedPrev);
    final long delta = distSpace / colspan;
    for (int i = 0; i < colspan - 1; i++)
    {
      allSpaces[colIdx + i] = delta;
    }
    // any uneven remainder gets added to the last column
    allSpaces[colIdx + colspan - 1] = distSpace - ((colspan - 1) * delta);
  }

  public TableColumnGroup getColumnGroup(int i)
  {
    return (TableColumnGroup) columnGroups.get(i);
  }

  public TableColumn getColumn(int i)
  {
    validate();
    return (TableColumn) columns.get(i);
  }
}
