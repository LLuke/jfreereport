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
 * PdfTest.java
 * -----------------------
 * (C)opyright 2000-2002, by Simba Management Limited.
 *
 * Changes
 * -------
 * 10-Jun-2002 : Updated code to work with latest version of the JCommon class library;
 *
 */
package com.jrefinery.report.junit;

import com.jrefinery.report.io.ReportGenerator;
import com.jrefinery.report.preview.PreviewFrame;
import com.jrefinery.report.JFreeReport;
import com.jrefinery.report.LabelElement;
import com.jrefinery.report.ItemFactory;
import com.jrefinery.report.ElementConstants;
import com.jrefinery.report.TextElement;
import com.jrefinery.report.Band;
import com.jrefinery.report.demo.SampleData3;
import com.jrefinery.report.demo.SampleData2;
import com.jrefinery.ui.RefineryUtilities;

import javax.swing.JOptionPane;
import javax.swing.JFrame;
import java.io.File;
import java.awt.geom.Rectangle2D;
import java.awt.Color;

public class PdfTest
{
  /**
   * Displays a preview frame for report two.  If the preview frame already exists, it is brought
   * to the front.
   */
  public void preview()
  {
    JFreeReport report3 = null;
    File in =
      new File(getClass().getResource("/com/jrefinery/report/demo/report2.xml").getFile());
    if (in == null)
    {
      JOptionPane.showMessageDialog(
        null,
        "ReportDefinition report3.xml not found on classpath");
      return;
    }
    ReportGenerator gen = ReportGenerator.getInstance();

    try
    {
      report3 = gen.parseReport(in);
    }
    catch (Exception ioe)
    {
      ioe.printStackTrace();
      JOptionPane.showMessageDialog(
        null, ioe.getMessage(), "Error: " + ioe.getClass().getName(),
        JOptionPane.ERROR_MESSAGE);
      return;
    }
    LabelElement field = new LabelElement();
    field.setLabel("xxxxxx");
    field.setName("xxxxxx");
    field.setBounds(new Rectangle2D.Double(60,0,76,16));
/*
    TextElement field = ItemFactory.createLabelElement(
            "xxx",
            new Rectangle2D.Double(60,0,76,16),
            Color.black,
            ElementConstants.LEFT,
            Band.DEFAULT_FONT,
            "XXXXX"
      );
    */
    report3.getItemBand().addElement(field);

    report3.setData(new SampleData2());
    PreviewFrame frame3 = new PreviewFrame(report3);
    frame3.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame3.pack();
    RefineryUtilities.positionFrameRandomly(frame3);
    frame3.show();
    frame3.requestFocus();
  }

  public static void main (String[] args)
  {
    PdfTest test = new PdfTest();
    test.preview();
  }

}
