/**
 * Date: Jan 9, 2003
 * Time: 8:49:58 PM
 *
 * $Id: InitialReportHandler.java,v 1.1 2003/01/12 21:35:08 taqua Exp $
 */
package com.jrefinery.report.io;

import com.jrefinery.report.io.ext.ExtReportHandler;
import com.jrefinery.report.io.simple.ReportFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class InitialReportHandler implements ReportDefinitionHandler
{
  public static final String REPORT_DEFINITION_TAG = "report-definition";
  public static final String OLD_REPORT_TAG = "report";

  private Parser parser;

  public InitialReportHandler(Parser parser)
  {
    this.parser = parser;
  }

  public void startElement(String tagName, Attributes attrs)
    throws SAXException
  {
    if (tagName.equals(REPORT_DEFINITION_TAG))
    {
      ReportDefinitionHandler reportDefinitionHandler = new ExtReportHandler(getParser(), tagName);
      getParser().pushFactory(reportDefinitionHandler);
    }
    else if (tagName.equals(OLD_REPORT_TAG))
    {
      ReportFactory reportDefinitionHandler = new ReportFactory(getParser(), tagName);
      getParser().pushFactory(reportDefinitionHandler);
      reportDefinitionHandler.startElement(tagName, attrs);
    }
    else
    {
      throw new SAXException ("Invalid TagName: " + tagName + ", expected one of: " +
                              REPORT_DEFINITION_TAG + ".");
    }
  }

  public void characters(char ch[], int start, int length)
  {
    // characters are ignored at this point...
  }

  public void endElement(String tagName) throws SAXException
  {
    if (tagName.equals(REPORT_DEFINITION_TAG))
    {
      // ignore the report definition tag
    }
    else if (tagName.equals(OLD_REPORT_TAG))
    {
      // also ignore this one ...
    }
    else
    {
      throw new SAXException ("Invalid TagName: " + tagName + ", expected one of: " +
                              REPORT_DEFINITION_TAG + ".");
    }
  }

  public Parser getParser()
  {
    return parser;
  }
}
