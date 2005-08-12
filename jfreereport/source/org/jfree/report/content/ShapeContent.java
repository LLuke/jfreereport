/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
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
 * -----------------
 * ShapeContent.java
 * -----------------
 * (C)opyright 2002, 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Object Refinery Limited);
 *
 * $Id: ShapeContent.java,v 1.9 2005/06/25 17:51:57 taqua Exp $
 *
 * Changes
 * -------
 * 03-Dec-2002 : Javadocs (DG);
 *
 */

package org.jfree.report.content;

import java.awt.Shape;

import org.jfree.report.util.geom.StrictBounds;

/**
 * A report content item that encapsulates a <code>Shape</code> object. The shape
 * contained in the Content is not modified - and does not necessarily fit the given
 * bounds. If clipping is needed, it is up to the output target to transform the given
 * shape to fit the bounds.
 *
 * @author Thomas Morgner.
 */
public class ShapeContent implements Content
{
  /**
   * The shape.
   */
  private Shape shape;

  /**
   * The bounds.
   */
  private StrictBounds bounds;

  /**
   * Creates a new shape content.
   *
   * @param s      the shape.
   * @param bounds the bounds.
   */
  public ShapeContent (final Shape s, final StrictBounds bounds)
  {
    if (s == null)
    {
      throw new NullPointerException();
    }
    if (bounds == null)
    {
      throw new NullPointerException();
    }

    this.shape = s;
    this.bounds = (StrictBounds) bounds.clone();
  }

  /**
   * Returns the content type, in this case {@link org.jfree.report.content.ContentType#SHAPE}.
   *
   * @return the content type.
   */
  public ContentType getContentType ()
  {
    return ContentType.SHAPE;
  }

  /**
   * Returns the shape.
   *
   * @return the shape.
   */
  public Shape getShape ()
  {
    return shape;
  }

  /**
   * Returns the bounds for the content.
   *
   * @return the bounds.
   */
  public StrictBounds getBounds ()
  {
    return (StrictBounds) bounds.clone();
  }

  /**
   * Returns the minimum content size.
   *
   * @return the minimum content size.
   */
  public StrictBounds getMinimumContentSize ()
  {
    return getBounds();
  }

  /**
   * Returns content that falls within the specified bounds.
   *
   * @param bounds the bounds.
   * @return the content.
   */
  public Content getContentForBounds (final StrictBounds bounds)
  {
    final StrictBounds newBounds = bounds.createIntersection(getBounds());
    return new ShapeContent(getShape(), newBounds);
  }


  /**
   * Hack-Attack: Used for alignment of the content.
   *
   * @param x the x translation.
   * @param y the y translation.
   */
  public void translate (final long x, final long y)
  {
    bounds.setRect
            (bounds.getX() + x, bounds.getY() + y,
             bounds.getWidth(), bounds.getHeight());
  }
}

