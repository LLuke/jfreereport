package org.jfree.report.modules.parser.ext.readhandlers;

import org.jfree.xml.parser.AbstractXmlReadHandler;
import org.jfree.xml.parser.XmlReadHandler;
import org.jfree.xml.parser.XmlReaderException;
import org.jfree.report.JFreeReport;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class ReportConfigReadHandler extends AbstractXmlReadHandler
{
  public ReportConfigReadHandler ()
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
    if (tagName.equals("page-definition"))
    {
      return new PageDefinitionReadHandler();
    }
    else if (tagName.equals("configuration"))
    {
      final JFreeReport report = (JFreeReport)
              getRootHandler().getHelperObject(ReportDefinitionReadHandler.REPORT_KEY);
      return new ConfigurationReadHandler(report.getReportConfiguration());
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
