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
 * $Id:$
 *
 * Changes
 * -------
 * 03-Dec-2002 : Added header and Javadoc comments (DG);
 *
 */
package com.jrefinery.report.targets.pageable.bandlayout;

import com.jrefinery.report.Band;
import com.jrefinery.report.Element;

import java.awt.geom.Dimension2D;
import java.util.Hashtable;

/**
 * A cache for a band layout manager.
 *
 * @author Thomas Morgner
 */
public class LayoutManagerCache
{
  /**
   * Caches info for a single element.
   */
  private class ElementCacheCarrier
  {
    /** The minimum size. */
    public Dimension2D minSize;
    
    /** The preferred size. */
    public Dimension2D prefSize;
  }

  /** The last band. */
  private Band lastBand;
  
  /** The element cache. */
  private Hashtable elementCache;

  /**
   * Creates a new cache.
   */
  public LayoutManagerCache()
  {
    lastBand = null;
    elementCache = new Hashtable();
  }

  /**
   * Sets the current band.
   *
   * @param b  the band (null permitted).
   */
  public void setCurrentBand (Band b)
  {
    if (b == null)
    {
      lastBand = null;
      elementCache.clear();
    }
    else if (lastBand == null)
    {
      lastBand = b;
    }
    else if (lastBand.equals(b) == false)
    {
      elementCache.clear();
      lastBand = b;
    }
  }

  /**
   * Caches the minimum size for an element.
   *
   * @param e  the element.
   * @param min  the minimum size.
   */
  public void putMinSize (Element e, Dimension2D min)
  {
    ElementCacheCarrier ec = (ElementCacheCarrier) elementCache.get (e);
    if (ec == null)
    {
      ec = new ElementCacheCarrier();
      elementCache.put(e, ec);
    }
    ec.minSize = min;
  }

  /**
   * Fetches the cached minimum size for an element.
   * <p>
   * This method returns <code>null</code> if the element record is not found in the cache.
   *
   * @param e  the element.
   *
   * @return  the minimum size.
   */
  public Dimension2D getMinSize (Element e)
  {
    ElementCacheCarrier ec = (ElementCacheCarrier) elementCache.get(e);
    if (ec != null)
    {
      return ec.minSize;
    }
    return null;
  }

  /**
   * Caches the preferred size for an element.
   *
   * @param e  the element.
   * @param preferred  the minimum size.
   */
  public void putPrefSize (Element e, Dimension2D preferred)
  {
    ElementCacheCarrier ec = (ElementCacheCarrier) elementCache.get (e);
    if (ec == null)
    {
      ec = new ElementCacheCarrier();
      elementCache.put(e, ec);
    }
    ec.prefSize = preferred;
  }

  /**
   * Fetches the cached preferred size for an element.
   * <p>
   * This method returns <code>null</code> if the element record is not found in the cache.
   *
   * @param e  the element.
   *
   * @return  the preferred size.
   */
  public Dimension2D getPrefSize (Element e)
  {
    ElementCacheCarrier ec = (ElementCacheCarrier) elementCache.get(e);
    if (ec != null)
    {
      return ec.prefSize;
    }
    return null;
  }

  /**
   * Clears the cache.
   */
  public void flushCache()
  {
    setCurrentBand(null);
  }

}
