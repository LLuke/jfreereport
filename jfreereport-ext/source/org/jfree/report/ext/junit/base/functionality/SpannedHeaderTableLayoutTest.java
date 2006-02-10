/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2005, by Object Refinery Limited and Contributors.
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
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * ------------
 * SpannedHeaderTableLayoutTest.java
 * ------------
 * (C) Copyright 2002-2005, by Object Refinery Limited.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: SpannedHeaderTableLayoutTest.java,v 1.3 2005/09/07 11:24:09 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.report.ext.junit.base.functionality;

import java.net.URL;

import junit.framework.TestCase;
import org.jfree.report.JFreeReport;
import org.jfree.report.ReportDefinition;
import org.jfree.report.ReportProcessingException;
import org.jfree.report.modules.output.meta.MetaBandProducer;
import org.jfree.report.modules.output.meta.MetaElement;
import org.jfree.report.modules.output.table.base.SheetLayout;
import org.jfree.report.modules.output.table.base.SheetLayoutCollection;
import org.jfree.report.modules.output.table.base.TableContentCreator;
import org.jfree.report.modules.output.table.base.TableCreator;
import org.jfree.report.modules.output.table.base.TableProcessor;
import org.jfree.report.modules.output.table.html.HtmlMetaBandProducer;
import org.jfree.report.modules.parser.base.ReportGenerator;
import org.jfree.util.Log;
import org.jfree.util.ObjectUtilities;

public class SpannedHeaderTableLayoutTest extends TestCase
{
  private static class TestTableContentCreator extends TableContentCreator
  {
    private boolean open;

    public TestTableContentCreator (final SheetLayoutCollection sheetLayoutCollection)
    {
      super(sheetLayoutCollection);
    }

    protected void handleBeginTable (final ReportDefinition reportDefinition)
            throws ReportProcessingException
    {
      assertTrue("Open @BeginTable", open);
    }

    protected void handleClose ()
            throws ReportProcessingException
    {
      open = false;
    }

    protected void handleEndTable ()
            throws ReportProcessingException
    {
      assertTrue("Open @EndTable", open);
      // check the layout ...
      final SheetLayout layout = getCurrentLayout();
      //layout.getColumnCount();
      final int columns = layout.getColumnCount();
      assertEquals(5, columns);
      assertEquals(100000, layout.getCellWidth(0, 1));
      assertEquals(100000, layout.getCellWidth(1, 2));
      assertEquals(100000, layout.getCellWidth(2, 3));
      assertEquals(100000, layout.getCellWidth(3, 4));
      // using A4 with a total width of 451pt
      assertEquals(51000, layout.getCellWidth(4, 5));
      Log.debug ("Validated number of columns .. OK");
    }

    /**
     * Add the specified element to the logical page. Create content from the values
     * contained in the element and format the content by using the element's attributes.
     *
     * @param e the element.
     * @throws NullPointerException if the element has no valid layout (no BOUNDS defined).
     *                              Bounds are usually defined by the BandLayoutManager.
     */
    protected void processElement (final MetaElement e)
    {
      super.processElement(e);
    }

    protected void handleOpen (final ReportDefinition reportDefinition)
            throws ReportProcessingException
    {
      open = true;
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

  private static class TestTableProcessor extends TableProcessor
  {
    public TestTableProcessor (final JFreeReport report)
            throws ReportProcessingException
    {
      super(report);
      init();
    }


    protected TableCreator createContentCreator ()
    {
      return new TestTableContentCreator(getLayoutCreator().getSheetLayoutCollection());
    }

    protected MetaBandProducer createMetaBandProducer ()
    {
      return new HtmlMetaBandProducer(true, true, false);
    }

    protected String getExportDescription()
    {
      return "table/test";
    }

    /**
     * Gets the report configuration prefix for that processor. This prefix defines how to
     * map the property names into the global report configuration.
     *
     * @return the report configuration prefix.
     */
    protected String getReportConfigurationPrefix ()
    {
      return "org.jfree.report.junit.tabletest.";
    }
  }

  public SpannedHeaderTableLayoutTest (final String s)
  {
    super(s);
  }


  public JFreeReport getReport() throws Exception
  {
    final URL url = ObjectUtilities.getResourceRelative
            ("resources/spanned-header.xml", SpannedHeaderTableLayoutTest.class);
    assertNotNull(url);
    final JFreeReport report = ReportGenerator.getInstance().parseReport(url);
    return report;
  }

  public void testLayout ()
          throws Exception
  {
    final JFreeReport report = getReport();
    final TestTableProcessor proc = new TestTableProcessor(report);
    proc.processReport();
  }
}
