/**
 * Date: Jan 18, 2003
 * Time: 7:19:44 PM
 *
 * $Id$
 */
package com.jrefinery.report.targets.table;

import com.jrefinery.report.JFreeReport;
import com.jrefinery.report.Band;
import com.jrefinery.report.Element;
import com.jrefinery.report.TextElement;
import com.jrefinery.report.targets.table.TableCellData;
import com.jrefinery.report.targets.table.TableCellDataFactory;
import com.jrefinery.report.targets.table.TableGridPosition;
import com.jrefinery.report.targets.style.ElementStyleSheet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.awt.geom.Rectangle2D;

public abstract class TableProducer
{
  /** The list of all the cells to be generated within the XLS-file */
  private ArrayList cells;

  protected static class TableProducerLayout
  {
    /** The list of all x-positions where cells begin or end */
    private ArrayList xCuts;

    /** The list of all y-positions where cells begin or end */
    private ArrayList yCuts;

    private TableGridPosition[][] grid;

    public TableProducerLayout(TableGridPosition[][] grid, ArrayList xCuts, ArrayList yCuts)
    {
      this.grid = grid;
      this.yCuts = yCuts;
      this.xCuts = xCuts;
    }

    public ArrayList getxCuts()
    {
      return xCuts;
    }

    public ArrayList getyCuts()
    {
      return yCuts;
    }

    public TableGridPosition[][] getGrid()
    {
      return grid;
    }
  }

  public TableProducer()
  {
    cells = new ArrayList();
  }

  /** A useful constant for specifying the PDF creator. */
  protected static final String CREATOR =
      JFreeReport.getInfo().getName()
      + " version "
      + JFreeReport.getInfo().getVersion();

  public abstract void open();
  public abstract void close();

  public abstract void endPage();
  public abstract void beginPage(String name);
  public abstract TableCellDataFactory getCellDataFactory ();

  public void clearCells ()
  {
    cells.clear();
  }

  /** Calculates the positions for the Excel cells. */
  protected TableProducerLayout layoutGrid()
  {
    ArrayList xCuts = new ArrayList();
    ArrayList yCuts = new ArrayList();

    int nSize = cells.size();
    // System.out.println("We have " + nSize + " cells");
    for (int i = 0; i < nSize; i++)
    {
      TableCellData cell = (TableCellData) cells.get(i);
      // System.out.println("Cell # " + i + " : " + cell);

      // which coordinates are used?
      Rectangle2D bounds = cell.getBounds();
      Long minX = new Long(Math.round(bounds.getX()));
      Long maxX = new Long(Math.round(bounds.getX() + bounds.getWidth()));
      Long minY = new Long(Math.round(bounds.getY()));
      Long maxY = new Long(Math.round(bounds.getY() + bounds.getHeight()));
      if (!xCuts.contains(minX))
      {
        xCuts.add(minX);
      }
      if (!xCuts.contains(maxX))
      {
        xCuts.add(maxX);
      }
      if (!yCuts.contains(minY))
      {
        yCuts.add(minY);
      }
      if (!yCuts.contains(maxY))
      {
        yCuts.add(maxY);
      }
    }
    Collections.sort(xCuts);
    Collections.sort(yCuts);
    // let's have a look at the results
    /*
    int nXCount = xCuts.size();
    for (int i = 0; i < nXCount; i++)
    {
      // System.out.println("found XCut at " + xCuts.get(i));
    }
    int nYCount = yCuts.size();
    for (int i = 0; i < nYCount; i++)
    {
      // System.out.println("found YCut at " + yCuts.get(i));
    }
    */

    // generate a grid with empty cells
    int xCellCount = xCuts.size() - 1;
    int yCellCount = yCuts.size() - 1;

    TableGridPosition[][] grid = new TableGridPosition[yCellCount][xCellCount];
    for (int j = 0; j < yCellCount; j++)
    {
      for (int i = 0; i < xCellCount; i++)
      {
        grid[j][i] =
            new TableGridPosition(
                null,
                ((Long) xCuts.get(i + 1)).longValue() - ((Long) xCuts.get(i)).longValue(),
                ((Long) yCuts.get(j + 1)).longValue() - ((Long) yCuts.get(j)).longValue(),
                1,
                1
            );
      }
    }
    // place all used cells within the grid
    for (int i = 0; i < nSize; i++)
    {
      TableCellData cell = (TableCellData) cells.get(i);

      Rectangle2D bounds = cell.getBounds();
      Long minX = new Long(Math.round(bounds.getX()));
      Long maxX = new Long(Math.round(bounds.getX() + bounds.getWidth()));
      Long minY = new Long(Math.round(bounds.getY()));
      Long maxY =
          new Long(Math.round(bounds.getY() + bounds.getHeight()));
      // Debug
      // System.out.println( "Positions: minX=" + minX + ", maxX=" + maxX + ", minY=" + minY + ", maxY=" + maxY);
      int x1 = xCuts.indexOf(minX);
      int y1 = yCuts.indexOf(minY);
      int x2 = xCuts.indexOf(maxX);
      int y2 = yCuts.indexOf(maxY);

      int colSpan = x2 - x1;
      int rowSpan = y2 - y1;
      int width = maxX.intValue() - minX.intValue();
      int height = maxY.intValue() - minY.intValue();

      grid[y1][x1] = new TableGridPosition(cell, width, height, colSpan, rowSpan);
    }
    TableProducerLayout layout = new TableProducerLayout(grid, xCuts, yCuts);
    return layout;
  }

  public int getCellCount ()
  {
    return cells.size();
  }

  public void addCell (TableCellData data)
  {
    cells.add(data);
  }

  public abstract boolean isOpen();


  /**
   * @param bounds the bounds that define where to print the given band on this logical page
   * @param band the band that should be spooled/printed
   */
  public void processBand(Rectangle2D bounds, Band band)
  {
    if (isOpen() == false)
    {
      throw new IllegalStateException("Band already closed");
    }

    // do nothing if the band is invisble
    if (band.isVisible() == false)
    {
      return;
    }
    // do nothing if the band has a height of 0 (also invisible)
    if (bounds.getHeight() == 0)
    {
      return;
    }

    // process all elements
    List l = band.getElements();
    for (int i = 0; i < l.size(); i++)
    {
      Element e = (Element) l.get(i);
      if (e instanceof Band)
      {
        Rectangle2D bbounds = (Rectangle2D) e.getStyle().getStyleProperty(ElementStyleSheet.BOUNDS);
        processBand(translateSubRect(bbounds, bounds), (Band) e);
      }
      else
      {
        processElement(bounds, e);
      }
    }
  }

  /**
   * Converts an inner rectangle to the coordinate space of the outer rectangle.
   * The inner rectangle's origin (0,0) is mapped to the outer rectangles upper
   * left corner.
   *
   * @param outer the outer rectangle in the global coordinate space
   * @param inner the inner rectangle in the local coordinate space
   * @return the translated sub rectangle.
   */
  private Rectangle2D translateSubRect(Rectangle2D outer, Rectangle2D inner)
  {
    Rectangle2D rt = outer.getBounds2D();

    double w = Math.min (rt.getWidth() - inner.getX(), inner.getWidth());
    double h = Math.min (rt.getHeight() - inner.getY(), inner.getHeight());
    rt.setRect(
        rt.getX() + inner.getX(),
        rt.getY() + inner.getY(),
        Math.max(0, w),
        Math.max(0, h));
    return rt;
  }

  /**
   * Add the specified element to the logical page. Create content from the values
   * contained in the element and format the content by using the element's attributes.
   * <p>
   * @param bounds  the element bounds.
   * @param e  the element.
   *
   * @throws NullPointerException if the element has no valid layout (no BOUNDS defined).
   * Bounds are usually defined by the BandLayoutManager.
   */
  private void processElement(Rectangle2D bounds, Element e)
  {
    if (e.isVisible() == false)
    {
      return;
    }
    if (e.getContentType().equals(TextElement.CONTENT_TYPE) == false)
    {
      return;
    }

    Rectangle2D elementBounds = (Rectangle2D)
        e.getStyle().getStyleProperty(ElementStyleSheet.BOUNDS);
    if (elementBounds == null)
    {
      throw new NullPointerException("No layout for element");
    }
    Rectangle2D drawBounds = translateSubRect(bounds, elementBounds);
    addCell(getCellDataFactory().createCellData(e, drawBounds));
  }
}
