/**
 * Date: Jan 18, 2003
 * Time: 8:06:54 PM
 *
 * $Id$
 */
package com.jrefinery.report.targets.table.html;

import com.jrefinery.report.JFreeReport;
import com.jrefinery.report.targets.table.TableCellDataFactory;
import com.jrefinery.report.targets.table.TableGridPosition;
import com.jrefinery.report.targets.table.TableProducer;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;

public class HtmlProducer extends TableProducer
{
  private PrintStream pout;
  private JFreeReport report;
  private HtmlCellDataFactory cellDataFactory;
  private boolean isOpen;

  public HtmlProducer(OutputStream out,JFreeReport report)
  {
    this.pout = new PrintStream(out);
    this.report = report;
    this.cellDataFactory = new HtmlCellDataFactory();
  }

  public void open()
  {
    pout.println("<html>");
    pout.print("<head><title>");
    pout.print(report.getName());
    pout.println("</title></head>");
    pout.println("<body>");
    isOpen = true;
  }

  public void close()
  {
    pout.println("</body></html>");
    isOpen = false;
  }

  public void endPage()
  {
    generatePage(layoutGrid());
    pout.println("</table>");
    clearCells();
  }

  public void beginPage(String name)
  {
    pout.println("<table border=\"2\">");
  }

  public TableCellDataFactory getCellDataFactory()
  {
    return cellDataFactory;
  }

  public boolean isOpen()
  {
    return isOpen;
  }

  private void generatePage (TableProducerLayout layout)
  {
    ArrayList xCuts = layout.getxCuts();
    TableGridPosition[][] grid = layout.getGrid();

    for (int y = 0; y < grid.length; y++)
    {

      long lastRowHeight = grid[y][0].getHeight();
      pout.println("<tr height=\"" + lastRowHeight + "\">");

      int x = 0;
      for (x = 0; x < grid[y].length; x++)
      {
        TableGridPosition gridPosition = grid[y][x];
        if (gridPosition.getElement() != null)
        {
          HtmlCellData cellData = (HtmlCellData) gridPosition.getElement();

          pout.println("<td rowspan=\"" +
                       gridPosition.getRowSpan() +
                       "\" colspan=\"" +
                       gridPosition.getColSpan() +
                       "\">");
          pout.println(cellData.getValue());
          pout.println("</td>");

          x += gridPosition.getColSpan() - 1;
        }
      }
      pout.println("</tr>");
    }
  }
}
