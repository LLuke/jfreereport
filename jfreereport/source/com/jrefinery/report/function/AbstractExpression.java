/**
 * =============================================================
 * JFreeReport : an open source reporting class library for Java
 * =============================================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport;
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
 *
 * (C) Copyright 2000-2002, by Simba Management Limited and Contributors.
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
 * ---------------------
 * AbstractExpression.java
 * ---------------------
 * (C)opyright 2000-2002, by Thomas Morgner and Contributors.
 *
 * changes
 * -------
 * 12-Aug-2002 : Initial version
 * 27-Aug-2002 : Documentation
 */
package com.jrefinery.report.function;

import com.jrefinery.report.DataRow;
import com.jrefinery.report.util.Log;

import java.util.Enumeration;
import java.util.Properties;

/**
 * The abstract expression is a base class for expressions used in JFreeReport. This class provides
 * a default implementation to make own expressions easier to implement.
 * <p>
 * Expressions are stateless functions which have access to the datarow of the report. All expressions
 * are named and the defined names have to be unique within the reports expressions, functions and fields of
 * the datasource. Expressions are configured using properties.
 * <p>
 * @todo define a property query interface similiar to the JDBC-Property interface
 */
public abstract class AbstractExpression implements Expression
{
  private Properties properties;
  private String name;
  private DataRow dataRow;

  /**
   * create an unnamed expression. Make sure the name of the expression is set using setName() before
   * the expression is added to an expressioncollection.
   */
  public AbstractExpression()
  {
    this.properties = new Properties();
    setName("");
  }

  /**
   * returns the name of the expression. Do not change the name of the expression after this expression
   * was added to the expression collection. The name of the expression has to be unique with the scope
   * of the reports functions, expressions and datasource fields.
   *
   * @returns the name of the expression.
   */
  public String getName()
  {
    return name;
  }

  /**
   * Defines the name of the expression. Do not change the name of the expression after this expression
   * was added to the expression collection. The name of the expression has to be unique with the scope
   * of the reports functions, expressions and datasource fields.
   *
   * @returns the name of the expression.
   * @throws NullPointerException if the name is null
   */
  public void setName(String name)
  {
    if (name == null) throw new NullPointerException("Name must not be null");
    this.name = name;
  }

  /**
   * Sets the properties for this expression. All parameters are defined
   * by properties. Common parameters are "field" and "group" to define
   * the targets of the expression.
   *
   * Every expression defines its own set of properties and it is up to
   * the report generator to fill the properties.
   *
   * The properties in <code>p</code> are added to the expressions properties,
   * eventually overwriting existing properties with the same name.
   *
   * @param p The properties.
   */
  public void setProperties(Properties p)
  {
    if (p != null)
    {
      Enumeration names = p.keys();
      while (names.hasMoreElements())
      {
        String name = (String) names.nextElement();
        String prop = (String) p.get(name);
        setProperty(name, prop);
      }
    }
  }

  /**
   * @returns a copy of the properties defined for this expression.
   */
  public Properties getProperties()
  {
    Properties retval = new Properties();
    retval.putAll(properties);
    return retval;
  }

  /**
   * Returns the value of a property.
   * <P>
   * Returns null if no such property was found.
   *
   * @param name The property name.
   *
   * @return The property value.
   */
  public String getProperty(String name)
  {
    return getProperty(name, null);
  }

  /**
   * Returns the value of a property.
   * <P>
   * If there is no property with the specified name, then the defaultVal is returned.
   *
   * @param name The property name.
   * @param defaultVal The default property value.
   *
   * @return The property value.
   */
  public String getProperty(String name, String defaultVal)
  {
    return properties.getProperty(name, defaultVal);
  }

  /**
   * Sets a property for the function. The the property value is null, the property will be removed
   * from this expressions set of properties.
   *
   * @param name The property name.
   * @param value The property value.
   * @throws NullPointerException if the name is null
   */
  public final void setProperty(String name, String value)
  {
    if (name == null) throw new NullPointerException();
    if (value == null)
    {
      properties.remove(name);
    }
    else
    {
      properties.setProperty(name, value);
    }
  }

  /**
   * returns the datarow for this expression. A datarow is used to query the values of functions,
   * expressions and datasource fields in an uniform way.
   *
   * @returns the assigned datarow for this expression.
   */
  public DataRow getDataRow()
  {
    return dataRow;
  }

  /**
   * Defines the datarow for this expression. A datarow is used to query the values of functions,
   * expressions and datasource fields in an uniform way.
   *
   * @param dataRow assignes the datarow for this expression.
   */
  public void setDataRow(DataRow dataRow)
  {
    this.dataRow = dataRow;
  }

  /**
   * Clones this expression. Expressions are cloned when the report processing startes, to remove any
   * connection to outside objects which could influence the reporting process.
   */
  public Object clone() throws CloneNotSupportedException
  {
    AbstractExpression function = (AbstractExpression) super.clone();
    function.properties = (Properties) properties.clone();
    return function;
  }

  /**
   * Initializes this Expression, and throws a FunctionInitializeException if the expression has no name
   * defined.
   *
   * @throws FunctionInitializeException if no name was defined for this function.
   */
  public void initialize() throws FunctionInitializeException
  {
    if (getName() == null) throw new FunctionInitializeException("Name must not be null");
  }


}
