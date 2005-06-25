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
 * --------------------
 * DrawableContent.java
 * --------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Object Refinery Limited);
 *
 * $Id: DrawableContent.java,v 1.10 2005/02/23 21:04:36 taqua Exp $
 *
 * Changes
 * -------
 * 09-Apr-2003 : Added standard header (DG);
 *
 */
package org.jfree.report.content;

import org.jfree.report.util.geom.StrictBounds;
import org.jfree.ui.Drawable;

/**
 * A simple wrapper around the DrawableContainer. The ContentImplementation is able to
 * adjust the Clipping Bounds of the DrawableContainer.
 *
 * @author Thomas Morgner
 */
public class DrawableContent implements Content
{
  /**
   * The drawable content. The content will be drawn using the drawable bounds, but only
   * the rectangle contentBounds will be visible.
   */
  private Drawable drawable;

  /**
   * The content bounds.
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
  public DrawableContent (final Drawable ref,
                          final StrictBounds bounds)
  {
    this(ref, bounds, new StrictBounds
            (0, 0, bounds.getWidth(), bounds.getHeight()));
  }

  /**
   * Creates a new image content.
   *
   * @param ref    the image reference.
   * @param bounds the content bounds.
   */
  protected DrawableContent (final Drawable ref,
                             final StrictBounds bounds,
                             final StrictBounds imageArea)
  {
    if (ref == null)
    {
      throw new NullPointerException("ImageContainer must not be null for ImageContent.");
    }
    if (bounds == null)
    {
      throw new NullPointerException("Bounds must not be null");
    }
    if (imageArea == null)
    {
      throw new NullPointerException("ImageArea must not be null.");
    }
    this.drawable = ref;
    this.bounds = bounds;
    this.imageArea = imageArea;

  }


  /**
   * Returns the content type ContentType.DRAWABLE.
   *
   * @return the content type.
   */
  public ContentType getContentType ()
  {
    return ContentType.DRAWABLE;
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
    return new DrawableContent(drawable, myBounds, imageArea);
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
   * Returns the image area for this drawable content. The image area is a view on the
   * unscaled drawable contained in the content. This is used to split the drawable
   * content into subcontents if necessary.
   *
   * @return the image area.
   */
  public StrictBounds getImageArea ()
  {
    return (StrictBounds) imageArea.clone();
  }

  /**
   * Returns the minimum content size.
   *
   * @return the minimum size.
   */
  public StrictBounds getMinimumContentSize ()
  {
    return getBounds();
  }

  /**
   * Returns the content.
   *
   * @return The content.
   */
  public Drawable getContent ()
  {
    return drawable;
  }
}
