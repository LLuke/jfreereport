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

import com.jrefinery.report.targets.G2OutputTarget;
import com.jrefinery.report.JFreeReport;
import com.jrefinery.report.targets.OutputTarget;
import com.jrefinery.report.ReportState;
import com.jrefinery.report.ReportProcessingException;
import com.jrefinery.report.ReportStateList;
import com.jrefinery.report.preview.ReportPane;
import com.jrefinery.report.demo.SampleData1;
import com.jrefinery.report.function.Function;
import com.jrefinery.report.function.ItemSumFunction;
import com.jrefinery.report.io.ReportGenerator;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.awt.Graphics2D;
import java.util.List;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.math.BigDecimal;

public class ReportPropertyLostBug extends TestCase
{

  public class TestPane extends ReportPane
  {
    public ReportStateList getPageStates ()
    {
      return getPageStateList ();
    }

    public TestPane (JFreeReport report, G2OutputTarget target)
    {
      super (report, target);
    }

    public void repaginate (Graphics2D g) throws ReportProcessingException
    {
      super.repaginate(getOutputTarget());
    }
  }

  JFreeReport report;
  TestPane pane;
  ItemSumFunction fc;

  /**
   * Returns the tests as a test suite.
   */
  public static Test suite ()
  {
    return new TestSuite (ReportPropertyLostBug.class);
  }

  /**
   * Constructs a new set of tests.
   * @param The name of the tests.
   */
  public ReportPropertyLostBug (String name)
  {
    super (name);
  }

  /**
   * Common test setup.
   */
  protected void setUp () throws Exception
  {
    URL url = getClass ().getResource ("/com/jrefinery/report/demo/report1.xml");
    if (url == null)
      throw new FileNotFoundException ();
    File in = new File (url.getFile ());
    report = ReportGenerator.getInstance ().parseReport (in);
    report.setData (new SampleData1 ());
    BufferedImage buffer = new BufferedImage (100, 100, BufferedImage.TYPE_INT_RGB);
    Graphics2D g2 = (Graphics2D) buffer.getGraphics ();

    // set LandScape
    Paper paper = new Paper ();
    paper.setSize (595.275590551181d, 419.5275590551181);
    paper.setImageableArea (70.86614173228338, 70.86614173228347, 453.54330708661416, 277.8236220472441);

    PageFormat format = new PageFormat ();
    format.setOrientation (PageFormat.LANDSCAPE);
    format.setPaper (paper);
    this.pane = new TestPane (report, new G2OutputTarget (g2, format));

  }

  /**
   * Check that a report property set before the report is processed is also available when
   * the report processing has started.
   */
  public void testReportState () throws Exception
  {
    report.setProperty("test", "not null");
    assertNotNull(report.getProperty("test"));

    BufferedImage buffer = new BufferedImage (100, 100, BufferedImage.TYPE_INT_RGB);
    Graphics2D g2 = (Graphics2D) buffer.getGraphics ();
    pane.repaginate(g2);

    ReportState sstate = (ReportState) pane.getPageStates().get (1);

    for (int i = 0; i < 2; i++)
    {
      System.out.println ("I = " + i);
      ReportState state = (ReportState) pane.getPageStates().get (1);
      ReportState s2 = report.processPage (pane.getOutputTarget(), state, true);

      assertNotNull(s2.getProperty("test"));
    }
  }

  public static void main (String[] args)
  {
    // Select A5 landscape to view the required page dimensions.
    PrinterJob pj = PrinterJob.getPrinterJob ();
    PageFormat fmt = pj.pageDialog (pj.defaultPage ());
    System.out.println (fmt.getWidth () + " " + fmt.getHeight ());
    System.out.println (fmt.getImageableX () + " " + fmt.getImageableY () + " " + fmt.getImageableWidth () + " " + fmt.getImageableHeight ());
    System.exit (0);
  }
}
