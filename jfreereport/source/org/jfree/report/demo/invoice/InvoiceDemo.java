package org.jfree.report.demo.invoice;

import org.jfree.report.JFreeReportBoot;
import org.jfree.report.demo.helper.CompoundDemoFrame;
import org.jfree.report.demo.helper.DefaultDemoSelector;
import org.jfree.report.demo.helper.DemoSelector;
import org.jfree.report.demo.invoice.combined.CombinedAdvertisingDemoHandler;
import org.jfree.ui.RefineryUtilities;

/**
 * Creation-Date: 27.08.2005, 14:18:59
 *
 * @author: Thomas Morgner
 */
public class InvoiceDemo extends CompoundDemoFrame
{
  public InvoiceDemo(final DemoSelector demoSelector)
  {
    super(demoSelector);
    init();
  }

  public static void main(String[] args)
  {
    JFreeReportBoot.getInstance().start();

    final DefaultDemoSelector demoSelector = createDemoInfo();

    final InvoiceDemo frame = new InvoiceDemo(demoSelector);
    frame.pack();
    RefineryUtilities.centerFrameOnScreen(frame);
    frame.setVisible(true);

  }

  public static DefaultDemoSelector createDemoInfo() {
    final DefaultDemoSelector demoSelector =
            new DefaultDemoSelector("Invoice demos");
    demoSelector.addDemo(new SimpleInvoiceDemoHandler());
    demoSelector.addDemo(new SimpleAdvertisingDemoHandler());
    demoSelector.addDemo(new CombinedAdvertisingDemoHandler());
    return demoSelector;
  }
}
