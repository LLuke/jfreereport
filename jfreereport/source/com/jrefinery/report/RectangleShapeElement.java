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
 * RectangleShapeElement.java
 * -----------------------
 * (C)opyright 2000-2002, by Simba Management Limited.
 *
 * Changes
 * -------
 * 09-Jun-2002 : Documentation
 */
package com.jrefinery.report;

import com.jrefinery.report.targets.OutputTarget;
import com.jrefinery.report.targets.OutputTargetException;

import java.awt.Shape;
import java.awt.geom.Rectangle2D;

/**
 * A RectangleShapeElement encapsulates an Rectangle2D-Shape to display it on the report.
 * This rectangle is filled with this elements paint, but no outline is drawn.
 */
public class RectangleShapeElement extends ShapeElement
{
  /**
   * DefaultConstructor
   */
  public RectangleShapeElement ()
  {
    setShouldDraw (false);
    setShouldFill (true);
  }

  /**
   * @return this elements Rectangle2D shape.
   */
  public Rectangle2D getRectangle ()
  {
    return (Rectangle2D) getBounds ();
  }

  /**
   * Sets this elements shape to a Rectangle2D-shape.
   *
   * @param rect the shape for this element of type Rectangle2D
   */
  public void setRectangle (Rectangle2D rect)
  {
    setShape (rect);
  }

  /**
   * Sets this elements shape. If the shape is no instance of Rectangle2D an ClassCastException is
   * thrown.
   *
   * @param shape the shape for this element of type Rectangle2D
   */
  public void setShape (Shape shape)
  {
    super.setShape ((Rectangle2D) shape);
    super.setBounds ((Rectangle2D) shape);
  }

  /**
   * Defines the bounds for this element.
   * <p>
   * The bounds must not be null, or a NullPointerException is thrown.
   * The contents of the bounds are copied into this elements bounds, the parameter
   * object can be reused by the caller.
   * <p>
   * Adjusts the shape of this element to fit the bounds.
   *
   * @param bounds the bounds of this element
   * @throws NullPointerException
   */
  public void setBounds (Rectangle2D bounds)
  {
    super.setBounds (bounds);
    super.setShape (bounds);
  }

  /**
   * Draw the rectangle. The rectangle drawn is this elements bounds object. The bounds are
   * adjusted by the band if they contain relative values.
   */
  public void draw (OutputTarget target, Band band) throws OutputTargetException
  {
    super.setShape (target.getCursor ().getElementBounds ());
    super.draw (target, band);
    System.out.println ("DRAW: " + isShouldDraw() + " FILL " + isShouldFill() + " Name=" + getName());
  }
}
