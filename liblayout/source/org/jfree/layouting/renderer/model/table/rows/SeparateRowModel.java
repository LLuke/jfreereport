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
 * SeparateRowModel.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: SeparateRowModel.java,v 1.1 2006/07/22 15:31:00 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.layouting.renderer.model.table.rows;

import org.jfree.layouting.renderer.model.table.TableRenderBox;
import org.jfree.layouting.renderer.model.table.TableSectionRenderBox;

/**
 * Creation-Date: 22.07.2006, 15:23:00
 *
 * @author Thomas Morgner
 */
public class SeparateRowModel extends AbstractRowModel
{
  private long preferredSize;
  private long minimumChunkSize;
  private long validateSize;
  private boolean validatedSize;
  private long rowSpacing;

  public SeparateRowModel(final TableSectionRenderBox tableSection)
  {
    super(tableSection);
  }

  public long getPreferredSize()
  {
    validateSizes();
    return preferredSize;
  }

  public long getMinimumChunkSize()
  {
    validateSizes();
    return minimumChunkSize;
  }

  public long getRowSpacing()
  {
    return rowSpacing;
  }

  public void validateSizes()
  {
    if (validatedSize)
    {
      return;
    }

    int maxRowSpan = 0;
    final TableRow[] rows = getRows();
    final int rowCount = rows.length;
    for (int i = 0; i < rowCount; i++)
    {
      final TableRow row = rows[i];
      final int cs = row.getMaximumRowSpan();
      if (cs > maxRowSpan)
      {
        maxRowSpan = cs;
      }
    }

    final TableRenderBox table = getTableSection().getTable();
    rowSpacing = table.getRowSpacing();

    minimumChunkSize = (rowCount - 1) * rowSpacing;
    preferredSize = (rowCount - 1) * rowSpacing;

    // first, find out how much space is already used.
    final long[] minChunkSizes = new long[rowCount];
    final long[] preferredSizes = new long[rowCount];
    // For each rowspan ...
    for (int rowspan = 1; rowspan <= maxRowSpan; rowspan += 1)
    {
      for (int rowIdx = 0; rowIdx < minChunkSizes.length; rowIdx++)
      {
        final TableRow row = rows[rowIdx];
        final long minimumChunkSize = row.getMinimumChunkSize(rowspan);
        final long preferredSize = row.getPreferredSize(rowspan);

        distribute(minimumChunkSize, minChunkSizes, rowIdx, rowspan);
        distribute(preferredSize, preferredSizes, rowIdx, rowspan);
      }
    }

    for (int i = 0; i < minChunkSizes.length; i++)
    {
      minimumChunkSize += minChunkSizes[i];
      preferredSize += preferredSizes[i];

      final TableRow row = rows[i];
      row.setMinimumChunkSize(minChunkSizes[i]);
      row.setPreferredSize(preferredSizes[i]);
    }

    validatedSize = true;
  }


  public void validateActualSizes()
  {
    int maxRowSpan = 0;
    final TableRow[] rows = getRows();
    final int rowCount = rows.length;
    for (int i = 0; i < rowCount; i++)
    {
      final TableRow row = rows[i];
      final int cs = row.getMaxValidatedRowSpan();
      if (cs > maxRowSpan)
      {
        maxRowSpan = cs;
      }
    }

    // first, find out how much space is already used.
    final long[] trailingSizes = new long[rowCount];
    // For each rowspan ...
    for (int rowspan = 1; rowspan <= maxRowSpan; rowspan += 1)
    {
      for (int rowIdx = 0; rowIdx < trailingSizes.length; rowIdx++)
      {
        final TableRow row = rows[rowIdx];
        final long size = row.getValidatedTrailingSize(rowspan);

        distribute(size, trailingSizes, rowIdx, rowspan);
      }
    }

    for (int i = 0; i < trailingSizes.length; i++)
    {
      final TableRow row = rows[i];
      row.setValidateSize(trailingSizes[i] + row.getValidatedLeadingSize());
    }
  }

  private void distribute (long usedSpace, long[] allSpaces,
                           int colIdx, int colspanX)
  {
    int maxColspan = Math.min (colIdx + colspanX, allSpaces.length) - colIdx;
    long usedPrev = 0;
    final int maxSize = Math.min(allSpaces.length, colIdx + maxColspan);
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
    final long delta = distSpace / maxColspan;
    for (int i = 0; i < maxColspan - 1; i++)
    {
      allSpaces[colIdx + i] = delta;
    }
    // any uneven remainder gets added to the last column
    allSpaces[colIdx + maxColspan - 1] = distSpace - ((maxColspan - 1) * delta);
  }

  public void clear()
  {
    final TableRow[] rows = getRows();
    final int rowCount = rows.length;
    for (int i = 0; i < rowCount; i++)
    {
      final TableRow row = rows[i];
      row.clear();
    }
  }
}
