package org.jfree.report.demo;

import java.io.IOException;
import javax.swing.table.TableModel;

import org.jfree.report.JFreeReport;
import org.jfree.report.JFreeReportBoot;
import org.jfree.report.demo.helper.SimpleDemoFrame;
import org.jfree.ui.RefineryUtilities;
import org.jfree.xml.ElementDefinitionException;

public class ReportFooterDemo extends SimpleDemoFrame
{
  public ReportFooterDemo ()
  {
    init();
  }

  protected JFreeReport createReport ()
          throws ElementDefinitionException, IOException
  {
    return loadReport("/org/jfree/report/demo/footer-demo1.xml");
  }

  protected TableModel getData ()
  {
    return new SampleData2();
  }

  protected String getResourcePrefix ()
  {
    return "demo.report-footer.simple";
  }

  public static void main (final String[] args)
  {
    JFreeReportBoot.getInstance().start();
    final ReportFooterDemo frame = new ReportFooterDemo();
    frame.pack();
    RefineryUtilities.centerFrameOnScreen(frame);
    frame.setVisible(true);

  }
}
