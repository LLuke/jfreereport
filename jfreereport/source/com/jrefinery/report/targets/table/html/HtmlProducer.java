/**
 * Date: Jan 18, 2003
 * Time: 8:06:54 PM
 *
 * $Id: HtmlProducer.java,v 1.13 2003/02/02 23:43:52 taqua Exp $
 *
 * This file now produces valid HTML4
 */
package com.jrefinery.report.targets.table.html;

import com.jrefinery.report.function.FunctionProcessingException;
import com.jrefinery.report.targets.table.TableCellBackground;
import com.jrefinery.report.targets.table.TableCellDataFactory;
import com.jrefinery.report.targets.table.TableGridLayout;
import com.jrefinery.report.targets.table.TableGridPosition;
import com.jrefinery.report.targets.table.TableProducer;
import com.jrefinery.report.util.CharacterEntityParser;
import com.jrefinery.report.util.IOUtils;
import com.jrefinery.report.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringReader;
import java.util.Enumeration;
import java.util.List;
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
  private String encoding;

  private ByteArrayOutputStream content;
  private boolean isOpen;
  private static final String[] XHTML_HEADER = {
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
                      String reportName,
                      boolean strict,
                      boolean useXHTML,
                      String encoding)
  {
    super(strict);
    if (filesystem == null) throw new NullPointerException();
    if (reportName == null) reportName = "unnamed report";
    if (encoding == null) throw new NullPointerException();
    
    this.filesystem = filesystem;
    this.content = null;
    this.pout = null;
    this.reportName = reportName;
    this.styleCollection = new HtmlStyleCollection();
    this.cellDataFactory = new HtmlCellDataFactory(styleCollection, useXHTML);
    this.useXHTML = useXHTML;
    this.encoding = encoding;
  }

  public String getEncoding()
  {
    return encoding;
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
    try
    {
      this.pout = new PrintWriter(new OutputStreamWriter(deflaterStream, getEncoding()));
    }
    catch (IOException ioe)
    {
      throw new FunctionProcessingException("Failed to create the writer", ioe);
    }

    // the style sheet definition will be inserted right before the content is written ...
    pout.print("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=");
    pout.print(getEncoding());
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
        writer.print ("<?xml version=\"1.0\" encoding=\"");
        writer.print (getEncoding());
        writer.println ("\"?>");
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

      IOUtils.getInstance().copyWriter(inReader, writer);

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
    if (isDummy() == false)
    {
      generatePage(layoutGrid());
    }
    pout.println("</table></p>");
    clearCells();
  }

  public void beginPage(String name)
  {
    pout.println ("<p>");
    pout.println("<table width=\"100%\" cellspacing=\"0\" cellpadding=\"0\">");
    //pout.println("<table border=\"2\" width=\"100%\" cellspacing=\"0\" cellpadding=\"0\">");
  }

  public TableCellDataFactory getCellDataFactory()
  {
    return cellDataFactory;
  }

  public boolean isOpen()
  {
    return isOpen;
  }

  protected String createHtmlBackgroundStyle (List background)
  {
    TableCellBackground bg = createTableCellStyle(background);
    if (bg == null)
      return null;

    return styleCollection.getBackgroundStyle(bg);
  }

  private void generatePage (TableGridLayout layout)
  {
    pout.println();

    for (int y = 0; y < layout.getHeight(); y++)
    {
      int lastRowHeight = layout.getRowEnd(y) - layout.getRowStart(y);

      pout.println("<tr style=\"height:" + lastRowHeight + "pt\">");
      boolean printed = false;
      for (int x = 0; x < layout.getWidth(); x++)
      {
        TableGridLayout.Element gridElement = layout.getData(x, y);
        if (gridElement == null)
        {
          int width = layout.getColumnEnd(x) - layout.getColumnStart(x);
          pout.println ("<!-- No Element -->");
          pout.println("<td style=\"width:" + width + "pt\"></td>");
          printed = true;
          continue;
        }

        TableGridPosition gridPosition = gridElement.getRoot();
        if (gridPosition == null || gridPosition.isInvalidCell())
        {
          int width = layout.getColumnEnd(x) - layout.getColumnStart(x);
          if (gridPosition == null)
          {
            pout.println ("<!-- gridposition is null -->");
          }
          else
          {
            pout.println("<!-- is invalid cell -->");
          }

          pout.print("<td style=\"width:");
          pout.print(width);
          pout.print("pt");

          String style = createHtmlBackgroundStyle(gridElement.getBackground());
          if (style != null)
          {
            pout.print("; ");
            pout.print(style);
          }
          pout.println ("\"></td>");
          printed = true;
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
        pout.print("pt");
        String style = createHtmlBackgroundStyle(gridElement.getBackground());
        if (style != null)
        {
          pout.print("; ");
          pout.print(style);
        }
        pout.print("\" ");

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
        printed = true;
      }

      if (!printed)
      {
        Log.debug ("The Row at " + y + " was not printed");
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
