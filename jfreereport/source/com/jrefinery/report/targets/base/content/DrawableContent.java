/**
 * Date: Mar 5, 2003
 * Time: 6:25:58 PM
 *
 * $Id: DrawableContent.java,v 1.1 2003/03/07 13:49:37 taqua Exp $
 */
package com.jrefinery.report.targets.base.content;

import com.jrefinery.report.DrawableContainer;
import com.jrefinery.report.util.Log;

import java.awt.geom.Rectangle2D;

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
  private Rectangle2D contentBounds;

  public DrawableContent(DrawableContainer drawable)
  {
    if (drawable == null) throw new NullPointerException();

    Log.debug ("Created Content ");
    this.drawable = drawable;
    Log.debug ("Create bounds: " + getBounds());
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
    return drawable.getClippingBounds();
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
    Rectangle2D newBounds = bounds.createIntersection(getBounds());
    DrawableContainer newContainer = new DrawableContainer(drawable,
                                                           newBounds);
    Log.debug ("Create Content for bounds: " + drawable);
    return new DrawableContent(newContainer);
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
