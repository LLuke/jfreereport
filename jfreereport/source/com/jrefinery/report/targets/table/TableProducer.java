/**
 * Date: Jan 18, 2003
 * Time: 7:19:44 PM
 *
 * $Id: TableProducer.java,v 1.8 2003/01/30 00:04:54 taqua Exp $
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
  private boolean dummy;

  public TableProducer(boolean strictLayout)
  {
    grid = new TableGrid(strictLayout);
    dummy = false;
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
      return;
    }

    if (bounds.getHeight() == 0)
    {
      return;
    }

    processBandInner(bounds, band);
  }

  private void processBandInner (Rectangle2D bounds, Band band)
  {
    if (bounds.getHeight() == 0)
    {
      return;
    }
    // handle the band itself, the band's bounds are already translated.
    processElement(bounds, band);

    // process all elements
    Element[] l = band.getElementArray();
    for (int i = 0; i < l.length; i++)
    {
      Element e = l[i];
      if (e instanceof Band)
      {
        Rectangle2D bbounds = (Rectangle2D) e.getStyle().getStyleProperty(ElementStyleSheet.BOUNDS);
        processBandInner(translateSubRect(bbounds, bounds), (Band) e);
      }
      else
      {
        Rectangle2D elementBounds = (Rectangle2D)
            e.getStyle().getStyleProperty(ElementStyleSheet.BOUNDS);

        if (elementBounds == null)
        {
          throw new NullPointerException("No layout for element");
        }

        Rectangle2D drawBounds = translateSubRect(bounds, elementBounds);
        processElement(drawBounds, e);
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


    double w = Math.min (outer.getX() + outer.getWidth() - inner.getX(), inner.getWidth());
    double h = Math.min (outer.getY() + outer.getHeight() - inner.getY(), inner.getHeight());
    Rectangle2D rc = new Rectangle2D.Double(
        outer.getX() + inner.getX(),
        outer.getY() + inner.getY(),
        Math.max(0, w),
        Math.max(0, h));

    return rc;
  }

  /**
   * Add the specified element to the logical page. Create content from the values
   * contained in the element and format the content by using the element's attributes.
   * <p>
   * @param drawBounds  the bounds where the element should be painted in the target area.
   * @param e  the element.
   *
   * @throws NullPointerException if the element has no valid layout (no BOUNDS defined).
   * Bounds are usually defined by the BandLayoutManager.
   */
  private void processElement(Rectangle2D drawBounds, Element e)
  {
/*
    if (drawBounds.getHeight() == 0)
    {
      Log.info ("Log.Element   : " + e);
      Log.info ("Log.BandBounds: " + bandbounds);
    }
*/
    TableCellData data = getCellDataFactory().createCellData(e, drawBounds);
    if (data != null)
    {
      addCell(data);

      Band parent = e.getParent();
      Class parentClass = null;
      if (parent != null)
      {
        parentClass = parent.getClass();
      }
      data.debugChunk = new Log.SimpleMessage("Element ",
                                              e.getClass().getName(),
                                              " -> " ,
                                              new Log.SimpleMessage(
                                                  e.getName() ,
                                                  " (" , parentClass , ")"));
    }
  }

  protected TableCellBackground createTableCellStyle (List background)
  {
    if (background == null)
    {
      return null;
    }

    //Log.debug ("Background: " + background.size());

    TableCellBackground bg = null;
    for (int i = 0; i < background.size(); i++)
    {
      TableGridPosition listBgPos = (TableGridPosition) background.get(i);
      TableCellBackground listBg = (TableCellBackground) listBgPos.getElement();

      if (bg == null)
      {
        bg =  listBg;
      }
      else
      {
        bg = bg.merge(listBg);
      }
    }

    if (bg == null)
    {
      return null;
    }

    return (bg);
  }

  public boolean isDummy ()
  {
    return dummy;
  }

  public void setDummy(boolean dummy)
  {
    this.dummy = dummy;
  }
}
