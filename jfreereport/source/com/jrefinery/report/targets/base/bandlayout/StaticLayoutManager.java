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
 * ------------------------
 * StaticLayoutManager.java
 * ------------------------
 * (C)opyright 2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: StaticLayoutManager.java,v 1.1 2003/01/29 03:13:01 taqua Exp $
 *
 * Changes
 * -------
 * 03-Dec-2002 : Javadocs (DG);
 * 21-Jan-2003 : removed ABSOLUTE_DIM StyleKey. The beast would cause trouble.
 */

package com.jrefinery.report.targets.base.bandlayout;

import com.jrefinery.report.Band;
import com.jrefinery.report.Element;
import com.jrefinery.report.targets.FloatDimension;
import com.jrefinery.report.targets.LayoutSupport;
import com.jrefinery.report.targets.base.ElementLayoutInformation;
import com.jrefinery.report.targets.base.content.Content;
import com.jrefinery.report.targets.style.ElementStyleSheet;
import com.jrefinery.report.targets.style.StyleKey;
import com.jrefinery.report.util.Log;

import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**
 * An implementation of the BandLayoutManager interface.
 * <p>
 * Rule: Bands can have minimum, max and pref size defined. These values are hints for
 * the layout container, no restrictions. If min and pref are '0', they are ignored.
 * MaxSize is never ignored.
 * <p>
 * Elements that have the "dynamic" flag set, are checked for their content-bounds.
 * This operation is expensive, so this is only done if really needed. The dynamic
 * flag will influence the height of an element, a valid width must be already set.
 * <p>
 * It is not yet tested, whether the band composition works with this layoutmanager.
 *
 * @author Thomas Morgner
 */
public class StaticLayoutManager implements BandLayoutManager
{
  /** A key for the absolute position of an element. */
  public static final StyleKey ABSOLUTE_POS  = StyleKey.getStyleKey("absolute_pos", Point2D.class);

  /** A key for the dynamic height flag for an element. */
  public static final StyleKey DYNAMIC_HEIGHT = StyleKey.getStyleKey("dynamic_height",
                                                                     Boolean.class);

  /** The output target. */
  private LayoutSupport layoutSupport;

  /** A cache for element attributes.  Not in use yet. */
  //private LayoutManagerCache elementCache;

  /**
   * Creates a new layout manager.
   */
  public StaticLayoutManager()
  {
    //elementCache = new LayoutManagerCache();
  }

  public LayoutSupport getLayoutSupport()
  {
    return layoutSupport;
  }

  public void setLayoutSupport(LayoutSupport layoutSupport)
  {
    this.layoutSupport = layoutSupport;
  }

  /**
   * Calculates the preferred size of an element.
   *
   * @param e  the element.
   * @param containerBounds  the bounds of the element's container.
   *
   * @return the preferred size of the element.
   */
  private Dimension2D getPreferredSize(Element e, Dimension2D containerBounds)
  {
    Dimension2D retval = null;

    // if e is a band, then try to calculate the preferred size
    if (e instanceof Band)
    {
      BandLayoutManager lm = BandLayoutManagerUtil.getLayoutManager(e, getLayoutSupport());
      retval = lm.preferredLayoutSize((Band) e, containerBounds);
    }
    else
    {
      // if prefsize is defined, return it
      Dimension2D d = (Dimension2D) e.getStyle().getStyleProperty(ElementStyleSheet.PREFERREDSIZE);
      if (d != null)
      {
        retval = d;
      }
      else
      {
        // return the absolute dimension as fallback
        retval = (Dimension2D) e.getStyle().getStyleProperty(ElementStyleSheet.MINIMUMSIZE);
      }
    }

    if (e.getStyle().getBooleanStyleProperty(DYNAMIC_HEIGHT))
    {
      retval = getElementContentBounds(retval, e, containerBounds);
    }

    return retval;
  }

  /**
   * Calculation rules: Take the width of given bounds to calculate a height based
   * on the content. Then cut the content to a maybe defined max-value.
   * todo redefine the context creation process, height or width can be dynamic
   *
   * @param bounds  the bounds of the element calculated so far.
   * @param e  the element.
   * @param conBounds  the bounds of the surrounding container.
   *
   * @return the new elements dimension.
   */
  private Dimension2D getElementContentBounds (Dimension2D bounds, Element e, Dimension2D conBounds)
  {
    // check if we can handle the content before doing anything...
    // ...
    // bounds can be null, if no absolute dim was defined.
    com.jrefinery.report.targets.base.operations.OperationModule mod = com.jrefinery.report.targets.base.operations.OperationFactory.getInstance().getModul(e.getContentType());
    if (mod == null)
    {
      return bounds;
    }

    Dimension2D parentDim = new FloatDimension(conBounds.getWidth(), conBounds.getHeight());
    bounds = correctDimension(bounds, parentDim);

    Point2D absPos = correctPoint(
        (Point2D) e.getStyle().getStyleProperty(ABSOLUTE_POS), parentDim);
    Dimension2D maxSize = correctDimension(
        (Dimension2D) e.getStyle().getStyleProperty(ElementStyleSheet.MAXIMUMSIZE), parentDim);
    Dimension2D minSize = correctDimension(
        (Dimension2D) e.getStyle().getStyleProperty(ElementStyleSheet.MINIMUMSIZE), parentDim);

    Dimension2D prefSize = (Dimension2D) e.getStyle().getStyleProperty(ElementStyleSheet.PREFERREDSIZE);
    if (prefSize != null)
    {
      prefSize = correctDimension(prefSize, parentDim);
    }

    // the width is the limiting element in the calculation, height is considered
    // infinitive ...
    maxSize.setSize(Math.min (parentDim.getWidth() - absPos.getX(), maxSize.getWidth()),
                    Short.MAX_VALUE);

    // Rectangle2D elementBounds = new Rectangle2D.Double (0, 0, w, h);
    try
    {
      ElementLayoutInformation eli = new ElementLayoutInformation(absPos, minSize, maxSize, prefSize);
      Content content = mod.createContentForElement(e, eli, getLayoutSupport());
      Rectangle2D contentBounds = content.getMinimumContentSize();
      if (contentBounds == null)
      {
        return new FloatDimension();
      }
      return new FloatDimension(contentBounds.getWidth(), contentBounds.getHeight());
    }
    catch (Exception ex)
    {
      Log.warn ("Error while calculating the content bounds: ", ex);
      return new FloatDimension(bounds.getWidth(), bounds.getHeight());
    }

  }

  /**
   * Returns the minimum size for an element.
   *
   * @param e  the element.
   *
   * @return the minimum size.
   */
  private Dimension2D getMinimumSize(Element e, Dimension2D containerBounds)
  {
    Dimension2D retval = null;

    // if this is a band, then try to calculate the min-size
    if (e instanceof Band)
    {
      BandLayoutManager lm = BandLayoutManagerUtil.getLayoutManager(e, getLayoutSupport());
      retval = lm.minimumLayoutSize((Band) e, containerBounds);
    }
    else
    {
      // return the minimum size as fallback
      retval = (Dimension2D) e.getStyle().getStyleProperty(ElementStyleSheet.MINIMUMSIZE);
    }
    return retval;
  }

  /**
   * Calculates the preferred layout size for a band. The band is limited
   * to the given container bounds as well as to the own maximum size.
   *
   * @param b  the band.
   * @param containerDims the maximum size the band should use for that container.
   *
   * @return the preferred size.
   */
  public Dimension2D preferredLayoutSize(Band b, Dimension2D containerDims)
  {
    // invisible bands do not need any space
    if (b.isVisible() == false)
    {
      return new FloatDimension(0, 0);
    }

    //elementCache.setCurrentBand(b);

    double height = 0;
    double width = 0;

    Dimension2D prefSize
        = (Dimension2D) b.getStyle().getStyleProperty(ElementStyleSheet.PREFERREDSIZE);

    // there is a preferredSize defined, don't calculate one...
    if (prefSize != null)
    {
      height = Math.max(height, prefSize.getHeight());
      width = Math.max(width, prefSize.getWidth());
    }

    Dimension2D minSize
        = (Dimension2D) b.getStyle().getStyleProperty(ElementStyleSheet.MINIMUMSIZE);

    // check for an minimum size and take that into account
    height = Math.max(height, minSize.getHeight());
    width = Math.max(width, minSize.getWidth());

    // now take the maximum limit defined for that band into account.
    Dimension2D maxSize
        = (Dimension2D) b.getStyle().getStyleProperty(ElementStyleSheet.MAXIMUMSIZE);
    double maxW = Math.min (maxSize.getWidth(), containerDims.getWidth());
    double maxH = Math.min (maxSize.getHeight(), containerDims.getHeight());
    Dimension2D parentBounds = new FloatDimension(maxW, maxH);

    // Now adjust the defined sizes by using the elements stored in the band.
    Element[] elements = b.getElementArray();

    // calculate absolute width
    for (int i = 0; i < elements.length; i++)
    {
      Element e = elements[i];
      if (isElementStaticWidth(e))
      {
        Dimension2D size = getPreferredSize(e, parentBounds);
        Point2D absPos = (Point2D) e.getStyle().getStyleProperty(ABSOLUTE_POS);

        width = Math.max(size.getWidth() + absPos.getX(), width);
      }
      if (isElementStaticHeight(e))
      {
        Dimension2D size = getPreferredSize(e, parentBounds);
        Point2D absPos = (Point2D) e.getStyle().getStyleProperty(ABSOLUTE_POS);

        height = Math.max(size.getHeight() + absPos.getY(), height);
      }
    }

    // now apply the maximum limit defined for that band in case the calculated height
    // is higher than the given max height.
    height = Math.min(height, maxH);
    width = Math.min(width, maxW);

    FloatDimension base = new FloatDimension(width, height);

    // calculate relative widths
    for (int i = 0; i < elements.length; i++)
    {
      Element e = elements[i];
      if (isElementStaticWidth(e) == false)
      {
        Dimension2D size = correctDimension(getPreferredSize(e, parentBounds), base);
        Point2D absPos = correctPoint((Point2D) e.getStyle().getStyleProperty(ABSOLUTE_POS), base);

        width = Math.max(size.getWidth() + absPos.getX(), width);
      }
      if (isElementStaticHeight(e) == false)
      {
        Dimension2D size = correctDimension(getPreferredSize(e, parentBounds), base);
        Point2D absPos = correctPoint((Point2D) e.getStyle().getStyleProperty(ABSOLUTE_POS), base);

        height = Math.max(size.getHeight() + absPos.getY(), height);
      }
    }

    // now align the calculated data ...
    return new FloatDimension(alignUp(width, layoutSupport.getHorizontalAlignmentBorder()),
                              alignUp(height, layoutSupport.getVerticalAlignmentBorder()));
  }

  private double alignUp (double value, double boundry)
  {
    if (boundry == 0)
      return value;

    return Math.ceil(value / boundry) * boundry;
  }
/*
  private double alignDown (double value, double boundry)
  {
    if (boundry == 0)
      return value;

    return Math.floor(value / boundry) * boundry;
  }
*/
  /**
   * Calculates the minimum layout size for a band.
   *
   * @param b  the band.
   *
   * @return the minimum size.
   */
  public Dimension2D minimumLayoutSize(Band b, Dimension2D containerBounds)
  {
    // invisible bands do not need any space
    if (b.isVisible() == false)
    {
      return new FloatDimension(0, 0);
    }
    //elementCache.setCurrentBand(b);

    double height = 0;
    double width = 0;

    Dimension2D minSize
        = (Dimension2D) b.getStyle().getStyleProperty(ElementStyleSheet.MINIMUMSIZE);

    if (minSize != null)
    {
      height = Math.max(height, minSize.getHeight());
      width = Math.max(width, minSize.getWidth());
    }

    // Check the position of the elements inside and calculate the minimum width
    // needed to display all elements
    Element[] elements = b.getElementArray();

    // calculate absolute width
    for (int i = 0; i < elements.length; i++)
    {
      Element e = elements[i];
      if (isElementStaticWidth(e))
      {
        Dimension2D size = getMinimumSize(e, containerBounds);
        Point2D absPos = (Point2D) e.getStyle().getStyleProperty(ABSOLUTE_POS);

        width = Math.max(size.getWidth() + absPos.getX(), width);
      }
      if (isElementStaticHeight(e))
      {
        Dimension2D size = getMinimumSize(e, containerBounds);
        Point2D absPos = (Point2D) e.getStyle().getStyleProperty(ABSOLUTE_POS);

        height = Math.max(size.getHeight() + absPos.getY(), height);
      }
    }

    // now apply the maximum limit defined for that band in case the calculated height
    // is higher than the given max height.
    Dimension2D maxSize
        = (Dimension2D) b.getStyle().getStyleProperty(ElementStyleSheet.MAXIMUMSIZE);
    height = Math.min(height, maxSize.getHeight());
    width = Math.min(width, maxSize.getWidth());

    FloatDimension base = new FloatDimension(width, height);

    // calculate relative widths
    for (int i = 0; i < elements.length; i++)
    {
      Element e = elements[i];
      if (isElementStaticWidth(e) == false)
      {
        Dimension2D size = correctDimension(getPreferredSize(e, containerBounds), base);
        Point2D absPos = correctPoint((Point2D) e.getStyle().getStyleProperty(ABSOLUTE_POS), base);

        width = Math.max(size.getWidth() + absPos.getX(), width);
      }
      if (isElementStaticHeight(e) == false)
      {
        Dimension2D size = correctDimension(getPreferredSize(e, containerBounds), base);
        Point2D absPos = correctPoint((Point2D) e.getStyle().getStyleProperty(ABSOLUTE_POS), base);

        height = Math.max(size.getHeight() + absPos.getY(), height);
      }
    }


    // now take the maximum limit defined for that band into account for a last time.
    if (maxSize != null)
    {
      height = Math.min(height, maxSize.getHeight());
      width = Math.min(width, maxSize.getWidth());
    }

    // now align the calculated data ...
    return new FloatDimension(alignUp(width, layoutSupport.getHorizontalAlignmentBorder()),
                              alignUp(height, layoutSupport.getVerticalAlignmentBorder()));
  }

  /**
   * Layout a single band with all elements contained within the band.
   * <p>
   * The band has its <code>BOUNDS</code> already set and all elements are laid out
   * within these bounds. The band's properties will not be changed during the layouting.
   * <p>
   * This layout manager requires that all direct child elements have the <code>ABSOLUTE_POS</code>
   * and <code>MINIMUM_SIZE</code> properties set to valid values.
   *
   * @param b the band to lay out.
   */
  public void doLayout(Band b)
  {
    //elementCache.setCurrentBand(b);

    Element[] elements = b.getElementArray();
    Rectangle2D parentBounds = BandLayoutManagerUtil.getBounds(b, null);
    if (parentBounds == null)
    {
      throw new NullPointerException("Need the parent's bound set");
    }
    Dimension2D parentDim = new FloatDimension(parentBounds.getWidth(), parentBounds.getHeight());
    Point2D parentPoint = new Point2D.Double (parentBounds.getX(), parentBounds.getY());

//    Log.debug ("ParentBounds: " + parentBounds);

    for (int i = 0; i < elements.length; i++)
    {
      Element e = elements[i];
      Dimension2D uncorrectedSize = getPreferredSize(e, parentDim);
      Dimension2D size = correctDimension(uncorrectedSize, parentDim);

//    Log.debug ("UnCorrectedSize: " + uncorrectedSize);

      Point2D absPos
          = correctPoint((Point2D) e.getStyle().getStyleProperty(ABSOLUTE_POS), parentDim);
      absPos.setLocation(absPos.getX() + parentPoint.getX(), absPos.getY() + parentPoint.getY());
      Rectangle2D bounds = new Rectangle2D.Double(alignUp(absPos.getX(), layoutSupport.getHorizontalAlignmentBorder()),
                                                  alignUp(absPos.getY(), layoutSupport.getVerticalAlignmentBorder()),
                                                  alignUp(size.getWidth(), layoutSupport.getHorizontalAlignmentBorder()),
                                                  alignUp(size.getHeight(), layoutSupport.getVerticalAlignmentBorder()));
      BandLayoutManagerUtil.setBounds(e, bounds);
//      Log.debug ("Set Bounds: " + bounds);
      if (e instanceof Band)
      {
        Band band = (Band) e;
        doLayout(band);
      }
    }
  }

  /**
   * Returns <code>true</code> if the element has a static width, and <code>false</code> otherwise.
   *
   * @param e  the element.
   *
   * @return <code>true</code> or </code>false</code>.
   */
  private boolean isElementStaticWidth (Element e)
  {
    Point2D absPos = (Point2D) e.getStyle().getStyleProperty(ABSOLUTE_POS);
    if (absPos == null)
      throw new IllegalArgumentException("Element " + e + " has no valid constraints. ABSOLUTE_POS is missing");

    if (absPos.getX() < 0)
    {
      return false;
    }
    Dimension2D maxSize
        = (Dimension2D) e.getStyle().getStyleProperty(ElementStyleSheet.MAXIMUMSIZE);
    if (maxSize.getWidth() < 0)
    {
      return false;
    }
    return true;
  }

  /**
   * Returns true if the element has a static height, and false otherwise.
   *
   * @param e  the element.
   *
   * @return true or false.
   */
  private boolean isElementStaticHeight (Element e)
  {
    Point2D absPos = (Point2D) e.getStyle().getStyleProperty(ABSOLUTE_POS);
    if (absPos == null)
      throw new IllegalArgumentException("Element " + e + " has no valid constraints. ABSOLUTE_POS is missing");

    if (absPos.getY() < 0)
    {
      return false;
    }
    Dimension2D maxSize
        = (Dimension2D) e.getStyle().getStyleProperty(ElementStyleSheet.MAXIMUMSIZE);
    if (maxSize.getHeight() < 0)
    {
      return false;
    }
    return true;
  }

  /**
   * Clears any cached items used by the layout manager.
   */
  public void flushLayout()
  {
    //elementCache.flushCache();
    //elementCache.setCurrentBand(null);
  }

  /**
   * Corrects the relative (proportional) values. The values are given
   * in the range from -100 (= 100%) to 0 (0%) and are resolved to the
   * base values.
   *
   * @param dim  the dimension that should be corrected.
   * @param base  the base to define the 100% limit.
   *
   * @return the corrected dimension.
   */
  private Dimension2D correctDimension (Dimension2D dim, Dimension2D base)
  {
    double newWidth = dim.getWidth();
    if (dim.getWidth() < 0)
    {
      newWidth = (dim.getWidth() * base.getWidth() / -100);
    }
    double newHeight = dim.getHeight();
    if (dim.getHeight() < 0)
    {
      newHeight = (dim.getHeight() * base.getHeight() / -100);
    }
    return new FloatDimension(newWidth, newHeight);
  }

  /**
   * Corrects the relative (proportional) values. The values are given
   * in the range from -100 (= 100%) to 0 (0%) and are resolved to the
   * base values.
   *
   * @param dim  the point that should be corrected.
   * @param base  the base to define the 100% limit.
   *
   * @return the corrected point.
   */
  private Point2D correctPoint (Point2D dim, Dimension2D base)
  {
    double x = dim.getX();
    double y = dim.getY();
    if (x < 0)
    {
      x = (dim.getX() * base.getWidth() / -100);
    }
    if (y < 0)
    {
      y = (dim.getY() * base.getHeight() / -100);
    }
    return new Point2D.Double(x, y);
  }

}
