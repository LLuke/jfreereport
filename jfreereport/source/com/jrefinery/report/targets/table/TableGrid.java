/**
 * Date: Jan 25, 2003
 * Time: 8:40:14 AM
 *
 * $Id: TableGrid.java,v 1.3 2003/01/27 18:24:53 taqua Exp $
 */
package com.jrefinery.report.targets.table;

import com.jrefinery.report.util.Log;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeSet;

public class TableGrid
{
  private TreeSet xBounds;
  private TreeSet yBounds;

  private int[] yBoundsArray;
  private int[] xBoundsArray;

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
    if (xBoundsArray == null)
    {
      int[] xBoundsArray = new int[xBounds.size()];
      Iterator it = xBounds.iterator();
      int count = 0;
      while (it.hasNext())
      {
        Integer i = (Integer) it.next();
        xBoundsArray[count] = i.intValue();
        count += 1;
      }
      this.xBoundsArray = xBoundsArray;
    }
    return xBoundsArray;
  }

  public int[] getYCuts()
  {
    if (yBoundsArray == null)
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
      this.yBoundsArray = yBoundsArray;
    }
    return yBoundsArray;
  }
}
