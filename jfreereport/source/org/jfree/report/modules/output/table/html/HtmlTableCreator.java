/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
 *
 * (C) Copyright 2000-2004, by Simba Management Limited and Contributors.
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
 * ------------------------------
 * HtmlTableCreator.java
 * ------------------------------
 * (C)opyright 2004, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id$
 *
 * Changes 
 * -------------------------
 * Mar 13, 2004 : Initial version
 *  
 */

package org.jfree.report.modules.output.table.html;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Iterator;

import org.jfree.report.ReportDefinition;
import org.jfree.report.function.FunctionProcessingException;
import org.jfree.report.modules.output.table.base.SheetLayoutCollection;
import org.jfree.report.modules.output.table.base.TableContentCreator;
import org.jfree.report.modules.output.table.base.GenericObjectTable;
import org.jfree.report.modules.output.table.base.TableRectangle;
import org.jfree.report.modules.output.table.base.TableCellBackground;
import org.jfree.report.modules.output.table.html.ref.HtmlReference;
import org.jfree.report.modules.output.table.html.util.HtmlCharacterEntities;
import org.jfree.report.modules.output.table.html.metaelements.HtmlMetaElement;
import org.jfree.report.modules.output.meta.MetaElement;
import org.jfree.report.util.ReportConfiguration;

public class HtmlTableCreator extends TableContentCreator
{

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

  private HtmlFilesystem filesystem;
  private boolean useXHTML;
  private boolean isOpen;
  private boolean createBodyFragment;
  private PrintWriter pout;
  private HtmlStyleCollection styleCollection;
  private boolean debug;

  public HtmlTableCreator
          (final HtmlFilesystem filesystem, final boolean useXHTML,
           final HtmlStyleCollection styles,
           final SheetLayoutCollection sheetLayoutCollection)
  {
    super(sheetLayoutCollection);
    if (filesystem == null)
    {
      throw new NullPointerException();
    }
    if (sheetLayoutCollection == null)
    {
      throw new NullPointerException();
    }
    if (styles == null)
    {
      throw new NullPointerException();
    }
    this.filesystem = filesystem;
    this.useXHTML = useXHTML;
    this.styleCollection = styles;
    this.debug = true;
  }

  /**
   * Checks, whether the report processing has started.
   *
   * @return true, if the report is open, false otherwise.
   */
  public boolean isOpen ()
  {
    return isOpen;
  }

  public boolean isUseXHTML ()
  {
    return useXHTML;
  }


  /**
   * Creates the global Cascading Stylesheet definition for the
   * report.
   *
   * @return the global stylesheet as html reference.
   * @throws IOException if an error occured.
   */
  private HtmlReference buildGlobalStyleSheet() throws IOException
  {
    //
    // Creates the stylesheets and the StyleSheet reference.
    //
    final StringWriter cssbuffer = new StringWriter();
    final PrintWriter csswriter = new PrintWriter(cssbuffer);

    final Iterator styles = styleCollection.getDefinedStyles();
    while (styles.hasNext())
    {
      final HtmlContentStyle style = (HtmlContentStyle) styles.next();
      if (styleCollection.isRegistered(style))
      {
        final String name = styleCollection.lookupName(style);
        csswriter.print(".");
        csswriter.print(name);
        csswriter.println(" { ");
        csswriter.println(style.getCSSString());
        csswriter.println(" } ");
        csswriter.println();
      }
    }
    return filesystem.createCSSReference(cssbuffer.toString());
  }

  protected void handleBeginTable (final ReportDefinition reportDefinition)
  {
    String sheetName = null;
    if (getSheetNameFunction() != null)
    {
      sheetName = String.valueOf(reportDefinition.getDataRow().get(getSheetNameFunction()));
    }
    if (sheetName != null)
    {
      pout.println(isUseXHTML() ? "<hr />" : "<hr>");
      pout.println("<h3>");
      pout.println(sheetName);
      pout.println("</h3>");
      pout.println(isUseXHTML() ? "<hr />" : "<hr>");
    }
    pout.println("<p>");
    pout.println("<table cellspacing=\"0\" cellpadding=\"0\">");
  }

  protected void handleEndTable ()
  {
    pout.println("</table></p>");
  }

  /**
   * Starts the report processing. This method is called only once per report processing.
   * The TableCreator might use the report definition to configure itself and to perform
   * startup operations.
   *
   * @param report the report definition.
   */
  public void handleOpen (final ReportDefinition report)
  {
    isOpen = true;
    final ReportConfiguration config = report.getReportConfiguration();
    createBodyFragment = config.getConfigProperty
            (HtmlProcessor.CONFIGURATION_PREFIX + "." +
            HtmlProcessor.BODY_FRAGMENT, "false").equals("true");
    final String encoding = config.getConfigProperty
            (HtmlProcessor.CONFIGURATION_PREFIX + "." +
            HtmlProcessor.ENCODING, HtmlProcessor.ENCODING_DEFAULT);
    final String title = config.getConfigProperty
            (HtmlProcessor.CONFIGURATION_PREFIX + "." +
            HtmlProcessor.TITLE);

    if (createBodyFragment == false)
    {
      try
      {
        this.pout = new PrintWriter(new OutputStreamWriter
            (filesystem.getRootStream(), encoding), false);
        // write the standard headers
        if (isUseXHTML())
        {
          pout.print("<?xml version=\"1.0\" encoding=\"");
          pout.print(encoding);
          pout.println("\"?>");
          // now finish the style sheet definition
          for (int i = 0; i < XHTML_HEADER.length; i++)
          {
            pout.println(XHTML_HEADER[i]);
          }
        }
        else
        {
          // now finish the style sheet definition
          for (int i = 0; i < HTML4_HEADER.length; i++)
          {
            pout.println(HTML4_HEADER[i]);
          }
        }


        // the style sheet definition will be inserted right before the content is written ...
        pout.print("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=");
        pout.print(encoding);
        if (isUseXHTML())
        {
          pout.println("\" />");
        }
        else
        {
          pout.println("\">");
        }

        if (title != null)
        {
          pout.print("<title>");
          pout.print(HtmlCharacterEntities.getEntityParser().encodeEntities(title));
          pout.println("</title>");
        }

        final HtmlReference cssRef = buildGlobalStyleSheet();
        // write the generated stylesheet ...
        // is a href type ...
        if (cssRef.isExternal())
        {
          pout.print("<link rel=\"stylesheet\" type=\"text/css\" ");
          pout.print(cssRef.getReferenceData());
          if (isUseXHTML())
          {
            pout.println(" />");
          }
          else
          {
            pout.println(">");
          }
        }
        else
        {
          pout.println("<style type=\"text/css\">");
          pout.print(cssRef.getReferenceData());
          pout.println("</style>");
        }

        pout.println("</html>");
        pout.println("<body>");
      }
      catch (IOException ioe)
      {
        throw new FunctionProcessingException
            ("Failed to create the writer or write the header.", ioe);
      }
    }
  }


  /**
   * Closes the report processing.
   */
  public void handleClose ()
  {
    try
    {
      if (createBodyFragment == false)
      {
        pout.println("</body>");
        pout.println("</html>");
      }
      pout.flush();
      filesystem.close();
    }
    catch (IOException ioe)
    {
      throw new FunctionProcessingException("Failed to write on close.", ioe);
    }

    isOpen = false;
  }

  public boolean isCreateBodyFragment ()
  {
    return createBodyFragment;
  }

  /**
   * Commits all bands. See the class description for details on the flushing process.
   *
   * @return true, if the content was flushed, false otherwise.
   */
  public boolean flush ()
  {
    final HtmlSheetLayout layout = (HtmlSheetLayout) getCurrentLayout();
    final GenericObjectTable go = getBackend();

    final int height = go.getRowCount();
    final int width = go.getColumnCount();

    for (int y = 0; y < height; y++)
    {
      // start a new row ...
      // first we check, whether this style is already collected.
      // if so, then we reuse that collected style
      final int lastRowHeight = (int) layout.getRowHeight(y);
      printRowStart(lastRowHeight);

      for (int x = 0; x < width; x++)
      {

        final MetaElement element = (MetaElement) go.getObject(y, x);
        if (element == null)
        {
          printEmptyCell(x, y);
          continue;
        }

        final TableRectangle rectangle =
                layout.getTableBounds(element, getLookupRectangle());
        if (rectangle.isOrigin(x, y) == false)
        {
          // this is a spanned cell - ignore it completly
          continue;
        }
        printContentCellStart(rectangle, x, y);

        final String cellStyleName = layout.getContentStyleAt(y, x);
        final HtmlStyle style = layout.getStyleCollection().lookupStyle(cellStyleName);

        if (cellStyleName != null)
        {
          pout.print("<div class=\"");
          pout.print(cellStyleName);
          pout.print("\">");
        }
        else if (style != null)
        {
          pout.print("<div style=\"");
          pout.print(style.getCSSString());
          pout.print("\">");
        }
        else
        {
          pout.print("<div>");
        }
        if (element instanceof HtmlMetaElement)
        {
          final HtmlMetaElement htmlElement = (HtmlMetaElement) element;
          htmlElement.write(pout, filesystem);
        }
        else
        {
          pout.println ("<!-- Invalid element @(" + x + ", " + y + ") -->&nbsp;");
        }
        pout.println("</div></td>");
      }

      pout.println("</tr>");
    }
    
    go.clear();
    return true;
  }

  /**
   * Prints the table data cell definition for a content cell.
   *
   * @param rectangle the cell's rectangle in the table grid
   * @param x the cell's x coordinate
   * @param y the cell's y coordinate
   */
  private void printContentCellStart (final TableRectangle rectangle,
                                 final int x, final int y)
  {
    final HtmlSheetLayout layout = (HtmlSheetLayout) getCurrentLayout();
    // now, finally we got a printable data cell.
    // lets do some work!
    pout.print("<td ");
    if (rectangle.getRowSpan() > 1)
    {
      pout.print ("rowspan=\"");
      pout.print (rectangle.getRowSpan());
      pout.print ("\" ");
    }
    if (rectangle.getColumnSpan() > 1)
    {
      pout.print ("colspan=\"");
      pout.print (rectangle.getColumnSpan());
      pout.print ("\" ");
    }

    String cellStyleName = layout.getBackgroundStyleAt(y, x);
    HtmlStyle style = layout.getStyleCollection().lookupStyle(cellStyleName);

    // first, check, whether we have a style ..
    // if not, then create one for the current background
    // (which can be null, if there is no background defined).
    final TableCellBackground background = layout.getElementAt(y, x);
    if (style == null)
    {
      final int cellWidth = (int) layout.getCellWidth(x, x + 1);
      style = new HtmlTableCellStyle(background, cellWidth);
    }
    // now check, whether an equal style is already stored.
    // if so, then reference that style instead of printing the
    // whole stuff over and over again ..
    if (cellStyleName == null)
    {
      cellStyleName = layout.getStyleCollection().lookupName(style);
    }

    if (debug)
    {
      pout.println("<!-- content cell @(" + x + "," + y +") -->");
    }
    if (cellStyleName != null)
    {
      pout.print("class=\"");
      pout.print(cellStyleName);
      pout.println("\">");
    }
    else
    {
      pout.print("style=\"");
      pout.print(style.getCSSString());
      pout.println("\">");
    }
  }

  /**
   * Prints a table row definition for the given height.
   *
   * @param lastRowHeight the height.
   */
  private void printRowStart (final int lastRowHeight)
  {
    final HtmlTableRowStyle rowStyle = new HtmlTableRowStyle(lastRowHeight);

    final String trStyleClass = styleCollection.lookupName(rowStyle);
    if (trStyleClass == null || isCreateBodyFragment())
    {
      pout.print("<tr style=\"");
      pout.print(rowStyle.getCSSString());
      pout.println("\">");
    }
    else
    {
      pout.print("<tr class=\"");
      pout.print(trStyleClass);
      pout.println("\">");
    }
  }

  /**
   * Prints an empty cell for the given grid position.
   *
   * @param x the cell column
   * @param y the cell row.
   */
  private void printEmptyCell (final int x, final int y)
  {
    final HtmlSheetLayout layout = (HtmlSheetLayout) getCurrentLayout();
    final TableCellBackground background = layout.getElementAt(y, x);

    String cellStyleName = layout.getBackgroundStyleAt(y, x);
    HtmlStyle style = layout.getStyleCollection().lookupStyle(cellStyleName);

    // empty cell - we still have to check for the background
    // first, check, whether we have a style ..
    // if not, then create one for the current background
    // (which can be null, if there is no background defined).
    if (style == null)
    {
      final int cellWidth = (int) layout.getCellWidth(x, x + 1);
      style = new HtmlTableCellStyle(background, cellWidth);
    }
    // now check, whether an equal style is already stored.
    // if so, then reference that style instead of printing the
    // whole stuff over and over again ..
    if (cellStyleName == null)
    {
      cellStyleName = layout.getStyleCollection().lookupName(style);
    }

    if (debug)
    {
      pout.println("<!-- empty cell @(" + x + "," + y +")-->");
    }

    if (cellStyleName != null)
    {
      pout.print("<td class=\"");
      pout.print(cellStyleName);
      pout.println("\">&nbsp;</td>");
    }
    else
    {
      pout.print("<td style=\"");
      pout.print(style.getCSSString());
      pout.println("\">&nbsp;</td>");
    }
  }

}
