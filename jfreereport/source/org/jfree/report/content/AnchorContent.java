/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2005, by Object Refinery Limited and Contributors.
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
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * ------------
 * AnchorContent.java
 * ------------
 * (C) Copyright 2002-2005, by Object Refinery Limited.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: AnchorContent.java,v 1.3 2005/03/03 22:59:58 taqua Exp $
 *
 * Changes
 * -------
 * 25-Jul-2005: Added JavaDoc comments.
 *
 */
package org.jfree.report.content;

import org.jfree.report.Anchor;
import org.jfree.report.util.geom.StrictBounds;
import org.jfree.report.util.geom.StrictPoint;

/**
 * The Anchor content encapsulates possible link targets. Anchors are bound to
 * a certain point on the document and can be referenced using HREF's.
 *
 * @author Thomas Morgner
 * @see Anchor
 */
public class AnchorContent implements Content
{
  /** The encapsulated anchor. */
  private Anchor ancor;
  /** The anchors position. */
  private StrictPoint point;

  /**
   * Creates a new Anchor content for the given anchor and position.
   *
   * @param ancor the anchor
   * @param point the position of the anchor within the page/document.
   */
  public AnchorContent (final Anchor ancor, final StrictPoint point)
  {
    if (ancor == null)
    {
      throw new NullPointerException("Anchor must not be null.");
    }
    if (point == null)
    {
      throw new NullPointerException("Position must not be null");
    }
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
    return new StrictBounds(point.getX(), point.getY(), 1, 1);
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

  /**
   * Returns the anchor contained in this content.
   *
   * @return the anchor.
   */
  public Anchor getAnchor ()
  {
    return ancor;
  }
}
