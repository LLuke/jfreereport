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
 * $Id: StaticImageElementFactory.java,v 1.5 2003/10/05 21:52:32 taqua Exp $
 *
 * Changes
 * -------------------------
 * 06-Jul-2003 : Initial version
 *
 */

package org.jfree.report.elementfactory;

import java.awt.Image;

import org.jfree.report.Element;
import org.jfree.report.ImageElement;
import org.jfree.report.ImageReference;
import org.jfree.report.filter.StaticDataSource;

/**
 * A factory to create static image elements. These element contain an immutable
 * image. The image should not be modified.
 *
 * @author Thomas Morgner
 */
public class StaticImageElementFactory extends ImageElementFactory
{
  /** The image reference is the static content of the new element. */
  private ImageReference imageReference;

  /**
   * Default Constructor.
   *
   */
  public StaticImageElementFactory()
  {
  }

  /**
   * Returns the image reference instance of the element.
   *
   * @return the image reference containing the image data.
   */
  public ImageReference getImageReference()
  {
    return imageReference;
  }

  /**
   * Defines the image reference instance of the element.
   *
   * @param imageReference the image reference containing the image data.
   */
  public void setImageReference(final ImageReference imageReference)
  {
    this.imageReference = imageReference;
  }

  /**
   * Returns the AWT-image contained in the image reference.
   *
   * @return the AWT image.
   */
  public Image getImage()
  {
    if (getImageReference() == null)
    {
      return null;
    }
    return getImageReference().getImage();
  }

  /**
   * Defines the image as AWT image. This produces an on-the-fly loaded image.
   *
   * @param image the new image.
   */
  public void setImage(final Image image)
  {
    setImageReference(new ImageReference(image));
  }

  /**
   * Creates the image element.
   *
   * @see org.jfree.report.elementfactory.ElementFactory#createElement()
   *
   * @return the generated image element.
   * @throws IllegalStateException if the image is not defined.
   */
  public Element createElement()
  {
    if (getImageReference() == null)
    {
      throw new IllegalStateException("Content is not set.");
    }

    final StaticDataSource datasource = new StaticDataSource(getImageReference());
    final ImageElement element = new ImageElement();
    applyElementName(element);
    applyStyle(element.getStyle());
    element.setDataSource(datasource);

    return element;
  }
}
