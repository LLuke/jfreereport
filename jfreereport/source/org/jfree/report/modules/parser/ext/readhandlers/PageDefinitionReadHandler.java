package org.jfree.report.modules.parser.ext.readhandlers;

import java.util.ArrayList;

import org.jfree.xml.parser.AbstractXmlReadHandler;
import org.jfree.xml.parser.XmlReaderException;
import org.jfree.xml.parser.XmlReadHandler;
import org.jfree.report.CustomPageDefinition;
import org.jfree.report.JFreeReport;
import org.xml.sax.SAXException;
import org.xml.sax.Attributes;

public class PageDefinitionReadHandler extends AbstractXmlReadHandler
{
  private ArrayList pageDefList;

  public PageDefinitionReadHandler ()
  {
    pageDefList = new ArrayList();
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
    if (tagName.equals("page"))
    {
    }
    return null;
  }

  /**
   * Done parsing.
   *
   * @throws org.xml.sax.SAXException if there is a parsing error.
   * @throws org.jfree.xml.parser.XmlReaderException
   *                                  if there is a reader error.
   */
  protected void doneParsing ()
          throws SAXException, XmlReaderException
  {
    if (pageDefList.size() == 0)
    {
      throw new SAXException
              ("page-definition element needs at least one page definition.");
    }

    final CustomPageDefinition pageDefinition = new CustomPageDefinition();

    for (int i = 0; i < pageDefList.size(); i++)
    {
      final PageReadHandler readHandler = (PageReadHandler) pageDefList.get(i);
      pageDefinition.addPageFormat(readHandler.getPageFormat(),
              readHandler.getX(), readHandler.getY());
    }

    final JFreeReport report = (JFreeReport)
            getRootHandler().getHelperObject(ReportDefinitionReadHandler.REPORT_KEY);
    report.setPageDefinition(pageDefinition);
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
