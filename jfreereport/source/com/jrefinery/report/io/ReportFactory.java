/**
 * =============================================================
 * JFreeReport : an open source reporting class library for Java
 * =============================================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
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
 * ------------------
 * ReportFactory.java
 * ------------------
 * (C)opyright 2002, by Simba Management Limited and Contributors.
 *
 * Original Author:  Thomas Morgner (taquera@sherito.org);
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * Changes
 * -------
 * 10-May-2002 : Initial version
 * 05-Jun-2002 : Updated Javadoc comments (DG);
 */

package com.jrefinery.report.io;

import com.jrefinery.report.JFreeReport;
import com.jrefinery.report.Band;
import com.jrefinery.report.util.Log;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * A handler for the SAX events generated by the top level <REPORT> element in the JFreeReport
 * XML report definition file.
 */
public class ReportFactory extends DefaultHandler implements ReportDefinitionTags
{

  /** The report under construction. */
  private JFreeReport report;

  /**
   * the base handler used as general coordination instance. This is the DefaultHandler used
   * by sax. This base handler distributes the sax events to the different factories depending on
   * the current state
   */
  private ReportDefinitionContentHandler handler;

  /** the current band */
  private Band currentBand;

  /**
   * Constructs a new handler.
   *
   * @param handler the base handler for this report factory
   */
  public ReportFactory (ReportDefinitionContentHandler handler)
  {
    this.handler = handler;
  }

  /**
   * A SAX event indicating that an element start tag has been read.
   */
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
      throw new SAXException ("Expected one of <report>, <groups>, <items>, <functions>");
    }
  }

  /**
   * Handle the end-tag event of sax.
   */
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

  /**
   * creates a new report depending on the attributes given.
   */
  public void startReport (Attributes atts)
          throws SAXException
  {
    String name = getHandler ().generateName (atts.getValue ("name"));

    JFreeReport report = new JFreeReport ();
    report.setName (name);
    setReport (report);
  }

  /**
   * creates a new group list for the report. The group factory will be the new default handler
   * for SAX Events
   */
  public void startGroups (Attributes atts)
          throws SAXException
  {
    GroupFactory groupFactory = handler.createGroupFactory ();
    getHandler ().setExpectedHandler (groupFactory);
  }

  /**
   * creates a new function collection for the report. The FunctionFactory will be the new
   * default handler for SAX Events
   */
  public void startFunctions (Attributes atts)
          throws SAXException
  {
    FunctionFactory functionFactory = handler.createFunctionFactory ();
    getHandler ().setExpectedHandler (functionFactory);
  }

  /**
   * Finishes the report generation.
   */
  public void endReport ()
          throws SAXException
  {
    getHandler ().setReport (report);
  }

  /**
   * returns the currently to be build report
   */
  public JFreeReport getReport ()
  {
    return report;
  }

  /**
   * return the current ReportDefinitionContentHandler
   */
  public ReportDefinitionContentHandler getHandler ()
  {
    return handler;
  }

  /**
   * returns the current band
   */
  public Band getCurrentBand ()
  {
    return currentBand;
  }

  /**
   * defines the current band.
   */
  public void setCurrentBand (Band band)
  {
    this.currentBand = band;
  }

  /**
   * sets the current report
   */
  private void setReport (JFreeReport report)
  {
    this.report = report;
  }
}
