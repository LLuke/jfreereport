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
 * $Id: BandCursor.java,v 1.8 2002/11/07 21:45:28 taqua Exp $
 *
 * Changes
 * -------
 * 16-May-2002 : Initial version
 * 08-Jun-2002 : Documentation added
 *
 */

package com.jrefinery.report.targets;

import java.awt.geom.Rectangle2D;

/**
 * A 'cursor' used to track the bounds of the current band and element for an OutputTarget.
 * <p>
 * The coordinates follow the Java2D convention where x-values increase from left to right, and
 * y-values increase from top to bottom.
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
    this.bandBounds = new Rectangle2D.Float();
    this.elementBounds = new Rectangle2D.Float();
  }

  /**
   * Sets the bounds for the current band.
   *
   * @param bounds  the bounds (null not allowed).
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
   * Returns the bounds for the current band.
   * <p>
   * The structure returned is a copy of the cursor's internal structure, so modifying it will have
   * no effect on the cursor state.
   *
   * @return  the bounds.
   */
  public Rectangle2D getBandBounds()
  {
    Rectangle2D bounds = new Rectangle2D.Float();
    bounds.setRect(this.bandBounds);
    return bounds;
  }

  /**
   * Defines the bounds for the current element within the current band.
   *
   * @param bounds  the element bounds (null not permitted).
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
   * <p>
   * The structure returned is a copy of the internal structure, so modifying it will have
   * no effect on the cursor state.
   *
   * @return the bounds.
   */
  public Rectangle2D getElementBounds()
  {
    Rectangle2D bounds = new Rectangle2D.Float();
    bounds.setRect(elementBounds);
    return bounds;
  }

  /**
   * Returns the element bounds translated to the target's underlying coordinate system.
   * <P>
   * Some targets may have a different coordinate system and so will require translation of the
   * coordinates into the native format.
   *
   * @return the bounds.
   */
  public Rectangle2D getDrawBounds()
  {
    return getElementBounds();
  }

  /**
   * Sets the element bounds using the underlying coordinate system of the target.  The
   * translated element bounds will also be recorded.
   *
   * @param bounds  the bounds.
   */
  public void setDrawBounds(Rectangle2D bounds)
  {
    setElementBounds(bounds);
  }

}
