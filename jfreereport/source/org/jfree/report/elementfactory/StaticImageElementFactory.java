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
 * $Id: StaticImageElementFactory.java,v 1.6 2003/10/18 22:05:11 taqua Exp $
 *
 * Changes
 * -------------------------
 * 06-Jul-2003 : Initial version
 * 30-Dec-2004 : Image property points to a ImageContainer and replaces the old
 *               AWT-Image and ImageReference properties. 
 */

package org.jfree.report.elementfactory;

import org.jfree.report.Element;
import org.jfree.report.ImageContainer;
import org.jfree.report.ImageElement;
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
  private ImageContainer imageReference;

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
  public ImageContainer getImage()
  {
    return imageReference;
  }

  /**
   * Defines the image reference instance of the element.
   *
   * @param imageReference the image reference containing the image data.
   */
  public void setImageReference(final ImageContainer imageReference)
  {
    this.imageReference = imageReference;
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
    if (getImage() == null)
    {
      throw new IllegalStateException("Content is not set.");
    }

    final StaticDataSource datasource = new StaticDataSource(getImage());
    final ImageElement element = new ImageElement();
    applyElementName(element);
    applyStyle(element.getStyle());
    element.setDataSource(datasource);

    return element;
  }
}
