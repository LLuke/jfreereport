/**
 * Date: Jan 9, 2003
 * Time: 8:49:58 PM
 *
 * $Id: ExtReportHandler.java,v 1.2 2003/01/22 19:38:24 taqua Exp $
 */
package com.jrefinery.report.io.ext;

import com.jrefinery.report.JFreeReport;
import com.jrefinery.report.io.InitialReportHandler;
import com.jrefinery.report.io.Parser;
import com.jrefinery.report.io.ReportDefinitionHandler;
import com.jrefinery.report.io.ext.factory.datasource.DataSourceCollector;
import com.jrefinery.report.io.ext.factory.elements.ElementFactoryCollector;
import com.jrefinery.report.io.ext.factory.objects.ClassFactoryCollector;
import com.jrefinery.report.io.ext.factory.objects.URLClassFactory;
import com.jrefinery.report.io.ext.factory.stylekey.StyleKeyFactoryCollector;
import com.jrefinery.report.io.ext.factory.templates.TemplateCollector;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import java.net.URL;
import java.util.Hashtable;

public class ExtReportHandler implements ReportDefinitionHandler
{
  public static final String REPORT_DEFINITION_TAG = InitialReportHandler.REPORT_DEFINITION_TAG;
  public static final String PARSER_CONFIG_TAG = "parser-config";
  public static final String REPORT_CONFIG_TAG = "report-config";
  public static final String STYLES_TAG = "styles";
  public static final String TEMPLATES_TAG = "templates";
  public static final String REPORT_DESCRIPTION_TAG = "report-description";
  public static final String FUNCTIONS_TAG = "functions";
  public static final String DATA_DEFINITION_TAG = "data-definition";

  private Parser parser;
  private String finishTag;

  public ExtReportHandler(Parser parser, String finishTag)
  {
    this.parser = parser;
    this.finishTag = finishTag;

    // create the initial JFreeReport object.
    getParser().setConfigurationValue(REPORT_DEFINITION_TAG, new JFreeReport());
    getParser().setConfigurationValue(StylesHandler.STYLES_COLLECTION, new Hashtable());
    createClassFactoryHolder();
    createStyleKeyFactoryHolder();
    createTemplateFactoryHolder();
    createDataSourceFactoryHolder();
    createElementFactoryHolder();
  }

  public void startElement(String tagName, Attributes attrs)
    throws SAXException
  {
    if (tagName.equals(PARSER_CONFIG_TAG))
    {
      // create the various factories ... order does matter ...
      getParser().pushFactory(new ParserConfigHandler(getParser(), tagName));
    }
    else if (tagName.equals(REPORT_CONFIG_TAG))
    {
      getParser().pushFactory(new ReportConfigHandler(getParser(), tagName));
    }
    else if (tagName.equals(STYLES_TAG))
    {
      getParser().pushFactory(new StylesHandler(getParser(), tagName));
    }
    else if (tagName.equals(TEMPLATES_TAG))
    {
      getParser().pushFactory(new TemplatesHandler(getParser(), tagName));
    }
    else if (tagName.equals(FUNCTIONS_TAG))
    {
      getParser().pushFactory(new FunctionsHandler(getParser(), tagName));
    }
    else if (tagName.equals(DATA_DEFINITION_TAG))
    {
      getParser().pushFactory(new DataDefinitionHandler(getParser()));
    }
    else if (tagName.equals(REPORT_DESCRIPTION_TAG))
    {
      getParser().pushFactory(new ReportDescriptionHandler(getParser(), tagName));
    }
    else
    {
      throw new SAXException ("Invalid TagName: " + tagName + ", expected one of: " +
                              PARSER_CONFIG_TAG + ", " +
                              REPORT_DESCRIPTION_TAG + ", " +
                              REPORT_CONFIG_TAG + ", " +
                              STYLES_TAG + ", " +
                              TEMPLATES_TAG + ", " +
                              FUNCTIONS_TAG + ", " +
                              DATA_DEFINITION_TAG + ".");
    }
  }

  private void createClassFactoryHolder ()
  {
    ClassFactoryCollector fc =
        (ClassFactoryCollector) getParser().getConfigurationValue(ParserConfigHandler.OBJECT_FACTORY_TAG);
    if (fc == null)
    {
      fc = new ClassFactoryCollector();

      // special treatment for URLs, they need the content base information ...
      URL contentBase = (URL) getParser().getConfigurationValue(Parser.CONTENTBASE_KEY);
      fc.addFactory(new URLClassFactory(contentBase));

      getParser().setConfigurationValue(ParserConfigHandler.OBJECT_FACTORY_TAG, fc);
    }
  }

  private void createStyleKeyFactoryHolder ()
  {
    StyleKeyFactoryCollector fc =
        (StyleKeyFactoryCollector) getParser().getConfigurationValue(ParserConfigHandler.STYLEKEY_FACTORY_TAG);
    if (fc == null)
    {
      fc = new StyleKeyFactoryCollector();
      getParser().setConfigurationValue(ParserConfigHandler.STYLEKEY_FACTORY_TAG, fc);
      fc.init(getParser());
    }
  }

  private void createTemplateFactoryHolder ()
  {
    TemplateCollector fc =
        (TemplateCollector) getParser().getConfigurationValue(ParserConfigHandler.TEMPLATE_FACTORY_TAG);
    if (fc == null)
    {
      fc = new TemplateCollector();
      getParser().setConfigurationValue(ParserConfigHandler.TEMPLATE_FACTORY_TAG, fc);
    }
  }

  private void createDataSourceFactoryHolder ()
  {
    DataSourceCollector fc =
        (DataSourceCollector) getParser().getConfigurationValue(ParserConfigHandler.DATASOURCE_FACTORY_TAG);
    if (fc == null)
    {
      fc = new DataSourceCollector();
      getParser().setConfigurationValue(ParserConfigHandler.DATASOURCE_FACTORY_TAG, fc);

      ClassFactoryCollector cfc =
          (ClassFactoryCollector) getParser().getConfigurationValue(ParserConfigHandler.OBJECT_FACTORY_TAG);
      cfc.addFactory(fc);
    }
  }

  private void createElementFactoryHolder ()
  {
    ElementFactoryCollector fc =
        (ElementFactoryCollector) getParser().getConfigurationValue(ParserConfigHandler.ELEMENT_FACTORY_TAG);
    if (fc == null)
    {
      fc = new ElementFactoryCollector();
      getParser().setConfigurationValue(ParserConfigHandler.ELEMENT_FACTORY_TAG, fc);
    }
  }

  public void characters(char ch[], int start, int length)
  {
    // characters are ignored at this point...
  }

  public void endElement(String tagName) throws SAXException
  {
    if (tagName.equals(finishTag))
    {
      getParser().popFactory().endElement(tagName);
    }
    else if (tagName.equals(PARSER_CONFIG_TAG))
    {
      // ignore this event ... forwarded from subFactory ...
    }
    else if (tagName.equals(REPORT_CONFIG_TAG))
    {
      // ignore this event ... should not happen here ...
    }
    else if (tagName.equals(STYLES_TAG))
    {
      // ignore this event ... should not happen here ...
    }
    else if (tagName.equals(TEMPLATES_TAG))
    {
      // ignore this event ... should not happen here ...
    }
    else if (tagName.equals(FUNCTIONS_TAG))
    {
      // ignore this event ... should not happen here ...
    }
    else if (tagName.equals(DATA_DEFINITION_TAG))
    {
      // ignore this event ... should not happen here ...
    }
    else if (tagName.equals(REPORT_DESCRIPTION_TAG))
    {
      // ignore this event ... should not happen here ...
    }
    else
    {
      throw new SAXException ("Invalid TagName: " + tagName + ", expected one of: " +
                              REPORT_DESCRIPTION_TAG+ ", " +
                              PARSER_CONFIG_TAG + ", " +
                              REPORT_CONFIG_TAG + ", " +
                              STYLES_TAG + ", " +
                              TEMPLATES_TAG + ", " +
                              FUNCTIONS_TAG + ", " +
                              DATA_DEFINITION_TAG + ".");
    }
  }

  public Parser getParser()
  {
    return parser;
  }
}
