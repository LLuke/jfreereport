/**
 * Date: Jan 9, 2003
 * Time: 9:08:15 PM
 *
 * $Id$
 */
package com.jrefinery.report.io.ext;

import org.xml.sax.Attributes;
import com.jrefinery.report.io.Parser;
import com.jrefinery.report.io.ReportDefinitionHandler;

public class DataDefinitionHandler implements ReportDefinitionHandler
{
  private Parser parser;

  public DataDefinitionHandler(Parser parser)
  {
    this.parser = parser;
  }

  public void startElement(String tagName, Attributes attrs)
  {
  }

  public void characters(char ch[], int start, int length)
  {
  }

  public void endElement(String tagName)
  {
  }

  public Parser getParser()
  {
    return parser;
  }

  public void setParser(Parser parser)
  {
    this.parser = parser;
  }
}
