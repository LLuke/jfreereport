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
 * -------------------
 * RTFProducer.java
 * -------------------
 * (C)opyright 2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: RTFProducer.java,v 1.3 2003/02/20 00:39:37 taqua Exp $
 *
 * Changes
 * -------
 * 01-Feb-2003 : Initial version
 */
package com.jrefinery.report.targets.table.rtf;

import com.jrefinery.report.function.FunctionProcessingException;
import com.jrefinery.report.targets.table.TableCellBackground;
import com.jrefinery.report.targets.table.TableCellDataFactory;
import com.jrefinery.report.targets.table.TableGridLayout;
import com.jrefinery.report.targets.table.TableGridPosition;
import com.jrefinery.report.targets.table.TableProducer;
import com.jrefinery.report.util.Log;
import com.jrefinery.report.util.NoCloseOutputStream;
import com.lowagie.text.Cell;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Table;
import com.lowagie.text.rtf.RtfWriter;

import java.awt.Color;
import java.io.OutputStream;
import java.util.List;

public class RTFProducer extends TableProducer
{
  private OutputStream outputStream;
  private Document document;
  private RtfWriter writer;
  private RTFCellDataFactory cellDataFactory;

  public RTFProducer(OutputStream outputStream, boolean strictLayout)
  {
    super(strictLayout);
    this.outputStream = outputStream;
    cellDataFactory = new RTFCellDataFactory();
  }

  public void open()
  {
    // rtf does not support PageFormats or other meta data...
    document = new Document();
    writer = RtfWriter.getInstance(document, new NoCloseOutputStream (outputStream));
    document.open();
  }

  public void close()
  {
    document.close();
  }

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
    }
    clearCells();
  }

  private void setCellBackgroundStyle(Cell cell, List background)
  {
    TableCellBackground bg = createTableCellStyle(background);
    if (bg == null)
      return;

    Color color = bg.getColor();
    if (color != null)
    {
      cell.setBackgroundColor(color);
    }

    // Cell Borders are only added if all borders are defined and equal,
    // as iText-RTF does not support different borders ...
    Color bT = bg.getColorTop();
    Color bB = bg.getColorBottom();
    Color bL = bg.getColorLeft();
    Color bR = bg.getColorRight();
    if (bT == null || bB == null || bL == null || bR == null)
      return;
    if (bT.equals(bB) && bT.equals(bL) && bT.equals(bR) &&
        bg.getBorderSizeBottom() == bg.getBorderSizeTop() &&
        bg.getBorderSizeBottom() == bg.getBorderSizeLeft() &&
        bg.getBorderSizeBottom() == bg.getBorderSizeRight())
    {
      cell.setBorderColor(bT);
      cell.setBorderWidth(bg.getBorderSizeTop());
    }
  }

  private void generatePage (TableGridLayout layout)
    throws DocumentException
  {
    Table table = new Table(layout.getWidth(), layout.getHeight());
    table.setAutoFillEmptyCells(false);

    for (int y = 0; y < layout.getHeight(); y++)
    {
      // there is no need to create rows, this is handled automaticly...
      boolean printed = false;
      for (int x = 0; x < layout.getWidth(); x++)
      {
        TableGridLayout.Element gridElement = layout.getData(x, y);
        if (gridElement == null)
        {
          // cell is empty, autofill will create an empty cell here ...
          Cell cell = new Cell();
          cell.setBorderWidth(0);
          table.addCell(cell, y, x);
          printed = true;
          continue;
        }

        TableGridPosition gridPosition = gridElement.getRoot();
        if (gridPosition == null || gridPosition.isInvalidCell())
        {
          Cell cell = new Cell();
          cell.setBorderWidth(0);
          // iText cell width is a string, why?

          setCellBackgroundStyle(cell, gridElement.getBackground());
          table.addCell(cell, y, x);
          printed = true;
          continue;
        }

        if (gridPosition.isOrigin(x, y) == false)
        {
          // this is a spanned field, no need to do anything
          continue;
        }

        RTFCellData cellData = (RTFCellData) gridPosition.getElement();
        Cell cell = cellData.getCell();
        cell.setBorderWidth(0);
        setCellBackgroundStyle(cell, gridElement.getBackground());

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

  public void beginPage(String name)
  {
    // ignore the event, not important ...
  }

  public TableCellDataFactory getCellDataFactory()
  {
    return cellDataFactory;
  }

  public boolean isOpen()
  {
    if (document == null)
      return false;
    return document.isOpen();
  }
}
