package org.jfree.report.modules.output.table.html;

import java.util.ArrayList;

import org.jfree.report.JFreeReport;
import org.jfree.report.ReportProcessingException;
import org.jfree.report.modules.output.table.base.LayoutCreator;
import org.jfree.report.modules.output.table.base.ProxyTableCreator;
import org.jfree.report.modules.output.table.base.TableCreator;
import org.jfree.report.states.ReportState;

/**
 * Creation-Date: 04.09.2005, 19:32:13
 *
 * @author: Thomas Morgner
 */
public class AppendingHtmlProcessor extends HtmlProcessor
{
  private ArrayList additionalReports;
  private int reportCount;
  private ProxyTableCreator layoutCreator;
  private ProxyTableCreator tableCreator;

  public AppendingHtmlProcessor(final JFreeReport report,
                                final boolean useXHTML)
          throws ReportProcessingException
  {
    super(report, useXHTML);
    this.additionalReports = new ArrayList();
    this.additionalReports.add(super.getReport());
  }

  public AppendingHtmlProcessor(final JFreeReport report)
          throws ReportProcessingException
  {
    super(report);
    this.additionalReports = new ArrayList();
    this.additionalReports.add(super.getReport());
  }

  public synchronized void addReport(final JFreeReport report)
  {
    report.addExpression(getTableWriter());
    this.additionalReports.add(report);
  }

  protected JFreeReport getReport()
  {
    if (additionalReports == null)
    {
      return super.getReport();
    }
    return (JFreeReport) additionalReports.get(reportCount);
  }

  public int getSize()
  {
    return additionalReports.size();
  }

  public synchronized void processReport() throws ReportProcessingException
  {
    try
    {
      layoutCreator = null;
      tableCreator = null;

      final ArrayList states = new ArrayList();
      createLayoutCreator();
      layoutCreator.getParent().open(getReport());
      for (reportCount = 0;
           reportCount < getSize(); reportCount += 1)
      {
        final ReportState state = super.repaginate();
        states.add(state);
      }
      layoutCreator.getParent().close();
      reportCount = 0;

      createContentCreator();
      tableCreator.getParent().open(getReport());
      for (reportCount = 0;
           reportCount < getSize(); reportCount += 1)
      {
        final ReportState state = (ReportState) states.get(reportCount);
        createContent(state);
      }
      tableCreator.getParent().close();
    }
    finally
    {
      reportCount = 0;
      layoutCreator = null;
      tableCreator = null;
    }
  }

  protected TableCreator createContentCreator()
  {
    if (tableCreator == null)
    {
      final HtmlLayoutCreator htmlLayoutCreator = (HtmlLayoutCreator) layoutCreator
              .getParent();
      final HtmlContentCreator htmlTableCreator =
              new HtmlContentCreator(getFilesystem(), isGenerateXHTML(),
                      htmlLayoutCreator.getStyleCollection(),
                      layoutCreator.getSheetLayoutCollection());
      tableCreator = new ProxyTableCreator(htmlTableCreator);
    }
    return tableCreator;
  }

  protected LayoutCreator createLayoutCreator()
  {
    if (layoutCreator == null)
    {
      layoutCreator = new ProxyTableCreator(super.createLayoutCreator());
    }
    return layoutCreator;
  }
}
