/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
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
 * ------------
 * Content.java
 * ------------
 * (C)opyright 2002, 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: Content.java,v 1.1 2003/01/29 03:13:01 taqua Exp $
 *
 * Changes
 * -------
 * 03-Dec-2002 : Javadocs (DG);
 *
 */

package com.jrefinery.report.targets.base.content;

import java.awt.geom.Rectangle2D;

/**
 * An interface for report content.
 *
 * @author Thomas Morgner.
 */
public interface Content
{
  /**
   * Returns the content type (the types include <code>TEXT</code>, <code>IMAGE</code>,
   * <code>SHAPE</code> and <code>CONTAINER</code>).
   *
   * @return the content type.
   */
  public ContentType getContentType();

  /**
   * Returns the bounds for the content.
   *
   * @return the bounds.
   */
  public Rectangle2D getBounds();

  /**
   * Returns the minimum content size.
   *
   * @return the minimum size.
   */
  public Rectangle2D getMinimumContentSize();

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
  public Content getContentForBounds (Rectangle2D bounds);

  /**
   * Returns the number of sub-content items for this item.
   * <P>
   * Only subclasses of {@link com.jrefinery.report.targets.base.content.ContentContainer} will 
   * return non-zero results.
   *
   * @return the number of sub-content items.
   */
  public int getContentPartCount ();

  /**
   * Returns a sub-content item.
   *
   * @param part  the sub-content index (zero-based).
   *
   * @return the subcontent (possibly null).
   */
  public Content getContentPart (int part);

}
