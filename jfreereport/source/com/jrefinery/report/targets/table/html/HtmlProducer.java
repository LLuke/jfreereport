/**
 * Date: Jan 18, 2003
 * Time: 8:06:54 PM
 *
 * $Id: HtmlProducer.java,v 1.6 2003/01/25 20:34:12 taqua Exp $
 *
 * This file now produces valid HTML4
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
import java.util.Enumeration;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;

public class HtmlProducer extends TableProducer
{
  private PrintWriter pout;
  private String reportName;
  private HtmlCellDataFactory cellDataFactory;
  private static CharacterEntityParser entityParser;
  private HtmlStyleCollection styleCollection;
  private HtmlFilesystem filesystem;
  private boolean useXHTML;

  private ByteArrayOutputStream content;
  private boolean isOpen;
  private static final String[] XHTML_HEADER = {
       "<?xml version=\"1.0\" encoding=\"us-ascii\"?>",
       "<!DOCTYPE html",
       "     PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\"",
       "     \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\"",
       "<html xmlns=\"http://www.w3.org/1999/xhtml\" >",
       "<head>"};

  private static final String[] HTML4_HEADER = {
       "<!DOCTYPE HTML ",
       "     PUBLIC \"-//W3C//DTD HTML 4.01//EN\"",
       "     \"http://www.w3.org/TR/html4/strict.dtd\">",
       "<html>",
       "<head>"};


  public HtmlProducer(HtmlFilesystem filesystem,
                      String reportName, boolean strict, boolean useXHTML)
  {
    super(strict);
    this.filesystem = filesystem;
    this.content = null;
    this.pout = null;
    this.reportName = reportName;
    this.styleCollection = new HtmlStyleCollection();
    this.cellDataFactory = new HtmlCellDataFactory(styleCollection, useXHTML);
    this.useXHTML = useXHTML;
  }

  private static CharacterEntityParser getEntityParser ()
  {
    if (entityParser == null)
    {
      entityParser = CharacterEntityParser.createHTMLEntityParser();
    }
    return entityParser;
  }

  public void open()
  {
    this.content = new ByteArrayOutputStream();
    DeflaterOutputStream deflaterStream = new DeflaterOutputStream(content, new Deflater(Deflater.BEST_COMPRESSION));
    this.pout = new PrintWriter(deflaterStream);

    // the style sheet definition will be inserted right before the content is written ...
    pout.print("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=");
    // todo get the file encoding
    pout.print("us-ascii");
    if (useXHTML)
    {
      pout.println("\" />");
    }
    else
    {
      pout.println("\">");
    }
    pout.print("<title>");
    pout.print(getEntityParser().encodeEntities(reportName));
    pout.println("</title></head>");
    pout.println("<body>");
    isOpen = true;
  }

  public void close()
  {
    try
    {
      pout.println("</body></html>");

      StringBuffer cssbuffer = new StringBuffer();

      Enumeration styles = styleCollection.getDefinedStyles();
      while (styles.hasMoreElements())
      {
        HtmlCellStyle style = (HtmlCellStyle) styles.nextElement();
        if (styleCollection.isRegistered(style))
        {
          String name = styleCollection.lookupName(style);
          cssbuffer.append (".");
          cssbuffer.append (name);
          cssbuffer.append (" { ");
          cssbuffer.append (styleCollection.createStyleSheetDefinition(style));
          cssbuffer.append (" } ");
          cssbuffer.append (System.getProperty("line.separator", "\n"));
        }
      }
      HtmlReferenceData cssRef = filesystem.createCSSReference(cssbuffer.toString());


      PrintWriter writer = new PrintWriter(filesystem.getRootStream());

      if (useXHTML)
      {
        // now finish the style sheet definition
        for (int i = 0; i < XHTML_HEADER.length; i++)
        {
          writer.println(XHTML_HEADER[i]);
        }
      }
      else
      {
        // now finish the style sheet definition
        for (int i = 0; i < HTML4_HEADER.length; i++)
        {
          writer.println(HTML4_HEADER[i]);
        }
      }
      // is a href type ...
      if (cssRef.isExternal())
      {
        writer.print("<link rel=\"stylesheet\" type=\"text/css\" ");
        writer.print(cssRef.getReference());
        if (useXHTML)
        {
          writer.println(" />");
        }
        else
        {
          writer.println(">");
        }
      }
      else
      {
        writer.println("<style type=\"text/css\">");
        writer.print(cssRef.getReference());
        writer.println("</style>");
      }
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
      writer.flush();
      filesystem.close();
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
    // border=\"2\"
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

  private void generatePage (TableGridLayout layout)
  {
    pout.println();

    for (int y = 0; y < layout.getHeight(); y++)
    {
      int lastRowHeight = layout.getRowEnd(y) - layout.getRowStart(y);

      pout.println("<tr style=\"height:" + lastRowHeight + "pt\">");

      for (int x = 0; x < layout.getWidth(); x++)
      {
        TableGridLayout.Element gridElement = layout.getData(x, y);
        if (gridElement == null)
        {
          int width = layout.getColumnEnd(x) - layout.getColumnStart(x);
          pout.println("<td style=\"width:" + width + "pt\">&nbsp;</td>");
          continue;
        }

        TableGridPosition gridPosition = gridElement.getRoot();
        if (gridPosition == null)
        {
          int width = layout.getColumnEnd(x) - layout.getColumnStart(x);
          pout.println("<td   style=\"width:" + width + "pt\">&nbsp;</td>");
          continue;
        }

        if (gridPosition.isOrigin(x, y) == false)
        {
          // this is a spanned field.
          continue;
        }

        HtmlCellData cellData = (HtmlCellData) gridPosition.getElement();

        pout.print("    <td style=\"width:");
        pout.print((int) gridPosition.getBounds().getWidth());
        pout.print("pt");
        pout.print("; height:");
        pout.print((int) gridPosition.getBounds().getHeight());
        pout.print("pt\"");

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
          pout.print("<div class=\"");
          pout.print(styleCollection.lookupName(cellData.getStyle()));
          pout.print("\">");
        }
        else
        {
          // stylesheet defined as inline style
          pout.print("<div style=\"");
          pout.print(styleCollection.createStyleSheetDefinition(cellData.getStyle()));
          pout.print("\">");
        }

        cellData.write(pout, filesystem);

        pout.print("</div>");
        pout.println("</td>");

        x += gridPosition.getColSpan() - 1;
      }
      pout.println("</tr>");
    }
  }

  public static void printText (PrintWriter pout, String text, boolean useXHTML)
  {
    if (text.length() == 0)
    {
      pout.print("");
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
          if (useXHTML)
          {
            pout.println("<br />&nbsp;");
          }
          else
          {
            pout.println("<br>&nbsp;");
          }
        }
        pout.print(getEntityParser().encodeEntities(readLine));
      }
      reader.close();
    }
    catch (IOException ioe)
    {
      Log.info("This will not happen.", ioe);
    }
  }
}
