/**
 * Date: Jan 25, 2003
 * Time: 9:40:17 AM
 *
 * $Id: TableGridLayout.java,v 1.4 2003/01/28 22:05:25 taqua Exp $
 */
package com.jrefinery.report.targets.table;

import com.jrefinery.report.util.Log;

import java.awt.geom.Rectangle2D;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Arrays;

public class TableGridLayout
{
  public static class Element
  {
    private TableGridPosition root;
    private ArrayList backGrounds;

    public Element()
    {
      backGrounds = new ArrayList();
    }

    public void add(TableGridPosition pos)
    {
      if (root == null)
      {
        if (pos.getElement().isBackground())
        {
          backGrounds.add (0, pos);
          //Log.debug ("backGrounds -> added " + backGrounds.size());
        }
        else
        {
          root = pos;
          Iterator it = backGrounds.iterator();
          while (it.hasNext())
          {
            TableGridPosition gpos = (TableGridPosition) it.next();
            if (gpos.contains(root) == false)
            {
              it.remove();
              //Log.debug ("backGrounds !!removed " + backGrounds.size());
            }
          }
          //Log.debug ("backGrounds -> removed " + backGrounds.size());
        }
      }
      else
      {
        if (pos.getElement().isBackground())
        {
          if (pos.contains(root))
          {
            backGrounds.add(0, pos);
//            Log.debug ("backGrounds -> added " + backGrounds.size());
          }
          else
          {
//            Log.debug ("The background was not fully contained in this element");
          }
        }
        else
        {
          /*
          Log.warn (new Log.SimpleMessage("Root already added: " , pos.getElement().getBounds()));
          Log.warn (new Log.SimpleMessage("+            added: " , root.getElement().getBounds()));
          Log.warn (new Log.SimpleMessage("+            added: " , pos.getElement().debugChunk));
          Log.warn (new Log.SimpleMessage("+            added: Col=" , new Integer(root.getCol()) , "  Row=" , new Integer(root.getRow())));
          */
        }
      }
    }

    public List getBackground ()
    {
      return backGrounds;
    }

    public TableGridPosition getRoot()
    {
      return root;
    }

    public String toString ()
    {
      StringBuffer buffer = new StringBuffer();
      buffer.append("TableGridLayout.Element={root=");
      buffer.append(root);
      buffer.append(", backgrounds=");
      buffer.append(backGrounds);
      buffer.append("}");
      return buffer.toString();
    }
  }

  private Object data[][];
  private int[] xCuts;
  private int[] yCuts;
  private int maxX;
  private int maxY;

  public TableGridLayout(int[] pxCuts, int[] pyCuts, TableCellData[] positions)
  {
    this.xCuts = new int[pxCuts.length];
    this.yCuts = new int[pyCuts.length];

    System.arraycopy(pxCuts, 0, xCuts, 0, pxCuts.length);
    System.arraycopy(pyCuts, 0, yCuts, 0, pyCuts.length);

    Arrays.sort(xCuts);
    Arrays.sort(yCuts);

    // +1 for outer boundry ...
    int width = xCuts.length;
    int height = yCuts.length;
    Log.debug ("Created GridLayout with " + width + ", " + height);
    data = new Object[width][height];

    for (int i = 0; i < positions.length; i++)
    {
      TableCellData pos = positions[i];
      add(pos);
    }
  }

  protected void add (TableCellData pos)
  {
    Rectangle2D bounds = pos.getBounds();

    int maxBoundsX = (int) (bounds.getX() + bounds.getWidth());
    int maxBoundsY = (int) (bounds.getY() + bounds.getHeight());

    TableGridPosition gPos = new TableGridPosition(pos);
    gPos.setCol(findBoundry(xCuts, (int) bounds.getX(), false));
    gPos.setRow(findBoundry(yCuts, (int) bounds.getY(), false));
    gPos.setColSpan(Math.max(1, findBoundry(xCuts, maxBoundsX, true) - gPos.getCol()));
    gPos.setRowSpan(Math.max(1, findBoundry(yCuts, maxBoundsY, true) - gPos.getRow()));
/*
    if (pos instanceof TableBandArea)
    {
      Log.debug ("DebugChunk: " + pos.debugChunk);
      Log.debug ("gPos.getCol: " + gPos.getCol() + " -> " + getColumnStart(gPos.getCol()));
      Log.debug ("gPos.getRow: " + gPos.getRow() + " -> " + getRowStart(gPos.getRow()));
      Log.debug ("gPos.getColSpan: " + gPos.getColSpan() + " -> " + getColumnEnd(gPos.getColSpan() + gPos.getCol() - 1));
      Log.debug ("gPos.getRowSpan: " + gPos.getRowSpan() + " -> " + getRowEnd(gPos.getRowSpan() + gPos.getRow() - 1));
    }
*/
    int startY = gPos.getRow();
    int endY = gPos.getRow() + gPos.getRowSpan();
    for (int posY = startY; posY < endY; posY ++)
    {
      int startX = gPos.getCol();
      int endX = gPos.getCol() + gPos.getColSpan();
      for (int posX = startX; posX < endX; posX ++)
      {
        try
        {
          addToGrid(posX, posY, gPos);
        }
        catch (IndexOutOfBoundsException ie)
        {
          Log.debug ("DebugChunk: " + pos.debugChunk);
          Log.debug ("gPos.getCol: " + gPos.getCol());// + " -> " + getColumnStart(gPos.getCol()));
          Log.debug ("gPos.getRow: " + gPos.getRow());// + " -> " + getRowStart(gPos.getRow()));
          Log.debug ("gPos.getColSpan: " + gPos.getColSpan());// + " -> " + getColumnEnd(gPos.getColSpan() + gPos.getCol() - 1));
          Log.debug ("gPos.getRowSpan: " + gPos.getRowSpan());// + " -> " + getRowEnd(gPos.getRowSpan() + gPos.getRow() - 1));
          throw ie;
        }
      }
    }

    this.maxX = Math.max(this.maxX, maxBoundsX);
    this.maxY = Math.max(this.maxY, maxBoundsY);
  }

  protected void addToGrid (int posX, int posY, TableGridPosition gPos)
  {
    if (posX >= getWidth())
      throw new IndexOutOfBoundsException("X: " + posX + " > " + getWidth());

    if (posY >= getHeight())
      throw new IndexOutOfBoundsException("Y: " + posY + " > " + getHeight());

    Object o = data[posX][posY];
    if (o == null)
    {
      Element e = new Element();
      e.add(gPos);
      data[posX][posY] = e;

    }
    else
    {
      Element e = (Element) o;
      e.add(gPos);
    }
  }

  public Element getData (int x, int y)
  {
    return (Element) data[x][y];
  }

  public int getWidth()
  {
    return xCuts.length;
  }

  public int getHeight()
  {
    return yCuts.length;
  }

  public int getColumnStart (int column)
  {
    return xCuts[column];
  }

  public int getRowStart (int row)
  {
    return yCuts[row];
  }

  public int getColumnEnd (int column)
  {
    if (column == xCuts.length - 1)
    {
      return maxX;
    }
    else
    {
      return getColumnStart(column + 1);
    }
  }

  public int getRowEnd (int row)
  {
    if (row == yCuts.length - 1)
    {
      return maxY;
    }
    else
    {
      return getRowStart(row + 1);
    }
  }

  private int findBoundry (int[] data, int d, boolean upperBounds)
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
    return data.length - 1;
  }


}
