/**
 * Date: Jan 18, 2003
 * Time: 7:19:44 PM
 *
 * $Id: TableProducer.java,v 1.4 2003/01/27 03:17:43 taqua Exp $
 */
package com.jrefinery.report.targets.table;

import com.jrefinery.report.Band;
import com.jrefinery.report.Element;
import com.jrefinery.report.JFreeReport;
import com.jrefinery.report.targets.style.ElementStyleSheet;
import com.jrefinery.report.util.Log;

import java.awt.geom.Rectangle2D;
import java.util.List;

public abstract class TableProducer
{
  private TableGrid grid;

  public TableProducer(boolean strictLayout)
  {
    grid = new TableGrid(strictLayout);
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
    grid.clear();
  }

  /** Calculates the positions for the Excel cells. */
  protected TableGridLayout layoutGrid()
  {
    return grid.performLayout();
  }

  public int getCellCount ()
  {
    return grid.size();
  }

  public void addCell (TableCellData data)
  {
    grid.addData(data);
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
      Log.debug ("Band is invisible" + band);
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
        processElement(bounds, e, band.getClass().getName());
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
  private void processElement(Rectangle2D bounds, Element e, String band)
  {
    Rectangle2D elementBounds = (Rectangle2D)
        e.getStyle().getStyleProperty(ElementStyleSheet.BOUNDS);

    if (elementBounds == null)
    {
      throw new NullPointerException("No layout for element");
    }

    Rectangle2D drawBounds = translateSubRect(bounds, elementBounds);
    TableCellData data = getCellDataFactory().createCellData(e, drawBounds);
    if (data != null)
    {
      addCell(data);
      data.debugChunk = new Log.SimpleMessage("Element",
                                              e.getClass().getName(),
                                              " -> " ,
                                              new Log.SimpleMessage(
                                                  e.getName() ,
                                                  " (" , band , ")"));
    }
  }

}
