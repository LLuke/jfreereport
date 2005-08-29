package org.jfree.report.demo.functions;

import org.jfree.report.JFreeReportBoot;
import org.jfree.report.demo.helper.CompoundDemoFrame;
import org.jfree.report.demo.helper.DefaultDemoSelector;
import org.jfree.report.demo.helper.DemoSelector;
import org.jfree.ui.RefineryUtilities;

/**
 * Creation-Date: 27.08.2005, 14:18:59
 *
 * @author: Thomas Morgner
 */
public class FunctionsDemo extends CompoundDemoFrame
{
  public FunctionsDemo(final DemoSelector demoSelector)
  {
    super(demoSelector);
    init();
  }

  public static void main(String[] args)
  {
    JFreeReportBoot.getInstance().start();

    final DefaultDemoSelector demoSelector = createDemoInfo();

    final FunctionsDemo frame = new FunctionsDemo(demoSelector);
    frame.pack();
    RefineryUtilities.centerFrameOnScreen(frame);
    frame.setVisible(true);

  }

  public static DefaultDemoSelector createDemoInfo() {
    final DefaultDemoSelector demoSelector =
            new DefaultDemoSelector("Functions demos");
    demoSelector.addDemo(new ItemHidingDemoHandler());
    demoSelector.addDemo(new PaintComponentDemoHandler());
    demoSelector.addDemo(new PercentageDemo());
    return demoSelector;
  }
}
