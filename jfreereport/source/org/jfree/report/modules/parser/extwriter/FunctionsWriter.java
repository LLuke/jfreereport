/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
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
 * $Id: FunctionsWriter.java,v 1.13 2005/09/19 15:38:48 taqua Exp $
 *
 * Changes
 * -------
 * 20-Feb-2003 : Added standard header and Javadocs (DG);
 *
 */

package org.jfree.report.modules.parser.extwriter;

import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;

import org.jfree.report.function.Expression;
import org.jfree.report.function.ExpressionCollection;
import org.jfree.report.modules.parser.base.CommentHintPath;
import org.jfree.report.util.ReportProperties;
import org.jfree.report.util.beans.BeanException;
import org.jfree.report.util.beans.BeanUtility;
import org.jfree.xml.CommentHandler;
import org.jfree.xml.factory.objects.ClassFactoryCollector;
import org.jfree.xml.factory.objects.ObjectDescription;
import org.jfree.xml.writer.AttributeList;

/**
 * An XML definition writer that outputs the functions.
 *
 * @author Thomas Morgner.
 */
public class FunctionsWriter extends AbstractXMLDefinitionWriter
{
  /**
   * The comment hint path for all functions related parser hints.
   */
  private static final CommentHintPath FUNCTIONS_PATH = new CommentHintPath
          (new String[]{REPORT_DEFINITION_TAG, FUNCTIONS_TAG});


  /**
   * The object factory.
   */
  private final ClassFactoryCollector cfc;

  /**
   * Creates a new writer.
   *
   * @param reportWriter the report writer.
   * @param indentLevel  the current indention level.
   */
  public FunctionsWriter (final ReportWriter reportWriter, final int indentLevel)
  {
    super(reportWriter, indentLevel);
    cfc = getReportWriter().getClassFactoryCollector();
  }

  /**
   * Writes the functions to XML.
   *
   * @param writer the writer.
   * @throws IOException           if there is an I/O problem.
   * @throws ReportWriterException if the report function definition could not be
   *                               written.
   */
  public void write (final Writer writer)
          throws IOException, ReportWriterException
  {
    writeComment(writer, FUNCTIONS_PATH, CommentHandler.OPEN_TAG_COMMENT);
    if (shouldWriteFunctions())
    {
      writeTag(writer, FUNCTIONS_TAG);

      writePropertyRefs(writer);
      writeExpressions(writer, getReport().getExpressions());

      writeComment(writer, FUNCTIONS_PATH, CommentHandler.CLOSE_TAG_COMMENT);
      writeCloseTag(writer, FUNCTIONS_TAG);
    }
  }

  /**
   * Tests, whether to start a functions section.
   *
   * @return true, if there are functions, marked properties or expressions defined, false
   *         otherwise.
   */
  private boolean shouldWriteFunctions ()
  {
    if (getReport().getProperties().containsMarkedProperties())
    {
      return true;
    }
    return getReport().getExpressions().size() != 0;
  }

  /**
   * Writes a collection of functions/expressions to XML.
   *
   * @param writer the writer.
   * @param exp    the collection.
   * @throws java.io.IOException if there is an I/O problem.
   */
  public void writeExpressions (final Writer writer, final ExpressionCollection exp)
          throws IOException
  {
    for (int i = 0; i < exp.size(); i++)
    {
      final Expression expression = exp.getExpression(i);

      final CommentHintPath path = FUNCTIONS_PATH.getInstance();
      path.addName(expression);
      writeComment(writer, path, CommentHandler.OPEN_TAG_COMMENT);

      try
      {
        BeanUtility bu = new BeanUtility(expression);
        String[] propertyNames = bu.getProperties();
        if (propertyNames.length == 0)
        {
          final AttributeList properties = new AttributeList();
          properties.setAttribute("name", expression.getName());
          properties.setAttribute("class", expression.getClass().getName());
          writeTag(writer, EXPRESSION_TAG, properties, CLOSE);
        }
        else
        {
          final AttributeList properties = new AttributeList();
          properties.setAttribute("name", expression.getName());
          properties.setAttribute("class", expression.getClass().getName());
          writeTag(writer, EXPRESSION_TAG, properties, OPEN);

          writeExpressionParameters(writer, path, propertyNames, bu);

          writeComment(writer, path, CommentHandler.CLOSE_TAG_COMMENT);
          writeCloseTag(writer, EXPRESSION_TAG);
        }

      }
      catch (Exception e)
      {
        throw new IOException("Unable to extract or write properties.");
      }
    }
  }

  /**
   * Writes the parameters for an expression or function.
   *
   * @param writer the writer.
   * @param path the comments as read from the parser.
   * @param propertyNames the names of the properties.
   * @param beanUtility the bean utility containing the expression bean.
   * @throws IOException if an IO error occurs.
   * @throws BeanException if a bean error occured.
   */
  private void writeExpressionParameters
          (Writer writer, final CommentHintPath path,
           final String[] propertyNames, final BeanUtility beanUtility)
          throws IOException, BeanException
  {
    final CommentHintPath propertiesPath = path.getInstance();
    propertiesPath.addName(PROPERTIES_TAG);
    writeComment(writer, propertiesPath, CommentHandler.OPEN_TAG_COMMENT);
    writeTag(writer, PROPERTIES_TAG);

    for (int i = 0; i < propertyNames.length; i++)
    {
      final String key = propertyNames[i];
      final Object property = beanUtility.getProperty(key);
      final Class propertyType = beanUtility.getPropertyType(key);
      final String value = beanUtility.getPropertyAsString(key);
      if (value != null && property != null)
      {
        final CommentHintPath propertyPath = propertiesPath.getInstance();
        propertyPath.addName(key);
        writeComment(writer, propertyPath, CommentHandler.OPEN_TAG_COMMENT);
        AttributeList attList = new AttributeList();
        attList.setAttribute("name", key);
        if (propertyType.equals(property.getClass()) == false)
        {
          attList.setAttribute("class", property.getClass().getName());
        }
        writeTag(writer, "property", attList, OPEN);
        writer.write(normalize(value));
        writeCloseTag(writer, "property");
      }
    }

    writeComment(writer, propertiesPath, CommentHandler.CLOSE_TAG_COMMENT);
    writeCloseTag(writer, PROPERTIES_TAG);
  }

  /**
   * Writes the property references to XML.
   *
   * @param writer the writer.
   * @throws IOException           if there is an I/O problem.
   * @throws ReportWriterException if the report definition could not be written.
   */
  private void writePropertyRefs (final Writer writer)
          throws IOException, ReportWriterException
  {
    final ReportProperties reportProperties = getReport().getProperties();
    final Iterator keys = reportProperties.keys();
    while (keys.hasNext())
    {
      final String name = (String) keys.next();
      if (reportProperties.isMarked(name))
      {
        final Object value = reportProperties.get(name);

        final CommentHintPath path = FUNCTIONS_PATH.getInstance();
        path.addName(name);

        if (value == null)
        {
          writeComment(writer, path, CommentHandler.OPEN_TAG_COMMENT);
          writeTag(writer, PROPERTY_REF_TAG, "name", name, CLOSE);
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
            writeComment(writer, path, CommentHandler.OPEN_TAG_COMMENT);
            writeTag(writer, PROPERTY_REF_TAG, "name", name, CLOSE);
          }
          else
          {
            final AttributeList properties = new AttributeList();
            properties.setAttribute("name", name);
            properties.setAttribute("class", od.getObjectClass().getName());

            writeComment(writer, path, CommentHandler.OPEN_TAG_COMMENT);
            writeTag(writer, PROPERTY_REF_TAG, properties, OPEN);
            writeObjectDescription(writer, od.getInstance(), value, path);
            writeComment(writer, path, CommentHandler.CLOSE_TAG_COMMENT);
            writeCloseTag(writer, PROPERTY_REF_TAG);
          }
        }
      }
    }
  }

  /**
   * Writes an object description to XML.
   *
   * @param writer the writer.
   * @param od     the object description.
   * @param o      the object.
   * @param path   the path on where to search for ext-parser comments in the report
   *               builder hints.
   * @throws IOException           if there is an I/O problem.
   * @throws ReportWriterException if the report definition could not be written.
   */
  private void writeObjectDescription (final Writer writer, final ObjectDescription od,
                                       final Object o, final CommentHintPath path)
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
      final String value = (String) od.getParameter("value");
      if (value != null)
      {
        writer.write(normalize(value));
      }
    }
    else
    {
      final ObjectWriter objectWriter =
              new ObjectWriter(getReportWriter(), o, od, getIndentLevel(), path);
      objectWriter.write(writer);
    }

  }

  /**
   * Returns <code>true</code> if the object description is for a basic object, and
   * <code>false</code> otherwise.
   *
   * @param od the object description.
   * @return <code>true</code> or <code>false</code>.
   */
  private boolean isBasicObject (final ObjectDescription od)
  {
    final Iterator odNames = od.getParameterNames();
    if (odNames.hasNext() == false)
    {
      return false;
    }

    final String param = (String) odNames.next();
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
