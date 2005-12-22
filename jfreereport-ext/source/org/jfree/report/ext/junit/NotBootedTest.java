package org.jfree.report.ext.junit;

import java.io.IOException;

import org.jfree.report.modules.parser.base.ReportGenerator;
import org.jfree.report.JFreeReportBoot;
import org.jfree.xml.ElementDefinitionException;
import org.jfree.util.Log;

/**
 * Creation-Date: 19.10.2005, 19:25:48
 *
 * @author Thomas Morgner
 */
public class NotBootedTest
{
  public NotBootedTest()
  {
  }

  public static void main(String[] args)
          throws IOException, ElementDefinitionException
  {
    JFreeReportBoot.getInstance().start();
    Log.debug ("Test");
  }
}
