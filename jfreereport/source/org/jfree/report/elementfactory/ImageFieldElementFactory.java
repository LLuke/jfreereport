/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
 *
 * (C) Copyright 2000-2003, by Simba Management Limited and Contributors.
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
 * ImageFieldElementFactory.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: ImageFieldElementFactory.java,v 1.1 2003/07/07 22:44:04 taqua Exp $
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
import org.jfree.report.filter.templates.ImageFieldTemplate;
import org.jfree.report.layout.StaticLayoutManager;
import org.jfree.report.style.ElementStyleSheet;
import org.jfree.ui.FloatDimension;

/**
 * A factory to define ImageFieldElements. These elements expect an java.awt.Image
 * or an ImageReference instance as content and will display the content in the report.
 * 
 * @author Thomas Morgner
 */
public class ImageFieldElementFactory extends ImageElementFactory
{
  /** The fieldname of the datarow from where to read the content. */
  private String fieldname;

  /**
   * DefaultConstructor.
   *
   */
  public ImageFieldElementFactory()
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
  public void setFieldname(String fieldname)
  {
    this.fieldname = fieldname;
  }

  /**
   * Creates the image element based on the defined properties.
   *  
   * @see org.jfree.report.elementfactory.ElementFactory#createElement()
   * 
   * @return the created image element.
   * @throws IllegalStateException if the fieldname is not set.
   */
  public Element createElement()
  {
    if (getFieldname() == null)
    {
      throw new IllegalStateException("Fieldname is not set.");
    }

    final ImageFieldTemplate template = new ImageFieldTemplate();
    template.setField(getFieldname());
    final ImageElement element = new ImageElement();
    element.setDataSource(template);
    if (getName() != null)
    {
      element.setName(getName());
    }

    final ElementStyleSheet style = element.getStyle();
    style.setStyleProperty(StaticLayoutManager.ABSOLUTE_POS, getAbsolutePosition());
    style.setStyleProperty(ElementStyleSheet.DYNAMIC_HEIGHT, getDynamicHeight());
    style.setStyleProperty(ElementStyleSheet.KEEP_ASPECT_RATIO, getKeepAspectRatio());
    style.setStyleProperty(ElementStyleSheet.MAXIMUMSIZE, getMaximumSize());
    style.setStyleProperty(ElementStyleSheet.MINIMUMSIZE, getMinimumSize());
    style.setStyleProperty(ElementStyleSheet.PREFERREDSIZE, getPreferredSize());
    style.setStyleProperty(ElementStyleSheet.SCALE, getScale());

    return element;
  }


  /**
   * Creates a new ImageElement.
   *
   * @param name the name of the new element
   * @param bounds the bounds of the new element
   * @param field  the name of the column/function/expression that returns the URL for the image.
   *
   * @return a report element for displaying an image.
   *
   * @throws NullPointerException if bounds, name or source are null
   * @throws IllegalArgumentException if the given alignment is invalid
   */
  public static ImageElement createImageDataRowElement(final String name,
                                                       final Rectangle2D bounds,
                                                       final String field
                                                       )
  {
    return createImageDataRowElement(name, bounds, field, true);
  }

  /**
   * Creates a new ImageElement.
   *
   * @param name  the name of the new element.
   * @param bounds  the bounds of the new element.
   * @param field  the name of the column/function/expression that returns the URL for the image.
   * @param scale  scale the image?
   *
   * @return a report element for displaying an image.
   *
   * @throws NullPointerException if bounds, name or source are null
   * @throws IllegalArgumentException if the given alignment is invalid
   */
  public static ImageElement createImageDataRowElement(final String name,
                                                       final Rectangle2D bounds,
                                                       final String field,
                                                       final boolean scale)
  {
    return createImageDataRowElement(name, bounds, field, scale, false);
  }

  /**
   * Creates a new ImageElement.
   *
   * @param name  the name of the new element.
   * @param bounds  the bounds of the new element.
   * @param field  the name of the column/function/expression that returns the URL for the image.
   * @param scale  scale the image?
   * @param keepAspectRatio  preserve the aspect ratio?
   *
   * @return a report element for displaying an image.
   *
   * @throws NullPointerException if bounds, name or source are null
   * @throws IllegalArgumentException if the given alignment is invalid
   */
  public static ImageElement createImageDataRowElement(final String name,
                                                       final Rectangle2D bounds,
                                                       final String field,
                                                       final boolean scale,
                                                       final boolean keepAspectRatio)
  {
    ImageFieldElementFactory factory = new ImageFieldElementFactory();
    factory.setName(name);
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
