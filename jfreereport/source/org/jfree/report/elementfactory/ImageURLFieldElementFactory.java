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
 * $Id: ImageURLFieldElementFactory.java,v 1.1 2003/07/07 22:44:04 taqua Exp $
 *
 * Changes 
 * -------------------------
 * 06.07.2003 : Initial version
 *  
 */

package org.jfree.report.elementfactory;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import org.jfree.report.Element;
import org.jfree.report.ImageElement;
import org.jfree.report.filter.templates.ImageURLFieldTemplate;
import org.jfree.report.layout.StaticLayoutManager;
import org.jfree.report.style.ElementStyleSheet;
import org.jfree.ui.FloatDimension;

public class ImageURLFieldElementFactory extends ImageElementFactory
{
  private String fieldname;

  public ImageURLFieldElementFactory()
  {
  }

  public String getFieldname()
  {
    return fieldname;
  }

  public void setFieldname(String fieldname)
  {
    this.fieldname = fieldname;
  }

  public Element createElement()
  {
    if (getFieldname() == null)
    {
      throw new IllegalStateException("Fieldname is not set.");
    }

    final ImageURLFieldTemplate template = new ImageURLFieldTemplate();
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
    ImageURLFieldElementFactory factory = new ImageURLFieldElementFactory();
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
