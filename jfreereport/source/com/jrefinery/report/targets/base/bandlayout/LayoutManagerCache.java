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
 * -----------------------
 * LayoutManagerCache.java
 * -----------------------
 * (C)opyright 2002, 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: LayoutManagerCache.java,v 1.3 2003/04/09 15:49:55 mungady Exp $
 *
 * Changes
 * -------
 * 03-Dec-2002 : Added header and Javadoc comments (DG);
 *
 */
package com.jrefinery.report.targets.base.bandlayout;

import java.awt.geom.Dimension2D;
import java.util.WeakHashMap;

import com.jrefinery.report.Band;
import com.jrefinery.report.Element;
import com.jrefinery.report.targets.style.ElementStyleSheet;
import com.jrefinery.report.util.Log;

/**
 * A cache for a band layout manager. Not very usefull yet, maybe later.
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
  }

  /** The put count. */
  private static int putCount;
  
  /** The get count. */
  private static int getCount;

  /** The element cache. */
  private WeakHashMap elementCache;

  /**
   * Default constructor.
   */
  public LayoutManagerCache ()
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
  public Dimension2D getMinSize(LayoutCacheKey e)
  {
    ElementCacheCarrier ec = (ElementCacheCarrier) elementCache.get(e);
    if (ec == null)
    {
      return null;
    }
    if (ec.minSize != null) 
    {
      getCount++;
    }
    return ec.minSize;
  }

  /**
   * Returns the preferred size of ???.
   * 
   * @param e  the layout cache key.
   * 
   * @return The preferred size.
   */
  public Dimension2D getPrefSize(LayoutCacheKey e)
  {
    ElementCacheCarrier ec = (ElementCacheCarrier) elementCache.get(e);
    if (ec == null)
    {
      return null;
    }
    if (ec.prefSize != null)
    {
      getCount++;
    }
    return ec.prefSize;
  }

  /**
   * Sets the minimum size of ???.
   * 
   * @param key  the key.
   * @param element  the element.
   * @param d  the minimum size.
   */
  public void setMinSize(LayoutCacheKey key, Element element, Dimension2D d)
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
      ec.minSize = d;
      if (key.isSearchKey())
      {
        elementCache.put (new LayoutCacheKey(element, key.getParentDim()), ec);
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
        ec.minSize = d;
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
  public void setPrefSize(LayoutCacheKey key, Element element, Dimension2D d)
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
      ec.prefSize = d;
      if (key.isSearchKey())
      {
        elementCache.put (new LayoutCacheKey(element, key.getParentDim()), ec);
      }
      else
      {
        elementCache.put(key, ec);
      }
    }
    else
    {
      ec.prefSize = d;
    }
  }

  /**
   * Returns true if the specified element is cachable, and false otherwise.
   * 
   * @param e  the element.
   * 
   * @return A boolean.
   */
  public boolean isCachable(Element e)
  {
    // if the element is dynamic, then it is not cachable ...
    if (e.getStyle().getBooleanStyleProperty(ElementStyleSheet.DYNAMIC_HEIGHT))
    {
      return false;
    }
    if (e instanceof Band)
    {
      // search for dynamic elements within the element's children ...
      // if there is no dynamic element, then the element is cachable ...
      Element[] elements = ((Band) e).getElementArray();
      for (int i = 0; i < elements.length; i++)
      {
        if (isCachable(elements[i]) == false)
        {
          return false;
        }
      }
      return true;
    }
    else
    {
      return true;
    }
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
    Log.debug ("CacheResults: " + getCount + ":" + putCount);
  }
}
