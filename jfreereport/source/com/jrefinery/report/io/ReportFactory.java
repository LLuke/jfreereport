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
 * ReportFactory.java
 * -----------------------
 * (C)opyright 2000-2002, by Simba Management Limited.
 *
 * 10-May-2002 : Initial version
 */
package com.jrefinery.report.io;

import com.jrefinery.report.JFreeReport;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * Handles: REPORT_TAG
 */
public class ReportFactory extends AbstractReportDefinitionHandler implements ReportDefinitionTags
{
  private JFreeReport report;
  private ReportDefinitionContentHandler handler;

  public ReportFactory (ReportDefinitionContentHandler handler)
  {
    this.handler = handler;
  }

  public void startElement (String namespaceURI,
                            String localName,
                            String qName,
                            Attributes atts) throws SAXException
  {
    String elementName = qName.toLowerCase ().trim ();
    if (elementName.equals (REPORT_TAG))
    {
      String name = generateName (atts.getValue ("name"));
      this.report = new JFreeReport ();
      this.report.setName (name);
    }
    else if (elementName.equals (REPORT_HEADER_TAG) ||
            elementName.equals (REPORT_FOOTER_TAG) ||
            elementName.equals (PAGE_HEADER_TAG) ||
            elementName.equals (PAGE_FOOTER_TAG) ||
            elementName.equals (ITEMS_TAG))
    {
      BandFactory bandFactory = new BandFactory (report, handler);
      handler.setExpectedHandler (bandFactory);
      bandFactory.startElement (namespaceURI, localName, qName, atts);
    }
    else if (elementName.equals (GROUPS_TAG))
    {
      startGroups ();
    }
    else if (elementName.equals (FUNCTIONS_TAG))
    {
      startFunctions ();
    }
    else
    {
      System.err.println ("Expected one of <report>, <groups>, <items>, <functions>");
      throw new SAXException ("Expected report, groups, items, functions");
    }
  }

  public void endElement (String namespaceURI, String localName, String qName) throws SAXException
  {
    String elementName = qName.toLowerCase ().trim ();
    if (elementName.equals (REPORT_TAG))
    {
      handler.setReport(report);
    }
    else
    {
      System.err.println ("Expected </report>");
      throw new SAXException ("Expected report");
    }
  }

  public void startGroups ()
  {
    GroupFactory groupFactory = new GroupFactory (report, handler);
    handler.setExpectedHandler (groupFactory);
  }

  public void startFunctions ()
  {
    FunctionFactory functionFactory = new FunctionFactory (report, handler);
    handler.setExpectedHandler (functionFactory);
  }

  public JFreeReport getReport ()
  {
    return report;
  }
}
