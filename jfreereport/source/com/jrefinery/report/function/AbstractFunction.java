/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
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
 * AbstractFunction.java
 * ---------------------
 * (C)opyright 2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: AbstractFunction.java,v 1.22 2002/12/11 01:10:41 mungady Exp $
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

package com.jrefinery.report.function;

import com.jrefinery.report.DataRow;
import com.jrefinery.report.event.ReportEvent;

import java.util.Enumeration;
import java.util.Properties;

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
  /** The DataRow assigned within this function. */
  private DataRow dataRow;

  /** The dependency level. */
  private int depency;

  /** Storage for the function properties. */
  private Properties properties;

  /** The function name. */
  private String name;

  /**
   * Constructs a new function.
   * <P>
   * Initially the function has no name...be sure to assign one before using the function.
   */
  protected AbstractFunction()
  {
    this.properties = new Properties();
  }

  /**
   * Returns the function name.
   *
   * @return The function name.
   */
  public String getName()
  {
    return this.name;
  }

  /**
   * Sets the function name.
   *
   * @param name The function name (null not permitted).
   * @throws NullPointerException if the given name is null
   */
  public void setName(String name)
  {
    if (name == null)
    {
      throw new NullPointerException("AbstractFunction.setName():  null not permitted.");
    }
    this.name = name;
  }

  /**
   * Checks that the function has been correctly initialized.  If there is a problem, this method
   * throws a FunctionInitializeException.
   * <P>
   * The default implementation checks that the function name is not null, and calls the
   * isInitialized() method (now deprecated).
   *
   * @throws FunctionInitializeException if the function name is not set or the call to
   * isInitialized returns false.
   */
  public void initialize() throws FunctionInitializeException
  {
    if (name == null)
    {
      throw new FunctionInitializeException("FunctionName is null");
    }
  }

  /**
   * Receives notification that the report has started.
   *
   * @param event  the event.
   */
  public void reportStarted(ReportEvent event)
  {
  }

  /**
   * Receives notification that the report has finished.
   *
   * @param event  the event.
   */
  public void reportFinished(ReportEvent event)
  {
  }

  /**
   * Receives notification that a page has started.
   *
   * @param event  the event.
   */
  public void pageStarted(ReportEvent event)
  {
  }

  /**
   * Receives notification that a page has ended.
   *
   * @param event  the event.
   */
  public void pageFinished(ReportEvent event)
  {
  }

  /**
   * Receives notification that a group has started.
   *
   * @param event  the event.
   */
  public void groupStarted(ReportEvent event)
  {
  }

  /**
   * Receives notification that a group has finished.
   *
   * @param event  the event.
   */
  public void groupFinished(ReportEvent event)
  {
  }


  /**
   * Receives notification that a row of data is being processed.
   *
   * @param event  the event.
   */
  public void itemsAdvanced(ReportEvent event)
  {
  }

  /**
   * Returns a clone of the function.
   * <P>
   * Be aware, this does not create a deep copy. If you have complex
   * strucures contained in objects, you have to overwrite this function.
   *
   * @return A clone of the function.
   *
   * @throws CloneNotSupportedException this should never happen.
   */
  public Object clone() throws CloneNotSupportedException
  {
    AbstractFunction function = (AbstractFunction) super.clone();
    function.properties = (Properties) properties.clone();
    return function;
  }

  /**
   * Sets the properties for this function. All parameters are defined
   * by properties. Common parameters are "field" and "group" to define
   * the targets of the function.
   *
   * Every function defines its own set of properties and it is up to
   * the report generator to fill the properties.
   *
   * The properties in <code>p</code> are added to the functions properties,
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
   * Sets a property for the function.
   *
   * @param name The property name.
   * @param value The property value.
   */
  public void setProperty(String name, String value)
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
   * Receives notification that a group of item bands is about to be processed.
   * <P>
   * The next events will be itemsAdvanced events until the itemsFinished event is raised.
   *
   * @param event The event.
   */
  public void itemsStarted(ReportEvent event)
  {
  }

  /**
   * Receives notification that a group of item bands has been completed.
   * <P>
   * The itemBand is finished, the report starts to close open groups.
   *
   * @param event The event.
   */
  public void itemsFinished(ReportEvent event)
  {
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
  public void setDataRow(DataRow dataRow)
  {
    this.dataRow = dataRow;
  }

  /**
   * @return a copy of the properties defined for this function.
   */
  public Properties getProperties()
  {
    Properties retval = new Properties();
    retval.putAll(properties);
    return retval;
  }

  /**
   * Returns true if this expression contains autoactive content and should be called by the system,
   * regardless whether this expression is referenced in the datarow.
   *
   * @return boolean
   */
  public boolean isActive()
  {
    return getProperty(AUTOACTIVATE_PROPERTY, "false").equals("true");
  }

  /**
   * The depency level defines the level of execution for this function. Higher depency functions
   * are executed before lower depency functions. The range for depencies is defined to start
   * from 0 (lowest depency possible) to 2^31 (upper limit of int).
   *
   * @return the level.
   */
  public int getDepencyLevel()
  {
    return depency;
  }

  /**
   * Sets the dependency level.
   *
   * @param level  the level.
   */
  public void setDepencyLevel(int level)
  {
    if (level < 0)
    {
      throw new IllegalArgumentException("No negative dependency allowed for user-defined "
                                         + "expressions.");
    }
    this.depency = level;
  }
}
