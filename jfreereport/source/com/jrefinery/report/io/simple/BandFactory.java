/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
 *
 * (C) Copyright 2000-2003, by Simba Management Limited and Contributors.
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
 * (C)opyright 2002, 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner (taquera@sherito.org);
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: BandFactory.java,v 1.13 2003/06/10 12:11:54 taqua Exp $
 *
 * Changes
 * -------
 * 10-May-2002 : Initial version
 * 22-May-2002 : Structured parsing functions: all tags have a startXXX and endXXX function
 * 05-Jun-2002 : Updated Javadoc comments (DG);
 * 10-Dec-2002 : Fixed issues reported by Checkstyle (DG);
 *
 */

package com.jrefinery.report.io.simple;

import com.jrefinery.report.ItemBand;
import com.jrefinery.report.PageFooter;
import com.jrefinery.report.PageHeader;
import com.jrefinery.report.ReportFooter;
import com.jrefinery.report.ReportHeader;
import com.jrefinery.report.io.ReportParserUtil;
import com.jrefinery.report.targets.style.BandStyleSheet;
import com.jrefinery.report.targets.style.ElementStyleSheet;
import org.jfree.ui.FloatDimension;
import org.jfree.xml.Parser;
import org.jfree.xml.ParserUtil;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * This class handles the SAX events generated for report bands.
 * <p>
 * Recognized root bands are:
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
public class BandFactory extends AbstractReportDefinitionHandler implements ReportDefinitionTags
{
  /**
   * The FontFactory is used to create java.awt.Font instances based on the data given
   * in the Band and Element definition.
   */
  private FontFactory fontFactory;

  /**
   * Initializes this BandFactory based on the data contained in the ReportFactory.
   *
   * @param parser the used parser to coordinate the parsing process.
   * @param finishTag the finish tag, that should trigger the deactivation of this parser.
   * @throws NullPointerException if the finishTag or the parser are null.
   */
  public BandFactory(Parser parser, String finishTag)
  {
    super(parser, finishTag);
    fontFactory = new FontFactory();
  }

  /**
   * SAX-Handler function that is forwarded from the ReportDefinitionContentHandler.
   * StartTag-occurences for ReportFooter and -header, PageFooter and -header and
   * the itemBand are handled. If an unknown element is encountered, a SAXException is
   * thrown.
   *
   * @param qName  the element name.
   * @param atts  the element attributes.
   *
   * @throws SAXException if an unknown tag is encountered.
   */
  public void startElement(String qName,
                           Attributes atts) throws SAXException
  {
    String elementName = qName.toLowerCase().trim();
    if (elementName.equals(REPORT_HEADER_TAG))
    {
      startReportHeader(atts);
    }
    // *** REPORT FOOTER ***
    else if (elementName.equals(REPORT_FOOTER_TAG))
    {
      startReportFooter(atts);
    }
    // *** PAGE HEADER ***
    else if (elementName.equals(PAGE_HEADER_TAG))
    {
      startPageHeader(atts);
    }
    // *** PAGE FOOTER ***
    else if (elementName.equals(PAGE_FOOTER_TAG))
    {
      startPageFooter(atts);
    }
    else if (elementName.equals(ITEMS_TAG))
    {
      startItems(atts);
    }
    else
    {
      throw new SAXException("Expected one of: reportheader, reportfooter, pageheader, "
          + "pagefooter or items. " + qName + " - " + getFinishTag());
    }
  }


  /**
   * SAX-Handler function that is forwarded from the ReportDefinitionContentHandler.
   * EndTag-occurences for ReportFooter and -header, PageFooter and -header and
   * the itemBand are handled. If an unknown element is encountered, a SAXException is
   * thrown.
   *
   * @param qName  the element name.
   *
   * @throws SAXException if an unknown tag is encountered.
   */
  public void endElement(String qName) throws SAXException
  {
    String elementName = qName.toLowerCase().trim();
    if (elementName.equals(REPORT_HEADER_TAG))
    {
      endReportHeader();
    }
    // *** REPORT FOOTER ***
    else if (elementName.equals(REPORT_FOOTER_TAG))
    {
      endReportFooter();
    }
    // *** PAGE HEADER ***
    else if (elementName.equals(PAGE_HEADER_TAG))
    {
      endPageHeader();
    }
    // *** PAGE FOOTER ***
    else if (elementName.equals(PAGE_FOOTER_TAG))
    {
      endPageFooter();
    }
    else if (elementName.equals(ITEMS_TAG))
    {
      endItems();
    }
    else if (elementName.equals(getFinishTag()))
    {
      getParser().popFactory().endElement(qName);
    }
    else
    {
      throw new SAXException("Expected one of: reportheader, reportfooter, pageheader, "
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
  public void startReportHeader(Attributes attr)
      throws SAXException
  {
    float height = ParserUtil.parseFloat(attr.getValue("height"), 0);
    boolean ownPage = ParserUtil.parseBoolean(attr.getValue("ownpage"), false);

    // create the report header...
    ReportHeader reportHeader = new ReportHeader();
    reportHeader.getStyle().setStyleProperty(ElementStyleSheet.MINIMUMSIZE,
        new FloatDimension(0, height));
    reportHeader.getStyle().setBooleanStyleProperty
        (BandStyleSheet.PAGEBREAK_AFTER, ownPage);

    FontFactory.FontInformation fi = fontFactory.createFont(attr);
    FontFactory.applyFontInformation(reportHeader.getStyle(), fi);
    FontFactory.applyFontInformation(reportHeader.getBandDefaults(), fi);

    String valign = attr.getValue(VALIGNMENT_ATT);
    if (valign != null)
    {
      reportHeader.getBandDefaults().setStyleProperty(ElementStyleSheet.VALIGNMENT,
          ReportParserUtil.parseVerticalElementAlignment(valign));
    }
    String halign = attr.getValue(ALIGNMENT_ATT);
    if (halign != null)
    {
      reportHeader.getBandDefaults().setStyleProperty(ElementStyleSheet.ALIGNMENT,
          ReportParserUtil.parseHorizontalElementAlignment(halign));
    }

    getReport().setReportHeader(reportHeader);
    getParser().pushFactory(new ElementFactory(getParser(), REPORT_HEADER_TAG, reportHeader));
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
  public void startReportFooter(Attributes attr)
      throws SAXException
  {
    // get the height...
    float height = ParserUtil.parseFloat(attr.getValue("height"), 0);
    boolean ownPage = ParserUtil.parseBoolean(attr.getValue("ownpage"), false);

    // create the report footer...
    ReportFooter reportFooter = new ReportFooter();
    reportFooter.getStyle().setStyleProperty(ElementStyleSheet.MINIMUMSIZE,
        new FloatDimension(0, height));
    reportFooter.getStyle().setBooleanStyleProperty
        (BandStyleSheet.PAGEBREAK_BEFORE, ownPage);
    FontFactory.FontInformation fi = fontFactory.createFont(attr);
    FontFactory.applyFontInformation(reportFooter.getStyle(), fi);
    FontFactory.applyFontInformation(reportFooter.getBandDefaults(), fi);

    String valign = attr.getValue(VALIGNMENT_ATT);
    if (valign != null)
    {
      reportFooter.getBandDefaults().setStyleProperty(ElementStyleSheet.VALIGNMENT,
          ReportParserUtil.parseVerticalElementAlignment(valign));
    }
    String halign = attr.getValue(ALIGNMENT_ATT);
    if (halign != null)
    {
      reportFooter.getBandDefaults().setStyleProperty(ElementStyleSheet.ALIGNMENT,
          ReportParserUtil.parseHorizontalElementAlignment(halign));
    }

    getReport().setReportFooter(reportFooter);
    getParser().pushFactory(new ElementFactory(getParser(), REPORT_FOOTER_TAG, reportFooter));
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
  public void startPageHeader(Attributes attr)
      throws SAXException
  {
    // get the height...
    float height = ParserUtil.parseFloat(attr.getValue("height"), 0);
    boolean firstPage = ParserUtil.parseBoolean(attr.getValue("onfirstpage"), true);
    boolean lastPage = ParserUtil.parseBoolean(attr.getValue("onlastpage"), true);

    // create the page header...
    PageHeader pageHeader = new PageHeader();
    pageHeader.getStyle().setStyleProperty(ElementStyleSheet.MINIMUMSIZE,
        new FloatDimension(0, height));
    pageHeader.setDisplayOnFirstPage(firstPage);
    pageHeader.setDisplayOnLastPage(lastPage);

    FontFactory.FontInformation fi = fontFactory.createFont(attr);
    FontFactory.applyFontInformation(pageHeader.getStyle(), fi);
    FontFactory.applyFontInformation(pageHeader.getBandDefaults(), fi);

    String valign = attr.getValue(VALIGNMENT_ATT);
    if (valign != null)
    {
      pageHeader.getBandDefaults().setStyleProperty(ElementStyleSheet.VALIGNMENT,
          ReportParserUtil.parseVerticalElementAlignment(valign));
    }
    String halign = attr.getValue(ALIGNMENT_ATT);
    if (halign != null)
    {
      pageHeader.getBandDefaults().setStyleProperty(ElementStyleSheet.ALIGNMENT,
          ReportParserUtil.parseHorizontalElementAlignment(halign));
    }

    getReport().setPageHeader(pageHeader);
    getParser().pushFactory(new ElementFactory(getParser(), PAGE_HEADER_TAG, pageHeader));
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
  public void startPageFooter(Attributes attr)
      throws SAXException
  {
    // get the height...
    float height = ParserUtil.parseFloat(attr.getValue("height"), 0);
    boolean firstPage = ParserUtil.parseBoolean(attr.getValue("onfirstpage"), true);
    boolean lastPage = ParserUtil.parseBoolean(attr.getValue("onlastpage"), true);

    // create the page footer...
    PageFooter pageFooter = new PageFooter();
    pageFooter.getStyle().setStyleProperty(ElementStyleSheet.MINIMUMSIZE,
        new FloatDimension(0, height));
    pageFooter.setDisplayOnFirstPage(firstPage);
    pageFooter.setDisplayOnLastPage(lastPage);

    FontFactory.FontInformation fi = fontFactory.createFont(attr);
    FontFactory.applyFontInformation(pageFooter.getStyle(), fi);
    FontFactory.applyFontInformation(pageFooter.getBandDefaults(), fi);

    String valign = attr.getValue(VALIGNMENT_ATT);
    if (valign != null)
    {
      pageFooter.getBandDefaults().setStyleProperty(ElementStyleSheet.VALIGNMENT,
          ReportParserUtil.parseVerticalElementAlignment(valign));
    }
    String halign = attr.getValue(ALIGNMENT_ATT);
    if (halign != null)
    {
      pageFooter.getBandDefaults().setStyleProperty(ElementStyleSheet.ALIGNMENT,
          ReportParserUtil.parseHorizontalElementAlignment(halign));
    }

    getReport().setPageFooter(pageFooter);
    getParser().pushFactory(new ElementFactory(getParser(), PAGE_FOOTER_TAG, pageFooter));
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
  public void startItems(Attributes attr)
      throws SAXException
  {
    // get the height...
    float height = ParserUtil.parseFloat(attr.getValue("height"), 0);
    ItemBand items = new ItemBand();
    items.getStyle().setStyleProperty
        (ElementStyleSheet.MINIMUMSIZE, new FloatDimension(0, height));
    FontFactory.FontInformation fi = fontFactory.createFont(attr);
    FontFactory.applyFontInformation(items.getStyle(), fi);
    FontFactory.applyFontInformation(items.getBandDefaults(), fi);

    String valign = attr.getValue(VALIGNMENT_ATT);
    if (valign != null)
    {
      items.getBandDefaults().setStyleProperty
          (ElementStyleSheet.VALIGNMENT, ReportParserUtil.parseVerticalElementAlignment(valign));
    }
    String halign = attr.getValue(ALIGNMENT_ATT);
    if (halign != null)
    {
      items.getBandDefaults().setStyleProperty
          (ElementStyleSheet.ALIGNMENT, ReportParserUtil.parseHorizontalElementAlignment(halign));
    }

    getReport().setItemBand(items);
    getParser().pushFactory(new ElementFactory(getParser(), ITEMS_TAG, items));
  }

  /**
   * Handles the end of an ItemBand definition.
   *
   * @see com.jrefinery.report.ItemBand
   *
   * @throws SAXException if a Parser error occurs.
   */
  public void endItems() throws SAXException
  {
    getParser().popFactory().endElement(ITEMS_TAG);
  }

  /**
   * Handles the end of a PageHeader definition.
   *
   * @see com.jrefinery.report.PageHeader
   *
   * @throws SAXException if a parser error occurs.
   */
  public void endPageHeader() throws SAXException
  {
    getParser().popFactory().endElement(PAGE_HEADER_TAG);
  }

  /**
   * Handles the end of a PageFooter definition.
   *
   * @see com.jrefinery.report.PageFooter
   *
   * @throws SAXException if a parser error occurs.
   */
  private void endPageFooter() throws SAXException
  {
    getParser().popFactory().endElement(PAGE_FOOTER_TAG);
  }

  /**
   * Handles the end of a ReportHeader definition.
   *
   * @see com.jrefinery.report.ReportHeader
   *
   * @throws SAXException if a parser error occurs.
   */
  private void endReportHeader() throws SAXException
  {
    getParser().popFactory().endElement(REPORT_HEADER_TAG);
  }

  /**
   * Handles the end of a ReportFooter definition.
   *
   * @see com.jrefinery.report.ReportFooter
   *
   * @throws SAXException if a parser error occurs.
   */
  private void endReportFooter() throws SAXException
  {
    getParser().popFactory().endElement(REPORT_FOOTER_TAG);
  }
}
