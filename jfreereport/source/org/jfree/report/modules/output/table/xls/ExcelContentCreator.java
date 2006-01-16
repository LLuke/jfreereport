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
 * $Id: ExcelContentCreator.java,v 1.17 2005/12/10 17:39:48 taqua Exp $
 *
 * Changes 
 * -------------------------
 * Mar 15, 2004 : Initial version
 *  
 */

package org.jfree.report.modules.output.table.xls;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.awt.Font;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFPrintSetup;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
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
import org.jfree.report.util.geom.StrictGeomUtility;
import org.jfree.util.Log;
import org.jfree.base.config.ModifiableConfiguration;

public class ExcelContentCreator extends TableContentCreator
        implements ExcelExportContext
{
  private HSSFCellStyleProducer cellStyleProducer;
  private OutputStream outputStream;
  private boolean open;
  private HSSFWorkbook workbook;
  private HSSFSheet sheet;
  private boolean newTable;

  /**
   * This is a weird thing: According to
   * http://support.microsoft.com/?kbid=214123
   * this value depends on the Normal-Font used in the cells.
   * The character '0' is used for that.
   * <p>
   * The actual width unit is 1/256th of a character. Now how to
   * compute a character for a proportial font. Which one is used.
   * These funny things make me wanting to hurt the guys who implemented
   * that weird stuff in MS-Excel.
   * <p>
   * With POI 3.0 it seems that some default fonts have already been created,
   * which actually make the workbook widths independent from the used fonts.
   */
  private double SCALE_FACTOR = 2000f/40f;
  private double scaleFactor;

  private HSSFPatriarch patriarch;
  private HashMap sheetNamesCount;

  public ExcelContentCreator (final SheetLayoutCollection sheetLayoutCollection,
                              final OutputStream outputStream)
  {
    super(sheetLayoutCollection);
    this.outputStream = outputStream;
    this.scaleFactor = SCALE_FACTOR;
    sheetNamesCount = new HashMap();
  }

  private String makeUnique (String name)
  {
    Integer count = (Integer) sheetNamesCount.get(name);
    if (count == null)
    {
      sheetNamesCount.put (name, new Integer(1));
      return name;
    }
    else
    {
      final int value = count.intValue() + 1;
      sheetNamesCount.put (name, new Integer(value));
      return makeUnique(name + " " + value);
    }

  }

  protected void handleBeginTable (final ReportDefinition reportDefinition)
          throws ReportProcessingException
  {
    newTable = true;
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
      final String uniqueSheetname = makeUnique(sheetName);
      if (uniqueSheetname.length() == 0 || uniqueSheetname.length() > 31)
      {
        Log.warn ("A sheet name must not be empty and greater than 31 characters");
        sheet = workbook.createSheet();
      }
      else if (isValidSheetName(uniqueSheetname) == false)
      {
        Log.warn ("A sheet name must not contain any of '/\\*?[]'");
        // OpenOffice is even more restrictive and only allows Letters,
        // Digits, Spaces and the Underscore
        sheet = workbook.createSheet();
      }
      else
      {
        Log.debug ("Creating a workbook: " + uniqueSheetname);
        sheet = workbook.createSheet(uniqueSheetname);
      }
    }
    // make sure a new patriarch is created if needed.
    patriarch = null;

    final ModifiableConfiguration config = reportDefinition
            .getReportConfiguration();
    final String paper = config.getConfigProperty
            (ExcelProcessor.CONFIGURATION_PREFIX + ".Paper");
    final String paperOrientation = config.getConfigProperty
            (ExcelProcessor.CONFIGURATION_PREFIX + ".PaperOrientation");

    final HSSFPrintSetup printSetup = sheet.getPrintSetup();
    ExcelPrintSetupFactory.performPageSetup
            (printSetup, reportDefinition.getPageDefinition(),
                    paper, paperOrientation);

    final boolean displayGridLines = "true".equals(config.getConfigProperty
            (ExcelProcessor.CONFIGURATION_PREFIX + ".GridLinesDisplayed"));
    final boolean printGridLines = "true".equals(config.getConfigProperty
            (ExcelProcessor.CONFIGURATION_PREFIX + ".GridLinesPrinted"));
    sheet.setDisplayGridlines(displayGridLines);
    sheet.setPrintGridlines(printGridLines);
  }

  private boolean isValidSheetName(final String sheetname)
  {
    if ((sheetname.indexOf('/') > -1)
      || (sheetname.indexOf('\\') > -1)
      || (sheetname.indexOf('?') > -1)
      || (sheetname.indexOf('*') > -1)
      || (sheetname.indexOf(']') > -1)
      || (sheetname.indexOf('[') > -1) )
    {
      return false;
    }

    return true;
  }

  protected void handleClose ()
          throws ReportProcessingException
  {
    open = false;
    try
    {
      workbook.write(outputStream);
      // cleanup..
      patriarch = null;
      sheet = null;
    }
    catch (IOException e)
    {
      Log.warn("could not write xls data. Message:", e);
    }
  }

  protected void handleEndTable ()
          throws ReportProcessingException
  {
    patriarch = null;
    sheet = null;
  }

  protected void handleOpen (final ReportDefinition reportDefinition)
          throws ReportProcessingException
  {
    open = true;
    workbook = new HSSFWorkbook();
    final boolean hardLimit = "true".equals
            (reportDefinition.getReportConfiguration().getConfigProperty
            ("org.jfree.report.modules.output.table.xls.HardStyleCountLimit"));
    cellStyleProducer = new HSSFCellStyleProducer(workbook, hardLimit);
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
  public boolean handleFlush ()
          throws ReportProcessingException
  {
    final GenericObjectTable go = getBackend();
    final SheetLayout layout = getCurrentLayout();

    final int width = Math.max(go.getColumnCount(), layout.getColumnCount());
    if (newTable)
    {
      for (short i = 0; i < width; i++)
      {
        final double cellWidth = StrictGeomUtility.toExternalValue
                (layout.getCellWidth(i, i + 1));
        final double poiCellWidth = (cellWidth * SCALE_FACTOR);
        sheet.setColumnWidth(i, (short) poiCellWidth);
      }
      newTable = false;
    }

    /** Split: first create the rows */
    final int height = go.getRowCount();
    final int layoutOffset = getLayoutOffset();
    for (int y = layoutOffset; y < height + layoutOffset; y++)
    {
      final HSSFRow row = getRowAt((short) y);
      final double lastRowHeight =
              StrictGeomUtility.toExternalValue(layout.getRowHeight(y));
      // we use 1/72 as unit, Excel uses 1/20 so we have to convert
      // POI sets a special flag, when using this function to make sure that
      // the height of the row is not connected to the fontsize.
      row.setHeightInPoints((short) (lastRowHeight));
    }

    /** Split: and then fill them */
    for (int y = layoutOffset; y < height + layoutOffset; y++)
    {
      for (int x = 0; x < width; x++)
      {
        final MetaElement element =
                (MetaElement) go.getObject(y - layoutOffset, x);

        if (element == null)
        {
          // only background stuff ...
          // just apply the background, if any ...
          final TableCellBackground background = layout.getElementAt(y, x);
          if (background != null)
          {
            final HSSFCell cell = getCellAt((short) x, y);
            final HSSFCellStyle style =
                    cellStyleProducer.createCellStyle(null, background);
            if (style != null)
            {
              cell.setCellStyle(style);
            }
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

        exportCell(element, rectangle, (short) x, y);
      }
    }
    return true;
  }


  /**
   * Exports the cell. The cell is generated and the stored cell style applied.
   *
   * @param x         the column
   * @param y         the row number
   * @param element   the content
   * @param rectangle the rectangle within the global grid
   */
  private void exportCell (final MetaElement element,
                           final TableRectangle rectangle,
                           final short x, final int y)
  {
    final HSSFCell cell = getCellAt(x, y);
    if (rectangle.getColumnSpan() > 1 || rectangle.getRowSpan() > 1)
    {
      sheet.addMergedRegion(new Region(y, x,
              (y + rectangle.getRowSpan() - 1),
              (short) (x + rectangle.getColumnSpan() - 1)));

      final int rectX = rectangle.getX1();
      final int rectY = rectangle.getY1();
      for (int row = 0; row < rectangle.getRowSpan(); row += 1)
      {
        for (int col = 0; col < rectangle.getColumnSpan(); col += 1)
        {
          final TableCellBackground bg = getCurrentLayout().getElementAt(rectY + row, rectX + col);
          final HSSFCell regionCell = getCellAt((short)(x + col), y + row);
          final HSSFCellStyle style = cellStyleProducer.createCellStyle(element, bg);
          if (style != null)
          {
            regionCell.setCellStyle(style);
          }
        }
      }
    }
    else
    {
      final TableCellBackground bg = getCurrentLayout().getRegionBackground(rectangle);
      final HSSFCellStyle style =
              cellStyleProducer.createCellStyle(element, bg);
      if (style != null)
      {
        cell.setCellStyle(style);
      }
    }


    if (element instanceof ExcelMetaElement)
    {
      final ExcelMetaElement me = (ExcelMetaElement) element;
      cell.setEncoding(HSSFCell.ENCODING_UTF_16);
      me.applyValue(this, cell);
    }
  }

  private HSSFCell getCellAt (final short x, final int y)
  {
    final HSSFRow row = getRowAt(y);
    final HSSFCell cell = row.getCell(x);
    if (cell != null)
    {
      return cell;
    }
    return row.createCell(x);
  }

  private HSSFRow getRowAt (final int y)
  {
    final HSSFRow row = sheet.getRow(y);
    if (row != null)
    {
      return row;
    }
    return sheet.createRow(y);
  }

  public HSSFWorkbook getWorkbook()
  {
    return workbook;
  }

  public HSSFPatriarch getPatriarch()
  {
    if (patriarch == null)
    {
      patriarch = getCurrentSheet().createDrawingPatriarch();
    }
    return patriarch;
  }

  public HSSFSheet getCurrentSheet()
  {
    return sheet;
  }

  public SheetLayout getCurrentLayout()
  {
    return super.getCurrentLayout();
  }

  public int getLayoutOffset()
  {
    return super.getLayoutOffset();
  }
}
