/**
 * Date: Jan 10, 2003
 * Time: 10:29:08 PM
 *
 * $Id: CompoundObjectHandler.java,v 1.1 2003/01/12 21:33:53 taqua Exp $
 */
package com.jrefinery.report.io.ext;

import org.xml.sax.SAXException;
import org.xml.sax.Attributes;
import com.jrefinery.report.io.ext.factory.objects.ObjectDescription;
import com.jrefinery.report.io.ext.BasicObjectHandler;
import com.jrefinery.report.io.Parser;
import com.jrefinery.report.util.Log;

public class CompoundObjectHandler extends BasicObjectHandler
{
  public static final String COMPOUND_OBJECT_TAG = "compound-object";
  public static final String BASIC_OBJECT_TAG = "basic-object";

  private BasicObjectHandler basicFactory;
  private String parameterName;

  public CompoundObjectHandler(Parser parser, String finishTag, ObjectDescription od)
  {
    super(parser, finishTag, od);
  }

  public CompoundObjectHandler(Parser parser, String finishTag, Class targetObject)
    throws SAXException
  {
    super(parser, finishTag, targetObject);
  }

  private ObjectDescription getKeyObjectDescription ()
  {
    return getTargetObjectDescription();
  }

  public void endElement(String tagName) throws SAXException
  {
    if (tagName.equals(COMPOUND_OBJECT_TAG))
    {
      if (basicFactory == null && tagName.equals(getFinishTag()))
      {
        getParser().popFactory().endElement(tagName);
        return;
      }
      else
      {
        Object o = basicFactory.getValue();
        if (o == null)
          throw new SAXException("Parameter value is null");

        getKeyObjectDescription().setParameter(parameterName, o);
        basicFactory = null;
      }
    }
    else if (tagName.equals(BASIC_OBJECT_TAG))
    {
      Object o = basicFactory.getValue();
      if (o == null)
        throw new SAXException("Parameter value is null");

      getKeyObjectDescription().setParameter(parameterName, o);
      basicFactory = null;
    }
    else if (tagName.equals(getFinishTag()))
    {
      getParser().popFactory().endElement(tagName);
    }
    else
    {
      throw new SAXException ("Invalid TagName: " + tagName + ", expected one of: " +
                              getFinishTag() + ", " +
                              COMPOUND_OBJECT_TAG + ", " +
                              BASIC_OBJECT_TAG + ". ");
    }
  }

  public void characters(char ch[], int start, int length) throws SAXException
  {
    // ignore the event...
  }

  public void startElement(String tagName, Attributes attrs) throws SAXException
  {
    if (tagName.equals(BASIC_OBJECT_TAG))
    {
      parameterName = attrs.getValue("name");
      if (parameterName == null)
        throw new SAXException ("Attribute 'name' is missing.");

      ObjectDescription od = getKeyObjectDescription();
      if (od == null)
        throw new SAXException("No ObjectFactory for the key");

      Class parameter = od.getParameterDefinition(parameterName);
      if (parameter == null)
        throw new SAXException("No such parameter: " + parameterName);

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

      ObjectDescription od = getKeyObjectDescription();
      if (od == null)
        throw new SAXException("No ObjectFactory for the key");

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
}
