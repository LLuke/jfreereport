package org.jfree.report.content;

import java.awt.geom.Rectangle2D;

import org.jfree.report.Anchor;

public class AnchorContent implements Content
{
  private Anchor ancor;
  private float x;
  private float y;

  public AnchorContent (final Anchor ancor, final float x, final float y)
  {
    this.ancor = ancor;
    this.x = x;
    this.y = y;
  }

  /**
   * Returns the bounds for the content. ContentBounds are always relative to the element
   * bounds.
   *
   * @return the bounds.
   */
  public Rectangle2D getBounds ()
  {
    return new Rectangle2D.Float(x, y, 0, 0);
  }

  /**
   * Returns the content for the given bounds. The extracted content is the content that
   * would be displayed in the specific bounds if the content would be printed with
   * clipping enabled at the given boundary.
   * <p/>
   * This method returns <code>null</code> if there is no content in these bounds.
   *
   * @param bounds the bounds.
   * @return the content (possibly <code>null</code>).
   */
  public Content getContentForBounds (final Rectangle2D bounds)
  {
    if (bounds.contains(x, y))
    {
      return this;
    }
    return null;
  }

  /**
   * Returns the content type (the types include <code>TEXT</code>, <code>IMAGE</code>,
   * <code>SHAPE</code> and <code>CONTAINER</code>).
   *
   * @return the content type.
   */
  public ContentType getContentType ()
  {
    return ContentType.ANCHOR;
  }

  /**
   * Returns the minimum content size.
   *
   * @return the minimum size.
   */
  public Rectangle2D getMinimumContentSize ()
  {
    return getBounds();
  }

  public Anchor getAnchor ()
  {
    return ancor;
  }
}
