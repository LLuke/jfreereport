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
 * -----------------------------
 * ReportDescriptionHandler.java
 * -----------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: ReportDescriptionHandler.java,v 1.1 2003/07/07 22:44:08 taqua Exp $
 *
 * Changes
 * -------
 * 24-Feb-2003 : Added standard header and Javadocs (DG);
 *
 */

package org.jfree.report.modules.parser.ext;

import org.jfree.report.Band;
import org.jfree.report.JFreeReport;
import org.jfree.report.modules.parser.base.InitialReportHandler;
import org.jfree.xml.ElementDefinitionHandler;
import org.jfree.xml.Parser;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * A report description handler. The report description defines the visual
 * appearance of an report and defines the various bands and elements.
 *
 * @author Thomas Morgner
 */
public class ReportDescriptionHandler implements ElementDefinitionHandler
{
  /** The 'report-header' tag name. */
  public static final String REPORT_HEADER_TAG = "report-header";

  /** The 'report-footer' tag name. */
  public static final String REPORT_FOOTER_TAG = "report-footer";

  /** The 'page-header' tag name. */
  public static final String PAGE_HEADER_TAG = "page-header";

  /** The 'page-footer' tag name. */
  public static final String PAGE_FOOTER_TAG = "page-footer";

  /** The 'itemband' tag name. */
  public static final String ITEMBAND_TAG = "itemband";

  /** The 'groups' tag name. */
  public static final String GROUPS_TAG = "groups";

  /** The parser. */
  private Parser parser;

  /** The finish tag. */
  private String finishTag;

  /** The band handler. */
  private BandHandler bandFactory;

  /**
   * Creates a new handler.
   *
   * @param parser  the parser.
   * @param finishTag  the finish tag.
   */
  public ReportDescriptionHandler(final Parser parser, final String finishTag)
  {
    if (parser == null)
    {
      throw new NullPointerException("Parser is null");
    }
    if (finishTag == null)
    {
      throw new NullPointerException("FinishTag is null");
    }
    this.parser = parser;
    this.finishTag = finishTag;
  }

  /**
   * Callback to indicate that an XML element start tag has been read by the parser.
   *
   * @param tagName  the tag name.
   * @param attrs  the attributes.
   *
   * @throws SAXException if a parser error occurs or the validation failed.
   */
  public void startElement(final String tagName, final Attributes attrs)
      throws SAXException
  {
    if (tagName.equals(REPORT_HEADER_TAG))
    {
      final Band band = getReport().getReportHeader();
      final String name = attrs.getValue("name");
      if (name != null)
      {
        band.setName(name);
      }
      bandFactory = new BandHandler(getParser(), tagName, band);
      getParser().pushFactory(bandFactory);
    }
    else if (tagName.equals(REPORT_FOOTER_TAG))
    {
      final Band band = getReport().getReportFooter();
      final String name = attrs.getValue("name");
      if (name != null)
      {
        band.setName(name);
      }
      bandFactory = new BandHandler(getParser(), tagName, band);
      getParser().pushFactory(bandFactory);
    }
    else if (tagName.equals(PAGE_HEADER_TAG))
    {
      final Band band = getReport().getPageHeader();
      final String name = attrs.getValue("name");
      if (name != null)
      {
        band.setName(name);
      }
      bandFactory = new BandHandler(getParser(), tagName, band);
      getParser().pushFactory(bandFactory);
    }
    else if (tagName.equals(PAGE_FOOTER_TAG))
    {
      final Band band = getReport().getPageFooter();
      final String name = attrs.getValue("name");
      if (name != null)
      {
        band.setName(name);
      }
      bandFactory = new BandHandler(getParser(), tagName, band);
      getParser().pushFactory(bandFactory);
    }
    else if (tagName.equals(ITEMBAND_TAG))
    {
      final Band band = getReport().getItemBand();
      final String name = attrs.getValue("name");
      if (name != null)
      {
        band.setName(name);
      }
      bandFactory = new BandHandler(getParser(), tagName, band);
      getParser().pushFactory(bandFactory);
    }
    else if (tagName.equals(GROUPS_TAG))
    {
      final GroupsHandler groupFactory = new GroupsHandler(getParser(), tagName);
      getParser().pushFactory(groupFactory);
    }
    else
    {
      throw new SAXException("Invalid TagName: " + tagName + ", expected one of: "
          + REPORT_HEADER_TAG + ", "
          + REPORT_FOOTER_TAG + ", "
          + PAGE_HEADER_TAG + ", "
          + PAGE_FOOTER_TAG + ", "
          + ITEMBAND_TAG + ", "
          + GROUPS_TAG);
    }

  }

  /**
   * Callback to indicate that some character data has been read.
   *
   * @param ch  the character array.
   * @param start  the start index for the characters.
   * @param length  the length of the character sequence.
   */
  public void characters(final char[] ch, final int start, final int length)
  {
    // ignore the characters ...
  }

  /**
   * Returns the report.
   *
   * @return The report.
   */
  private JFreeReport getReport()
  {
    return (JFreeReport) getParser().getHelperObject(
        InitialReportHandler.REPORT_DEFINITION_TAG);
  }

  /**
   * Callback to indicate that an XML element end tag has been read by the parser.
   *
   * @param tagName  the tag name.
   *
   * @throws SAXException if a parser error occurs or the validation failed.
   */
  public void endElement(final String tagName)
      throws SAXException
  {
    if (tagName.equals(REPORT_HEADER_TAG))
    {
    }
    else if (tagName.equals(REPORT_FOOTER_TAG))
    {
    }
    else if (tagName.equals(PAGE_HEADER_TAG))
    {
    }
    else if (tagName.equals(PAGE_FOOTER_TAG))
    {
    }
    else if (tagName.equals(ITEMBAND_TAG))
    {
    }
    else if (tagName.equals(GROUPS_TAG))
    {
      // groups finished, nothing to do here
    }
    else if (tagName.equals(finishTag))
    {
      getParser().popFactory().endElement(tagName);
    }
    else
    {
      throw new SAXException("Invalid TagName: " + tagName + ", expected one of: "
          + REPORT_HEADER_TAG + ", "
          + REPORT_FOOTER_TAG + ", "
          + PAGE_HEADER_TAG + ", "
          + PAGE_FOOTER_TAG + ", "
          + ITEMBAND_TAG + ", "
          + GROUPS_TAG + ", "
          + finishTag);
    }
  }

  /**
   * Returns the parser.
   *
   * @return The parser.
   */
  public Parser getParser()
  {
    return parser;
  }

}
