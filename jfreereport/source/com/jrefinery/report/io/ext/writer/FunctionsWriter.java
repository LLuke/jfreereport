/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
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
 * --------------------
 * FunctionsWriter.java
 * --------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: FunctionsWriter.java,v 1.14 2003/06/10 17:14:40 taqua Exp $
 *
 * Changes
 * -------
 * 20-Feb-2003 : Added standard header and Javadocs (DG);
 *
 */

package com.jrefinery.report.io.ext.writer;

import java.io.IOException;
import java.io.Writer;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Properties;

import com.jrefinery.report.function.Expression;
import com.jrefinery.report.function.ExpressionCollection;
import com.jrefinery.report.function.Function;
import com.jrefinery.report.io.ext.ExpressionHandler;
import com.jrefinery.report.io.ext.ExtReportHandler;
import com.jrefinery.report.io.ext.FunctionsHandler;
import com.jrefinery.report.util.ReportProperties;
import org.jfree.xml.factory.objects.ClassFactoryCollector;
import org.jfree.xml.factory.objects.ObjectDescription;

/**
 * An XML definition writer that outputs the functions.
 *
 * @author Thomas Morgner.
 */
public class FunctionsWriter extends AbstractXMLDefinitionWriter
{
  /** The object factory. */
  private ClassFactoryCollector cfc;

  /**
   * Creates a new writer.
   *
   * @param reportWriter  the report writer.
   * @param indentLevel the current indention level.
   */
  public FunctionsWriter(ReportWriter reportWriter, int indentLevel)
  {
    super(reportWriter, indentLevel);
    cfc = getReportWriter().getClassFactoryCollector();
  }

  /**
   * Writes the functions to XML.
   *
   * @param writer  the writer.
   *
   * @throws IOException if there is an I/O problem.
   * @throws ReportWriterException if the report function definition
   * could not be written.
   */
  public void write(Writer writer)
      throws IOException, ReportWriterException
  {
    writeTag(writer, ExtReportHandler.FUNCTIONS_TAG);

    writePropertyRefs(writer);
    writeExpressions(writer, getReport().getExpressions());
    writeExpressions(writer, getReport().getFunctions());

    writeCloseTag(writer, ExtReportHandler.FUNCTIONS_TAG);
  }

  /**
   * Writes a collection of functions/expressions to XML.
   *
   * @param writer  the writer.
   * @param exp  the collection.
   *
   * @throws IOException if there is an I/O problem.
   */
  public void writeExpressions(Writer writer, ExpressionCollection exp)
      throws IOException
  {
    for (int i = 0; i < exp.size(); i++)
    {
      Expression expression = exp.getExpression(i);
      String tagName = FunctionsHandler.EXPRESSION_TAG;
      if (expression instanceof Function)
      {
        tagName = FunctionsHandler.FUNCTION_TAG;
      }

      Properties expressionProperties = expression.getProperties();
      if (expressionProperties.isEmpty())
      {
        Properties properties = new Properties();
        properties.setProperty("name", expression.getName());
        properties.setProperty("class", expression.getClass().getName());
        writeTag(writer, tagName, properties, CLOSE);
      }
      else
      {
        Properties properties = new Properties();
        properties.setProperty("name", expression.getName());
        properties.setProperty("class", expression.getClass().getName());
        writeTag(writer, tagName, properties, OPEN);

        Enumeration enum = expressionProperties.keys();
        if (enum.hasMoreElements())
        {
          writeTag(writer, ExpressionHandler.PROPERTIES_TAG);
          while (enum.hasMoreElements())
          {
            String key = (String) enum.nextElement();
            String value = expressionProperties.getProperty(key);
            if (value != null)
            {
              writeTag(writer, "property", "name", key, OPEN);
              writer.write(normalize(value));
              writeCloseTag(writer, "property");
            }
          }
          writeCloseTag(writer, ExpressionHandler.PROPERTIES_TAG);
        }

        writeCloseTag(writer, tagName);
      }
    }
  }

  /**
   * Writes the property references to XML.
   *
   * @param writer  the writer.
   *
   * @throws IOException if there is an I/O problem.
   * @throws ReportWriterException if the report definition could not be written.
   */
  private void writePropertyRefs(Writer writer)
      throws IOException, ReportWriterException
  {
    ReportProperties reportProperties = getReport().getProperties();
    Iterator enum = reportProperties.keys();
    while (enum.hasNext())
    {
      String name = (String) enum.next();
      if (reportProperties.isMarked(name))
      {
        Object value = reportProperties.get(name);
        if (value == null)
        {
          writeTag(writer, FunctionsHandler.PROPERTY_REF_TAG, "name", name, CLOSE);
        }
        else
        {
          ObjectDescription od = cfc.getDescriptionForClass(value.getClass());
          if (od == null)
          {
            od = cfc.getSuperClassObjectDescription(value.getClass(), null);
          }
          if (od == null)
          {
            writeTag(writer, FunctionsHandler.PROPERTY_REF_TAG, "name", name, CLOSE);
          }
          else
          {
            Properties properties = new Properties();
            properties.setProperty("name", name);
            properties.setProperty("class", od.getObjectClass().getName());
            writeTag(writer, FunctionsHandler.PROPERTY_REF_TAG, properties, OPEN);
            writeObjectDescription(writer, od.getInstance(), value);
            writeCloseTag(writer, FunctionsHandler.PROPERTY_REF_TAG);
          }
        }
      }
    }
  }

  /**
   * Writes an object description to XML.
   *
   * @param writer  the writer.
   * @param od  the object description.
   * @param o  the object.
   *
   * @throws IOException if there is an I/O problem.
   * @throws ReportWriterException if the report definition could not be written.
   */
  private void writeObjectDescription(Writer writer, ObjectDescription od, Object o)
      throws IOException, ReportWriterException
  {
    try
    {
      od.setParameterFromObject(o);
    }
    catch (Exception e)
    {
      throw new ReportWriterException("Unable to write the report property reference", e);
    }

    if (isBasicObject(od))
    {
      String value = (String) od.getParameter("value");
      if (value != null)
      {
        writer.write(normalize(value));
      }
    }
    else
    {
      ObjectWriter objectWriter = new ObjectWriter(getReportWriter(), o, od, getIndentLevel());
      objectWriter.write(writer);
    }

  }

  /**
   * Returns <code>true</code> if the object description is for a basic object, and
   * <code>false</code> otherwise.
   *
   * @param od  the object description.
   *
   * @return <code>true</code> or <code>false</code>.
   */
  private boolean isBasicObject(ObjectDescription od)
  {
    Iterator odNames = od.getParameterNames();
    if (odNames.hasNext() == false)
    {
      return false;
    }

    String param = (String) odNames.next();
    if (odNames.hasNext() == true)
    {
      return false;
    }

    if (param.equals("value"))
    {
      if (od.getParameterDefinition("value").equals(String.class))
      {
        return true;
      }
    }
    return false;
  }
}
