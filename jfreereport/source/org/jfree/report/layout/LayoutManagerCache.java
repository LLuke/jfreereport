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
 * -----------------------
 * LayoutManagerCache.java
 * -----------------------
 * (C)opyright 2002, 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: LayoutManagerCache.java,v 1.7 2003/09/08 18:11:48 taqua Exp $
 *
 * Changes
 * -------
 * 03-Dec-2002 : Added header and Javadoc comments (DG);
 *
 */
package org.jfree.report.layout;

import java.awt.geom.Dimension2D;
import java.util.WeakHashMap;

import org.jfree.report.Band;
import org.jfree.report.Element;
import org.jfree.report.style.ElementStyleSheet;
import org.jfree.report.util.Log;

/**
 * A cache for a band layout manager. This caches element bounds, not
 * elements.
 * todo: check how to improve performance or how to reuse the cached objects ...
 *
 * @author Thomas Morgner
 */
public class LayoutManagerCache
{
  /**
   * Caches info for a single element.
   */
  private static class ElementCacheCarrier
  {
    /** The minimum size. */
    private Dimension2D minSize;

    /** The preferred size. */
    private Dimension2D prefSize;

    /**
     * Default Constructor.
     */
    public ElementCacheCarrier()
    {
    }

    /**
     * Returns the minimum size stored in this carrier.
     *  
     * @return the minimum size stored in that carrier.
     */
    public Dimension2D getMinSize()
    {
      return minSize;
    }

    /**
     * Returns the preferred size stored in this carrier.
     *  
     * @return the preferred size stored in that carrier.
     */
    public Dimension2D getPrefSize()
    {
      return prefSize;
    }

    /**
     * Defines the minimum size stored in this carrier.
     *  
     * @param dimension2D the minimum size to be stored in that carrier.
     */
    public void setMinSize(final Dimension2D dimension2D)
    {
      minSize = dimension2D;
    }

    /**
     * Defines the minimum size stored in this carrier.
     *  
     * @param dimension2D the minimum size stored in that carrier.
     */
    public void setPrefSize(final Dimension2D dimension2D)
    {
      prefSize = dimension2D;
    }

  }

  /** The put count. */
  private static int putCount;

  /** The get count. */
  private static int getCount;

  /** The element cache. */
  private final WeakHashMap elementCache;

  /**
   * Default constructor.
   */
  public LayoutManagerCache()
  {
    elementCache = new WeakHashMap();
  }

  /**
   * Returns the minimum size of ???.
   *
   * @param e  the layout cache key.
   *
   * @return The minimum size.
   */
  public Dimension2D getMinSize(final LayoutCacheKey e)
  {
    final ElementCacheCarrier ec = (ElementCacheCarrier) elementCache.get(e);
    if (ec == null)
    {
      return null;
    }
    final Dimension2D dim = ec.getMinSize();
    if (dim != null)
    {
      getCount++;
    }
    return dim;
  }

  /**
   * Returns the preferred size of ???.
   *
   * @param e  the layout cache key.
   *
   * @return The preferred size.
   */
  public Dimension2D getPrefSize(final LayoutCacheKey e)
  {
    final ElementCacheCarrier ec = (ElementCacheCarrier) elementCache.get(e);
    if (ec == null)
    {
      return null;
    }
    final Dimension2D dim = ec.getPrefSize();
    if (dim != null)
    {
      getCount++;
    }
    return dim;
  }

  /**
   * Sets the minimum size of ???.
   *
   * @param key  the key.
   * @param element  the element.
   * @param d  the minimum size.
   */
  public void setMinSize(final LayoutCacheKey key, final Element element, final Dimension2D d)
  {
    if (element == null)
    {
      throw new NullPointerException("Element is null");
    }

    if (isCachable(element) == false)
    {
      return;
    }
    putCount++;

    ElementCacheCarrier ec = (ElementCacheCarrier) elementCache.get(key);
    if (ec == null)
    {
      ec = new ElementCacheCarrier();
      ec.setMinSize(d);
      if (key.isSearchKey())
      {
        elementCache.put(new LayoutCacheKey(element, key.getParentDim()), ec);
      }
      else
      {
        elementCache.put(key, ec);
      }
    }
    else
    {
      if (isCachable(element) == true)
      {
        ec.setMinSize(d);
      }
    }
  }

  /**
   * Sets the preferred size of ???.
   *
   * @param key  the key.
   * @param element  the element.
   * @param d  the minimum size.
   */
  public void setPrefSize(final LayoutCacheKey key, final Element element, final Dimension2D d)
  {
    if (element == null)
    {
      throw new IllegalArgumentException("LayoutCacheKey: Element is null");
    }

    if (isCachable(element) == false)
    {
      return;
    }
    putCount++;

    ElementCacheCarrier ec = (ElementCacheCarrier) elementCache.get(key);
    if (ec == null)
    {
      ec = new ElementCacheCarrier();
      ec.setPrefSize(d);
      if (key.isSearchKey())
      {
        elementCache.put(new LayoutCacheKey(element, key.getParentDim()), ec);
      }
      else
      {
        elementCache.put(key, ec);
      }
    }
    else
    {
      ec.setPrefSize(d);
    }
  }

  /**
   * Returns true if the specified element is cachable, and false otherwise.
   *
   * @param e  the element.
   *
   * @return A boolean.
   */
  public boolean isCachable(final Element e)
  {
    if (e.getStyle().getBooleanStyleProperty(ElementStyleSheet.ELEMENT_LAYOUT_CACHEABLE) == false)
    {
      return false;
    }

    // if the element is dynamic, then it is not cachable ...
    if (e.getStyle().getBooleanStyleProperty(ElementStyleSheet.DYNAMIC_HEIGHT))
    {
      e.getStyle().setBooleanStyleProperty
          (ElementStyleSheet.ELEMENT_LAYOUT_CACHEABLE, false);
      return false;
    }

    if (e instanceof Band)
    {
      // search for dynamic elements within the element's children ...
      // if there is no dynamic element, then the element is cachable ...
      final Element[] elements = ((Band) e).getElementArray();
      for (int i = 0; i < elements.length; i++)
      {
        if (isCachable(elements[i]) == false)
        {
          e.getStyle().setBooleanStyleProperty
              (ElementStyleSheet.ELEMENT_LAYOUT_CACHEABLE, false);
          return false;
        }
      }
    }
    return true;
  }

  /**
   * Flushes the cache.
   */
  public void flush()
  {
    elementCache.clear();
  }

  /**
   * Prints debugging information.
   */
  public static void printResults()
  {
    Log.debug("CacheResults: " + 
        getCount + ":" + putCount + " Ratio: " + 
        (getCount / (double) putCount));
  }
}
