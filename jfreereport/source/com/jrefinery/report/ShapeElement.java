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
 * -----------------
 * ShapeElement.java
 * -----------------
 * (C)opyright 2000-2002, by Simba Management Limited.
 *
 * Original Author:  David Gilbert (for Simba Management Limited);
 * Contributor(s):   -;
 *
 * $Id: ShapeElement.java,v 1.16 2002/08/19 21:17:29 taqua Exp $
 *
 * Changes (from 8-Feb-2002)
 * -------------------------
 * 08-Feb-2002 : Updated code to work with latest version of the JCommon class library (DG);
 * 05-Mar-2002 : Added paint attribute to Element.java (DG);
 * 10-May-2002 : Removed all but the default constructor. Added accessor functions for all
 *               properties.
 * 12-May-2002 : Declared abstract and moved line functionality into LineShapeElement-class
 * 16-May-2002 : using protected member m_paint instead of getter methode
 *               stroke property added (JS)
 * 26-May-2002 : Added shoudDraw and shouldFill properties. These are internal and customize
 *               whether a shape is drawn, filled or both by the default drawing engine
 */

package com.jrefinery.report;

import com.jrefinery.report.targets.OutputTarget;
import com.jrefinery.report.targets.OutputTargetException;

import java.awt.BasicStroke;
import java.awt.Shape;
import java.awt.Stroke;

/**
 * Used to draw shapes (typically lines and boxes) on a report band. This is the abstract
 * base class for all specialized shape elements.
 *
 * @author DG
 */
public abstract class ShapeElement extends Element
{
  /** default stroke size. */
  public static final BasicStroke DEFAULT_STROKE = new BasicStroke (0.5f);

  /** The shape to draw. */
  private Shape shape;

  /** The stroke. */
  private Stroke stroke;

  /** Fill the shape? */
  private boolean shouldFill;

  /** Draw the shape. */
  private boolean shouldDraw;

  /**
   * Constructs a shape element.
   */
  public ShapeElement ()
  {
    setStroke (DEFAULT_STROKE);
  }

  /**
   * Returns the shape to draw.
   *
   * @return the shape.
   */
  public Shape getShape ()
  {
    return shape;
  }

  /**
   * Defines the shape to draw in this element. subclasses should not override this element
   * directly instead they should provide accessor functionality suitable for their shape-type.
   *
   * @param shape  the shape.
   */
  protected void setShape (Shape shape)
  {
    if (shape == null)
    {
      throw new NullPointerException ("NullShape is not valid");
    }

    this.shape = shape;
  }

  /**
   * Returns a string describing the element.  Useful for debugging.
   *
   * @return the string.
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
    b.append (", stroke=");
    b.append (getStroke ());
    b.append ("}");

    return b.toString ();
  }

  /**
   * Draws the element at its location relative to the band co-ordinates supplied.
   *
   * @param target The output target on which to draw.
   * @param band The band.
   *
   * @throws OutputTargetException if there is a problem with the target.
   */
  public void draw (OutputTarget target, Band band) throws OutputTargetException
  {
    // set the paint...
    target.setPaint (getPaint (band));
    target.setStroke (getStroke ());
    if (isShouldDraw ())
    {
      target.drawShape (getShape ());
    }
    if (isShouldFill ())
    {
      target.fillShape (getShape ());
    }
  }

  /**
   * Returns the stroke.
   *
   * @return the Stroke.
   */
  public Stroke getStroke ()
  {
    return this.stroke;
  }

  /**
   * Sets the stroke.
   *
   * @param stroke  the stroke
   */
  public void setStroke (Stroke stroke)
  {
    if (stroke == null)
    {
      throw new NullPointerException ();
    }
    this.stroke = stroke;
  }

  /**
   * Specifies whether the outline of this elements shape should be printed.
   * By default this returns true.
   *
   * @return true if the outline should be drawn, false otherwise
   */
  public boolean isShouldDraw ()
  {
    return shouldDraw;
  }

  /**
   * Specifies whether the contents of this elements shape should be filled with this elements
   * paint. By default this returns true.
   *
   * @return true if the outline should be drawn, false otherwise
   */
  public boolean isShouldFill ()
  {
    return shouldFill;
  }

  /**
   * Sets a flag that controls whether or not the outline of the shape is drawn.
   *
   * @param shouldDraw  the flag.
   */
  public void setShouldDraw (boolean shouldDraw)
  {
    this.shouldDraw = shouldDraw;
  }

  /**
   * Sets a flag that controls whether or not the area of the shape is filled.
   *
   * @param shouldFill  the flag.
   */
  public void setShouldFill (boolean shouldFill)
  {
    this.shouldFill = shouldFill;
  }

}
