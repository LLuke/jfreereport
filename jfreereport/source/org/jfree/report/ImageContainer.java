/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
 *
 * (C) Copyright 2000-2004, by Object Refinery Limited and Contributors.
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
 * ImageContainer.java
 * ------------------------------
 * (C)opyright 2004, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Object Refinery Limited);
 *
 * $Id: ImageContainer.java,v 1.5 2005/02/23 21:04:29 taqua Exp $
 *
 * Changes
 * -------------------------
 * 09-Feb-2004 : Initial version
 *
 */
package org.jfree.report;

/**
 * A image container stores all layout information to process images in a report.
 * <p/>
 * The ImageContainer is the common base interface for the URLImageContainer (which
 * references remote images) and the LocalImageContainer (which references local AWT-Image
 * instances).
 * <p/>
 * All the layouting engine needs to know about images, are the image dimensions and the
 * possible scale factor for the contained image. Only the content creators need the
 * knowledge on how to access the contained image and and which other container types
 * might exist.
 *
 * @author Thomas Morgner
 */
public interface ImageContainer extends Cloneable
{
  /**
   * Returns the unscaled width of the contained image. The width must be known during the
   * layouting process, returning -1 to indicate an unknown size (as the AWT does) is not
   * valid.
   *
   * @return the width of the image.
   */
  public int getImageWidth ();

  /**
   * Returns the unscaled height of the contained image. The height must be known during
   * the layouting process, returning -1 to indicate an unknown size (as the AWT does) is
   * not valid.
   *
   * @return the height of the image.
   */
  public int getImageHeight ();

  /**
   * Defines the image's horizontal scale. This is the factor to convert the image from
   * it's original resolution to the java resolution of 72dpi.
   * <p/>
   * This is not the scale that is computed by the layouter; that one is derived from the
   * ImageContent itself. 
   *
   * @return the horizontal scale.
   */
  public float getScaleX ();

  /**
   * Defines the image's vertical scale. This is the factor to convert the image from it's
   * original resolution to the java resolution of 72dpi.
   * <p/>
   * This is not the scale that is computed by the layouter; that one is derived from the
   * ImageContent.
   *
   * @return the vertical scale.
   */
  public float getScaleY ();
}
