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
 * $Id: TableRow.java,v 1.1 2006/07/22 15:31:00 taqua Exp $
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
  private long validatedLeadingSize;
  private LongList validatedTrailingSize;
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
    this.validatedLeadingSize = 0;
    this.validatedTrailingSize = new LongList(10);
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
    if (rowSpan < 1)
    {
      throw new IllegalArgumentException();
    }
    final int idx = rowSpan - 1;

    if ((idx >= preferredSizes.size()) ||
            (preferredSizes.get(idx) < preferredWidth))
    {
      preferredSizes.set(idx, preferredWidth);
    }
    if ((rowSpan >= minimumChunkSizes.size()) ||
            (minimumChunkSizes.get(idx) < chunkSizes))
    {
      minimumChunkSizes.set(idx, chunkSizes);
    }
  }

  public void setSizes(int rowSpan,
                       long preferredWidth,
                       long chunkSizes)
  {
    preferredSizes.set(rowSpan - 1, preferredWidth);
    minimumChunkSizes.set(rowSpan - 1, chunkSizes);
  }

  public long getValidatedLeadingSize()
  {
    return validatedLeadingSize;
  }

  public long getValidatedTrailingSize(int rowSpan)
  {
    return validatedTrailingSize.get(rowSpan - 1);
  }

//  public void setValidatedLeadingSize(final long validatedSize)
//  {
//    this.validatedLeadingSize = validatedSize;
//  }

  public void setValidatedTralingSize(final int rowSpan,
                                      final long validatedSize)
  {
    this.validatedTrailingSize.set(rowSpan - 1, validatedSize);
  }

  public int getMaxValidatedRowSpan()
  {
    return this.validatedTrailingSize.size();
  }

  public void updateValidatedSize(final int rowSpan,
                                  final long leading,
                                  final long trailing)
  {
    final int idx = rowSpan - 1;
    if (validatedLeadingSize < leading)
    {
      validatedLeadingSize = leading;
    }
    
    if ((idx >= validatedTrailingSize.size()) ||
            (validatedTrailingSize.get(idx) < trailing))
    {
      validatedTrailingSize.set(idx, trailing);
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

  public void clear()
  {
    //this.validatedLeadingSize = 0;
    this.validatedTrailingSize.clear();
    this.validateSize = 0;
  }
}
