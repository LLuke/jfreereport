/**
 * =============================================================
 * JFreeReport : an open source reporting class library for Java
 * =============================================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
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
 * ---------------------------
 * ReportPropertyFunction.java
 * ---------------------------
 * (C)opyright 2002, by Simba Management Limited and Contributors.
 *
 * Original Author:  David Gilbert (for Simba Management Limited);
 * Contributor(s):   Thomas Morgner;
 *
 * $Id: ReportPropertyFunction.java,v 1.7 2002/05/28 19:36:41 taqua Exp $
 *
 * Changes
 * -------
 * 01-Mar-2002 : Version 1 (DG);
 * 18-Apr-2002 : Using the generator to create a function will create the function using the
 *               default constructor. In this case, field was null and raised a null pointer
 *               exception (TM);
 * 24-Apr-2002 : Changed the implementation to reflect the changes in Function and
 *               AbstractFunction (TM);
 * 10-May-2002 : Applied the ReportEvent interface (TM);
 * 16-May-2002 : Changed 'field' to 'reportProperty' when looking up attributes.  Updated the
 *               Javadoc comments (DG);
 *
 */

package com.jrefinery.report.function;

import com.jrefinery.report.ReportState;
import com.jrefinery.report.event.ReportEvent;

/**
 * A report function that returns a property that has been set for a report.
 * <P>
 * There are some properties that are defined for all reports: "report.name" and "report.date".
 * You can add arbitrary properties to a report using the setProperty method.
 * ReportProperties are now retrieved from the report state.
 */
public class ReportPropertyFunction extends AbstractFunction
{

  /** The function value. */
  private Object value;

  /** The name of the report property that this function accesses. */
  private String field;

  // DEVNOTE: I'd recommend changing 'field' to 'propertyName' (DG);

  /**
   * Default constructor.
   */
  public ReportPropertyFunction ()
  {
  }

  /**
   * Constructs a new function.
   *
   * @param name The function name.
   * @param propertyName The property name.
   */
  public ReportPropertyFunction (String name, String propertyName)
  {
    setName (name);
    setField (propertyName);
  }

  /**
   * Receives notification that a new report is about to start.
   *
   * @param event The event.
   */
  public void reportStarted (ReportEvent event)
  {
    ReportState state = event.getState ();
    value = state.getProperty (field);
  }

  /**
   * Returns the function's value.
   *
   * @return The function value.
   */
  public Object getValue ()
  {
    return this.value;
  }

  /**
   * Returns the name of the report property that the function accesses.
   * <P>
   * I recommend renaming this method getReportPropertyName() (DG);
   *
   * @return The name of the report property.
   */
  public String getField ()
  {
    return field;
  }

  /**
   * Sets the name of the report property that the function accesses.
   * <P>
   * I recommend renaming this method setReportPropertyName() (DG);
   *
   * @param field The report property name.
   */
  public void setField (String field)
  {
    if (field == null)
      throw new NullPointerException ();
    this.field = field;
    setProperty ("field", field);
  }

  /**
   * Initialises the function.
   */
  public void initialize ()
          throws FunctionInitializeException
  {
    super.initialize ();
    String fieldProp = getProperty ("reportProperty");
    if (fieldProp == null)
    {
      throw new FunctionInitializeException ("No Such Property : reportProperty");
    }
    setField (fieldProp);
  }

}
