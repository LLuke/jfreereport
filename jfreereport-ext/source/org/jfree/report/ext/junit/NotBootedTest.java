package org.jfree.report.ext.junit;

import java.io.IOException;

import org.jfree.report.modules.parser.base.ReportGenerator;
import org.jfree.xml.ElementDefinitionException;

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
    ReportGenerator gen = ReportGenerator.getInstance();
    gen.parseReport("not found");
  }
}
