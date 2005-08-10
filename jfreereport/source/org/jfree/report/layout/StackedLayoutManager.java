/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2005, by Object Refinery Limited and Contributors.
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
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * ------------
 * StackedLayoutManager.java
 * ------------
 * (C) Copyright 2002-2005, by Object Refinery Limited.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: JCommon.java,v 1.1 2004/07/15 14:49:46 mungady Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.report.layout;

import java.awt.geom.Dimension2D;

import org.jfree.report.Band;
import org.jfree.report.Element;
import org.jfree.report.style.ElementStyleSheet;
import org.jfree.report.util.ElementLayoutInformation;
import org.jfree.report.util.geom.StrictBounds;
import org.jfree.report.util.geom.StrictDimension;
import org.jfree.report.util.geom.StrictGeomUtility;

public class StackedLayoutManager extends AbstractBandLayoutManager
{
  public StackedLayoutManager ()
  {
  }

  /**
   * Performs the layout of a band.
   *
   * @param b       the band.
   * @param support the layout support used to compute sizes.
   */
  public void doLayout (final Band b,
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

    final Element[] elements = b.getElementArray();
    final StrictBounds parentBounds = BandLayoutManagerUtil.getBounds(b, null);
    if (parentBounds == null)
    {
      throw new IllegalStateException("Need the parent's bound set");
    }

    final long intHAlign = support.getInternalHorizontalAlignmentBorder();
    final long intVAlign = support.getInternalVerticalAlignmentBorder();
    final StrictDimension parentDim = new StrictDimension
            (parentBounds.getWidth(), parentBounds.getHeight());
    final long parentWidth = alignUp(parentBounds.getWidth(), intHAlign);
    long yPosition = 0;
    StrictDimension absDim = null;
    for (int i = 0; i < elements.length; i++)
    {
      final Element e = elements[i];

      if (e.isVisible() == false)
      {
        continue;
      }

      absDim = computePreferredSize(e, parentDim, absDim, support, true);
      // docmark: Compute preferred size does never return negative values!
      // Log.debug ("UBounds: Element: " + e.getName() + " Bounds: " + absDim);

      // here apply the maximum bounds ...
      final long height = alignUp(absDim.getHeight(), intVAlign);
      final StrictBounds bounds = new StrictBounds
              (alignDown(0, intHAlign),
              alignDown(yPosition, intVAlign),
              parentWidth, height);

      yPosition += height;
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

    maxSize.setSize
            (Math.min(containerBounds.getWidth(), maxSize.getWidth()),
            Math.min(containerBounds.getHeight(), maxSize.getHeight()));

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
        retval = correctDimension(sDim, containerBounds, retval, support);
      }
      else
      {
        // return the absolute dimension as fallback
        final Dimension2D minDim = (Dimension2D)
                e.getStyle().getStyleProperty(ElementStyleSheet.MINIMUMSIZE);
        final StrictDimension sDim =
                StrictGeomUtility.createDimension(minDim.getWidth(), minDim.getHeight());
        retval = correctDimension(sDim, containerBounds, retval, support);
      }
    }

    // now apply the maximum bounds to the retval.
    // the maximum bounds are defined by the element and the elements container.
    final Dimension2D maxDim = (Dimension2D)
            e.getStyle().getStyleProperty(ElementStyleSheet.MAXIMUMSIZE);
    final StrictDimension sMaxDim =
            StrictGeomUtility.createDimension(maxDim.getWidth(), maxDim.getHeight());
    final StrictDimension maxSize = correctDimension(sMaxDim, containerBounds, null, support);

    maxSize.setSize(Math.min(containerBounds.getWidth(), maxSize.getWidth()),
            Math.min(containerBounds.getHeight(), maxSize.getHeight()));

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
   * Calculates the minimum layout size for a band.
   *
   * @param b             the band.
   * @param containerDims the bounds of the surrounding container.
   * @param support       the layout support used to compute sizes.
   * @return the minimum size.
   */
  public StrictDimension minimumLayoutSize (final Band b,
                                            final StrictDimension containerDims,
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
            createLayoutInformationForMinimumSize(b, containerDims, support);
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
        final StrictDimension size = computeMinimumSize
                (e, maxSize, tmpResult, support, staticHeight && staticWidth);

        if (staticWidth)
        {
          width = Math.max(size.getWidth(), width);
        }
        if (staticHeight)
        {
          height += size.getHeight();
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
        absDim = correctDimension
                (computeMinimumSize(e, base, absDim, support, true), base, absDim, support);

        if (staticWidth == false)
        {
          width = Math.max(absDim.getWidth(), width);
        }
        if (staticHeight == false)
        {
          height += absDim.getHeight();
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
   * Calculates the preferred layout size for a band.
   *
   * @param b             the band.
   * @param containerDims the bounds of the surrounding container.
   * @param support       the layout support used to compute sizes.
   * @return the preferred size.
   */
  public StrictDimension preferredLayoutSize (final Band b,
                                              final StrictDimension containerDims,
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
        final StrictDimension size = computePreferredSize
                (e, maxSize, tmpResult, support, staticHeight && staticWidth);

        if (staticWidth)
        {
          width = Math.max(size.getWidth(), width);
        }
        if (staticHeight)
        {
          height += size.getHeight();
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
        absDim = correctDimension
                (computePreferredSize(e, base, absDim, support, true), base, absDim, support);

        if (staticWidth == false)
        {
          width = Math.max(absDim.getWidth(), width);
        }
        if (staticHeight == false)
        {
          height += absDim.getHeight();
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
}
