/**
 * ===========================================
 * JFreeReport : a free Java reporting library
 * ===========================================
 *
 * Project Info:  http://reporting.pentaho.org/
 *
 * (C) Copyright 2006-2007, by Pentaho Corporation and Contributors.
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
 * DemoFrontend.java
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */

package org.jfree.report.demo;

import java.net.URL;
import javax.swing.JComponent;

import org.jfree.report.demo.util.DemoSelector;
import org.jfree.report.demo.util.CompoundDemoFrame;
import org.jfree.report.demo.util.DefaultDemoSelector;
import org.jfree.report.demo.quadrant.QuadrantDemoHandler;
import org.jfree.report.demo.quadrant.QuadrantSubReportDemoHandler;
import org.jfree.report.demo.world.WorldDemoHandler;
import org.jfree.report.demo.world.WorldAPIDemoHandler;
import org.jfree.report.demo.loader.DemoLoaderDemoHandler;
import org.jfree.report.demo.autotable.AutoTableDemoHandler;
import org.jfree.report.demo.layoutcontroller.HelloWorldComponentDemoHandler;
import org.jfree.report.demo.pivoting.PivotDemoHandler;
import org.jfree.base.config.ModifiableConfiguration;
import org.jfree.util.ObjectUtilities;
import org.jfree.ui.RefineryUtilities;

public class DemoFrontend extends CompoundDemoFrame
{
  private JComponent infoPane;

  public DemoFrontend(final DemoSelector demoSelector)
  {
    super(demoSelector);
    setIgnoreEmbeddedConfig(true);
    final ModifiableConfiguration editableConfig =
            JFreeReportDemoBoot.getInstance().getEditableConfig();
    editableConfig.setConfigProperty(EMBEDDED_KEY, "true");
    init();
  }

  public static DemoSelector createDemoInfo ()
  {
    final DefaultDemoSelector rootSelector =
        new DefaultDemoSelector("All JFreeReport Demos");
    rootSelector.addDemo(new PivotDemoHandler());
    rootSelector.addDemo(new QuadrantDemoHandler());
    rootSelector.addDemo(new QuadrantSubReportDemoHandler());
    rootSelector.addDemo(new WorldDemoHandler());
    rootSelector.addDemo(new WorldAPIDemoHandler());
    rootSelector.addDemo(new DemoLoaderDemoHandler());
    rootSelector.addDemo(new AutoTableDemoHandler());
    rootSelector.addDemo(new HelloWorldComponentDemoHandler());
    return rootSelector;
  }

  protected JComponent getNoHandlerInfoPane()
  {
    if (infoPane == null)
    {
      final URL url = ObjectUtilities.getResource
            ("org/jfree/report/demo/demo-introduction.html", CompoundDemoFrame.class);

      infoPane = createDescriptionTextPane(url);
    }
    return infoPane;
  }

  public static void main (final String[] args)
  {
    JFreeReportDemoBoot.getInstance().start();

    final DemoFrontend frontend = new DemoFrontend(createDemoInfo());
    frontend.pack();
    RefineryUtilities.centerFrameOnScreen(frontend);
    frontend.setVisible(true);
  }
}
