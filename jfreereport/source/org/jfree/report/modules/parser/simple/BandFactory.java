/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
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
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: BandFactory.java,v 1.7.2.1.2.3 2004/10/13 18:42:23 taqua Exp $
 *
 * Changes
 * -------
 * 10-May-2002 : Initial version
 * 22-May-2002 : Structured parsing functions: all tags have a startXXX and endXXX function
 * 05-Jun-2002 : Updated Javadoc comments (DG);
 * 10-Dec-2002 : Fixed issues reported by Checkstyle (DG);
 *
 */

package org.jfree.report.modules.parser.simple;

import org.jfree.report.Band;
import org.jfree.report.ItemBand;
import org.jfree.report.PageFooter;
import org.jfree.report.PageHeader;
import org.jfree.report.ReportFooter;
import org.jfree.report.ReportHeader;
import org.jfree.report.modules.parser.base.ReportParser;
import org.jfree.report.modules.parser.base.ReportParserUtil;
import org.jfree.report.style.BandStyleSheet;
import org.jfree.report.style.ElementStyleSheet;
import org.jfree.ui.FloatDimension;
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
public class BandFactory extends AbstractReportDefinitionHandler
    implements ReportDefinitionTags
{
  /**
   * Initializes this BandFactory based on the data contained in the ReportFactory.
   *
   * @param parser the used parser to coordinate the parsing process.
   * @param finishTag the finish tag, that should trigger the deactivation of this parser.
   * @throws NullPointerException if the finishTag or the parser are null.
   */
  public BandFactory(final ReportParser parser, final String finishTag)
  {
    super(parser, finishTag);
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
  public void startElement(final String qName,
                           final Attributes atts) throws SAXException
  {
    final String elementName = qName.toLowerCase().trim();
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
    else if (elementName.equals(WATERMARK_TAG))
    {
      startWatermark(atts);
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
  public void endElement(final String qName) throws SAXException
  {
    final String elementName = qName.toLowerCase().trim();
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
    else if (elementName.equals(WATERMARK_TAG))
    {
      endWatermark();
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
          + "pagefooter or items, but found " + qName);
    }
  }

  /**
   * Handles the start of a reportheader definition.
   *
   * @param attr  the element attributes.
   *
   * @throws SAXException if there is a parsing problem.
   *
   * @see org.jfree.report.ReportHeader
   */
  private void startReportHeader(final Attributes attr)
      throws SAXException
  {
    // create the report header...
    final ReportHeader reportHeader = getReport().getReportHeader();

    final String height = attr.getValue("height");
    if (height != null)
    {
      final float heightValue = ParserUtil.parseFloat(height, 0);
      reportHeader.getStyle().setStyleProperty(ElementStyleSheet.MINIMUMSIZE,
          new FloatDimension(0, heightValue));
    }

    final String fixedPos = attr.getValue("fixed-position");
    if (fixedPos != null)
    {
      final float fixedPosValue = ParserUtil.parseFloat
              (fixedPos, "FixedPosition is invalid!");
      reportHeader.getStyle().setStyleProperty(BandStyleSheet.FIXED_POSITION,
              new Float (fixedPosValue));
    }

    String ownPageAttr = attr.getValue("ownpage");
    if (ownPageAttr == null)
    {
      ownPageAttr = attr.getValue("pagebreak-after");
    }
    if (ownPageAttr != null)
    {
      final boolean ownPage = ParserUtil.parseBoolean(ownPageAttr, false);

      reportHeader.getStyle().setBooleanStyleProperty
          (BandStyleSheet.PAGEBREAK_AFTER, ownPage);
    }

    final FontFactory.FontInformation fi = FontFactory.createFont(attr);
    FontFactory.applyFontInformation(reportHeader.getBandDefaults(), fi);

    final String valign = attr.getValue(VALIGNMENT_ATT);
    if (valign != null)
    {
      reportHeader.getBandDefaults().setStyleProperty(ElementStyleSheet.VALIGNMENT,
          ReportParserUtil.parseVerticalElementAlignment(valign));
    }
    final String halign = attr.getValue(ALIGNMENT_ATT);
    if (halign != null)
    {
      reportHeader.getBandDefaults().setStyleProperty(ElementStyleSheet.ALIGNMENT,
          ReportParserUtil.parseHorizontalElementAlignment(halign));
    }

    getParser().pushFactory(new ElementFactory(getReportParser(), REPORT_HEADER_TAG, reportHeader));
  }

  /**
   * Handles the start of a reportfooter definition.
   *
   * @param attr  the element attributes.
   *
   * @throws SAXException if there is a parsing problem.
   *
   * @see org.jfree.report.ReportFooter
   */
  private void startReportFooter(final Attributes attr)
      throws SAXException
  {
    // create the report footer...
    final ReportFooter reportFooter = getReport().getReportFooter();

    // get the height...
    final String height = attr.getValue("height");
    if (height != null)
    {
      final float heightValue = ParserUtil.parseFloat(height, 0);
      reportFooter.getStyle().setStyleProperty(ElementStyleSheet.MINIMUMSIZE,
          new FloatDimension(0, heightValue));
    }

    final String fixedPos = attr.getValue("fixed-position");
    if (fixedPos != null)
    {
      final float fixedPosValue = ParserUtil.parseFloat
              (fixedPos, "FixedPosition is invalid!");
      reportFooter.getStyle().setStyleProperty(BandStyleSheet.FIXED_POSITION,
              new Float (fixedPosValue));
    }

    String ownPageAttr = attr.getValue("ownpage");
    if (ownPageAttr == null)
    {
      ownPageAttr = attr.getValue("pagebreak-before");
    }
    if (ownPageAttr != null)
    {
      final boolean ownPage = ParserUtil.parseBoolean(ownPageAttr, false);

      reportFooter.getStyle().setBooleanStyleProperty
          (BandStyleSheet.PAGEBREAK_BEFORE, ownPage);
    }



    final FontFactory.FontInformation fi = FontFactory.createFont(attr);
    FontFactory.applyFontInformation(reportFooter.getBandDefaults(), fi);

    final String valign = attr.getValue(VALIGNMENT_ATT);
    if (valign != null)
    {
      reportFooter.getBandDefaults().setStyleProperty(ElementStyleSheet.VALIGNMENT,
          ReportParserUtil.parseVerticalElementAlignment(valign));
    }
    final String halign = attr.getValue(ALIGNMENT_ATT);
    if (halign != null)
    {
      reportFooter.getBandDefaults().setStyleProperty(ElementStyleSheet.ALIGNMENT,
          ReportParserUtil.parseHorizontalElementAlignment(halign));
    }

    getParser().pushFactory(new ElementFactory(getReportParser(), REPORT_FOOTER_TAG, reportFooter));
  }

  /**
   * Handles the start of a pageheader definition.
   *
   * @param attr  the element attributes.
   *
   * @throws SAXException if there is a parsing problem.
   *
   * @see org.jfree.report.PageHeader
   */
  private void startPageHeader(final Attributes attr)
      throws SAXException
  {
    // create the page header...
    final PageHeader pageHeader = getReport().getPageHeader();

    // get the height...
    final String heightAttr = attr.getValue("height");
    if (heightAttr != null)
    {
      final float height = ParserUtil.parseFloat(heightAttr, 0);
      pageHeader.getStyle().setStyleProperty(ElementStyleSheet.MINIMUMSIZE,
          new FloatDimension(0, height));
    }

    final String firstPageAttr = attr.getValue("onfirstpage");
    if (firstPageAttr != null)
    {
      final boolean firstPage = ParserUtil.parseBoolean(firstPageAttr, true);
      pageHeader.setDisplayOnFirstPage(firstPage);
    }

    final String lastPageAttr = attr.getValue("onlastpage");
    if (lastPageAttr != null)
    {
      final boolean lastPage = ParserUtil.parseBoolean(lastPageAttr, true);
      pageHeader.setDisplayOnLastPage(lastPage);
    }

    final FontFactory.FontInformation fi = FontFactory.createFont(attr);
    FontFactory.applyFontInformation(pageHeader.getBandDefaults(), fi);

    final String valign = attr.getValue(VALIGNMENT_ATT);
    if (valign != null)
    {
      pageHeader.getBandDefaults().setStyleProperty(ElementStyleSheet.VALIGNMENT,
          ReportParserUtil.parseVerticalElementAlignment(valign));
    }
    final String halign = attr.getValue(ALIGNMENT_ATT);
    if (halign != null)
    {
      pageHeader.getBandDefaults().setStyleProperty(ElementStyleSheet.ALIGNMENT,
          ReportParserUtil.parseHorizontalElementAlignment(halign));
    }

    getParser().pushFactory(new ElementFactory(getReportParser(), PAGE_HEADER_TAG, pageHeader));
  }

  /**
   * Handles the start of a pagefooter definition.
   *
   * @param attr  the element attributes.
   *
   * @throws SAXException if there is a parsing problem.
   *
   * @see org.jfree.report.PageFooter
   */
  private void startPageFooter(final Attributes attr)
      throws SAXException
  {
    // create the page header...
    final PageFooter pageFooter = getReport().getPageFooter();

    // get the height...
    final String heightAttr = attr.getValue("height");
    if (heightAttr != null)
    {
      final float height = ParserUtil.parseFloat(heightAttr, 0);
      pageFooter.getStyle().setStyleProperty(ElementStyleSheet.MINIMUMSIZE,
          new FloatDimension(0, height));
    }

    final String firstPageAttr = attr.getValue("onfirstpage");
    if (firstPageAttr != null)
    {
      final boolean firstPage = ParserUtil.parseBoolean(firstPageAttr, true);
      pageFooter.setDisplayOnFirstPage(firstPage);
    }

    final String lastPageAttr = attr.getValue("onlastpage");
    if (lastPageAttr != null)
    {
      final boolean lastPage = ParserUtil.parseBoolean(lastPageAttr, true);
      pageFooter.setDisplayOnLastPage(lastPage);
    }

    final FontFactory.FontInformation fi = FontFactory.createFont(attr);
    FontFactory.applyFontInformation(pageFooter.getBandDefaults(), fi);

    final String valign = attr.getValue(VALIGNMENT_ATT);
    if (valign != null)
    {
      pageFooter.getBandDefaults().setStyleProperty(ElementStyleSheet.VALIGNMENT,
          ReportParserUtil.parseVerticalElementAlignment(valign));
    }
    final String halign = attr.getValue(ALIGNMENT_ATT);
    if (halign != null)
    {
      pageFooter.getBandDefaults().setStyleProperty(ElementStyleSheet.ALIGNMENT,
          ReportParserUtil.parseHorizontalElementAlignment(halign));
    }

    getReport().setPageFooter(pageFooter);
    getParser().pushFactory(new ElementFactory(getReportParser(), PAGE_FOOTER_TAG, pageFooter));
  }

  /**
   * Handles the start of an Watermark definition.
   *
   * @param attr  the element attributes.
   *
   * @throws SAXException if there is a parsing problem.
   *
   * @see org.jfree.report.Watermark
   */
  private void startWatermark(final Attributes attr)
      throws SAXException
  {
    final Band watermark = getReport().getWatermark();
    final String heightAttr = attr.getValue("height");
    if (heightAttr != null)
    {
      // get the height...
      final float height = ParserUtil.parseFloat(heightAttr, 0);

      watermark.getStyle().setStyleProperty
          (ElementStyleSheet.MINIMUMSIZE, new FloatDimension(0, height));
    }

    final FontFactory.FontInformation fi = FontFactory.createFont(attr);
    FontFactory.applyFontInformation(watermark.getBandDefaults(), fi);

    final String valign = attr.getValue(VALIGNMENT_ATT);
    if (valign != null)
    {
      watermark.getBandDefaults().setStyleProperty
          (ElementStyleSheet.VALIGNMENT, ReportParserUtil.parseVerticalElementAlignment(valign));
    }
    final String halign = attr.getValue(ALIGNMENT_ATT);
    if (halign != null)
    {
      watermark.getBandDefaults().setStyleProperty
          (ElementStyleSheet.ALIGNMENT, ReportParserUtil.parseHorizontalElementAlignment(halign));
    }
    getParser().pushFactory(new ElementFactory(getReportParser(), WATERMARK_TAG, watermark));
  }

  /**
   * Handles the start of an ItemBand definition.
   *
   * @param attr  the element attributes.
   *
   * @throws SAXException if there is a parsing problem.
   *
   * @see org.jfree.report.ItemBand
   */
  private void startItems(final Attributes attr)
      throws SAXException
  {
    final ItemBand items = getReport().getItemBand();
    final String heightAttr = attr.getValue("height");
    if (heightAttr != null)
    {
      // get the height...
      final float height = ParserUtil.parseFloat(heightAttr, 0);

      items.getStyle().setStyleProperty
          (ElementStyleSheet.MINIMUMSIZE, new FloatDimension(0, height));
    }

    final String fixedPos = attr.getValue("fixed-position");
    if (fixedPos != null)
    {
      final float fixedPosValue = ParserUtil.parseFloat
              (fixedPos, "FixedPosition is invalid!");
      items.getStyle().setStyleProperty(BandStyleSheet.FIXED_POSITION,
              new Float (fixedPosValue));
    }

    final FontFactory.FontInformation fi = FontFactory.createFont(attr);
    FontFactory.applyFontInformation(items.getBandDefaults(), fi);

    final String valign = attr.getValue(VALIGNMENT_ATT);
    if (valign != null)
    {
      items.getBandDefaults().setStyleProperty
          (ElementStyleSheet.VALIGNMENT, ReportParserUtil.parseVerticalElementAlignment(valign));
    }
    final String halign = attr.getValue(ALIGNMENT_ATT);
    if (halign != null)
    {
      items.getBandDefaults().setStyleProperty
          (ElementStyleSheet.ALIGNMENT, ReportParserUtil.parseHorizontalElementAlignment(halign));
    }
    getParser().pushFactory(new ElementFactory(getReportParser(), ITEMS_TAG, items));
  }

  /**
   * Handles the end of an Watermark definition.
   *
   * @throws SAXException if a Parser error occurs.
   */
  private void endWatermark() throws SAXException
  {
    getParser().popFactory().endElement(WATERMARK_TAG);
  }

  /**
   * Handles the end of an ItemBand definition.
   *
   * @see org.jfree.report.ItemBand
   *
   * @throws SAXException if a Parser error occurs.
   */
  private void endItems() throws SAXException
  {
    getParser().popFactory().endElement(ITEMS_TAG);
  }

  /**
   * Handles the end of a PageHeader definition.
   *
   * @see org.jfree.report.PageHeader
   *
   * @throws SAXException if a parser error occurs.
   */
  private void endPageHeader() throws SAXException
  {
    getParser().popFactory().endElement(PAGE_HEADER_TAG);
  }

  /**
   * Handles the end of a PageFooter definition.
   *
   * @see org.jfree.report.PageFooter
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
   * @see org.jfree.report.ReportHeader
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
   * @see org.jfree.report.ReportFooter
   *
   * @throws SAXException if a parser error occurs.
   */
  private void endReportFooter() throws SAXException
  {
    getParser().popFactory().endElement(REPORT_FOOTER_TAG);
  }
}
