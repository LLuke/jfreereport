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
 * ----------------------
 * DrawableContainer.java
 * ----------------------
 * (C)opyright 2003, by Object Refinery Limited and Contributors.
 *
 * Original Author:  David Gilbert (for Object Refinery Limited);
 * Contributor(s):   Thomas Morgner;
 *
 * $Id: DrawableContainer.java,v 1.1 2003/07/07 22:43:59 taqua Exp $
 *
 * Changes
 * -------
 * 04-Mar-2003 : Initial version
 */

package org.jfree.report;

import java.awt.geom.Dimension2D;
import java.awt.geom.Rectangle2D;

import org.jfree.ui.Drawable;

/**
 * Wrapper for the drawable object, assigns content bounds and clipping bounds
 * to the drawable.
 * <p>
 * The drawable dimensions define the size of the whole drawable object. These
 * dimensions are logical anchors to define a logical coordinate space for the
 * content bounds.
 * <p>
 * The clipping bounds define the clipping area of the drawable object.
 *
 * @author Thomas Morgner
 */
public class DrawableContainer
{
  /** The drawable object. */
  private Drawable drawable;

  /** The size for drawing. */
  private Dimension2D drawableSize;

  /** The clipping bounds. */
  private Rectangle2D clippingBounds;

  /**
   * Creates a new container.
   *
   * @param container  the container.
   * @param clippingBounds  the clipping bounds.
   */
  public DrawableContainer(final DrawableContainer container, final Rectangle2D clippingBounds)
  {
    this(container.getDrawable(), container.getDrawableSize(), clippingBounds);
  }

  /**
   * Creates a new container.
   *
   * @param drawable  the drawable object.
   * @param drawableSize  the size.
   * @param clippingBounds  the clipping region.
   */
  public DrawableContainer(final Drawable drawable, final Dimension2D drawableSize, final Rectangle2D clippingBounds)
  {
    this.drawable = drawable;
    this.drawableSize = drawableSize;
    this.clippingBounds = clippingBounds;
  }

  /**
   * Returns the drawable object.
   *
   * @return The drawable object.
   */
  public Drawable getDrawable()
  {
    return drawable;
  }

  /**
   * Returns the drawable size.
   *
   * @return The size.
   */
  public Dimension2D getDrawableSize()
  {
    return (Dimension2D) drawableSize.clone();
  }

  /**
   * Sets the drawable size.
   *
   * @param drawableSize  the drawable size.
   */
  public void setDrawableSize(final Dimension2D drawableSize)
  {
    this.drawableSize.setSize(drawableSize);
  }

  /**
   * Returns the clipping bounds.
   *
   * @return The clipping bounds.
   */
  public Rectangle2D getClippingBounds()
  {
    return clippingBounds.getBounds2D();
  }

  /**
   * Sets the clipping bounds.
   *
   * @param clippingBounds  the clipping bounds.
   */
  public void setClippingBounds(final Rectangle2D clippingBounds)
  {
    this.clippingBounds.setRect(clippingBounds);
  }

  /**
   * Returns a string representation of this object (useful for debugging).
   *
   * @return A string.
   */
  public String toString()
  {
    final StringBuffer b = new StringBuffer();
    b.append(getClass().getName());
    b.append("={Drawable=");
    b.append(getDrawable());
    b.append(", drawableSize=");
    b.append(getDrawableSize());
    b.append(", ClippingBounds=");
    b.append(getClippingBounds());
    b.append("}");
    return b.toString();
  }
}
