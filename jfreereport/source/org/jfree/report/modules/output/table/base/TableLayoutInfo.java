/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
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
 * TableLayoutInfo.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id$
 *
 * Changes 
 * -------------------------
 * 12.07.2003 : Initial version
 *  
 */

package org.jfree.report.modules.output.table.base;

import java.util.ArrayList;

public class TableLayoutInfo
{
  private boolean globalLayout;
  private ArrayList pageLayouts;

  public TableLayoutInfo(boolean globalLayout)
  {
    this.pageLayouts = new ArrayList();
    this.globalLayout = globalLayout;
  }

  public void addLayout (TableGridBounds bounds)
  {
    if (isGlobalLayout())
    {
      if (pageLayouts.isEmpty())
      {
        pageLayouts.add (bounds);
      }
      else
      {
        pageLayouts.set(0, bounds);
      }
    }
    else
    {
      pageLayouts.add (bounds);
    }
  }

  public boolean isGlobalLayout ()
  {
    return globalLayout;
  }

  public TableGridBounds getLayoutForPage (int page)
  {
    if (isGlobalLayout())
    {
      return (TableGridBounds) pageLayouts.get(0);
    }
    else
    {
      return (TableGridBounds) pageLayouts.get(page);
    }
  }

  public int getPageCount ()
  {
    return pageLayouts.size();
  }
}
