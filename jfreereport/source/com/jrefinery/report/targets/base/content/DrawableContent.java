/**
 * Date: Mar 5, 2003
 * Time: 6:25:58 PM
 *
 * $Id$
 */
package com.jrefinery.report.targets.base.content;

import com.jrefinery.ui.Drawable;

import java.awt.geom.Rectangle2D;

public class DrawableContent implements Content
{
  /**
   * The bounds for that part of the content that should be painted. This can
   * be a sub-rectangle of the original bounds.
   */
  private Rectangle2D contentBounds;

  /**
   * The original bounds of the complete drawable. The drawable is already scaled,
   * if external scaling must be applied.
   */
  private Rectangle2D drawableBounds;

  /**
   * The drawable content. The content will be drawn using the drawable bounds,
   * but only the rectangle contentBounds will be visible.
   */
  private Drawable drawable;

  public DrawableContent(Drawable drawable, Rectangle2D drawableBounds, Rectangle2D contentBounds)
  {
    if (drawable == null) throw new NullPointerException();
    if (drawableBounds == null) throw new NullPointerException();
    if (contentBounds == null) throw new NullPointerException();

    this.drawable = drawable;
    this.drawableBounds = drawableBounds;
    this.contentBounds = contentBounds;
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
    return contentBounds.getBounds2D();
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
    return new DrawableContent(drawable, drawableBounds, newBounds);
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
}
