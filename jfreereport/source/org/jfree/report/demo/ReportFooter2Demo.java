package org.jfree.report.demo;

import java.io.IOException;
import javax.swing.table.TableModel;

import org.jfree.report.JFreeReport;
import org.jfree.report.JFreeReportBoot;
import org.jfree.report.demo.helper.SimpleDemoFrame;
import org.jfree.ui.RefineryUtilities;
import org.jfree.xml.ElementDefinitionException;

public class ReportFooter2Demo extends SimpleDemoFrame
{
  public ReportFooter2Demo ()
  {
    init();
  }

  protected JFreeReport createReport ()
          throws ElementDefinitionException, IOException
  {
    return loadReport("/org/jfree/report/demo/footer-demo2.xml");
  }

  protected TableModel getData ()
  {
    return new SampleData2(110);
  }

  protected String getResourcePrefix ()
  {
    return "demo.report-footer.complex";
  }

  public static void main (final String[] args)
  {
    JFreeReportBoot.getInstance().start();
    final ReportFooter2Demo frame = new ReportFooter2Demo();
    frame.pack();
    RefineryUtilities.centerFrameOnScreen(frame);
    frame.setVisible(true);

  }
}
