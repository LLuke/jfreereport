/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
 *
 * (C) Copyright 2000-2003, by Object Refinery Limited and Contributors.
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
 * CSVTableCreator.java
 * ------------------------------
 * (C)opyright 2004, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Object Refinery Limited);
 *
 * $Id: CSVContentCreator.java,v 1.5 2005/05/08 15:41:15 taqua Exp $
 *
 * Changes 
 * -------------------------
 * 01.03.2004 : Initial version
 *  
 */

package org.jfree.report.modules.output.table.csv;

import java.io.PrintWriter;
import java.io.Writer;

import org.jfree.report.ReportDefinition;
import org.jfree.report.content.Content;
import org.jfree.report.content.ContentType;
import org.jfree.report.modules.output.meta.MetaElement;
import org.jfree.report.modules.output.table.base.GenericObjectTable;
import org.jfree.report.modules.output.table.base.RawContent;
import org.jfree.report.modules.output.table.base.SheetLayout;
import org.jfree.report.modules.output.table.base.SheetLayoutCollection;
import org.jfree.report.modules.output.table.base.TableContentCreator;
import org.jfree.report.modules.output.table.base.TableRectangle;
import org.jfree.report.util.CSVQuoter;

public class CSVContentCreator extends TableContentCreator
{
  private PrintWriter writer;
  private boolean open;

  /**
   * The CSVQuoter that is used when writing the content.
   */
  private CSVQuoter quoter;

  public CSVContentCreator
          (final SheetLayoutCollection sheetLayoutCollection, final Writer writer)
  {
    super(sheetLayoutCollection);
    if (writer == null)
    {
      throw new NullPointerException("Writer is null.");
    }
    this.writer = new PrintWriter(writer);
  }

  protected void handleBeginTable (final ReportDefinition reportDefinition)
  {
    // remains empty, as we don't support multiple tables.
  }

  protected void handleEndTable ()
  {
    // remains empty, as we don't support multiple tables.
  }

  protected void handleOpen (final ReportDefinition reportDefinition)
  {
    // remains empty, as we don't support multiple tables.
    // possibly write a header ...
    open = true;
    final String separator =
            reportDefinition.getReportConfiguration().getConfigProperty
            (CSVTableProcessor.CONFIGURATION_PREFIX + "." + CSVTableProcessor.SEPARATOR_KEY,
                    CSVTableProcessor.SEPARATOR_DEFAULT);

    if (separator.length() == 0)
    {
      throw new IllegalArgumentException("CSV separate cannot be an empty string.");
    }
    this.quoter = new CSVQuoter(separator.charAt(0));
  }

  protected void handleClose ()
  {
    // remains empty, as we don't support multiple tables.
    open = false;
  }

  /**
   * Commits all bands. See the class description for details on the flushing process.
   *
   * @return true, if the content was flushed, false otherwise.
   */
  public boolean handleFlush ()
  {
    final GenericObjectTable go = getBackend();
    final SheetLayout layout = getCurrentLayout();

    final int height = go.getRowCount();
    final int width = Math.max(go.getColumnCount(), layout.getColumnCount());
    final int layoutOffset = getLayoutOffset();
    for (int y = layoutOffset; y < height + layoutOffset; y++)
    {
      for (int x = 0; x < width; x++)
      {
        final MetaElement element = (MetaElement) go.getObject(y - layoutOffset, x);
        if (element == null)
        {
          writer.print(quoter.getSeparator());
          continue;
        }

        final TableRectangle rectangle =
                layout.getTableBounds(element, getLookupRectangle());

        if (rectangle.isOrigin(x, y) == false)
        {
          // colspanned cell
          writer.print(quoter.getSeparator());
          continue;
        }

        final Content c = element.getContent();
        if (c.getContentType().equals(ContentType.RAW))
        {
          final RawContent rc = (RawContent) c;
          writer.print(quoter.doQuoting(String.valueOf(rc.getContent())));
        }
        writer.print(quoter.getSeparator());
      }
      writer.println();
    }
    return true;
  }

  /**
   * Checks, whether the report processing has started.
   *
   * @return true, if the report is open, false otherwise.
   */
  public boolean isOpen ()
  {
    return open;
  }
}
