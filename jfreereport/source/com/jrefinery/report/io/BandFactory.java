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
 * BandFactory.java
 * -----------------------
 * (C)opyright 2000-2002, by Simba Management Limited.
 *
 * 10-May-2002 : Initial version
 * 22-May-2002 : Structured parsing functions: all tags have a startXXX and endXXX function
 */
package com.jrefinery.report.io;

import com.jrefinery.report.Band;
import com.jrefinery.report.ItemBand;
import com.jrefinery.report.JFreeReport;
import com.jrefinery.report.PageFooter;
import com.jrefinery.report.PageHeader;
import com.jrefinery.report.ReportFooter;
import com.jrefinery.report.ReportHeader;
import com.jrefinery.report.util.Log;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * The band factory handles all Bands. Bands can be one of
 * <ul>
 * <li>pageheader
 * <li>pagefooter
 * <li>reportheader
 * <li>reporthooter
 * <li>items
 * </ul>
 */
public class BandFactory extends DefaultHandler implements ReportDefinitionTags
{
  private ReportDefinitionContentHandler handler;
  private FontFactory fontFactory;
  private JFreeReport report;

  public BandFactory (ReportFactory base)
  {
    this.handler = base.getHandler ();
    this.report = base.getReport ();
    this.fontFactory = handler.getFontFactory ();
  }

  private JFreeReport getReport ()
  {
    return report;
  }

  public Band getCurrentBand ()
  {
    return handler.getReportFactory().getCurrentBand();
  }

  protected void setCurrentBand (Band band)
  {
    handler.getReportFactory().setCurrentBand(band);
  }

  public void startElement (String namespaceURI,
                            String localName,
                            String qName,
                            Attributes atts) throws SAXException
  {
    String elementName = qName.toLowerCase ().trim ();
    Log.debug ("Band factory received: " + elementName);
    if (elementName.equals (REPORT_HEADER_TAG))
    {
      startReportHeader (atts);
    }
    // *** REPORT FOOTER ***
    else if (elementName.equals (REPORT_FOOTER_TAG))
    {
      startReportFooter (atts);
    }
    // *** PAGE HEADER ***
    else if (elementName.equals (PAGE_HEADER_TAG))
    {
      startPageHeader (atts);
    }
    // *** PAGE FOOTER ***
    else if (elementName.equals (PAGE_FOOTER_TAG))
    {
      startPageFooter (atts);
    }
    else if (elementName.equals (ITEMS_TAG))
    {
      startItems (atts);
    }
    else
    {
      throw new SAXException ("Expected one of: reportheader, reportfooter, pageheader, pagefooter or items");
    }
  }

  public void endElement (String namespaceURI,
                          String localName,
                          String qName) throws SAXException
  {
    String elementName = qName.toLowerCase ().trim ();
    if (elementName.equals (REPORT_HEADER_TAG))
    {
      endReportHeader ();
    }
    // *** REPORT FOOTER ***
    else if (elementName.equals (REPORT_FOOTER_TAG))
    {
      endReportFooter ();
    }
    // *** PAGE HEADER ***
    else if (elementName.equals (PAGE_HEADER_TAG))
    {
      endPageHeader ();
    }
    // *** PAGE FOOTER ***
    else if (elementName.equals (PAGE_FOOTER_TAG))
    {
      endPageFooter ();
    }
    else if (elementName.equals (ITEMS_TAG))
    {
      endItems ();
    }
    else
    {
      throw new SAXException ("Expected one of: reportheader, reportfooter, pageheader, pagefooter or items");
    }
  }

  public void startReportHeader (Attributes attr)
          throws SAXException
  {
    float height = ParserUtil.parseFloat (attr.getValue ("height"), "Element height not specified");
    boolean ownPage = ParserUtil.parseBoolean (attr.getValue ("ownpage"), false);

    // create the report header...
    ReportHeader reportHeader = new ReportHeader ();
    reportHeader.setHeight (height);
    reportHeader.setOwnPage (ownPage);
    reportHeader.setDefaultFont (fontFactory.createDefaultFont (attr));
    getReport ().setReportHeader (reportHeader);
    setCurrentBand (reportHeader);
    handler.setExpectedHandler (handler.createElementFactory ());
  }

  public void startReportFooter (Attributes attr)
          throws SAXException
  {
    // get the height...
    float height = ParserUtil.parseFloat (attr.getValue ("height"), "Element height not specified");
    boolean ownPage = ParserUtil.parseBoolean (attr.getValue ("ownpage"), false);

    // create the report footer...
    ReportFooter reportFooter = new ReportFooter ();
    reportFooter.setHeight (height);
    reportFooter.setOwnPage (ownPage);
    reportFooter.setDefaultFont (fontFactory.createDefaultFont (attr));
    getReport ().setReportFooter (reportFooter);
    setCurrentBand (reportFooter);
    handler.setExpectedHandler (handler.createElementFactory ());
  }

  public void startPageHeader (Attributes attr)
          throws SAXException
  {
    // get the height...
    float height = ParserUtil.parseFloat (attr.getValue ("height"), "Element height not specified");
    boolean firstPage = ParserUtil.parseBoolean (attr.getValue ("onfirstpage"), true);
    boolean lastPage = ParserUtil.parseBoolean (attr.getValue ("onlastpage"), true);

    // create the page header...
    PageHeader pageHeader = new PageHeader ();
    pageHeader.setHeight (height);
    pageHeader.setDisplayOnFirstPage (firstPage);
    pageHeader.setDisplayOnLastPage (lastPage);
    pageHeader.setDefaultFont (fontFactory.createDefaultFont (attr));
    setCurrentBand (pageHeader);
    getReport ().setPageHeader (pageHeader);
    handler.setExpectedHandler (handler.createElementFactory ());
  }

  public void startPageFooter (Attributes attr)
          throws SAXException
  {
    // get the height...
    float height = ParserUtil.parseFloat (attr.getValue ("height"), "Element height not specified");
    boolean firstPage = ParserUtil.parseBoolean (attr.getValue ("onfirstpage"), true);
    boolean lastPage = ParserUtil.parseBoolean (attr.getValue ("onlastpage"), true);

    // create the page footer...
    PageFooter pageFooter = new PageFooter ();
    pageFooter.setHeight (height);
    pageFooter.setDisplayOnFirstPage (firstPage);
    pageFooter.setDisplayOnLastPage (lastPage);
    pageFooter.setDefaultFont (fontFactory.createDefaultFont (attr));
    setCurrentBand (pageFooter);
    getReport ().setPageFooter (pageFooter);
    handler.setExpectedHandler (handler.createElementFactory ());
  }

  public void startItems (Attributes attr)
          throws SAXException
  {
    // get the height...
    float height = ParserUtil.parseFloat (attr.getValue ("height"), "Element height not specified");
    ItemBand items = new ItemBand ();
    items.setHeight (height);
    items.setDefaultFont (fontFactory.createDefaultFont (attr));
    setCurrentBand (items);
    getReport ().setItemBand (items);
    handler.setExpectedHandler (handler.createElementFactory ());
  }

  public void endItems ()
  {
    handler.finishedHandler ();
  }

  public void endPageHeader ()
  {
    handler.finishedHandler ();
  }

  public void endPageFooter ()
  {
    handler.finishedHandler ();
  }

  public void endReportHeader ()
  {
    handler.finishedHandler ();
  }

  public void endReportFooter ()
  {
    handler.finishedHandler ();
  }
}
