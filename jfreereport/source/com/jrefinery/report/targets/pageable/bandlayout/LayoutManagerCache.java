/**
 * Date: Nov 27, 2002
 * Time: 6:05:43 PM
 *
 * $Id$
 */
package com.jrefinery.report.targets.pageable.bandlayout;

import com.jrefinery.report.Band;
import com.jrefinery.report.Element;

import java.awt.geom.Dimension2D;
import java.util.Hashtable;

public class LayoutManagerCache
{
  private class ElementCacheCarrier
  {
    public Dimension2D minSize;
    public Dimension2D prefSize;
  }

  private Band lastBand;
  private Hashtable elementCache;

  public LayoutManagerCache()
  {
    lastBand = null;
    elementCache = new Hashtable();
  }

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

  public Dimension2D getMinSize (Element e)
  {
    ElementCacheCarrier ec = (ElementCacheCarrier) elementCache.get(e);
    if (ec != null)
    {
      return ec.minSize;
    }
    return null;
  }

  public void putPrefSize (Element e, Dimension2D min)
  {
    ElementCacheCarrier ec = (ElementCacheCarrier) elementCache.get (e);
    if (ec == null)
    {
      ec = new ElementCacheCarrier();
      elementCache.put(e, ec);
    }
    ec.prefSize = min;
  }

  public Dimension2D getPrefSize (Element e)
  {
    ElementCacheCarrier ec = (ElementCacheCarrier) elementCache.get(e);
    if (ec != null)
    {
      return ec.prefSize;
    }
    return null;
  }

  // forget everyting that might been cached
  public void flushCache()
  {
    setCurrentBand(null);
  }


}
