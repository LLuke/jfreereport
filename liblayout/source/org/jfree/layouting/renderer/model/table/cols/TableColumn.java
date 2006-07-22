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
 * TableColumn.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: TableColumn.java,v 1.1 2006/07/20 17:53:50 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.layouting.renderer.model.table.cols;

import org.jfree.layouting.renderer.border.Border;
import org.jfree.layouting.renderer.border.RenderLength;
import org.jfree.layouting.util.LongList;

/**
 * A column definition. A column has an effective definedWidth, which corresponds with
 * the computed definedWidth of the content. If that definedWidth gets greater than the
 * initial definedWidth (the definedWidth that has been computed by the table at the beginning
 * of the rendering), we entered the auto-mode.
 *
 * @author Thomas Morgner
 */
public class TableColumn
{
  private Border border;

  private RenderLength definedWidth;
  private long initialWidth;
  // todo Backgrounds ..

  private boolean autoWidth;

  private long effectiveWidth;

  private long minimumChunkSize;
  private long preferredSize;

  private LongList minimumChunkSizes;
  private LongList preferredSizes;

  public TableColumn()
  {
    this(RenderLength.AUTO, Border.createEmptyBorder());
  }

  public TableColumn(final RenderLength width,
                     final Border border)
  {
    this.preferredSizes = new LongList(10);
    this.minimumChunkSizes = new LongList(10);
    this.border = border;
    this.definedWidth = width;

    if (RenderLength.AUTO.equals(width))
    {
      autoWidth = true;
    }
    else
    {
      autoWidth = true;
    }
  }

  public boolean isAutoWidth()
  {
    return autoWidth && (effectiveWidth > initialWidth);
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

  public long getEffectiveWidth()
  {
    return effectiveWidth;
  }

  public void setEffectiveWidth(final long effectiveWidth)
  {
    this.effectiveWidth = effectiveWidth;
  }

  public long getInitialWidth()
  {
    return initialWidth;
  }

  public void setInitialWidth(final long initialWidth)
  {
    this.initialWidth = initialWidth;
  }

  public Border getBorder()
  {
    return border;
  }

  public RenderLength getDefinedWidth()
  {
    return definedWidth;
  }

  public long getPreferredSize (int colspan)
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

  public long getMinimumChunkSize (int colspan)
  {
    final int index = colspan - 1;
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

  public int getMaximumColspan ()
  {
    return preferredSizes.size();
  }

  public void updateSizes (int colspan,
                           long preferredWidth,
                           long chunkSizes)
  {
    if ((preferredSizes.size() <= colspan) ||
         (preferredSizes.get(colspan) < preferredWidth))
    {
      preferredSizes.set(colspan - 1, preferredWidth);
    }
    if ((colspan >= minimumChunkSizes.size()) ||
         (minimumChunkSizes.get(colspan) < chunkSizes))
    {
      minimumChunkSizes.set(colspan - 1, chunkSizes);
    }
  }

  public void setSizes (int colspan,
                           long preferredWidth,
                           long chunkSizes)
  {
    preferredSizes.set(colspan - 1, preferredWidth);
    minimumChunkSizes.set(colspan - 1, chunkSizes);
  }
}
