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
 * -----------------------------
 * ElementLayoutInformation.java
 * -----------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: ElementLayoutInformation.java,v 1.6 2003/02/25 14:57:00 mungady Exp $
 *
 * Changes
 * -------
 * 25-Feb-2003 : Added standard header and fixed Checkstyle issues (DG);
 * 
 */
package com.jrefinery.report.targets.base;

import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import com.jrefinery.report.targets.FloatDimension;

/**
 * A small carrier class to encapsulate the common layout parameters. This information is a 
 * utility class, don't expect to find one bound to an element.
 * 
 * @author Thomas Morgner
 */
public class ElementLayoutInformation
{
  /** The absolute position of the element. */
  private Point2D absolutePosition;
 
  /** The current minimum size for the element. */
  private Dimension2D minimumSize;

  /** The current maximum size for the element. */
  private Dimension2D maximumSize;

  /** The current preferred size for the element. */
  private Dimension2D preferredSize;

  /**
   * Creates a new instance.
   * <p>
   * The position will be <code>rect.x, rect.y</code> and all dimensions
   * are set to <code>rect.width, rect.height</code>.
   *
   * @param rect  the rectangle that will be the base for this ElementLayoutInformation.
   * 
   * @throws NullPointerException if the given rectangle is null.
   */
  public ElementLayoutInformation(Rectangle2D rect)
  {
    if (rect == null)
    {
      throw new NullPointerException();
    }
    absolutePosition = new Point2D.Float((float) rect.getX(), (float) rect.getY());
    maximumSize = new FloatDimension((float) rect.getWidth(), (float) rect.getHeight());
    minimumSize = new FloatDimension((float) rect.getWidth(), (float) rect.getHeight());
    preferredSize = new FloatDimension((float) rect.getWidth(), (float) rect.getHeight());
  }
  
  /**
   * Creates a new instance. 
   * <p>
   * The preferred size will be undefined (<code>null</code>).
   *
   * @param absolutePosition  the absolute position for the element.
   * @param minimumSize  the minimum size for the element.
   * @param maximumSize  the maximum size for the element.
   * @throws NullPointerException if one of the parameters is <code>null</code>.
   */
  public ElementLayoutInformation(Point2D absolutePosition, 
                                  Dimension2D minimumSize, 
                                  Dimension2D maximumSize)
  {
    this (absolutePosition, minimumSize, maximumSize, null);
  }

  /**
   * Creates a new instance. 
   * <p>
   * If the preferred size is <code>null</code>, then it is left undefined.
   *
   * @param absolutePosition  the absolute position for the element
   * @param minimumSize  the minimum size for the element
   * @param maximumSize  the maximum size for the element
   * @param preferredSize  the preferred size or <code>null</code> if not known.
   * 
   * @throws NullPointerException if the position or max/min size is <code>null</code>.
   *
   */
  public ElementLayoutInformation(Point2D absolutePosition, 
                                  Dimension2D minimumSize, 
                                  Dimension2D maximumSize, 
                                  Dimension2D preferredSize)
  {
    if (absolutePosition == null)
    {
      throw new NullPointerException();
    }
    if (minimumSize == null)
    {
      throw new NullPointerException();
    }
    if (maximumSize == null)
    {
      throw new NullPointerException();
    }
    this.absolutePosition = (Point2D) absolutePosition.clone();
    this.minimumSize = (Dimension2D) minimumSize.clone();
    this.maximumSize = (Dimension2D) maximumSize.clone();
    if (preferredSize != null)
    {
      this.preferredSize = (Dimension2D) preferredSize.clone();
    }
  }

  /**
   * Gets the absolute positon defined in this LayoutInformation.
   *
   * @return a clone of the absolute position.
   */
  public Point2D getAbsolutePosition()
  {
    return (Point2D) absolutePosition.clone();
  }

  /**
   * Gets the minimum size defined in this LayoutInformation.
   *
   * @return a clone of the minimum size.
   */
  public Dimension2D getMinimumSize()
  {
    return (Dimension2D) minimumSize.clone();
  }

  /**
   * Gets the maximum size defined in this LayoutInformation.
   *
   * @return a clone of the maximum size.
   */
  public Dimension2D getMaximumSize()
  {
    return (Dimension2D) maximumSize.clone();
  }

  /**
   * Gets the preferred size defined in this LayoutInformation.
   *
   * @return a clone of the preferred size.
   */
  public Dimension2D getPreferredSize()
  {
    if (preferredSize == null)
    {
      return null;
    }
    return (Dimension2D) preferredSize.clone();
  }

  /**
   * Create a minimum dimension of the given 2 dimension objects. If pref is
   * not given, the max parameter is returned.
   *
   * @param max  ??.
   * @param pref  ??.
   * 
   * @return  the minimum dimension.
   */
  public static Dimension2D unionMin (Dimension2D max, Dimension2D pref)
  {
    if (pref == null)
    {
      return max;
    }
    return new FloatDimension((float) Math.min(pref.getWidth(), max.getWidth()),
                              (float) Math.min(pref.getHeight(), max.getHeight()));
  }

  /**
   * Create a maximum dimension of the given 2 dimension objects. If pref is
   * not given, the min parameter is returned.
   *
   * @param min  ??.
   * @param pref  ??.
   * 
   * @return The maximum dimension.
   */
  public static Dimension2D unionMax (Dimension2D min, Dimension2D pref)
  {
    if (pref == null)
    {
      return min;
    }
    return new FloatDimension((float) Math.max(pref.getWidth(), min.getWidth()),
                              (float) Math.max(pref.getHeight(), min.getHeight()));
  }

}
