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
 * $Id: StaticImageElementFactory.java,v 1.2 2003/08/18 18:27:58 taqua Exp $
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
import org.jfree.report.layout.StaticLayoutManager;
import org.jfree.report.style.ElementStyleSheet;

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
  public void setImageReference(ImageReference imageReference)
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
  public void setImage(Image image)
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
    element.setDataSource(datasource);
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
}
