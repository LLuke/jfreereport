/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2003, by Object Refinery Limited and Contributors.
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
 * Contributor(s):   David Gilbert (for Object Refinery Limited);
 *
 * $Id: LayoutManagerCache.java,v 1.16 2005/09/19 13:09:09 taqua Exp $
 *
 * Changes
 * -------
 * 03-Dec-2002 : Added header and Javadoc comments (DG);
 *
 */
package org.jfree.report.layout;

import java.util.HashMap;

import org.jfree.report.Band;
import org.jfree.report.Element;
import org.jfree.report.style.ElementStyleSheet;
import org.jfree.report.util.geom.StrictDimension;
import org.jfree.util.Log;

/**
 * A cache for a band layout manager. This caches element bounds, not elements.
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
    private boolean dynamic;
    /**
     * The minimum size.
     */
    private StrictDimension minSize;

    /**
     * The preferred size.
     */
    private StrictDimension prefSize;

    /**
     * Default Constructor.
     */
    public ElementCacheCarrier ()
    {
    }

    public boolean isDynamic()
    {
      return dynamic;
    }

    public void setDynamic(final boolean dynamic)
    {
      this.dynamic = dynamic;
    }

    /**
     * Returns the minimum size stored in this carrier.
     *
     * @return the minimum size stored in that carrier.
     */
    public StrictDimension getMinSize ()
    {
      return minSize;
    }

    /**
     * Returns the preferred size stored in this carrier.
     *
     * @return the preferred size stored in that carrier.
     */
    public StrictDimension getPrefSize ()
    {
      return prefSize;
    }

    /**
     * Defines the minimum size stored in this carrier.
     *
     * @param dimension2D the minimum size to be stored in that carrier.
     */
    public void setMinSize (final StrictDimension dimension2D)
    {
      minSize = dimension2D;
    }

    /**
     * Defines the minimum size stored in this carrier.
     *
     * @param dimension2D the minimum size stored in that carrier.
     */
    public void setPrefSize (final StrictDimension dimension2D)
    {
      prefSize = dimension2D;
    }

  }

  /**
   * The put count.
   */
  private static int putCount;

  /**
   * The get count.
   */
  private static int getCount;

  /**
   * The element cache.
   */
  private final HashMap elementCache;
  private final HashMap dynamicCache;

  /**
   * Default constructor.
   */
  public LayoutManagerCache ()
  {
    elementCache = new HashMap();
    dynamicCache = new HashMap();
  }

  /**
   * Returns the minimum size of ???.
   *
   * @param e the layout cache key.
   * @return The minimum size.
   */
  public StrictDimension getMinSize (final Object e)
  {
    final ElementCacheCarrier ec = lookupCacheEntry(e);
    if (ec == null)
    {
      return null;
    }
    final StrictDimension dim = ec.getMinSize();
    if (dim != null)
    {
      getCount++;
      return dim.getUnlockedInstance();
    }
    return null;
  }

  private ElementCacheCarrier lookupCacheEntry(final Object e)
  {
    final ElementCacheCarrier staticCacheEntry =
            (ElementCacheCarrier) elementCache.get(e);
    if (staticCacheEntry != null)
    {
      return staticCacheEntry;
    }
    return (ElementCacheCarrier) dynamicCache.get(e);
  }

  /**
   * Returns the preferred size of ???.
   *
   * @param e the layout cache key.
   * @return The preferred size.
   */
  public StrictDimension getPrefSize (final Object e)
  {
    final ElementCacheCarrier ec = lookupCacheEntry(e);
    if (ec == null)
    {
      return null;
    }
    final StrictDimension dim = ec.getPrefSize();
    if (dim != null)
    {
      getCount++;
      return dim.getUnlockedInstance();
    }
    return null;
  }

  /**
   * Sets the minimum size of the element as computed by the layout manager.
   *
   * @param element the element.
   * @param d       the minimum size.
   */
  public void setMinSize (final Element element, final StrictDimension d)
  {
    if (element == null)
    {
      throw new NullPointerException("Element is null");
    }
    putCount++;

    final boolean dynamic = element.isDynamicContent();
    ElementCacheCarrier ec = lookupCacheEntry(element.getObjectID());
    if (ec == null)
    {
      ec = new ElementCacheCarrier();
      ec.setMinSize(d.getLockedInstance());
      ec.setDynamic(dynamic);
      if (dynamic)
      {
        dynamicCache.put(element.getObjectID(), ec);
      }
      else
      {
        elementCache.put(element.getObjectID(), ec);
      }
    }
    else
    {
      ec.setMinSize(d.getLockedInstance());
      if (dynamic != ec.isDynamic())
      {
        if (dynamic)
        {
          dynamicCache.put(element.getObjectID(), ec);
          elementCache.remove(element.getObjectID());
        }
        else
        {
          dynamicCache.remove(element.getObjectID());
          elementCache.put(element.getObjectID(), ec);
        }
      }
    }
  }

  /**
   * Sets the preferred size of the element as computed by the layout manager.
   *
   * @param element the element.
   * @param d       the minimum size.
   */
  public void setPrefSize (final Element element, final StrictDimension d)
  {
    if (element == null)
    {
      throw new IllegalArgumentException("LayoutCacheKey: Element is null");
    }

    putCount++;

    final boolean dynamic = element.isDynamicContent();
    ElementCacheCarrier ec = lookupCacheEntry(element.getObjectID());
    if (ec == null)
    {
      ec = new ElementCacheCarrier();
      ec.setPrefSize(d.getLockedInstance());
      ec.setDynamic(dynamic);
      elementCache.put(element.getObjectID(), ec);
    }
    else
    {
      ec.setPrefSize(d.getLockedInstance());
      if (dynamic != ec.isDynamic())
      {
        if (dynamic)
        {
          dynamicCache.put(element.getObjectID(), ec);
          elementCache.remove(element.getObjectID());
        }
        else
        {
          dynamicCache.remove(element.getObjectID());
          elementCache.put(element.getObjectID(), ec);
        }
      }
    }
  }

  /**
   * Returns true if the specified element is cachable, and false otherwise.
   * If the element itself is dynamic, the element is considered cachable. If
   * the element is a band and contains a dynamic element as its child,
   * it is considered uncachable.
   *
   * @param e the element.
   * @return A boolean.
   */
  public boolean isCachable (final Element e)
  {
    if (e.getStyle().getBooleanStyleProperty(ElementStyleSheet.ELEMENT_LAYOUT_CACHEABLE) == false)
    {
      return false;
    }

    if (e instanceof Band)
    {
      // search for dynamic elements within the element's children ...
      // if there is no dynamic element, then the element is cachable ...
      final Element[] elements = ((Band) e).getElementArray();
      for (int i = 0; i < elements.length; i++)
      {
        if (isChildCachable(elements[i]) == false)
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
   * Returns true if the specified element contains dynamic or non-cachable
   * elements.
   *
   * @param e the element.
   * @return A boolean.
   */
  private boolean isChildCachable (final Element e)
  {
    if (e.getStyle().getBooleanStyleProperty(ElementStyleSheet.ELEMENT_LAYOUT_CACHEABLE) == false)
    {
      return false;
    }

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
        if (isChildCachable(elements[i]) == false)
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
  public void flush ()
  {
    elementCache.clear();
    dynamicCache.clear();
  }

  public void flushDynamicCache ()
  {
    //Log.debug ("Flushing the dynamics cache");
    dynamicCache.clear();
  }
  
  /**
   * Prints debugging information.
   */
  public static void printResults ()
  {
    Log.debug("CacheResults: " +
            getCount + ":" + putCount + " Ratio: " +
            (getCount / (double) putCount));
  }
}
