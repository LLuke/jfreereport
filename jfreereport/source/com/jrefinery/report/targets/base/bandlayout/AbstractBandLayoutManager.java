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
 * ------------------------------
 * AbstractBandLayoutManager.java
 * ------------------------------
 * (C)opyright 2002, 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner (taquera@sherito.org);
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: AbstractBandLayoutManager.java,v 1.7 2003/04/11 17:32:03 taqua Exp $
 *
 * Changes
 * -------
 * 24.03.2003 : Initial version
 */
package com.jrefinery.report.targets.base.bandlayout;

import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import com.jrefinery.report.Band;
import com.jrefinery.report.Element;
import com.jrefinery.report.targets.FloatDimension;
import com.jrefinery.report.targets.base.ElementLayoutInformation;
import com.jrefinery.report.targets.base.content.Content;
import com.jrefinery.report.targets.base.content.ContentFactory;
import com.jrefinery.report.targets.base.layout.LayoutSupport;
import com.jrefinery.report.targets.style.ElementStyleSheet;
import com.jrefinery.report.util.Log;

/**
 * An abstract band layout manager.
 * 
 * @author Thomas Morgner.
 */
public abstract class AbstractBandLayoutManager implements BandLayoutManager
{
  /** Layout support. */
  private LayoutSupport support;

  /**
   * Default constructor.
   */
  public AbstractBandLayoutManager()
  {
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
      retval = correctDimension(dim, containerBounds, null);
    }

    // docmark: minimum size also checks the dynamic height.
    if (e.getStyle().getBooleanStyleProperty(ElementStyleSheet.DYNAMIC_HEIGHT))
    {
      retval = getElementContentBounds(retval, e, containerBounds);
    }

    // now apply the maximum bounds to the retval.
    // the maximum bounds are defined by the element and the elements container.
    Dimension2D maxSize = correctDimension((Dimension2D)
        e.getStyle().getStyleProperty(ElementStyleSheet.MAXIMUMSIZE), containerBounds, null);

    maxSize.setSize(Math.min (containerBounds.getWidth(), maxSize.getWidth()),
                    Math.min (containerBounds.getHeight(), maxSize.getHeight()));

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
    //Log.debug (">> calculate PreferredSize: " + e);
    //Log.debug (">> calculate PreferredSize: " + containerBounds);

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
        retval = correctDimension(d, containerBounds, null);
      }
      else
      {
        // return the absolute dimension as fallback
        retval = correctDimension((Dimension2D)
            e.getStyle().getStyleProperty(ElementStyleSheet.MINIMUMSIZE), containerBounds, null);
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
                                                    containerBounds, null);

    maxSize.setSize(Math.min (containerBounds.getWidth(), maxSize.getWidth()),
                    Math.min (containerBounds.getHeight(), maxSize.getHeight()));

    retval.setSize(Math.min (retval.getWidth(), maxSize.getWidth()),
                   Math.min (retval.getHeight(), maxSize.getHeight()));
    //Log.debug ("-- calculate PreferredSize: " + retval);
    // layouting has failed, if negative values are returned ... !
    if (retval.getWidth() < 0 || retval.getHeight() < 0)
    {
      throw new IllegalStateException(
          "Layouting failed, getPreferredSize returned negative values."
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
   * todo redefine the context creation process, height or width can be dynamic
   *
   * @param bounds  the bounds of the element calculated so far.
   * @param e  the element.
   * @param conBounds  the bounds of the surrounding container.
   *
   * @return the new elements dimension.
   */
  protected Dimension2D getElementContentBounds (Dimension2D bounds, 
                                                 Element e, 
                                                 Dimension2D conBounds)
  {
    // check if we can handle the content before doing anything...
    // ...
    // bounds can be null, if no absolute dim was defined.
    ContentFactory contentFactory = getLayoutSupport().getContentFactory();
    if (contentFactory.canHandleContent(e.getContentType()) == false)
    {
      return bounds;
    }

    ElementLayoutInformation eli = createLayoutInfoForDynamics(e, conBounds);
    Dimension2D minSize = eli.getMinimumSize();
    try
    {

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
      Log.debug ("Exception: ", ex);
      return new FloatDimension((float) Math.max (minSize.getWidth(), bounds.getWidth()),
                                (float) Math.max (minSize.getHeight(), bounds.getHeight()));
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
  protected ElementLayoutInformation createLayoutInfoForDynamics (Element e, Dimension2D parentDim)
  {
    Dimension2D maxSize = correctDimension(
        (Dimension2D) e.getStyle().getStyleProperty(ElementStyleSheet.MAXIMUMSIZE), parentDim, null
    );
    Dimension2D minSize = correctDimension(
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
    maxSize.setSize(Math.min (parentDim.getWidth(), maxSize.getWidth()),
                    maxSize.getHeight());
    ElementLayoutInformation eli
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
  protected ElementLayoutInformation 
      createLayoutInformationForMinimumSize (Element e, Dimension2D containerBounds)
  {
    // the preferred size of an band can be a relative value. Then this value is
    // relative to the container bounds
    Dimension2D minSize  = correctDimension((Dimension2D)
        e.getStyle().getStyleProperty(ElementStyleSheet.MINIMUMSIZE), containerBounds, null);

    // now take the maximum limit defined for that band into account.
    Dimension2D maxSize = correctDimension((Dimension2D)
        e.getStyle().getStyleProperty(ElementStyleSheet.MAXIMUMSIZE), containerBounds, null);
    float maxW = (float) Math.min (maxSize.getWidth(), containerBounds.getWidth());
    float maxH = (float) Math.min (maxSize.getHeight(), containerBounds.getHeight());

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
  protected ElementLayoutInformation 
      createLayoutInformationForPreferredSize (Element e, Dimension2D containerDims)
  {
    float height = 0;
    float width = 0;

    // the preferred size of an band can be a relative value. Then this value is
    // relative to the container bounds
    Dimension2D prefSize
        = (Dimension2D) e.getStyle().getStyleProperty(ElementStyleSheet.PREFERREDSIZE);

    Dimension2D minSize = correctDimension((Dimension2D)
        e.getStyle().getStyleProperty(ElementStyleSheet.MINIMUMSIZE), containerDims, null);

    Dimension2D maxSize = correctDimension((Dimension2D)
        e.getStyle().getStyleProperty(ElementStyleSheet.MAXIMUMSIZE), containerDims, null);

    // there is a preferredSize defined, don't calculate one...
    if (prefSize != null)
    {
      prefSize = correctDimension(prefSize, containerDims, null);
      height = (float) Math.max(height, prefSize.getHeight());
      width = (float) Math.max(width, prefSize.getWidth());
    }

    // check for an minimum size and take that into account
    height = (float) Math.max(height, minSize.getHeight());
    width = (float) Math.max(width, minSize.getWidth());

    // now take the maximum limit defined for that band into account.
    float maxW = (float) Math.min (maxSize.getWidth(), containerDims.getWidth());
    float maxH = (float) Math.min (maxSize.getHeight(), containerDims.getHeight());

    // the bounds inherited from the parent, cut down to the maximum size defined
    // for this elements.

    // maxSize defines the bounds of the parent and the maximum size we defined.
    maxSize.setSize(maxW, maxH);
    // minSize is our defined Size (we use at least regardless whether there is
    // some more content ...
    minSize.setSize(width, height);
    return new ElementLayoutInformation(new Point2D.Float(), minSize, maxSize, prefSize);
  }

  /**
   * Sets the output target for the layout manager. The LayoutManager support must
   * be cleared (set to null) after the layouting is complete.
   *
   * @param target  the target.
   */
  public void setLayoutSupport(LayoutSupport target)
  {
    support = target;
  }

  /**
   * Returns the output target for the layout manager.
   *
   * @return the target.
   */
  public LayoutSupport getLayoutSupport()
  {
    return support;
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
  protected boolean isElementStaticHeight (Element e)
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
  protected  Dimension2D correctDimension (Dimension2D dim, Dimension2D base, Dimension2D retval)
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
  protected Point2D correctPoint (Point2D dim, Dimension2D base, Point2D retval)
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
   * Aligns the given value to the boundary.
   *
   * @param value  the value.
   * @param boundary  the boundary.
   *
   * @return The aligned value.
   */
  protected float align (float value, float boundary)
  {
    if (boundary == 0)
    {
      return value;
    }

    return (float) Math.floor(value / boundary) * boundary;
  }

}
