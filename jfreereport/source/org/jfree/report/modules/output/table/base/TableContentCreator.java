/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
 *
 * (C) Copyright 2000-2003, by Object Refinery Limited and Contributors.
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
 * ------------------------------
 * TableContentCreator.java
 * ------------------------------
 * (C)opyright 2004, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Object Refinery Limited);
 *
 * $Id: TableContentCreator.java,v 1.6 2005/09/04 16:45:51 taqua Exp $
 *
 * Changes 
 * -------------------------
 * Feb 27, 2004 : Initial version
 *  
 */

package org.jfree.report.modules.output.table.base;

import org.jfree.report.ReportDefinition;
import org.jfree.report.ReportProcessingException;
import org.jfree.report.modules.output.meta.MetaBand;
import org.jfree.report.modules.output.meta.MetaElement;
import org.jfree.util.Log;

/**
 * Collects the generated MetaElements and produces the layout. The layout is stored in an
 * ObjectTable and will be converted into the output data when commit() is called.
 */
public abstract class TableContentCreator extends AbstractTableCreator
{
  /**
   * The SheetName-function property, defines the name of an StringFunction that creates
   * the sheet names.
   */
  public static final String SHEET_NAME_FUNCTION_PROPERTY =
          "org.jfree.report.targets.table.TableWriter.SheetNameFunction";

  public static final String DEBUG_REPORT_LAYOUT =
          "org.jfree.report.targets.table.TableWriter.DebugReportLayout";

  private GenericObjectTable backend;
  private SheetLayoutCollection sheetLayoutCollection;
  private SheetLayout currentLayout;
  private int tableCounter;
  private TableRectangle lookupRectangle;
  private String sheetNameFunction;
  private int layoutOffset;
  private boolean debugReportLayout;

  public TableContentCreator (final SheetLayoutCollection sheetLayoutCollection)
  {
    if (sheetLayoutCollection == null)
    {
      throw new NullPointerException();
    }
    this.sheetLayoutCollection = sheetLayoutCollection;
    this.backend = new GenericObjectTable();
    this.lookupRectangle = new TableRectangle();
  }

  /**
   * Starts the report processing. This method is called only once per report processing.
   * The TableCreator might use the report definition to configure itself and to perform
   * startup operations.
   *
   * @param report the report definition.
   */
  public final void open (final ReportDefinition report)
          throws ReportProcessingException
  {
    tableCounter = -1;
    sheetNameFunction = report.getReportConfiguration()
            .getConfigProperty(SHEET_NAME_FUNCTION_PROPERTY);
    debugReportLayout = report.getReportConfiguration()
            .getConfigProperty(DEBUG_REPORT_LAYOUT, "true").equals("true");
    handleOpen(report);
  }

  protected abstract void handleOpen (ReportDefinition reportDefinition)
          throws ReportProcessingException;

  /**
   * Begins a table. A table is considered a closed entity, it usually represents a sheet
   * or a single page. Table headers and table properties can be defined using the given
   * report definition.
   *
   * @param report the report definiton.
   */
  public final void beginTable (final ReportDefinition report)
          throws ReportProcessingException
  {
    setEmpty(true);
    backend.clear();
    tableCounter += 1;
    layoutOffset = 0;
    currentLayout = sheetLayoutCollection.getLayoutForPage(tableCounter);
    handleBeginTable(report);
  }

  protected abstract void handleBeginTable (ReportDefinition reportDefinition)
          throws ReportProcessingException;

  /**
   * Finishes the current table.
   */
  public final void endTable ()
          throws ReportProcessingException
  {
    handleEndTable();
    backend.clear();
    setEmpty(true);
    currentLayout = null;
  }

  /**
   * Commits all bands. See the class description for details on the flushing process.
   *
   * @return true, if the content was flushed, false otherwise.
   */
  public final boolean flush ()
          throws ReportProcessingException
  {
    if (debugReportLayout)
    {
      Log.debug("Begin Flush! " + layoutOffset);
    }
    if (handleFlush())
    {
      layoutOffset += backend.getRowCount();
      backend.clear();
      return true;
    }
    return false;
  }

  protected boolean handleFlush ()
          throws ReportProcessingException
  {
    return false;
  }

  protected int getRowCount ()
  {
    return backend.getRowCount();
  }

  protected abstract void handleEndTable ()
          throws ReportProcessingException;

  /**
   * Add the specified band definition to the table sheet. By default, Band definitions
   * are not used to create content, but they might be important for the layout. it is up
   * to the implementor to decide whether to use the supplied content of the band (if
   * any).
   * <p/>
   * This implementation does nothing and always returns false, as bands do not create any
   * content.
   *
   * @param e the element.
   * @return true, if the band is fully processed and the children should be ignored,
   *         false to indicate that we need the children to complete the process.
   *
   * @throws NullPointerException if the element has no valid layout (no BOUNDS defined).
   *                              Bounds are usually defined by the BandLayoutManager.
   */
  protected boolean processBandDefinition (final MetaBand e)
  {
    return false;
  }

  /**
   * Add the specified element to the logical page. Create content from the values
   * contained in the element and format the content by using the element's attributes.
   *
   * @param e the element.
   * @throws NullPointerException if the element has no valid layout (no BOUNDS defined).
   *                              Bounds are usually defined by the BandLayoutManager.
   */
  protected void processElement (final MetaElement e)
  {
    if (currentLayout == null)
    {
      throw new IllegalStateException("No current layout");
    }
    final TableRectangle rect = currentLayout.getTableBounds(e, getLookupRectangle());

    if (e instanceof TableCellBackground)
    {
      // make sure, that even if a band does not contain content
      // the background is properly exported..
      final int x2 = rect.getX2();
      final int y2 = rect.getY2() - layoutOffset;
      if (y2 > 0 && x2 > 0)
      {
        backend.ensureCapacity(y2, x2);
        backend.setObject(y2 - 1, x2 - 1, null);
      }
      return;
    }

    if (isCellSpaceOccupied(rect) == false)
    {
      final int x2 = rect.getX2();
      final int y2 = rect.getY2() - layoutOffset;
      backend.ensureCapacity(y2, x2);

      for (int r = rect.getY1() - layoutOffset; r < y2; r++)
      {
        for (int c = rect.getX1(); c < x2; c++)
        {
          backend.setObject(r, c, e);
        }
      }
      setEmpty(false);
    }
    else
    {
      if (debugReportLayout)
      {
        Log.debug("Offending Content: " + e);
      }
    }
  }

  private boolean isCellSpaceOccupied (final TableRectangle rect)
  {
    final int x2 = rect.getX2();
    final int y2 = rect.getY2() - layoutOffset;

    for (int r = rect.getY1() - layoutOffset; r < y2; r++)
    {
      for (int c = rect.getX1(); c < x2; c++)
      {
        final MetaElement object = (MetaElement) backend.getObject(r, c);
        if (object != null)
        {
          if (debugReportLayout)
          {
            Log.debug("Cell (" + c + ", " + r + ") already filled: " +
                    "Content in cell: " + object);
          }
          return true;
        }
      }
    }
    return false;
  }

  protected boolean isDebugReportLayout ()
  {
    return debugReportLayout;
  }

  protected final TableRectangle getLookupRectangle ()
  {
    return lookupRectangle;
  }

  /**
   * Closes the report processing.
   */
  public final void close ()
          throws ReportProcessingException
  {
    handleClose();
    tableCounter = -1;
  }

  protected int getCurrentTableNumber ()
  {
    return tableCounter;
  }

  protected abstract void handleClose ()
          throws ReportProcessingException;

  protected GenericObjectTable getBackend ()
  {
    return backend;
  }

  protected SheetLayout getCurrentLayout ()
  {
    return currentLayout;
  }

  /**
   * Gets the name of the SheetName function. The sheetname function defines the names of
   * the generated sheets.
   *
   * @return the name of the sheet name function, or null, if that name is not known yet.
   */
  protected String getSheetNameFunction ()
  {
    return sheetNameFunction;
  }

  public int getLayoutOffset ()
  {
    return layoutOffset;
  }
}
