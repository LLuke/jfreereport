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
 * -------------------
 * FloatDimension.java
 * -------------------
 * (C)opyright 2000-2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: FloatDimension.java,v 1.3 2002/12/07 20:53:13 taqua Exp $
 *
 * Changes
 * -------
 * 05-Dec-2002 : Updated Javadocs (DG);
 */
package com.jrefinery.report.targets;

import java.awt.geom.Dimension2D;
import java.io.Serializable;

/**
 * A dimension object specified using <code>float</code> values.
 *
 * @author Thomas Morgner
 */
public class FloatDimension extends Dimension2D implements Cloneable, Serializable
{
  /** The width. */
  private float width;
  
  /** The height. */
  private float height;

  /** 
   * Creates a new dimension object with width and height set to zero. 
   */
  public FloatDimension()
  {
    width = 0.0f;
    height = 0.0f;
  }

  /**
   * Creates a new dimension that is a copy of another dimension.
   *
   * @param fd  the dimension to copy.
   */
  public FloatDimension(FloatDimension fd)
  {
    this.width = fd.width;
    this.height = fd.height;
  }

  /**
   * Creates a new dimension.
   *
   * @param width  the width.
   * @param height  the height.
   */
  public FloatDimension(double width, double height)
  {
    this.width = (float) width;
    this.height = (float) height;
  }

  /**
   * Returns the width.
   *
   * @return the width.
   */
  public double getWidth()
  {
    return width;
  }

  /**
   * Returns the height.
   *
   * @return the height.
   */
  public double getHeight()
  {
    return height;
  }

  /**
   * Sets the width.
   *
   * @param width  the width.
   */
  public void setWidth(double width)
  {
    this.width = (float) width;
  }

  /**
   * Sets the height.
   *
   * @param height  the height.
   */
  public void setHeight(double height)
  {
    this.height = (float) height;
  }

  /**
   * Sets the size of this <code>Dimension</code> object to the specified width and height.
   * This method is included for completeness, to parallel the
   * {@link java.awt.Component#getSize getSize} method of
   * {@link java.awt.Component}.
   * @param width  the new width for the <code>Dimension</code>
   * object
   * @param height  the new height for the <code>Dimension</code>
   * object
   */
  public void setSize(double width, double height)
  {
    setHeight((float) height);
    setWidth((float) width);
  }

  /**
   * Creates and returns a copy of this object.  
   *
   * @return     a clone of this instance.
   * @exception  OutOfMemoryError            if there is not enough memory.
   * @see        java.lang.Cloneable
   */
  public Object clone()
  {
    return super.clone();
  }

  /**
   * Returns a string representation of the object. In general, the
   * <code>toString</code> method returns a string that
   * "textually represents" this object. The result should
   * be a concise but informative representation that is easy for a
   * person to read.
   * <p>
   *
   * @return  a string representation of the object.
   */
  public String toString()
  {
    return getClass().getName() + ":={height=" + getHeight() + ", width=" + getWidth() + "}";
  }
}

