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
 * StateBug.java
 * -----------------------
 * (C)opyright 2000-2002, by Simba Management Limited.
 *
 */
package com.jrefinery.report.junit;

import com.jrefinery.report.targets.G2OutputTarget;
import com.jrefinery.report.JFreeReport;
import com.jrefinery.report.targets.OutputTarget;
import com.jrefinery.report.ReportState;
import com.jrefinery.report.ReportProcessingException;
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

public class StateBug extends TestCase
{

  public class TestPane extends ReportPane
  {
    public List getPageStates ()
    {
      return pageStates;
    }

    public TestPane (JFreeReport report, G2OutputTarget target)
    {
      super (report, target);
    }

    public void repaginate (Graphics2D g) throws ReportProcessingException
    {
      super.repaginate(g);
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
    return new TestSuite (StateBug.class);
  }

  /**
   * Constructs a new set of tests.
   * @param The name of the tests.
   */
  public StateBug (String name)
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
   * Counts the number of groups.
   */
  public void testReportState () throws Exception
  {
    BufferedImage buffer = new BufferedImage (100, 100, BufferedImage.TYPE_INT_RGB);
    Graphics2D g2 = (Graphics2D) buffer.getGraphics ();
    pane.repaginate(g2);

    ReportState sstate = (ReportState) pane.getPageStates().get (1);
    Object o = sstate.getFunctions().get("sum");
    Object o2 = ((ReportState)(sstate.clone())).getFunctions().get("sum");

    assertTrue ("Function Cloning works", o2.hashCode() != o.hashCode());

    for (int i = 0; i < 2; i++)
    {
      System.out.println ("I = " + i);
      ReportState state = (ReportState) pane.getPageStates().get (1);

      Object beforeCollection = state.getFunctions();
      Object beforeFunction = state.getFunctions().get("sum");
      ItemSumFunction fn = (ItemSumFunction) beforeFunction;
      Object beforeValue = state.getFunctions().get("sum").getValue();

      ReportState s2 = report.processPage (pane.getOutputTarget(), state, true);

      Object afterCollection = state.getFunctions();
      Object afterFunction = state.getFunctions().get("sum");
      Object afterValue = state.getFunctions().get("sum").getValue();

      Object nextFunction = s2.getFunctions().get("sum");
      Object nextValue = s2.getFunctions().get("sum").getValue();

      System.out.println ("BC: " + beforeCollection.hashCode() + " AC " + afterCollection.hashCode());
      System.out.println ("Before: " + beforeFunction + " " + beforeValue);
      System.out.println ("After: " + afterFunction + " " + afterValue);
      System.out.println ("Next: " + nextFunction + " " + nextValue);
      assertTrue ("Function Value did not change", beforeValue.equals (afterValue));
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
