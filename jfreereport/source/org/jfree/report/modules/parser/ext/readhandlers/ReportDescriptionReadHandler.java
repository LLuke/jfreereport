package org.jfree.report.modules.parser.ext.readhandlers;

import org.jfree.xml.parser.AbstractXmlReadHandler;
import org.jfree.xml.parser.XmlReaderException;
import org.jfree.xml.parser.XmlReadHandler;
import org.jfree.report.JFreeReport;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class ReportDescriptionReadHandler extends AbstractXmlReadHandler
{
  public ReportDescriptionReadHandler ()
  {
  }

  /**
   * Returns the handler for a child element.
   *
   * @param tagName the tag name.
   * @param atts    the attributes.
   * @return the handler or null, if the tagname is invalid.
   *
   * @throws org.xml.sax.SAXException if there is a parsing error.
   * @throws org.jfree.xml.parser.XmlReaderException
   *                                  if there is a reader error.
   */
  protected XmlReadHandler getHandlerForChild (final String tagName,
                                               final Attributes atts)
          throws XmlReaderException, SAXException
  {
    final JFreeReport report = (JFreeReport)
            getRootHandler().getHelperObject(ReportDefinitionReadHandler.REPORT_KEY);

    if (tagName.equals("report-header"))
    {
      return new BandReadHandler(report.getReportHeader());
    }
    else if (tagName.equals("report-footer"))
    {
      return new BandReadHandler(report.getReportFooter());
    }
    else if (tagName.equals("page-header"))
    {
      return new BandReadHandler(report.getPageHeader());
    }
    else if (tagName.equals("page-footer"))
    {
      return new BandReadHandler(report.getPageFooter());
    }
    else if (tagName.equals("watermark"))
    {
      return new BandReadHandler(report.getWatermark());
    }
    else if (tagName.equals("groups"))
    {
      return new GroupsReadHandler(report.getGroups());
    }
    else if (tagName.equals("itemband"))
    {
      return new BandReadHandler(report.getItemBand());
    }
    return null;
  }

  /**
   * Returns the object for this element or null, if this element does not create an
   * object.
   *
   * @return the object.
   *
   * @throws org.jfree.xml.parser.XmlReaderException
   *          if there is a parsing error.
   */
  public Object getObject ()
          throws XmlReaderException
  {
    return null;
  }
}
