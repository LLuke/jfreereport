/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
 *
 * (C) Copyright 2000-2002, by Simba Management Limited and Contributors.
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
 * --------------------
 * ExcelProducer.java
 * --------------------
 * (C)opyright 2002, by Hawesko GmbH & Co KG
 *
 * Original Author:  Heiko Evermann (for Hawesko GmbH & Co KG)
 * Contributor(s):   -;
 * The Excel layout uses ideas and code from JRXlsExporter.java of JasperReports
 *
 * $Id: ExcelProducer.java,v 1.9 2003/02/18 19:37:35 taqua Exp $
 *
 * Changes
 * -------
 * 03-Jan-2002 : initial version
 */

package com.jrefinery.report.targets.table.excel;

import com.jrefinery.report.targets.table.TableCellBackground;
import com.jrefinery.report.targets.table.TableCellDataFactory;
import com.jrefinery.report.targets.table.TableGridLayout;
import com.jrefinery.report.targets.table.TableGridPosition;
import com.jrefinery.report.targets.table.TableProducer;
import com.jrefinery.report.util.Log;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.Region;

import java.io.IOException;
import java.io.OutputStream;

/**
 * An output target for the report engine that generates an Excel file using the hffs class library
 * from the Apache Jakarta Project
 * (see <code>http://jakarta.apache.org/poi/index.html</code>.
 * <p>
 * At the moment only texts are exported.
 * <p>
 * <p>
 *
 * @author Heiko Evermann
 */
public class ExcelProducer extends TableProducer
{
  /** The configuration prefix. */
  public static final String CONFIGURATION_PREFIX =
      "com.jrefinery.report.targets.excel.default.";

  /** The output stream. */
  private OutputStream out;

  /** The Excel cell style factory */
  private ExcelCellDataFactory cellDataFactory;

  /** factor for transformation of internal scale to excel scale */
  private static final int XFACTOR = 43;

  /** factor for transformation of internal scale to excel scale */
  private static final int YFACTOR = 30;

  /** the excel workbook represents the excel document. */
  private HSSFWorkbook workbook;
  /** the current excel sheet */
  private HSSFSheet sheet;

  /**
   * Creates a new ExcelOutputTarget.
   *
   * @param out  the output stream.
   */
  public ExcelProducer(OutputStream out, boolean strict)
  {
    super(strict);
    this.out = out;
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
    workbook = new HSSFWorkbook();
    ExcelCellStyleFactory cellStyleFactory = new ExcelCellStyleFactory(workbook);
    cellDataFactory = new ExcelCellDataFactory(cellStyleFactory);

    // style for empty cells

    // Clear list of cells
    clearCells();
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
  public void beginPage (String name)
  {
    if(name == null)
    {
      sheet = workbook.createSheet();
    }
    else
    {
      sheet = workbook.createSheet(name);
    }
  }

  /**
   * Handles the end of a page, lays out the collected cells
   * and write the excel sheet.
   */
  public void endPage ()
  {
    if (isDummy() == false)
    {
      writeSheet(layoutGrid());
    }
    clearCells();
    sheet = null;
  }

  /**
   * Tests, whether the page is open and a valid sheet has been
   * created.
   *
   * @return true, if the page is open, false otherwise.
   */
  private boolean isPageOpen ()
  {
    return sheet != null;
  }

  /**
   * Closes the document and write the generated document.
   */
  public void close()
  {
    // now we have all cell data that we need. Let's generate the file
    if (isPageOpen())
      throw new IllegalStateException();

    try
    {
      workbook.write(out);
    }
    catch (IOException e)
    {
      Log.warn("could not write xls data. Message:", e);
    }
  }

  /**
   * Generate the XLS data structure.
   *
   * @param layout the layouted sheet.
   */
  private void writeSheet(TableGridLayout layout)
  {
    for (int i = 0; i < layout.getWidth(); i++)
    {
      float width = (layout.getColumnEnd(i) - layout.getColumnStart(i));
      sheet.setColumnWidth((short) (i), (short) (width * XFACTOR));
    }

    for (int y = 0; y < layout.getHeight(); y++)
    {
      HSSFRow row = sheet.createRow((short) y);

      float lastRowHeight = (layout.getRowEnd(y) - layout.getRowStart(y));
      row.setHeight((short) (lastRowHeight * YFACTOR));

      for (int x = 0; x < layout.getWidth(); x++)
      {
        TableGridLayout.Element gridPosition = layout.getData(x,y);
        if (gridPosition == null)
        {
          continue;
        }

        // background stuff ...

        TableGridPosition root = gridPosition.getRoot();
        TableCellBackground bg = createTableCellStyle(gridPosition.getBackground());
        if (root == null)
        {
          // just apply the background, if any ...
          if (bg != null)
          {
            HSSFCell cell = row.createCell((short) x);
            HSSFCellStyle style = cellDataFactory.getStyleFactory().createCellStyle(null, bg);
            cell.setCellStyle(style);
          }
          continue;
        }

        if (root.isOrigin(x,y))
        {
          exportCell(row, root, bg, (short) x, y);
        }
      }
    }
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
  private void exportCell(HSSFRow row,
                          TableGridPosition content,
                          TableCellBackground bg, short x, int y)
  {
    if (content.getColSpan() > 1 || content.getRowSpan() > 1)
    {
      sheet.addMergedRegion(new Region(y, x,
                                       (y + content.getRowSpan() - 1),
                                       (short)(x + content.getColSpan() - 1)));
    }

    ExcelCellData contentCell = (ExcelCellData) content.getElement();

    HSSFCell cell = row.createCell(x);
    HSSFCellStyle style = cellDataFactory.getStyleFactory().createCellStyle(contentCell.getExcelCellStyle(), bg);
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
    return workbook != null;
  }

}
