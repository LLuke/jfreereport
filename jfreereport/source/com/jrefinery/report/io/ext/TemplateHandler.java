/**
 * Date: Jan 11, 2003
 * Time: 4:14:03 PM
 *
 * $Id: TemplateHandler.java,v 1.2 2003/01/14 21:07:47 taqua Exp $
 */
package com.jrefinery.report.io.ext;

import com.jrefinery.report.io.Parser;
import com.jrefinery.report.io.ReportDefinitionHandler;
import com.jrefinery.report.io.ext.factory.objects.ObjectDescription;
import com.jrefinery.report.io.ext.factory.templates.TemplateDescription;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class TemplateHandler implements ReportDefinitionHandler
{
  public static final String COMPOUND_OBJECT_TAG = "compound-object";
  public static final String BASIC_OBJECT_TAG = "basic-object";

  private BasicObjectHandler basicFactory;
  private String parameterName;

  private Parser parser;
  private String finishTag;
  private TemplateDescription template;

  public TemplateHandler(Parser parser, String finishTag, TemplateDescription template)
  {
    this.parser = parser;
    this.finishTag = finishTag;
    this.template = template;
    if (template == null) throw new NullPointerException();
  }

  public void startElement(String tagName, Attributes attrs) throws SAXException
  {
    if (tagName.equals(BASIC_OBJECT_TAG))
    {
      parameterName = attrs.getValue("name");
      if (parameterName == null)
        throw new SAXException ("Attribute 'name' is missing.");

      ObjectDescription od = getTemplate();
      Class parameter = od.getParameterDefinition(parameterName);
      if (parameter == null)
        throw new SAXException("No such parameter '" + parameterName + "' in template. ");

      String overrideClassName = attrs.getValue("class");
      if (overrideClassName != null)
      {
        try
        {
          parameter = Class.forName(overrideClassName);
        }
        catch (Exception e)
        {
          throw new SAXException("Attribute 'class' is invalid.", e);
        }
      }

      basicFactory = new BasicObjectHandler(getParser(), tagName, parameter);
      getParser().pushFactory(basicFactory);
    }
    else if (tagName.equals(COMPOUND_OBJECT_TAG))
    {
      parameterName = attrs.getValue("name");
      if (parameterName == null)
        throw new SAXException ("Attribute 'name' is missing.");

      ObjectDescription od = getTemplate();
      Class parameter = od.getParameterDefinition(parameterName);
      if (parameter == null)
        throw new SAXException("No such parameter");

      String overrideClassName = attrs.getValue("class");
      if (overrideClassName != null)
      {
        try
        {
          parameter = Class.forName(overrideClassName);
        }
        catch (Exception e)
        {
          throw new SAXException("Attribute 'class' is invalid.", e);
        }
      }

      basicFactory = new CompoundObjectHandler(getParser(), tagName, parameter);
      getParser().pushFactory(basicFactory);
    }
    else
    {
      throw new SAXException ("Invalid TagName: " + tagName + ", expected one of: " +
                              COMPOUND_OBJECT_TAG + ", " +
                              BASIC_OBJECT_TAG + ". ");
    }
  }

  public void characters(char ch[], int start, int length) throws SAXException
  {
    // ignore ...
  }

  public void endElement(String tagName) throws SAXException
  {
    if ((tagName.equals(BASIC_OBJECT_TAG)) ||
        (tagName.equals(COMPOUND_OBJECT_TAG)))
    {
      Object o = basicFactory.getValue();
      if (o == null)
        throw new SAXException("Parameter value is null");

      getTemplate().setParameter(parameterName, o);
      basicFactory = null;
    }
    else if (tagName.equals(finishTag))
    {
      getParser().popFactory().endElement(tagName);
    }
    else
    {
      throw new SAXException ("Invalid TagName: " + tagName + ", expected one of: " +
                              finishTag + ", " +
                              COMPOUND_OBJECT_TAG + ", " +
                              BASIC_OBJECT_TAG + ". ");
    }
  }

  public Parser getParser()
  {
    return parser;
  }

  public TemplateDescription getTemplate()
  {
    return template;
  }
}
