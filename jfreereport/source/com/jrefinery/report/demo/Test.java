package com.jrefinery.report.demo;

import com.jrefinery.report.JFreeReport;
import com.jrefinery.report.GroupList;
import com.jrefinery.report.Group;
import com.jrefinery.report.util.Log;
import com.jrefinery.report.util.SystemOutLogTarget;
import com.jrefinery.report.io.ReportDefinitionContentHandler;
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

      args = new String [] { "report1.xml" };
    }

    SAXParserFactory factory = SAXParserFactory.newInstance ();
    try
    {
      SAXParser parser = factory.newSAXParser ();
      ReportDefinitionContentHandler handler = new ReportDefinitionContentHandler ();

      try
      {
        File file1 = FileUtilities.findFileOnClassPath (args[0]);
        if (file1 == null)
        {
          System.err.println ("Unable to find " + args[0] + " on the classpath");
          file1 = new File (args[0]);
        }
        InputStream in = new FileInputStream (file1);
        parser.parse (in, handler);
        JFreeReport report = handler.getReport ();
        System.out.println ("Report: " + report);
        System.out.println ("PageHeader: " + report.getPageHeader());
        System.out.println ("PageFooter: " + report.getPageFooter());
        System.out.println ("ReportHeader: " + report.getReportHeader());
        System.out.println ("ReportFooter: " + report.getReportFooter());

        GroupList groups = report.getGroups();
        for (int i = 0; i < groups.size(); i++)
        {
          Group g = groups.get(i);
          System.out.println ("GroupHeader: " + g.getName() + " " + g.getHeader());
          System.out.println ("GroupFooter: " + g.getName() + " " + g.getFooter());
        }
      }
      catch (IOException e)
      {
        System.out.println (e.getMessage ());
      }
    }
    catch (ParserConfigurationException e)
    {
      System.out.println (e.getMessage ());
    }
    catch (SAXException e)
    {
      System.out.println (e.getMessage ());
    }
  }

}