/**
 * Date: Mar 5, 2003
 * Time: 6:50:46 PM
 *
 * $Id: DrawableContainer.java,v 1.1 2003/03/07 13:49:37 taqua Exp $
 */
package com.jrefinery.report;

import com.jrefinery.ui.Drawable;

import java.awt.geom.Rectangle2D;
import java.awt.geom.Dimension2D;

/**
 * Wrapper for the drawable object, assigns content bounds and clipping bounds
 * to the drawable.
 * <p>
 * The drawable dimensions define the size of the whole drawable object. These
 * dimensions are logical anchors to define a logical coordinate space for the
 * content bounds.
 * <p>
 * The clipping bounds define the clipping area of the drawable object.
 */
public class DrawableContainer
{
  private Drawable drawable;
  private Dimension2D drawableSize;
  private Rectangle2D clippingBounds;

  public DrawableContainer(DrawableContainer container, Rectangle2D clippingBounds)
  {
    this (container.getDrawable(), container.getDrawableSize(), clippingBounds);
  }

  public DrawableContainer(Drawable drawable, Dimension2D drawableSize, Rectangle2D clippingBounds)
  {
    this.drawable = drawable;
    this.drawableSize = drawableSize;
    this.clippingBounds = clippingBounds;
  }

  public Drawable getDrawable()
  {
    return drawable;
  }

  public Dimension2D getDrawableSize()
  {
    return (Dimension2D) drawableSize.clone();
  }

  public void setDrawableSize(Dimension2D drawableSize)
  {
    this.drawableSize.setSize(drawableSize);
  }

  public Rectangle2D getClippingBounds()
  {
    return clippingBounds.getBounds2D();
  }

  public void setClippingBounds(Rectangle2D clippingBounds)
  {
    this.clippingBounds.setRect(clippingBounds);
  }

  public String toString ()
  {
    StringBuffer b = new StringBuffer();
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
