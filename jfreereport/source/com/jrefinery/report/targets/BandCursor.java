/**
 * =============================================================
 * JFreeReport : an open source reporting class library for Java
 * =============================================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
 * Project Lead:  David Gilbert (david.gilbert@object-refinery.com)
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
 * -----------------------
 * BandCursor.java
 * -----------------------
 * (C)opyright 2000-2002, by Simba Management Limited.
 *
 */
package com.jrefinery.report.targets;

import java.awt.geom.Rectangle2D;

/**
 * A BandCursor is used to define the bounds of the currently drawn element in the
 * current OutputTarget.
 */
public class BandCursor
{
  private Rectangle2D bandBounds;
  private Rectangle2D elementBounds;

  /**
   * Creates a new Cursor.
   */
  public BandCursor ()
  {
    elementBounds = new Rectangle2D.Float ();
    bandBounds = new Rectangle2D.Float ();
  }

  /**
   * Defines the bounds for the currently drawn band.
   */
  public void setBandBounds (Rectangle2D bounds)
  {
    if (bounds == null)
      throw new NullPointerException ();
    this.bandBounds.setRect (bounds);
  }

  /**
   * returns the bounds for the currently drawn band.
   */
  public Rectangle2D getBandBounds ()
  {
    Rectangle2D bounds = new Rectangle2D.Float ();
    bounds.setRect (bandBounds);
    return bounds;
  }

  /**
   * defines the bounds for the currently drawn element within the defined band.
   */
  public void setElementBounds (Rectangle2D bounds)
  {
    if (bounds == null)
      throw new NullPointerException ();
    this.elementBounds.setRect (bounds);
  }

  /**
   * returns the bounds for the currently drawn element within the defined band.
   */
  public Rectangle2D getElementBounds ()
  {
    Rectangle2D bounds = new Rectangle2D.Float ();
    bounds.setRect (elementBounds);
    return bounds;
  }

  /**
   * Translated
   */
  public Rectangle2D getDrawBounds ()
  {
    return getElementBounds ();
  }
}
