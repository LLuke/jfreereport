/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
 *
 * (C) Copyright 2000-2004, by Object Refinery Limited and Contributors.
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
 * ExcelContentCreator.java
 * ------------------------------
 * (C)opyright 2004, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Object Refinery Limited);
 *
 * $Id: ExcelContentCreator.java,v 1.3 2005/01/25 00:16:30 taqua Exp $
 *
 * Changes 
 * -------------------------
 * Mar 15, 2004 : Initial version
 *  
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
import org.jfree.report.ReportDefinition;
import org.jfree.report.ReportProcessingException;
import org.jfree.report.modules.output.meta.MetaElement;
import org.jfree.report.modules.output.table.base.GenericObjectTable;
import org.jfree.report.modules.output.table.base.SheetLayout;
import org.jfree.report.modules.output.table.base.SheetLayoutCollection;
import org.jfree.report.modules.output.table.base.TableCellBackground;
import org.jfree.report.modules.output.table.base.TableContentCreator;
import org.jfree.report.modules.output.table.base.TableRectangle;
import org.jfree.report.modules.output.table.xls.metaelements.ExcelMetaElement;
import org.jfree.report.modules.output.table.xls.util.ExcelPrintSetupFactory;
import org.jfree.report.util.Log;

public class ExcelContentCreator extends TableContentCreator
{
  /** factor for transformation of internal scale to excel scale. */
  private static final int XFACTOR = 55;

  /** factor for transformation of internal scale to excel scale. */
  private static final int YFACTOR = 40;

  private HSSFCellStyleProducer cellStyleProducer;
  private OutputStream outputStream;
  private boolean open;
  private HSSFWorkbook workbook;
  private HSSFSheet sheet;

  public ExcelContentCreator (final SheetLayoutCollection sheetLayoutCollection,
                              final OutputStream outputStream)
  {
    super(sheetLayoutCollection);
    this.outputStream = outputStream;
  }

  protected void handleBeginTable (final ReportDefinition reportDefinition)
          throws ReportProcessingException
  {
    String sheetName = null;
    if (getSheetNameFunction() != null)
    {
      sheetName = String.valueOf
              (reportDefinition.getDataRow().get(getSheetNameFunction()));
    }
    if (sheetName == null)
    {
      sheet = workbook.createSheet();
    }
    else
    {
      sheet = workbook.createSheet(sheetName);
    }

    final String paper = reportDefinition.getReportConfiguration().getConfigProperty
            (ExcelProcessor.CONFIGURATION_PREFIX + ".Paper");
    final String paperOrientation = reportDefinition.getReportConfiguration().getConfigProperty
            (ExcelProcessor.CONFIGURATION_PREFIX + ".PaperOrientation");

    final HSSFPrintSetup printSetup = sheet.getPrintSetup();
    ExcelPrintSetupFactory.performPageSetup
        (printSetup, reportDefinition.getPageDefinition(),
            paper, paperOrientation);

  }

  protected void handleClose ()
          throws ReportProcessingException
  {
    open = false;
    try
    {
      workbook.write(outputStream);
    }
    catch (IOException e)
    {
      Log.warn("could not write xls data. Message:", e);
    }
  }

  protected void handleEndTable ()
          throws ReportProcessingException
  {
    sheet = null;
  }

  protected void handleOpen (final ReportDefinition reportDefinition)
          throws ReportProcessingException
  {
    open = true;
    workbook = new HSSFWorkbook();
    cellStyleProducer = new HSSFCellStyleProducer(workbook);

  }

  /**
   * Checks, whether the report processing has started.
   *
   * @return true, if the report is open, false otherwise.
   */
  public boolean isOpen ()
  {
    return open;
  }

  /**
   * Commits all bands. See the class description for details on the flushing process.
   *
   * @return true, if the content was flushed, false otherwise.
   */
  public boolean handleFlush () throws ReportProcessingException
  {
    final GenericObjectTable go = getBackend();
    final SheetLayout layout = getCurrentLayout();

    final int width = Math.max(go.getColumnCount(), layout.getColumnCount());
    for (int i = 0; i < width; i++)
    {
      final int cellWidth = layout.getCellWidth(i , i+1);
//      Log.debug ("CellWidth: [" + i + "] " + cellWidth);
      sheet.setColumnWidth((short) i, (short) (cellWidth * XFACTOR));
    }
    
    final int height = go.getRowCount();
    final int layoutOffset = getLayoutOffset();
    for (int y = layoutOffset; y < height + layoutOffset; y++)
    {
      final HSSFRow row = sheet.createRow((short) y);
      final int lastRowHeight = layout.getRowHeight(y);
      row.setHeight((short) (lastRowHeight * YFACTOR));
//      Log.debug ("RowHeight: [" + y + "] " + lastRowHeight);

      for (int x = 0; x < width; x++)
      {
        final MetaElement element =
                (MetaElement) go.getObject(y - layoutOffset, x);
        final TableCellBackground background = layout.getElementAt(y, x);

        if (element == null)
        {
          // only background stuff ...
          // just apply the background, if any ...
          if (background != null)
          {
            final HSSFCell cell = row.createCell((short) x);
            final HSSFCellStyle style =
                    cellStyleProducer.createCellStyle(null, background);
            cell.setCellStyle(style);
          }
          continue;
        }

        final TableRectangle rectangle =
                layout.getTableBounds(element, getLookupRectangle());
        if (rectangle.isOrigin(x, y) == false)
        {
          // this is a spanned cell - ignore it completly
          continue;
        }

        exportCell(row, element, background, rectangle,
                (short) x, y);
      }
    }
    return true;
  }


  /**
   * Exports the cell. The cell is generated and the stored cell style
   * applied.
   *
   * @param row the HSSFRow, where the generated cell gets added.
   * @param x the column
   * @param y the row number
   * @param element the content
   * @param bg the background style.
   * @param rectangle the rectangle within the global grid
   */
  private void exportCell(final HSSFRow row,
                          final MetaElement element,
                          final TableCellBackground bg,
                          final TableRectangle rectangle,
                          final short x, final int y)
  {
    if (rectangle.getColumnSpan() > 1 || rectangle.getRowSpan() > 1)
    {
      sheet.addMergedRegion(new Region(y, x,
          (y + rectangle.getRowSpan() - 1),
          (short) (x + rectangle.getColumnSpan() - 1)));
    }
    final HSSFCell cell = row.createCell(x);
    final HSSFCellStyle style =
            cellStyleProducer.createCellStyle(element, bg);

    cell.setCellStyle(style);

    if (element instanceof ExcelMetaElement)
    {
      final ExcelMetaElement me = (ExcelMetaElement) element;
      cell.setEncoding(HSSFCell.ENCODING_UTF_16);
      me.applyValue(cell);
    }
  }

}
