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
 * $Id: EmptyContent.java,v 1.2 2004/03/27 20:21:14 taqua Exp $
 *
 * Changes 
 * -------------------------
 * 14.02.2004 : Initial version
 *  
 */

package org.jfree.report.content;

import java.awt.geom.Rectangle2D;

public class EmptyContent implements Content
{
  private static EmptyContent singleton;

  public static EmptyContent getDefaultEmptyContent()
  {
    if (singleton == null)
    {
      singleton = new EmptyContent();
    }
    return singleton;
  }
  public EmptyContent()
  {
  }

  /**
   * Returns the bounds for the content.
   *
   * @return the bounds.
   */
  public Rectangle2D getBounds()
  {
    return new Rectangle2D.Float();
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
  public Content getContentForBounds(final Rectangle2D bounds)
  {
    return null;
  }

  /**
   * Returns the content type, in this case
   * {@link org.jfree.report.content.ContentType#CONTAINER}.
   *
   * @return the content type.
   */
  public ContentType getContentType()
  {
    return ContentType.CONTAINER;
  }

  /**
   * Returns the minimum content size.
   *
   * @return the minimum size.
   */
  public Rectangle2D getMinimumContentSize()
  {
    return new Rectangle2D.Float();
  }
}