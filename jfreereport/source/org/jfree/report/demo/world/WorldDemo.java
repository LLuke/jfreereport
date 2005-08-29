package org.jfree.report.demo.world;

import org.jfree.report.JFreeReportBoot;
import org.jfree.report.demo.helper.CompoundDemoFrame;
import org.jfree.report.demo.helper.DefaultDemoSelector;
import org.jfree.report.demo.helper.DemoSelector;
import org.jfree.ui.RefineryUtilities;

/**
 * Creation-Date: 28.08.2005, 21:57:24
 *
 * @author: Thomas Morgner
 */
public class WorldDemo extends CompoundDemoFrame
{
  public WorldDemo(final DemoSelector demoSelector)
  {
    super(demoSelector);
    init();
  }

  public static void main(String[] args)
  {
    JFreeReportBoot.getInstance().start();

    final DefaultDemoSelector demoSelector = createDemoInfo();

    final WorldDemo frame = new WorldDemo(demoSelector);
    frame.pack();
    RefineryUtilities.centerFrameOnScreen(frame);
    frame.setVisible(true);

  }

  public static DefaultDemoSelector createDemoInfo() {
    final DefaultDemoSelector demoSelector =
            new DefaultDemoSelector("World demos");
    demoSelector.addDemo(new CountryReportAPIDemoHandler());
    demoSelector.addDemo(new CountryReportXMLDemoHandler());
    demoSelector.addDemo(new CountryReportExtXMLDemoHandler());
    demoSelector.addDemo(new CountryReportSecurityXMLDemoHandler());
    demoSelector.addDemo(new MultiPageCountryDataDemoHandler());
    return demoSelector;
  }
}
