/**
 * Date: Jan 9, 2003
 * Time: 9:08:15 PM
 *
 * $Id: TemplatesHandler.java,v 1.3 2003/01/14 21:07:49 taqua Exp $
 */
package com.jrefinery.report.io.ext;

import com.jrefinery.report.io.Parser;
import com.jrefinery.report.io.ReportDefinitionHandler;
import com.jrefinery.report.io.ext.factory.templates.TemplateCollector;
import com.jrefinery.report.io.ext.factory.templates.TemplateDescription;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class TemplatesHandler implements ReportDefinitionHandler
{
  public static final String TEMPLATE_TAG = "template";

  private Parser parser;
  private String finishTag;
  private TemplateCollector templateCollector;
  private TemplateHandler templateFactory;

  public TemplatesHandler(Parser parser, String finishTag)
  {
    this.parser = parser;
    this.finishTag = finishTag;
    templateCollector = (TemplateCollector)
        getParser().getConfigurationValue(ParserConfigHandler.TEMPLATE_FACTORY_TAG);
    if (templateCollector == null)
    {
      throw new IllegalStateException("No template collector defined for this parser?");
    }

  }

  public void startElement(String tagName, Attributes attrs)
    throws SAXException
  {
    if (tagName.equals(TEMPLATE_TAG) == false)
    {
      throw new SAXException("Expected tag '" + TEMPLATE_TAG + "'");
    }

    String templateName = attrs.getValue("name");
    if (templateName == null)
      throw new SAXException("The 'name' attribute is required for template definitions");
    String references = attrs.getValue("references");
    TemplateDescription template = templateCollector.getTemplate(references);
    if (template == null) throw new SAXException("The template '" + references + "' is not defined");

    // Clone the defined template ... we don't change the original ..
    template = (TemplateDescription) template.getInstance();
    template.setName(templateName);
    templateFactory = new TemplateHandler(getParser(),TEMPLATE_TAG, template);
    getParser().pushFactory(templateFactory);
  }

  public void characters(char ch[], int start, int length)
  {
    // ignore characters ...
  }

  public void endElement(String tagName)
    throws SAXException
  {
    if (tagName.equals(TEMPLATE_TAG))
    {
      templateCollector.addTemplate(templateFactory.getTemplate());
    }
    else if (tagName.equals(finishTag))
    {
      getParser().popFactory().endElement(tagName);
    }
    else throw new SAXException("Wrong tag, expected one of " +
                                finishTag + "," +
                                TEMPLATE_TAG);
  }

  public Parser getParser()
  {
    return parser;
  }

  public void setParser(Parser parser)
  {
    this.parser = parser;
  }
}
