/**
 * =============================================================
 * JFreeReport : an open source reporting class library for Java
 * =============================================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
 *
 * (C) Copyright 2000-2002, by Simba Management Limited and Contributors.
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
 * ---------------
 * BandCursor.java
 * ---------------
 * (C)opyright 2000-2002, by Simba Management Limited.
 *
 * Changes
 * -------
 * 16-May-2002 : Initial version
 * 08-Jun-2002 : Documentation added
 */
package com.jrefinery.report.targets;

import java.awt.geom.Rectangle2D;

/**
 * A 'cursor' used to track the bounds of the current band and element for an OutputTarget.
 *
 * @author TM
 */
public class BandCursor
{
  /** The bounds for the current band. */
  private Rectangle2D bandBounds;

  /** The bounds for the current element. */
  private Rectangle2D elementBounds;

  /**
   * Creates a new cursor.
   */
  public BandCursor()
  {
    elementBounds = new Rectangle2D.Float();
    bandBounds = new Rectangle2D.Float();
  }

  /**
   * Defines the bounds for the currently drawn band.
   *
   * @param bounds  the bounds.
   */
  public void setBandBounds(Rectangle2D bounds)
  {
    if (bounds == null)
    {
      throw new NullPointerException();
    }
    this.bandBounds.setRect(bounds);
  }

  /**
   * Returns the bounds for the currently drawn band.
   *
   * @return  The bounds.
   */
  public Rectangle2D getBandBounds()
  {
    Rectangle2D bounds = new Rectangle2D.Float();
    bounds.setRect(bandBounds);
    return bounds;
  }

  /**
   * Defines the bounds for the current element within the current band.
   *
   * @param bounds  the element bounds.
   */
  public void setElementBounds(Rectangle2D bounds)
  {
    if (bounds == null)
    {
      throw new NullPointerException();
    }
    this.elementBounds.setRect(bounds);
  }

  /**
   * Returns the bounds for the currently drawn element within the defined band.
   *
   * @return The bounds.
   */
  public Rectangle2D getElementBounds()
  {
    Rectangle2D bounds = new Rectangle2D.Float();
    bounds.setRect(elementBounds);
    return bounds;
  }

  /**
   * Returns the translated element bounds.
   * <P>
   * Some targets may have a different coordinate system and so will require translation of the
   * coordinates into the native format.
   *
   * @return The bounds.
   */
  public Rectangle2D getDrawBounds()
  {
    return getElementBounds();
  }

  /**
   * Translated values. Use this if you call an TargetInteral function.
   *
   * @param bounds  the bounds.
   */
  public void setDrawBounds(Rectangle2D bounds)
  {
    setElementBounds(bounds);
  }

}
