/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
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
 * ----------------
 * RTFProducer.java
 * ----------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: RTFProducer.java,v 1.8 2003/10/11 21:33:08 taqua Exp $
 *
 * Changes
 * -------
 * 01-Feb-2003 : Initial version
 */
package org.jfree.report.modules.output.table.rtf;

import java.awt.Color;
import java.awt.geom.Rectangle2D;
import java.io.OutputStream;
import java.util.List;

import com.lowagie.text.Cell;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Table;
import com.lowagie.text.rtf.RtfWriter;
import org.jfree.report.function.FunctionProcessingException;
import org.jfree.report.modules.output.table.base.TableCellBackground;
import org.jfree.report.modules.output.table.base.TableCellDataFactory;
import org.jfree.report.modules.output.table.base.TableGridLayout;
import org.jfree.report.modules.output.table.base.TableGridPosition;
import org.jfree.report.modules.output.table.base.TableProducer;
import org.jfree.report.util.NoCloseOutputStream;

/**
 * The TableProducer is responsible for creating the produced Table. After
 * the writer has finished the band layout process, the layouted bands are
 * forwarded into the TableProducer. The TableProducer coordinates the cell
 * creation process and collects the generated TableCellData. The raw CellData
 * objects are later transformed into a TableGridLayout.
 *
 * @author Thomas Morgner
 */
public class RTFProducer extends TableProducer
{
  /** the output stream used to write the content. */
  private OutputStream outputStream;

  /** the iText document used for writing the content. */
  private Document document;

  /** the cell factory. */
  private RTFCellDataFactory cellDataFactory;

  /** A flag that stores the open-state of this producer. */
  private boolean open;

  /**
   * Creates a new RTFProducer to create the output based on the given
   * layout information.
   *
   * @param gridBoundsCollection the precomputed layout from the pagination run.
   * @param outputStream the output stream that should receive the content.
   */
  public RTFProducer(final RTFLayoutInfo gridBoundsCollection, final OutputStream outputStream)
  {
    super(gridBoundsCollection);
    if (outputStream == null)
    {
      throw new NullPointerException();
    }
    this.outputStream = outputStream;
    cellDataFactory = new RTFCellDataFactory(gridBoundsCollection.getBaseFontSupport());
  }

  /**
   * Creates a new RTFProducer.
   *
   * @param strictLayout the stric layout flag.
   * @param gridBoundsCollection the grid layout inforrmation that is used to
   * store the cell grid position.
   * @throws NullPointerException if the outputstream is null.
   */
  public RTFProducer(final RTFLayoutInfo gridBoundsCollection, final boolean strictLayout)
  {
    super(gridBoundsCollection, strictLayout);
    cellDataFactory = new RTFCellDataFactory(gridBoundsCollection.getBaseFontSupport());
  }

  /**
   * Starts the report writing and opens the RTF document for writing.
   */
  public void open()
  {
    if (isDummy() == false)
    {
      // rtf does not support PageFormats or other meta data...
      document = new Document();
      RtfWriter.getInstance(document, new NoCloseOutputStream(outputStream));
      document.open();
    }
    open = true;
  }

  /**
   * Closes the report and finishs the report writing. Closes the RTF document.
   */
  public void close()
  {
    if (isDummy() == false)
    {
      document.close();
    }
    open = false;
  }

  /**
   * Handles the end of a page and performs the table layout for the current
   * table.
   */
  public void endPage()
  {
    if (isDummy() == false)
    {
      try
      {
        generatePage(layoutGrid());
      }
      catch (DocumentException de)
      {
        throw new FunctionProcessingException("Failed to generate page", de);
      }
      clearCells();
    }
  }

  /**
   * Write the collected data. This method is called when ever it is safe to
   * commit all previous content. An auto-commit is also performed after the page
   * has ended.
   * <p>
   * Implementations have to take care, that empty commits do not produce any
   * output. Successfully written content must be removed.
   */
  public void commit()
  {
    // does nothing, we write the table at the end of the page ...
  }

  /**
   * Defines the cell background style for the given cell. The cell background
   * is defined in the background list.
   *
   * @param cell the cell that should be defined.
   * @param cellbounds the bounds of the cell for which we create the background
   * @param background the background definition for the cell.
   */
  private void setCellBackgroundStyle(final Cell cell,
                                      final List background,
                                      final Rectangle2D cellbounds)
  {
    final TableCellBackground bg = createTableCellStyle(background, cellbounds);
    if (bg == null)
    {
      return;
    }
    final Color color = bg.getColor();
    if (color != null)
    {
      cell.setBackgroundColor(color);
    }

    // Cell Borders are only added if all borders are defined and equal,
    // as iText-RTF does not support different borders ...
    final Color bT = bg.getColorTop();
    final Color bB = bg.getColorBottom();
    final Color bL = bg.getColorLeft();
    final Color bR = bg.getColorRight();
    if (bT == null || bB == null || bL == null || bR == null)
    {
      return;
    }
    if (bT.equals(bB) && bT.equals(bL) && bT.equals(bR)
        && bg.getBorderSizeBottom() == bg.getBorderSizeTop()
        && bg.getBorderSizeBottom() == bg.getBorderSizeLeft()
        && bg.getBorderSizeBottom() == bg.getBorderSizeRight())
    {
      cell.setBorderColor(bT);
      cell.setBorderWidth(bg.getBorderSizeTop());
    }
  }

  /**
   * Generates the cell.
   *
   * @param layout the layouted table content.
   * @throws DocumentException if an error occured when generating the document.
   */
  private void generatePage(final TableGridLayout layout)
      throws DocumentException
  {
    if (layout.getWidth() == 0 || layout.getHeight() == 0)
    {
      // the table is empty
      return;
    }

    final Table table = new Table(layout.getWidth(), layout.getHeight());
    table.setAutoFillEmptyCells(false);
    Rectangle2D cellBounds = new Rectangle2D.Float();

    for (int y = 0; y < layout.getHeight(); y++)
    {
      // there is no need to create rows, this is handled automaticly...
      boolean printed = false;
      for (int x = 0; x < layout.getWidth(); x++)
      {
        final TableGridLayout.Element gridElement = layout.getData(x, y);
        if (gridElement == null)
        {
          // cell is empty, autofill will create an empty cell here ...
          final Cell cell = new Cell();
          cell.setBorderWidth(0);
          table.addCell(cell, y, x);
          printed = true;
          continue;
        }

        final TableGridPosition gridPosition = gridElement.getRoot();
        if (gridPosition == null || gridPosition.isInvalidCell())
        {
          final Cell cell = new Cell();
          cell.setBorderWidth(0);
          // iText cell width is a string, why?

          cellBounds = createCellBounds(layout, x, y, cellBounds);

          setCellBackgroundStyle(cell, gridElement.getBackground(), cellBounds);
          table.addCell(cell, y, x);
          printed = true;
          continue;
        }

        if (gridPosition.isOrigin(x, y) == false)
        {
          // this is a spanned field, no need to do anything
          continue;
        }

        final RTFCellData cellData = (RTFCellData) gridPosition.getElement();
        final Cell cell = cellData.getCell();
        cell.setBorderWidth(0);
        cellBounds.setRect(layout.getColumnStart(x), layout.getColumnEnd(x),
            layout.getRowStart(y), layout.getRowEnd(y));
        setCellBackgroundStyle(cell, gridElement.getBackground(), cellBounds);

        if (gridPosition.getRowSpan() > 1)
        {
          cell.setRowspan(gridPosition.getRowSpan());
        }
        if (gridPosition.getColSpan() > 1)
        {
          cell.setColspan(gridPosition.getColSpan());
        }

        table.addCell(cell, y, x);
        x += gridPosition.getColSpan() - 1;
        printed = true;
      }

      if (!printed)
      {
//        Log.debug ("The Row at " + y + " was not printed");
      }
    }

    document.add(table);
  }

  /**
   * Gets the TableProducer implementation of this TableProducer.
   *
   * @return the TableProducers TableCellDataFactory, which is used to create
   * the TableCellData.
   */
  public TableCellDataFactory getCellDataFactory()
  {
    return cellDataFactory;
  }

  /**
   * Returns true, if the TableProducer is open. Only open producers
   * are able to write TableCells or to create TableCellData from Elements.
   *
   * @return checks, whether the TableProducer is open.
   */
  public boolean isOpen()
  {
    return open;
  }
}
