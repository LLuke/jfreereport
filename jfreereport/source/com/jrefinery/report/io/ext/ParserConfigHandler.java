/**
 * Date: Jan 9, 2003
 * Time: 9:08:15 PM
 *
 * $Id: ParserConfigHandler.java,v 1.4 2003/02/04 17:56:11 taqua Exp $
 */
package com.jrefinery.report.io.ext;

import com.jrefinery.report.io.Parser;
import com.jrefinery.report.io.ReportDefinitionHandler;
import com.jrefinery.report.io.ext.factory.datasource.DataSourceCollector;
import com.jrefinery.report.io.ext.factory.datasource.DataSourceFactory;
import com.jrefinery.report.io.ext.factory.elements.ElementFactory;
import com.jrefinery.report.io.ext.factory.elements.ElementFactoryCollector;
import com.jrefinery.report.io.ext.factory.objects.ClassFactory;
import com.jrefinery.report.io.ext.factory.objects.ClassFactoryCollector;
import com.jrefinery.report.io.ext.factory.stylekey.StyleKeyFactory;
import com.jrefinery.report.io.ext.factory.stylekey.StyleKeyFactoryCollector;
import com.jrefinery.report.io.ext.factory.templates.TemplateCollection;
import com.jrefinery.report.io.ext.factory.templates.TemplateCollector;
import com.jrefinery.report.util.Log;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class ParserConfigHandler implements ReportDefinitionHandler
{
  // for style-key creation ...
  public static final String STYLEKEY_FACTORY_TAG = "stylekey-factory";
  public static final String TEMPLATE_FACTORY_TAG = "template-factory";
  public static final String OBJECT_FACTORY_TAG = "object-factory";
  public static final String DATADEFINITION_FACTORY_TAG = "datadefinition-factory";
  public static final String DATASOURCE_FACTORY_TAG = "datasource-factory";
  public static final String ELEMENT_FACTORY_TAG = "element-factory";

  public static final String CLASS_ATTRIBUTE = "class";

  private Parser parser;
  private String finishTag;

  public ParserConfigHandler(Parser parser, String finishTag)
  {
    this.parser = parser;
    this.finishTag = finishTag;
  }

  public void startElement(String tagName, Attributes attrs)
    throws SAXException
  {
    if (tagName.equals(STYLEKEY_FACTORY_TAG))
    {
      String className = attrs.getValue(CLASS_ATTRIBUTE);
      if (className == null) throw new SAXException("Attribute 'class' is missing.");
      StyleKeyFactoryCollector fc =
          (StyleKeyFactoryCollector) getParser().getConfigurationValue(STYLEKEY_FACTORY_TAG);

      StyleKeyFactory factory = (StyleKeyFactory) createFactory(className);
      if (factory == null) throw new SAXException("Unable to create Factory");
      factory.init(getParser());
      fc.addFactory(factory);
    }
    else if (tagName.equals(OBJECT_FACTORY_TAG))
    {
      String className = attrs.getValue(CLASS_ATTRIBUTE);
      if (className == null) throw new SAXException("Attribute 'class' is missing.");
      ClassFactoryCollector fc =
          (ClassFactoryCollector) getParser().getConfigurationValue(OBJECT_FACTORY_TAG);
      fc.addFactory((ClassFactory) createFactory(className));
    }
    else if (tagName.equals(TEMPLATE_FACTORY_TAG))
    {
      String className = attrs.getValue(CLASS_ATTRIBUTE);
      if (className == null) throw new SAXException("Attribute 'class' is missing.");
      TemplateCollector fc =
          (TemplateCollector) getParser().getConfigurationValue(TEMPLATE_FACTORY_TAG);
      fc.addTemplateCollection((TemplateCollection) createFactory(className));
    }
    else if (tagName.equals(DATASOURCE_FACTORY_TAG))
    {
      String className = attrs.getValue(CLASS_ATTRIBUTE);
      if (className == null) throw new SAXException("Attribute 'class' is missing.");

      DataSourceFactory factory = (DataSourceFactory) createFactory(className);
      DataSourceCollector fc =
          (DataSourceCollector) getParser().getConfigurationValue(DATASOURCE_FACTORY_TAG);
      fc.addFactory(factory);
    }
    else if (tagName.equals(ELEMENT_FACTORY_TAG))
    {
      String className = attrs.getValue(CLASS_ATTRIBUTE);
      if (className == null) throw new SAXException("Attribute 'class' is missing.");
      ElementFactoryCollector fc =
          (ElementFactoryCollector) getParser().getConfigurationValue(ELEMENT_FACTORY_TAG);
      fc.addFactory((ElementFactory) createFactory(className));
    }
    else if (tagName.equals(DATADEFINITION_FACTORY_TAG))
    {

    }
    else
    {
      throw new SAXException ("Invalid TagName: " + tagName + ", expected one of: " +
                              STYLEKEY_FACTORY_TAG + ", " +
                              DATASOURCE_FACTORY_TAG + ", " +
                              DATADEFINITION_FACTORY_TAG + ", " +
                              TEMPLATE_FACTORY_TAG + ", " +
                              OBJECT_FACTORY_TAG
                             );
    }
  }

  private Object createFactory (String classname) throws SAXException
  {
    try
    {
      Class f = getClass().getClassLoader().loadClass(classname);
      return f.newInstance();
    }
    catch (Exception e)
    {
      Log.error ("Failed to parse Factory: ", e);
      throw new SAXException("Invalid Factory class specified");
    }
  }

  public void characters(char ch[], int start, int length)
  {
    // is not used ...
  }

  public void endElement(String tagName) throws SAXException
  {
    if (tagName.equals(STYLEKEY_FACTORY_TAG) ||
        tagName.equals(DATADEFINITION_FACTORY_TAG) ||
        tagName.equals(TEMPLATE_FACTORY_TAG) ||
        tagName.equals(DATASOURCE_FACTORY_TAG) ||
        tagName.equals(ELEMENT_FACTORY_TAG) ||
        tagName.equals(OBJECT_FACTORY_TAG))
    {
    }
    else if (tagName.equals(finishTag))
    {
      getParser().popFactory().endElement(tagName);
    }
    else
    {
      throw new SAXException ("Invalid TagName: " + tagName + ", expected one of: " +
                              STYLEKEY_FACTORY_TAG + ", " +
                              DATADEFINITION_FACTORY_TAG + ", " +
                              TEMPLATE_FACTORY_TAG + ", " +
                              DATASOURCE_FACTORY_TAG + ", " +
                              ELEMENT_FACTORY_TAG + ", " +
                              OBJECT_FACTORY_TAG);
    }
  }

  public Parser getParser()
  {
    return parser;
  }
}
