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
import org.jfree.report.modules.output.table.base.TableCellBackground;
import org.jfree.report.modules.output.table.html.HtmlMetaBandProducer;
import org.jfree.report.modules.parser.base.ReportGenerator;
import org.jfree.util.Log;
import org.jfree.util.ObjectUtilities;

public class TableLayoutTest extends TestCase
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
      assertEquals(4, columns);
      assertEquals(102000, layout.getCellWidth(0, 1));
      assertEquals(2000, layout.getCellWidth(1, 2));
      assertEquals(96000, layout.getCellWidth(2, 3));
      assertEquals(251000, layout.getCellWidth(3, 4));

      Log.debug ("Validated number of columns .. OK");

      // first two rows are reserved for the PageHeader ..
      final TableCellBackground bgTop = layout.getElementAt(2, 2);
      final TableCellBackground bgTop2 = layout.getElementAt(2, 3);
      assertNotNull(bgTop);
      assertNotNull(bgTop2);

      final TableCellBackground bgBlack = layout.getElementAt(11, 2);
      final TableCellBackground bgGray = layout.getElementAt(11, 0);
      assertNotNull(bgGray);
      assertNotNull(bgBlack);
      open = false;
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
    }


    protected TableCreator createContentCreator ()
    {
      return new TestTableContentCreator(getLayoutCreator().getSheetLayoutCollection());
    }

    protected MetaBandProducer createMetaBandProducer ()
    {
      return new HtmlMetaBandProducer(true, true);
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

  public TableLayoutTest (final String s)
  {
    super(s);
  }


  private JFreeReport getReport() throws Exception
  {
    final URL url = ObjectUtilities.getResource
            ("resources/table-layout.xml", TableLayoutTest.class);
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
