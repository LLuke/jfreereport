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
 * LineShapeElement.java
 * -----------------------
 * (C)opyright 2000-2002, by Simba Management Limited.
 *
 *
 *  Changes:
 *  -----------------
 *  10-May-2002: Initial version
 */
package com.jrefinery.report;

import java.awt.geom.Line2D;
import java.awt.Shape;

/**
 * A LineShapeElement encapsulates an Line2D-Shape to display it on the report.
 * <p>
 * Still ToDo: How to print complexer shapes in a report? How about boxes? Or just a simple
 * line style. !!<b>Include WMF-Pixie-Commands into JFreeReport.</b>
 */
public class LineShapeElement extends ShapeElement
{
  public LineShapeElement ()
  {
    setShape(new Line2D.Float());
  }

  /**
   * Sets this elements shape to an Line2D.
   *
   * @param line the shape for this element of type Line2D
   */
  public void setLine (Line2D line)
  {
    setShape(line);
  }

  /**
   * @returns this elements Line2D shape.
   */
  public Line2D getLine ()
  {
    return (Line2D) getShape();
  }

  /**
   * Sets this elements shape. If the shape is no instance of Line2D an ClassCastException is
   * thrown.
   *
   * @param shape the shape for this element of type Line2D
   */
  protected void setShape (Shape shape)
  {
    super.setShape ((Line2D) shape);
  }

  /**
   * Draw the line. If the lines x-coordinates and the y-coordinates are equal, consider
   * this line a horizontal line and adjust the length of this line. This should be done
   * by some sort of relative addressing as in HTML with &lt;hr width="100%"&gt;
   */
  public void draw (OutputTarget target, Band band, float bandX, float bandY)
  {
    Line2D l = getLine();
    if ((l.getX1()==l.getX2()) && (l.getY1()==l.getY2()))
    {
      l.setLine(0.0d, l.getY1(), target.getUsableWidth(), l.getY2());
    }
    super.draw (target, band, bandX, bandY);
  }

  public String toString ()
  {
    Line2D line = getLine();
    StringBuffer b = new StringBuffer ();
    b.append ("Line={ name=");
    b.append (getName());
    b.append (", bounds=");
    b.append (getBounds ());
    b.append (", x1=");
    b.append (line.getX1());
    b.append (", y1=");
    b.append (line.getY1());
    b.append (", x2=");
    b.append (line.getX2());
    b.append (", y2=");
    b.append (line.getY2());
    b.append ("}");
    return b.toString();
  }
}
