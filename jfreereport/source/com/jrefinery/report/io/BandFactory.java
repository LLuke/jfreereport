/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
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
 * (C)opyright 2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner (taquera@sherito.org);
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: BandFactory.java,v 1.9 2002/12/02 17:30:36 taqua Exp $
 *
 * Changes
 * -------
 * 10-May-2002 : Initial version
 * 22-May-2002 : Structured parsing functions: all tags have a startXXX and endXXX function
 * 05-Jun-2002 : Updated Javadoc comments (DG);
 * 10-Dec-2002 : Fixed issues reported by Checkstyle (DG);
 *
 */

package com.jrefinery.report.io;

import com.jrefinery.report.Band;
import com.jrefinery.report.ItemBand;
import com.jrefinery.report.JFreeReport;
import com.jrefinery.report.PageFooter;
import com.jrefinery.report.PageHeader;
import com.jrefinery.report.ReportFooter;
import com.jrefinery.report.ReportHeader;
import com.jrefinery.report.ElementAlignment;
import com.jrefinery.report.targets.FloatDimension;
import com.jrefinery.report.targets.style.BandStyleSheet;
import com.jrefinery.report.targets.style.ElementStyleSheet;
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
 * @author Thomas Morgner
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
   * @see ReportHeader
   */
  public void startReportHeader (Attributes attr)
          throws SAXException
  {
    float height = ParserUtil.parseFloat (attr.getValue ("height"),
                                          "Element height not specified");
    boolean ownPage = ParserUtil.parseBoolean (attr.getValue ("ownpage"), false);

    // create the report header...
    ReportHeader reportHeader = new ReportHeader ();
    reportHeader.getStyle().setStyleProperty(ElementStyleSheet.MINIMUMSIZE,
                                             new FloatDimension(0, height));
    reportHeader.getStyle().setStyleProperty(BandStyleSheet.PAGEBREAK_AFTER, new Boolean (ownPage));
    reportHeader.getBandDefaults().setFontStyleProperty(fontFactory.createDefaultFont (attr));
    String valign = attr.getValue(VALIGNMENT_ATT);
    if (valign != null)
    {
      reportHeader.getBandDefaults().setStyleProperty(ElementStyleSheet.VALIGNMENT,
          ParserUtil.parseVerticalElementAlignment(valign));
    }
    String halign = attr.getValue(ALIGNMENT_ATT);
    if (halign != null)
    {
      reportHeader.getBandDefaults().setStyleProperty(ElementStyleSheet.ALIGNMENT,
          ParserUtil.parseHorizontalElementAlignment(halign));
    }

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
   * @see ReportFooter
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
    reportFooter.getStyle().setStyleProperty(ElementStyleSheet.MINIMUMSIZE,
                                             new FloatDimension(0, height));
    reportFooter.getStyle().setStyleProperty(BandStyleSheet.PAGEBREAK_BEFORE,
                                             new Boolean (ownPage));
    reportFooter.getBandDefaults().setFontStyleProperty(fontFactory.createDefaultFont (attr));
    String valign = attr.getValue(VALIGNMENT_ATT);
    if (valign != null)
    {
      reportFooter.getBandDefaults().setStyleProperty(ElementStyleSheet.VALIGNMENT,
          ParserUtil.parseVerticalElementAlignment(valign));
    }
    String halign = attr.getValue(ALIGNMENT_ATT);
    if (halign != null)
    {
      reportFooter.getBandDefaults().setStyleProperty(ElementStyleSheet.ALIGNMENT,
          ParserUtil.parseHorizontalElementAlignment(halign));
    }

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
   * @see PageHeader
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
    pageHeader.getStyle().setStyleProperty(ElementStyleSheet.MINIMUMSIZE,
                                           new FloatDimension(0, height));
    pageHeader.setDisplayOnFirstPage (firstPage);
    pageHeader.setDisplayOnLastPage (lastPage);
    pageHeader.getBandDefaults().setFontStyleProperty(fontFactory.createDefaultFont (attr));
    String valign = attr.getValue(VALIGNMENT_ATT);
    if (valign != null)
    {
      pageHeader.getBandDefaults().setStyleProperty(ElementStyleSheet.VALIGNMENT,
          ParserUtil.parseVerticalElementAlignment(valign));
    }
    String halign = attr.getValue(ALIGNMENT_ATT);
    if (halign != null)
    {
      pageHeader.getBandDefaults().setStyleProperty(ElementStyleSheet.ALIGNMENT,
          ParserUtil.parseHorizontalElementAlignment(halign));
    }

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
   * @see PageFooter
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
    pageFooter.getStyle().setStyleProperty(ElementStyleSheet.MINIMUMSIZE,
                                           new FloatDimension(0, height));
    pageFooter.setDisplayOnFirstPage (firstPage);
    pageFooter.setDisplayOnLastPage (lastPage);
    pageFooter.getBandDefaults().setFontStyleProperty(fontFactory.createDefaultFont (attr));
    String valign = attr.getValue(VALIGNMENT_ATT);
    if (valign != null)
    {
      pageFooter.getBandDefaults().setStyleProperty(ElementStyleSheet.VALIGNMENT,
          ParserUtil.parseVerticalElementAlignment(valign));
    }
    String halign = attr.getValue(ALIGNMENT_ATT);
    if (halign != null)
    {
      pageFooter.getBandDefaults().setStyleProperty(ElementStyleSheet.ALIGNMENT,
          ParserUtil.parseHorizontalElementAlignment(halign));
    }

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
   * @see ItemBand
   */
  public void startItems (Attributes attr)
          throws SAXException
  {
    // get the height...
    float height = ParserUtil.parseFloat (attr.getValue ("height"),
                                          "Element height not specified");
    ItemBand items = new ItemBand ();
    items.getStyle().setStyleProperty(ElementStyleSheet.MINIMUMSIZE, new FloatDimension(0, height));
    items.getBandDefaults().setFontStyleProperty(fontFactory.createDefaultFont (attr));
    String valign = attr.getValue(VALIGNMENT_ATT);
    if (valign != null)
    {
      items.getBandDefaults().setStyleProperty(ElementStyleSheet.VALIGNMENT,
                                               ParserUtil.parseVerticalElementAlignment(valign));
    }
    String halign = attr.getValue(ALIGNMENT_ATT);
    if (halign != null)
    {
      items.getBandDefaults().setStyleProperty(ElementStyleSheet.ALIGNMENT,
                                               ParserUtil.parseHorizontalElementAlignment(halign));
    }

    setCurrentBand (items);
    getReport ().setItemBand (items);
    handler.setExpectedHandler (handler.createElementFactory ());
  }

  /**
   * Handles the end of an ItemBand definition.
   *
   * @see ItemBand
   */
  public void endItems ()
  {
    handler.finishedHandler ();
  }

  /**
   * Handles the end of a PageHeader definition.
   *
   * @see PageHeader
   */
  public void endPageHeader ()
  {
    handler.finishedHandler ();
  }

  /**
   * Handles the end of a PageFooter definition.
   *
   * @see PageFooter
   */
  public void endPageFooter ()
  {
    handler.finishedHandler ();
  }

  /**
   * Handles the end of a ReportHeader definition.
   *
   * @see ReportHeader
   */
  public void endReportHeader ()
  {
    handler.finishedHandler ();
  }

  /**
   * Handles the end of a ReportFooter definition.
   *
   * @see ReportFooter
   */
  public void endReportFooter ()
  {
    handler.finishedHandler ();
  }
}
