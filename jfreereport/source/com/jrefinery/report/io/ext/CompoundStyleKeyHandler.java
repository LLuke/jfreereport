/**
 * Date: Jan 10, 2003
 * Time: 7:43:07 PM
 *
 * $Id$
 */
package com.jrefinery.report.io.ext;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import com.jrefinery.report.io.ext.factory.objects.ClassFactory;
import com.jrefinery.report.io.ext.factory.objects.ObjectDescription;
import com.jrefinery.report.util.Log;
import com.jrefinery.report.io.ext.BasicObjectHandler;
import com.jrefinery.report.io.ext.BasicStyleKeyHandler;
import com.jrefinery.report.io.ext.CompoundObjectHandler;
import com.jrefinery.report.io.Parser;

public class CompoundStyleKeyHandler extends BasicStyleKeyHandler
{
  public static final String COMPOUND_OBJECT_TAG = "compound-object";
  public static final String BASIC_OBJECT_TAG = "basic-object";

  private BasicObjectHandler basicFactory;
  private ObjectDescription keyObjectDescription;
  private String parameterName;

  public CompoundStyleKeyHandler(Parser parser, String finishTag, String name)
    throws SAXException
  {
    super (parser, finishTag, name);
    ClassFactory fact = (ClassFactory) getParser().getConfigurationValue(
        ParserConfigHandler.OBJECT_FACTORY_TAG);
    keyObjectDescription = fact.getDescriptionForClass(getStyleKey().getValueType());
    if (keyObjectDescription == null)
    {
      throw new SAXException("No object definition for class " + getStyleKey().getValueType());
    }
  }

  private ObjectDescription getKeyObjectDescription ()
  {
    return keyObjectDescription;
  }

  public void startElement(String tagName, Attributes attrs) throws SAXException
  {
    if (tagName.equals(BASIC_OBJECT_TAG))
    {
      parameterName = attrs.getValue("name");
      if (parameterName == null)
        throw new SAXException ("Attribute 'name' is missing.");

      ObjectDescription od = getKeyObjectDescription();
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

      basicFactory = new BasicObjectHandler(getParser(), tagName, parameter);
      getParser().pushFactory(basicFactory);
    }
    else if (tagName.equals(COMPOUND_OBJECT_TAG))
    {
      parameterName = attrs.getValue("name");
      if (parameterName == null)
        throw new SAXException ("Attribute 'name' is missing.");

      ObjectDescription od = getKeyObjectDescription();
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

      getKeyObjectDescription().setParameter(parameterName, o);
      basicFactory = null;
    }
    else if (tagName.equals(getFinishTag()))
    {
      Log.debug ("CompoundStyleKe: " + getValue());

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

  public Object getValue ()
  {
    return getKeyObjectDescription().createObject();
  }
}
