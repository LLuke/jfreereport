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
 * ExcelOutputTarget.java
 * --------------------
 * (C)opyright 2002, by Hawesko GmbH & Co KG
 *
 * Original Author:  Heiko Evermann (for Hawesko GmbH & Co KG)
 * Contributor(s):   -;
 * The Excel layout uses ideas and code from JRXlsExporter.java of JasperReports
 *
 * $Id: ExcelProducer.java,v 1.1 2003/01/18 20:47:36 taqua Exp $
 *
 * Changes
 * -------
 * 03-Jan-2002 : initial version
 */

package com.jrefinery.report.targets.table.excel;

import com.jrefinery.report.targets.table.TableCellDataFactory;
import com.jrefinery.report.targets.table.TableGridLayout;
import com.jrefinery.report.targets.table.TableGridPosition;
import com.jrefinery.report.targets.table.TableProducer;
import com.jrefinery.report.util.Log;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.util.Region;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

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

  private HSSFWorkbook workbook;
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

  public TableCellDataFactory getCellDataFactory()
  {
    return cellDataFactory;
  }

  /**
   * Opens the document.
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

  public void endPage ()
  {
    writeSheet(layoutGrid());
    clearCells();
    sheet = null;
  }

  private boolean isPageOpen ()
  {
    return sheet != null;
  }

  /**
   * Closes the document.
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
   * generate the XLS data structure
   *
   * The empty cell stuff was never referenced and did nothing usefull, removed it.
   */
  private void writeSheet(TableGridLayout layout)
  {
    for (int i = 0; i < layout.getWidth(); i++)
    {
      double width = (layout.getColumnEnd(i) - layout.getColumnStart(i));
      sheet.setColumnWidth((short) (i), (short) (width * XFACTOR));
    }

    for (int y = 0; y < layout.getHeight(); y++)
    {
      HSSFRow row = sheet.createRow((short) y);

      double lastRowHeight = (layout.getRowEnd(y) - layout.getRowStart(y));
      row.setHeight((short) (lastRowHeight * YFACTOR));

      for (int x = 0; x < layout.getWidth(); x++)
      {
        HSSFCell cell = null;
        TableGridLayout.Element gridPosition = layout.getData(x,y);
        if (gridPosition != null)
        {
          // background stuff ...

          TableGridPosition root = gridPosition.getRoot();
          if (root != null)
          {
            if (root.isOrigin(x,y))
            {
              cell = exportCell(row, gridPosition, (short) x, y);
            }
          }
        }
// todo style implementieren ...
      }
    }
  }

  /**
   *
   * @param row
   * @param gridCell
   * @param x
   * @param y
   */
  private HSSFCell exportCell(HSSFRow row, TableGridLayout.Element gridCell, short x, int y)
  {
    TableGridPosition content = gridCell.getRoot();

    ExcelBackgroundCellStyle background = null;
    List backgroundCells = gridCell.getBackground();
    for (int i = 0; i < backgroundCells.size(); i++)
    {
      TableGridPosition pos = (TableGridPosition) backgroundCells.get(i);
      if (pos.getElement() instanceof BackgroundExcelCellData)
      {
        BackgroundExcelCellData bgData = (BackgroundExcelCellData) pos.getElement();
        if (pos.contains(content))
        {
          ExcelBackgroundCellStyle bgStyle = bgData.getStyle();
          bgStyle.setParent(background);
          background = bgStyle;
        }
      }
    }

    if (content.getColSpan() > 1 || content.getRowSpan() > 1)
    {
      sheet.addMergedRegion(new Region(y, x,
                                       (y + content.getRowSpan() - 1),
                                       (short)(x + content.getColSpan() - 1)));
    }

    HSSFCell cell = row.createCell(x);
    ExcelContentCellData contentCell = (ExcelContentCellData) content.getElement();
    contentCell.getExcelCellStyle().setBackgroundStyleDefinition(background);

    HSSFCellStyle style = cellDataFactory.getStyleFactory().createCellStyle(contentCell.getExcelCellStyle());
    cell.setCellStyle(style);

    if (contentCell.isEmpty() == false)
    {
      cell.setEncoding(HSSFCell.ENCODING_UTF_16);
      contentCell.applyContent(cell);
    }
    return cell;
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
