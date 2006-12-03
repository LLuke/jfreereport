/**
 * ===========================================
 * LibLayout : a free Java layouting library
 * ===========================================
 *
 * Project Info:  http://jfreereport.pentaho.org/liblayout/
 *
 * (C) Copyright 2000-2005, by Pentaho Corporation and Contributors.
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
 * $Id$
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 */
package org.jfree.layouting.renderer.model.table.cols;

import org.jfree.layouting.renderer.border.Border;
import org.jfree.layouting.renderer.border.RenderLength;
import org.jfree.layouting.util.LongList;

/**
 * A column definition. A column has an effective definedWidth, which
 * corresponds with the computed definedWidth of the content. If that
 * definedWidth gets greater than the initial definedWidth (the definedWidth
 * that has been computed by the table at the beginning of the rendering), we
 * entered the auto-mode.
 *
 * Once a column has been explicitly marked as validated, any attempt to
 * redefine the computed sizes must fail. (This makes sure that the table
 * stays in sync and does not get disordered if its rendered incrementally.)
 *
 * A column is constrained by three metrics:
 *
 * The Minimum ChunkSize defines the smallest non-breakable content item in
 * the column. A column will always consume at least this space. (This is zero,
 * if the column has overflow enabled.)
 *
 * The Maximum Box-Width is the size the content would consume, if there is
 * infinite space available. Manual linebreaks are taken into account, but not
 * automatic ones.
 *
 * If the column explicitly defines a width, the preferred size indicates that.
 * If no preferred width is defined, the preferred size will be zero.
 *
 * @author Thomas Morgner
 */
public class TableColumn
{
  private Border border;
  private RenderLength definedWidth;

  private long computedMinChunkSize;
  private long computedMaximumWidth;
  private long computedPreferredSize;

  private long effectiveSize;
  private long effectiveCellPosition;

  private LongList minimumChunkSizes;
  private LongList maximumBoxWidths;
  private LongList preferredSizes;

  private boolean autoGenerated;
  private boolean validated;

  public TableColumn(final Border border,
                     final RenderLength definedWidth,
                     final boolean autoGenerated)
  {
    if (border == null)
    {
      throw new NullPointerException();
    }
    if (definedWidth == null)
    {
      throw new NullPointerException();
    }

    this.definedWidth = definedWidth;
    this.border = border;
    this.autoGenerated = autoGenerated;
    this.minimumChunkSizes = new LongList(10);
    this.maximumBoxWidths = new LongList(10);
    this.preferredSizes = new LongList(10);
  }

  public RenderLength getDefinedWidth()
  {
    return definedWidth;
  }

  public Border getBorder()
  {
    return border;
  }

  public long getComputedPreferredSize()
  {
    return computedPreferredSize;
  }

  public void setComputedPreferredSize(final long computedPreferredSize)
  {
    this.computedPreferredSize = computedPreferredSize;
  }

  public long getComputedMinChunkSize()
  {
    return computedMinChunkSize;
  }

  public void setComputedMinChunkSize(final long computedMinChunkSize)
  {
    if (isValidated())
    {
      throw new IllegalStateException();
    }
    this.computedMinChunkSize = computedMinChunkSize;
  }

  public long getComputedMaximumWidth()
  {
    return computedMaximumWidth;
  }

  public void setComputedMaximumWidth(final long computedMaximumWidth)
  {
    if (isValidated())
    {
      throw new IllegalStateException();
    }
    this.computedMaximumWidth = computedMaximumWidth;
  }

  public long getEffectiveSize()
  {
    return effectiveSize;
  }

  public void setEffectiveSize(final long effectiveSize)
  {
    this.effectiveSize = effectiveSize;
  }

  public boolean isValidated()
  {
    return validated;
  }

  public void setValidated(final boolean validated)
  {
    this.validated = validated;
  }

  public boolean isAutoGenerated()
  {
    return autoGenerated;
  }

  public void setMinimumChunkSize(final int colspan,
                                  final long chunkSize)
  {
    minimumChunkSizes.set(colspan - 1, chunkSize);
  }

  public void setMaxBoxSize(final int colspan,
                            final long maxBoxSize)
  {
    maximumBoxWidths.set(colspan - 1, maxBoxSize);
  }

  public void setPreferredSize(final int colspan,
                            final long prefSize)
  {
    preferredSizes.set(colspan - 1, prefSize);
  }

  public long getMinimumChunkSize(final int colspan)
  {
    final int idx = colspan - 1;
    if (idx < minimumChunkSizes.size())
    {
      return minimumChunkSizes.get(colspan - 1);
    }
    return 0;
  }

  public long getMaximumBoxWidth(final int colspan)
  {
    final int idx = colspan - 1;
    if (idx < maximumBoxWidths.size())
    {
      return maximumBoxWidths.get(colspan - 1);
    }
    return 0;
  }

  public long getPreferredWidth(final int colspan)
  {
    final int idx = colspan - 1;
    if (idx < preferredSizes.size())
    {
      return preferredSizes.get(colspan - 1);
    }
    return 0;
  }

  public void updateMinimumChunkSize(final int colspan,
                                     final long chunkSize)
  {
    final int idx = colspan - 1;
    if (minimumChunkSizes.size() <= idx)
    {
      minimumChunkSizes.set(idx, chunkSize);
    }
    else if (minimumChunkSizes.get(idx) < chunkSize)
    {
      minimumChunkSizes.set(idx, chunkSize);
    }
  }

  public void updateMaxBoxSize(final int colspan,
                               final long colSize)
  {
    final int idx = colspan - 1;
    if (maximumBoxWidths.size() <= idx)
    {
      maximumBoxWidths.set(idx, colSize);
    }
    else if (maximumBoxWidths.get(idx) < colSize)
    {
      maximumBoxWidths.set(idx, colSize);
    }
  }

  public void updatePreferredSize(final int colspan,
                                  final long colSize)
  {
    final int idx = colspan - 1;
    if (preferredSizes.size() <= idx)
    {
      preferredSizes.set(idx, colSize);
    }
    else if (preferredSizes.get(idx) < colSize)
    {
      preferredSizes.set(idx, colSize);
    }
  }

  public int getMaxColspan()
  {
    return maximumBoxWidths.size();
  }

  public long getEffectiveCellPosition()
  {
    return effectiveCellPosition;
  }

  public void setEffectiveCellPosition(final long effectiveCellPosition)
  {
    this.effectiveCellPosition = effectiveCellPosition;
  }
}
