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
 * StaticImageURLElementFactory.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Object Refinery Limited);
 *
 * $Id: StaticImageURLElementFactory.java,v 1.5 2003/10/05 21:52:32 taqua Exp $
 *
 * Changes
 * -------------------------
 * 06-Jul-2003 : Initial version
 *
 */

package org.jfree.report.elementfactory;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.net.URL;

import org.jfree.report.Element;
import org.jfree.report.ImageElement;
import org.jfree.report.filter.templates.ImageURLElementTemplate;
import org.jfree.ui.FloatDimension;

/**
 * A factory to create an image element that loads its image from an
 * static URL. The content string may contain a relative URL if an valid
 * base URL is given.
 *
 * @author Thomas Morgner
 */
public class StaticImageURLElementFactory extends ImageElementFactory
{
  /** The content URL (may be a relative URL). */
  private String content;
  /** The base URL for relative content URLs. */
  private URL baseURL;

  /**
   * Default-Constructor.
   *
   */
  public StaticImageURLElementFactory()
  {
  }

  /**
   * Creates the image URL element.
   *
   * @see org.jfree.report.elementfactory.ElementFactory#createElement()
   *
   * @return the generated element.
   * @throws IllegalStateException if the content is not defined.
   */
  public Element createElement()
  {
    if (getContent() == null)
    {
      throw new IllegalStateException("Content is not set.");
    }

    final ImageURLElementTemplate template = new ImageURLElementTemplate();
    template.setBaseURL(getBaseURL());
    template.setContent(getContent());
    final ImageElement element = new ImageElement();
    applyElementName(element);
    applyStyle(element.getStyle());
    element.setDataSource(template);

    return element;
  }

  /**
   * Returns the content part of the URL. This string may contain a relative URL,
   * if the base URL is defined.
   *
   * @return the content part of the URL.
   */
  public String getContent()
  {
    return content;
  }

  /**
   * Defines the content part of the URL. This string may contain a relative URL,
   * if the base URL is defined.
   *
   * @param content the content part of the URL.
   */
  public void setContent(final String content)
  {
    this.content = content;
  }

  /**
   * Returns the BaseURL. The base URL is used to build the complete URL if the
   * content url is relative.
   *
   * @return the base URL.
   */
  public URL getBaseURL()
  {
    return baseURL;
  }

  /**
   * Defines the base URL.
   *
   * @param baseURL the base URL.
   */
  public void setBaseURL(final URL baseURL)
  {
    this.baseURL = baseURL;
  }

  /**
   * Creates a new ImageElement. The source URL is predefined in an StaticDataSource and will
   * not change during the report processing.
   *
   * @param name the name of the new element
   * @param bounds the bounds of the new element
   * @param source the source url from where to load the image
   *
   * @return a report element for displaying an image.
   *
   * @throws NullPointerException if bounds, name or source are null
   * @throws IllegalArgumentException if the given alignment is invalid
   */
  public static ImageElement createImageElement(final String name,
                                                final Rectangle2D bounds,
                                                final URL source)
  {
    return createImageElement(name, bounds, source, true);
  }

  /**
   * Creates a new ImageElement. The source URL is predefined in an StaticDataSource and will
   * not change during the report processing.
   *
   * @param name  the name of the new element.
   * @param bounds  the bounds of the new element.
   * @param source  the source url from where to load the image.
   * @param scale  scale the image?
   *
   * @return a report element for displaying an image.
   *
   * @throws NullPointerException if bounds, name or source are null
   * @throws IllegalArgumentException if the given alignment is invalid
   */
  public static ImageElement createImageElement(final String name,
                                                final Rectangle2D bounds,
                                                final URL source,
                                                final boolean scale)
  {
    return createImageElement(name, bounds, source, scale, false);
  }

  /**
   * Creates a new ImageElement. The source URL is predefined in an StaticDataSource and will
   * not change during the report processing.
   *
   * @param name  the name of the new element.
   * @param bounds  the bounds of the new element.
   * @param source  the source url from where to load the image.
   * @param scale  scale the image?
   * @param keepAspectRatio  preserve the aspect ratio?
   *
   * @return a report element for displaying an image.
   *
   * @throws NullPointerException if bounds, name or source are null
   * @throws IllegalArgumentException if the given alignment is invalid
   */
  public static ImageElement createImageElement(final String name,
                                                final Rectangle2D bounds,
                                                final URL source,
                                                final boolean scale,
                                                final boolean keepAspectRatio)
  {

    final StaticImageURLElementFactory factory = new StaticImageURLElementFactory();
    factory.setName(name);
    factory.setAbsolutePosition(new Point2D.Double(bounds.getX(), bounds.getY()));
    factory.setMinimumSize(new FloatDimension
        ((float) bounds.getWidth(), (float) bounds.getHeight()));
    factory.setScale(new Boolean(scale));
    factory.setKeepAspectRatio(new Boolean(keepAspectRatio));
    factory.setContent(source.toExternalForm());
    return (ImageElement) factory.createElement();
  }
}
