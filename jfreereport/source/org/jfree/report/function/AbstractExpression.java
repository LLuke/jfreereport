/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
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
 * As a special exception, the copyright holders of JFreeReport give you
 * permission to extend JFreeReport with independent modules that communicate with
 * JFreeReport solely through the "Expression" or the "Function" interface, regardless
 * of the license terms of these independent modules, and to copy and distribute the
 * resulting combined work under terms of your choice, provided that
 * every copy of the combined work is accompanied by a complete copy of
 * the source code of JFreeReport (the version of JFreeReport used to produce the
 * combined work), being distributed under the terms of the GNU Lesser General
 * Public License plus this exception.  An independent module is a module
 * which is not derived from or based on JFreeReport.
 *
 * This exception applies to the Java interfaces "Expression" and "Function"
 * and the classes "AbstractExpression" and "AbstractFunction".
 *
 * Note that people who make modified versions of JFreeReport are not obligated
 * to grant this special exception for their modified versions; it is
 * their choice whether to do so.  The GNU Lesser General Public License gives
 * permission to release a modified version without this exception; this
 * exception also makes it possible to release a modified version which
 * carries forward this exception.
 *
 * -----------------------
 * AbstractExpression.java
 * -----------------------
 * (C)opyright 2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: AbstractExpression.java,v 1.3 2003/10/18 19:32:12 taqua Exp $
 *
 * Changes
 * -------
 * 12-Aug-2002 : Initial version
 * 27-Aug-2002 : Documentation
 * 10-Dec-2002 : Fixed issues reported by Checkstyle (DG);
 * 18-Dec-2002 : More Javadoc updates (DG);
 *
 */

package org.jfree.report.function;

import java.util.Enumeration;
import java.util.Properties;
import java.io.Serializable;

import org.jfree.report.DataRow;

/**
 * An abstract base class for implementing new report expressions.
 * <p>
 * Expressions are stateless functions which have access to the report's {@link DataRow}. All
 * expressions are named and the defined names have to be unique within the report's expressions,
 * functions and fields of the datasource. Expressions are configured using properties.
 * <p>
 * todo: define a property query interface similar to the JDBC-Property interface
 *
 * @author Thomas Morgner
 */
public abstract class AbstractExpression implements Expression, Serializable
{
  /** The expression name. */
  private String name;

  /** The dependency level. */
  private int dependency;

  /** Storage for the expression properties. */
  private Properties properties;

  /** The data row. */
  private transient DataRow dataRow;

  /**
   * Creates an unnamed expression. Make sure the name of the expression is set using
   * {@link #setName} before the expression is added to the report's expression collection.
   */
  protected AbstractExpression()
  {
    setName("");
    this.properties = new Properties();
  }

  /**
   * Returns the name of the expression.
   *
   * @return the name.
   */
  public String getName()
  {
    return name;
  }

  /**
   * Sets the name of the expression.
   * <P>
   * The name should be unique among:
   * <ul>
   *   <li>the functions and expressions for the report;
   *   <li>the columns in the report's <code>TableModel</code>;
   * </ul>
   * This allows the expression to be referenced by name from any report element.
   * <p>
   * You should not change the name of an expression once it has been added to the report's
   * expression collection.
   *
   * @param name  the name (<code>null</code> not permitted).
   */
  public void setName(final String name)
  {
    if (name == null)
    {
      throw new NullPointerException("AbstractExpression.setName(...) : name is null.");
    }
    this.name = name;
  }

  /**
   * Returns the value of a property, or <code>null</code> if no such property is defined.
   *
   * @param name  the property name.
   *
   * @return the property value.
   */
  public String getProperty(final String name)
  {
    return getProperty(name, null);
  }

  /**
   * Returns the value of a property, or <code>defaultVal</code> if no such property is defined.
   *
   * @param name  the property name.
   * @param defaultVal  the default value.
   *
   * @return the property value.
   */
  public String getProperty(final String name, final String defaultVal)
  {
    return properties.getProperty(name, defaultVal);
  }

  /**
   * Returns <code>true</code> if this expression contains "auto-active" content and should be
   * called by the system regardless of whether this expression is referenced in the
   * {@link DataRow}.
   *
   * @return true, if the expression is activated automaticly, false otherwise.
   */
  public boolean isActive()
  {
    return getProperty(AUTOACTIVATE_PROPERTY, "false").equals("true");
  }

  /**
   * Sets a property for the expression.  If the property value is <code>null</code>, the property
   * will be removed from the property collection.
   *
   * @param name  the property name (<code>null</code> not permitted).
   * @param value  the property value.
   */
  public final void setProperty(final String name, final String value)
  {
    if (name == null)
    {
      throw new NullPointerException();
    }
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
   * Returns a copy of the properties for this expression.
   *
   * @return the properties.
   */
  public Properties getProperties()
  {
    final Properties retval = new Properties();
    retval.putAll(properties);
    return retval;
  }

  /**
   * Adds a property collection to the properties for this expression (overwriting existing
   * properties with the same name).
   * <P>
   * Expression parameters are recorded as properties.  The required parameters (if any) will be
   * specified in the documentation for the class that implements the expression.
   *
   * @param p  the properties.
   */
  public void setProperties(final Properties p)
  {
    if (p != null)
    {
      final Enumeration names = p.keys();
      while (names.hasMoreElements())
      {
        final String name = (String) names.nextElement();
        final String prop = (String) p.get(name);
        setProperty(name, prop);
      }
    }
  }

  /**
   * Returns the dependency level for the expression (controls evaluation order for expressions
   * and functions).
   *
   * @return the level.
   */
  public int getDependencyLevel()
  {
    return dependency;
  }

  /**
   * Sets the dependency level for the expression.
   * <p>
   * The dependency level controls the order of evaluation for expressions and functions.  Higher
   * level expressions are evaluated before lower level expressions.  Any level in the range
   * 0 to Integer.MAX_VALUE is allowed.  Negative values are reserved for system functions
   * (printing and layouting).
   *
   * @param level  the level (must be greater than or equal to 0).
   */
  public void setDependencyLevel(final int level)
  {
    if (level < 0)
    {
      throw new IllegalArgumentException("AbstractExpression.setDependencyLevel(...) : negative "
          + "dependency not allowed for user-defined expressions.");
    }
    this.dependency = level;
  }

  /**
   * Returns the current {@link DataRow}.
   *
   * @return the data row.
   */
  public DataRow getDataRow()
  {
    return dataRow;
  }

  /**
   * Sets the current {@link DataRow} for the expression.  The data row is set when the report
   * processing starts and can be used to access the values of other expressions, functions, and
   * the report's <code>TableModel</code>.
   * <p>
   * This method is used by the report processing engine, you shouldn't need to call it yourself.
   *
   * @param row the data row.
   */
  public void setDataRow(final DataRow row)
  {
    this.dataRow = row;
  }

  /**
   * Checks that the expression has been correctly initialized.
   * <p>
   * The only check performed at present is to make sure the name is not <code>null</code>.
   *
   * @throws FunctionInitializeException in case the expression is not initialized properly.
   */
  public void initialize() throws FunctionInitializeException
  {
    if (getName() == null)
    {
      throw new FunctionInitializeException("Name must not be null");
    }
  }

  /**
   * Clones the expression.  The expression should be reinitialized after the cloning.
   * <P>
   * Expressions maintain no state, cloning is done at the beginning of the report processing to
   * disconnect the expression from any other object space.
   *
   * @return a clone of this expression.
   *
   * @throws CloneNotSupportedException this should never happen.
   */
  public Object clone() throws CloneNotSupportedException
  {
    final AbstractExpression function = (AbstractExpression) super.clone();
    function.properties = (Properties) properties.clone();
    return function;
  }

  /**
   * Return a completly separated copy of this function. The copy does no
   * longer share any changeable objects with the original function. Only
   * the datarow may be shared.
   *
   * @return a copy of this function.
   */
  public Expression getInstance()
  {
    try
    {
      return (Expression) clone();
    }
    catch (CloneNotSupportedException cne)
    {
      return null;
    }
  }


}
