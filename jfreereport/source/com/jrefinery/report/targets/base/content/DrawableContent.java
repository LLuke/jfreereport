/**
 * Date: Mar 5, 2003
 * Time: 6:25:58 PM
 *
 * $Id: DrawableContent.java,v 1.2 2003/03/07 16:56:01 taqua Exp $
 */
package com.jrefinery.report.targets.base.content;

import com.jrefinery.report.DrawableContainer;
import com.jrefinery.report.util.Log;

import java.awt.geom.Rectangle2D;
import java.awt.geom.Point2D;

/**
 * A simple wrapper around the DrawableContainer. The ContentImplementation
 * is able to adjust the Clipping Bounds of the DrawableContainer.
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

  public DrawableContent(DrawableContainer drawable, Point2D contentOrigin)
  {
    if (drawable == null) throw new NullPointerException();

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
    Rectangle2D clippBounds = drawable.getClippingBounds();
    return new Rectangle2D.Float((float) contentOrigin.getX(),
                                 (float) contentOrigin.getY(),
                                 (float) clippBounds.getWidth(),
                                 (float) clippBounds.getHeight());
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
  public Content getContentForBounds(Rectangle2D bounds)
  {
    Rectangle2D myBounds = getBounds();

    if (bounds.intersects(myBounds) == false)
      return null;

    Rectangle2D newBounds = bounds.createIntersection(myBounds);
    Rectangle2D clipBounds = new Rectangle2D.Float((float) (newBounds.getX() - contentOrigin.getX()),
                                                   (float) (newBounds.getY() - contentOrigin.getY()),
                                                   (float) newBounds.getWidth(),
                                                   (float) newBounds.getHeight());
    DrawableContainer newContainer = new DrawableContainer(drawable,
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
  public Content getContentPart(int part)
  {
    return null;
  }

  public DrawableContainer getContent()
  {
    return drawable;
  }
}
