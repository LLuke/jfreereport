/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
 *
 * (C) Copyright 2000-2003, by Simba Management Limited and Contributors.
 *
 * This library is free software; you can redistribute it and/or modify it under the terms
 * of the GNU Lesser General Public License as published by the Free Software Foundation;
 * either version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this
 * library; if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 * Boston, MA 02111-1307, USA.
 *
 * -----------------
 * ObjectWriter.java
 * -----------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: ObjectWriter.java,v 1.13 2003/06/01 19:11:42 taqua Exp $
 *
 * Changes
 * -------
 * 20-Feb-2003 : Added standard header and Javadocs (DG);
 *
 */

package com.jrefinery.report.io.ext.writer;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import com.jrefinery.report.io.ext.CompoundObjectHandler;
import com.jrefinery.report.util.Log;
import org.jfree.xml.factory.objects.ClassFactoryCollector;
import org.jfree.xml.factory.objects.ObjectDescription;
import org.jfree.xml.factory.objects.ObjectFactoryException;

/**
 * A writer.
 * 
 * @author Thomas Morgner.
 */
public class ObjectWriter extends AbstractXMLDefinitionWriter
{
  /** The base object. */
  private Object baseObject;
  
  /** The object description. */
  private ObjectDescription objectDescription;
  
  /** The object factory. */
  private ClassFactoryCollector cc;

  /**
   * Creates a new writer.
   * 
   * @param reportWriter  the report writer.
   * @param baseObject  the base object (<code>null</code> not permitted).
   * @param objectDescription  the object description (<code>null</code> not permitted).
   */
  public ObjectWriter(ReportWriter reportWriter, Object baseObject, 
                      ObjectDescription objectDescription, int indentLevel)
  {
    super(reportWriter, indentLevel);
    if (baseObject == null)
    {
      throw new NullPointerException("BaseObject is null");
    }
    if (objectDescription == null)
    {
      throw new NullPointerException("ObjectDescription is null");
    }
    
    this.baseObject = baseObject;
    this.objectDescription = objectDescription;
    cc = getReportWriter().getClassFactoryCollector();
  }

  /**
   * Returns the object description.
   * 
   * @return The object description.
   */
  public ObjectDescription getObjectDescription()
  {
    return objectDescription;
  }

  /**
   * Returns the base object.
   * 
   * @return The base object.
   */
  public Object getBaseObject()
  {
    return baseObject;
  }

  /**
   * Returns the object factory.
   * 
   * @return The object factory.
   */
  public ClassFactoryCollector getClassFactoryCollector ()
  {
    return cc;
  }

  /**
   * Writes the description.
   * 
   * @param writer  the writer.
   * 
   * @throws IOException if there is an I/O problem.
   * @throws ReportWriterException if the object could not be written.
   */
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

  /**
   * Returns a description of a parameter.
   * 
   * @param name  the parameter name.
   * 
   * @return The description.
   */
  protected ObjectDescription getParameterDescription (String name)
  {

    // Try to find the object description directly ...
    // by looking at the given object. This is the most accurate
    // option ...
    ObjectDescription parameterDescription = null;
    Object o = objectDescription.getParameter(name);
    if (o != null)
    {
      parameterDescription = cc.getDescriptionForClass(o.getClass());
      if (parameterDescription == null)
      {
        // try to find the super class of the parameter object.
        // maybe this can be used to save the object....
        //Log.debug ("ParameterDescription not found [OBJECT]");
        parameterDescription = cc.getSuperClassObjectDescription(o.getClass(), null);
      }
      else
      {
        //Log.debug ("ParameterDescription found [OBJECT]");
        return parameterDescription;
      }
    }
    else
    {
      Class parameterClass = objectDescription.getParameterDefinition(name);
      parameterDescription = cc.getDescriptionForClass(parameterClass);

      if (parameterDescription != null)
      {
        //Log.debug ("ParameterDescription found [PARAM]");
        return parameterDescription;
      }

      // try to find the super class of the parameter object.
      // maybe this can be used to save the object....
      //Log.debug ("ParameterDescription not found [SUPER_PARAM]");
      parameterDescription = cc.getSuperClassObjectDescription(parameterClass, null);
    }

    if (parameterDescription == null)
    {
      Log.info ("Unable to get parameter description for class: " + o.getClass());
    }
    return parameterDescription;
  }

  /**
   * Writes a parameter to XML.
   * 
   * @param writer  the writer.
   * @param parameterName  the parameter name.
   * 
   * @throws IOException if there is an I/O problem.
   * @throws ReportWriterException if transforming the report into a stream failed.
   */
  protected void writeParameter (Writer writer, String parameterName)
    throws IOException, ReportWriterException
  {
    Object parameterValue = getObjectDescription().getParameter(parameterName);
    if (parameterValue == null)
    {
      // Log.info ("Parameter '" + parameterName + "' is null. The Parameter will not be defined.");
      return;
    }

    Class parameterDefinition = getObjectDescription().getParameterDefinition(parameterName);
    ObjectDescription parameterDescription = getParameterDescription(parameterName);
    if (parameterDescription == null)
    {
      throw new ReportWriterException("Unable to get Parameter description for "
                                      + getBaseObject() + " Parameter: " + parameterName);
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
      writer.write(normalize((String) parameterDescription.getParameter("value")));
      writeCloseTag(writer, CompoundObjectHandler.BASIC_OBJECT_TAG);
    }
    else
    {
      writeTag(writer, CompoundObjectHandler.COMPOUND_OBJECT_TAG, p, OPEN);

      ObjectWriter objWriter = new ObjectWriter(getReportWriter(), parameterValue, 
                                                parameterDescription, getIndentLevel());
      objWriter.write(writer);

      writeCloseTag(writer, CompoundObjectHandler.COMPOUND_OBJECT_TAG);
    }

  }

  /**
   * Returns <code>true</code> if this is a basic object, and <code>false</code> otherwise.
   * 
   * @param parameters  the parameter.
   * @param od  the descriptions.
   *
   * @return A boolean.
   */
  protected static boolean isBasicObject(List parameters, ObjectDescription od)
  {
    if (od == null)
    {
      throw new NullPointerException();
    }
    
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

  /**
   * Returns a list of parameter names.
   * 
   * @param d  the description.
   * 
   * @return The list.
   */
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
