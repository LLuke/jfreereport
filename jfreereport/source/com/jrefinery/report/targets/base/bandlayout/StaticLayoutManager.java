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
 * $Id: StaticLayoutManager.java,v 1.14 2003/02/20 21:05:00 taqua Exp $
 *
 * Changes
 * -------
 * 03-Dec-2002 : Javadocs (DG);
 * 21-Jan-2003 : removed ABSOLUTE_DIM StyleKey. The beast would cause trouble.
 * 01-Feb-2003 : BugFix: Layouting was not correct for subbands.
 */

package com.jrefinery.report.targets.base.bandlayout;

import com.jrefinery.report.Band;
import com.jrefinery.report.Element;
import com.jrefinery.report.util.Log;
import com.jrefinery.report.targets.FloatDimension;
import com.jrefinery.report.targets.base.ElementLayoutInformation;
import com.jrefinery.report.targets.base.content.Content;
import com.jrefinery.report.targets.base.content.ContentFactory;
import com.jrefinery.report.targets.base.layout.LayoutSupport;
import com.jrefinery.report.targets.style.ElementStyleSheet;
import com.jrefinery.report.targets.style.StyleKey;

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
      Dimension2D dim = (Dimension2D) e.getStyle().getStyleProperty(ElementStyleSheet.MINIMUMSIZE);
      retval = correctDimension(dim, containerBounds);
    }

    // docmark: minimum size also checks the dynamic height.
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
        retval = correctDimension(d, containerBounds);
      }
      else
      {
        // return the absolute dimension as fallback
        retval = correctDimension((Dimension2D)
            e.getStyle().getStyleProperty(ElementStyleSheet.MINIMUMSIZE), containerBounds);
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
   * Calculates the size of an element by creating the content for this element and
   * then trying to layout that content. This operation is performed for all
   * "dynamic" elements.
   * <p>
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
    ContentFactory contentFactory = layoutSupport.getContentFactory();
    if (contentFactory.canHandleContent(e.getContentType()) == false)
    {
      return bounds;
    }

    Dimension2D parentDim = new FloatDimension((float) conBounds.getWidth(),
                                               (float) conBounds.getHeight());
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
    try
    {
      ElementLayoutInformation eli = new ElementLayoutInformation(absPos, minSize, maxSize, prefSize);
      Content content = contentFactory.createContentForElement(e, eli, getLayoutSupport());
      Rectangle2D contentBounds = content.getMinimumContentSize();
      if (contentBounds == null)
      {
        return new FloatDimension();
      }
      return new FloatDimension((float) Math.max (minSize.getWidth(), contentBounds.getWidth()),
                                (float) Math.max (minSize.getHeight(), contentBounds.getHeight()));
    }
    catch (Exception ex)
    {
//      Log.warn ("Error while calculating the content bounds: ", ex);
      return new FloatDimension((float) Math.max (minSize.getWidth(), bounds.getWidth()),
                                (float) Math.max (minSize.getHeight(), bounds.getHeight()));
    }
  }

  /**
   * Calculates the preferred layout size for a band. The band is limited
   * to the given container bounds as well as to the own maximum size.
   * <p>
   * The preferred size of non-absolute elements is calculated by using the
   * parents dimensions supplied in containerDims. Elements with a width or
   * height of 100% will consume all available space of the parent.
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

    float height = 0;
    float width = 0;

    // the preferred size of an band can be a relative value. Then this value is
    // relative to the container bounds
    Dimension2D prefSize
        = (Dimension2D) b.getStyle().getStyleProperty(ElementStyleSheet.PREFERREDSIZE);

    // there is a preferredSize defined, don't calculate one...
    if (prefSize != null)
    {
      prefSize = correctDimension(prefSize, containerDims);
      height = (float) Math.max(height, prefSize.getHeight());
      width = (float) Math.max(width, prefSize.getWidth());
    }

    Dimension2D minSize
        = correctDimension((Dimension2D) b.getStyle().getStyleProperty(ElementStyleSheet.MINIMUMSIZE), containerDims);

    // check for an minimum size and take that into account
    height = (float) Math.max(height, minSize.getHeight());
    width = (float) Math.max(width, minSize.getWidth());

    // now take the maximum limit defined for that band into account.
    Dimension2D maxSize
        = correctDimension((Dimension2D) b.getStyle().getStyleProperty(ElementStyleSheet.MAXIMUMSIZE), containerDims);
    float maxW = (float) Math.min (maxSize.getWidth(), containerDims.getWidth());
    float maxH = (float) Math.min (maxSize.getHeight(), containerDims.getHeight());

    // the bounds inherited from the parent, cut down to the maximum size defined
    // for this elements.
    Dimension2D parentBounds = new FloatDimension(maxW, maxH);

    // Now adjust the defined sizes by using the elements stored in the band.
    Element[] elements = b.getElementArray();

    // calculate absolute width
    for (int i = 0; i < elements.length; i++)
    {
      Element e = elements[i];

      if (e.isVisible() == false)
        continue;

      boolean staticWidth = isElementStaticWidth(e);
      boolean staticHeight = isElementStaticHeight(e);
      if (staticWidth || staticHeight)
      {
        Dimension2D size = getPreferredSize(e, parentBounds);
        // absPos is read only...
        Point2D absPos = (Point2D) e.getStyle().getStyleProperty(ABSOLUTE_POS);

        if (staticWidth)
        {
          width = (float)Math.max(size.getWidth() + absPos.getX(), width);
        }
        if (staticHeight)
        {
          height =(float) Math.max(size.getHeight() + absPos.getY(), height);
        }
      }
    }

    // now apply the minimum limit defined for that band in case the calculated height
    // is lower than the given minimum height.
    height = Math.max(height, (float) minSize.getHeight());
    width = Math.max(width, (float) minSize.getWidth());

    // now apply the maximum limit defined for that band in case the calculated height
    // is higher than the given max height.
    height = Math.min(height, maxH);
    width = Math.min(width, maxW);

    // calculate relative widths
    for (int i = 0; i < elements.length; i++)
    {
      Element e = elements[i];

      if (e.isVisible() == false)
        continue;

      boolean staticWidth = isElementStaticWidth(e);
      boolean staticHeight = isElementStaticHeight(e);
      if (staticWidth == false || staticHeight == false)
      {
        Dimension2D size = correctDimension(getPreferredSize(e, parentBounds), parentBounds);
        // absPos is not modified ...
        Point2D absPos = correctPoint((Point2D) e.getStyle().getStyleProperty(ABSOLUTE_POS), parentBounds);

        if (staticWidth == false)
        {
          width = Math.max((float) (size.getWidth() + absPos.getX()), width);
        }
        if (staticHeight == false)
        {
          height = Math.max((float) (size.getHeight() + absPos.getY()), height);
        }
      }
    }

    // now apply the minimum limit defined for that band in case the calculated height
    // is lower than the given minimum height.
    height = (float)Math.max(height, minSize.getHeight());
    width = (float)Math.max(width, minSize.getWidth());

    // now take the maximum limit defined for that band into account for a last time.
    // or specifying -140 would be a nice way to kill the layout ...
    height = (float)Math.min(height, maxSize.getHeight());
    width = (float)Math.min(width, maxSize.getWidth());

    // now align the calculated data ...
    FloatDimension fdim =  new FloatDimension(align(width, layoutSupport.getHorizontalAlignmentBorder()),
                                                align(height, layoutSupport.getVerticalAlignmentBorder()));
    return fdim;
  }

  private float align (float value, float boundry)
  {
    if (boundry == 0)
    {
      return value;
    }

    return (float) Math.floor(value / boundry) * boundry;
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

    // the preferred size of an band can be a relative value. Then this value is
    // relative to the container bounds
    Dimension2D minSize
        = correctDimension((Dimension2D) b.getStyle().getStyleProperty(ElementStyleSheet.MINIMUMSIZE),
                           containerBounds);

    float height = (float)Math.max(0, minSize.getHeight());
    float width = (float)Math.max(0, minSize.getWidth());

    //Log.debug ("MinimumLayoutSize: " + b.getName() + " > " + minSize);

    // now take the maximum limit defined for that band into account.
    Dimension2D maxSize
        = correctDimension((Dimension2D) b.getStyle().getStyleProperty(ElementStyleSheet.MAXIMUMSIZE), containerBounds);
    float maxW = (float) Math.min (maxSize.getWidth(), containerBounds.getWidth());
    float maxH = (float) Math.min (maxSize.getHeight(), containerBounds.getHeight());

    // the bounds inherited from the parent, cut down to the maximum size defined
    // for this elements.
    Dimension2D parentBounds = new FloatDimension(maxW, maxH);

    // Check the position of the elements inside and calculate the minimum width
    // needed to display all elements
    Element[] elements = b.getElementArray();

    // calculate absolute width
    for (int i = 0; i < elements.length; i++)
    {
      Element e = elements[i];

      if (e.isVisible() == false)
        continue;

      boolean staticWidth = isElementStaticWidth(e);
      boolean staticHeight = isElementStaticHeight(e);
      if (staticWidth || staticHeight)
      {
        Dimension2D size = getMinimumSize(e, parentBounds);
        // absPos is readonly ...
        Point2D absPos = (Point2D) e.getStyle().getStyleProperty(ABSOLUTE_POS);

        if (staticWidth)
        {
          width = (float)Math.max(size.getWidth() + absPos.getX(), width);
        }
        if (staticHeight)
        {
          height = (float)Math.max(size.getHeight() + absPos.getY(), height);
        }
      }
    }

    //Log.debug ("Dimension after static part: " + width + " -> " + height);
    // now apply the minimum limit defined for that band in case the calculated height
    // is lower than the given minimum height.
    height = (float)Math.max(height, minSize.getHeight());
    width = (float)Math.max(width, minSize.getWidth());

    // now apply the maximum limit defined for that band in case the calculated height
    // is higher than the given max height.
    height = (float)Math.min(height, maxSize.getHeight());
    width = (float)Math.min(width, maxSize.getWidth());

    FloatDimension base = new FloatDimension(width, height);
    //Log.debug ("Dimension after static correction: " + width + " -> " + height);

    // calculate relative widths
    for (int i = 0; i < elements.length; i++)
    {
      Element e = elements[i];

      if (e.isVisible() == false)
        continue;

      boolean staticWidth = isElementStaticWidth(e);
      boolean staticHeight = isElementStaticHeight(e);
      if (staticWidth == false || staticHeight == false)
      {
        Dimension2D size = correctDimension(getMinimumSize(e, base), base);
        Point2D absPos = correctPoint((Point2D) e.getStyle().getStyleProperty(ABSOLUTE_POS), base);

        if (staticWidth == false)
        {
          width = (float)Math.max(size.getWidth() + absPos.getX(), width);
        }
        if (staticHeight == false)
        {
          height = (float)Math.max(size.getHeight() + absPos.getY(), height);
        }
        //Log.debug ("Element " + e + " -> " + size);
      }
    }
    //Log.debug ("Dimension after dynamic part: " + width + " -> " + height);
    // now apply the minimum limit defined for that band in case the calculated height
    // is lower than the given minimum height.
    height =(float) Math.max(height, minSize.getHeight());
    width = (float)Math.max(width, minSize.getWidth());

    // now take the maximum limit defined for that band into account for a last time.
    height = (float)Math.min(height, maxSize.getHeight());
    width = (float)Math.min(width, maxSize.getWidth());

    //Log.debug ("Dimension after dynamic correction: " + maxSize);
    //Log.debug ("Dimension after dynamic correction: " + width + " -> " + height);
    // now align the calculated data ...
    FloatDimension fdim =  new FloatDimension(align(width, layoutSupport.getHorizontalAlignmentBorder()),
                                                align(height, layoutSupport.getVerticalAlignmentBorder()));
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
    /*
    if (b.isVisible() == false)
    {
      b.getStyle().setStyleProperty(ElementStyleSheet.BOUNDS, null);
      return;
    }
*/

    Element[] elements = b.getElementArray();
    Rectangle2D parentBounds = BandLayoutManagerUtil.getBounds(b, null);
    if (parentBounds == null)
    {
      throw new NullPointerException("Need the parent's bound set");
    }
 //   Log.warn("ParentBounds: " + parentBounds + " > " + b.getName());

    Dimension2D parentDim = new FloatDimension((float) parentBounds.getWidth(),
                                               (float) parentBounds.getHeight());
    //Point2D parentPoint = new Point2D.Double (parentBounds.getX(), parentBounds.getY());

    for (int i = 0; i < elements.length; i++)
    {
      Element e = elements[i];

      if (e.isVisible() == false)
        continue;

      Dimension2D uncorrectedSize = getPreferredSize(e, parentDim);
      Dimension2D size = correctDimension(uncorrectedSize, parentDim);

      Point2D absPos
          = correctPoint((Point2D) e.getStyle().getStyleProperty(ABSOLUTE_POS), parentDim);
      //absPos.setLocation(absPos.getX() + parentPoint.getX(), absPos.getY() + parentPoint.getY());

      // here apply the maximum bounds ...
      Rectangle2D bounds = new Rectangle2D.Float(align((float)absPos.getX(), layoutSupport.getHorizontalAlignmentBorder()),
                                                  align((float)absPos.getY(), layoutSupport.getVerticalAlignmentBorder()),
                                                  align((float)size.getWidth(), layoutSupport.getHorizontalAlignmentBorder()),
                                                  align((float)size.getHeight(), layoutSupport.getVerticalAlignmentBorder()));
      BandLayoutManagerUtil.setBounds(e, bounds);
//      Log.debug ("Bounds Defined: " + e.getName() + " > " + bounds);
//      Log.debug ("       Points : " + e.getName() + " > " + absPos + " -> "+ parentPoint);
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
    float newWidth = (float) dim.getWidth();
    if (dim.getWidth() < 0)
    {
      newWidth = (float)(dim.getWidth() * base.getWidth() / -100);
    }
    float newHeight = (float) dim.getHeight();
    if (dim.getHeight() < 0)
    {
      newHeight = (float) (dim.getHeight() * base.getHeight() / -100);
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
    return new Point2D.Float((float) x, (float) y);
  }

}
