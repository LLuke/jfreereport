/**
 * =============================================================
 * JFreeReport : an open source reporting class library for Java
 * =============================================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
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
 * ImageElement.java
 * -----------------
 * (C)opyright 2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: ImageElement.java,v 1.6 2002/05/28 19:28:22 taqua Exp $
 *
 * Changes:
 * --------
 * 24-Apr-2002 : Defines a reference to an Bitmap or Wmf-Image for the reports (TM);
 * 10-May-2002 : Removed all but the default constructor. Added accessor functions for all
 *               properties (TM);
 * 16-May-2002 : Added Javadoc comments (DG);
 * 16-May-2002 : using protected member m_paint instead of getter methode (JS)
 *
 */

package com.jrefinery.report;

import com.jrefinery.report.targets.OutputTarget;
import com.jrefinery.report.targets.OutputTargetException;

import java.awt.geom.Rectangle2D;

/**
 * Used to draw images (Gif, JPEG, PNG or wmf) on a report band.
 * PNG Support needs JDK 1.3 or higher. This class encapsulates an
 * ImageReference into an element.
 */

public class ImageElement extends Element
{

  /** The image to draw. */
  private ImageReference image;

  /**
   * Constructs a image element.
   */
  public ImageElement()
  {
  }

  /**
   * Sets the ImageReference for this element. The bounds of this element are adjusted according
   * to the given ImageReference.
   *
   * @param reference The image reference for this element.
   *
   * @throws NullPointerException if the reference is null.
   */
  public void setImageReference(ImageReference reference)
  {

    if (reference == null)
      throw new NullPointerException("ImageElement.setImageReference: null not allowed.");

    this.image = reference;
  }

  /**
   * Returns the image reference for this element.
   *
   * @return The image reference.
   */
  public ImageReference getImageReference()
  {

    return image;
  }

  /**
   * Draws the element at its location relative to the band co-ordinates supplied.
   *
   * @param target The target on which to print.
   * @param band The band.
   * @param bandX The x-coordinate for the element within its band.
   * @param bandY The y-coordinate for the element within its band.
   */
  public void draw(OutputTarget target, Band band) throws OutputTargetException
  {
    // set the paint...
    target.setPaint(getPaint(band));
    target.drawImage(getImageReference());
  }


  /**
   * Returns the required width of the image (in points, 1/72 inch).
   *
   * @return The width.
   */
  public float getWidth()
  {

    return (float) getBounds().getWidth();
  }

  /**
   * Returns the required height of the image (in points, 1/72 inch).
   *
   * @return the desired height of the image.
   */
  public float getHeight()
  {

    return (float) getBounds().getHeight();
  }

  /**
   * Returns the required left origin of the image.
   *
   * @return The left origin.
   */
  public float getX()
  {

    return (float) getBounds().getX();
  }

  /**
   * Returns the upper origin of the image.
   *
   * @return The upper origin.
   */
  public float getY()
  {

    return (float) getBounds().getY();
  }
}
