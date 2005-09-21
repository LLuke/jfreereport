/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
 *
 * (C) Copyright 2000-2004, by Simba Management Limited and Contributors.
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
 * ------------------------------
 * FunctionsDemo.java
 * ------------------------------
 * (C)opyright 2004, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: Treatment.java,v 1.2 2005/01/25 01:13:55 taqua Exp $
 *
 * Changes
 * -------------------------
 * 27-Aug-2005 : Initial version
 *
 */
package org.jfree.report.demo.functions;

import org.jfree.report.JFreeReportBoot;
import org.jfree.report.demo.helper.CompoundDemoFrame;
import org.jfree.report.demo.helper.DefaultDemoSelector;
import org.jfree.report.demo.helper.DemoSelector;
import org.jfree.ui.RefineryUtilities;

/**
 * This demo provides a frontend for all demos showing how to use functions.
 *
 * @author Thomas Morgner
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
