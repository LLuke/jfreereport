/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
 *
 * (C) Copyright 2000-2002, by Simba Management Limited and Contributors.
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
 * (C)opyright 2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: Content.java,v 1.1 2002/12/02 17:56:54 taqua Exp $
 *
 * Changes
 * -------
 * 03-Dec-2002 : Javadocs (DG);
 *
 */

package com.jrefinery.report.targets.pageable.contents;

import java.awt.geom.Rectangle2D;

/**
 * An interface for report content.
 * <p>
 * Classes that implement this interface include:  ContentContainer, TextLine, ImageContent.
 *
 * @author Thomas Morgner.
 */
public interface Content
{
  /**
   * Returns a subcontent item.
   *
   * @param part  the subcontent index (zero-based).
   *
   * @return the subcontent (possibly null).
   */
  public Content getContentPart (int part);
  
  /**
   * Returns the number of items of "subcontent" for this content item.
   *
   * @return the count.
   */
  public int getContentPartCount ();

  /**
   * Returns the bounds for the content.
   *
   * @return the bounds.
   */
  public Rectangle2D getBounds();

  /**
   * Returns the content type.
   *
   * @return the content type.
   */
  public ContentType getContentType();
  
  /**
   * Returns the content for the given bounds.
   *
   * @return the content (possibly null).
   */
  public Content getContentForBounds (Rectangle2D bounds);

  /**
   * Returns the minimum content size.
   *
   * @return the minimum size.
   */
  public Rectangle2D getMinimumContentSize();

}
