package org.jfree.report.content;

import org.jfree.report.Anchor;
import org.jfree.report.util.geom.StrictBounds;
import org.jfree.report.util.geom.StrictPoint;

public class AnchorContent implements Content
{
  private Anchor ancor;
  private StrictPoint point;

  public AnchorContent (final Anchor ancor, final StrictPoint point)
  {
    this.ancor = ancor;
    this.point = (StrictPoint) point.clone();
  }

  /**
   * Returns the bounds for the content. ContentBounds are always relative to the element
   * bounds.
   *
   * @return the bounds.
   */
  public StrictBounds getBounds ()
  {
    return new StrictBounds(point.getX(), point.getY(), 0, 0);
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
  public Content getContentForBounds (final StrictBounds bounds)
  {
    if (bounds.contains(point.getX(), point.getY()))
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
  public StrictBounds getMinimumContentSize ()
  {
    return getBounds();
  }

  public Anchor getAnchor ()
  {
    return ancor;
  }
}
