/**
 * Date: Jan 21, 2003
 * Time: 2:22:18 PM
 *
 * $Id: ElementLayoutInformation.java,v 1.1 2003/01/29 03:13:01 taqua Exp $
 */
package com.jrefinery.report.targets.base;

import com.jrefinery.report.targets.FloatDimension;

import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class ElementLayoutInformation
{
  private Point2D absolutePosition;
  private Dimension2D minimumSize;
  private Dimension2D maximumSize;
  private Dimension2D preferredSize;

  public ElementLayoutInformation(Rectangle2D rect)
  {
    if (rect == null)
      throw new NullPointerException();

    absolutePosition = new Point2D.Double(rect.getX(), rect.getY());
    maximumSize= new FloatDimension(rect.getWidth(), rect.getHeight());
    minimumSize= new FloatDimension(rect.getWidth(), rect.getHeight());
    preferredSize = new FloatDimension(rect.getWidth(), rect.getHeight());
  }

  public ElementLayoutInformation(Point2D absolutePosition, Dimension2D minimumSize, Dimension2D maximumSize)
  {
    this (absolutePosition, minimumSize, maximumSize, null);
  }

  public ElementLayoutInformation(Point2D absolutePosition, Dimension2D minimumSize, Dimension2D maximumSize, Dimension2D preferredSize)
  {
    if (absolutePosition == null)
      throw new NullPointerException();

    if (minimumSize == null)
      throw new NullPointerException();

    if (maximumSize == null)
      throw new NullPointerException();

    this.absolutePosition = absolutePosition;
    this.minimumSize = minimumSize;
    this.maximumSize = maximumSize;
    this.preferredSize = preferredSize;
  }

  public Point2D getAbsolutePosition()
  {
    return absolutePosition;
  }

  public Dimension2D getMinimumSize()
  {
    return minimumSize;
  }

  public Dimension2D getMaximumSize()
  {
    return maximumSize;
  }

  public Dimension2D getPreferredSize()
  {
    return preferredSize;
  }

  /**
   * Create a minimum dimension of the given 2 dimension objects. If pref is
   * not given, the max parameter is returned.
   *
   * @param max
   * @param pref
   * @return
   */
  public static Dimension2D unionMin (Dimension2D max, Dimension2D pref)
  {
    if (pref == null)
      return max;

    return new FloatDimension(Math.min(pref.getWidth(), max.getWidth()),
                              Math.min(pref.getHeight(), max.getHeight()));
  }

  /**
   * Create a maximum dimension of the given 2 dimension objects. If pref is
   * not given, the min parameter is returned.
   *
   * @param min
   * @param pref
   * @return
   */
  public static Dimension2D unionMax (Dimension2D min, Dimension2D pref)
  {
    if (pref == null)
      return min;

    return new FloatDimension(Math.max(pref.getWidth(), min.getWidth()),
                              Math.max(pref.getHeight(), min.getHeight()));
  }

}
