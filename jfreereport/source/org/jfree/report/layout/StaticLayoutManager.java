/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
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
 * $Id: StaticLayoutManager.java,v 1.10 2003/10/11 21:32:46 taqua Exp $
 *
 * Changes
 * -------
 * 03-Dec-2002 : Javadocs (DG);
 * 21-Jan-2003 : removed ABSOLUTE_DIM StyleKey. The beast would cause trouble.
 * 01-Feb-2003 : BugFix: Layouting was not correct for subbands.
 * 24-Mar-2003 : Invisible bands will be layouted, invisible childs will be ignored.
 */

package org.jfree.report.layout;

import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import org.jfree.report.Band;
import org.jfree.report.Element;
import org.jfree.report.style.ElementStyleSheet;
import org.jfree.report.style.StyleKey;
import org.jfree.report.util.ElementLayoutInformation;
import org.jfree.ui.FloatDimension;

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
public strictfp class StaticLayoutManager extends AbstractBandLayoutManager
{
  /** A key for the absolute position of an element. */
  public static final StyleKey ABSOLUTE_POS = StyleKey.getStyleKey("absolute_pos", Point2D.class);

  /** The default position. */
  private static final Point2D DEFAULT_POS = new Point2D.Float(0, 0);

  /** A cache. */
  private final LayoutManagerCache cache;

  /** A cache key. */
  private final LayoutSearchKey cacheKey;

  /**
   * Creates a new layout manager.
   */
  public StaticLayoutManager()
  {
    cache = new LayoutManagerCache();
    cacheKey = new LayoutSearchKey();
  }

  /**
   * Returns the minimum size for an element.
   *
   * @param e  the element.
   * @param containerBounds the bounds of the elements parents.
   * @param retval a dimension object that should be filled, or null,
   * if a new object should be created
   *
   * @return the minimum size.
   */
  protected Dimension2D computeMinimumSize(final Element e, final Dimension2D containerBounds,
                                           Dimension2D retval)
  {
    final boolean isCacheable = cache.isCachable(e);
    if (isCacheable)
    {
      cacheKey.setSearchConstraint(e, containerBounds);
      final Dimension2D cretval = cache.getMinSize(cacheKey);
      if (cretval != null)
      {
        return cretval;
      }
    }

    // the absolute position of the element within its parent is used
    // to calculate the maximum available space for the element.
    final Point2D absPos = (Point2D)
      e.getStyle().getStyleProperty(ABSOLUTE_POS, DEFAULT_POS);
    final float absPosX = correctRelativeValue((float) absPos.getX(),
        (float) containerBounds.getWidth());
    final float absPosY = correctRelativeValue((float) absPos.getY(),
        (float) containerBounds.getHeight());

    if (containerBounds.getWidth() < absPosX)
    {
      // Element is not within the range of the container. Abort, Retry, Ignore?
      throw new IllegalStateException("Width violation!");
    }
    if (containerBounds.getHeight() < absPosY)
    {
      // Element is not within the range of the container. Abort, Retry, Ignore?
      throw new IllegalStateException("Height violation!");
    }

    // if this is a band, then try to calculate the min-size
    if (e instanceof Band)
    {
      final BandLayoutManager lm = BandLayoutManagerUtil.getLayoutManager(e, getLayoutSupport());
      retval = lm.minimumLayoutSize((Band) e, containerBounds);
    }
    else
    {
      // return the minimum size as fallback
      final Dimension2D dim = (Dimension2D)
          e.getStyle().getStyleProperty(ElementStyleSheet.MINIMUMSIZE);
      retval = correctDimension(dim, containerBounds, retval);
    }

    // now apply the maximum bounds to the retval.
    // the maximum bounds are defined by the element and the elements container.
    final Dimension2D maxSize = correctDimension((Dimension2D)
        e.getStyle().getStyleProperty(ElementStyleSheet.MAXIMUMSIZE), containerBounds, null);

    maxSize.setSize(Math.min(containerBounds.getWidth() - absPosX, maxSize.getWidth()),
        Math.min(containerBounds.getHeight() - absPosY, maxSize.getHeight()));

    if (e.getStyle().getBooleanStyleProperty(ElementStyleSheet.DYNAMIC_HEIGHT))
    {
      retval = getElementContentBounds(retval, e, maxSize);
    }

    retval.setSize(Math.min(retval.getWidth(), maxSize.getWidth()),
        Math.min(retval.getHeight(), maxSize.getHeight()));

    //Log.debug ("-- calculate MinimumSize: " + retval);
    // layouting has failed, if negative values are returned ... !
    if (retval.getWidth() < 0 || retval.getHeight() < 0)
    {
      throw new IllegalStateException
        ("Layouting failed, computeMinimumSize returned negative values.");
    }

    if (isCacheable)
    {
      cache.setMinSize(cacheKey, e, retval);
    }
    return retval;
  }

  /**
   * Calculates the preferred size of an element.
   *
   * @param e  the element.
   * @param containerBounds  the bounds of the element's container.
   * @param retval a dimension object that should be filled, or null,
   * if a new object should be created
   *
   * @return the preferred size of the element.
   */
  protected Dimension2D computePreferredSize(final Element e,
      final Dimension2D containerBounds, Dimension2D retval)
  {
    final boolean isCachable = cache.isCachable(e);
    if (isCachable)
    {
      cacheKey.setSearchConstraint(e, containerBounds);
      final Dimension2D cretval = cache.getMinSize(cacheKey);
      if (cretval != null)
      {
        // Log.debug ("+" + cretval);
        return cretval;
      }
    }

    // the absolute position of the element within its parent is used
    // to calculate the maximum available space for the element.
    final Point2D absPos = (Point2D)
      e.getStyle().getStyleProperty(ABSOLUTE_POS, DEFAULT_POS);
    final float absPosX = correctRelativeValue((float) absPos.getX(),
        (float) containerBounds.getWidth());
    final float absPosY = correctRelativeValue((float) absPos.getY(),
        (float) containerBounds.getHeight());

    if (containerBounds.getWidth() < absPosX)
    {
      // Element is not within the range of the container. Abort, Retry, Ignore?
      throw new IllegalStateException("Width violation!");
    }
    if (containerBounds.getHeight() < absPosY)
    {
      // Element is not within the range of the container. Abort, Retry, Ignore?
      throw new IllegalStateException("Height violation!");
    }

    // if e is a band, then try to calculate the preferred size
    if (e instanceof Band)
    {
      final BandLayoutManager lm = BandLayoutManagerUtil.getLayoutManager(e, getLayoutSupport());
      retval = lm.preferredLayoutSize((Band) e, containerBounds);
    }
    else
    {
      // if prefsize is defined, return it
      final Dimension2D d = (Dimension2D)
          e.getStyle().getStyleProperty(ElementStyleSheet.PREFERREDSIZE);
      if (d != null)
      {
        retval = correctDimension(d, containerBounds, retval);
      }
      else
      {
        // return the absolute dimension as fallback
        retval = correctDimension((Dimension2D)
            e.getStyle().getStyleProperty(ElementStyleSheet.MINIMUMSIZE), containerBounds, retval);
      }
    }

    // now apply the maximum bounds to the retval.
    // the maximum bounds are defined by the element and the elements container.
    final Dimension2D maxSize = correctDimension(
        (Dimension2D) e.getStyle().getStyleProperty(ElementStyleSheet.MAXIMUMSIZE),
        containerBounds, null);
    maxSize.setSize(Math.min(containerBounds.getWidth() - absPosX, maxSize.getWidth()),
        Math.min(containerBounds.getHeight() - absPosY, maxSize.getHeight()));

    if (e.getStyle().getBooleanStyleProperty(ElementStyleSheet.DYNAMIC_HEIGHT))
    {
      retval = getElementContentBounds(retval, e, maxSize);
    }

    retval.setSize(Math.min(retval.getWidth(), maxSize.getWidth()),
        Math.min(retval.getHeight(), maxSize.getHeight()));

    // layouting has failed, if negative values are returned ... !
    if (retval.getWidth() < 0 || retval.getHeight() < 0)
    {
      throw new IllegalStateException(
          "Layouting failed, computePreferredSize returned negative values." + e.getName());
    }

    if (isCachable)
    {
      cache.setPrefSize(cacheKey, e, retval);
    }
    //Log.debug ("+" + retval);
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
  public Dimension2D preferredLayoutSize(final Band b, final Dimension2D containerDims)
  {
    synchronized (b.getTreeLock())
    {
      //Log.debug(">" + containerDims + " vs - " + b.getName());
      final ElementLayoutInformation eli =
        createLayoutInformationForPreferredSize(b, containerDims);
      final Dimension2D maxSize = eli.getMaximumSize();
      final Dimension2D minSize = eli.getMinimumSize();

      final FloatDimension base = new FloatDimension
          ((float) maxSize.getWidth(), (float) maxSize.getHeight());

      float height = (float) minSize.getHeight();
      float width = (float) minSize.getWidth();

      // Now adjust the defined sizes by using the elements stored in the band.
      final Element[] elements = b.getElementArray();

      final Dimension2D tmpResult = null;
      // calculate absolute width
      for (int i = 0; i < elements.length; i++)
      {
        final Element e = elements[i];

        if (e.isVisible() == false)
        {
          continue;
        }
        final boolean staticWidth = isElementStaticWidth(e);
        final boolean staticHeight = isElementStaticHeight(e);
        if (staticWidth || staticHeight)
        {
          final Point2D absPos = (Point2D) e.getStyle().getStyleProperty(ABSOLUTE_POS, DEFAULT_POS);

          // check whether the element would be visible .. if not visible, then
          // dont do anything ...
          if (absPos.getX() > maxSize.getWidth() || absPos.getY() > maxSize.getHeight())
          {
            // dont display, as this element is larger than the container ...
            continue;
          }
          final Dimension2D absDim = computePreferredSize(e, base, tmpResult);

          if (staticWidth)
          {
            width = (float) Math.max(absDim.getWidth() + absPos.getX(), width);
          }
          if (staticHeight)
          {
            height = (float) Math.max(absDim.getHeight() + absPos.getY(), height);
          }
        }
      }

      // now apply the minimum limit defined for that band in case the calculated height
      // is lower than the given minimum height.
      height = Math.max(height, (float) minSize.getHeight());
      width = Math.max(width, (float) minSize.getWidth());

      // now apply the maximum limit defined for that band in case the calculated height
      // is higher than the given max height.
      height = (float)Math.min(height, maxSize.getHeight());
      width = (float) Math.min(width, maxSize.getWidth());

      // Log.debug ("Mid Stream; " + width + ", " + height);
      // Log.debug ("Dimension after static correction [PREF]: " + width + " -> " + height);
      // final FloatDimension base = new FloatDimension(width, height);
      base.setHeight(height);

      Point2D absPos = null;
      Dimension2D absDim = null;

      // calculate relative widths
      for (int i = 0; i < elements.length; i++)
      {
        final Element e = elements[i];

        if (e.isVisible() == false)
        {
          continue;
        }
        final boolean staticWidth = isElementStaticWidth(e);
        final boolean staticHeight = isElementStaticHeight(e);
        if (staticWidth == false || staticHeight == false)
        {
          absPos = correctPoint((Point2D) e.getStyle().getStyleProperty(ABSOLUTE_POS, DEFAULT_POS),
              base, absPos);
          // check whether the element would be visible .. if not visible, then
          // dont do anything ...
          if (absPos.getX() > base.getWidth() || absPos.getY() > base.getHeight())
          {
            // dont display, as this element is larger than the container ...
            continue;
          }

          absDim = correctDimension(computePreferredSize(e, base, absDim), base, absDim);

          if (staticWidth == false)
          {
            width = Math.max((float) (absDim.getWidth() + absPos.getX()), width);
          }
          if (staticHeight == false)
          {
            height = Math.max((float) (absDim.getHeight() + absPos.getY()), height);
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
      base.setSize(align(width, getLayoutSupport().getHorizontalAlignmentBorder()),
          align(height, getLayoutSupport().getVerticalAlignmentBorder()));

      // Log.debug("<" + base + " vs - " + b.getName());
      return base;
    }
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
  public Dimension2D minimumLayoutSize(final Band b, final Dimension2D containerBounds)
  {
    synchronized (b.getTreeLock())
    {
      final ElementLayoutInformation eli =
        createLayoutInformationForMinimumSize(b, containerBounds);
      final Dimension2D maxSize = eli.getMaximumSize();
      final Dimension2D minSize = eli.getMinimumSize();

      // we use the max width, as the width is bound to the outside container,
      // either an other band or the page; the width is required to compute dynamic
      // elements or elements with an 100% width ...
      float height = (float) minSize.getHeight();
      float width = (float) maxSize.getWidth();

      // Check the position of the elements inside and calculate the minimum width
      // needed to display all elements
      final Element[] elements = b.getElementArray();
      final Dimension2D tmpResult = null;

      // calculate absolute width
      for (int i = 0; i < elements.length; i++)
      {
        final Element e = elements[i];

        if (e.isVisible() == false)
        {
          continue;
        }
        final boolean staticWidth = isElementStaticWidth(e);
        final boolean staticHeight = isElementStaticHeight(e);
        if (staticWidth || staticHeight)
        {
          // absPos is readonly ...
          final Point2D absPos = (Point2D) e.getStyle().getStyleProperty(ABSOLUTE_POS, DEFAULT_POS);
          // check whether the element would be visible .. if not visible, then
          // dont do anything ...
          if (absPos.getX() > maxSize.getWidth() || absPos.getY() > maxSize.getHeight())
          {
            // dont display, as this element is larger than the container ...
            continue;
          }
          final Dimension2D size = computeMinimumSize(e, maxSize, tmpResult);

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
      final FloatDimension base = new FloatDimension(width, height);
      Point2D absPos = null;
      Dimension2D absDim = null;

      // calculate relative widths
      for (int i = 0; i < elements.length; i++)
      {
        final Element e = elements[i];

        if (e.isVisible() == false)
        {
          continue;
        }
        final boolean staticWidth = isElementStaticWidth(e);
        final boolean staticHeight = isElementStaticHeight(e);
        if (staticWidth == false || staticHeight == false)
        {
          absPos = correctPoint((Point2D)
              e.getStyle().getStyleProperty(ABSOLUTE_POS, DEFAULT_POS), base, absPos);
          // check whether the element would be visible .. if not visible, then
          // dont do anything ...
          if (absPos.getX() > base.getWidth() || absPos.getY() > base.getHeight())
          {
            // dont display, as this element is larger than the container ...
            continue;
          }
          absDim = correctDimension(computeMinimumSize(e, base, absDim), base, absDim);

          if (staticWidth == false)
          {
            width = (float) Math.max(absDim.getWidth() + absPos.getX(), width);
          }
          if (staticHeight == false)
          {
            height = (float) Math.max(absDim.getHeight() + absPos.getY(), height);
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
      final FloatDimension fdim = new FloatDimension(
          align(width, getLayoutSupport().getHorizontalAlignmentBorder()),
          align(height, getLayoutSupport().getVerticalAlignmentBorder()));
      return fdim;
    }
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
   * @throws java.lang.IllegalStateException if the bands has no bounds set.
   */
  public synchronized void doLayout(final Band b)
  {
    synchronized (b.getTreeLock())
    {
      final Element[] elements = b.getElementArray();
      final Rectangle2D parentBounds = BandLayoutManagerUtil.getBounds(b, null);
      if (parentBounds == null)
      {
        throw new IllegalStateException("Need the parent's bound set");
      }

      final Dimension2D parentDim = new FloatDimension((float) parentBounds.getWidth(),
          (float) parentBounds.getHeight());

      // Log.debug ("My LayoutSize: " + b.getName() + " " + parentDim);

      Dimension2D absDim = null;
      Point2D absPos = null;
      final LayoutSupport layoutSupport = getLayoutSupport();
      for (int i = 0; i < elements.length; i++)
      {
        final Element e = elements[i];

        if (e.isVisible() == false)
        {
          continue;
        }
        absPos = correctPoint((Point2D)
            e.getStyle().getStyleProperty(ABSOLUTE_POS, DEFAULT_POS), parentDim, absPos);
        // check whether the element would be visible .. if not visible, then
        // dont do anything ...
        if (absPos.getX() > parentDim.getWidth() || absPos.getY() > parentDim.getHeight())
        {
          // dont display, as this element is larger than the container ...
          // Log.debug ("Element Out of Bounds: " + e);
          BandLayoutManagerUtil.setBounds(e, new Rectangle2D.Float(0, 0, 0, 0));
          continue;
        }

        absDim = computePreferredSize(e, parentDim, absDim);
        // docmark: Compute preferred size does never return negative values!
        // Log.debug ("UBounds: Element: " + e.getName() + " Bounds: " + uncorrectedSize);
        // absDim = correctDimension(uncorrectedSize, parentDim, absDim);
        // Log.debug ("CBounds: Element: " + e.getName() + " Bounds: " + absDim);

        // here apply the maximum bounds ...
        final Rectangle2D bounds = new Rectangle2D.Float(
            align((float) absPos.getX(), layoutSupport.getHorizontalAlignmentBorder()),
            align((float) absPos.getY(), layoutSupport.getVerticalAlignmentBorder()),
            align((float) absDim.getWidth(), layoutSupport.getHorizontalAlignmentBorder()),
            align((float) absDim.getHeight(), layoutSupport.getVerticalAlignmentBorder()));
        BandLayoutManagerUtil.setBounds(e, bounds);
        // Log.debug ("Bounds: Element: " + e.getName() + " Bounds: " + bounds);
        if (e instanceof Band)
        {
          final BandLayoutManager lm =
            BandLayoutManagerUtil.getLayoutManager(e, getLayoutSupport());
          lm.doLayout((Band) e);
        }
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
  protected boolean isElementStaticWidth(final Element e)
  {
    final Point2D absPos = (Point2D) e.getStyle().getStyleProperty(ABSOLUTE_POS, DEFAULT_POS);
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
  protected boolean isElementStaticHeight(final Element e)
  {
    final Point2D absPos = (Point2D) e.getStyle().getStyleProperty(ABSOLUTE_POS, DEFAULT_POS);
    if (absPos.getY() < 0)
    {
      return false;
    }
    return super.isElementStaticHeight(e);
  }

  /**
   * Clears any cached items used by the layout manager.
   *
   * @param container  the container band.
   */
  public void invalidateLayout(final Band container)
  {
    synchronized (container.getTreeLock())
    {
      cache.flush();
    }
  }
}
