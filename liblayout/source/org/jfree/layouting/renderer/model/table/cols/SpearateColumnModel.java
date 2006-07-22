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
 * $Id: TableColumnModel.java,v 1.1 2006/07/20 17:53:50 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.layouting.renderer.model.table.cols;

import org.jfree.layouting.renderer.border.RenderLength;
import org.jfree.layouting.renderer.model.table.TableRenderBox;

/**
 * Creation-Date: 18.07.2006, 16:46:11
 *
 * @author Thomas Morgner
 */
public class SpearateColumnModel extends AbstractColumnModel implements TableColumnModel
{
  private boolean validatedSize;
  private long preferredSize;
  private long minimumChunkSize;
  private long borderSpacing;

  public SpearateColumnModel(final TableRenderBox table)
  {
    super(table);
  }


  public void validateSizes()
  {
    if (isValidated() && validatedSize)
    {
      return;
    }

    validate();

    int maxColSpan = 0;
    final TableColumn[] columns = getColumns();
    final int colCount = columns.length;
    for (int i = 0; i < colCount; i++)
    {
      final TableColumn column = columns[i];
      final int cs = column.getMaximumColspan();
      if (cs > maxColSpan)
      {
        maxColSpan = cs;
      }
    }

    final RenderLength borderSpacingLength = getTable().getBorderSpacing();
    borderSpacing = borderSpacingLength.resolve
            (getTable().getComputedBlockContextWidth());

    minimumChunkSize = (colCount - 1) * borderSpacing;
    preferredSize = (colCount - 1) * borderSpacing;

    // first, find out how much space is already used.
    final long[] minChunkSizes = new long[colCount];
    final long[] preferredSizes = new long[colCount];
    // For each colspan ...
    for (int colspan = 1; colspan <= maxColSpan; colspan += 1)
    {
      for (int colIdx = 0; colIdx < minChunkSizes.length; colIdx++)
      {
        final TableColumn column = columns[colIdx];
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

      final TableColumn column = columns[i];
      column.setMinimumChunkSize(minChunkSizes[i]);
      column.setPreferredSize(preferredSizes[i]);
    }

    validatedSize = true;
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

  public long getBorderSpacing()
  {
    return borderSpacing;
  }
}
