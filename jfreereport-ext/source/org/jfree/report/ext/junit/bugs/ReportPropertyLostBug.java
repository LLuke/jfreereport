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
package org.jfree.report.ext.junit.bugs;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.net.URL;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.jfree.report.JFreeReport;
import org.jfree.report.demo.SampleData1;
import org.jfree.report.modules.output.pageable.base.OutputTarget;
import org.jfree.report.modules.output.pageable.base.PageableReportProcessor;
import org.jfree.report.modules.output.pageable.base.ReportStateList;
import org.jfree.report.modules.output.pageable.graphics.G2OutputTarget;
import org.jfree.report.modules.parser.base.ReportGenerator;
import org.jfree.report.states.ReportState;
import org.jfree.report.util.Log;

/**
 * Tests whether report properties get lost during the report processing.
 * 
 * @author Thomas Morgner
 */
public class ReportPropertyLostBug extends TestCase
{
  /** The report used to perform the test. */
  private JFreeReport report;

  /**
   * Returns the tests as a test suite.
   * @return the created suite
   */
  public static Test suite()
  {
    return new TestSuite(ReportPropertyLostBug.class);
  }

  /**
   * Constructs a new set of tests.
   * 
   * @param name The name of the tests.
   */
  public ReportPropertyLostBug(final String name)
  {
    super(name);
  }

  /**
   * Common test setup.
   * 
   * @throws Exception if an error occurs
   */
  protected void setUp() throws Exception
  {
    final URL url = getClass().getResource("/org/jfree/report/demo/report2.xml");
    if (url == null)
    {
      throw new FileNotFoundException();
    }

    report = ReportGenerator.getInstance().parseReport(url, url);
    report.setData(new SampleData1());
  }

  /**
   * Check that a report property set before the report is processed is also available when
   * the report processing has started.
   * 
   * @throws Exception if an error occurs
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
