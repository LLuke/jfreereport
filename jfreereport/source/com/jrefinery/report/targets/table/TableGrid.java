/**
 * Date: Jan 25, 2003
 * Time: 8:40:14 AM
 *
 * $Id: TableGrid.java,v 1.1 2003/01/25 20:38:29 taqua Exp $
 */
package com.jrefinery.report.targets.table;

import com.jrefinery.report.util.Log;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.TreeSet;
import java.util.Iterator;

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


    int[] xCuts = getXCuts();
    Arrays.sort(xCuts);
    int[] yCuts = getYCuts();
    Arrays.sort(yCuts);

    for (int i = 0; i < xCuts.length; i++)
    {
      Log.debug("XCuts = " + xCuts[i]);
    }
    for (int i = 0; i < yCuts.length; i++)
    {
      Log.debug("YCuts = " + yCuts[i]);
    }

    TableGridLayout layout = new TableGridLayout(xCuts, yCuts);

    for (int i = 0; i < positions.length; i++)
    {
      TableCellData pos = positions[i];
      Rectangle2D bounds = pos.getBounds();
      int maxX = (int) (bounds.getX() + bounds.getWidth());
      int maxY = (int) (bounds.getY() + bounds.getHeight());

      TableGridPosition gPos = new TableGridPosition(pos);
      gPos.setCol(findDouble(xCuts, (int) bounds.getX(), false));
      gPos.setRow(findDouble(yCuts, (int) bounds.getY(), false));
      gPos.setColSpan(findDouble(xCuts, maxX, true) - gPos.getCol());
      gPos.setRowSpan(findDouble(yCuts, maxY, true) - gPos.getRow());

      Log.debug ("DebugChunk: " + pos.debugChunk);
      Log.debug ("gPos.getCol: " + gPos.getCol() + " -> " + layout.getColumnStart(gPos.getCol()));
      Log.debug ("gPos.getRow: " + gPos.getRow() + " -> " + layout.getRowStart(gPos.getRow()));
      Log.debug ("gPos.getColSpan: " + gPos.getColSpan() + " -> " + layout.getColumnEnd(gPos.getColSpan() + gPos.getCol() - 1));
      Log.debug ("gPos.getRowSpan: " + gPos.getRowSpan() + " -> " + layout.getRowEnd(gPos.getRowSpan() + gPos.getRow() - 1));


      int startY = gPos.getRow();
      int endY = gPos.getRow() + gPos.getRowSpan();
      for (int posY = startY; posY < endY; posY ++)
      {
        int startX = gPos.getCol();
        int endX = gPos.getCol() + gPos.getColSpan();
        for (int posX = startX; posX < endX; posX ++)
        {
          layout.add(posX, posY, gPos);
        }
      }
    }

    return layout;
  }

  private int findDouble (int[] data, int d, boolean upperBounds)
  {
    for (int i = 0; i < data.length; i++)
    {
      int dV = data[i];
      if (dV == d)
      {
        return i;
      }
      else
      {
        if (dV > d)
        {
          if (upperBounds)
          {
            return i;
          }
          else
          {
            if (i == 0)
              return 0;
            else
              return i - 1;
          }
        }
      }
    }
    return data.length;
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
