/**
 *
 *  Date: 26.07.2002
 *  Expression.java
 *  ------------------------------
 *  26.07.2002 : ...
 */
package com.jrefinery.report.function;

import java.util.Properties;

public interface Expression
{

  /**
   * Returns the name of the function.
   * <P>
   * Every function within a report is required to have a unique name.
   *
   * @return The function name.
   */
  public String getName ();

  /**
   * Sets the name of the function.
   * <P>
   * The name must not be null and must be unique within the function group.
   *
   * @param name The name.
   */
  public void setName (String name);

  /**
   * Return the current function value.
   * <P>
   * The value depends (obviously) on the function implementation.   For example, a page counting
   * function will return the current page number.
   *
   * @return The value of the function.
   */
  public Object getValue ();

  /**
   * Set the function properties.
   * <P>
   * Function parameters are recorded as properties.
   *
   * @param p The properties.
   */
  public void setProperties (Properties p);

  /**
   * Checks that the function has been correctly initialized.  If there is a problem, this method
   * throws a FunctionInitializeException.
   */
  public void initialize () throws FunctionInitializeException;

}