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
 * ---------------------
 * AbstractFunction.java
 * ---------------------
 * (C)opyright 2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: AbstractFunction.java,v 1.2 2003/08/24 15:13:22 taqua Exp $
 *
 * Changes
 * -------
 * 15-Feb-2002 : Version 1, contributed by Thomas Morgner (DG);
 * 24-Apr-2002 : Added property support and removed the get/set Field/Group
 *               functions.
 * 10-May-2002 : Support for ReportListenerInterface added. All old eventFunctions are
 *               marked deprecated. The name-attribute must not be null, or the validity check
 *               will fail.
 * 05-Jun-2002 : Updated Javadoc comments (DG);
 * 27-Aug-2002 : Documentation and removed the deprecated functions
 * 31-Aug-2002 : Documentation update and removed isInitializedFunction
 * 10-Dec-2002 : Fixed issues reported by Checkstyle (DG);
 *
 */

package org.jfree.report.function;

import java.util.Enumeration;
import java.util.Properties;

import org.jfree.report.DataRow;
import org.jfree.report.event.ReportEvent;

/**
 * Base class for implementing new report functions.  Provides empty implementations of all the
 * methods in the Function interface.
 * <p>
 * The function is initialized when it gets added to the report. The method <code>initialize</code>
 * gets called to perform the required initializations. At this point, all function properties must
 * have been set to a valid state and the function must be named. If the initialisation fails, a
 * FunctionInitializeException is thrown and the function get not added to the report.
 *
 * @author Thomas Morgner
 */
public abstract class AbstractFunction implements Function
{
  /** The function name. */
  private String name;

  /** The dependency level. */
  private int dependency;

  /** Storage for the function properties. */
  private Properties properties;

  /** The data row. */
  private transient DataRow dataRow;

  /**
   * Creates an unnamed function. Make sure the name of the function is set using
   * {@link #setName} before the function is added to the report's function collection.
   */
  protected AbstractFunction()
  {
    this.properties = new Properties();
  }

  /**
   * Creates an named function.
   *
   * @param name the name of the function.
   */
  protected AbstractFunction(final String name)
  {
    this();
    setName(name);
  }


  /**
   * Returns the function name.
   *
   * @return the function name.
   */
  public String getName()
  {
    return this.name;
  }

  /**
   * Sets the name of the function.
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
      throw new NullPointerException("AbstractFunction.setName():  null not permitted.");
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
   * Returns true if this expression contains autoactive content and should be called by the system,
   * regardless whether this expression is referenced in the datarow.
   *
   * @return true, if the expression is activated automaticly, false otherwise.
   */
  public boolean isActive()
  {
    return getProperty(AUTOACTIVATE_PROPERTY, "false").equals("true");
  }

  /**
   * Sets a property for the function.  If the property value is <code>null</code>, the property
   * will be removed from the property collection.
   *
   * @param name  the property name (<code>null</code> not permitted).
   * @param value  the property value.
   */
  public void setProperty(final String name, final String value)
  {
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
   * Gets a copy of the properties used in this function. Modifying the returned
   * properties has no effect on the function.
   *
   * @return a copy of the properties defined for this function.
   */
  public Properties getProperties()
  {
    final Properties retval = new Properties();
    retval.putAll(properties);
    return retval;
  }

  /**
   * Adds a property collection to the properties for this function (overwriting existing
   * properties with the same name).
   * <P>
   * Function parameters are recorded as properties.  The required parameters (if any) will be
   * specified in the documentation for the class that implements the function.
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
   * Returns the dependency level for the functions (controls evaluation order for expressions
   * and functions).
   *
   * @return the level.
   */
  public int getDependencyLevel()
  {
    return dependency;
  }

  /**
   * Sets the dependency level for the function.
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
      throw new IllegalArgumentException("AbstractFunction.setDependencyLevel(...) : negative "
          + "dependency not allowed for user-defined functions.");
    }
    this.dependency = level;
  }

  /**
   * returns the datarow for this function. A datarow is used to query the values of functions,
   * expressions and datasource fields in an uniform way.
   *
   * @return the assigned datarow for this expression.
   */
  public DataRow getDataRow()
  {
    return dataRow;
  }

  /**
   * Defines the datarow for this function. A datarow is used to query the values of functions,
   * expressions and datasource fields in an uniform way.
   *
   * @param dataRow assigns the datarow for this expression.
   */
  public void setDataRow(final DataRow dataRow)
  {
    this.dataRow = dataRow;
  }

  /**
   * Checks that the function has been correctly initialized.
   * <p>
   * The only check performed at present is to make sure the name is not <code>null</code>.
   *
   * @throws FunctionInitializeException in case the function is not initialized properly.
   */
  public void initialize() throws FunctionInitializeException
  {
    if (name == null)
    {
      throw new FunctionInitializeException("FunctionName is null");
    }
  }

  /**
   * Receives notification that report generation initializes the current run.
   * <P>
   * The event carries a ReportState.Started state.  Use this to initialize the report.
   *
   * @param event The event.
   */
  public void reportInitialized(final ReportEvent event)
  {
  }

  /**
   * Receives notification that the report has started.
   *
   * @param event  the event.
   */
  public void reportStarted(final ReportEvent event)
  {
  }

  /**
   * Receives notification that the report has finished.
   *
   * @param event  the event.
   */
  public void reportFinished(final ReportEvent event)
  {
  }

  /**
   * Receives notification that a group has started.
   *
   * @param event  the event.
   */
  public void groupStarted(final ReportEvent event)
  {
  }

  /**
   * Receives notification that a group has finished.
   *
   * @param event  the event.
   */
  public void groupFinished(final ReportEvent event)
  {
  }

  /**
   * Receives notification that a row of data is being processed.
   *
   * @param event  the event.
   */
  public void itemsAdvanced(final ReportEvent event)
  {
  }

  /**
   * Receives notification that a group of item bands is about to be processed.
   * <P>
   * The next events will be itemsAdvanced events until the itemsFinished event is raised.
   *
   * @param event The event.
   */
  public void itemsStarted(final ReportEvent event)
  {
  }

  /**
   * Receives notification that a group of item bands has been completed.
   * <P>
   * The itemBand is finished, the report starts to close open groups.
   *
   * @param event The event.
   */
  public void itemsFinished(final ReportEvent event)
  {
  }

  /**
   * Clones the function.
   * <P>
   * Be aware, this does not create a deep copy. If you have complex
   * strucures contained in objects, you have to override this function.
   *
   * @return a clone of this function.
   *
   * @throws CloneNotSupportedException this should never happen.
   */
  public Object clone() throws CloneNotSupportedException
  {
    final AbstractFunction function = (AbstractFunction) super.clone();
    function.properties = (Properties) properties.clone();
    return function;
  }

  /**
   * Return a completly separated copy of this function. The copy does no
   * longer share any changeable objects with the original function.
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

  /**
   * Receives notification that report generation has completed, the report footer was printed,
   * no more output is done. This is a helper event to shut down the output service.
   *
   * @param event The event.
   */
  public void reportDone(final ReportEvent event)
  {
    // does nothing...
  }
}
