/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
 *
 * (C) Copyright 2000-2003, by Simba Management Limited and Contributors.
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
 * -----------------
 * HtmlProducer.java
 * -----------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: HtmlProducer.java,v 1.32 2003/06/27 14:25:25 taqua Exp $
 *
 * Changes
 * -------
 * 18-Jan-2003 : Initial version
 */
package com.jrefinery.report.targets.table.html;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;

import com.jrefinery.report.function.FunctionProcessingException;
import com.jrefinery.report.targets.table.TableCellBackground;
import com.jrefinery.report.targets.table.TableCellDataFactory;
import com.jrefinery.report.targets.table.TableGridLayout;
import com.jrefinery.report.targets.table.TableGridPosition;
import com.jrefinery.report.targets.table.TableProducer;
import com.jrefinery.report.util.CharacterEntityParser;
import org.jfree.io.IOUtils;

/**
 * The TableProducer is responsible for creating the produced Table. After
 * the writer has finished the band layout process, the layouted bands are
 * forwarded into the TableProducer. The TableProducer coordinates the cell
 * creation process and collects the generated TableCellData. The raw CellData
 * objects are later transformed into a TableGridLayout.
 * <p>
 * The generated HTML code is cached and written after the last cell was created,
 * to insert the StyleSheet into the html header.
 *
 * @author Thomas Morgner
 */
public class HtmlProducer extends TableProducer
{
  /** the printwriter for the main html file. */
  private PrintWriter pout;

  /** the cell data factory used for creating the content cells. */
  private HtmlCellDataFactory cellDataFactory;

  /** the character entity parser converts Strings into the HTML format. */
  private static CharacterEntityParser entityParser;

  /** the style collection is used to create the style sheet and the cell styles. */
  private HtmlStyleCollection styleCollection;

  /** the Filesystem is used to store the main html file and any external content. */
  private HtmlFilesystem filesystem;

  /** a flag indicating whether to use XHTML output. */
  private boolean useXHTML;

  /** the fileencoding for the main html file. */
  public static final String ENCODING = "Encoding";
  /** a default value for the fileencoding of the main html file. */
  public static final String ENCODING_DEFAULT = "UTF-8";

  /** the content cache for the main html file. */
  private ByteArrayOutputStream content;

  /** a flag indicating whether this producer is open. */
  private boolean isOpen;

  /** the standard XHTML document type declaration and header. */
  private static final String[] XHTML_HEADER = {
    "<!DOCTYPE html",
    "     PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\"",
    "     \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">",
    "<html xmlns=\"http://www.w3.org/1999/xhtml\">",
    "<head>"};

  /** the standard HTML4 document type declaration and header. */
  private static final String[] HTML4_HEADER = {
    "<!DOCTYPE HTML ",
    "     PUBLIC \"-//W3C//DTD HTML 4.01//EN\"",
    "     \"http://www.w3.org/TR/html4/strict.dtd\">",
    "<html>",
    "<head>"};


  /**
   * Creates a new HTMLProducer.
   *
   * @param filesystem the filesystem used to store the generated content.
   * @param strict a flag whether to use the strict layout mode.
   * @param useXHTML a flag whether to generate XHTML content.
   */
  public HtmlProducer(final HtmlFilesystem filesystem,
                      final boolean strict,
                      final boolean useXHTML)
  {
    super(strict);
    if (filesystem == null)
    {
      throw new NullPointerException();
    }

    this.filesystem = filesystem;
    this.content = null;
    this.pout = null;
    this.styleCollection = new HtmlStyleCollection();
    this.cellDataFactory = new HtmlCellDataFactory(styleCollection, useXHTML);
    this.useXHTML = useXHTML;
  }

  /**
   * Gets the defined file encoding for the main html file.
   *
   * @return the encoding.
   */
  public String getEncoding()
  {
    return String.valueOf(getProperty(ENCODING, ENCODING_DEFAULT));
  }

  /**
   * Gets the character entity parser for HTML content. The CharacterEntity parser
   * translates known characters into predefined entities.
   *
   * @return the character entity parser instance.
   */
  public static CharacterEntityParser getEntityParser()
  {
    if (entityParser == null)
    {
      entityParser = CharacterEntityParser.createHTMLEntityParser();
    }
    return entityParser;
  }

  /**
   * Starts the report writing and prepares the cached output stream.
   */
  public void open()
  {
    this.content = new ByteArrayOutputStream();
    final DeflaterOutputStream deflaterStream
        = new DeflaterOutputStream(content, new Deflater(Deflater.BEST_COMPRESSION));
    try
    {
      this.pout = new PrintWriter(new OutputStreamWriter
          (deflaterStream, getEncoding()), false);
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
    pout.flush();
    final String title = String.valueOf(getProperty(TITLE, "Untitled report"));
    pout.print(getEntityParser().encodeEntities(title));
    pout.println("</title></head>");
    pout.println("<body>");
    pout.flush();
    isOpen = true;
  }

  /**
   * Closes the target and writes all generated content into the root stream of the
   * filesystem after generating the StyleSheet information.
   */
  public void close()
  {
    try
    {
      pout.println("</body></html>");

      //
      // Creates the stylesheets and the StyleSheet reference.
      //
      final StringBuffer cssbuffer = new StringBuffer();

      final Iterator styles = styleCollection.getDefinedStyles();
      while (styles.hasNext())
      {
        final HtmlCellStyle style = (HtmlCellStyle) styles.next();
        if (styleCollection.isRegistered(style))
        {
          final String name = styleCollection.lookupName(style);
          cssbuffer.append(".");
          cssbuffer.append(name);
          cssbuffer.append(" { ");
          cssbuffer.append(styleCollection.createStyleSheetDefinition(style));
          cssbuffer.append(" } ");
          cssbuffer.append(System.getProperty("line.separator", "\n"));
        }
      }
      final HtmlReferenceData cssRef = filesystem.createCSSReference(cssbuffer.toString());

      final PrintWriter writer = new PrintWriter(filesystem.getRootStream());

      // write the standard headers
      if (useXHTML)
      {
        writer.print("<?xml version=\"1.0\" encoding=\"");
        writer.print(getEncoding());
        writer.println("\"?>");
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
      // write the generated stylesheet ...
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

      // now copy all cached content into the root stream
      pout.flush();
      pout.close();
      final byte[] data = content.toByteArray();
      content.close();
      content = null;
      pout = null;

      final InflaterInputStream infIn
          = new InflaterInputStream(new BufferedInputStream(new ByteArrayInputStream(data)));
      final InputStreamReader inReader = new InputStreamReader(infIn);

      IOUtils.getInstance().copyWriter(inReader, writer);

      inReader.close();
      writer.flush();
      filesystem.close();
    }
    catch (IOException ioe)
    {
      throw new FunctionProcessingException("Failed to write", ioe);
    }

    isOpen = false;
  }

  /**
   * End the page and generate the table for the page.
   */
  public void endPage()
  {
    if (isDummy() == false)
    {
      generatePage(layoutGrid());
    }
    pout.println("</table></p>");
    clearCells();
  }

  /**
   * Start a new page, start a new table.
   *
   * @param name the page name
   */
  public void beginPage(final String name)
  {
    if (name != null)
    {
      pout.println(useXHTML ? "<hr />" : "<hr>");
      pout.println("<h3>");
      pout.println(name);
      pout.println("</h3>");
      pout.println(useXHTML ? "<hr />" : "<hr>");
    }
    pout.println("<p>");
    pout.println("<table width=\"100%\" cellspacing=\"0\" cellpadding=\"0\">");
    //pout.println("<table border=\"2\" width=\"100%\" cellspacing=\"0\" cellpadding=\"0\">");
  }

  /**
   * Gets the TableProducer implementation of this TableProducer.
   *
   * @return the TableProducers TableCellDataFactory, which is used to create
   * the TableCellData.
   */
  public TableCellDataFactory getCellDataFactory()
  {
    return cellDataFactory;
  }

  /**
   * Returns true, if the TableProducer is open. Only open producers
   * are able to write TableCells or to create TableCellData from Elements.
   *
   * @return checks, whether the TableProducer is open.
   */
  public boolean isOpen()
  {
    return isOpen;
  }

  /**
   * Merges the backgrounds and creates the StyleSheet information for
   * the cell background.
   *
   * @param background the (unmerged) background styles.
   * @return the background style sheet definition.
   */
  protected String createHtmlBackgroundStyle(final List background)
  {
    final TableCellBackground bg = createTableCellStyle(background);
    if (bg == null)
    {
      return null;
    }
    return styleCollection.getBackgroundStyle(bg);
  }

  /**
   * Generates and writes the HTML table specified by the given TableGridLayout.
   *
   * @param layout the layouted cells.
   */
  private void generatePage(final TableGridLayout layout)
  {
    pout.println();

    for (int y = 0; y < layout.getHeight(); y++)
    {
      final int lastRowHeight = layout.getRowEnd(y) - layout.getRowStart(y);

      pout.println("<tr style=\"height:" + lastRowHeight + "pt\">");
//      boolean printed = false;
      for (int x = 0; x < layout.getWidth(); x++)
      {
        final TableGridLayout.Element gridElement = layout.getData(x, y);
        // no element defined for the given cell...
        if (gridElement == null)
        {
          final int width = layout.getColumnEnd(x) - layout.getColumnStart(x);
          pout.println("<!-- No Element -->");
          pout.println("<td style=\"width:" + width + "pt\"></td>");
//          printed = true;
          continue;
        }

        // no data cell defined, but there exists a background defintion
        // for that cell.
        final TableGridPosition gridPosition = gridElement.getRoot();
        if (gridPosition == null || gridPosition.isInvalidCell())
        {
          final int width = layout.getColumnEnd(x) - layout.getColumnStart(x);
          if (gridPosition == null)
          {
            pout.println("<!-- gridposition is null -->");
          }
          else
          {
            pout.println("<!-- is invalid cell -->");
          }

          pout.print("<td style=\"width:");
          pout.print(width);
          pout.print("pt");

          final String style = createHtmlBackgroundStyle(gridElement.getBackground());
          if (style != null)
          {
            pout.print("; ");
            pout.print(style);
          }
          pout.println("\"></td>");
//          printed = true;
          continue;
        }

        // a spanned field
        if (gridPosition.isOrigin(x, y) == false)
        {
          // this is a spanned field.
          continue;
        }

        // real data ...
        final HtmlCellData cellData = (HtmlCellData) gridPosition.getElement();

        pout.print("    <td style=\"width:");
        pout.print((int) gridPosition.getBounds().getWidth());
        pout.print("pt");
        pout.print("; height:");
        pout.print((int) gridPosition.getBounds().getHeight());
        pout.print("pt");
        final String style = createHtmlBackgroundStyle(gridElement.getBackground());
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
//        printed = true;
      }
/*
      if (!printed)
      {
        //Log.debug ("The Row at " + y + " was not printed");
      }
*/
      pout.println("</tr>");
    }
  }

  /**
   * Configures the table producer by reading the configuration settings from
   * the given map.
   *
   * @param configuration the configuration supplied by the table processor.
   */
  public void configure(final Properties configuration)
  {
    setProperty(ENCODING, configuration.getProperty(ENCODING, ENCODING_DEFAULT));
  }
}
