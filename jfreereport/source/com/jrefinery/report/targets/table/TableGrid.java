/**
 * Date: Jan 25, 2003
 * Time: 8:40:14 AM
 *
 * $Id$
 */
package com.jrefinery.report.targets.table;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.TreeSet;

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
    Double x = new Double(bounds.getX());
    Double y = new Double(bounds.getY());
    xBounds.add(x);
    yBounds.add(y);

    if (isStrict())
    {
      Double xW = new Double(bounds.getX() + bounds.getWidth());
      Double yW = new Double(bounds.getY() + bounds.getHeight());
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


    Double[] xCuts = getXCuts();
    Arrays.sort(xCuts);
    Double[] yCuts = getYCuts();
    Arrays.sort(yCuts);
    /*
    for (int i = 0; i < xCuts.length; i++)
    {
      Log.debug("X = " + xCuts[i]);
    }
    for (int i = 0; i < yCuts.length; i++)
    {
      Log.debug("Y = " + yCuts[i]);
    }
    */
    TableGridLayout layout = new TableGridLayout(xCuts, yCuts);

    for (int i = 0; i < positions.length; i++)
    {
      TableCellData pos = positions[i];
      Rectangle2D bounds = pos.getBounds();
      double maxX = bounds.getX() + bounds.getWidth();
      double maxY = bounds.getY() + bounds.getHeight();

      TableGridPosition gPos = new TableGridPosition(pos);
      gPos.setCol(findDouble(xCuts, bounds.getX(), false));
      gPos.setRow(findDouble(yCuts, bounds.getY(), false));
      gPos.setColSpan(findDouble(xCuts, maxX, true) - gPos.getCol());
      gPos.setRowSpan(findDouble(yCuts, maxY, true) - gPos.getRow());

      /*
      Log.debug ("gPos.getCol: " + gPos.getCol() + " -> " + layout.getColumnStart(gPos.getCol()));
      Log.debug ("gPos.getRow: " + gPos.getRow() + " -> " + layout.getRowStart(gPos.getRow()));
      Log.debug ("gPos.getColSpan: " + gPos.getColSpan() + " -> " + layout.getColumnEnd(gPos.getColSpan() + gPos.getCol() - 1));
      Log.debug ("gPos.getRowSpan: " + gPos.getRowSpan() + " -> " + layout.getRowEnd(gPos.getRowSpan() + gPos.getRow() - 1));
      */

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

  private int findDouble (Double[] data, double d, boolean upperBounds)
  {
    for (int i = 0; i < data.length; i++)
    {
      double dV = data[i].doubleValue();
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

  public Double[] getXCuts()
  {
    return (Double[]) xBounds.toArray(new Double[xBounds.size()]);
  }

  public Double[] getYCuts()
  {
    return (Double[]) yBounds.toArray(new Double[yBounds.size()]);
  }
}
