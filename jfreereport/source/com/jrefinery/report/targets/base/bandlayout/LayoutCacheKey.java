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
 * ----------------
 * LayoutCacheKey.java
 * ----------------
 * (C)opyright 2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner (taquera@sherito.org);
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: LayoutCacheKey.java,v 1.1 2003/04/06 20:43:00 taqua Exp $
 *
 * Changes
 * -------
 * 06.04.2003 : Initial version
 */
package com.jrefinery.report.targets.base.bandlayout;

import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;

import com.jrefinery.report.Element;
import com.jrefinery.report.targets.style.ElementStyleSheet;

public class LayoutCacheKey
{
  private Dimension2D parentDim;
  private Dimension2D minSize;
  private Dimension2D maxSize;
  private Dimension2D prefSize;
  private Point2D absPos;

  protected LayoutCacheKey()
  {
  }

  public LayoutCacheKey(Element e, Dimension2D parentDim)
  {
    setElement(e);
    setParentDim(parentDim);
  }

  public boolean isSearchKey ()
  {
    return false;
  }

  protected void setElement(Element e)
  {
    if (e == null) throw new NullPointerException();
    minSize = (Dimension2D) e.getStyle().getStyleProperty(ElementStyleSheet.MINIMUMSIZE);
    minSize = (Dimension2D) minSize.clone();
    maxSize = (Dimension2D) e.getStyle().getStyleProperty(ElementStyleSheet.MAXIMUMSIZE);
    maxSize = (Dimension2D) maxSize.clone();
    prefSize = (Dimension2D) e.getStyle().getStyleProperty(ElementStyleSheet.PREFERREDSIZE);
    if (prefSize != null)
    {
      prefSize = (Dimension2D) prefSize.clone();
    }
    absPos = (Point2D) e.getStyle().getStyleProperty(StaticLayoutManager.ABSOLUTE_POS);
    if (absPos != null)
    {
      absPos = (Point2D) absPos.clone();
    }
  }

  protected void setParentDim(Dimension2D parentDim)
  {
    if (parentDim == null) throw new NullPointerException();
    this.parentDim = parentDim;
  }

  public Dimension2D getParentDim()
  {
    return parentDim;
  }

  public boolean equals(Object o)
  {
    if (this == o) return true;
    if (!(o instanceof LayoutCacheKey)) return false;

    final LayoutCacheKey layoutCacheKey = (LayoutCacheKey) o;
/*
    Log.debug ("--------------------");
    if (absPos != null && layoutCacheKey.absPos != null)
    {
      Log.debug ("AbsPos: " + absPos + "  " + layoutCacheKey.absPos);
    }
    if (maxSize != null && layoutCacheKey.maxSize != null)
    {
      Log.debug ("maxSize: " + maxSize + "  " + layoutCacheKey.maxSize);
    }
    if (minSize != null && layoutCacheKey.minSize != null)
    {
      Log.debug ("minSize: " + minSize + "  " + layoutCacheKey.minSize);
    }
    if (parentDim != null && layoutCacheKey.parentDim != null)
    {
      Log.debug ("parentDim: " + parentDim + "  " + layoutCacheKey.parentDim);
    }
    if (prefSize != null && layoutCacheKey.prefSize != null)
    {
      Log.debug ("prefSize: " + prefSize + "  " + layoutCacheKey.prefSize);
    }
*/
    if (absPos != null ? !absPos.equals(layoutCacheKey.absPos) : layoutCacheKey.absPos != null) return false;
    if (maxSize != null ? !maxSize.equals(layoutCacheKey.maxSize) : layoutCacheKey.maxSize != null) return false;
    if (minSize != null ? !minSize.equals(layoutCacheKey.minSize) : layoutCacheKey.minSize != null) return false;
    if (parentDim != null ? !parentDim.equals(layoutCacheKey.parentDim) : layoutCacheKey.parentDim != null) return false;
    if (prefSize != null ? !prefSize.equals(layoutCacheKey.prefSize) : layoutCacheKey.prefSize != null) return false;

    return true;
  }

  public int hashCode()
  {
    int result;
    result = (parentDim != null ? parentDim.hashCode() : 0);
    result = 29 * result + (minSize != null ? minSize.hashCode() : 0);
    result = 29 * result + (maxSize != null ? maxSize.hashCode() : 0);
    result = 29 * result + (prefSize != null ? prefSize.hashCode() : 0);
    result = 29 * result + (absPos != null ? absPos.hashCode() : 0);
    return result;
  }
}
