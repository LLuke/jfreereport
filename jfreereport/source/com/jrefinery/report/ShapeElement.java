/**
 * =============================================================
 * JFreeReport : an open source reporting class library for Java
 * =============================================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport;
 * Project Lead:  David Gilbert (david.gilbert@jrefinery.com);
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
 * -----------------
 * ShapeElement.java
 * -----------------
 * (C)opyright 2000-2002, by Simba Management Limited.
 *
 * Original Author:  David Gilbert (for Simba Management Limited);
 * Contributor(s):   -;
 *
 * $Id: ShapeElement.java,v 1.1.1.1 2002/04/25 17:02:23 taqua Exp $
 *
 * Changes (from 8-Feb-2002)
 * -------------------------
 * 08-Feb-2002 : Updated code to work with latest version of the JCommon class library (DG);
 * 05-Mar-2002 : Added paint attribute to Element.java (DG);
 * 10-May-2002 : removed all but the default constructor. Added accessor functions for all properties.
 * 12-May-2002 : Declared abstract and moved line functionality into LineShapeElement-class
 */

package com.jrefinery.report;

import com.jrefinery.report.util.Log;

import java.awt.Paint;
import java.awt.Shape;

/**
 * Used to draw shapes (typically lines and boxes) on a report band. This is the abstract
 * base class for all specialized shape elements.
 */
public abstract class ShapeElement extends Element
{

  /** The shape to draw. */
  private Shape shape;

  /**
   * Constructs a shape element.
   */
  public ShapeElement ()
  {
  }

  /**
   * @return the shape to draw.
   */
  public Shape getShape ()
  {
    return shape;
  }

  /**
   * Defines the shape to draw in this element. subclasses should not override this element
   * directly instead they sould provide accessor functionality suitable for their shape-type.
   */
  protected void setShape (Shape shape)
  {
    if (shape == null)
      throw new NullPointerException ("NullShape is not valid");

    this.shape = shape;
  }

  /**
   * Debugging function.
   */
  public String toString ()
  {
    StringBuffer b = new StringBuffer ();
    b.append ("Shape={ name=");
    b.append (getName ());
    b.append (", bounds=");
    b.append (getBounds ());
    b.append (", shape=");
    b.append (getShape ());
    b.append ("}");
    return b.toString ();
  }

  /**
   * Draws the element at its location relative to the band co-ordinates supplied.
   *
   * @param target The output target on which to draw.
   * @param band The band.
   * @param bandX The x-coordinate for the element within its band.
   * @param bandY The y-coordinate for the element within its band.
   */
  public void draw (OutputTarget target, Band band, float bandX, float bandY)
  {
    Paint paint = getPaint ();

    // set the paint...
    if (paint != null)
    {
      target.setPaint (paint);
    }
    else
    {
      target.setPaint (band.getDefaultPaint ());
    }

    Shape s = getShape ();

    target.drawShape (s, bandX, bandY);
    Log.debug (this.toString ());
  }

}
