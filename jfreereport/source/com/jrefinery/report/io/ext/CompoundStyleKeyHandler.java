/**
 * Date: Jan 10, 2003
 * Time: 7:43:07 PM
 *
 * $Id: CompoundStyleKeyHandler.java,v 1.2 2003/01/22 19:38:23 taqua Exp $
 */
package com.jrefinery.report.io.ext;

import com.jrefinery.report.io.Parser;
import com.jrefinery.report.io.ext.factory.objects.ClassFactory;
import com.jrefinery.report.io.ext.factory.objects.ObjectDescription;
import com.jrefinery.report.util.Log;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class CompoundStyleKeyHandler extends BasicStyleKeyHandler
{
  public static final String COMPOUND_OBJECT_TAG = CompoundObjectHandler.COMPOUND_OBJECT_TAG;
  public static final String BASIC_OBJECT_TAG = CompoundObjectHandler.BASIC_OBJECT_TAG;

  private BasicObjectHandler basicFactory;
  private ObjectDescription keyObjectDescription;
  private String parameterName;

  public CompoundStyleKeyHandler(Parser parser, String finishTag, String name, Class c)
    throws SAXException
  {
    super (parser, finishTag, name, c);
    ClassFactory fact = (ClassFactory) getParser().getConfigurationValue(
        ParserConfigHandler.OBJECT_FACTORY_TAG);
    keyObjectDescription = fact.getDescriptionForClass(getKeyValueClass());
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
