/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
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
 * $Id: TableProducer.java,v 1.4 2003/08/18 18:28:01 taqua Exp $
 *
 * Changes
 * -------
 * 18-Jan-2003 : Initial version
 * 24-Feb-2003 : Fixed Checkstyle issues (DG);
 *
 */
package org.jfree.report.modules.output.table.base;

import java.awt.geom.Rectangle2D;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.jfree.report.Band;
import org.jfree.report.Element;
import org.jfree.report.JFreeReport;
import org.jfree.report.style.ElementStyleSheet;

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

  /** A constant to define that this layout has not yet printed any pages. */
  private static final int BEFORE_FIRST_PAGE = -1;

  /** the dummy mode flag. */
  private boolean dummy;

  /** the strict layout flag. */
  private boolean strictLayout;

  /** Storage for the output target properties. */
  private Properties properties;

  /** The layout. If in dummy mode, then this contains only the Bounds. */
  private TableGridBounds gridBounds;

  /** The collected grid bounds. This collection is used to store the table grid layout. */
  private TableLayoutInfo gridBoundsCollection;

  /** The layouted elements. Used during the non-dummy mode. */
  private TableGrid grid;

  /** The number of the current page. */
  private int page;

  /**
   * Creates a new TableProducer. This constructor should be used to create an
   * producer for the repagination process. 
   *
   * @param gridBoundsCollection the layout information implementation used to create 
   * the table layout.  
   * @param strictLayout the strict layout flag. Set to true, to enable the strict
   * layout mode.
   */
  public TableProducer(final TableLayoutInfo gridBoundsCollection, 
                       final boolean strictLayout)
  {
    this.page = BEFORE_FIRST_PAGE;
    this.properties = new Properties();
    this.dummy = true;
    this.strictLayout = strictLayout;
    this.gridBoundsCollection = gridBoundsCollection;
  }

  /**
   * Creates a new TableProducer. This constructor must be used to complete the content
   * creation after the pagination was done.
   *
   * @param gridBoundsCollection the layout information implementation used to create 
   * the table layout.  
   */
  public TableProducer(final TableLayoutInfo gridBoundsCollection)
  {
    if (gridBoundsCollection.getPageCount() == 0)
    {
      throw new IllegalArgumentException("The bounds collection must not be empty.");
    }
    this.page = BEFORE_FIRST_PAGE;
    this.properties = new Properties();
    this.dummy = false;
    this.gridBoundsCollection = gridBoundsCollection;
    this.grid = new TableGrid(gridBoundsCollection.getLayoutForPage(0).isStrict());
  }

  /** A useful constant for specifying the creator constant. */
  protected static final String CREATOR =
      JFreeReport.getInfo().getName()
      + " version "
      + JFreeReport.getInfo().getVersion();

  /**
   * Starts the report writing. This method is called before any other report handling
   * method is called. This method is called only once for a given instance.
   */
  public abstract void open();

  /**
   * Closes the report and finishs the report writing. Any used resource should
   * be freed when this method returns. The current page is already closed.
   * This method is called only once for a given instance.
   */
  public abstract void close();

  /**
   * Handles the end of a page. This calls commit() and clears the layout.
   */
  public void endPage()
  {
    commit();
    if (isDummy() == false)
    {
      clearCells();
    }
  }

  /**
   * Handles the start of a new page. The page name is given as parameter.
   * The TableWriter starts a new page whenever a manual pagebreak is found
   * in the report definition. The ReportProducer has been opened before.
   * <p>
   * Always make sure that you call <code>super.beginPage()</code> before
   * any elements are added by the producer.
   *
   * @param name the page name
   */
  public void beginPage(String name)
  {
    page += 1;

    // the global layout reuses the layout grid from the first page to
    // unify the layout for all pages. The global layout is disabled by
    // default.
    if (isGlobalLayout() == false || page == 0)
    {
      if (isDummy())
      {
        gridBounds = new TableGridBounds(isStrictLayout());
        gridBoundsCollection.addLayout (gridBounds);
      }
      else
      {
        gridBounds = gridBoundsCollection.getLayoutForPage(page);
      }
    }
  }

  /**
   * Gets the TableProducer implementation of this TableProducer.
   *
   * @return the TableProducers TableCellDataFactory, which is used to create
   * the TableCellData.
   */
  public abstract TableCellDataFactory getCellDataFactory();

  /**
   * Clears the grid, removes all created cell bounds.
   */
  public void clearCellsBounds()
  {
    gridBounds.clear();
  }

  /**
   * Clears the contents of the table grid. The layout itself is not cleared, only all
   * data is removed.
   */
  public void clearCells ()
  {
    if (isDummy() == false)
    {
      grid.clear();
    }
    else
    {
      throw new IllegalStateException("This is the dummy mode, no layout possible.");
    }
  }

  /**
   * Checks whether the current layout contains some content. Returns false, if the table
   * is empty.
   * 
   * @return true, if there is content, false otherwise.
   */
  public boolean isLayoutContainsContent ()
  {
    if (grid == null)
    {
      return false;
    }
    return grid.size() != 0;
  }

  /**
   * Calculates the positions for the table cells.
   *
   * @return The table grid layout.
   * @throws IllegalStateException if called while this producer is in dummy mode.
   */
  protected TableGridLayout layoutGrid()
  {
    if (isDummy() == false)
    {
      return grid.performLayout();
    }
    else
    {
      throw new IllegalStateException("This is the dummy mode, no layout possible.");
    }
  }

  /**
   * Adds a new TableCellData to the grid.
   *
   * @param data the new TableCellData.
   */
  protected void addCell(final TableCellData data)
  {
    if (isDummy() == false)
    {
      grid.addData(data);
    }
    else
    {
      gridBounds.addData(data);
    }
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
   * @return true, if at least one cell was accepted, false otherwise.
   */
  public boolean processBand(final Rectangle2D bounds, final Band band)
  {
    if (isOpen() == false)
    {
      throw new IllegalStateException("Producer already closed");
    }

    // do nothing if the band is invisble
    if (band.isVisible() == false)
    {
      return false;
    }

    // do nothing if the band has no height...
    if (bounds.getHeight() == 0)
    {
      return false;
    }

    // handle the band itself, the band's bounds are already translated.
    boolean retval = processElement(bounds, band);

    // process all elements
    final Element[] l = band.getElementArray();
    for (int i = 0; i < l.length; i++)
    {
      final Element e = l[i];
      if (e instanceof Band)
      {
        final Rectangle2D bbounds = (Rectangle2D) 
          e.getStyle().getStyleProperty(ElementStyleSheet.BOUNDS);
        if (processBand(translateSubRect(bbounds, bounds), (Band) e) == true)
        {
          retval = true;
        }
      }
      else
      {
        final Rectangle2D elementBounds = (Rectangle2D)
            e.getStyle().getStyleProperty(ElementStyleSheet.BOUNDS);

        if (elementBounds == null)
        {
          throw new NullPointerException("No layout for element");
        }

        final Rectangle2D drawBounds = translateSubRect(bounds, elementBounds);
        if (processElement(drawBounds, e) == true)
        {
          retval = true;
        }
      }
    }
    return retval;
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
  private Rectangle2D translateSubRect(final Rectangle2D outer, final Rectangle2D inner)
  {
    final float w = 
      (float) Math.min(outer.getX() + outer.getWidth() - inner.getX(), inner.getWidth());
    final float h = 
      (float) Math.min(outer.getY() + outer.getHeight() - inner.getY(), inner.getHeight());
    final Rectangle2D rc = new Rectangle2D.Float(
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
   * @return true, if the cell was added to the table content, and false otherwise.
   * @throws NullPointerException if the element has no valid layout (no BOUNDS defined).
   * Bounds are usually defined by the BandLayoutManager.
   */
  private boolean processElement(final Rectangle2D drawBounds, final Element e)
  {
    final TableCellData data = getCellDataFactory().createCellData(e, drawBounds);
    if (data != null)
    {
      addCell(data);
      return true;
    }
    return false;
  }

  /**
   * Merges all TableCellBackgrounds contained in the given list. The list must
   * be sorted by preference, the first background in the list overlays all other
   * backgrounds.
   *
   * @param background the collected backgrounds for a single table cell.
   * @return the merged TableCellBackground.
   */
  protected TableCellBackground createTableCellStyle(final List background)
  {
    if (background == null)
    {
      return null;
    }

    TableCellBackground bg = null;
    for (int i = 0; i < background.size(); i++)
    {
      final TableGridPosition listBgPos = (TableGridPosition) background.get(i);
      final TableCellBackground listBg = (TableCellBackground) listBgPos.getElement();

      if (bg == null)
      {
        bg = listBg;
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
  public boolean isDummy()
  {
    return dummy;
  }

  /**
   * Defines a property for this output target. Properties are the standard way of configuring
   * an output target.
   *
   * @param property  the name of the property to set (<code>null</code> not permitted).
   * @param value  the value of the property.  If the value is <code>null</code>, the property is
   * removed from the output target.
   */
  public void setProperty(final String property, final String value)
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
  public String getProperty(final String property)
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
  public String getProperty(final String property, final String defaultValue)
  {
    if (property == null)
    {
      throw new NullPointerException();
    }

    return properties.getProperty(property, defaultValue);
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
   * Configures the table producer by reading the configuration settings from
   * the given map.
   *
   * @param configuration the configuration supplied by the table processor.
   */
  public void configure(Properties configuration)
  {
    properties.putAll(configuration);
  }

  /**
   * Write the collected data. This method is called when ever it is safe to
   * commit all previous content. An auto-commit is also performed after the page
   * has ended.
   * <p>
   * Implementations have to take care, that empty commits do not produce any
   * output. Successfully written content must be removed.
   */
  public abstract void commit ();

  /**
   * Returns true, if the strict layouting algorithm is used.
   *
   * @return true if strict layouting is used, false otherwise.
   */
  public boolean isStrictLayout()
  {
    return strictLayout;
  }

  /**
   * Returns the value of the local gridBoundsCollection's Global Layout flag.
   * The global layout makes sure, that all pages use the same table grid layout to
   * format their contents. This makes sure that all pages have the same look.
   * 
   * @return true, if a global layout is used, false otherwise
   */
  protected boolean isGlobalLayout ()
  {
    return gridBoundsCollection.isGlobalLayout();
  }

  /**
   * The collected layout information.
   *
   * @return the collected bounds of the table grid.
   */
  public TableLayoutInfo getGridBoundsCollection()
  {
    return gridBoundsCollection;
  }
}
