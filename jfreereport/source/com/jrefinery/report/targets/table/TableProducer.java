/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
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
 * -------------------
 * TableProducer.java
 * -------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: TableProducer.java,v 1.19 2003/03/18 18:28:45 taqua Exp $
 *
 * Changes
 * -------
 * 18-Jan-2003 : Initial version
 * 24-Feb-2003 : Fixed Checkstyle issues (DG);
 *
 */
package com.jrefinery.report.targets.table;

import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.jrefinery.report.Band;
import com.jrefinery.report.Element;
import com.jrefinery.report.JFreeReport;
import com.jrefinery.report.targets.style.ElementStyleSheet;

/**
 * The TableProducer is responsible for creating the produced Table. After
 * the writer has finished the band layout process, the layouted bands are
 * forwarded into the TableProducer. The TableProducer coordinates the cell
 * creation process and collects the generated TableCellData. The raw CellData
 * objects are later transformed into a TableGridLayout.
 * <p>
 * This class defines the global contract and provides some helper methods for
 * the implementors.
 * 
 * @author Thomas Morgner
 */
public abstract class TableProducer
{
  /** Literal text for the 'title' property name. */
  public static final String TITLE = "Title";

  /** Literal text for the 'author' property name. */
  public static final String AUTHOR = "Author";
  
  /** the grid, that stores the collected TableCellData. */
  private TableGrid grid;
  
  /** the dummy mode flag. */
  private boolean dummy;

  /** Storage for the output target properties. */
  private HashMap properties;

  /**
   * Creates a new TableProducer.
   *
   * @param strictLayout the strict layout flag. Set to true, to enable the strict
   * layout mode.
   */
  public TableProducer(boolean strictLayout)
  {
    properties = new HashMap();
    grid = new TableGrid(strictLayout);
    dummy = false;
  }

  /** A useful constant for specifying the creator constant. */
  protected static final String CREATOR =
      JFreeReport.getInfo().getName()
      + " version "
      + JFreeReport.getInfo().getVersion();

  /**
   * Starts the report writing. This method is called before any other report handling
   * method is called.
   */
  public abstract void open();

  /**
   * Closes the report and finishs the report writing. Any used resource should
   * be freed when this method returns. The current page is already closed.
   */
  public abstract void close();

  /**
   * Handles the end of a page.
   */
  public abstract void endPage();

  /**
   * Handles the start of a new page. The page name is given as parameter.
   * The TableWriter starts a new page whenever a manual pagebreak is found
   * in the report definition. The ReportProducer has been opened before.
   *
   * @param name the page name
   */
  public abstract void beginPage(String name);

  /**
   * Gets the TableProducer implementation of this TableProducer.
   *
   * @return the TableProducers TableCellDataFactory, which is used to create
   * the TableCellData.
   */
  public abstract TableCellDataFactory getCellDataFactory ();

  /**
   * Clears the grid, removes all created cells.
   */
  public void clearCells ()
  {
    grid.clear();
  }

  /** 
   * Calculates the positions for the Excel cells. 
   * 
   * @return The table grid layout.
   */
  protected TableGridLayout layoutGrid()
  {
    return grid.performLayout();
  }

  /**
   * Gets the number of created cells in the grid.
   *
   * @return the number of stored cells in the grid.
   */
  public int getCellCount ()
  {
    return grid.size();
  }

  /**
   * Adds a new TableCellData to the grid.
   *
   * @param data the new TableCellData.
   */
  protected void addCell (TableCellData data)
  {
    grid.addData(data);
  }

  /**
   * Returns true, if the TableProducer is open. Only open producers
   * are able to write TableCells or to create TableCellData from Elements.
   *
   * @return checks, whether the TableProducer is open.
   */
  public abstract boolean isOpen();


  /**
   * Processes the layouted band. The band is inserted on the specified bounds in
   * the TableGrid.
   *
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

    // do nothing if the band has no height...
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
        processBand(translateSubRect(bbounds, bounds), (Band) e);
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
    float w = (float) Math.min (outer.getX() + outer.getWidth() - inner.getX(), inner.getWidth());
    float h = (float) Math.min (outer.getY() + outer.getHeight() - inner.getY(), inner.getHeight());
    Rectangle2D rc = new Rectangle2D.Float(
        (float) (outer.getX() + inner.getX()),
        (float) (outer.getY() + inner.getY()),
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
    TableCellData data = getCellDataFactory().createCellData(e, drawBounds);
    if (data != null)
    {
      addCell(data);
    }
  }

  /**
   * Merges all TableCellBackgrounds contained in the given list. The list must
   * be sorted by preference, the first background in the list overlays all other
   * backgrounds.
   *
   * @param background the collected backgrounds for a single table cell.
   * @return the merged TableCellBackground.
   */
  protected TableCellBackground createTableCellStyle (List background)
  {
    if (background == null)
    {
      return null;
    }

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

  /**
   * Gets the dummy mode state, in dummy mode no output is done.
   *
   * @return true, if the producer is working in dummy mode, no output is done.
   */
  public boolean isDummy ()
  {
    return dummy;
  }

  /**
   * Defines the dummy mode.
   *
   * @param dummy set to true, to activate the dummy mode, so that all output is skipped. 
   */
  public void setDummy(boolean dummy)
  {
    this.dummy = dummy;
  }

  /**
   * Defines a property for this output target. Properties are the standard way of configuring
   * an output target.
   *
   * @param property  the name of the property to set (<code>null</code> not permitted).
   * @param value  the value of the property.  If the value is <code>null</code>, the property is
   * removed from the output target.
   */
  public void setProperty(String property, Object value)
  {
    if (property == null)
    {
      throw new NullPointerException();
    }

    if (value == null)
    {
      properties.remove(property);
    }
    else
    {
      properties.put(property, value);
    }
  }

  /**
   * Queries the property named with <code>property</code>. If the property is not found, <code>
   * null</code> is returned.
   *
   * @param property the name of the property to be queried
   *
   * @return the value stored under the given property name
   *
   * @throws java.lang.NullPointerException if <code>property</code> is null
   */
  public Object getProperty(String property)
  {
    return getProperty(property, null);
  }

  /**
   * Queries the property named with <code>property</code>. If the property is not found, the
   * default value is returned.
   *
   * @param property the name of the property to be queried
   * @param defaultValue the defaultvalue returned if there is no such property
   *
   * @return the value stored under the given property name
   *
   * @throws NullPointerException if <code>property</code> is null
   */
  public Object getProperty(String property, Object defaultValue)
  {
    if (property == null)
    {
      throw new NullPointerException();
    }

    Object retval = properties.get(property);
    if (retval == null)
    {
      return defaultValue;
    }
    return retval;
  }

  /**
   * Returns an enumeration of the property names.
   *
   * @return the enumeration.
   */
  protected Iterator getPropertyNames()
  {
    return properties.keySet().iterator();
  }

  /**
   * Sets the properties from the given hashtable into the internal properties
   * storage. All properties not contained in the given hashtable are removed.
   *
   * @param table the new properties collection.
   */
  public void setProperties (Map table)
  {
    properties.clear();
    properties.putAll(table);
  }
}
