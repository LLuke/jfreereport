/**
 * Date: Jan 9, 2003
 * Time: 9:08:15 PM
 *
 * $Id: ReportDescriptionHandler.java,v 1.1 2003/01/12 21:33:53 taqua Exp $
 */
package com.jrefinery.report.io.ext;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import com.jrefinery.report.ReportHeader;
import com.jrefinery.report.ReportFooter;
import com.jrefinery.report.Band;
import com.jrefinery.report.PageHeader;
import com.jrefinery.report.PageFooter;
import com.jrefinery.report.ItemBand;
import com.jrefinery.report.JFreeReport;
import com.jrefinery.report.io.ext.BandHandler;
import com.jrefinery.report.io.InitialReportHandler;
import com.jrefinery.report.io.Parser;
import com.jrefinery.report.io.ReportDefinitionHandler;

public class ReportDescriptionHandler implements ReportDefinitionHandler
{
  public static final String REPORT_HEADER_TAG = "report-header";
  public static final String REPORT_FOOTER_TAG = "report-footer";
  public static final String PAGE_HEADER_TAG = "page-header";
  public static final String PAGE_FOOTER_TAG = "page-footer";
  public static final String ITEMBAND_TAG = "itemband";
  public static final String GROUPS_TAG = "groups";

  private Parser parser;
  private String finishTag;
  private BandHandler bandFactory;

  public ReportDescriptionHandler(Parser parser, String finishTag)
  {
    this.parser = parser;
    this.finishTag = finishTag;
  }

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
      throw new SAXException ("Invalid TagName: " + tagName + ", expected one of: " +
                              REPORT_HEADER_TAG + ", " +
                              REPORT_FOOTER_TAG + ", " +
                              PAGE_HEADER_TAG + ", " +
                              PAGE_FOOTER_TAG + ", " +
                              ITEMBAND_TAG + ", " +
                              GROUPS_TAG);
    }

  }

  public void characters(char ch[], int start, int length)
  {
    // ignore the characters ...
  }

  private JFreeReport getReport ()
  {
    return (JFreeReport) getParser().getConfigurationValue(InitialReportHandler.REPORT_DEFINITION_TAG);
  }

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
      throw new SAXException ("Invalid TagName: " + tagName + ", expected one of: " +
                              REPORT_HEADER_TAG + ", " +
                              REPORT_FOOTER_TAG + ", " +
                              PAGE_HEADER_TAG + ", " +
                              PAGE_FOOTER_TAG + ", " +
                              ITEMBAND_TAG + ", " +
                              GROUPS_TAG+ ", " +
                              finishTag);
    }
  }

  public Parser getParser()
  {
    return parser;
  }

}
