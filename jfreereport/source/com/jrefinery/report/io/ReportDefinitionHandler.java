/**
 * Date: Jan 9, 2003
 * Time: 8:37:15 PM
 *
 * $Id: ReportDefinitionHandler.java,v 1.1 2003/01/12 21:35:08 taqua Exp $
 */
package com.jrefinery.report.io;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public interface ReportDefinitionHandler
{
  public void startElement (String tagName, Attributes attrs) throws SAXException;
  public void characters(char ch[], int start, int length) throws SAXException;
  public void endElement (String tagName) throws SAXException;

  public Parser getParser ();
}
