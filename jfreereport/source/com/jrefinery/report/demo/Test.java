package com.jrefinery.report.demo;

import com.jrefinery.report.JFreeReport;
import com.jrefinery.report.GroupList;
import com.jrefinery.report.Group;
import com.jrefinery.report.targets.G2OutputTarget;
import com.jrefinery.report.util.Log;
import com.jrefinery.report.util.SystemOutLogTarget;
import com.jrefinery.report.io.ReportDefinitionContentHandler;
import com.jrefinery.report.io.ReportGenerator;
import com.jrefinery.io.FileUtilities;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.swing.JOptionPane;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.File;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.net.URL;

public class Test
{

  public Test ()
  {
  }

  public static void main (String[] args)
  {
    Log.addTarget(new SystemOutLogTarget ());
    if (args.length < 1)
    {
    }

    try
    {
      URL url = Log.class.getResource("/com/jrefinery/report/demo/report2.xml");

      JFreeReport report = ReportGenerator.getInstance().parseReport(url);
      report.setData(new SampleData5());

      // set LandScape
      Paper paper = new Paper ();
      paper.setSize (595.275590551181d, 419.5275590551181);
      paper.setImageableArea (70.86614173228338, 70.86614173228347, 453.54330708661416, 277.8236220472441);

      PageFormat format = new PageFormat ();
      format.setOrientation (PageFormat.PORTRAIT);
      format.setPaper (paper);

      long start = System.currentTimeMillis();
      G2OutputTarget t = new G2OutputTarget(G2OutputTarget.createEmptyGraphics(), format);
      report.processReport(t);
      System.out.println("Time: " + (System.currentTimeMillis() - start));
    }
    catch (Exception e)
    {
      System.out.println (e.getMessage ());
    }
  }

}
