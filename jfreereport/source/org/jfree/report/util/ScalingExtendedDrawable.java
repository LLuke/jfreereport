/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://jfreereport.pentaho.org/
 *
 * (C) Copyright 2000-2006, by Object Refinery Limited, Pentaho Corporation and Contributors.
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
 * $Id$
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 */
package org.jfree.report.util;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import org.jfree.ui.ExtendedDrawable;

/**
 * Creation-Date: 20.01.2006, 19:46:10
 *
 * @author Thomas Morgner
 */
public class ScalingExtendedDrawable implements ExtendedDrawable
{
  private float scaleX;
  private float scaleY;
  private ExtendedDrawable drawable;

  public ScalingExtendedDrawable()
  {
    scaleX = 1;
    scaleY = 1;
  }

  public ExtendedDrawable getDrawable()
  {
    return drawable;
  }

  public void setDrawable(final ExtendedDrawable drawable)
  {
    this.drawable = drawable;
  }

  public float getScaleY()
  {
    return scaleY;
  }

  public void setScaleY(final float scaleY)
  {
    this.scaleY = scaleY;
  }

  public float getScaleX()
  {
    return scaleX;
  }

  public void setScaleX(final float scaleX)
  {
    this.scaleX = scaleX;
  }

  /**
   * Returns the preferred size of the drawable. If the drawable is aspect ratio
   * aware, these bounds should be used to compute the preferred aspect ratio
   * for this drawable.
   *
   * @return the preferred size.
   */
  public Dimension getPreferredSize()
  {
    if (drawable == null)
    {
      return new Dimension(0,0);
    }

    Dimension dim = drawable.getPreferredSize();
    if (dim == null)
    {
      return null;
    }
    dim.setSize(scaleX * dim.getWidth(), scaleY * dim.getHeight());
    return dim;
  }

  /**
   * Returns true, if this drawable will preserve an aspect ratio during the
   * drawing.
   *
   * @return true, if an aspect ratio is preserved, false otherwise.
   */
  public boolean isPreserveAspectRatio()
  {
    if (drawable == null)
    {
      return false;
    }
    return drawable.isPreserveAspectRatio();
  }

  /**
   * Draws the object.
   *
   * @param g2   the graphics device.
   * @param area the area inside which the object should be drawn.
   */
  public void draw(Graphics2D g2, Rectangle2D area)
  {
    if (drawable == null)
    {
      return;
    }
    Graphics2D derived = (Graphics2D) g2.create();
    derived.scale(scaleX, scaleY);
    Rectangle2D scaledArea = (Rectangle2D) area.clone();
    scaledArea.setRect(scaledArea.getX() * scaleX, scaledArea.getY() * scaleY,
            scaledArea.getWidth() * scaleX, scaledArea.getHeight() * scaleY);
    drawable.draw(derived, scaledArea);
    derived.dispose();
  }
}
