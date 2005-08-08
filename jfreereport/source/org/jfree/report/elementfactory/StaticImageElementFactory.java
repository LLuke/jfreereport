/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
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
 * StaticImageElementFactory.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: StaticImageElementFactory.java,v 1.11 2005/07/18 18:09:19 taqua Exp $
 *
 * Changes
 * -------------------------
 * 06-Jul-2003 : Initial version
 * 30-Dec-2004 : Image property points to a ImageContainer and replaces the old
 *               AWT-Image and ImageReference properties. 
 */

package org.jfree.report.elementfactory;

import java.awt.Image;
import java.io.IOException;

import org.jfree.report.Element;
import org.jfree.report.ImageContainer;
import org.jfree.report.ImageElement;
import org.jfree.report.LocalImageContainer;
import org.jfree.report.DefaultImageReference;
import org.jfree.report.filter.StaticDataSource;

/**
 * A factory to create static image elements. These element contain an immutable image.
 * The image should not be modified.
 *
 * @author Thomas Morgner
 */
public class StaticImageElementFactory extends ImageElementFactory
{
  /**
   * The image reference is the static content of the new element.
   */
  private ImageContainer imageContainer;

  /**
   * Default Constructor.
   */
  public StaticImageElementFactory ()
  {
  }

  /**
   * Returns the image assigned to this element.
   *
   * @return the element's image.
   */
  public Image getImage ()
  {
    if (imageContainer == null)
    {
      return null;
    }
    if (imageContainer instanceof LocalImageContainer)
    {
      final LocalImageContainer lc = (LocalImageContainer) imageContainer;
      return lc.getImage();
    }
    return null;
  }

  /**
   * Defines the image for this image element factory.
   *
   * @param image the image.
   * @throws IOException if the given image could not be completly loaded.
   */
  public void setImage (final Image image)
          throws IOException
  {
    this.imageContainer = new DefaultImageReference (image);
  }

  /**
   * Returns the image reference instance of the element.
   *
   * @return the image reference containing the image data.
   */
  public ImageContainer getImageContainer ()
  {
    return imageContainer;
  }

  /**
   * Defines the image reference instance of the element.
   *
   * @param imageReference the image reference containing the image data.
   * @deprecated use setImageContainer instead
   */
  public void setImageReference (final ImageContainer imageReference)
  {
    this.imageContainer = imageReference;
  }

  /**
   * Assigns the image container for this image element.
   *
   * @param imageReference the image container for this element.
   */
  public void setImageContainer (final ImageContainer imageReference)
  {
    this.imageContainer = imageReference;
  }

  /**
   * Creates the image element.
   *
   * @return the generated image element.
   *
   * @throws IllegalStateException if the image is not defined.
   * @see org.jfree.report.elementfactory.ElementFactory#createElement()
   */
  public Element createElement ()
  {
    if (getImage() == null)
    {
      throw new IllegalStateException("Content is not set.");
    }

    final StaticDataSource datasource = new StaticDataSource(getImageContainer());
    final ImageElement element = new ImageElement();
    applyElementName(element);
    applyStyle(element.getStyle());
    element.setDataSource(datasource);

    return element;
  }
}
