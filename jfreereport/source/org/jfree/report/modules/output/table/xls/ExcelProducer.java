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
 * ------------------
 * ExcelProducer.java
 * ------------------
 * (C)opyright 2003, by Hawesko GmbH & Co KG
 *
 * Original Author:  Heiko Evermann (for Hawesko GmbH & Co KG)
 * Contributor(s):   -;
 * The Excel layout uses ideas and code from JRXlsExporter.java of JasperReports
 *
 * $Id: ExcelProducer.java,v 1.6 2003/08/19 21:01:34 taqua Exp $
 *
 * Changes
 * -------
 * 03-Jan-2002 : initial version
 */

package org.jfree.report.modules.output.table.xls;

import java.io.IOException;
import java.io.OutputStream;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFPrintSetup;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.Region;
import org.jfree.report.modules.output.table.base.TableCellBackground;
import org.jfree.report.modules.output.table.base.TableCellDataFactory;
import org.jfree.report.modules.output.table.base.TableGridLayout;
import org.jfree.report.modules.output.table.base.TableGridPosition;
import org.jfree.report.modules.output.table.base.TableLayoutInfo;
import org.jfree.report.modules.output.table.base.TableProducer;
import org.jfree.report.util.Log;

/**
 * An output target for the report engine that generates an Excel file using the
 * hffs class library from the Apache Jakarta Project
 * (see <code>http://jakarta.apache.org/poi/index.html</code>.
 * <p>
 * At the moment only texts are exported.
 *
 * @author Heiko Evermann
 */
public class ExcelProducer extends TableProducer
{
  /** The output stream. */
  private OutputStream out;

  /** The Excel cell style factory. */
  private ExcelCellDataFactory cellDataFactory;

  /** factor for transformation of internal scale to excel scale. */
  private static final int XFACTOR = 55;

  /** factor for transformation of internal scale to excel scale. */
  private static final int YFACTOR = 40;

  /** the excel workbook represents the excel document. */
  private HSSFWorkbook workbook;

  /** the current excel sheet. */
  private HSSFSheet sheet;

  /** The cell style producer is used to create cell content. */
  private HSSFCellStyleProducer cellStyleProducer;

  /** A flag to keep track of the open state. */
  private boolean open;
  
  /** the row count used to indicate the progress. */
  private int layoutRowCount;

  /**
   * Creates a new Excel producer that will use an predefined layout to perform
   * the output.
   *
   * @param out  the output stream.
   * @param layout the tablelayout that contains the layout information.
   */
  public ExcelProducer(final TableLayoutInfo layout, final OutputStream out)
  {
    super(layout);
    if (out == null)
    {
      throw new NullPointerException();
    }
    this.out = out;
    cellDataFactory = null;
  }

  /**
   * Creates a new Excel producer to compute the layout.
   *
   * @param strict true, if a stricter layout should be used, false otherwise.
   * @param info the tablelayout that will contain the grid boundries.
   * @see org.jfree.report.modules.output.table.base.TableGridBounds#isStrict
   */
  public ExcelProducer(TableLayoutInfo info, final boolean strict)
  {
    super(info, strict);
    cellDataFactory = null;
  }

  /**
   * Gets the TableProducer implementation of this TableProducer.
   *
   * @return the ExcelCellDataFactory of this producer.
   */
  public TableCellDataFactory getCellDataFactory()
  {
    return cellDataFactory;
  }

  /**
   * Opens the document; creates a new Workbook and initializes the
   * excel file creation process.
   */
  public void open()
  {
    final ExcelCellStyleFactory cellStyleFactory = new ExcelCellStyleFactory();
    cellDataFactory = new ExcelCellDataFactory(cellStyleFactory);
    cellDataFactory.setDefineDataFormats(isMapData());

    if (isDummy() == false)
    {
      workbook = new HSSFWorkbook();
      this.cellStyleProducer = new HSSFCellStyleProducer(workbook);
    }

    open = true;
  }

  /**
   * Handles the start of a new page. The page name is given as parameter.
   * The TableWriter starts a new page whenever a manual pagebreak is found
   * in the report definition. The ReportProducer has been opened before.
   * <p>
   * If the name is null, the default excel names are used as sheet names.
   *
   * @param name the page name
   */
  public void beginPage(final String name)
  {
    if (isDummy() == false)
    {
      layoutRowCount = 0;
      if (name == null)
      {
        sheet = workbook.createSheet();
      }
      else
      {
        sheet = workbook.createSheet(name);
      }
    }
    super.beginPage(name);
  }

  /**
   * Handles the end of a page, lays out the collected cells
   * and write the excel sheet.
   */
  public void commit()
  {
    if (isDummy() == false)
    {
      writeSheet(layoutGrid());
      clearCells();
    }
  }

  /**
   * Handles the end of a page. This calls commit() and clears the layout.
   */
  public void endPage()
  {
    super.endPage();
    sheet = null;
  }

  /**
   * Tests, whether the page is open and a valid sheet has been
   * created.
   *
   * @return true, if the page is open, false otherwise.
   */
  private boolean isPageOpen()
  {
    return sheet != null;
  }

  /**
   * Closes the document and write the generated document.
   */
  public void close()
  {
    if (isDummy() == false)
    {
      // now we have all cell data that we need. Let's generate the file
      if (isPageOpen())
      {
        throw new IllegalStateException("Page seems to be open.");
      }
      try
      {
        workbook.write(out);
      }
      catch (IOException e)
      {
        Log.warn("could not write xls data. Message:", e);
      }
    }
    open = false;
  }


  /**
   * Generate the XLS data structure.
   *
   * @param layout the layouted sheet.
   */
  private void writeSheet(final TableGridLayout layout)
  {
    for (int i = 0; i < layout.getWidth(); i++)
    {
      final float width = (layout.getColumnEnd(i) - layout.getColumnStart(i));
      sheet.setColumnWidth((short) (i), (short) (width * XFACTOR));
    }

    HSSFPrintSetup printSetup = sheet.getPrintSetup();
    ExcelPrintSetupFactory.performPageSetup
      (printSetup, getGridBoundsCollection().getPageFormat(), getProperty("Paper"));
        
    int startY = layoutRowCount;

    for (int y = 0; y < layout.getHeight(); y++)
    {
      final HSSFRow row = sheet.createRow((short) y + startY);

      final float lastRowHeight = (layout.getRowEnd(y) - layout.getRowStart(y));
      row.setHeight((short) (lastRowHeight * YFACTOR));

      for (int x = 0; x < layout.getWidth(); x++)
      {
        final TableGridLayout.Element gridPosition = layout.getData(x, y);
        if (gridPosition == null)
        {
          continue;
        }

        // background stuff ...

        final TableGridPosition root = gridPosition.getRoot();
        final TableCellBackground bg = createTableCellStyle(gridPosition.getBackground());
        if (root == null)
        {
          // just apply the background, if any ...
          if (bg != null)
          {
            final HSSFCell cell = row.createCell((short) x);
            final HSSFCellStyle style = cellStyleProducer.createCellStyle(null, bg);
            cell.setCellStyle(style);
          }
          continue;
        }

        if (root.isOrigin(x, y))
        {
          exportCell(row, root, bg, (short) x, y + startY);
        }
      }
    }

    layoutRowCount += layout.getHeight();
  }

  /**
   * Exports the cell. The cell is generated and the stored cell style
   * applied.
   *
   * @param row the HSSFRow, where the generated cell gets added.
   * @param x the column
   * @param y the row number
   * @param content the grid position
   * @param bg the background style.
   */
  private void exportCell(final HSSFRow row,
                          final TableGridPosition content,
                          final TableCellBackground bg, final short x, final int y)
  {
    if (content.getColSpan() > 1 || content.getRowSpan() > 1)
    {
      sheet.addMergedRegion(new Region(y, x,
          (y + content.getRowSpan() - 1),
          (short) (x + content.getColSpan() - 1)));
    }

    final ExcelCellData contentCell = (ExcelCellData) content.getElement();

    final HSSFCell cell = row.createCell(x);
    final HSSFCellStyle style = cellStyleProducer.createCellStyle
        (contentCell.getExcelCellStyle(), bg);
    cell.setCellStyle(style);

    if (contentCell.isEmpty() == false)
    {
      cell.setEncoding(HSSFCell.ENCODING_UTF_16);
      contentCell.applyContent(cell);
    }
  }

  /**
   * Returns true if the output target is open, and false otherwise.
   *
   * @return true or false.
   */
  public boolean isOpen()
  {
    return open;
  }

  /**
   * Checks, whether to map numeric and date content to excel data cells.
   * 
   * @return true, if enhanced mapping is enabled, false otherwise.
   */
  private boolean isMapData ()
  {
    final String mapData = getProperty
        (ExcelProcessor.ENHANCED_DATA_FORMAT_PROPERTY, "true");
    return (mapData.equalsIgnoreCase("true"));
  }
}
