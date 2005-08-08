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
 * -----------------
 * ImageContent.java
 * -----------------
 * (C)opyright 2002, 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: ImageContent.java,v 1.15 2005/06/25 17:51:57 taqua Exp $
 *
 * Changes
 * -------
 * 03-Dec-2002 : Javadocs (DG);
 * 07-Feb-2003 : Moved from package com.jrefinery.report.targets.pageable.content.
 *
 */
package org.jfree.report.content;

import org.jfree.report.ImageContainer;
import org.jfree.report.util.geom.StrictBounds;
import org.jfree.report.util.geom.StrictGeomUtility;

/**
 * The Image content is a wrapper around a java.awt.Image object. The content defines
 * a viewport (a clipping area) over the contained image.
 *
 * @author Thomas Morgner.
 */
public class ImageContent implements Content
{
  /**
   * The image reference.
   */
  private final ImageContainer reference;

  /**
   * The bounds.
   */
  private final StrictBounds bounds;

  /**
   * The bounds of the displayed area of the image (unscaled).
   */
  private final StrictBounds imageArea;

  /**
   * Creates a new image content.
   *
   * @param ref    the image reference.
   * @param bounds the content bounds.
   */
  public ImageContent (final ImageContainer ref,
                       final StrictBounds bounds)
  {
    this(ref, bounds, StrictGeomUtility.createBounds
            (0, 0, ref.getImageWidth(), ref.getImageHeight()));
  }

  /**
   * Creates a new image content.
   *
   * @param ref    the image reference.
   * @param bounds the content bounds.
   * @param imageArea the displayed area of the image.
   */
  protected ImageContent (final ImageContainer ref,
                          final StrictBounds bounds,
                          final StrictBounds imageArea)
  {
    if (ref == null)
    {
      throw new NullPointerException("ImageContainer must not be null for ImageContent.");
    }
    if (bounds.getWidth() == 0)
    {
      throw new IllegalArgumentException("A bounds width of zero is not allowed");
    }
    if (bounds.getHeight() == 0)
    {
      throw new IllegalArgumentException("A bounds height of zero is not allowed");
    }
    if (imageArea.getWidth() == 0)
    {
      throw new IllegalArgumentException("An image width of zero is not allowed");
    }
    if (imageArea.getHeight() == 0)
    {
      throw new IllegalArgumentException("An image height of zero is not allowed");
    }
    this.reference = ref;
    this.bounds = (StrictBounds) bounds.clone();
    this.imageArea = (StrictBounds) imageArea.clone();

  }

  /**
   * Returns the content type, in this case {@link org.jfree.report.content.ContentType#IMAGE}.
   *
   * @return the content type.
   */
  public ContentType getContentType ()
  {
    return ContentType.IMAGE;
  }

  /**
   * Returns the content bounds.
   *
   * @return the content bounds.
   */
  public StrictBounds getBounds ()
  {
    return (StrictBounds) bounds.clone();
  }

  /**
   * Returns content that falls within the specified bounds.
   *
   * @param bounds the bounds.
   * @return the content.
   */
  public Content getContentForBounds (final StrictBounds bounds)
  {
    if (StrictBounds.intersects(bounds, this.bounds) == false)
    {
      return EmptyContent.getDefaultEmptyContent();
    }

    final StrictBounds myBounds = bounds.createIntersection(this.bounds);
    final StrictBounds imageArea = new StrictBounds
                        (mapHorizontalPointToImage(myBounds.getX() - this.bounds.getX()),
                         mapVerticalPointToImage(myBounds.getY() - this.bounds.getY()),
                         mapHorizontalPointToImage(myBounds.getWidth()),
                         mapVerticalPointToImage(myBounds.getHeight()));
    return new ImageContent(reference, myBounds, imageArea);
  }

  /**
   * Maps the given horizontal coordinate into the unscaled image.
   *
   * @param px the horizontal coordinate
   * @return the scaled coordinate.
   */
  private long mapHorizontalPointToImage (final long px)
  {
    return (px * imageArea.getWidth() / bounds.getWidth());
  }

  /**
   * Maps the given vertical coordinate into the unscaled image.
   *
   * @param px the vertical coordinate
   * @return the scaled coordinate.
   */
  private long mapVerticalPointToImage (final long px)
  {
    return (px * imageArea.getHeight() / bounds.getHeight());
  }

  /**
   * Returns the image area for this image content. The image area is a view on the
   * unscaled image contained in the content. This is used to split the image
   * content into subcontents if necessary.
   *
   * @return the image area.
   */
  public StrictBounds getImageArea ()
  {
    return (StrictBounds) imageArea.clone();
  }

  /**
   * Returns the image contents.
   *
   * @return the image.
   */
  public ImageContainer getContent ()
  {
    return reference;
  }

  /**
   * Returns the minimum content size.
   *
   * @return the minimum content size.
   */
  public StrictBounds getMinimumContentSize ()
  {
    return getBounds();
  }
}
