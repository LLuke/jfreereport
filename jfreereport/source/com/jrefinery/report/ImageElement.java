/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
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
 * $Id: ImageElement.java,v 1.20 2002/12/02 18:23:59 taqua Exp $
 *
 * Changes:
 * --------
 * 24-Apr-2002 : Defines a reference to a Bitmap or Wmf-Image for the reports (TM);
 * 10-May-2002 : Removed all but the default constructor. Added accessor functions for all
 *               properties (TM);
 * 16-May-2002 : Added Javadoc comments (DG);
 * 16-May-2002 : using protected member m_paint instead of getter methode (JS)
 * 09-Jun-2002 : documentation
 * 27-Jun-2002 : ImageRefence is set using a DataSource
 * 17-Jul-2002 : Handled the case when ImageReference returned by getValue() is null
 */

package com.jrefinery.report;

import com.jrefinery.report.targets.style.ElementStyleSheet;

/**
 * Used to draw images (Gif, JPEG, PNG or wmf) on a report band.
 * PNG Support needs JDK 1.3 or higher. This class encapsulates an
 * ImageReference into an element.
 * <p>
 *
 * @author Thomas Morgner
 */
public class ImageElement extends Element
{
  /** A string for the content type. */
  public static final String CONTENT_TYPE = "image/generic";

  /**
   * Constructs a image element.
   */
  public ImageElement ()
  {
  }

  /**
   * Returns the content type, in this case 'image/generic'.
   *
   * @return the content type.
   */
  public String getContentType()
  {
    return CONTENT_TYPE;
  }

  /**
   * Returns true if the image should be scaled, and false otherwise.
   *
   * @return true or false.
   */
  public boolean isScale()
  {
    return getStyle().getBooleanStyleProperty(ElementStyleSheet.SCALE);
  }

  /**
   * Sets a flag that controls whether the image should be scaled to fit the element bounds.
   *
   * @param scale  the flag.
   */
  public void setScale(boolean scale)
  {
    getStyle().setStyleProperty(ElementStyleSheet.SCALE, new Boolean(scale));
  }

  /**
   * Returns true if the image's aspect ratio should be preserved, and false otherwise.
   *
   * @return true or false.
   */
  public boolean isKeepAspectRatio()
  {
    return getStyle().getBooleanStyleProperty(ElementStyleSheet.KEEP_ASPECT_RATIO);
  }

  /**
   * Sets a flag that controls whether the shape's aspect ratio should be preserved.
   *
   * @param kar the flag.
   */
  public void setKeepAspectRatio(boolean kar)
  {
    getStyle().setStyleProperty(ElementStyleSheet.KEEP_ASPECT_RATIO, new Boolean(kar));
  }
}
