/**
 * =============================================================
 * JFreeReport : an open source reporting class library for Java
 * =============================================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport;
 * Project Lead:  David Gilbert (david.gilbert@jrefinery.com);
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
 * ---------------------------
 * ReportPropertyFunction.java
 * ---------------------------
 * (C)opyright 2002, by Simba Management Limited.
 *
 * Original Author:  David Gilbert (for Simba Management Limited);
 * Contributor(s):   -;
 *
 * $Id: ReportPropertyFunction.java,v 1.1.1.1 2002/04/25 17:02:33 taqua Exp $
 *
 * Changes
 * -------
 * 01-Mar-2002 : Version 1 (DG);
 * 18-Apr-2002 : Using the generator to create a function will create
 *               the function using the default constructor. In this
 *               case, field was null and raised a nullpointerexception.
 * 24-Apr-2002 : Changed the implementation to reflect the changes in Function and
 *               AbstractFunction
 * 10-May-2002 : Applied the ReportEvent interface
 */

package com.jrefinery.report.function;

import com.jrefinery.report.JFreeReport;
import com.jrefinery.report.event.ReportEvent;

/**
 * A report function that returns a report property.
 */
public class ReportPropertyFunction extends AbstractFunction
{

  /** The function value. */
  private Object value;
  private String field;

  public ReportPropertyFunction ()
  {
  }

  /**
   * Constructs a new function.
   *
   * @param name The function name.
   * @param propertyNam The property name.
   */
  public ReportPropertyFunction (String name, String propertyName)
  {
    setName (name);
    setField (propertyName);
  }

  /**
   * Receives notification that a new report is about to start.
   */
  public void reportStarted (ReportEvent event)
  {
    JFreeReport report = event.getReport ();
    Object value = report.getProperty (field);
    if (value == null)
    {
      this.value = "-";
    }
    else
    {
      this.value = value;
    }
  }

  /**
   * Returns the function's value.
   */
  public Object getValue ()
  {
    return this.value;
  }

  /**
   * Returns a copy of this function.
   */
  public Object clone ()
  {

    Object result = null;

    try
    {
      result = super.clone ();
    }
    catch (CloneNotSupportedException e)
    {
      // this should never happen...
      System.err.println ("ReportProertyFunction: clone not supported");
    }

    return result;

  }

  public String getField ()
  {
    return field;
  }

  public void setField (String field)
  {
    if (field == null)
      throw new NullPointerException ();
    this.field = field;
    setProperty("field", field);
  }


  public void initialize ()
    throws FunctionInitializeException
  {
    super.initialize();
    String fieldProp = getProperty ("field");
    if (fieldProp == null)
    {
      throw new FunctionInitializeException("No Such Property : field");
    }
    setField (fieldProp);
  }

}