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
 * -----------------------
 * LayoutManagerCache.java
 * -----------------------
 * (C)opyright 2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: LayoutManagerCache.java,v 1.1 2003/01/29 03:13:01 taqua Exp $
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
    public Dimension2D minSize;
    
    /** The preferred size. */
    public Dimension2D prefSize;
  }

  private static int putCount;
  private static int getCount;

  /** The element cache. */
  private WeakHashMap elementCache;

  public LayoutManagerCache ()
  {
    elementCache = new WeakHashMap();
  }

  public Dimension2D getMinSize(LayoutCacheKey e)
  {
    ElementCacheCarrier ec = (ElementCacheCarrier) elementCache.get(e);
    if (ec == null)
    {
      return null;
    }
    if (ec.minSize != null) getCount++;
    return ec.minSize;
  }

  public Dimension2D getPrefSize(LayoutCacheKey e)
  {
    ElementCacheCarrier ec = (ElementCacheCarrier) elementCache.get(e);
    if (ec == null)
    {
      return null;
    }
    if (ec.prefSize != null) getCount++;
    return ec.prefSize;
  }

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

  public void flush()
  {
    elementCache.clear();
  }

  public static void printResults()
  {
    Log.debug ("CacheResults: " + getCount + ":" + putCount);
  }
}
