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
 * ------------------------
 * StaticLayoutManager.java
 * ------------------------
 * (C)opyright 2002, 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: StaticLayoutManager.java,v 1.25 2003/03/30 17:05:13 taqua Exp $
 *
 * Changes
 * -------
 * 03-Dec-2002 : Javadocs (DG);
 * 21-Jan-2003 : removed ABSOLUTE_DIM StyleKey. The beast would cause trouble.
 * 01-Feb-2003 : BugFix: Layouting was not correct for subbands.
 * 24-Mar-2003 : Invisible bands will be layouted, invisible childs will be ignored.
 */

package com.jrefinery.report.targets.base.bandlayout;

import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import com.jrefinery.report.Band;
import com.jrefinery.report.Element;
import com.jrefinery.report.targets.FloatDimension;
import com.jrefinery.report.targets.base.layout.LayoutSupport;
import com.jrefinery.report.targets.base.ElementLayoutInformation;
import com.jrefinery.report.targets.style.ElementStyleSheet;
import com.jrefinery.report.targets.style.StyleKey;

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
 * Invisible elements within the layouted band are not evaluated. This layout manager
 * will ignore invisible child bands and -elements.
 * <p>
 * Note to everybody who tries to understand this class: This class is full of old
 * compatibility code, this class is not designed to be smart, or suitable for complex
 * layouts. The only purpose of this class is to maintain backward compatiblity with
 * older releases of JFreeReport.
 * <p>
 * The use of relative elements (the one's with 100% should be considered carefully,
 * as these elements are not fully predictable).
 *
 * @author Thomas Morgner
 */
public class StaticLayoutManager extends AbstractBandLayoutManager
{
  /** A key for the absolute position of an element. */
  public static final StyleKey ABSOLUTE_POS = StyleKey.getStyleKey("absolute_pos", Point2D.class);

  /**
   * A key for the dynamic height flag for an element.
   * @deprecated moved to ElementStyleSheet.DYNAMIC_HEIGHT
   */
  public static final StyleKey DYNAMIC_HEIGHT = ElementStyleSheet.DYNAMIC_HEIGHT;

  /**
   * Creates a new layout manager.
   */
  public StaticLayoutManager()
  {
    //elementCache = new LayoutManagerCache();
  }

  /**
   * Returns the minimum size for an element.
   *
   * @param e  the element.
   * @param containerBounds the bounds of the elements parents.
   *
   * @return the minimum size.
   */
  protected Dimension2D getMinimumSize(Element e, Dimension2D containerBounds)
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
    if (e.getStyle().getBooleanStyleProperty(ElementStyleSheet.DYNAMIC_HEIGHT))
    {
      retval = getElementContentBounds(retval, e, containerBounds);
    }

    // now apply the maximum bounds to the retval.
    // the maximum bounds are defined by the element and the elements container.
    Dimension2D maxSize = correctDimension((Dimension2D)
        e.getStyle().getStyleProperty(ElementStyleSheet.MAXIMUMSIZE), containerBounds);

    maxSize.setSize(Math.min (containerBounds.getWidth() - absPos.getX(), maxSize.getWidth()),
                    Math.min (containerBounds.getHeight() - absPos.getY(), maxSize.getHeight()));

    retval.setSize(Math.min (retval.getWidth(), maxSize.getWidth()),
                   Math.min (retval.getHeight(), maxSize.getHeight()));
    //Log.debug ("-- calculate MinimumSize: " + retval);
    // layouting has failed, if negative values are returned ... !
    if (retval.getWidth() < 0 || retval.getHeight() < 0)
    {
      throw new IllegalStateException("Layouting failed, getMinimumSize returned negative values.");
    }

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
  protected Dimension2D getPreferredSize(Element e, Dimension2D containerBounds)
  {
    Dimension2D retval;

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

    if (e.getStyle().getBooleanStyleProperty(ElementStyleSheet.DYNAMIC_HEIGHT))
    {
      retval = getElementContentBounds(retval, e, containerBounds);
    }

    // now apply the maximum bounds to the retval.
    // the maximum bounds are defined by the element and the elements container.
    Dimension2D maxSize = correctDimension(
        (Dimension2D) e.getStyle().getStyleProperty(ElementStyleSheet.MAXIMUMSIZE),
                                                    containerBounds);
    maxSize.setSize(Math.min (containerBounds.getWidth() - absPos.getX(), maxSize.getWidth()),
                    Math.min (containerBounds.getHeight() - absPos.getY(), maxSize.getHeight()));


    retval.setSize(Math.min (retval.getWidth(), maxSize.getWidth()),
                   Math.min (retval.getHeight(), maxSize.getHeight()));

    // layouting has failed, if negative values are returned ... !
    if (retval.getWidth() < 0 || retval.getHeight() < 0)
    {
      throw new IllegalStateException("Layouting failed, getPreferredSize returned negative values.");
    }

    return retval;
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
    ElementLayoutInformation eli = createLayoutInformationForPreferredSize(b, containerDims);
    Dimension2D maxSize = eli.getMaximumSize();
    Dimension2D minSize = eli.getMinimumSize();

    float height = (float) eli.getMinimumSize().getHeight();
//    float width = (float) eli.getMinimumSize().getWidth();
    float width = (float) maxSize.getWidth();

    // Now adjust the defined sizes by using the elements stored in the band.
    Element[] elements = b.getElementArray();

    // calculate absolute width
    for (int i = 0; i < elements.length; i++)
    {
      Element e = elements[i];

      if (e.isVisible() == false)
      {
        continue;
      }
      boolean staticWidth = isElementStaticWidth(e);
      boolean staticHeight = isElementStaticHeight(e);
      if (staticWidth || staticHeight)
      {
        Dimension2D size = correctDimension(getPreferredSize(e, maxSize), maxSize);
        // absPos is read only...
        Point2D absPos = correctPoint((Point2D) e.getStyle().getStyleProperty(ABSOLUTE_POS),
                                      maxSize);

        if (staticWidth)
        {
          width = (float) Math.max(size.getWidth() + absPos.getX(), width);
        }
        if (staticHeight)
        {
          height = (float) Math.max(size.getHeight() + absPos.getY(), height);
        }
      }
    }

    // now apply the minimum limit defined for that band in case the calculated height
    // is lower than the given minimum height.
    height = Math.max(height, (float) minSize.getHeight());
    width = Math.max(width, (float) minSize.getWidth());

    // now apply the maximum limit defined for that band in case the calculated height
    // is higher than the given max height.
    height = (float) Math.min(height, maxSize.getHeight());
    width = (float) Math.min(width, maxSize.getWidth());

    //Log.debug ("Dimension after static correction [PREF]: " + width + " -> " + height);
    FloatDimension base = new FloatDimension(width, height);

    // calculate relative widths
    for (int i = 0; i < elements.length; i++)
    {
      Element e = elements[i];

      if (e.isVisible() == false)
      {
        continue;
      }
      boolean staticWidth = isElementStaticWidth(e);
      boolean staticHeight = isElementStaticHeight(e);
      if (staticWidth == false || staticHeight == false)
      {
        Dimension2D size = correctDimension(getPreferredSize(e, base), base);
        // absPos is not modified ...
        Point2D absPos = correctPoint((Point2D) e.getStyle().getStyleProperty(ABSOLUTE_POS),
                                      base);

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
    height = (float) Math.max(height, minSize.getHeight());
    width = (float) Math.max(width, minSize.getWidth());

    // now take the maximum limit defined for that band into account for a last time.
    // or specifying -140 would be a nice way to kill the layout ...
    height = (float) Math.min(height, maxSize.getHeight());
    width = (float) Math.min(width, maxSize.getWidth());

    // now align the calculated data ...
    FloatDimension fdim
        = new FloatDimension(align(width, getLayoutSupport().getHorizontalAlignmentBorder()),
                             align(height, getLayoutSupport().getVerticalAlignmentBorder()));
    return fdim;
  }

  /**
   * Calculates the minimum layout size for a band. The width for the child elements
   * are not calculated, as we assume that the width's are defined fixed within the
   * parent.
   *
   * @param b  the band.
   * @param containerBounds the bounds of the bands parents.
   *
   * @return the minimum size.
   */
  public Dimension2D minimumLayoutSize(Band b, Dimension2D containerBounds)
  {
    ElementLayoutInformation eli = createLayoutInformationForMinimumSize(b, containerBounds);
    Dimension2D maxSize = eli.getMaximumSize();
    Dimension2D minSize = eli.getMinimumSize();

    float height = (float) eli.getMinimumSize().getHeight();
//    float width = (float) eli.getMinimumSize().getWidth();
    float width = (float) maxSize.getWidth();

    // Check the position of the elements inside and calculate the minimum width
    // needed to display all elements
    Element[] elements = b.getElementArray();

    // calculate absolute width
    for (int i = 0; i < elements.length; i++)
    {
      Element e = elements[i];

      if (e.isVisible() == false)
      {
        continue;
      }
      boolean staticWidth = isElementStaticWidth(e);
      boolean staticHeight = isElementStaticHeight(e);
      if (staticWidth || staticHeight)
      {
        Dimension2D size = getMinimumSize(e, maxSize);
        // absPos is readonly ...
        Point2D absPos = (Point2D) e.getStyle().getStyleProperty(ABSOLUTE_POS);

        if (staticWidth)
        {
          width = (float) Math.max(size.getWidth() + absPos.getX(), width);
        }
        if (staticHeight)
        {
          height = (float) Math.max(size.getHeight() + absPos.getY(), height);
        }
      }
    }

    //Log.debug ("Dimension after static part: " + width + " -> " + height);
    // now apply the minimum limit defined for that band in case the calculated height
    // is lower than the given minimum height.
    height = (float) Math.max(height, minSize.getHeight());
    width = (float) Math.max(width, minSize.getWidth());

    // now apply the maximum limit defined for that band in case the calculated height
    // is higher than the given max height.
    height = (float) Math.min(height, maxSize.getHeight());
    width = (float) Math.min(width, maxSize.getWidth());

    //Log.debug ("Dimension after static correction [MIN]: " + width + " -> " + height);
    FloatDimension base = new FloatDimension(width, height);

    // calculate relative widths
    for (int i = 0; i < elements.length; i++)
    {
      Element e = elements[i];

      if (e.isVisible() == false)
      {
        continue;
      }
      boolean staticWidth = isElementStaticWidth(e);
      boolean staticHeight = isElementStaticHeight(e);
      if (staticWidth == false || staticHeight == false)
      {
        Dimension2D size = correctDimension(getMinimumSize(e, base), base);
        Point2D absPos = correctPoint((Point2D) e.getStyle().getStyleProperty(ABSOLUTE_POS), base);

        if (staticWidth == false)
        {
          width = (float) Math.max(size.getWidth() + absPos.getX(), width);
        }
        if (staticHeight == false)
        {
          height = (float) Math.max(size.getHeight() + absPos.getY(), height);
        }
        //Log.debug ("Element " + e + " -> " + size);
      }
    }

    //Log.debug ("Dimension after dynamic part: " + width + " -> " + height);
    // now apply the minimum limit defined for that band in case the calculated height
    // is lower than the given minimum height.
    height = (float) Math.max(height, minSize.getHeight());
    width = (float) Math.max(width, minSize.getWidth());

    // now take the maximum limit defined for that band into account for a last time.
    height = (float) Math.min(height, maxSize.getHeight());
    width = (float) Math.min(width, maxSize.getWidth());

    //Log.debug ("Dimension after dynamic correction: " + maxSize);
    //Log.debug ("Dimension after dynamic correction: " + width + " -> " + height);
    // now align the calculated data ...
    FloatDimension fdim = new FloatDimension(
        align(width, getLayoutSupport().getHorizontalAlignmentBorder()),
        align(height, getLayoutSupport().getVerticalAlignmentBorder()));
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
   * @throws IllegalStateException if the bands has no bounds set.
   */
  public void doLayout(Band b)
  {
    //elementCache.setCurrentBand(b);

    Element[] elements = b.getElementArray();
    Rectangle2D parentBounds = BandLayoutManagerUtil.getBounds(b, null);
    if (parentBounds == null)
    {
      throw new IllegalStateException("Need the parent's bound set");
    }

    Dimension2D parentDim = new FloatDimension((float) parentBounds.getWidth(),
                                               (float) parentBounds.getHeight());

    //Log.debug ("My LayoutSize: " + b.getName() + " " + parentDim);

    LayoutSupport layoutSupport = getLayoutSupport();
    for (int i = 0; i < elements.length; i++)
    {
      Element e = elements[i];

      if (e.isVisible() == false)
      {
        continue;
      }
      Dimension2D uncorrectedSize = getPreferredSize(e, parentDim);
      //Log.debug ("UBounds: Element: " + e.getName() + " Bounds: " + uncorrectedSize);
      Dimension2D size = correctDimension(uncorrectedSize, parentDim);
      //Log.debug ("CBounds: Element: " + e.getName() + " Bounds: " + size);

      Point2D absPos
          = correctPoint((Point2D) e.getStyle().getStyleProperty(ABSOLUTE_POS), parentDim);

      // here apply the maximum bounds ...
      Rectangle2D bounds = new Rectangle2D.Float(
          align((float) absPos.getX(), layoutSupport.getHorizontalAlignmentBorder()),
          align((float) absPos.getY(), layoutSupport.getVerticalAlignmentBorder()),
          align((float) size.getWidth(), layoutSupport.getHorizontalAlignmentBorder()),
          align((float) size.getHeight(), layoutSupport.getVerticalAlignmentBorder()));
      BandLayoutManagerUtil.setBounds(e, bounds);
      //Log.debug ("Bounds: Element: " + e.getName() + " Bounds: " + bounds);
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
  protected boolean isElementStaticWidth (Element e)
  {
    Point2D absPos = (Point2D) e.getStyle().getStyleProperty(ABSOLUTE_POS);
    if (absPos == null)
    {
      throw new IllegalArgumentException("Element " + e
                                         + " has no valid constraints. ABSOLUTE_POS is missing");
    }
    if (absPos.getX() < 0)
    {
      return false;
    }
    return super.isElementStaticWidth(e);
  }

  /**
   * Returns true if the element has a static height, and false otherwise.
   *
   * @param e  the element.
   *
   * @return true or false.
   */
  protected boolean isElementStaticHeight (Element e)
  {
    Point2D absPos = (Point2D) e.getStyle().getStyleProperty(ABSOLUTE_POS);
    if (absPos == null)
    {
      throw new IllegalArgumentException("Element " + e
                                         + " has no valid constraints. ABSOLUTE_POS is missing");
    }
    if (absPos.getY() < 0)
    {
      return false;
    }
    return super.isElementStaticHeight(e);
  }

  /**
   * Clears any cached items used by the layout manager.
   */
  public void invalidateLayout(Band container)
  {
  }

  /**
   * Adds the specified component to the layout, the specified constraints are stored
   * in the elements style sheet.
   *
   * @param e the element to be added to the layout manager.
   */
  public void addLayoutElement(Element e)
  {
  }

  /**
   * Removed the specified component from the layout.
   *
   * @param e the element to be removed from the layout manager.
   */
  public void removeLayoutElement(Element e)
  {
  }
}
