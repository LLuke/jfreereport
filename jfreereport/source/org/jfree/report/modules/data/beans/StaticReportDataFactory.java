/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2006, by Object Refinery Limited, Pentaho Corporation and Contributors.
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
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * ------------
 * StaticReportDataFactory.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: StaticReportDataFactory.java,v 1.2 2006/07/30 13:09:50 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.report.modules.data.beans;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.jfree.report.util.CSVTokenizer;
import org.jfree.report.util.DataSetUtility;
import org.jfree.report.ReportDataFactory;
import org.jfree.report.ReportData;
import org.jfree.report.DataSet;
import org.jfree.report.ReportDataFactoryException;
import org.jfree.util.ObjectUtilities;

/**
 * This report data factory uses introspection to search for a report data
 * source. The query has the following format:
 *
 * &lt;full-qualified-classname&gt;#methodName(Parameters)
 *
 * @author Thomas Morgner
 */
public class StaticReportDataFactory implements ReportDataFactory
{
  public StaticReportDataFactory()
  {
  }

  /**
   * Queries a datasource. The string 'query' defines the name of the query. The
   * Parameterset given here may contain more data than actually needed.
   * <p/>
   * The dataset may change between two calls, do not assume anything!
   *
   * @param query
   * @param parameters
   * @return
   */
  public ReportData queryData(final String query, final DataSet parameters)
          throws ReportDataFactoryException
  {
    final int methodSeparatorIdx = query.indexOf('#');
    final int parameterStartIdx = query.indexOf('(');
    if (parameterStartIdx >= 0 && methodSeparatorIdx > parameterStartIdx)
    {
      throw new ReportDataFactoryException("Malformed query: " + query);
    }

    if ((methodSeparatorIdx + 1 ) >= query.length())
    {
      throw new ReportDataFactoryException("Malformed query: " + query);
    }

    final String className = query.substring(0, methodSeparatorIdx);
    final String methodName;
    final String[] parameterNames;
    if (parameterStartIdx == -1)
    {
      // no parameters. Nice.
      parameterNames = new String[0];
      methodName = query.substring(methodSeparatorIdx + 1, query.length());
    }
    else
    {
      methodName = query.substring(methodSeparatorIdx + 1, parameterStartIdx);
      final int parameterEndIdx = query.lastIndexOf(')');
      if (parameterEndIdx < parameterStartIdx)
      {
        throw new ReportDataFactoryException("Malformed query: " + query);
      }
      final String parameterText =
              query.substring(parameterStartIdx + 1, parameterEndIdx);
      final CSVTokenizer tokenizer = new CSVTokenizer(parameterText);
      final int size = tokenizer.countTokens();
      parameterNames = new String[size];
      int i = 0;
      while (tokenizer.hasMoreTokens())
      {
        parameterNames[i] = tokenizer.nextToken();
        i += 1;
      }
    }

    try
    {
      Method m = findCallableMethod(className, methodName, parameterNames.length);
      Object[] params = new Object[parameterNames.length];
      for (int i = 0; i < parameterNames.length; i++)
      {
        final String name = parameterNames[i];
        params[i] = DataSetUtility.getByName(parameters, name, null);
      }

      if (Modifier.isStatic(m.getModifiers()))
      {
        return (ReportData) m.invoke(null, params);
      }
      else
      {
        Object o = ObjectUtilities.loadAndInstantiate
                (className, StaticReportDataFactory.class);
        if (o == null)
        {
          throw new ReportDataFactoryException
                  ("Unable to instantiate class for non static call.");
        }
        return (ReportData) m.invoke(o, params);
      }
    }
    catch(ReportDataFactoryException rdfe)
    {
      throw rdfe;
    }
    catch (Exception e)
    {
      throw new ReportDataFactoryException
              ("Something went terribly wrong: ", e);
    }
  }

  private Method findCallableMethod (final String className,
                                     final String methodName,
                                     final int paramCount)
          throws ReportDataFactoryException
  {
    ClassLoader classLoader =
            ObjectUtilities.getClassLoader(StaticReportDataFactory.class);
    if (classLoader == null)
    {
      throw new ReportDataFactoryException("No classloader!");
    }
    try
    {
      Class c = classLoader.loadClass(className);

      Method[] methods = c.getMethods();
      for (int i = 0; i < methods.length; i++)
      {
        final Method method = methods[i];
        if (Modifier.isPublic(method.getModifiers())  == false)
        {
          continue;
        }
        if (Modifier.isAbstract(method.getModifiers()))
        {
          continue;
        }
        if (method.getName().equals(methodName) == false)
        {
          continue;
        }
        final Class returnType = method.getReturnType();
        if (ReportData.class.isAssignableFrom(returnType) == false)
        {
          continue;
        }
        if (method.getParameterTypes().length != paramCount)
        {
          continue;
        }
        return method;
      }
    }
    catch (ClassNotFoundException e)
    {
      throw new ReportDataFactoryException("No such Class", e);
    }
    throw new ReportDataFactoryException("No such Method");
  }

  public void open()
  {

  }

  public void close()
  {

  }
}
