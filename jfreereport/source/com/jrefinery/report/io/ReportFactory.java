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
import com.jrefinery.report.Band;
import com.jrefinery.report.util.Log;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Handles: REPORT_TAG
 */
public class ReportFactory extends DefaultHandler implements ReportDefinitionTags
{
  private JFreeReport report;
  private ReportDefinitionContentHandler handler;
  private Band currentBand;

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
      startReport(atts);
    }
    else if (elementName.equals (REPORT_HEADER_TAG) ||
            elementName.equals (REPORT_FOOTER_TAG) ||
            elementName.equals (PAGE_HEADER_TAG) ||
            elementName.equals (PAGE_FOOTER_TAG) ||
            elementName.equals (ITEMS_TAG))
    {
      // Forward the event to the newly created
      BandFactory bandFactory = handler.createBandFactory ();
      handler.setExpectedHandler (bandFactory);
      bandFactory.startElement (namespaceURI, localName, qName, atts);
    }
    else if (elementName.equals (GROUPS_TAG))
    {
      startGroups (atts);
    }
    else if (elementName.equals (FUNCTIONS_TAG))
    {
      startFunctions (atts);
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
      endReport ();
    }
    else
    {
      Log.error ("Expected </report>");
      throw new SAXException ("Expected report end tag");
    }
  }

  public void startReport (Attributes atts)
          throws SAXException
  {
    String name = getHandler ().generateName (atts.getValue ("name"));

    JFreeReport report = new JFreeReport ();
    report.setName (name);
    setReport (report);
  }

  public void startGroups (Attributes atts)
          throws SAXException
  {
    GroupFactory groupFactory = handler.createGroupFactory ();
    getHandler ().setExpectedHandler (groupFactory);
  }

  public void startFunctions (Attributes atts)
          throws SAXException
  {
    FunctionFactory functionFactory = handler.createFunctionFactory ();
    getHandler ().setExpectedHandler (functionFactory);
  }

  public void endReport ()
          throws SAXException
  {
    getHandler ().setReport (report);
  }

  public JFreeReport getReport ()
  {
    return report;
  }

  public ReportDefinitionContentHandler getHandler ()
  {
    return handler;
  }

  public Band getCurrentBand ()
  {
    return currentBand;
  }

  public void setCurrentBand (Band band)
  {
    this.currentBand = band;
  }

  public void setReport (JFreeReport report)
  {
    this.report = report;
  }
}
