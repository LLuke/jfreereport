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
 * $Id: DrawableContent.java,v 1.7 2005/01/24 23:58:14 taqua Exp $
 *
 * Changes
 * -------
 * 09-Apr-2003 : Added standard header (DG);
 *
 */
package org.jfree.report.content;

import java.awt.geom.Dimension2D;
import java.awt.geom.Rectangle2D;

import org.jfree.ui.Drawable;
import org.jfree.util.ShapeUtilities;

/**
 * A simple wrapper around the DrawableContainer. The ContentImplementation
 * is able to adjust the Clipping Bounds of the DrawableContainer.
 *
 * @author Thomas Morgner
 */
public strictfp class DrawableContent implements Content
{
  /**
   * The drawable content. The content will be drawn using the drawable bounds,
   * but only the rectangle contentBounds will be visible.
   */
  private Drawable drawable;


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
  public DrawableContent(final Drawable ref,
                         final Rectangle2D bounds)
  {
    this (ref, bounds, new Rectangle2D.Float
            (0, 0, (float) bounds.getWidth(), (float) bounds.getHeight()));
  }

  /**
   * Creates a new image content.
   *
   * @param ref  the image reference.
   * @param bounds  the content bounds.
   */
  protected DrawableContent(final Drawable ref,
                         final Rectangle2D bounds,
                         final Rectangle2D imageArea)
  {
    if (ref == null)
    {
      throw new NullPointerException("ImageContainer must not be null for ImageContent.");
    }
    this.drawable = ref;
    this.bounds = bounds;
    this.imageArea = imageArea;

  }


  /**
   * Returns the content type (the types include <code>TEXT</code>, <code>IMAGE</code>,
   * <code>SHAPE</code> and <code>CONTAINER</code>).
   *
   * @return the content type.
   */
  public ContentType getContentType()
  {
    return ContentType.DRAWABLE;
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
    return new DrawableContent(drawable, myBounds,
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
   * Returns the minimum content size.
   *
   * @return the minimum size.
   */
  public Rectangle2D getMinimumContentSize()
  {
    return getBounds();
  }

  /**
   * Returns the content.
   *
   * @return The content.
   */
  public Drawable getContent()
  {
    return drawable;
  }

  public Dimension2D getDrawableSize ()
  {
    return null;
  }
}
