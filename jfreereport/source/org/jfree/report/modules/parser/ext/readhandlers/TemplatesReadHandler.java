package org.jfree.report.modules.parser.ext.readhandlers;

import java.util.ArrayList;

import org.jfree.xml.parser.AbstractXmlReadHandler;
import org.jfree.xml.parser.XmlReadHandler;
import org.jfree.xml.parser.XmlReaderException;
import org.jfree.report.modules.parser.ext.factory.templates.TemplateCollection;
import org.jfree.report.modules.parser.ext.factory.templates.TemplateDescription;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class TemplatesReadHandler extends AbstractXmlReadHandler
{
  private ArrayList templateList;

  public TemplatesReadHandler ()
  {
    templateList = new ArrayList();
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
    if (tagName.equals("template"))
    {
      final TemplateReadHandler readHandler = new TemplateReadHandler(true);
      templateList.add (readHandler);
      return readHandler;
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
    final TemplateCollection templateCollection = (TemplateCollection)
            getRootHandler().getHelperObject
            (ReportDefinitionReadHandler.TEMPLATE_FACTORY_KEY);
    for (int i = 0; i < templateList.size(); i++)
    {
      final TemplateReadHandler readHandler =
              (TemplateReadHandler) templateList.get(i);
      templateCollection.addTemplate
              ((TemplateDescription) readHandler.getObjectDescription());
    }
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
