/**
 * Date: Jan 21, 2003
 * Time: 4:47:35 PM
 *
 * $Id: CSVTableProducer.java,v 1.2 2003/01/25 20:34:12 taqua Exp $
 */
package com.jrefinery.report.targets.table.csv;

import com.jrefinery.report.targets.csv.CSVQuoter;
import com.jrefinery.report.targets.table.TableCellDataFactory;
import com.jrefinery.report.targets.table.TableGridLayout;
import com.jrefinery.report.targets.table.TableGridPosition;
import com.jrefinery.report.targets.table.TableProducer;

import java.io.PrintWriter;

public class CSVTableProducer extends TableProducer
{
  private PrintWriter writer;
  private CSVQuoter quoter;
  private CSVCellDataFactory cellDataFactory;
  private boolean isOpen;

  public CSVTableProducer(PrintWriter writer, boolean strict)
  {
    super(strict);
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

  public String getSeparator()
  {
    return quoter.getSeparator();
  }

  public void setSeparator(String separator)
  {
    this.quoter.setSeparator(separator);
  }

  public void endPage()
  {
    if (isDummy() == false)
    {
      generatePage(layoutGrid());
    }
    clearCells();
  }

  private void generatePage (TableGridLayout layout)
  {
    for (int y = 0; y < layout.getHeight(); y++)
    {
      for (int x = 0; x < layout.getWidth(); x++)
      {
        TableGridLayout.Element gridPosition = layout.getData(x,y);
        if (gridPosition == null)
          continue;

        if (gridPosition.getRoot() != null)
        {
          TableGridPosition pos = gridPosition.getRoot();
          if ((pos.getCol() == x && pos.getRow() == y) &&
              (pos.getElement() != null))
          {
            CSVCellData cellData = (CSVCellData) pos.getElement();

            writer.print(quoter.doQuoting(cellData.getValue()));
            writer.print(quoter.getSeparator());
            x += pos.getColSpan() - 1;
          }
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
