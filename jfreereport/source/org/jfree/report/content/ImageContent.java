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
 * $Id: ImageContent.java,v 1.5 2003/09/13 15:14:40 taqua Exp $
 *
 * Changes
 * -------
 * 03-Dec-2002 : Javadocs (DG);
 * 07-Feb-2003 : Moved from package com.jrefinery.report.targets.pageable.content.
 *
 */
package org.jfree.report.content;

import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.Image;

import org.jfree.report.ImageContainer;
import org.jfree.report.DefaultImageReference;
import org.jfree.util.ShapeUtilities;
import org.jfree.util.Log;
import org.jfree.base.BaseBoot;

/**
 * Image content.
 *
 * @author Thomas Morgner.
 */
public strictfp class ImageContent implements Content
{
  /** The image reference. */
  private final ImageContainer reference;

  /** The bounds. */
  private final Rectangle2D bounds;

  /** The bounds of the displayed area of the image (unscaled). */
  private final Rectangle2D imageArea;

  /**
   * Creates a new image content.
   *
   * @param ref  the image reference.
   * @param bounds  the content bounds.
   */
  public ImageContent(final ImageContainer ref,
                      final Rectangle2D bounds)
  {
    this (ref, bounds, new Rectangle2D.Float
            (0, 0, ref.getImageWidth(), ref.getImageHeight()));
  }

  /**
   * Creates a new image content.
   *
   * @param ref  the image reference.
   * @param bounds  the content bounds.
   */
  protected ImageContent(final ImageContainer ref,
                         final Rectangle2D bounds,
                         final Rectangle2D imageArea)
  {
    if (ref == null)
    {
      throw new NullPointerException("ImageContainer must not be null for ImageContent.");
    }
    this.reference = ref;
    this.bounds = bounds;
    this.imageArea = imageArea;

  }

  /**
   * Returns the content type, in this case
   * {@link org.jfree.report.content.ContentType#IMAGE}.
   *
   * @return the content type.
   */
  public ContentType getContentType()
  {
    return ContentType.IMAGE;
  }

  /**
   * This class does not store sub-content items, so this method always returns zero.
   *
   * @return always zero, image content does never contains multiple parts.
   */
  public int getContentPartCount()
  {
    return 0;
  }

  /**
   * This class does not store sub-content items, so this method always returns <code>null</code>.
   *
   * @param part  ignored.
   *
   * @return <code>null</code>.
   */
  public Content getContentPart(final int part)
  {
    return null;
  }

  /**
   * Returns the content bounds.
   *
   * @return the content bounds.
   */
  public Rectangle2D getBounds()
  {
    return bounds.getBounds2D();
  }

  /**
   * Returns content that falls within the specified bounds.
   *
   * @param bounds  the bounds.
   *
   * @return the content.
   */
  public Content getContentForBounds(final Rectangle2D bounds)
  {
    if (ShapeUtilities.intersects(bounds, this.bounds) == false)
    {
      return new EmptyContent();
    }

    final Rectangle2D myBounds = bounds.createIntersection(this.bounds);
    return new ImageContent(reference, myBounds,
            new Rectangle2D.Float
                    (mapHorizontalPointToImage(myBounds.getX() - this.bounds.getX()),
                     mapVerticalPointToImage(myBounds.getY() - this.bounds.getY()),
                     mapHorizontalPointToImage(myBounds.getWidth()),
                     mapVerticalPointToImage(myBounds.getHeight())));
  }

  private float mapHorizontalPointToImage (final double px)
  {
    return (float) (px * imageArea.getWidth() / bounds.getWidth());
  }

  private float mapVerticalPointToImage (final double px)
  {
    return (float) (px * imageArea.getHeight() / bounds.getHeight());
  }

  public Rectangle2D getImageArea ()
  {
    return imageArea.getBounds2D();
  }

  /**
   * Returns the image contents.
   *
   * @return the image.
   */
  public ImageContainer getContent()
  {
    return reference;
  }

  /**
   * Returns the minimum content size.
   *
   * @return the minimum content size.
   */
  public Rectangle2D getMinimumContentSize()
  {
    return getBounds();
  }

  public static void main (final String[] args)
  {
    BaseBoot.getInstance().start();

    final Image img = new BufferedImage
            (400, 400, BufferedImage.TYPE_3BYTE_BGR);
    final Rectangle2D.Float rec = new Rectangle2D.Float(0, 0, 100, 100);
    final ImageContent ic = new ImageContent
            (new DefaultImageReference(img), rec);

    final Rectangle2D.Float rec2 = new Rectangle2D.Float(50, 50, 50, 50);
    final ImageContent ic2 = (ImageContent) ic.getContentForBounds(rec2);
    Log.debug (ic2.imageArea);

    final Rectangle2D.Float rec3 = new Rectangle2D.Float(75, 75, 25, 25);
    final ImageContent ic3 = (ImageContent) ic2.getContentForBounds(rec3);
    Log.debug (ic3.imageArea);
  }
}
