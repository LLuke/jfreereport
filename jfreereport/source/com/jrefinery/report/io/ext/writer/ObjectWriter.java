/**
 * Date: Jan 13, 2003
 * Time: 6:32:21 PM
 *
 * $Id: ObjectWriter.java,v 1.3 2003/01/23 18:07:46 taqua Exp $
 */
package com.jrefinery.report.io.ext.writer;

import com.jrefinery.report.io.ext.CompoundObjectHandler;
import com.jrefinery.report.io.ext.factory.objects.ClassFactoryCollector;
import com.jrefinery.report.io.ext.factory.objects.ObjectDescription;
import com.jrefinery.report.io.ext.factory.objects.ObjectFactoryException;
import com.jrefinery.report.util.Log;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

public class ObjectWriter extends AbstractXMLDefinitionWriter
{
  private Object baseObject;
  private ObjectDescription objectDescription;
  private ClassFactoryCollector cc;

  public ObjectWriter(ReportWriter reportWriter, Object baseObject, ObjectDescription objectDescription)
  {
    super(reportWriter);
    if (baseObject == null)
      throw new NullPointerException("BaseObject is null");
    if (objectDescription == null)
      throw new NullPointerException("ObjectDescription is null");

    this.baseObject = baseObject;
    this.objectDescription = objectDescription;
    cc = getReportWriter().getClassFactoryCollector();
  }

  public ObjectDescription getObjectDescription()
  {
    return objectDescription;
  }

  public Object getBaseObject()
  {
    return baseObject;
  }

  public ClassFactoryCollector getClassFactoryCollector ()
  {
    return cc;
  }

  public void write(Writer writer) throws IOException, ReportWriterException
  {
    writer.flush();

    try
    {
      objectDescription.setParameterFromObject(baseObject);
    }
    catch (Exception e)
    {
      throw new ReportWriterException("Unable to save object", e);
    }

    Iterator names = objectDescription.getParameterNames();
    while (names.hasNext())
    {
      String name = (String) names.next();
      writeParameter(writer, name);
    }
  }

  protected ObjectDescription getParameterDescription (String name)
  {
    Class parameterClass = objectDescription.getParameterDefinition(name);
    ObjectDescription parameterDescription =
        cc.getDescriptionForClass(parameterClass);

    if (parameterDescription == null)
    {
      Object o = objectDescription.getParameter(name);
      if (o == null)
        return null;

      parameterDescription = cc.getDescriptionForClass(o.getClass());
      if (parameterDescription == null)
      {
        Log.debug ("Unable to get parameter description for class: " + o.getClass());
      }
    }
    return parameterDescription;
  }


  protected void writeParameter (Writer writer, String parameterName)
    throws IOException, ReportWriterException
  {
    Object parameterValue = getObjectDescription().getParameter(parameterName);
    if (parameterValue == null)
    {
      Log.debug ("Parameter " + parameterName + " is null. The Parameter will not be defined.");
      return;
    }

    Class parameterDefinition = getObjectDescription().getParameterDefinition(parameterName);
    ObjectDescription parameterDescription = getParameterDescription(parameterName);
    if (parameterDescription == null)
    {
      Log.debug("Parameter:" + parameterName + " Value: " + objectDescription.getParameter(parameterName));
      throw new ReportWriterException("Unable to get Parameter description for " +
                                      getBaseObject() + " Parameter: " + parameterName);
    }

    try
    {
      parameterDescription.setParameterFromObject(parameterValue);
    }
    catch (ObjectFactoryException ofe)
    {
      throw new ReportWriterException("Unable to fill parameter object:" + parameterName);
    }

    Properties p = new Properties();
    p.setProperty("name", parameterName);
    if ((parameterDefinition.equals(parameterValue.getClass())) == false)
    {
      p.setProperty("class", parameterValue.getClass().getName());
    }

    List parameterNames = getParameterNames(parameterDescription);
    if (isBasicObject(parameterNames, parameterDescription))
    {
      writeTag(writer, CompoundObjectHandler.BASIC_OBJECT_TAG, p, OPEN);
      Log.debug ("Write BasicObject: " + parameterName + " -> " + parameterDescription.getParameter("value"));
      Log.debug ("Write BasicObject: " + parameterName + " -> " + parameterDescription.getObjectClass());
      writer.write(normalize((String) parameterDescription.getParameter("value")));
      writeCloseTag(writer, CompoundObjectHandler.BASIC_OBJECT_TAG);
    }
    else
    {
      writeTag(writer, CompoundObjectHandler.COMPOUND_OBJECT_TAG, p, OPEN);

      ObjectWriter objWriter = new ObjectWriter(getReportWriter(), parameterValue, parameterDescription);
      objWriter.write(writer);

      writeCloseTag(writer, CompoundObjectHandler.COMPOUND_OBJECT_TAG);
    }

  }

  protected static boolean isBasicObject(List parameters, ObjectDescription od)
  {
    if (od == null)
      throw new NullPointerException();

    if (parameters.size() == 1)
    {
      String param = (String) parameters.get(0);
      if (param.equals("value"))
      {
        if (od.getParameterDefinition("value").equals(String.class))
        {
          return true;
        }
      }
    }
    return false;
  }

  protected static ArrayList getParameterNames (ObjectDescription d)
  {
    ArrayList list = new ArrayList();

    Iterator it = d.getParameterNames();
    while (it.hasNext())
    {
      String name = (String) it.next();
      list.add(name);
    }
    return list;
  }


}
