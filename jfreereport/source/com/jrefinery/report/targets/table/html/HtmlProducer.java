/**
 * Date: Jan 18, 2003
 * Time: 8:06:54 PM
 *
 * $Id: HtmlProducer.java,v 1.1 2003/01/18 20:47:36 taqua Exp $
 */
package com.jrefinery.report.targets.table.html;

import com.jrefinery.report.JFreeReport;
import com.jrefinery.report.io.ext.factory.objects.ColorObjectDescription;
import com.jrefinery.report.targets.table.TableCellDataFactory;
import com.jrefinery.report.targets.table.TableGridPosition;
import com.jrefinery.report.targets.table.TableProducer;
import com.jrefinery.report.util.CharacterEntityParser;
import com.jrefinery.report.util.Log;

import java.awt.Color;
import java.awt.Font;
import java.io.OutputStream;
import java.io.PrintStream;

public class HtmlProducer extends TableProducer
{
  private ColorObjectDescription colorObjectDescription;
  private PrintStream pout;
  private JFreeReport report;
  private HtmlCellDataFactory cellDataFactory;
  private CharacterEntityParser entityParser;

  private boolean isOpen;

  public HtmlProducer(OutputStream out,JFreeReport report)
  {
    this.pout = new PrintStream(out);
    this.report = report;
    this.cellDataFactory = new HtmlCellDataFactory();
    this.colorObjectDescription = new ColorObjectDescription();
    this.entityParser = CharacterEntityParser.createHTMLEntityParser();
  }

  public void open()
  {
    pout.println("<html>");
    pout.print("<head><title>");
    pout.print(entityParser.encodeEntities(report.getName()));
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
    pout.println("<table width=\"100%\">");
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
          Font font = cellData.getStyle().getFont();
          String colorValue = getColorString(cellData.getStyle().getFontColor());

          pout.print("<span style=\"font-family:'");
          pout.print(font.getName());
          pout.print("';font-size:");
          pout.print(font.getSize());
          if (font.isBold())
          {
            pout.print(";font-weight:bold");
          }
          if (font.isItalic())
          {
            pout.print(";font-style:italic");
          }
          if (colorValue != null)
          {
            pout.print(";color:");
            pout.print(colorValue);
          }
          pout.print("\">");
          pout.println(entityParser.encodeEntities(cellData.getValue()));
          pout.println("</div>");
          pout.println("</td>");

          x += gridPosition.getColSpan() - 1;
        }
      }
      pout.println("</tr>");
    }
  }

  private String getColorString (Color color)
  {
    try
    {
      colorObjectDescription.setParameterFromObject(color);
      return (String) colorObjectDescription.getParameter ("value");
    }
    catch (Exception ofe)
    {
      Log.debug ("Failed to refactor the color value");
    }
    return null;
  }
}
