/**
 * Date: Jan 25, 2003
 * Time: 9:40:17 AM
 *
 * $Id: TableGridLayout.java,v 1.1 2003/01/25 20:38:30 taqua Exp $
 */
package com.jrefinery.report.targets.table;

import com.jrefinery.report.util.Log;

import java.awt.geom.Rectangle2D;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

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
            }
          }
        }
      }
      else
      {
        if (pos.getElement().isBackground())
        {
          if (pos.contains(root))
          {
            backGrounds.add(0, pos);
          }
        }
        else
        {

          Log.debug (new Log.SimpleMessage("Root already added: " , pos.getElement().getBounds()));
          Log.debug (new Log.SimpleMessage("+            added: " , root.getElement().getBounds()));
          Log.debug (new Log.SimpleMessage("+            added: " , pos.getElement().debugChunk));
          Log.debug (new Log.SimpleMessage("+            added: Col=" , new Integer(root.getCol()) , "  Row=" , new Integer(root.getRow())));
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
  }

  private Object data[][];
  private int[] xCuts;
  private int[] yCuts;
  private int maxX;
  private int maxY;

  public TableGridLayout(int[] xCuts, int[] yCuts)
  {
    // +1 for outer boundry ...
    data = new Object[xCuts.length + 1][yCuts.length + 1];
    this.xCuts = xCuts;
    this.yCuts = yCuts;
    Log.debug ("Created GridLayout with " + xCuts.length + ", " + yCuts.length);
  }

  public void add (int x, int y, TableGridPosition pos)
  {
    if (x < 0 || y < 0) throw new IllegalArgumentException("Parameter must be > 0");
    if (x > xCuts.length || y > yCuts.length)
      throw new IllegalArgumentException("Parameter must be < ?Cut size");

    Object o = data[x][y];
    if (o == null)
    {
      Element e = new Element();
      e.add(pos);
      data[x][y] = e;

    }
    else
    {
      Element e = (Element) data[x][y];
      e.add(pos);
    }
    Rectangle2D bounds = pos.getBounds();
    maxX = (int) Math.max(bounds.getX() + bounds.getWidth(), maxX);
    maxY = (int) Math.max(bounds.getY() + bounds.getHeight(), maxY);
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
}
