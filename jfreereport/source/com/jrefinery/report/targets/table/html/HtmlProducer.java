/**
 * Date: Jan 18, 2003
 * Time: 8:06:54 PM
 *
 * $Id: HtmlProducer.java,v 1.5 2003/01/25 02:47:10 taqua Exp $
 */
package com.jrefinery.report.targets.table.html;

import com.jrefinery.report.function.FunctionProcessingException;
import com.jrefinery.report.targets.table.TableCellDataFactory;
import com.jrefinery.report.targets.table.TableGridLayout;
import com.jrefinery.report.targets.table.TableGridPosition;
import com.jrefinery.report.targets.table.TableProducer;
import com.jrefinery.report.util.CharacterEntityParser;
import com.jrefinery.report.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.Writer;
import java.util.Enumeration;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;

public class HtmlProducer extends TableProducer
{
  private Writer writer;
  private PrintWriter pout;
  private String reportName;
  private HtmlCellDataFactory cellDataFactory;
  private CharacterEntityParser entityParser;
  private HtmlStyleCollection styleCollection;

  private ByteArrayOutputStream content;
  private boolean isOpen;
  private static final String XHTML_HEADER =
       "<!DOCTYPE html \n" +
       "     PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\"\n" +
       "     \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">\n" +
       "<html xmlns=\"http://www.w3.org/1999/xhtml\" >\n" +
       "<head>\n";

  public HtmlProducer(Writer w, String reportName, boolean strict)
  {
    super(strict);
    this.writer = w;
    this.content = null;
    this.pout = null;
    this.reportName = reportName;
    this.styleCollection = new HtmlStyleCollection();
    this.cellDataFactory = new HtmlCellDataFactory(styleCollection);
    this.entityParser = CharacterEntityParser.createHTMLEntityParser();
  }

  public void open()
  {
    this.content = new ByteArrayOutputStream();
    DeflaterOutputStream deflaterStream = new DeflaterOutputStream(content, new Deflater(Deflater.BEST_COMPRESSION));
    this.pout = new PrintWriter(deflaterStream);

    // the style sheet definition will be inserted before the content is written ...
    pout.print("<title>");
    pout.print(entityParser.encodeEntities(reportName));
    pout.println("</title></head>");
    pout.println("<body>");
    isOpen = true;
  }

  public void close()
  {
    pout.println("</body></html>");

    try
    {
      // now finish the style sheet definition
      writer.write(XHTML_HEADER);
      writer.write("<style>");

      Enumeration styles = styleCollection.getDefinedStyles();
      while (styles.hasMoreElements())
      {
        HtmlCellStyle style = (HtmlCellStyle) styles.nextElement();
        if (styleCollection.isRegistered(style))
        {
          String name = styleCollection.lookupName(style);
          writer.write ("span.");
          writer.write (name);
          writer.write (" { ");
          writer.write (styleCollection.createStyleSheetDefinition(style));
          writer.write (" }; ");
        }
      }

      writer.write("</style>");
      writer.flush();

      pout.flush();
      pout.close();
      byte[] data = content.toByteArray();
      content.close();
      content = null;
      pout = null;

      InflaterInputStream infIn = new InflaterInputStream(new BufferedInputStream(new ByteArrayInputStream(data)));
      InputStreamReader inReader = new InputStreamReader(infIn);

      char[] buffer = new char[4096];

      int bytesRead = inReader.read(buffer);
      while (bytesRead > 0)
      {
        writer.write(buffer, 0, bytesRead);
        bytesRead = inReader.read(buffer);
      }

      inReader.close();
    }
    catch (IOException ioe)
    {
      throw new FunctionProcessingException ("Failed to write", ioe);
    }

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
    pout.println("<table width=\"100%\" border=\"2\">");
  }

  public TableCellDataFactory getCellDataFactory()
  {
    return cellDataFactory;
  }

  public boolean isOpen()
  {
    return isOpen;
  }

  private void generatePage (TableGridLayout layout)
  {
    pout.println();

    for (int y = 0; y < layout.getHeight(); y++)
    {
      int lastRowHeight = (int)(layout.getRowEnd(y) - layout.getRowStart(y));

      pout.println("<tr height=\"" + lastRowHeight + "\">");

      for (int x = 0; x < layout.getWidth(); x++)
      {
        TableGridLayout.Element gridElement = layout.getData(x, y);
        if (gridElement == null)
        {
          pout.println("<td>&nbsp;</td>");
          continue;
        }

        TableGridPosition gridPosition = gridElement.getRoot();
        if (gridPosition == null)
        {
          pout.println("<td>&nbsp;</td>");
          continue;
        }

        if (gridPosition.isOrigin(x, y) == false)
        {
          // this is a spanned field.
          continue;
        }

        HtmlCellData cellData = (HtmlCellData) gridPosition.getElement();

        pout.print("    <td");
        if (gridPosition.getRowSpan() > 1)
        {
          pout.print(" rowspan=\"");
          pout.print(gridPosition.getRowSpan());
          pout.print("\"");
        }
        if (gridPosition.getColSpan() > 1)
        {
          pout.print(" colspan=\"");
          pout.print(gridPosition.getColSpan());
          pout.print("\"");
        }
        pout.print(">");

        if (styleCollection.isRegistered(cellData.getStyle()))
        {
          // stylesheet defined in the header
          pout.print("<span class=\"");
          pout.print(styleCollection.lookupName(cellData.getStyle()));
          pout.print("\">");
        }
        else
        {
          // stylesheet defined as inline style
          pout.print("<span style=\"");
          pout.print(styleCollection.createStyleSheetDefinition(cellData.getStyle()));
          pout.print("\">");
        }

        //printText(entityParser.encodeEntities(gridPosition.getElement().debugChunk));
        printText(entityParser.encodeEntities(cellData.getValue()));
        pout.print("</span>");
        pout.println("</td>");

        x += gridPosition.getColSpan() - 1;
      }
      pout.println("</tr>");
    }
  }

  private void printText (String text)
  {
    if (text.length() == 0)
    {
      pout.print("&nbsp;");
      return;
    }

    try
    {
      BufferedReader reader = new BufferedReader(new StringReader(text));
      String readLine = null;
      boolean flagStart = true;
      while ((readLine = reader.readLine()) != null)
      {
        if (flagStart == true)
        {
          flagStart = false;
        }
        else
        {
          pout.println("<br>");
        }
        pout.print(readLine);
      }
      reader.close();
    }
    catch (IOException ioe)
    {
      Log.info("This will not happen.", ioe);
    }
  }
}
