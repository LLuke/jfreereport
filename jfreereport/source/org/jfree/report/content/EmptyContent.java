/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
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
 * ------------------------------
 * EmptyContent.java
 * ------------------------------
 * (C)opyright 2004, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Object Refinery Limited);
 *
 * $Id: EmptyContent.java,v 1.6 2005/02/23 21:04:37 taqua Exp $
 *
 * Changes 
 * -------------------------
 * 14-Feb-2004 : Initial version
 * 26-Jun-2005 : Added JavaDoc
 */

package org.jfree.report.content;

import org.jfree.report.util.geom.StrictBounds;

/**
 * The empty content is a place holder for all cases, where no other content
 * should be displayed.
 */
public class EmptyContent implements Content
{
  /** The singleton instance for all empty contents. */
  private static EmptyContent singleton;

  /**
   * Returns the singleton instance of the empty content.
   *
   * @return the singleton instance.
   */
  public static EmptyContent getDefaultEmptyContent ()
  {
    if (singleton == null)
    {
      singleton = new EmptyContent();
    }
    return singleton;
  }

  /**
   * Private constructor prevents object creation. 
   */
  private EmptyContent ()
  {
  }

  /**
   * Returns the bounds for the content.
   *
   * @return the bounds.
   */
  public StrictBounds getBounds ()
  {
    return new StrictBounds();
  }

  /**
   * Returns the content for the given bounds. As this is Empty content, the content
   * returns itself, no matter what bounds are given.
   *
   * @param bounds the bounds.
   * @return the content
   */
  public Content getContentForBounds (final StrictBounds bounds)
  {
    return this;
  }

  /**
   * Returns a sub-content item. This content has no subcontent, therefore an
   * IndexOutOfBoundsException is thrown.
   *
   * @param part the sub-content index (zero-based).
   * @return the subcontent (possibly null).
   */
  public Content getContentPart (final int part)
  {
    throw new IndexOutOfBoundsException("EmptyContent has no subcontent.");
  }

  /**
   * Returns the number of sub-content items for this item.
   *
   * @return zero.
   */
  public int getContentPartCount ()
  {
    return 0;
  }

  /**
   * Returns the content type, in this case {@link org.jfree.report.content.ContentType#CONTAINER}.
   *
   * @return the content type.
   */
  public ContentType getContentType ()
  {
    return ContentType.CONTAINER;
  }

  /**
   * Returns the minimum content size.
   *
   * @return the minimum size.
   */
  public StrictBounds getMinimumContentSize ()
  {
    return new StrictBounds();
  }
}
