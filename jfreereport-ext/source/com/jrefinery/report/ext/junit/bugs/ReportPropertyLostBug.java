/**
 * =============================================================
 * JFreeReport : an open source reporting class library for Java
 * =============================================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
 * Project Lead:  David Gilbert (david.gilbert@object-refinery.com)
 *
 * (C) Copyright 2000-2002, by Simba Management Limited and Contributors.
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
 * -----------------------
 * ReportPropertyLostBug.java
 * -----------------------
 * (C)opyright 2000-2002, by Simba Management Limited.
 *
 * Changes
 * -------
 * 2002-Jun-11 : The ReportProperty Bug is included
 */
package com.jrefinery.report.ext.junit.bugs;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.net.URL;

import com.jrefinery.report.JFreeReport;
import com.jrefinery.report.demo.SampleData1;
import com.jrefinery.report.io.ReportGenerator;
import com.jrefinery.report.states.ReportState;
import com.jrefinery.report.targets.pageable.PageableReportProcessor;
import com.jrefinery.report.targets.pageable.ReportStateList;
import com.jrefinery.report.targets.pageable.OutputTarget;
import com.jrefinery.report.targets.pageable.output.G2OutputTarget;
import com.jrefinery.report.util.Log;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class ReportPropertyLostBug extends TestCase
{
  private JFreeReport report;

  /**
   * Returns the tests as a test suite.
   */
  public static Test suite()
  {
    return new TestSuite(ReportPropertyLostBug.class);
  }

  /**
   * Constructs a new set of tests.
   * @param name The name of the tests.
   */
  public ReportPropertyLostBug(final String name)
  {
    super(name);
  }

  /**
   * Common test setup.
   */
  protected void setUp() throws Exception
  {
    final URL url = getClass().getResource("/com/jrefinery/report/demo/report2.xml");
    if (url == null)
      throw new FileNotFoundException();

    report = ReportGenerator.getInstance().parseReport(url, url);
    report.setData(new SampleData1());
  }

  /**
   * Check that a report property set before the report is processed is also available when
   * the report processing has started.
   */
  public void testReportState() throws Exception
  {
    report.setProperty("test", "not null");
    assertNotNull(report.getProperty("test"));

    final BufferedImage buffer = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
    final Graphics2D g2 = (Graphics2D) buffer.getGraphics();
    final PageableReportProcessor proc = new PageableReportProcessor(report);
    final OutputTarget ot = new G2OutputTarget(g2, report.getDefaultPageFormat());
    ot.open();
    proc.setOutputTarget(ot);

    final ReportStateList list = proc.repaginate();

    for (int i = 0; i < 2; i++)
    {
      Log.debug("I = " + i);
      final ReportState state = list.get(1);
      final ReportState s2 = proc.processPage(state, proc.getOutputTarget());

      assertNotNull(s2.getProperty("test"));
    }
  }
}
