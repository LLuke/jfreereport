/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
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
 * StrictDimension.java
 * ------------
 * (C) Copyright 2002-2005, by Object Refinery Limited.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: JCommon.java,v 1.1 2004/07/15 14:49:46 mungady Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.report.util.geom;

import java.io.Serializable;

public class StrictDimension implements Serializable, Cloneable
{
  private long width;
  private long height;
  private boolean locked;

  public StrictDimension ()
  {
  }

  public StrictDimension (final long width, final long height)
  {
    this.width = width;
    this.height = height;
  }

  public boolean isLocked ()
  {
    return locked;
  }

  public StrictDimension getLockedInstance ()
  {
    final StrictDimension retval = (StrictDimension) clone();
    retval.locked = true;
    return retval;
  }

  public StrictDimension getUnlockedInstance ()
  {
    final StrictDimension retval = (StrictDimension) clone();
    retval.locked = false;
    return retval;
  }

  public long getHeight ()
  {
    return height;
  }

  public long getWidth ()
  {
    return width;
  }

  /**
   * Sets the size of this <code>Dimension</code> object to the specified width and
   * height. This method is included for completeness, to parallel the {@link
   * java.awt.Component#getSize getSize} method of {@link java.awt.Component}.
   *
   * @param width  the new width for the <code>Dimension</code> object
   * @param height the new height for the <code>Dimension</code> object
   */
  public void setSize (final long width, final long height)
  {
    if (locked)
    {
      throw new IllegalStateException();
    }

    this.width = width;
    this.height = height;
  }

  public void setHeight (final long height)
  {
    if (locked)
    {
      throw new IllegalStateException();
    }
    this.height = height;
  }

  public void setWidth (final long width)
  {
    if (locked)
    {
      throw new IllegalStateException();
    }
    this.width = width;
  }

  public Object clone ()
  {
    try
    {
      final StrictDimension sdim = (StrictDimension) super.clone();
      return sdim;
    }
    catch (CloneNotSupportedException e)
    {
      throw new InternalError("Clone must always be supported.");
    }
  }


  public String toString ()
  {
    return "org.jfree.report.util.geom.StrictDimension{" +
            "width=" + width +
            ", height=" + height +
            "}";
  }
}
