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
 * -------------------
 * TableGrid.java
 * -------------------
 * (C)opyright 2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: TableGrid.java,v 1.5 2003/01/29 21:57:12 taqua Exp $
 *
 * Changes
 * -------
 * 25-Jan-2003 : Initial version
 */
package com.jrefinery.report.targets.table;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeSet;

/**
 * The TableGrid is used to collect all table cells and to finally create the
 * TableGridLayout. The bounds of the 
 */
public class TableGrid
{
  private TreeSet xBounds;
  private TreeSet yBounds;

  private ArrayList elements;
  private boolean strict;

  public TableGrid(boolean strict)
  {
    xBounds = new TreeSet();
    yBounds = new TreeSet();
    elements = new ArrayList();
    this.strict = strict;
  }

  public void addData (TableCellData pos)
  {
    //Log.debug ("Add TableCellData: " + pos);

    elements.add(pos);

    Rectangle2D bounds = pos.getBounds();
    Integer x = new Integer((int) bounds.getX());
    Integer y = new Integer((int) bounds.getY());
    xBounds.add(x);
    yBounds.add(y);

    if (isStrict())
    {
      Integer xW = new Integer((int) (bounds.getX() + bounds.getWidth()));
      Integer yW = new Integer((int) (bounds.getY() + bounds.getHeight()));
      xBounds.add (xW);
      yBounds.add (yW);
    }
  }

  public boolean isStrict()
  {
    return strict;
  }

  public TableGridLayout performLayout ()
  {
    TableCellData[] positions =
        (TableCellData[]) elements.toArray(new TableCellData[elements.size()]);

    //Log.debug ("Performing Layout: " + positions.length);
    TableGridLayout layout = new TableGridLayout(getXCuts(), getYCuts(), positions);
    return layout;
  }

  public void clear ()
  {
    xBounds.clear();
    yBounds.clear();
    elements.clear();
  }

  public int size()
  {
    return elements.size();
  }

  public int[] getXCuts()
  {
    if (xBounds.size() == 0)
      return new int[0];

    int[] xBoundsArray = new int[xBounds.size()];
    Iterator it = xBounds.iterator();
    int count = 0;
    while (it.hasNext())
    {
      Integer i = (Integer) it.next();
      xBoundsArray[count] = i.intValue();
      count += 1;
    }

    if (!strict)
    {
      return xBoundsArray;
    }
    // in strict mode, all boundries are added. The last boundry does
    // not define a start of a cell, so it is removed.

    int[] retval = new int[xBoundsArray.length - 1];
    System.arraycopy(xBoundsArray, 0, retval, 0, retval.length);
    return retval;
  }

  public int[] getYCuts()
  {
    int[] yBoundsArray = new int[yBounds.size()];
    Iterator it = yBounds.iterator();
    int count = 0;
    while (it.hasNext())
    {
      Integer i = (Integer) it.next();
      yBoundsArray[count] = i.intValue();
      count += 1;
    }

    if (!strict)
    {
      return yBoundsArray;
    }
    // in strict mode, all boundries are added. The last boundry does
    // not define a start of a cell, so it is removed.

    int[] retval = new int[yBoundsArray.length - 1];
    System.arraycopy(yBoundsArray, 0, retval, 0, retval.length);
    return retval;
  }
}
