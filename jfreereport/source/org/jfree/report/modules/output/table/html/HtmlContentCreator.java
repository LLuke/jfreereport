/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
 *
 * (C) Copyright 2000-2004, by Object Refinery Limited and Contributors.
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
 * Contributor(s):   David Gilbert (for Object Refinery Limited);
 *
 * $Id: HtmlContentCreator.java,v 1.26 2005/09/07 14:25:11 taqua Exp $
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
import java.util.Map;
import java.util.TreeMap;

import org.jfree.report.Anchor;
import org.jfree.report.ElementAlignment;
import org.jfree.report.JFreeReport;
import org.jfree.report.ReportDefinition;
import org.jfree.report.function.FunctionProcessingException;
import org.jfree.report.modules.output.meta.MetaElement;
import org.jfree.report.modules.output.table.base.GenericObjectTable;
import org.jfree.report.modules.output.table.base.SheetLayoutCollection;
import org.jfree.report.modules.output.table.base.TableCellBackground;
import org.jfree.report.modules.output.table.base.TableContentCreator;
import org.jfree.report.modules.output.table.base.TableRectangle;
import org.jfree.report.modules.output.table.html.metaelements.HtmlMetaElement;
import org.jfree.report.modules.output.table.html.ref.HtmlReference;
import org.jfree.report.modules.output.table.html.util.HtmlCharacterEntities;
import org.jfree.report.modules.output.table.html.util.HtmlEncoderUtil;
import org.jfree.report.style.ElementStyleSheet;
import org.jfree.report.util.geom.StrictGeomUtility;
import org.jfree.util.Configuration;

public class HtmlContentCreator extends TableContentCreator
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
  private boolean emptyCellsUseCSS;
  private PrintWriter pout;
  private HtmlStyleCollection styleCollection;
  private boolean tableRowBorderDefinition;
  private boolean proportionalColumnWidths;

  public HtmlContentCreator
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
  }

  /**
   * Checks, whether the report processing has started.
   *
   * @return true, if the report is open, false otherwise.
   */
  public boolean isOpen()
  {
    return isOpen;
  }

  public boolean isUseXHTML()
  {
    return useXHTML;
  }


  /**
   * Creates the global Cascading Stylesheet definition for the report.
   *
   * @param report
   * @return the global stylesheet as html reference.
   * @throws IOException if an error occured.
   */
  private HtmlReference buildGlobalStyleSheet(final ReportDefinition report)
          throws IOException
  {
    //
    // Creates the stylesheets and the StyleSheet reference.
    //
    final StringWriter cssbuffer = new StringWriter();
    final PrintWriter csswriter = new PrintWriter(cssbuffer);
    final TreeMap stylesSorted = styleCollection.getSortedStyleMap();
    final Iterator styles = stylesSorted.entrySet().iterator();
    while (styles.hasNext())
    {
      final String styleHeader = report.getReportConfiguration()
              .getConfigProperty
                      ("org.jfree.report.modules.output.table.html.StyleSheetHeader");
      if (styleHeader != null)
      {
        csswriter.println(styleHeader);
      }
      final Map.Entry entry = (Map.Entry) styles.next();
      final HtmlStyle style = (HtmlStyle) entry.getValue();
      final String name = (String) entry.getKey();
      csswriter.print(name);
      csswriter.println(" {");
      csswriter.println(style.getCSSString(HtmlStyle.EXTERNAL));
      csswriter.println("}");
      csswriter.println();
    }
    return filesystem.createCSSReference(cssbuffer.toString());
  }

  protected void handleBeginTable(final ReportDefinition reportDefinition)
  {
    String sheetName = null;
    if (getSheetNameFunction() != null)
    {
      sheetName = String.valueOf(reportDefinition.getDataRow().get(
              getSheetNameFunction()));
    }
    if (sheetName != null)
    {
      pout.println(isUseXHTML() ? "<hr />" : "<hr>");
      pout.println("<h3>");
      pout.println(sheetName);
      pout.println("</h3>");
      pout.println(isUseXHTML() ? "<hr />" : "<hr>");
    }

    final HtmlSheetLayout layout = (HtmlSheetLayout) getCurrentLayout();
    final int noc = layout.getColumnCount();
    String style;
    if ((noc > 0) && (proportionalColumnWidths == false))
    {
      final int width = (int)
              StrictGeomUtility.toExternalValue(layout.getCellWidth(0, noc));
      style = "width: " + width + "pt;";
    }
    else
    {
      // Consume the complete width for proportional column widths
      style = "width: 100%;";
    }

    // style += "table-layout: fixed;";
    if (tableRowBorderDefinition)
    {
      style += "border-collapse: collapse;";
    }
    if (emptyCellsUseCSS)
    {
      style += "empty-cells: show";
    }

    final String styleClass = reportDefinition.getReportConfiguration()
            .getConfigProperty
                    ("org.jfree.report.modules.output.table.html.StyleClass");

    pout.print("<table cellspacing=\"0\" cellpadding=\"0\" style=\"");
    pout.print(style);
    if (styleClass != null)
    {
      pout.println("\" ");
      pout.print(styleClass);
    }
    pout.println("\">");
    pout.print("<colgroup span=\"");
    pout.print(noc);
    pout.println("\">");
    final int fullWidth = (int)
            StrictGeomUtility.toExternalValue(layout.getCellWidth(0, noc));

    for (int i = 0; i < noc; i++)
    {
      final int width = (int)
              StrictGeomUtility.toExternalValue(layout.getCellWidth(i, i + 1));
      pout.print("<col style=\"");
      pout.print("width:");
      if (proportionalColumnWidths == true)
      {
        final double colWidth = width * 100d / fullWidth;
        pout.print(colWidth);
        if (colWidth > 0)
        {
          pout.print("%");
        }
      }
      else
      {
        pout.print(width);
        pout.print("pt");
      }
      if (isUseXHTML())
      {
        pout.println("\" />");
      }
      else
      {
        pout.println("\">");
      }
    }
    pout.println("</colgroup>");
  }

  protected void handleEndTable()
  {
    pout.println("</table>");
  }

  /**
   * Starts the report processing. This method is called only once per report
   * processing. The TableCreator might use the report definition to configure
   * itself and to perform startup operations.
   *
   * @param report the report definition.
   */
  public void handleOpen(final ReportDefinition report)
  {
    isOpen = true;
    final Configuration config = report.getReportConfiguration();
    createBodyFragment = config.getConfigProperty
            (HtmlProcessor.CONFIGURATION_PREFIX + "." +
                    HtmlProcessor.BODY_FRAGMENT, "false").equals("true");
    emptyCellsUseCSS = config.getConfigProperty
            (HtmlProcessor.CONFIGURATION_PREFIX + "." +
                    HtmlProcessor.EMPTY_CELLS_USE_CSS, "false").equals("true");
    tableRowBorderDefinition = config.getConfigProperty
            (HtmlProcessor.CONFIGURATION_PREFIX + "." +
                    HtmlProcessor.TABLE_ROW_BORDER_DEFINITION, "false").equals(
            "true");
    proportionalColumnWidths = config.getConfigProperty
            (HtmlProcessor.CONFIGURATION_PREFIX + "." +
                    HtmlProcessor.PROPORTIONAL_COLUMN_WIDTHS, "false").equals(
            "true");
    final String encoding = config.getConfigProperty
            (HtmlProcessor.CONFIGURATION_PREFIX + "." +
                    HtmlProcessor.ENCODING, HtmlProcessor.ENCODING_DEFAULT);
    final String title = config.getConfigProperty
            (HtmlProcessor.CONFIGURATION_PREFIX + "." +
                    HtmlProcessor.TITLE);

    try
    {
      this.pout = new PrintWriter(new OutputStreamWriter
              (filesystem.getRootStream(), encoding), false);
      if (createBodyFragment == false)
      {
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
        pout.print(
                "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=");
        pout.print(encoding);
        if (isUseXHTML())
        {
          pout.println("\" />");
        }
        else
        {
          pout.println("\">");
        }

        pout.print("<title>");
        if (title != null)
        {
          pout.print(HtmlCharacterEntities.getEntityParser().encodeEntities(
                  title));
        }
        else
        {
          pout.print(report.getProperties().get(JFreeReport.NAME_PROPERTY,
                  "<unnamed>"));
        }
        pout.println("</title>");

        final HtmlReference cssRef = buildGlobalStyleSheet(report);
        // write the generated stylesheet ...
        // is a href type ...
        if (cssRef.isExternal())
        {
          pout.print("<link rel=\"stylesheet\" type=\"text/css\" href=\"");
          pout.print(cssRef.getReferenceData());
          if (isUseXHTML())
          {
            pout.println("\" />");
          }
          else
          {
            pout.println("\">");
          }
        }
        else
        {
          pout.println("<style type=\"text/css\">");
          pout.print(cssRef.getReferenceData());
          pout.println("</style>");
        }

        pout.println("</head>");
        pout.println("<body>");
      }
    }
    catch (IOException ioe)
    {
      throw new FunctionProcessingException
              ("Failed to create the writer or write the header.", ioe);
    }
  }


  /** Closes the report processing. */
  public void handleClose()
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

  public boolean isCreateBodyFragment()
  {
    return createBodyFragment;
  }

  /**
   * Commits all bands. See the class description for details on the flushing
   * process.
   *
   * @return true, if the content was flushed, false otherwise.
   */
  public boolean handleFlush()
  {
    final HtmlSheetLayout layout = (HtmlSheetLayout) getCurrentLayout();
    final GenericObjectTable go = getBackend();
    final int height = go.getRowCount();
    final int width = Math.max(go.getColumnCount(), layout.getColumnCount());

    final int layoutOffset = getLayoutOffset();
    for (int y = layoutOffset; y < layoutOffset + height; y++)
    {
      // start a new row ...
      // first we check, whether this style is already collected.
      // if so, then we reuse that collected style
      printRowStart(y, width);

      for (int x = 0; x < width; x++)
      {
        final MetaElement element =
                (MetaElement) go.getObject(y - layoutOffset, x);
        if (element == null)
        {
          printEmptyCell(x, y);
          continue;
        }

        final TableRectangle rectangle =
                layout.getTableBounds(element, getLookupRectangle());
        if (rectangle.isOrigin(x, y) == false)
        {
          if (isDebugReportLayout())
          {
            // this is a spanned cell - ignore it completly
            pout.println(
                    "<!-- Spanned cell @(" + x + ", " + y + ") ignored. -->");
          }
          continue;
        }
        if (isDebugReportLayout())
        {
          // this is a spanned cell - ignore it completly
          pout.println(
                  "<!-- cell @(" + x + ", " + y + ") [" + rectangle + "] -->");
        }
        printContentCellStart(rectangle, x, y,
                (ElementAlignment) element.getProperty(
                        ElementStyleSheet.VALIGNMENT));

        final String internalStyleName = layout.getContentStyleAt(y, x);
        final HtmlStyle style = layout.getStyleCollection().lookupStyle(
                internalStyleName);
        final String cellStyleName = layout.getStyleCollection().getPublicName(
                style);


        if (cellStyleName != null && (isCreateBodyFragment() == false))
        {
          pout.print("<div class=\"");
          pout.print(cellStyleName);
          pout.print("\">");
        }
        else if (style != null)
        {
          pout.print("<div style=\"");
          pout.print(style.getCSSString(HtmlStyle.INLINE));
          pout.print("\">");
        }
        else
        {
          pout.print("<div>");
        }

        final String hrefTarget = (String) element.getProperty(
                ElementStyleSheet.HREF_TARGET);
        if (hrefTarget != null)
        {
          pout.print("<a href=\"");
          pout.print(hrefTarget);
          pout.print("\">");
        }

        if (element instanceof HtmlMetaElement)
        {
          final HtmlMetaElement htmlElement = (HtmlMetaElement) element;
          htmlElement.write(pout, filesystem, emptyCellsUseCSS);
        }
        else if (isDebugReportLayout())
        {
          pout.println(
                  "<!-- Invalid element @(" + x + ", " + y + ") -->&nbsp;");
        }

        if (hrefTarget != null)
        {
          pout.println("</a>");
        }

        pout.println("</div>");
        pout.println("</td>");
      }

      pout.println("</tr>");
    }
    return true;
  }

  /**
   * Prints the table data cell definition for a content cell.
   *
   * @param rectangle        the cell's rectangle in the table grid
   * @param x                the cell's x coordinate
   * @param y                the cell's y coordinate
   * @param elementAlignment
   */
  private void printContentCellStart(final TableRectangle rectangle,
                                     final int x,
                                     final int y,
                                     final ElementAlignment elementAlignment)
  {
    final HtmlSheetLayout layout = (HtmlSheetLayout) getCurrentLayout();
    // now, finally we got a printable data cell.
    // lets do some work!
    if (isDebugReportLayout())
    {
      pout.println("<!-- content cell @(" + x + "," + y + ") -->");
    }

    pout.print("<td ");
    if (rectangle.getRowSpan() > 1)
    {
      pout.print("rowspan=\"");
      pout.print(rectangle.getRowSpan());
      pout.print("\" ");
    }
    if (rectangle.getColumnSpan() > 1)
    {
      pout.print("colspan=\"");
      pout.print(rectangle.getColumnSpan());
      pout.print("\" ");
    }

    final String internalStyleName = layout.getBackgroundStyleAt(y, x);
    final TableCellBackground background = layout.getRegionBackground(
            rectangle);

    // first, check, whether we have a style ..
    // if not, then create one for the current background
    // (which can be null, if there is no background defined).
    HtmlStyle style;
    if (background == null)
    {
      style = layout.getStyleCollection().getEmptyCellStyle();
    }
    else
    {
      style = layout.getStyleCollection().lookupStyle(internalStyleName);
      if (style == null)
      {
        style = new HtmlTableCellStyle(background, elementAlignment);
      }
    }
    // now check, whether an equal style is already stored.
    // if so, then reference that style instead of printing the
    // whole stuff over and over again ..
//    final int width = (int) StrictGeomUtility.toExternalValue
//            (layout.getCellWidth(rectangle.getX1(), rectangle.getX2()));

    final String cellStyleName =
            layout.getStyleCollection().getPublicName(style);
    if (cellStyleName != null && (isCreateBodyFragment() == false))
    {
      pout.print("class=\"");
      pout.print(cellStyleName);
//      pout.print("\" style=\"");
//      pout.print("width: ");
//      pout.print(width);
//      pout.print("pt");
      pout.println("\">");
    }
    else
    {
      pout.print("style=\"");
//      pout.print("width: ");
//      pout.print(width);
//      pout.print("pt;");
      pout.print(style.getCSSString(HtmlStyle.INLINE));
      pout.println("\">");
    }
    printAnchors(background);
  }

  /**
   * Prints a table row definition for the given height.
   *
   * @param y the current row.
   */
  private void printRowStart(final int y, final int tableWidth)
  {
    final HtmlSheetLayout layout = (HtmlSheetLayout) getCurrentLayout();
    final int lastRowHeight = (int)
            StrictGeomUtility.toExternalValue(layout.getRowHeight(y));
    final TableRectangle rect = new TableRectangle(0, tableWidth, y, y + 1);
    final TableCellBackground regionStyle = layout.getRegionBackground(rect);
    final HtmlTableRowStyle rowStyle;
    if (regionStyle == null)
    {
      rowStyle = new HtmlTableRowStyle(lastRowHeight, null,
              tableRowBorderDefinition);
    }
    else
    {
      rowStyle = new HtmlTableRowStyle(lastRowHeight, regionStyle.getColor(),
              tableRowBorderDefinition);
      rowStyle.setBorderTop(regionStyle.getColorTop(),
              regionStyle.getBorderSizeTop());
      rowStyle.setBorderBottom(regionStyle.getColorBottom(),
              regionStyle.getBorderSizeBottom());
    }


    final String trStyleClass = styleCollection.getPublicName(rowStyle);
    if (trStyleClass != null && (isCreateBodyFragment() == false))
    {
      pout.print("<tr class=\"");
      pout.print(trStyleClass);
      pout.println("\">");
    }
    else
    {
      pout.print("<tr style=\"");
      pout.print(rowStyle.getCSSString(HtmlStyle.INLINE));
      pout.println("\">");
    }
  }

  /**
   * Prints an empty cell for the given grid position.
   *
   * @param x the cell column
   * @param y the cell row.
   */
  private void printEmptyCell(final int x, final int y)
  {
    final HtmlSheetLayout layout = (HtmlSheetLayout) getCurrentLayout();
    final TableCellBackground background = layout.getElementAt(y, x);

    final String internalStyleName = layout.getBackgroundStyleAt(y, x);
    HtmlStyle style = layout.getStyleCollection().lookupStyle(
            internalStyleName);

    // empty cell - we still have to check for the background
    // first, check, whether we have a style ..
    // if not, then create one for the current background
    // (which can be null, if there is no background defined).
    if (style == null)
    {
      style = new HtmlTableCellStyle(background, ElementAlignment.TOP);
    }
    // now check, whether an equal style is already stored.
    // if so, then reference that style instead of printing the
    // whole stuff over and over again ..
    final String cellStyleName =
            layout.getStyleCollection().getPublicName(style);

    if (isDebugReportLayout())
    {
      pout.println("<!-- empty cell @(" + x + "," + y + ")-->");
    }

    if (cellStyleName != null && (isCreateBodyFragment() == false))
    {
      pout.print("<td class=\"");
      pout.print(cellStyleName);
    }
    else
    {
      pout.print("<td style=\"");
      pout.print(style.getCSSString(HtmlStyle.INLINE));
    }
    pout.println("\">");

    printAnchors(background);
    if (emptyCellsUseCSS == false)
    {
      pout.println("&nbsp;</td>");
    }
    else
    {
      pout.println("</td>");
    }
  }

  private boolean printAnchors(final TableCellBackground bg)
  {
    if (bg == null)
    {
      return false;
    }

    final Anchor[] anchors = bg.getAnchors();
    for (int i = 0; i < anchors.length; i++)
    {
      pout.print("<a name=\"");
      HtmlEncoderUtil.printText(pout, anchors[i].getName(), isUseXHTML());
      if (isUseXHTML())
      {
        pout.println("\" />");
      }
      else
      {
        pout.println("\" >");
      }
    }
    return anchors.length != 0;
  }

  protected PrintWriter getPrintWriter()
  {
    return pout;
  }
}
