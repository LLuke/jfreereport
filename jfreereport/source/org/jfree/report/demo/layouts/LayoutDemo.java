package org.jfree.report.demo.layouts;

import org.jfree.report.JFreeReportBoot;
import org.jfree.report.demo.helper.CompoundDemoFrame;
import org.jfree.report.demo.helper.DefaultDemoSelector;
import org.jfree.report.demo.helper.DemoSelector;
import org.jfree.report.demo.world.WorldDemo;
import org.jfree.report.demo.world.CountryReportAPIDemoHandler;
import org.jfree.report.demo.world.CountryReportXMLDemoHandler;
import org.jfree.report.demo.world.CountryReportExtXMLDemoHandler;
import org.jfree.report.demo.world.CountryReportSecurityXMLDemoHandler;
import org.jfree.report.demo.world.MultiPageCountryDataDemoHandler;
import org.jfree.ui.RefineryUtilities;

/**
 * Creation-Date: 28.08.2005, 21:57:24
 *
 * @author: Thomas Morgner
 */
public class LayoutDemo extends CompoundDemoFrame
{
  public LayoutDemo(final DemoSelector demoSelector)
  {
    super(demoSelector);
    init();
  }

  public static void main(String[] args)
  {
    JFreeReportBoot.getInstance().start();

    final DefaultDemoSelector demoSelector = createDemoInfo();

    final LayoutDemo frame = new LayoutDemo(demoSelector);
    frame.pack();
    RefineryUtilities.centerFrameOnScreen(frame);
    frame.setVisible(true);

  }

  public static DefaultDemoSelector createDemoInfo() {
    final DefaultDemoSelector demoSelector =
            new DefaultDemoSelector("Layout demos");
    demoSelector.addDemo(new BandInBandStackingDemoHandler());
    demoSelector.addDemo(new StackedLayoutXMLDemoHandler());
    demoSelector.addDemo(new StackedLayoutAPIDemoHandler());
    return demoSelector;
  }
}
