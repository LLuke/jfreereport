/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
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
 * $Id:$
 *
 * Changes
 * -------
 * 24-Feb-2003 : Added standard header and Javadocs (DG);
 *
 */

package com.jrefinery.report.io.ext;

import com.jrefinery.report.Band;
import com.jrefinery.report.ItemBand;
import com.jrefinery.report.JFreeReport;
import com.jrefinery.report.PageFooter;
import com.jrefinery.report.PageHeader;
import com.jrefinery.report.ReportFooter;
import com.jrefinery.report.ReportHeader;
import com.jrefinery.report.io.InitialReportHandler;
import com.jrefinery.report.io.Parser;
import com.jrefinery.report.io.ReportDefinitionHandler;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * A report description handler.
 * 
 * @author Thomas Morgner
 */
public class ReportDescriptionHandler implements ReportDefinitionHandler
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
  public ReportDescriptionHandler(Parser parser, String finishTag)
  {
    this.parser = parser;
    this.finishTag = finishTag;
  }

  /**
   * Callback to indicate that an XML element start tag has been read by the parser. 
   * 
   * @param tagName  the tag name.
   * @param attrs  the attributes.
   * 
   * @throws SAXException ??.
   */
  public void startElement(String tagName, Attributes attrs)
    throws SAXException
  {
    if (tagName.equals(REPORT_HEADER_TAG))
    {
      Band band = new ReportHeader();
      String name = attrs.getValue("name");
      if (name != null)
      {
        band.setName(name);
      }
      bandFactory = new BandHandler(getParser(), tagName, band);
      getParser().pushFactory(bandFactory);
    }
    else if (tagName.equals(REPORT_FOOTER_TAG))
    {
      Band band = new ReportFooter();
      String name = attrs.getValue("name");
      if (name != null)
      {
        band.setName(name);
      }
      bandFactory = new BandHandler(getParser(), tagName, band);
      getParser().pushFactory(bandFactory);
    }
    else if (tagName.equals(PAGE_HEADER_TAG))
    {
      Band band = new PageHeader();
      String name = attrs.getValue("name");
      if (name != null)
      {
        band.setName(name);
      }
      bandFactory = new BandHandler(getParser(), tagName, band);
      getParser().pushFactory(bandFactory);
    }
    else if (tagName.equals(PAGE_FOOTER_TAG))
    {
      Band band = new PageFooter();
      String name = attrs.getValue("name");
      if (name != null)
      {
        band.setName(name);
      }
      bandFactory = new BandHandler(getParser(), tagName, band);
      getParser().pushFactory(bandFactory);
    }
    else if (tagName.equals(ITEMBAND_TAG))
    {
      Band band = new ItemBand();
      String name = attrs.getValue("name");
      if (name != null)
      {
        band.setName(name);
      }
      bandFactory = new BandHandler(getParser(), tagName, band);
      getParser().pushFactory(bandFactory);
    }
    else if (tagName.equals(GROUPS_TAG))
    {
      GroupsHandler groupFactory = new GroupsHandler(getParser(), tagName);
      getParser().pushFactory(groupFactory);
    }
    else
    {
      throw new SAXException ("Invalid TagName: " + tagName + ", expected one of: "
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
  public void characters(char ch[], int start, int length)
  {
    // ignore the characters ...
  }

  /**
   * Returns the report.
   * 
   * @return The report.
   */
  private JFreeReport getReport ()
  {
    return (JFreeReport) getParser().getConfigurationValue(
        InitialReportHandler.REPORT_DEFINITION_TAG);
  }

  /**
   * Callback to indicate that an XML element end tag has been read by the parser. 
   * 
   * @param tagName  the tag name.
   * 
   * @throws SAXException ??.
   */
  public void endElement(String tagName)
    throws SAXException
  {
    if (tagName.equals(REPORT_HEADER_TAG))
    {
      getReport().setReportHeader((ReportHeader) bandFactory.getElement());
    }
    else if (tagName.equals(REPORT_FOOTER_TAG))
    {
      getReport().setReportFooter((ReportFooter) bandFactory.getElement());
    }
    else if (tagName.equals(PAGE_HEADER_TAG))
    {
      getReport().setPageHeader((PageHeader) bandFactory.getElement());
    }
    else if (tagName.equals(PAGE_FOOTER_TAG))
    {
      getReport().setPageFooter((PageFooter) bandFactory.getElement());
    }
    else if (tagName.equals(ITEMBAND_TAG))
    {
      getReport().setItemBand((ItemBand) bandFactory.getElement());
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
      throw new SAXException ("Invalid TagName: " + tagName + ", expected one of: "
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
