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
 * --------------------
 * DrawableContent.java
 * --------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: DrawableContent.java,v 1.2 2003/08/24 15:13:21 taqua Exp $
 *
 * Changes
 * -------
 * 09-Apr-2003 : Added standard header (DG);
 *
 */
package org.jfree.report.content;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import org.jfree.report.DrawableContainer;

/**
 * A simple wrapper around the DrawableContainer. The ContentImplementation
 * is able to adjust the Clipping Bounds of the DrawableContainer.
 *
 * @author Thomas Morgner
 */
public class DrawableContent implements Content
{
  /**
   * The drawable content. The content will be drawn using the drawable bounds,
   * but only the rectangle contentBounds will be visible.
   */
  private DrawableContainer drawable;

  /**
   * The content bounds define the position of this content in the global
   * coordinate space (where to print on the page).
   */
  private Point2D contentOrigin;

  /**
   * Creates a new instance.
   *
   * @param drawable  the drawable object.
   * @param contentOrigin  the origin.
   */
  public DrawableContent(final DrawableContainer drawable, final Point2D contentOrigin)
  {
    if (drawable == null)
    {
      throw new NullPointerException();
    }

    this.drawable = drawable;
    this.contentOrigin = contentOrigin;
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
   * Returns the bounds for the content.
   *
   * @return the bounds.
   */
  public Rectangle2D getBounds()
  {
    final Rectangle2D clippBounds = drawable.getClippingBounds();
    clippBounds.setRect(contentOrigin.getX(),
        contentOrigin.getY(),
        clippBounds.getWidth(),
        clippBounds.getHeight());
    return clippBounds;
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
   * Returns the content for the given bounds. The extracted content is the content
   * that would be displayed in the specific bounds if the content would be printed
   * with clipping enabled at the given boundary.
   * <p>
   * This method returns <code>null</code> if there is no content in these bounds.
   *
   * @param bounds  the bounds.
   *
   * @return the content (possibly <code>null</code>).
   */
  public Content getContentForBounds(final Rectangle2D bounds)
  {
    final Rectangle2D myBounds = getBounds();

    if (bounds.intersects(myBounds) == false)
    {
      return null;
    }
    final Rectangle2D newBounds = bounds.createIntersection(myBounds);
    final Rectangle2D clipBounds
        = new Rectangle2D.Float((float) (newBounds.getX() - contentOrigin.getX()),
            (float) (newBounds.getY() - contentOrigin.getY()),
            (float) newBounds.getWidth(),
            (float) newBounds.getHeight());
    final DrawableContainer newContainer = new DrawableContainer(drawable,
        clipBounds);

    return new DrawableContent(newContainer, new Point2D.Float((float) newBounds.getX(),
        (float) newBounds.getY()));
  }

  /**
   * Returns the number of sub-content items for this item.
   * <P>
   * Only subclasses of {@link ContentContainer} will
   * return non-zero results.
   *
   * @return the number of sub-content items.
   */
  public int getContentPartCount()
  {
    return 0;
  }

  /**
   * Returns a sub-content item.
   *
   * @param part  the sub-content index (zero-based).
   *
   * @return the subcontent (possibly null).
   */
  public Content getContentPart(final int part)
  {
    return null;
  }

  /**
   * Returns the content.
   *
   * @return The content.
   */
  public DrawableContainer getContent()
  {
    return drawable;
  }
}
