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
 * $Id: StaticLayoutManager.java,v 1.5 2003/02/02 23:43:51 taqua Exp $
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
import com.jrefinery.report.targets.base.operations.OperationFactory;
import com.jrefinery.report.targets.base.operations.OperationModule;
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
   * Returns the minimum size for an element.
   *
   * @param e  the element.
   *
   * @return the minimum size.
   */
  private Dimension2D getMinimumSize(Element e, Dimension2D containerBounds)
  {
    Dimension2D retval;

    // the absolute position of the element within its parent is used
    // to calculate the maximum available space for the element.
    Point2D absPos = correctPoint(
        (Point2D) e.getStyle().getStyleProperty(ABSOLUTE_POS), containerBounds);

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

    // docmark minimum size also checks the dynamic height.
    if (e.getStyle().getBooleanStyleProperty(DYNAMIC_HEIGHT))
    {
      retval = getElementContentBounds(retval, e, containerBounds);
    }

    // now apply the maximum bounds to the retval.
    // the maximum bounds are defined by the element and the elements container.
    Dimension2D maxSize = correctDimension(
        (Dimension2D) e.getStyle().getStyleProperty(ElementStyleSheet.MAXIMUMSIZE), containerBounds);

    maxSize.setSize(Math.min (containerBounds.getWidth() - absPos.getX(), maxSize.getWidth()),
                    Math.min (containerBounds.getHeight() - absPos.getY(), maxSize.getHeight()));

    retval.setSize(Math.min (retval.getWidth(), maxSize.getWidth()),
                   Math.min (retval.getHeight(), maxSize.getHeight()));
    //Log.debug ("-- calculate MinimumSize: " + retval);
    return retval;
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
    Dimension2D retval;
    //Log.debug (">> calculate PreferredSize: " + e);
    //Log.debug (">> calculate PreferredSize: " + containerBounds);

    // the absolute position of the element within its parent is used
    // to calculate the maximum available space for the element.
    Point2D absPos = correctPoint(
        (Point2D) e.getStyle().getStyleProperty(ABSOLUTE_POS), containerBounds);

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

    // now apply the maximum bounds to the retval.
    // the maximum bounds are defined by the element and the elements container.
    Dimension2D maxSize = correctDimension(
        (Dimension2D) e.getStyle().getStyleProperty(ElementStyleSheet.MAXIMUMSIZE), containerBounds);

    maxSize.setSize(Math.min (containerBounds.getWidth() - absPos.getX(), maxSize.getWidth()),
                    Math.min (containerBounds.getHeight() - absPos.getY(), maxSize.getHeight()));

    retval.setSize(Math.min (retval.getWidth(), maxSize.getWidth()),
                   Math.min (retval.getHeight(), maxSize.getHeight()));
    //Log.debug ("-- calculate PreferredSize: " + retval);
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
    OperationModule mod = OperationFactory.getInstance().getModul(e.getContentType());
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
      Log.debug ("Band is not visible");
      return new FloatDimension(0, 0);
    }

//    Log.debug ("> preferredLayoutSize: ContainerDimension: " + containerDims);
//    Log.debug ("  preferredLayoutSize: Container: " + b);

    double height = 0;
    double width = 0;

    // the preferred size of an band can be a relative value. Then this value is
    // relative to the container bounds
    Dimension2D prefSize
        = (Dimension2D) b.getStyle().getStyleProperty(ElementStyleSheet.PREFERREDSIZE);

    // there is a preferredSize defined, don't calculate one...
    if (prefSize != null)
    {
      prefSize = correctDimension(prefSize, containerDims);
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

    // the bounds inherited from the parent, cut down to the maximum size defined
    // for this elements.
    Dimension2D parentBounds = new FloatDimension(maxW, maxH);
//    Log.debug ("  my bounds so far: " + parentBounds);

    // Now adjust the defined sizes by using the elements stored in the band.
    Element[] elements = b.getElementArray();

    // calculate absolute width
    for (int i = 0; i < elements.length; i++)
    {
      Element e = elements[i];
      boolean staticWidth = isElementStaticWidth(e);
      boolean staticHeight = isElementStaticHeight(e);
      if (staticWidth || staticHeight)
      {
        Dimension2D size = getPreferredSize(e, parentBounds);
        Point2D absPos = (Point2D) e.getStyle().getStyleProperty(ABSOLUTE_POS);

        if (staticWidth)
        {
          width = Math.max(size.getWidth() + absPos.getX(), width);
        }
        if (staticHeight)
        {
          height = Math.max(size.getHeight() + absPos.getY(), height);
        }
      }
    }

    // now apply the minimum limit defined for that band in case the calculated height
    // is lower than the given minimum height.
    height = Math.max(height, minSize.getHeight());
    width = Math.max(width, minSize.getWidth());

    // now apply the maximum limit defined for that band in case the calculated height
    // is higher than the given max height.
    height = Math.min(height, maxH);
    width = Math.min(width, maxW);

    // docmark: using base as parent will try to use the bounds used by
    // docmark: static elements as base for the relative position.
    // docmark: this will fail if there is no static element ...
    // docmark: the element will start to consume all available space.
    //FloatDimension base = new FloatDimension(width, height);
//    Log.debug ("  my bounds after static processing: " + new FloatDimension(width, height));

    // calculate relative widths
    for (int i = 0; i < elements.length; i++)
    {
      Element e = elements[i];
      boolean staticWidth = isElementStaticWidth(e);
      boolean staticHeight = isElementStaticHeight(e);
      if (staticWidth == false || staticHeight == false)
      {
        Dimension2D size = correctDimension(getPreferredSize(e, parentBounds), parentBounds);
        Point2D absPos = correctPoint((Point2D) e.getStyle().getStyleProperty(ABSOLUTE_POS), parentBounds);

        if (staticWidth == false)
        {
          width = Math.max(size.getWidth() + absPos.getX(), width);
        }
        if (staticHeight == false)
        {
//          Log.debug ("CorrectHeight: " + size);
          height = Math.max(size.getHeight() + absPos.getY(), height);
        }
      }
    }
//    Log.debug ("  my bounds after dynamic processing: " + new FloatDimension(width, height));

    // now apply the minimum limit defined for that band in case the calculated height
    // is lower than the given minimum height.
    height = Math.max(height, minSize.getHeight());
    width = Math.max(width, minSize.getWidth());

    // now take the maximum limit defined for that band into account for a last time.
    // or specifying -140 would be a nice way to kill the layout ...
    height = Math.min(height, maxSize.getHeight());
    width = Math.min(width, maxSize.getWidth());

    // now align the calculated data ...
    FloatDimension fdim =  new FloatDimension(align(width, layoutSupport.getHorizontalAlignmentBorder()),
                                                align(height, layoutSupport.getVerticalAlignmentBorder()));
//    Log.debug ("- Preferred LayoutSize: " + fdim);
    return fdim;
  }

  private double align (double value, double boundry)
  {
    if (boundry == 0)
    {
      return value;
    }

    return Math.ceil(value / boundry) * boundry;
  }

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

//    Log.debug ("> MinimumLayoutSize: ContainerDimension: " + containerBounds);
//    Log.debug ("  MinimumLayoutSize: Container: " + b);

    // the preferred size of an band can be a relative value. Then this value is
    // relative to the container bounds
    Dimension2D minSize
        = correctDimension((Dimension2D) b.getStyle().getStyleProperty(ElementStyleSheet.MINIMUMSIZE),
                           containerBounds);

    double height = Math.max(0, minSize.getHeight());
    double width = Math.max(0, minSize.getWidth());

    // now take the maximum limit defined for that band into account.
    Dimension2D maxSize
        = (Dimension2D) b.getStyle().getStyleProperty(ElementStyleSheet.MAXIMUMSIZE);
    double maxW = Math.min (maxSize.getWidth(), containerBounds.getWidth());
    double maxH = Math.min (maxSize.getHeight(), containerBounds.getHeight());

    // the bounds inherited from the parent, cut down to the maximum size defined
    // for this elements.
    Dimension2D parentBounds = new FloatDimension(maxW, maxH);
//    Log.debug ("  my bounds so far: " + parentBounds);

    // Check the position of the elements inside and calculate the minimum width
    // needed to display all elements
    Element[] elements = b.getElementArray();

    // calculate absolute width
    for (int i = 0; i < elements.length; i++)
    {
      Element e = elements[i];
      boolean staticWidth = isElementStaticWidth(e);
      boolean staticHeight = isElementStaticHeight(e);
      if (staticWidth || staticHeight)
      {
        Dimension2D size = getMinimumSize(e, parentBounds);
        Point2D absPos = (Point2D) e.getStyle().getStyleProperty(ABSOLUTE_POS);

        if (staticWidth)
        {
          width = Math.max(size.getWidth() + absPos.getX(), width);
        }
        if (staticHeight)
        {
          height = Math.max(size.getHeight() + absPos.getY(), height);
        }
      }
    }

    // now apply the minimum limit defined for that band in case the calculated height
    // is lower than the given minimum height.
    height = Math.max(height, minSize.getHeight());
    width = Math.max(width, minSize.getWidth());

    // now apply the maximum limit defined for that band in case the calculated height
    // is higher than the given max height.
    height = Math.min(height, maxSize.getHeight());
    width = Math.min(width, maxSize.getWidth());

//    Log.debug ("  my bounds after static processing: " + new FloatDimension(width, height));
    FloatDimension base = new FloatDimension(width, height);

    // calculate relative widths
    for (int i = 0; i < elements.length; i++)
    {
      Element e = elements[i];
      boolean staticWidth = isElementStaticWidth(e);
      boolean staticHeight = isElementStaticHeight(e);
      if (staticWidth == false || staticHeight == false)
      {
        Dimension2D size = correctDimension(getMinimumSize(e, containerBounds), base);
        Point2D absPos = correctPoint((Point2D) e.getStyle().getStyleProperty(ABSOLUTE_POS), base);

        if (staticWidth == false)
        {
          width = Math.max(size.getWidth() + absPos.getX(), width);
        }
        if (staticHeight == false)
        {
          height = Math.max(size.getHeight() + absPos.getY(), height);
        }
      }
    }
//    Log.debug ("  my bounds after dynamic processing: " + new FloatDimension(width, height));

    // now apply the minimum limit defined for that band in case the calculated height
    // is lower than the given minimum height.
    height = Math.max(height, minSize.getHeight());
    width = Math.max(width, minSize.getWidth());

    // now take the maximum limit defined for that band into account for a last time.
    height = Math.min(height, maxSize.getHeight());
    width = Math.min(width, maxSize.getWidth());

    // now align the calculated data ...
    FloatDimension fdim =  new FloatDimension(align(width, layoutSupport.getHorizontalAlignmentBorder()),
                                                align(height, layoutSupport.getVerticalAlignmentBorder()));
//    Log.debug ("< Minimum LayoutSize: " + fdim);
    return fdim;
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

//      Log.debug ("PreferredSize:( ) " + uncorrectedSize);
//      Log.debug ("PreferredSize:(X) " + size);

      Point2D absPos
          = correctPoint((Point2D) e.getStyle().getStyleProperty(ABSOLUTE_POS), parentDim);
      absPos.setLocation(absPos.getX() + parentPoint.getX(), absPos.getY() + parentPoint.getY());

      // here apply the maximum bounds ...
      Rectangle2D bounds = new Rectangle2D.Double(align(absPos.getX(), layoutSupport.getHorizontalAlignmentBorder()),
                                                  align(absPos.getY(), layoutSupport.getVerticalAlignmentBorder()),
                                                  align(size.getWidth(), layoutSupport.getHorizontalAlignmentBorder()),
                                                  align(size.getHeight(), layoutSupport.getVerticalAlignmentBorder()));
      BandLayoutManagerUtil.setBounds(e, bounds);
//      Log.debug ("\n\n\nSet Bounds: " + bounds + " for Band " + e);
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
    Dimension2D minSize
        = (Dimension2D) e.getStyle().getStyleProperty(ElementStyleSheet.MINIMUMSIZE);
    if (minSize.getWidth() < 0)
    {
      return false;
    }
    minSize = (Dimension2D) e.getStyle().getStyleProperty(ElementStyleSheet.PREFERREDSIZE);
    if (minSize != null)
    {
      if (minSize.getWidth() < 0)
      {
        return false;
      }
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
    Dimension2D minSize
        = (Dimension2D) e.getStyle().getStyleProperty(ElementStyleSheet.MINIMUMSIZE);
    if (minSize.getHeight() < 0)
    {
      return false;
    }
    minSize = (Dimension2D) e.getStyle().getStyleProperty(ElementStyleSheet.PREFERREDSIZE);
    if (minSize != null)
    {
      if (minSize.getHeight() < 0)
      {
        return false;
      }
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
