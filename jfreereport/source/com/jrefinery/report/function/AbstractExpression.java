/*
 * Created by IntelliJ IDEA.
 * User: user
 * Date: 12.08.2002
 * Time: 20:54:09
 * To change template for new class use 
 * Code Style | Class Templates options (Tools | IDE Options).
 */
package com.jrefinery.report.function;

import com.jrefinery.report.DataRow;
import com.jrefinery.report.util.Log;

import java.util.Properties;
import java.util.Enumeration;

public abstract class AbstractExpression implements Expression
{
  private Properties properties;
  private String name;
  private DataRow dataRow;

  public AbstractExpression ()
  {
    this.properties = new Properties();
    setName("");
  }

  public String getName()
  {
    return name;
  }

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
  public void setProperties (Properties p)
  {
    if (p != null)
    {
      Log.debug (p + "");
      Enumeration names = p.keys ();
      while (names.hasMoreElements ())
      {
        String name = (String) names.nextElement ();
        String prop = (String) p.get (name);
        setProperty (name, prop);
        Log.debug ("Name: " + name + " Prop: " + prop);
      }
      Log.debug (getProperties() + "");
    }
  }

  public Properties getProperties ()
  {
    Properties retval = new Properties ();
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
  public String getProperty (String name)
  {
    return getProperty (name, null);
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
  public String getProperty (String name, String defaultVal)
  {
    return properties.getProperty (name, defaultVal);
  }

  /**
   * Sets a property for the function.
   *
   * @param name The property name.
   * @param value The property value.
   */
  public final void setProperty (String name, String value)
  {
    if (value == null)
    {
      System.out.println ("Remove is null " + name);
      properties.remove (name);
    }
    else
    {
      System.out.println ("Add is valid " + name + " - " + value);
      properties.setProperty (name, value);
    }
  }

  public DataRow getDataRow()
  {
    return dataRow;
  }

  public void setDataRow(DataRow dataRow)
  {
    this.dataRow = dataRow;
  }

  public Object clone () throws CloneNotSupportedException
  {
    AbstractExpression function = (AbstractExpression) super.clone ();
    function.properties = (Properties) properties.clone();
    return function;
  }

  public void initialize() throws FunctionInitializeException
  {
    if (getName() == null) throw new FunctionInitializeException("Name must not be null");
  }


}
