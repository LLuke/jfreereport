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
 * ------------------------------
 * AbstractBandLayoutManager.java
 * ------------------------------
 * (C)opyright 2002, 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: AbstractBandLayoutManager.java,v 1.12 2003/11/07 18:33:49 taqua Exp $
 *
 * Changes
 * -------
 * 24.03.2003 : Initial version
 */
package org.jfree.report.layout;

import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import org.jfree.report.Band;
import org.jfree.report.Element;
import org.jfree.report.content.Content;
import org.jfree.report.content.ContentFactory;
import org.jfree.report.style.ElementStyleSheet;
import org.jfree.report.util.ElementLayoutInformation;
import org.jfree.report.util.Log;
import org.jfree.ui.FloatDimension;

/**
 * An abstract band layout manager.
 *
 * @author Thomas Morgner.
 */
public abstract class AbstractBandLayoutManager implements BandLayoutManager
{
  /**
   * Default constructor.
   */
  protected AbstractBandLayoutManager()
  {
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
  protected strictfp Dimension2D computeMinimumSize
    (final Element e, final Dimension2D containerBounds, Dimension2D retval, final LayoutSupport support)
  {
    if (containerBounds.getWidth() < 0 || containerBounds.getHeight() < 0)
    {
      throw new IllegalArgumentException("Container bounds must be positive");
    }

    // if this is a band, then try to calculate the min-size
    if (e instanceof Band)
    {
      final BandLayoutManager lm = BandLayoutManagerUtil.getLayoutManager(e);
      retval = lm.minimumLayoutSize((Band) e, containerBounds, support);
    }
    else
    {
      // return the minimum size. The minimum size is always defined.
      final Dimension2D dim = (Dimension2D)
          e.getStyle().getStyleProperty(ElementStyleSheet.MINIMUMSIZE);
      retval = correctDimension(dim, containerBounds, retval);
    }

    final Dimension2D maxSize = correctDimension((Dimension2D)
        e.getStyle().getStyleProperty(ElementStyleSheet.MAXIMUMSIZE), containerBounds, null);

    maxSize.setSize(Math.min(containerBounds.getWidth(), maxSize.getWidth()),
        Math.min(containerBounds.getHeight(), maxSize.getHeight()));

    // docmark: minimum size also checks the dynamic height.
    if (e.getStyle().getBooleanStyleProperty(ElementStyleSheet.DYNAMIC_HEIGHT))
    {
      retval = getElementContentBounds(retval, e, maxSize, support);
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
  protected strictfp Dimension2D computePreferredSize
      (final Element e, final Dimension2D containerBounds,
       Dimension2D retval, final LayoutSupport support)
  {
    if (containerBounds.getWidth() < 0 || containerBounds.getHeight() < 0)
    {
      throw new IllegalArgumentException("Container bounds must be positive");
    }
    //Log.debug (">> calculate PreferredSize: " + e);
    //Log.debug (">> calculate PreferredSize: " + containerBounds);

    // if e is a band, then try to calculate the preferred size
    if (e instanceof Band)
    {
      final BandLayoutManager lm = BandLayoutManagerUtil.getLayoutManager(e);
      retval = lm.preferredLayoutSize((Band) e, containerBounds, support);
    }
    else
    {
      // if prefsize is defined, return it. The preferred size is optional,
      // so it may be required to also query the minimum size.
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
    final Dimension2D maxSize = correctDimension((Dimension2D)
        e.getStyle().getStyleProperty(ElementStyleSheet.MAXIMUMSIZE), containerBounds, null);

    maxSize.setSize(Math.min(containerBounds.getWidth(), maxSize.getWidth()),
        Math.min(containerBounds.getHeight(), maxSize.getHeight()));

    if (e.getStyle().getBooleanStyleProperty(ElementStyleSheet.DYNAMIC_HEIGHT))
    {
      retval = getElementContentBounds(retval, e, maxSize, support);
    }

    retval.setSize(Math.min(retval.getWidth(), maxSize.getWidth()),
        Math.min(retval.getHeight(), maxSize.getHeight()));

    //Log.debug ("-- calculate PreferredSize: " + retval);
    // layouting has failed, if negative values are returned ... !
    if (retval.getWidth() < 0 || retval.getHeight() < 0)
    {
      throw new IllegalStateException(
          "Layouting failed, computePreferredSize returns negative values."
      );
    }

    return retval;
  }

  /**
   * Calculates the size of an element by creating the content for this element and
   * then trying to layout that content. This operation is performed for all
   * "dynamic" elements.
   * <p>
   * Calculation rules: Take the width of given bounds to calculate a height based
   * on the content. Then cut the content to a maybe defined max-value.
   *
   * @param bounds  the bounds of the element calculated so far. These bounds will be modified
   * and returned.
   * @param e  the element.
   * @param conBounds  the bounds of the surrounding container.
   *
   * @return the new elements dimension.
   */
  protected strictfp Dimension2D getElementContentBounds
      (final Dimension2D bounds, final Element e,
       final Dimension2D conBounds, final LayoutSupport support)
  {
    // check if we can handle the content before doing anything...
    // ...
    final ContentFactory contentFactory = support.getContentFactory();
    if (contentFactory.canHandleContent(e.getContentType()) == false)
    {
      return bounds;
    }

    final ElementLayoutInformation eli = createLayoutInfoForDynamics(e, conBounds);
    final Dimension2D minSize = eli.getMinimumSize();
    try
    {

      final Content content = contentFactory.createContentForElement(e, eli, support);
      if (content == null)
      {
        bounds.setSize(0,0);
        return bounds;
      }
      final Rectangle2D contentBounds = content.getMinimumContentSize();
      if (contentBounds == null)
      {
        bounds.setSize(0,0);
        return bounds;
      }
      bounds.setSize(Math.max(minSize.getWidth(), contentBounds.getWidth()),
                     Math.max(minSize.getHeight(), contentBounds.getHeight()));
      return bounds;
    }
    catch (Exception ex)
    {
      Log.info("Exception while layouting dynamic content: ", ex);
      bounds.setSize(Math.max(minSize.getWidth(), bounds.getWidth()),
                     Math.max(minSize.getHeight(), bounds.getHeight()));
      return bounds;
    }
  }

  /**
   * Creates a layout information object for a DynamicElement content calculation.
   * The maximum height is only limited by the elements max height, not by the parent.
   *
   * @param e the element for that the layout should be done.
   * @param parentDim the dimensions for the parent of the element
   * @return the created layout information.
   */
  protected strictfp ElementLayoutInformation createLayoutInfoForDynamics
      (final Element e, final Dimension2D parentDim)
  {
    final Dimension2D maxSize = correctDimension(
        (Dimension2D) e.getStyle().getStyleProperty(ElementStyleSheet.MAXIMUMSIZE), parentDim, null
    );
    final Dimension2D minSize = correctDimension(
        (Dimension2D) e.getStyle().getStyleProperty(ElementStyleSheet.MINIMUMSIZE), parentDim, null
    );

    Dimension2D prefSize
        = (Dimension2D) e.getStyle().getStyleProperty(ElementStyleSheet.PREFERREDSIZE);
    if (prefSize != null)
    {
      prefSize = correctDimension(prefSize, parentDim, null);
    }

    // the width is the limiting element in the calculation, height is considered
    // infinitive ...
    maxSize.setSize(Math.min(parentDim.getWidth(), maxSize.getWidth()),
        maxSize.getHeight());
    final ElementLayoutInformation eli
        = new ElementLayoutInformation(new Point2D.Float(), minSize, maxSize, prefSize);
    return eli;
  }

  /**
   * Creates layout information for the minimum size.
   *
   * @param e  the element.
   * @param containerBounds  the bounds of the container.
   *
   * @return layout information.
   */
  protected strictfp ElementLayoutInformation
      createLayoutInformationForMinimumSize(final Element e, final Dimension2D containerBounds)
  {
    // the preferred size of an band can be a relative value. Then this value is
    // relative to the container bounds
    final Dimension2D minSize = correctDimension((Dimension2D)
        e.getStyle().getStyleProperty(ElementStyleSheet.MINIMUMSIZE), containerBounds, null);

    // now take the maximum limit defined for that band into account.
    final Dimension2D maxSize = correctDimension((Dimension2D)
        e.getStyle().getStyleProperty(ElementStyleSheet.MAXIMUMSIZE), containerBounds, null);
    final float maxW = (float) Math.min(maxSize.getWidth(), containerBounds.getWidth());
    final float maxH = (float) Math.min(maxSize.getHeight(), containerBounds.getHeight());

    // the bounds inherited from the parent, cut down to the maximum size defined
    // for this elements.
    maxSize.setSize(maxW, maxH);
    return new ElementLayoutInformation(new Point2D.Float(), minSize, maxSize);
  }

  /**
   * Creates layout information for the preferred size.
   *
   * @param e  the element.
   * @param containerDims  the dimensions of the container.
   *
   * @return layout information.
   */
  protected strictfp ElementLayoutInformation
      createLayoutInformationForPreferredSize(final Element e, final Dimension2D containerDims)
  {
    float height = 0;
    float width = 0;

    // the preferred size of an band can be a relative value. Then this value is
    // relative to the container bounds
    final Dimension2D prefSize
        = (Dimension2D) e.getStyle().getStyleProperty(ElementStyleSheet.PREFERREDSIZE);

    final Dimension2D minSize = correctDimension((Dimension2D)
        e.getStyle().getStyleProperty(ElementStyleSheet.MINIMUMSIZE), containerDims, null);

    final Dimension2D maxSize = correctDimension((Dimension2D)
        e.getStyle().getStyleProperty(ElementStyleSheet.MAXIMUMSIZE), containerDims, null);

    // there is a preferredSize defined, don't calculate one...
    if (prefSize != null)
    {
      height = Math.max(height,
          correctRelativeValue((float) prefSize.getHeight(), (float) containerDims.getHeight()));
      width = Math.max(width,
          correctRelativeValue((float) prefSize.getWidth(), (float) containerDims.getWidth()));
    }

    // check for an minimum size and take that into account
    height = (float) Math.max(height, minSize.getHeight());
    width = (float) Math.max(width, minSize.getWidth());

    // now take the maximum limit defined for that band into account.
    final float maxW = (float) Math.min(maxSize.getWidth(), containerDims.getWidth());
    final float maxH = (float) Math.min(maxSize.getHeight(), containerDims.getHeight());

    // the bounds inherited from the parent, cut down to the maximum size defined
    // for this elements.

    // maxSize defines the bounds of the parent and the maximum size we defined.
    // whatever is smaller gets used ...
    maxSize.setSize(maxW, maxH);
    // minSize is our defined Size (we use at least regardless whether there is
    // some more content ...
    minSize.setSize(width, height);
    return new ElementLayoutInformation(new Point2D.Float(), minSize, maxSize, prefSize);
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
    Dimension2D size
        = (Dimension2D) e.getStyle().getStyleProperty(ElementStyleSheet.MINIMUMSIZE);
    if (size.getWidth() < 0)
    {
      return false;
    }
    size = (Dimension2D) e.getStyle().getStyleProperty(ElementStyleSheet.MAXIMUMSIZE);
    if (size.getWidth() < 0)
    {
      return false;
    }
    size = (Dimension2D) e.getStyle().getStyleProperty(ElementStyleSheet.PREFERREDSIZE);
    if (size != null)
    {
      if (size.getWidth() < 0)
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
  protected boolean isElementStaticHeight(final Element e)
  {
    Dimension2D size
        = (Dimension2D) e.getStyle().getStyleProperty(ElementStyleSheet.MINIMUMSIZE);
    if (size.getHeight() < 0)
    {
      return false;
    }
    size = (Dimension2D) e.getStyle().getStyleProperty(ElementStyleSheet.MAXIMUMSIZE);
    if (size.getWidth() < 0)
    {
      return false;
    }
    size = (Dimension2D) e.getStyle().getStyleProperty(ElementStyleSheet.PREFERREDSIZE);
    if (size != null)
    {
      if (size.getHeight() < 0)
      {
        return false;
      }
    }
    return true;
  }

  /**
   * Corrects the relative (proportional) values. The values are given
   * in the range from -100 (= 100%) to 0 (0%) and are resolved to the
   * base values.
   *
   * @param dim  the dimension that should be corrected.
   * @param base  the base to define the 100% limit.
   * @param retval  the return value.
   *
   * @return the corrected dimension.
   */
  protected static strictfp Dimension2D correctDimension
      (final Dimension2D dim, final Dimension2D base, final Dimension2D retval)
  {
    float newWidth = (float) dim.getWidth();
    if (dim.getWidth() < 0)
    {
      newWidth = (float) (dim.getWidth() * base.getWidth() / -100);
    }
    float newHeight = (float) dim.getHeight();
    if (dim.getHeight() < 0)
    {
      newHeight = (float) (dim.getHeight() * base.getHeight() / -100);
    }
    if (retval == null)
    {
      return new FloatDimension(newWidth, newHeight);
    }
    else
    {
      retval.setSize(newWidth, newHeight);
      return retval;
    }
  }

  /**
   * Corrects a single value.
   * @param dim the dimensions value
   * @param base the base value (the containers value)
   * @return the corrected value if necessary or the dim value unchanged.
   */
  protected static strictfp float correctRelativeValue (final float dim, final float base)
  {
    if (dim < 0)
    {
      return (dim * base / -100f);
    }
    return dim;
  }

  /**
   * Corrects the relative (proportional) values. The values are given
   * in the range from -100 (= 100%) to 0 (0%) and are resolved to the
   * base values.
   *
   * @param dim  the point that should be corrected.
   * @param base  the base to define the 100% limit.
   * @param retval  the return value.
   *
   * @return the corrected point.
   */
  protected static strictfp Point2D correctPoint
    (final Point2D dim, final Dimension2D base, final Point2D retval)
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
    if (retval == null)
    {
      return new Point2D.Float((float) x, (float) y);
    }
    else
    {
      retval.setLocation(x, y);
      return retval;
    }
  }

  /**
   * Aligns the given value to the boundary. This is used to align
   * the content to an grid, in case that the output target needs
   * all coordinates aligned.
   *
   * @param value  the value.
   * @param boundary  the boundary.
   *
   * @return The aligned value.
   */
  protected static strictfp float align(final float value, final float boundary)
  {
    if (boundary == 0)
    {
      return value;
    }

    return (float) Math.floor(value / boundary) * boundary;
  }

}
