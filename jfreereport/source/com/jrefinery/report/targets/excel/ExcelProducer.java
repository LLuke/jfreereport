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
 * $Id: PDFOutputTarget.java,v 1.8 2002/12/11 01:10:41 mungady Exp $
 *
 * Changes
 * -------
 * 03-Jan-2002 : initial version
 */

package com.jrefinery.report.targets.excel;

import com.jrefinery.report.Band;
import com.jrefinery.report.Element;
import com.jrefinery.report.JFreeReport;
import com.jrefinery.report.TextElement;
import com.jrefinery.report.targets.pageable.OutputTargetException;
import com.jrefinery.report.targets.style.ElementStyleSheet;
import com.jrefinery.report.util.Log;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.Region;

import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
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
public class ExcelProducer
{
  /** The configuration prefix. */
  public static final String CONFIGURATION_PREFIX =
      "com.jrefinery.report.targets.excel.default.";

  /** A useful constant for specifying the PDF creator. */
  private static final String CREATOR =
      JFreeReport.getInfo().getName()
      + " version "
      + JFreeReport.getInfo().getVersion();

  /** The output stream. */
  private OutputStream out;

  /** The Excel cell style factory */
  private ExcelCellStyleFactory cellStyleFactory;

  /** The list of all the cells to be generated within the XLS-file */
  private ArrayList cells;

  /** The list of all x-positions where cells begin or end */
  private ArrayList xCuts;

  /** The list of all y-positions where cells begin or end */
  private ArrayList yCuts;

  /** A grid to place all the cells in, so that we can iterate over rows and columns when we generate the XLS file */
  private ExcelGridPosition grid[][] = null;

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
  public ExcelProducer(OutputStream out)
  {
    this.out = out;
    this.cells = new ArrayList();
    xCuts = null;
    yCuts = null;
    cellStyleFactory = null;
  }

  /**
   * Opens the document.
   */
  public void open()
  {
    workbook = new HSSFWorkbook();
    cellStyleFactory = new ExcelCellStyleFactory(workbook);

    // style for empty cells

    // Clear list of cells
    cells.clear();
  }

  public void beginPage (String name)
  {
    sheet = workbook.createSheet(name);
  }

  public void endPage ()
  {
    layoutGrid();
    writeSheet();
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

  /** Calculates the positions for the Excel cells. */
  private void layoutGrid()
  {
    xCuts = new ArrayList();
    yCuts = new ArrayList();

    int nSize = cells.size();
    // System.out.println("We have " + nSize + " cells");
    for (int i = 0; i < nSize; i++)
    {
      ExcelCellData cell = (ExcelCellData) cells.get(i);
      // System.out.println("Cell # " + i + " : " + cell);

      // which coordinates are used?
      Rectangle2D bounds = cell.getBounds();
      Long minX = new Long(Math.round(bounds.getX()));
      Long maxX = new Long(Math.round(bounds.getX() + bounds.getWidth()));
      Long minY = new Long(Math.round(bounds.getY()));
      Long maxY = new Long(Math.round(bounds.getY() + bounds.getHeight()));
      if (!xCuts.contains(minX))
      {
        xCuts.add(minX);
      }
      if (!xCuts.contains(maxX))
      {
        xCuts.add(maxX);
      }
      if (!yCuts.contains(minY))
      {
        yCuts.add(minY);
      }
      if (!yCuts.contains(maxY))
      {
        yCuts.add(maxY);
      }
    }
    Collections.sort(xCuts);
    Collections.sort(yCuts);
    // let's have a look at the results
    /*
    int nXCount = xCuts.size();
    for (int i = 0; i < nXCount; i++)
    {
      // System.out.println("found XCut at " + xCuts.get(i));
    }
    int nYCount = yCuts.size();
    for (int i = 0; i < nYCount; i++)
    {
      // System.out.println("found YCut at " + yCuts.get(i));
    }
    */

    // generate a grid with empty cells
    int xCellCount = xCuts.size() - 1;
    int yCellCount = yCuts.size() - 1;

    grid = new ExcelGridPosition[yCellCount][xCellCount];
    for (int j = 0; j < yCellCount; j++)
    {
      for (int i = 0; i < xCellCount; i++)
      {
        grid[j][i] =
            new ExcelGridPosition(
                null,
                ((Long) xCuts.get(i + 1)).longValue() - ((Long) xCuts.get(i)).longValue(),
                ((Long) yCuts.get(j + 1)).longValue() - ((Long) yCuts.get(j)).longValue(),
                1,
                1
            );
      }
    }
    // place all used cells within the grid
    for (int i = 0; i < nSize; i++)
    {
      ExcelCellData cell = (ExcelCellData) cells.get(i);

      Rectangle2D bounds = cell.getBounds();
      Long minX = new Long(Math.round(bounds.getX()));
      Long maxX = new Long(Math.round(bounds.getX() + bounds.getWidth()));
      Long minY = new Long(Math.round(bounds.getY()));
      Long maxY =
          new Long(Math.round(bounds.getY() + bounds.getHeight()));
      // Debug
      // System.out.println( "Positions: minX=" + minX + ", maxX=" + maxX + ", minY=" + minY + ", maxY=" + maxY);
      int x1 = xCuts.indexOf(minX);
      int y1 = yCuts.indexOf(minY);
      int x2 = xCuts.indexOf(maxX);
      int y2 = yCuts.indexOf(maxY);

      int colSpan = x2 - x1;
      int rowSpan = y2 - y1;
      int width = maxX.intValue() - minX.intValue();
      int height = maxY.intValue() - minY.intValue();

      grid[y1][x1] = new ExcelGridPosition(cell, width, height, colSpan, rowSpan);
    }

  }

  /**
   * generate the XLS data structure
   *
   * The empty cell stuff was never referenced and did nothing usefull, removed it.
   */
  private void writeSheet()
  {
    long width = 0;
    for (int i = 1; i < xCuts.size(); i++)
    {
      width = ((Long) xCuts.get(i)).longValue() - ((Long) xCuts.get(i - 1)).longValue();
      sheet.setColumnWidth((short) (i - 1), (short) (width * XFACTOR));
    }

    for (int y = 0; y < grid.length; y++)
    {
      HSSFRow row = sheet.createRow((short) y);

      long lastRowHeight = grid[y][0].getHeight();

      row.setHeight((short) (lastRowHeight * YFACTOR));

      int x = 0;
      for (x = 0; x < grid[y].length; x++)
      {
        ExcelGridPosition gridPosition = grid[y][x];
        if (gridPosition.getElement() != null)
        {
          ExcelCellData cellData = gridPosition.getElement();

          exportCell(row, cellData, gridPosition, x, y);

          x += gridPosition.getColSpan() - 1;
        }
      }
    }
  }

  /**
   *
   * @param row
   * @param cellData
   * @param gridCell
   * @param x
   * @param y
   */
  private void exportCell(HSSFRow row, ExcelCellData cellData, ExcelGridPosition gridCell, int x, int y)
  {

    if (gridCell.getColSpan() > 1 || gridCell.getRowSpan() > 1)
    {

      sheet.addMergedRegion(new Region(y, (short) x, (y + gridCell.getRowSpan() - 1), (short) (x + gridCell.getColSpan() - 1)));
    }

    if (cellData.getText() != null && cellData.getText().length() > 0)
    {
      HSSFCell cell = row.createCell((short) x);
      cell.setEncoding(HSSFCell.ENCODING_UTF_16);
      cell.setCellValue(cellData.getText());
      cell.setCellStyle(cellData.getStyle());
    }
  }

  /**
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
    // do nothing if the band has a height of 0 (also invisible)
    if (bounds.getHeight() == 0)
    {
      return;
    }

    // process all elements
    List l = band.getElements();
    for (int i = 0; i < l.size(); i++)
    {
      Element e = (Element) l.get(i);
      if (e instanceof Band)
      {
        Rectangle2D bbounds = (Rectangle2D) e.getStyle().getStyleProperty(ElementStyleSheet.BOUNDS);
        processBand(translateSubRect(bbounds, bounds), (Band) e);
      }
      else
      {
        processElement(bounds, e);
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
    Rectangle2D rt = outer.getBounds2D();

    double w = Math.min (rt.getWidth() - inner.getX(), inner.getWidth());
    double h = Math.min (rt.getHeight() - inner.getY(), inner.getHeight());
    rt.setRect(
        rt.getX() + inner.getX(),
        rt.getY() + inner.getY(),
        Math.max(0, w),
        Math.max(0, h));
    return rt;
  }

  /**
   * Add the specified element to the logical page. Create content from the values
   * contained in the element and format the content by using the element's attributes.
   * <p>
   * @param bounds  the element bounds.
   * @param e  the element.
   *
   * @throws NullPointerException if the element has no valid layout (no BOUNDS defined).
   * Bounds are usually defined by the BandLayoutManager.
   */
  private void processElement(Rectangle2D bounds, Element e)
  {
    if (e.isVisible() == false)
    {
      return;
    }
    if (e.getContentType().equals(TextElement.CONTENT_TYPE) == false)
    {
      return;
    }

    Rectangle2D elementBounds = (Rectangle2D)
        e.getStyle().getStyleProperty(ElementStyleSheet.BOUNDS);
    if (elementBounds == null)
    {
      throw new NullPointerException("No layout for element");
    }
    Rectangle2D drawBounds = translateSubRect(bounds, elementBounds);
    cells.add(new ExcelCellData(e, drawBounds, cellStyleFactory.getExcelCellStyle(e)));
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

  public int getCellCount ()
  {
    return cells.size();
  }
}
