/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
 *
 * (C) Copyright 2000-2004, by Object Refinery Limited and Contributors.
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
 * ------------------------------
 * RawContent.java
 * ------------------------------
 * (C)opyright 2004, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Object Refinery Limited);
 *
 * $Id: RawContent.java,v 1.1 2004/03/16 15:43:41 taqua Exp $
 *
 * Changes 
 * -------------------------
 * 09.03.2004 : Initial version
 *  
 */

package org.jfree.report.modules.output.table.base;

import org.jfree.report.content.Content;
import org.jfree.report.content.ContentType;
import java.awt.geom.Rectangle2D;

public class RawContent implements Content
{
  private Rectangle2D bounds;
  private Object content;

  /**
   * Creates a new RawContent instance.
   * @param bounds the bounds, relative to the element bounds
   * @param content the content that should be encapsulated.
   */
  public RawContent (final Rectangle2D bounds, final Object content)
  {
    this.bounds = bounds.getBounds2D();
    this.content = content;
  }

  /**
   * Returns the bounds for the content.
   *
   * @return the bounds.
   */
  public Rectangle2D getBounds ()
  {
    return bounds.getBounds2D();
  }

  /**
   * Returns the content for the given bounds. The extracted content is the content that would be displayed in the
   * specific bounds if the content would be printed with clipping enabled at the given boundary.
   * <p/>
   * This method returns <code>null</code> if there is no content in these bounds.
   *
   * @param bounds the bounds.
   * @return the content (possibly <code>null</code>).
   */
  public Content getContentForBounds (final Rectangle2D bounds)
  {
    return null;
  }

  /**
   * Returns a sub-content item.
   *
   * @param part the sub-content index (zero-based).
   * @return the subcontent (possibly null).
   */
  public Content getContentPart (final int part)
  {
    return null;
  }

  /**
   * Returns the number of sub-content items for this item. <P> Only subclasses of {@link
   * org.jfree.report.content.ContentContainer} will return non-zero results.
   *
   * @return the number of sub-content items.
   */
  public int getContentPartCount ()
  {
    return 0;
  }

  /**
   * Returns the content type (the types include <code>TEXT</code>, <code>IMAGE</code>, <code>SHAPE</code> and
   * <code>CONTAINER</code>).
   *
   * @return the content type.
   */
  public ContentType getContentType ()
  {
    return ContentType.RAW;
  }

  /**
   * Returns the minimum content size.
   *
   * @return the minimum size.
   */
  public Rectangle2D getMinimumContentSize ()
  {
    return bounds.getBounds2D();
  }

  public Object getContent ()
  {
    return content;
  }
}