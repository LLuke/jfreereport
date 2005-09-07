/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2005, by Object Refinery Limited and Contributors.
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
 * PeopleReportDemo.java
 * ------------
 * (C) Copyright 2002-2005, by Object Refinery Limited.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: PeopleReportDemo.java,v 1.6 2005/08/29 17:40:27 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.report.demo.onetomany;

import org.jfree.report.JFreeReportBoot;
import org.jfree.report.demo.helper.CompoundDemoFrame;
import org.jfree.report.demo.helper.DefaultDemoSelector;
import org.jfree.report.demo.helper.DemoSelector;
import org.jfree.ui.RefineryUtilities;

public class PeopleReportDemo extends CompoundDemoFrame
{
  public PeopleReportDemo(final DemoSelector demoSelector)
  {
    super(demoSelector);
    init();
  }

  public static DemoSelector createDemoInfo ()
  {
    final DefaultDemoSelector demoSelector =
            new DefaultDemoSelector("One-To-Many-Elements Reports");
    demoSelector.addDemo(new PeopleReportXmlDemoHandler());
    demoSelector.addDemo(new PeopleReportAPIDemoHandler());
    return demoSelector;
  }

  public static void main (final String[] args)
  {
    JFreeReportBoot.getInstance().start();


    final PeopleReportDemo frame = new PeopleReportDemo(createDemoInfo());
    frame.pack();
    RefineryUtilities.centerFrameOnScreen(frame);
    frame.setVisible(true);

  }
}
