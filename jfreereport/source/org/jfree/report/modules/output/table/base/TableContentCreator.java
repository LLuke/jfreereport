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
 * $Id: TableContentCreator.java,v 1.1 2004/03/16 15:43:41 taqua Exp $
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

/**
 * Collects the generated MetaElements and produces the layout.
 * The layout is stored in an ObjectTable and will be converted into
 * the output data when commit() is called.
 */
public abstract class TableContentCreator extends AbstractTableCreator
{
  /**
   * The SheetName-function property, defines the name of an StringFunction
   * that creates the sheet names.
   */
  public static final String SHEET_NAME_FUNCTION_PROPERTY =
      "org.jfree.report.targets.table.TableWriter.SheetNameFunction";

  private GenericObjectTable backend;
  private SheetLayoutCollection sheetLayoutCollection;
  private SheetLayout currentLayout;
  private int tableCounter;
  private TableRectangle lookupRectangle;
  private String sheetNameFunction;

  public TableContentCreator(final SheetLayoutCollection sheetLayoutCollection)
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
   * Starts the report processing. This method is called only once
   * per report processing. The TableCreator might use the report
   * definition to configure itself and to perform startup operations.
   *
   * @param report the report definition.
   */
  public final void open(final ReportDefinition report)
    throws ReportProcessingException
  {
    tableCounter = -1;
    sheetNameFunction = report.getReportConfiguration()
        .getConfigProperty(SHEET_NAME_FUNCTION_PROPERTY);
    handleOpen(report);
  }

  protected abstract void handleOpen (ReportDefinition reportDefinition)
           throws ReportProcessingException;

  /**
   * Begins a table. A table is considered a closed entity, it usually
   * represents a sheet or a single page. Table headers and table properties
   * can be defined using the given report definition.
   *
   * @param report the report definiton.
   */
  public final void beginTable(final ReportDefinition report)
    throws ReportProcessingException
  {
    backend.clear();
    tableCounter += 1;
    currentLayout = sheetLayoutCollection.getLayoutForPage(tableCounter);
    handleBeginTable(report);
  }

  protected abstract void handleBeginTable (ReportDefinition reportDefinition)
           throws ReportProcessingException;

  /**
   * Finishes the current table.
   */
  public final void endTable() throws ReportProcessingException
  {
    handleEndTable();
    backend.clear();
    currentLayout = null;
  }

  protected abstract void handleEndTable () throws ReportProcessingException;

  /**
   * Add the specified band definition to the table sheet. By default, Band definitions
   * are not used to create content, but they might be important for the layout. it is
   * up to the implementor to decide whether to use the supplied content of the band
   * (if any).
   * <p>
   * This implementation does nothing and always returns false, as bands do not create
   * any content.
   *
   * @param e  the element.
   * @throws NullPointerException if the element has no valid layout (no BOUNDS defined).
   * Bounds are usually defined by the BandLayoutManager.
   * @return true, if the band is fully processed and the children should be ignored,
   * false to indicate that we need the children to complete the process.
   */
  protected boolean processBandDefinition(final MetaBand e)
  {
    return false;
  }

  /**
   * Add the specified element to the logical page. Create content from the values
   * contained in the element and format the content by using the element's attributes.
   *
   * @param e  the element.
   * @throws NullPointerException if the element has no valid layout (no BOUNDS defined).
   * Bounds are usually defined by the BandLayoutManager.
   */
  protected void processElement(final MetaElement e)
  {
    if (e instanceof TableCellBackground)
    {
      // we don't handle backgrounds, this was already done by the sheetlayout
      // during the first pass
      return;
    }
    final TableRectangle rect = currentLayout.getTableBounds(e, getLookupRectangle());
    final int x2 = rect.getX2();
    final int y2 = rect.getY2();

    backend.ensureCapacity(x2, y2);

    for (int r = rect.getY1(); r < y2; r++)
    {
      for (int c = rect.getX1(); c < x2; c++)
      {
        backend.setObject(r, c, e);
      }
    }
  }

  protected final TableRectangle getLookupRectangle ()
  {
    return lookupRectangle;
  }

  /**
   * Closes the report processing.
   */
  public final void close() throws ReportProcessingException
  {
    handleClose();
    tableCounter = -1;
  }

  protected int getCurrentTableNumber ()
  {
    return tableCounter;
  }

  protected abstract void handleClose () throws ReportProcessingException;

  protected GenericObjectTable getBackend()
  {
    return backend;
  }

  protected SheetLayout getCurrentLayout()
  {
    return currentLayout;
  }

  /**
   * Gets the name of the SheetName function. The sheetname function defines the
   * names of the generated sheets.
   *
   * @return the name of the sheet name function, or null, if that name is not known yet.
   */
  protected String getSheetNameFunction()
  {
    return sheetNameFunction;
  }


}
