/**
 * Date: Jan 13, 2003
 * Time: 6:32:21 PM
 *
 * $Id: ObjectWriter.java,v 1.1 2003/01/13 21:39:06 taqua Exp $
 */
package com.jrefinery.report.io.ext.writer;

import com.jrefinery.report.io.ext.factory.objects.ObjectDescription;
import com.jrefinery.report.io.ext.factory.objects.ClassFactoryCollector;
import com.jrefinery.report.io.ext.StyleSheetHandler;
import com.jrefinery.report.io.ext.CompoundObjectHandler;

import java.io.Writer;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

public class ObjectWriter extends AbstractXMLDefinitionWriter
{
  private Object baseObject;
  private ObjectDescription objectDescription;
  private ClassFactoryCollector cc;

  public ObjectWriter(ReportWriter reportWriter, Object baseObject, ObjectDescription objectDescription)
  {
    super(reportWriter);
    if (baseObject == null)
      throw new NullPointerException();
    if (objectDescription == null)
      throw new NullPointerException();
    
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

      return cc.getDescriptionForClass(o.getClass());
    }
    return parameterDescription;
  }


  protected void writeParameter (Writer writer, String name)
    throws IOException, ReportWriterException
  {
    ObjectDescription parameterDescription = getParameterDescription(name);

    if (parameterDescription == null)
      throw new ReportWriterException("Unable to get Parameter description for " +
                                      getBaseObject() + " Parameter: " + name);

    List parameterNames = getParameterNames(parameterDescription);
    if (isBasicObject(parameterNames, parameterDescription))
    {
      writeTag(writer, CompoundObjectHandler.BASIC_OBJECT_TAG, "name", name, OPEN);
      writer.write(normalize((String) parameterDescription.getParameter("value")));
      writeCloseTag(writer, CompoundObjectHandler.BASIC_OBJECT_TAG);
    }
    else
    {
      for (int i = 0; i < parameterNames.size(); i++)
      {
        String parameterName = (String) parameterNames.get(i);
        Object object = parameterDescription.getParameter(parameterName);
        ObjectDescription subObjectDesc =
            cc.getDescriptionForClass(parameterDescription.getParameterDefinition(parameterName));
        if (subObjectDesc == null)
          throw new ReportWriterException("No object description");

        writeTag(writer, StyleSheetHandler.COMPOUND_KEY_TAG, "name", name, OPEN);
        ObjectWriter objWriter =
            new ObjectWriter(getReportWriter(), object, subObjectDesc );

        objWriter.write(writer);

        writeCloseTag(writer, StyleSheetHandler.COMPOUND_KEY_TAG);
      }
    }

  }

  protected boolean isBasicObject(List parameters, ObjectDescription od)
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

  protected ArrayList getParameterNames (ObjectDescription d)
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
