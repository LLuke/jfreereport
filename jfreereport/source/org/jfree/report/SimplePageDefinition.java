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
 * $Id: SimplePageDefinition.java,v 1.1 2004/03/16 15:34:26 taqua Exp $
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

  public SimplePageDefinition(final PageFormat format)
  {
    this.format = (PageFormat) format.clone();
  }

  public float getHeight()
  {
    return (float) format.getImageableHeight();
  }

  public int getPageCount()
  {
    return 1;
  }

  public PageFormat getPageFormat(final int pos)
  {
    if (pos != 0)
    {
      throw new IndexOutOfBoundsException("Index must be '0'");
    }
    return (PageFormat) format.clone();
  }

  public Rectangle2D getPagePosition(final int pos)
  {
    if (pos != 0)
    {
      throw new IndexOutOfBoundsException("Index must be '0'");
    }
    return new Rectangle2D.Float(0,0, getWidth(), getHeight());
  }

  public Rectangle2D[] getPagePositions()
  {
    return new Rectangle2D[]{getPagePosition(0)};
  }

  public float getWidth()
  {
    return (float) format.getImageableWidth();
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
