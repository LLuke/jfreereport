/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2003, by Object Refinery Limited and Contributors.
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
 * ------------------------------
 * ShapeFieldElementFactory.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Object Refinery Limited);
 *
 * $Id: ShapeFieldElementFactory.java,v 1.5 2003/10/05 21:52:32 taqua Exp $
 *
 * Changes
 * -------------------------
 * 09-Jun-2003 : Initial version
 *
 */

package org.jfree.report.elementfactory;

import java.awt.Color;
import java.awt.Stroke;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import org.jfree.report.Element;
import org.jfree.report.ShapeElement;
import org.jfree.report.filter.DataRowDataSource;
import org.jfree.ui.FloatDimension;

/**
 * A factory to define Shape field elements.
 *
 * @author Thomas Morgner
 */
public class ShapeFieldElementFactory extends ShapeElementFactory
{
  /** The fieldname of the datarow from where to read the content. */
  private String fieldname;

  /**
   * DefaultConstructor.
   *
   */
  public ShapeFieldElementFactory()
  {
  }

  /**
   * Returns the field name from where to read the content of the element.
   *
   * @return the field name.
   */
  public String getFieldname()
  {
    return fieldname;
  }

  /**
   * Defines the field name from where to read the content of the element.
   * The field name is the name of a datarow column.
   *
   * @param fieldname the field name.
   */
  public void setFieldname(final String fieldname)
  {
    this.fieldname = fieldname;
  }

  /**
   * Creates the shape field element.
   *
   * @see org.jfree.report.elementfactory.ElementFactory#createElement()
   *
   * @return the generated shape field element
   * @throws IllegalStateException if the fieldname is not defined.
   */
  public Element createElement()
  {
    if (getFieldname() == null)
    {
      throw new IllegalStateException("Fieldname is not set.");
    }
    final ShapeElement e = new ShapeElement();
    applyStyle(e.getStyle());
    applyElementName(e);

    e.setDataSource(new DataRowDataSource(getFieldname()));
    return e;
  }

  /**
   * Creates a new ShapeElement.
   *
   * @param name  the name of the new element.
   * @param bounds  the bounds.
   * @param paint  the line color of this element.
   * @param stroke  the stroke of this shape. For pdf use, restrict to BasicStrokes.
   * @param fieldname  the fieldname from where to get the shape.
   * @param shouldDraw  draw the shape?
   * @param shouldFill  fill the shape?
   * @param shouldScale  scale the shape?
   * @param keepAspectRatio  preserve the aspect ratio?
   *
   * @return a report element for drawing a line.
   *
   * @throws NullPointerException if bounds, name or shape are null
   * @throws IllegalArgumentException if the given alignment is invalid
   */
  public static ShapeElement createShapeElement(final String name,
                                                final Rectangle2D bounds,
                                                final Color paint,
                                                final Stroke stroke,
                                                final String fieldname,
                                                final boolean shouldDraw,
                                                final boolean shouldFill,
                                                final boolean shouldScale,
                                                final boolean keepAspectRatio)
  {
    final ShapeFieldElementFactory factory = new ShapeFieldElementFactory();
    factory.setName(name);
    factory.setAbsolutePosition(new Point2D.Double(bounds.getX(), bounds.getY()));
    factory.setMinimumSize(new FloatDimension
        ((float) bounds.getWidth(), (float) bounds.getHeight()));
    factory.setColor(paint);
    factory.setKeepAspectRatio(new Boolean(keepAspectRatio));
    factory.setFieldname(fieldname);
    factory.setStroke(stroke);
    factory.setShouldDraw(new Boolean(shouldDraw));
    factory.setShouldFill(new Boolean(shouldFill));
    factory.setScale(new Boolean(shouldScale));
    return (ShapeElement) factory.createElement();
  }

}
