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
 * $Id: StaticLayoutManager.java,v 1.21 2005/03/03 21:50:39 taqua Exp $
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

import org.jfree.report.Band;
import org.jfree.report.Element;
import org.jfree.report.style.ElementStyleSheet;
import org.jfree.report.style.StyleKey;
import org.jfree.report.util.ElementLayoutInformation;
import org.jfree.report.util.geom.StrictBounds;
import org.jfree.report.util.geom.StrictDimension;
import org.jfree.report.util.geom.StrictGeomUtility;
import org.jfree.report.util.geom.StrictPoint;

/**
 * An implementation of the BandLayoutManager interface.
 * <p/>
 * Rule: Bands can have minimum, max and pref size defined. These values are hints for the
 * layout container, no restrictions. If min and pref are '0', they are ignored. MaxSize
 * is never ignored.
 * <p/>
 * Elements that have the "dynamic" flag set, are checked for their content-bounds. This
 * operation is expensive, so this is only done if really needed. The dynamic flag will
 * influence the height of an element, a valid width must be already set.
 * <p/>
 * Invisible elements within the layouted band are not evaluated. This layout manager will
 * ignore invisible child bands and -elements.
 * <p/>
 * Note to everybody who tries to understand this class: This class is full of old
 * compatibility code, this class is not designed to be smart, or suitable for complex
 * layouts. The only purpose of this class is to maintain backward compatiblity with older
 * releases of JFreeReport.
 * <p/>
 * The use of relative elements (the one's with 100% should be considered carefully, as
 * these elements are not fully predictable).
 *
 * @author Thomas Morgner
 */
public strictfp class StaticLayoutManager extends AbstractBandLayoutManager
{
  /**
   * A key for the absolute position of an element.
   */
  public static final StyleKey ABSOLUTE_POS = StyleKey.getStyleKey("absolute_pos", Point2D.class);

  /**
   * The default position.
   */
  private static final Point2D DEFAULT_POS = new Point2D.Float(0, 0);

  /**
   * Creates a new layout manager.
   */
  public StaticLayoutManager ()
  {
  }

  /**
   * Returns the minimum size for an element.
   *
   * @param e               the element.
   * @param containerBounds the bounds of the elements parents.
   * @param retval          a dimension object that should be filled, or null, if a new
   *                        object should be created
   * @param support         the layout support used to compute sizes.
   * @return the minimum size.
   */
  protected StrictDimension computeMinimumSize
          (final Element e, final StrictDimension containerBounds,
           StrictDimension retval, final LayoutSupport support,
           final boolean allowCaching)
  {
    final LayoutManagerCache cache = support.getCache();
    final boolean isCacheable = cache.isCachable(e) && allowCaching;
    if (isCacheable)
    {
      final StrictDimension cretval = cache.getMinSize(e.getObjectID());
      if (cretval != null)
      {
        return cretval;
      }
    }

    // the absolute position of the element within its parent is used
    // to calculate the maximum available space for the element.
    final Point2D absPos = (Point2D)
            e.getStyle().getStyleProperty(ABSOLUTE_POS, DEFAULT_POS);
    final long absPosX = alignDown(correctRelativeValue
            (StrictGeomUtility.toInternalValue(absPos.getX()), containerBounds.getWidth()),
            support.getInternalHorizontalAlignmentBorder());
    final long absPosY = alignDown(correctRelativeValue
            (StrictGeomUtility.toInternalValue(absPos.getY()), containerBounds.getHeight()),
            support.getInternalVerticalAlignmentBorder());

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
      final BandLayoutManager lm = BandLayoutManagerUtil.getLayoutManager(e);
      retval = lm.minimumLayoutSize((Band) e, containerBounds, support);
    }
    else
    {
      // return the minimum size as fallback
      final Dimension2D dim = (Dimension2D)
              e.getStyle().getStyleProperty(ElementStyleSheet.MINIMUMSIZE);
      final StrictDimension sDim =
              StrictGeomUtility.createDimension(dim.getWidth(), dim.getHeight());
      retval = correctDimension(sDim, containerBounds, retval, support);
    }

    // now apply the maximum bounds to the retval.
    // the maximum bounds are defined by the element and the elements container.
    final Dimension2D maxSizeElement = (Dimension2D)
            e.getStyle().getStyleProperty(ElementStyleSheet.MAXIMUMSIZE);

    final StrictDimension maxSize =
            correctDimension(StrictGeomUtility.createDimension
            (maxSizeElement.getWidth(), maxSizeElement.getHeight()),
                    containerBounds, null, support);

    maxSize.setSize(Math.min(containerBounds.getWidth() - absPosX, maxSize.getWidth()),
            Math.min(containerBounds.getHeight() - absPosY, maxSize.getHeight()));

    if (e.getStyle().getBooleanStyleProperty(ElementStyleSheet.DYNAMIC_HEIGHT))
    {
      retval = getElementContentBounds(retval, e, containerBounds, support);
    }

    retval.setSize
           (alignUp(Math.min(retval.getWidth(), maxSize.getWidth()),
                   support.getInternalHorizontalAlignmentBorder()),
            alignUp(Math.min(retval.getHeight(), maxSize.getHeight()),
                    support.getInternalVerticalAlignmentBorder()));

    //Log.debug ("-- calculate MinimumSize: " + retval);
    // layouting has failed, if negative values are returned ... !
    if (retval.getWidth() < 0 || retval.getHeight() < 0)
    {
      throw new IllegalStateException
              ("Layouting failed, computeMinimumSize returned negative values.");
    }

    if (isCacheable)
    {
      cache.setMinSize(e, retval);
    }
    return retval;
  }

  /**
   * Calculates the preferred size of an element.
   *
   * @param e               the element.
   * @param containerBounds the bounds of the element's container.
   * @param support         the layout support used to compute sizes.
   * @return the preferred size of the element.
   */
  protected StrictDimension computePreferredSize (final Element e,
                                                  final StrictDimension containerBounds,
                                                  StrictDimension retval,
                                                  final LayoutSupport support,
                                                  final boolean allowCaching)
  {
    final LayoutManagerCache cache = support.getCache();
    final boolean isCachable = cache.isCachable(e) && allowCaching;
    if (isCachable)
    {
      final StrictDimension cretval = cache.getPrefSize(e.getObjectID());
      if (cretval != null)
      {
        return cretval;
      }
    }

    final long alignedX = support.getInternalHorizontalAlignmentBorder();
    final long alignedY = support.getInternalVerticalAlignmentBorder();

    // the absolute position of the element within its parent is used
    // to calculate the maximum available space for the element.
    final Point2D absPos = (Point2D)
            e.getStyle().getStyleProperty(ABSOLUTE_POS, DEFAULT_POS);
    final long absPosX = alignDown(correctRelativeValue
            (StrictGeomUtility.toInternalValue(absPos.getX()),
                    containerBounds.getWidth()), alignedX);
    final long absPosY = alignDown(correctRelativeValue
            (StrictGeomUtility.toInternalValue(absPos.getY()),
                    containerBounds.getHeight()), alignedY);

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
      final BandLayoutManager lm = BandLayoutManagerUtil.getLayoutManager(e);
      retval = lm.preferredLayoutSize((Band) e, containerBounds, support);
    }
    else
    {
      // if prefsize is defined, return it
      final Dimension2D d = (Dimension2D)
              e.getStyle().getStyleProperty(ElementStyleSheet.PREFERREDSIZE);
      if (d != null)
      {
        final StrictDimension sDim =
                StrictGeomUtility.createDimension(d.getWidth(), d.getHeight());
        retval = correctDimension(sDim, containerBounds, null, support);
      }
      else
      {
        // return the absolute dimension as fallback
        final Dimension2D minDim = (Dimension2D)
                e.getStyle().getStyleProperty(ElementStyleSheet.MINIMUMSIZE);
        final StrictDimension sDim =
                StrictGeomUtility.createDimension(minDim.getWidth(), minDim.getHeight());
        retval = correctDimension(sDim, containerBounds, null, support);
      }
    }

    // now apply the maximum bounds to the retval.
    // the maximum bounds are defined by the element and the elements container.
    final Dimension2D maxDim = (Dimension2D)
            e.getStyle().getStyleProperty(ElementStyleSheet.MAXIMUMSIZE);
    final StrictDimension sMaxDim =
            StrictGeomUtility.createDimension(maxDim.getWidth(), maxDim.getHeight());
    final StrictDimension maxSize = correctDimension(sMaxDim, containerBounds, null, support);

    maxSize.setSize(Math.min(containerBounds.getWidth() - absPosX, maxSize.getWidth()),
            Math.min(containerBounds.getHeight() - absPosY, maxSize.getHeight()));

    if (e.getStyle().getBooleanStyleProperty(ElementStyleSheet.DYNAMIC_HEIGHT))
    {
      retval = getElementContentBounds(retval, e, containerBounds, support);
    }

    retval.setSize
           (alignUp(Math.min(retval.getWidth(), maxSize.getWidth()), alignedX),
            alignUp(Math.min(retval.getHeight(), maxSize.getHeight()), alignedY));

    // layouting has failed, if negative values are returned ... !
    if (retval.getWidth() < 0 || retval.getHeight() < 0)
    {
      throw new IllegalStateException("Layouting failed, computePreferredSize returned negative values." + e.getName());
    }

    if (isCachable)
    {
      cache.setPrefSize(e, retval);
    }
    //Log.debug ("+" + retval);
    return retval;
  }

  /**
   * Calculates the preferred layout size for a band. The band is limited to the given
   * container bounds as well as to the own maximum size.
   * <p/>
   * The preferred size of non-absolute elements is calculated by using the parents
   * dimensions supplied in containerDims. Elements with a width or height of 100% will
   * consume all available space of the parent.
   *
   * @param b             the band.
   * @param containerDims the maximum size the band should use for that container.
   * @param support       the layout support used to compute sizes.
   * @return the preferred size.
   */
  public StrictDimension preferredLayoutSize
          (final Band b, final StrictDimension containerDims,
           final LayoutSupport support)
  {
    if (support == null)
    {
      throw new NullPointerException("LayoutSupport is null.");
    }
    if (b == null)
    {
      throw new NullPointerException("Band is null.");
    }
    if (containerDims == null)
    {
      throw new NullPointerException("ContainerBounds are null.");
    }
    final LayoutManagerCache cache = support.getCache();

    final boolean isCacheable = cache.isCachable(b);
    if (isCacheable)
    {
      final StrictDimension cretval = cache.getPrefSize(b.getObjectID());
      if (cretval != null)
      {
        return cretval;
      }
    }
    final long hAlignBorder = support.getInternalHorizontalAlignmentBorder();
    final long vAlignBorder = support.getInternalVerticalAlignmentBorder();

    //Log.debug(">" + containerDims + " vs - " + b.getName());
    final ElementLayoutInformation eli =
            createLayoutInformationForPreferredSize(b, containerDims, support);
    final StrictDimension maxSize = eli.getMaximumSize();
    final StrictDimension minSize = eli.getMinimumSize();
    final StrictDimension prefSize = eli.getPreferredSize();
    if (prefSize != null)
    {
      // the user define a prefered size, so no computation is necessary.
      if (isCacheable)
      {
        cache.setPrefSize(b, prefSize);
      }
      return prefSize;
    }

    final StrictDimension base = new StrictDimension
            (maxSize.getWidth(), maxSize.getHeight());

    long height = minSize.getHeight();
    long width = minSize.getWidth();

    // Now adjust the defined sizes by using the elements stored in the band.
    final Element[] elements = b.getElementArray();

    StrictDimension tmpResult = null;
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
        final Point2D absPosElement = (Point2D) e.getStyle().getStyleProperty(ABSOLUTE_POS, DEFAULT_POS);
        final long absPosX = alignDown(correctRelativeValue
                (StrictGeomUtility.toInternalValue(absPosElement.getX()),
                        containerDims.getWidth()), hAlignBorder);
        final long absPosY = alignDown(correctRelativeValue
                (StrictGeomUtility.toInternalValue(absPosElement.getY()),
                        containerDims.getHeight()), vAlignBorder);
        // check whether the element would be visible .. if not visible, then
        // dont do anything ...
        if (absPosX > maxSize.getWidth() || absPosY > maxSize.getHeight())
        {
          // dont display, as this element is larger than the container ...
          continue;
        }
        tmpResult = computePreferredSize(e, base, tmpResult, support,
                staticWidth && staticHeight);

        if (staticWidth)
        {
          width = Math.max(tmpResult.getWidth() + absPosX, width);
        }
        if (staticHeight)
        {
          height = Math.max(tmpResult.getHeight() + absPosY, height);
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

    //Log.debug ("Mid Stream; " + width + ", " + height);
    //Log.debug ("Dimension after static correction [PREF]: " + width + " -> " + height);
    // final StrictDimension base = new StrictDimension(width, height);
    base.setHeight(alignUp(height, vAlignBorder));
    // width remains 100% no matter what happens ..

    StrictDimension absDim = null;
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
        final Point2D absPosFromStyle = (Point2D)
                e.getStyle().getStyleProperty(ABSOLUTE_POS, DEFAULT_POS);

        final long absPosX = alignUp(correctRelativeValue
                (StrictGeomUtility.toInternalValue(absPosFromStyle.getX()),
                        containerDims.getWidth()), hAlignBorder);
        final long absPosY = alignUp(correctRelativeValue
                (StrictGeomUtility.toInternalValue(absPosFromStyle.getY()),
                        containerDims.getHeight()), vAlignBorder);
        // check whether the element would be visible .. if not visible, then
        // dont do anything ...
        if (absPosX > base.getWidth() || absPosY > base.getHeight())
        {
          // dont display, as this element is larger than the container ...
          continue;
        }

        absDim = correctDimension
                (computePreferredSize(e, base, absDim, support, true), base, absDim, support);

        if (staticWidth == false)
        {
          width = Math.max(absDim.getWidth() + absPosX, width);
        }
        if (staticHeight == false)
        {
          height = Math.max(absDim.getHeight() + absPosY, height);
        }
      }
    }

    // now apply the minimum limit defined for that band in case the calculated height
    // is lower than the given minimum height.
    height = Math.max(height, minSize.getHeight());
    width = Math.max(width, minSize.getWidth());

    // now take the maximum limit defined for that band into account for a last time.
    // or specifying -140 would be a nice way to kill the layout ...
    height = Math.min(height, maxSize.getHeight());
    width = Math.min(width, maxSize.getWidth());

    // now align the calculated data ...
    base.setSize(alignUp(width, hAlignBorder),
                 alignUp(height, vAlignBorder));

    // Log.debug("<" + base + " vs - " + b.getName());

    if (isCacheable)
    {
      cache.setPrefSize(b, base);
    }

    return base;
  }

  /**
   * Calculates the minimum layout size for a band. The width for the child elements are
   * not calculated, as we assume that the width's are defined fixed within the parent.
   *
   * @param b               the band.
   * @param containerBounds the bounds of the bands parents.
   * @param support         the layout support used to compute sizes.
   * @return the minimum size.
   */
  public StrictDimension minimumLayoutSize
          (final Band b, final StrictDimension containerBounds,
           final LayoutSupport support)
  {
    if (support == null)
    {
      throw new NullPointerException("LayoutSupport is null.");
    }
    if (b == null)
    {
      throw new NullPointerException("Band is null.");
    }
    if (containerBounds == null)
    {
      throw new NullPointerException("ContainerBounds is null.");
    }
    final LayoutManagerCache cache = support.getCache();

    final boolean isCacheable = cache.isCachable(b);
    if (isCacheable)
    {
      final StrictDimension cretval = cache.getMinSize(b.getObjectID());
      if (cretval != null)
      {
        return cretval;
      }
    }

    final long hAlignBorder = support.getInternalHorizontalAlignmentBorder();
    final long vAlignBorder = support.getInternalVerticalAlignmentBorder();

    final ElementLayoutInformation eli =
            createLayoutInformationForMinimumSize(b, containerBounds, support);
    final StrictDimension maxSize = eli.getMaximumSize();
    final StrictDimension minSize = eli.getMinimumSize();

    // we use the max width, as the width is bound to the outside container,
    // either an other band or the page; the width is required to compute dynamic
    // elements or elements with an 100% width ...
    long height = minSize.getHeight();
    long width = maxSize.getWidth();

    // Check the position of the elements inside and calculate the minimum width
    // needed to display all elements
    final Element[] elements = b.getElementArray();
    final StrictDimension tmpResult = new StrictDimension();

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
        final Point2D absPosFromStyle = (Point2D)
                e.getStyle().getStyleProperty(ABSOLUTE_POS, DEFAULT_POS);

        final long absPosX = alignDown(StrictGeomUtility.toInternalValue(absPosFromStyle.getX()), hAlignBorder);
        final long absPosY = alignDown(StrictGeomUtility.toInternalValue(absPosFromStyle.getY()), vAlignBorder);
        // check whether the element would be visible .. if not visible, then
        // dont do anything ...
        if (absPosX > maxSize.getWidth() || absPosY > maxSize.getHeight())
        {
          // dont display, as this element is larger than the container ...
          continue;
        }
        final StrictDimension size = computeMinimumSize
                (e, maxSize, tmpResult, support, staticHeight && staticWidth);

        if (staticWidth)
        {
          width = Math.max(size.getWidth() + absPosX, width);
        }
        if (staticHeight)
        {
          height = Math.max(size.getHeight() + absPosY, height);
        }
      }
    }

    //Log.debug ("Dimension after static part: " + width + " -> " + height);
    // now apply the minimum limit defined for that band in case the calculated height
    // is lower than the given minimum height.
    height = Math.max(height, minSize.getHeight());
    width = Math.max(width, minSize.getWidth());

    // now apply the maximum limit defined for that band in case the calculated height
    // is higher than the given max height.
    height = Math.min(height, maxSize.getHeight());
    width = Math.min(width, maxSize.getWidth());

    //Log.debug ("Dimension after static correction [MIN]: " + width + " -> " + height);
    final StrictDimension base = new StrictDimension(width, height);
    StrictDimension absDim = null;

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
        final Point2D absPosFromStyle = (Point2D)
                e.getStyle().getStyleProperty(ABSOLUTE_POS, DEFAULT_POS);

        final long absPosX = alignDown(correctRelativeValue
                (StrictGeomUtility.toInternalValue(absPosFromStyle.getX()),
                        containerBounds.getWidth()), hAlignBorder);
        final long absPosY = alignDown(correctRelativeValue
                (StrictGeomUtility.toInternalValue(absPosFromStyle.getY()),
                        containerBounds.getHeight()), vAlignBorder);
        // check whether the element would be visible .. if not visible, then
        // dont do anything ...
        if (absPosX > base.getWidth() || absPosY > base.getHeight())
        {
          // dont display, as this element is larger than the container ...
          continue;
        }
        absDim = correctDimension
                (computeMinimumSize(e, base, absDim, support, true), base, absDim, support);

        if (staticWidth == false)
        {
          width = Math.max(absDim.getWidth() + absPosX, width);
        }
        if (staticHeight == false)
        {
          height = Math.max(absDim.getHeight() + absPosY, height);
        }
        //Log.debug ("Element " + e + " -> " + size);
      }
    }

    //Log.debug ("Dimension after dynamic part: " + width + " -> " + height);
    // now apply the minimum limit defined for that band in case the calculated height
    // is lower than the given minimum height.
    height = Math.max(height, minSize.getHeight());
    width = Math.max(width, minSize.getWidth());

    // now take the maximum limit defined for that band into account for a last time.
    height = Math.min(height, maxSize.getHeight());
    width = Math.min(width, maxSize.getWidth());

    //Log.debug ("Dimension after dynamic correction: " + maxSize);
    //Log.debug ("Dimension after dynamic correction: " + width + " -> " + height);
    // now align the calculated data ...
    final StrictDimension fdim = new StrictDimension
            (alignUp(width, hAlignBorder),
             alignUp(height, vAlignBorder));

    if (isCacheable)
    {
      cache.setMinSize(b, fdim);
    }

    return fdim;
  }

  /**
   * Layout a single band with all elements contained within the band.
   * <p/>
   * The band has its <code>BOUNDS</code> already set and all elements are laid out within
   * these bounds. The band's properties will not be changed during the layouting.
   * <p/>
   * This layout manager requires that all direct child elements have the
   * <code>ABSOLUTE_POS</code> and <code>MINIMUM_SIZE</code> properties set to valid
   * values.
   *
   * @param b       the band to lay out.
   * @param support the layout support used to compute sizes.
   * @throws java.lang.IllegalStateException
   *          if the bands has no bounds set.
   */
  public synchronized void doLayout (final Band b, final LayoutSupport support)
  {
    if (support == null)
    {
      throw new NullPointerException("LayoutSupport is null.");
    }
    if (b == null)
    {
      throw new NullPointerException("Band is null.");
    }

    final Element[] elements = b.getElementArray();
    final StrictBounds parentBounds = BandLayoutManagerUtil.getBounds(b, null);
    if (parentBounds == null)
    {
      throw new IllegalStateException("Need the parent's bound set");
    }

    final StrictDimension parentDim = new StrictDimension
            (parentBounds.getWidth(), parentBounds.getHeight());

    StrictDimension absDim = null;
    for (int i = 0; i < elements.length; i++)
    {
      final Element e = elements[i];

      if (e.isVisible() == false)
      {
        continue;
      }
      final Point2D absPosFromStyle = (Point2D)
              e.getStyle().getStyleProperty(ABSOLUTE_POS, DEFAULT_POS);

      final long absPosX = alignDown(correctRelativeValue
              (StrictGeomUtility.toInternalValue(absPosFromStyle.getX()),
                      parentDim.getWidth()),
              support.getInternalHorizontalAlignmentBorder());
      final long absPosY = alignDown(correctRelativeValue
              (StrictGeomUtility.toInternalValue(absPosFromStyle.getY()),
                      parentDim.getHeight()),
              support.getInternalVerticalAlignmentBorder());
      // check whether the element would be visible .. if not visible, then
      // dont do anything ...
      if (absPosX > parentDim.getWidth() || absPosY > parentDim.getHeight())
      {
        // dont display, as this element is larger than the container ...
        // Log.debug ("Element Out of Bounds: " + e);
        BandLayoutManagerUtil.setBounds(e, new StrictBounds(0, 0, 0, 0));
        continue;
      }

      absDim = computePreferredSize(e, parentDim, absDim, support, true);
      // docmark: Compute preferred size does never return negative values!
      // Log.debug ("UBounds: Element: " + e.getName() + " Bounds: " + absDim);

      // here apply the maximum bounds ...
      final long intHAlign = support.getInternalHorizontalAlignmentBorder();
      final long intVAlign = support.getInternalVerticalAlignmentBorder();
      final StrictBounds bounds = new StrictBounds
              (alignDown(absPosX, intHAlign),
              alignDown(absPosY, intVAlign),
              alignUp(absDim.getWidth(), intHAlign),
              alignUp(absDim.getHeight(), intVAlign));

      BandLayoutManagerUtil.setBounds(e, bounds);
      // Log.debug ("Bounds: Element: " + e.getName() + " Bounds: " + bounds);
      if (e instanceof Band)
      {
        final BandLayoutManager lm =
                BandLayoutManagerUtil.getLayoutManager(e);
        lm.doLayout((Band) e, support);
      }
    }
  }

  /**
   * Returns <code>true</code> if the element has a static width, and <code>false</code>
   * otherwise.
   *
   * @param e the element.
   * @return <code>true</code> or </code>false</code>.
   */
  protected boolean isElementStaticWidth (final Element e)
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
   * @param e the element.
   * @return true or false.
   */
  protected boolean isElementStaticHeight (final Element e)
  {
    final Point2D absPos = (Point2D) e.getStyle().getStyleProperty(ABSOLUTE_POS, DEFAULT_POS);
    if (absPos.getY() < 0)
    {
      return false;
    }
    return super.isElementStaticHeight(e);
  }

  /**
   * Creates a layout information object for a DynamicElement content calculation. The
   * maximum height is only limited by the elements max height, not by the parent.
   *
   * @param e         the element for that the layout should be done.
   * @param parentDim the dimensions for the parent of the element
   * @return the created layout information.
   */
  protected strictfp ElementLayoutInformation
          createLayoutInfoForDynamics (final Element e, 
                                       final StrictDimension parentDim,
                                       final LayoutSupport support)
  {
    final Point2D absPosFromStyle = (Point2D)
            e.getStyle().getStyleProperty(ABSOLUTE_POS, DEFAULT_POS);

    final long hAlignBorder = StrictGeomUtility.toInternalValue
            (support.getHorizontalAlignmentBorder());
    final long vAlignBorder = StrictGeomUtility.toInternalValue
            (support.getVerticalAlignmentBorder());

    final long absPosX = alignDown(correctRelativeValue
            (StrictGeomUtility.toInternalValue(absPosFromStyle.getX()),
                    parentDim.getWidth()), hAlignBorder);
    final long absPosY = alignDown(correctRelativeValue
            (StrictGeomUtility.toInternalValue(absPosFromStyle.getY()),
                    parentDim.getHeight()), vAlignBorder);

    if (parentDim.getWidth() < absPosX)
    {
      // Element is not within the range of the container. Abort, Retry, Ignore?
      throw new IllegalStateException("Width violation!");
    }
    if (parentDim.getHeight() < absPosY)
    {
      // Element is not within the range of the container. Abort, Retry, Ignore?
      throw new IllegalStateException("Height violation!");
    }

    final long maximumWidth = (parentDim.getWidth() - absPosX);

    final Dimension2D eMaxDim = (Dimension2D) e.getStyle().getStyleProperty(ElementStyleSheet.MAXIMUMSIZE);
    final StrictDimension maxSize = correctDimension
            (StrictGeomUtility.createDimension
            (eMaxDim.getWidth(), eMaxDim.getHeight()), parentDim, null, support);

    final Dimension2D eMinDim = (Dimension2D) e.getStyle().getStyleProperty(ElementStyleSheet.MINIMUMSIZE);
    final StrictDimension minSize = correctDimension
            (StrictGeomUtility.createDimension
            (eMinDim.getWidth(), eMinDim.getHeight()), parentDim, null, support);

    maxSize.setSize(Math.min(maximumWidth, maxSize.getWidth()), maxSize.getHeight());
    minSize.setSize(Math.min(maximumWidth, minSize.getWidth()), minSize.getHeight());

    final Dimension2D ePrefDim
            = (Dimension2D) e.getStyle().getStyleProperty(ElementStyleSheet.PREFERREDSIZE);
    final StrictDimension prefSize;
    if (ePrefDim != null)
    {
      prefSize = correctDimension(StrictGeomUtility.createDimension
              (ePrefDim.getWidth(), ePrefDim.getHeight()), parentDim, null, support);
      prefSize.setSize(Math.min(maximumWidth, prefSize.getWidth()), prefSize.getHeight());
    }
    else
    {
      prefSize = null;
    }

    final ElementLayoutInformation eli = new ElementLayoutInformation
            (new StrictPoint(absPosX, absPosY), minSize, maxSize, prefSize);
    return eli;
  }
}
