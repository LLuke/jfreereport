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
 * ----------------
 * BandFactory.java
 * ----------------
 * (C)opyright 2002, by Simba Management Limited and Contributors.
 *
 * Original Author:  Thomas Morgner (taquera@sherito.org);
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id$
 *
 * Changes
 * -------
 * 10-May-2002 : Initial version
 * 22-May-2002 : Structured parsing functions: all tags have a startXXX and endXXX function
 * 05-Jun-2002 : Updated Javadoc comments (DG);
 */

package com.jrefinery.report.io;

import com.jrefinery.report.Band;
import com.jrefinery.report.ItemBand;
import com.jrefinery.report.JFreeReport;
import com.jrefinery.report.PageFooter;
import com.jrefinery.report.PageHeader;
import com.jrefinery.report.ReportFooter;
import com.jrefinery.report.ReportHeader;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * This class handles the SAX events generated for report bands:
 * <ul>
 * <li>pageheader</li>
 * <li>pagefooter</li>
 * <li>reportheader</li>
 * <li>reporthooter</li>
 * <li>items</li>
 * </ul>
 *
 * @author TM
 */
public class BandFactory extends DefaultHandler implements ReportDefinitionTags
{
  /**
   * The ReportDefinitionContentHandler that is used to parse the report. This is
   * a general coordination object for all parsers subfactories.
   */
  private ReportDefinitionContentHandler handler;

  /**
   * The FontFactory is used to create java.awt.Font instances based on the data given
   * in the Band and Element definition.
   */
  private FontFactory fontFactory;

  /**
   * The report that is currently build.
   */
  private JFreeReport report;

  /**
   * Initializes this BandFactory based on the data contained in the ReportFactory.
   *
   * @param base  the report handler.
   */
  public BandFactory (ReportFactory base)
  {
    this.handler = base.getHandler ();
    this.report = base.getReport ();
    this.fontFactory = handler.getFontFactory ();
  }

  /**
   * Returns the current report.
   *
   * @return the report.
   */
  protected JFreeReport getReport ()
  {
    return report;
  }

  /**
   * Returns the current band, that is being build.
   *
   * @return the current band.
   */
  public Band getCurrentBand ()
  {
    return handler.getReportFactory ().getCurrentBand ();
  }

  /**
   * Defines the current band that gets currently build.
   *
   * @param band  the band.
   */
  protected void setCurrentBand (Band band)
  {
    handler.getReportFactory ().setCurrentBand (band);
  }

  /**
   * SAX-Handler function that is forwarded from the ReportDefinitionContentHandler.
   * StartTag-occurences for ReportFooter and -header, PageFooter and -header and
   * the itemBand are handled. If an unknown element is encountered, a SAXException is
   * thrown.
   *
   * @param namespaceURI  the namespace URI.
   * @param localName  the local name.
   * @param qName  the element name.
   * @param atts  the element attributes.
   *
   * @throws SAXException if an unknown tag is encountered.
   */
  public void startElement (String namespaceURI,
                            String localName,
                            String qName,
                            Attributes atts) throws SAXException
  {
    String elementName = qName.toLowerCase ().trim ();
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
      throw new SAXException ("Expected one of: reportheader, reportfooter, pageheader, "
                            + "pagefooter or items");
    }
  }


  /**
   * SAX-Handler function that is forwarded from the ReportDefinitionContentHandler.
   * EndTag-occurences for ReportFooter and -header, PageFooter and -header and
   * the itemBand are handled. If an unknown element is encountered, a SAXException is
   * thrown.
   *
   * @param namespaceURI  the namespace URI.
   * @param localName  the local name.
   * @param qName  the element name.
   *
   * @throws SAXException if an unknown tag is encountered.
   */
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
      throw new SAXException ("Expected one of: reportheader, reportfooter, pageheader, "
                            + "pagefooter or items");
    }
  }

  /**
   * Handles the start of a reportheader definition.
   *
   * @param attr  the element attributes.
   *
   * @throws SAXException if there is a parsing problem.
   *
   * @see com.jrefinery.report.ReportHeader
   */
  public void startReportHeader (Attributes attr)
          throws SAXException
  {
    float height = ParserUtil.parseFloat (attr.getValue ("height"),
                                          "Element height not specified");
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

  /**
   * Handles the start of a reportfooter definition.
   *
   * @param attr  the element attributes.
   *
   * @throws SAXException if there is a parsing problem.
   *
   * @see com.jrefinery.report.ReportFooter
   */
  public void startReportFooter (Attributes attr)
          throws SAXException
  {
    // get the height...
    float height = ParserUtil.parseFloat (attr.getValue ("height"),
                                          "Element height not specified");
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

  /**
   * Handles the start of a pageheader definition.
   *
   * @param attr  the element attributes.
   *
   * @throws SAXException if there is a parsing problem.
   *
   * @see com.jrefinery.report.PageHeader
   */
  public void startPageHeader (Attributes attr)
          throws SAXException
  {
    // get the height...
    float height = ParserUtil.parseFloat (attr.getValue ("height"),
                                          "Element height not specified");
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

  /**
   * Handles the start of a pagefooter definition.
   *
   * @param attr  the element attributes.
   *
   * @throws SAXException if there is a parsing problem.
   *
   * @see com.jrefinery.report.PageFooter
   */
  public void startPageFooter (Attributes attr)
          throws SAXException
  {
    // get the height...
    float height = ParserUtil.parseFloat (attr.getValue ("height"),
                                          "Element height not specified");
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

  /**
   * Handles the start of an ItemBand definition.
   *
   * @param attr  the element attributes.
   *
   * @throws SAXException if there is a parsing problem.
   *
   * @see com.jrefinery.report.ItemBand
   */
  public void startItems (Attributes attr)
          throws SAXException
  {
    // get the height...
    float height = ParserUtil.parseFloat (attr.getValue ("height"),
                                          "Element height not specified");
    ItemBand items = new ItemBand ();
    items.setHeight (height);
    items.setDefaultFont (fontFactory.createDefaultFont (attr));
    setCurrentBand (items);
    getReport ().setItemBand (items);
    handler.setExpectedHandler (handler.createElementFactory ());
  }

  /**
   * Handles the end of an ItemBand definition.
   *
   * @see com.jrefinery.report.ItemBand
   */
  public void endItems ()
  {
    handler.finishedHandler ();
  }

  /**
   * Handles the end of a PageHeader definition.
   *
   * @see com.jrefinery.report.PageHeader
   */
  public void endPageHeader ()
  {
    handler.finishedHandler ();
  }

  /**
   * Handles the end of a PageFooter definition.
   *
   * @see com.jrefinery.report.PageFooter
   */
  public void endPageFooter ()
  {
    handler.finishedHandler ();
  }

  /**
   * Handles the end of a ReportHeader definition.
   *
   * @see com.jrefinery.report.ReportHeader
   */
  public void endReportHeader ()
  {
    handler.finishedHandler ();
  }

  /**
   * Handles the end of a ReportFooter definition.
   *
   * @see com.jrefinery.report.ReportFooter
   */
  public void endReportFooter ()
  {
    handler.finishedHandler ();
  }

}
