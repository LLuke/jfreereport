package org.jfree.report.demo.surveyscale;

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
public class SurveyScaleDemo extends CompoundDemoFrame
{
  public SurveyScaleDemo(final DemoSelector demoSelector)
  {
    super(demoSelector);
    init();
  }

  public static void main(String[] args)
  {
    JFreeReportBoot.getInstance().start();

    final DefaultDemoSelector demoSelector = createDemoInfo();

    final SurveyScaleDemo frame = new SurveyScaleDemo(demoSelector);
    frame.pack();
    RefineryUtilities.centerFrameOnScreen(frame);
    frame.setVisible(true);

  }

  public static DefaultDemoSelector createDemoInfo() {
    final DefaultDemoSelector demoSelector =
            new DefaultDemoSelector("Survey Scale Demos");
    demoSelector.addDemo(new SurveyScaleAPIDemoHandler());
    demoSelector.addDemo(new SurveyScaleXMLDemoHandler());
    return demoSelector;
  }
}
