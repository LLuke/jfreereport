/**
 * Date: Jan 22, 2003
 * Time: 5:33:53 PM
 *
 * $Id: FunctionsWriter.java,v 1.2 2003/01/23 18:07:46 taqua Exp $
 */
package com.jrefinery.report.io.ext.writer;

import com.jrefinery.report.function.Expression;
import com.jrefinery.report.function.ExpressionCollection;
import com.jrefinery.report.function.Function;
import com.jrefinery.report.io.ext.ExpressionHandler;
import com.jrefinery.report.io.ext.ExtReportHandler;
import com.jrefinery.report.io.ext.FunctionsHandler;
import com.jrefinery.report.io.ext.factory.objects.ClassFactoryCollector;
import com.jrefinery.report.io.ext.factory.objects.ObjectDescription;
import com.jrefinery.report.util.ReportProperties;

import java.io.IOException;
import java.io.Writer;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Properties;

public class FunctionsWriter extends AbstractXMLDefinitionWriter
{
  private ClassFactoryCollector cfc;

  public FunctionsWriter(ReportWriter reportWriter)
  {
    super(reportWriter);
    cfc = getReportWriter().getClassFactoryCollector();
  }

  public void write(Writer writer)
      throws IOException, ReportWriterException
  {
    writeTag(writer, ExtReportHandler.FUNCTIONS_TAG);

    writePropertyRefs(writer);
    writeExpressions (writer, getReport().getExpressions());
    writeExpressions (writer, getReport().getFunctions());

    writeCloseTag(writer, ExtReportHandler.FUNCTIONS_TAG);
  }


  public void writeExpressions (Writer writer, ExpressionCollection exp)
      throws IOException, ReportWriterException
  {
    for (int i = 0; i < exp.size(); i++)
    {
      Expression expression = exp.getExpression(i);
      String tagName = FunctionsHandler.EXPRESSION_TAG;
      if (expression instanceof Function)
      {
        tagName = FunctionsHandler.FUNCTION_TAG;
      }

      Properties properties = new Properties();
      properties.setProperty("name", expression.getName());
      properties.setProperty("class", expression.getClass().getName());
      writeTag(writer, tagName, properties, OPEN);

      Properties expressionProperties = expression.getProperties();
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

  private void writePropertyRefs (Writer writer)
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
        ObjectDescription od = cfc.getDescriptionForClass(value.getClass());
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

  private void writeObjectDescription (Writer writer, ObjectDescription od, Object o)
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
      ObjectWriter objectWriter = new ObjectWriter(getReportWriter(), o, od);
      objectWriter.write(writer);
    }

  }

  private boolean isBasicObject(ObjectDescription od)
  {
    Iterator odNames = od.getParameterNames();
    if (odNames.hasNext() == false)
      return false;

    String param = (String) odNames.next();
    if (odNames.hasNext() == true)
      return false;

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
