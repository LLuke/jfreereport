/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
 *
 * (C) Copyright 2000-2003, by Simba Management Limited and Contributors.
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
 * ------------------------------
 * SimplePageFormatDefinition.java
 * ------------------------------
 * (C)opyright 2004, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: SimplePageDefinition.java,v 1.3 2004/04/15 15:13:38 taqua Exp $
 *
 * Changes 
 * -------------------------
 * 14.02.2004 : Initial version
 *  
 */

package org.jfree.report;

import java.awt.print.PageFormat;
import java.awt.geom.Rectangle2D;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

import org.jfree.report.util.SerializerHelper;

public class SimplePageDefinition implements PageDefinition
{
  private transient PageFormat format;
  private int pageCountHorizontal;
  private int pageCountVertical;
  private Rectangle2D[] pagePositions;

  public SimplePageDefinition(final PageFormat format,
                              final int x, final int y)
  {
    if (format == null)
    {
      throw new NullPointerException("Format must not be null");
    }
    if (x < 1)
    {
      throw new IllegalArgumentException("PageCount must be greater or equal to 1");
    }
    if (y < 1)
    {
      throw new IllegalArgumentException("PageCount must be greater or equal to 1");
    }
    this.format = (PageFormat) format.clone();
    this.pageCountHorizontal = x;
    this.pageCountVertical = y;
    this.pagePositions = new Rectangle2D[pageCountHorizontal * pageCountVertical];

    final float width = (float) format.getImageableWidth();
    final float height = (float) format.getImageableHeight();
    float pageStartY = 0;
    for (int vert = 0; vert < pageCountVertical; vert++)
    {
      float pageStartX = 0;
      for (int hor = 0; hor < pageCountHorizontal; hor++)
      {
        final Rectangle2D rect =
                new Rectangle2D.Float(pageStartX, pageStartY, width, height);
        pagePositions[vert * pageCountHorizontal + hor] = rect;
        pageStartX += width;
      }
      pageStartY += height;
    }
  }

  public SimplePageDefinition(final PageFormat format)
  {
    this (format, 1, 1);
  }

  public int getPageCount()
  {
    return pageCountHorizontal * pageCountVertical;
  }

  public PageFormat getPageFormat(final int pos)
  {
    if (pos < 0 || pos > getPageCount())
    {
      throw new IndexOutOfBoundsException("Index is invalid");
    }
    return (PageFormat) format.clone();
  }

  public Rectangle2D getPagePosition(final int pos)
  {
    if (pos < 0 || pos > getPageCount())
    {
      throw new IndexOutOfBoundsException("Index is invalid");
    }
    return pagePositions[pos].getBounds2D();
  }

  public Rectangle2D[] getPagePositions()
  {
    final Rectangle2D[] rects =
            new Rectangle2D.Float[pagePositions.length];
    for (int i = 0; i < pagePositions.length; i++)
    {
      rects[i] = pagePositions[i].getBounds2D();
    }
    return rects;
  }

  public float getHeight()
  {
    return (float) (format.getImageableHeight() * pageCountVertical);
  }

  public float getWidth()
  {
    return (float) (format.getImageableWidth() * pageCountHorizontal);
  }

  /**
   * deserizalize the report and restore the pageformat.
   *
   * @param out the objectoutput stream
   * @throws java.io.IOException if errors occur
   */
  private void writeObject(final ObjectOutputStream out)
      throws IOException
  {
    out.defaultWriteObject();
    SerializerHelper.getInstance().writeObject(format, out);
  }

  /**
   * resolve the pageformat, as PageFormat is not serializable.
   *
   * @param in  the input stream.
   *
   * @throws java.io.IOException if there is an IO problem.
   * @throws ClassNotFoundException if there is a class problem.
   */
  private void readObject(final ObjectInputStream in)
      throws IOException, ClassNotFoundException
  {
    in.defaultReadObject();
    format = (PageFormat) SerializerHelper.getInstance().readObject(in);
  }

  public Object clone () throws CloneNotSupportedException
  {
    final SimplePageDefinition pdef = (SimplePageDefinition) super.clone();
    pdef.format = (PageFormat) format.clone();
    return pdef;
  }
}
