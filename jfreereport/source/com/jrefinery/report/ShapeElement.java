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
 * $Id: ShapeElement.java,v 1.4 2002/05/16 15:50:05 jaosch Exp $
 *
 * Changes (from 8-Feb-2002)
 * -------------------------
 * 08-Feb-2002 : Updated code to work with latest version of the JCommon class library (DG);
 * 05-Mar-2002 : Added paint attribute to Element.java (DG);
 * 10-May-2002 : removed all but the default constructor. Added accessor functions for all properties.
 * 12-May-2002 : Declared abstract and moved line functionality into LineShapeElement-class
 * 16-May-2002 : using protected member m_paint instead of getter methode
 *               stroke property added (JS)
 * 
 */

package com.jrefinery.report;

import java.awt.BasicStroke;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;

import com.jrefinery.report.util.Log;

/**
 * Used to draw shapes (typically lines and boxes) on a report band. This is the abstract
 * base class for all specialized shape elements.
 */
public abstract class ShapeElement extends Element
{
  /** default stroke size. */
  private final static BasicStroke STROKE = new BasicStroke(0.5f);

  /** The shape to draw. */
  private Shape shape;
  
  private Stroke m_stroke;

  /**
   * Constructs a shape element.
   */
  public ShapeElement()
  {
    m_stroke = STROKE;
  }

  /**
   * @return the shape to draw.
   */
  public Shape getShape()
  {
    return shape;
  }

  /**
   * Defines the shape to draw in this element. subclasses should not override this element
   * directly instead they sould provide accessor functionality suitable for their shape-type.
   */
  protected void setShape(Shape shape)
  {
    if (shape == null)
      throw new NullPointerException("NullShape is not valid");

    this.shape = shape;
  }

  /**
   * Debugging function.
   */
  public String toString()
  {
    StringBuffer b = new StringBuffer();
    b.append("Shape={ name=");
    b.append(getName());
    b.append(", bounds=");
    b.append(getBounds());
    b.append(", shape=");
    b.append(getShape());
    b.append("}");

    return b.toString();
  }

  /**
   * Draws the element at its location relative to the band co-ordinates supplied.
   *
   * @param target The output target on which to draw.
   * @param band The band.
   * @param bandX The x-coordinate for the element within its band.
   * @param bandY The y-coordinate for the element within its band.
   */
  public void draw(OutputTarget target, Band band, float bandX, float bandY)
  {
    // set the paint...
    if (m_paint != null)
    {
      target.setPaint(m_paint);
    }
    else
    {
      target.setPaint(band.getDefaultPaint());
    }

    Shape s = getShape();
    target.drawShape(this, bandX, bandY);
  }

  /**
   * Gets the stroke.
   * @return Returns a Stroke
   */
  public Stroke getStroke()
  {
    return m_stroke;
  }

  /**
   * Sets the stroke.
   * @param stroke The stroke to set
   */
  public void setStroke(Stroke stroke)
  {
    if (stroke != null) {
      m_stroke = stroke;
    }
  }
}