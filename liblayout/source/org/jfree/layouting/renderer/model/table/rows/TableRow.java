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
 * TableRow.java
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
package org.jfree.layouting.renderer.model.table.rows;

import org.jfree.layouting.renderer.border.Border;
import org.jfree.layouting.renderer.border.RenderLength;
import org.jfree.layouting.util.LongList;

/**
 * Creation-Date: 22.07.2006, 13:20:47
 *
 * @author Thomas Morgner
 */
public class TableRow
{
  private Border border;

  private long minimumChunkSize;
  private long preferredSize;
  private long validateSize;

  private LongList minimumChunkSizes;
  private LongList preferredSizes;
  private LongList validatedSizes;
  private RenderLength definedHeight;

  public TableRow()
  {
    this(RenderLength.AUTO, Border.createEmptyBorder());
  }

  public TableRow(final RenderLength definedHeight,
                  final Border border)
  {
    this.border = border;
    this.definedHeight = definedHeight;
    this.preferredSizes = new LongList(10);
    this.validatedSizes = new LongList(10);
    this.minimumChunkSizes = new LongList(10);
  }

  public RenderLength getDefinedHeight()
  {
    return definedHeight;
  }

  public Border getBorder()
  {
    return border;
  }

  public long getMinimumChunkSize()
  {
    return minimumChunkSize;
  }

  public void setMinimumChunkSize(final long minimumChunkSize)
  {
    this.minimumChunkSize = minimumChunkSize;
  }

  public long getPreferredSize()
  {
    return preferredSize;
  }

  public void setPreferredSize(final long preferredSize)
  {
    this.preferredSize = preferredSize;
  }

  public long getPreferredSize(int colspan)
  {
    final int index = colspan - 1;
    if (index < 0)
    {
      throw new IllegalArgumentException();
    }

    if (preferredSizes.size() <= index)
    {
      return 0;
    }
    return preferredSizes.get(index);
  }

  public long getMinimumChunkSize(int rowSpan)
  {
    final int index = rowSpan - 1;
    if (index < 0)
    {
      throw new IllegalArgumentException();
    }

    if (minimumChunkSizes.size() <= index)
    {
      return 0;
    }
    return minimumChunkSizes.get(index);
  }

  public int getMaximumRowSpan()
  {
    return preferredSizes.size();
  }

  public void updateSizes(int rowSpan,
                          long preferredWidth,
                          long chunkSizes)
  {
    if ((preferredSizes.size() <= rowSpan) ||
            (preferredSizes.get(rowSpan) < preferredWidth))
    {
      preferredSizes.set(rowSpan - 1, preferredWidth);
    }
    if ((rowSpan >= minimumChunkSizes.size()) ||
            (minimumChunkSizes.get(rowSpan) < chunkSizes))
    {
      minimumChunkSizes.set(rowSpan - 1, chunkSizes);
    }
  }

  public void setSizes(int rowSpan,
                       long preferredWidth,
                       long chunkSizes)
  {
    preferredSizes.set(rowSpan - 1, preferredWidth);
    minimumChunkSizes.set(rowSpan - 1, chunkSizes);
  }

  public long getValidatedSize(int rowSpan)
  {
    return validatedSizes.get(rowSpan - 1);
  }

  public void setValidatedSize(final int rowSpan,
                                final long validatedSize)
  {
    this.validatedSizes.set(rowSpan - 1, validatedSize);
  }

  public int getMaxValidatedRowSpan ()
  {
    return this.validatedSizes.size();
  }

  public void updateValidatedSize(final int rowSpan, final long height)
  {
    if ((rowSpan >= validatedSizes.size()) ||
            (validatedSizes.get(rowSpan) < height))
    {
      validatedSizes.set(rowSpan - 1, height);
    }
  }

  public long getValidateSize()
  {
    return validateSize;
  }

  public void setValidateSize(final long validateSize)
  {
    this.validateSize = validateSize;
  }
}
