/**
 * Date: Jan 21, 2003
 * Time: 4:47:35 PM
 *
 * $Id$
 */
package com.jrefinery.report.targets.table.csv;

import com.jrefinery.report.targets.csv.CSVQuoter;
import com.jrefinery.report.targets.table.TableCellDataFactory;
import com.jrefinery.report.targets.table.TableGridPosition;
import com.jrefinery.report.targets.table.TableProducer;

import java.io.PrintWriter;

public class CSVProducer extends TableProducer
{
  private PrintWriter writer;
  private CSVQuoter quoter;
  private CSVCellDataFactory cellDataFactory;
  private boolean isOpen;

  public CSVProducer(PrintWriter writer)
  {
    this.writer = writer;
    this.quoter = new CSVQuoter();
    this.cellDataFactory = new CSVCellDataFactory();
  }

  public void open()
  {
    isOpen = true;
  }

  public void close()
  {
    isOpen = false;
  }

  public void endPage()
  {
    generatePage(layoutGrid());
    writer.println("</table>");
    clearCells();
  }

  private void generatePage (TableProducerLayout layout)
  {
    TableGridPosition[][] grid = layout.getGrid();

    for (int y = 0; y < grid.length; y++)
    {
      int x = 0;
      for (x = 0; x < grid[y].length; x++)
      {
        TableGridPosition gridPosition = grid[y][x];
        if (gridPosition.getElement() != null)
        {
          CSVCellData cellData = (CSVCellData) gridPosition.getElement();

          writer.print(quoter.doQuoting(cellData.getValue()));
          writer.print(quoter.getSeparator());
          x += gridPosition.getColSpan() - 1;
        }
      }
      writer.println();
    }

  }

  public void beginPage(String name)
  {
    // remains empty ...
  }

  public TableCellDataFactory getCellDataFactory()
  {
    return cellDataFactory;
  }

  public boolean isOpen()
  {
    return isOpen;
  }
}
