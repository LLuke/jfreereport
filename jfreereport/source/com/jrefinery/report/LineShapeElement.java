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
 * ---------------------
 * LineShapeElement.java
 * ---------------------
 * (C)opyright 2000-2002, by Simba Management Limited.
 *
 *
 *  Changes:
 *  -----------------
 *  10-May-2002 : Initial version
 *  16-May-2002 : recalculate line width when orientation changed
 *  05-Sep-2002 : Documentation
 *
 */
package com.jrefinery.report;

import com.jrefinery.report.targets.OutputTarget;
import com.jrefinery.report.targets.OutputTargetException;

import java.awt.Shape;
import java.awt.geom.Line2D;

/**
 * A LineShapeElement encapsulates an Line2D-Shape to display it on the report.
 *
 * @author TM
 */
public class LineShapeElement extends ShapeElement
{
  /** A flag that controls whether the line extends for the full width of the band. */
  private boolean fullWidth;

  /**
   * Creates a new LineShapeElement.
   */
  public LineShapeElement ()
  {
    setLine (new Line2D.Float ());
    setShouldDraw (true);
    setShouldFill (false);
  }

  /**
   * Sets this elements shape to an Line2D.
   *
   * @param line the shape for this element of type Line2D
   */
  public void setLine (Line2D line)
  {
    setShape (line);
  }

  /**
   * @return this elements Line2D shape.
   */
  public Line2D getLine ()
  {
    return (Line2D) getShape ();
  }

  /**
   * Sets this elements shape. If the shape is no instance of Line2D an ClassCastException is
   * thrown.
   *
   * @param shape the shape for this element of type Line2D
   */
  protected void setShape (Shape shape)
  {
    Line2D line = (Line2D) ((Line2D) shape).clone ();
    this.fullWidth = (line.getX1 () == line.getX2 ()) && (line.getY1 () == line.getY2 ());

    super.setShape (line);
  }

  /**
   * Draw the line. If the lines x-coordinates and the y-coordinates are equal, consider
   * this line a horizontal line and adjust the length of this line. This should be done
   * by some sort of relative addressing as in HTML with &lt;hr width="100%"&gt;
   *
   * @param target  the output target.
   * @param band  the report band.
   *
   * @throws OutputTargetException if there is a problem with the target.
   */
  public void draw (OutputTarget target, Band band) throws OutputTargetException
  {
    Line2D l = getLine ();

    if (fullWidth && ((float) l.getX2 ()) != target.getUsableWidth ())
    {
      l.setLine (0.0d, l.getY1 (), target.getUsableWidth (), l.getY1 ());
    }
    super.draw (target, band);
  }

  /**
   * Displays the contents of this element as string (useful for debugging).
   *
   * @return a string.
   */
  public String toString ()
  {
    Line2D line = getLine ();
    StringBuffer b = new StringBuffer ();
    b.append ("Line={ name=");
    b.append (getName ());
    b.append (", bounds=");
    b.append (getBounds ());
    b.append (", x1=");
    b.append (line.getX1 ());
    b.append (", y1=");
    b.append (line.getY1 ());
    b.append (", x2=");
    b.append (line.getX2 ());
    b.append (", y2=");
    b.append (line.getY2 ());
    b.append ("}");
    return b.toString ();
  }
}
