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
 * ImageURLFieldElementFactory.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Object Refinery Limited);
 *
 * $Id: ImageURLFieldElementFactory.java,v 1.7 2003/11/01 19:52:27 taqua Exp $
 *
 * Changes
 * -------------------------
 * 06-Jul-2003 : Initial version
 *
 */

package org.jfree.report.elementfactory;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import org.jfree.report.Element;
import org.jfree.report.ImageElement;
import org.jfree.report.filter.templates.ImageURLFieldTemplate;
import org.jfree.ui.FloatDimension;

/**
 * A factory to define ImageURLFieldElements. These elements expect an java.net.URL
 * or an String as content and will display the image content of that URL in the report.
 *
 * @author Thomas Morgner
 */
public class ImageURLFieldElementFactory extends ImageElementFactory
{
  /** The fieldname of the datarow from where to read the content. */
  private String fieldname;

  /**
   * DefaultConstructor.
   *
   */
  public ImageURLFieldElementFactory()
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
   * Creates the image URL field element based on the defined properties.
   *
   * @see org.jfree.report.elementfactory.ElementFactory#createElement()
   *
   * @return the created element.
   */
  public Element createElement()
  {
    if (getFieldname() == null)
    {
      throw new IllegalStateException("Fieldname is not set.");
    }

    final ImageURLFieldTemplate template = new ImageURLFieldTemplate();
    template.setField(getFieldname());
    final ImageElement element = new ImageElement();
    applyElementName(element);
    applyStyle(element.getStyle());
    element.setDataSource(template);


    return element;
  }


  /**
   * Creates a new ImageElement, which is fed from an URL stored in the datasource.
   *
   * @param name the name of the new element
   * @param bounds the bounds of the new element
   * @param field  the name of the column/function/expression that returns the URL for the image.
   *
   * @return a report element for displaying an image based on a URL.
   *
   * @throws NullPointerException if bounds, name or source are null
   * @throws IllegalArgumentException if the given alignment is invalid
   */
  public static ImageElement createImageURLElement(final String name,
                                                   final Rectangle2D bounds,
                                                   final String field)
  {
    return createImageURLElement(name, bounds, field, true);
  }

  /**
   * Creates a new ImageElement, which is fed from an URL stored in the datasource.
   *
   * @param name  the name of the new element.
   * @param bounds  the bounds of the new element.
   * @param field  the name of the column/function/expression that returns the URL for the image.
   * @param scale  scale the image?
   *
   * @return a report element for displaying an image based on a URL.
   *
   * @throws NullPointerException if bounds, name or source are null
   * @throws IllegalArgumentException if the given alignment is invalid
   */
  public static ImageElement createImageURLElement(final String name,
                                                   final Rectangle2D bounds,
                                                   final String field,
                                                   final boolean scale)
  {
    return createImageURLElement(name, bounds, field, scale, false);
  }

  /**
   * Creates a new ImageElement, which is fed from an URL stored in the datasource.
   *
   * @param name  the name of the new element
   * @param bounds  the bounds of the new element
   * @param field  the name of the column/function/expression that returns the URL for the image.
   * @param scale  true if the content should be scaled to fit.
   * @param keepAspectRatio  preserve the aspect ratio.
   *
   * @return a report element for displaying an image based on a URL.
   *
   * @throws NullPointerException if bounds, name or source are null
   * @throws IllegalArgumentException if the given alignment is invalid
   */
  public static ImageElement createImageURLElement(final String name,
                                                   final Rectangle2D bounds,
                                                   final String field,
                                                   final boolean scale,
                                                   final boolean keepAspectRatio)
  {
    final ImageURLFieldElementFactory factory = new ImageURLFieldElementFactory();
    factory.setName(name);
    factory.setAbsolutePosition(new Point2D.Double(bounds.getX(), bounds.getY()));
    factory.setMinimumSize(new FloatDimension
        ((float) bounds.getWidth(), (float) bounds.getHeight()));
    factory.setScale(new Boolean(scale));
    factory.setKeepAspectRatio(new Boolean(keepAspectRatio));
    factory.setFieldname(field);
    return (ImageElement) factory.createElement();
  }
}
