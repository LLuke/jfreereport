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
 * -----------------
 * ShapeContent.java
 * -----------------
 * (C)opyright 2002, 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: ShapeContent.java,v 1.7 2003/03/08 16:08:07 taqua Exp $
 *
 * Changes
 * -------
 * 03-Dec-2002 : Javadocs (DG);
 *
 */

package com.jrefinery.report.targets.base.content;

import java.awt.Shape;
import java.awt.geom.Rectangle2D;

import com.jrefinery.report.util.Log;

/**
 * A report content item that encapsulates a <code>Shape</code> object.
 *
 * @author Thomas Morgner.
 */
public class ShapeContent implements Content
{
  /** The shape. */
  private Shape shape;

  /** The bounds. */
  private Rectangle2D bounds;

  /**
   * Creates a new shape content.
   *
   * @param s  the shape.
   */
  public ShapeContent(Shape s)
  {
    this (s, s.getBounds2D());
  }

  /**
   * Creates a new shape content.
   *
   * @param s  the shape.
   * @param bounds  the bounds.
   */
  public ShapeContent(Shape s, Rectangle2D bounds)
  {
    if (s == null)
    {
      throw new NullPointerException();
    }
    if (bounds == null)
    {
      throw new NullPointerException();
    }

    Log.debug ("Created Content for   : " + s);
    Log.debug ("   widh content bounds: " + bounds);
    this.shape = s;
    this.bounds = bounds;
  }

  /**
   * Returns the content type, in this case
   * {@link com.jrefinery.report.targets.base.content.ContentType#SHAPE}.
   *
   * @return the content type.
   */
  public ContentType getContentType()
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
  public Rectangle2D getBounds()
  {
    return bounds.getBounds2D();
  }

  /**
   * Returns the minimum content size.
   *
   * @return the minimum content size.
   */
  public Rectangle2D getMinimumContentSize()
  {
    return getBounds();
  }

  /**
   * This class does not store sub-content items, so this method always returns zero.
   *
   * @return always zero, as ShapeContent does not consist of multiple content parts.
   */
  public int getContentPartCount()
  {
    return 0;
  }

  /**
   * This class does not store sub-content items, so this method always returns <code>null</code>.
   *
   * @param part  ignored.
   *
   * @return <code>null</code>.
   */
  public Content getContentPart(int part)
  {
    return null;
  }

  /**
   * Returns content that falls within the specified bounds.
   *
   * @param bounds  the bounds.
   *
   * @return the content.
   */
  public Content getContentForBounds(Rectangle2D bounds)
  {
    Rectangle2D newBounds = bounds.createIntersection(getBounds());
    return new ShapeContent(getShape(), newBounds);
  }

}

