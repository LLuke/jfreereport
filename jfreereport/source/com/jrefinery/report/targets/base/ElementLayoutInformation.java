/**
 * Date: Jan 21, 2003
 * Time: 2:22:18 PM
 *
 * $Id: ElementLayoutInformation.java,v 1.4 2003/02/12 13:34:34 taqua Exp $
 */
package com.jrefinery.report.targets.base;

import com.jrefinery.report.targets.FloatDimension;

import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**
 * The ElementLayoutInformation is a small carrier class to encapsulate the common
 * layout parameters. This information is a utility class, don't expect to find one
 * bound to an element.
 */
public class ElementLayoutInformation
{
  /** The absolute position of the element. */
  private Point2D absolutePosition;
  /** the current minimum size for the element. */
  private Dimension2D minimumSize;
  /** the current maximum size for the element. */
  private Dimension2D maximumSize;
  /** the current preferred size for the element. */
  private Dimension2D preferredSize;

  /**
   * Creates an ElementLayoutInformation by using the given Rectangle.
   * The position will be <code>rect.x, rect.y</code> and all dimensions
   * are set to <code>rect.width, rect.height</code>.
   *
   * @param rect the rectangle that will be the base for this ElementLayoutInformation.
   * @throws NullPointerException if the given rectangle is null.
   */
  public ElementLayoutInformation(Rectangle2D rect)
  {
    if (rect == null)
      throw new NullPointerException();

    absolutePosition = new Point2D.Float((float) rect.getX(), (float) rect.getY());
    maximumSize= new FloatDimension((float) rect.getWidth(), (float) rect.getHeight());
    minimumSize= new FloatDimension((float) rect.getWidth(), (float) rect.getHeight());
    preferredSize = new FloatDimension((float) rect.getWidth(), (float) rect.getHeight());
  }
  /**
   * Creates an ElementLayoutInformation by using the given Rectangle. The preferred
   * size will be undefined (<code>null</code>).
   *
   * @param absolutePosition the absolute position for the element
   * @param minimumSize the minimum size for the element
   * @param maximumSize the maximum size for the element
   * @throws NullPointerException if one of the parameters is null.
   */
  public ElementLayoutInformation(Point2D absolutePosition, Dimension2D minimumSize, Dimension2D maximumSize)
  {
    this (absolutePosition, minimumSize, maximumSize, null);
  }

  /**
   * Creates an ElementLayoutInformation by using the given Rectangle. if the preferred
   * size is null, then it is left undefined.
   *
   * @param absolutePosition the absolute position for the element
   * @param minimumSize the minimum size for the element
   * @param maximumSize the maximum size for the element
   * @param preferredSize the preferred size or null if not known.
   * @throws NullPointerException if the position or max/min size is null.
   *
   */
  public ElementLayoutInformation(Point2D absolutePosition, Dimension2D minimumSize, Dimension2D maximumSize, Dimension2D preferredSize)
  {
    if (absolutePosition == null)
      throw new NullPointerException();

    if (minimumSize == null)
      throw new NullPointerException();

    if (maximumSize == null)
      throw new NullPointerException();

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
   * @param max
   * @param pref
   * @return
   */
  public static Dimension2D unionMin (Dimension2D max, Dimension2D pref)
  {
    if (pref == null)
      return max;

    return new FloatDimension((float) Math.min(pref.getWidth(), max.getWidth()),
                              (float) Math.min(pref.getHeight(), max.getHeight()));
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

    return new FloatDimension((float) Math.max(pref.getWidth(), min.getWidth()),
                              (float) Math.max(pref.getHeight(), min.getHeight()));
  }

}
