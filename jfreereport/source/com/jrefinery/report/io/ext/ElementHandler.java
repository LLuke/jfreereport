/**
 * Date: Jan 11, 2003
 * Time: 5:03:13 PM
 *
 * $Id: ElementHandler.java,v 1.2 2003/01/13 19:00:42 taqua Exp $
 */
package com.jrefinery.report.io.ext;

import com.jrefinery.report.Element;
import com.jrefinery.report.filter.DataSource;
import com.jrefinery.report.io.Parser;
import com.jrefinery.report.io.ReportDefinitionHandler;
import com.jrefinery.report.io.ext.factory.templates.TemplateCollector;
import com.jrefinery.report.io.ext.factory.templates.TemplateDescription;
import com.jrefinery.report.targets.style.ElementStyleSheet;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import java.util.Hashtable;

public class ElementHandler implements ReportDefinitionHandler
{
  public static final String STYLE_TAG = StylesHandler.STYLE_TAG;
  public static final String TEMPLATE_TAG = TemplatesHandler.TEMPLATE_TAG;
  public static final String DATASOURCE_TAG = DataSourceHandler.DATASOURCE_TAG;

  private String finishTag;
  private Parser parser;
  private Element element;
  private Hashtable styleCollection;
  private TemplateHandler templateFactory;
  private TemplateCollector templateCollector;
  private DataSourceHandler dataSourceHandler;

  public ElementHandler(Parser parser, String finishTag, Element element)
  {
    this.finishTag = finishTag;
    this.parser = parser;
    this.element = element;
    styleCollection = (Hashtable) getParser().getConfigurationValue(StylesHandler.STYLES_COLLECTION);
    if (styleCollection == null)
    {
      throw new IllegalStateException("No styles collection found in the configuration");
    }
    templateCollector = (TemplateCollector)
        getParser().getConfigurationValue(ParserConfigHandler.TEMPLATE_FACTORY_TAG);
    if (templateCollector == null)
    {
      throw new IllegalStateException("No template collector defined for this parser?");
    }
  }

  public void startElement(String tagName, Attributes attrs) throws SAXException
  {
    if (tagName.equals(TEMPLATE_TAG))
    {
      String references = attrs.getValue("references");
      if (references == null)
        throw new SAXException("A parent template must be specified");

      TemplateDescription template = templateCollector.getTemplate(references);
      if (template == null)
        throw new SAXException("The template '" + references + "' is not defined");

      // Clone the defined template ... we don't change the original ..
      template = (TemplateDescription) template.getInstance();
      templateFactory = new TemplateHandler(getParser(),TEMPLATE_TAG, template);
      getParser().pushFactory(templateFactory);
    }
    else if (tagName.equals(DATASOURCE_TAG))
    {
      String typeName = attrs.getValue("type");
      if (typeName == null)
        throw new SAXException("The datasource type must be specified");

      dataSourceHandler = new DataSourceHandler(getParser(), tagName, typeName);
      getParser().pushFactory(dataSourceHandler);
    }
    else if (tagName.equals(STYLE_TAG))
    {
      ElementStyleSheet styleSheet = element.getStyle();
      StyleSheetHandler styleSheetFactory = new StyleSheetHandler(getParser(), STYLE_TAG, styleSheet);
      getParser().pushFactory(styleSheetFactory);
    }
    else if (tagName.equals(finishTag))
    {
      getParser().popFactory().endElement(tagName);
    }
  }

  public void characters(char ch[], int start, int length) throws SAXException
  {
    // no characters allowed ...
  }

  public void endElement(String tagName) throws SAXException
  {
    if (tagName.equals(TEMPLATE_TAG))
    {
      TemplateDescription t = templateFactory.getTemplate();
      element.setDataSource(t.createTemplate());
      templateFactory = null;
    }
    else if (tagName.equals(DATASOURCE_TAG))
    {
      DataSource ds = (DataSource) dataSourceHandler.getValue();
      element.setDataSource(ds);
      dataSourceHandler = null;
    }
    else if (tagName.equals(STYLE_TAG))
    {
      // ignore event ...
    }
    else if (tagName.equals(finishTag))
    {
      getParser().popFactory().endElement(tagName);
    }
    else
    {
      throw new SAXException("Expected '" + STYLE_TAG + "' or " +
                             finishTag + "', found : " +tagName);
    }
  }

  public Parser getParser()
  {
    return parser;
  }

  public Element getElement()
  {
    return element;
  }

  public Hashtable getStyleCollection()
  {
    return styleCollection;
  }
}
