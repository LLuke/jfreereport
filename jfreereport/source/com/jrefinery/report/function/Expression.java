/**
 *
 *  Date: 26.07.2002
 *  Expression.java
 *  ------------------------------
 *  26.07.2002 : ...
 */
package com.jrefinery.report.function;

import com.jrefinery.report.DataRow;

import java.util.Properties;

public interface Expression extends Cloneable
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
   *
   * @throws FunctionInitializeException in case the function/expression is not initialized properly.
   */
  public void initialize () throws FunctionInitializeException;

  public DataRow getDataRow ();

  public void setDataRow (DataRow theDataRow);

  /**
   * Clones the function in its current state.
   * <P>
   * This is used for recording the report state at page boundaries.
   *
   * @return A clone of this function.
   */
  public Object clone () throws CloneNotSupportedException;


}