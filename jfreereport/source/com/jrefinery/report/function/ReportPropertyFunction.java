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
 * $Id: ReportPropertyFunction.java,v 1.13 2002/10/15 20:37:24 taqua Exp $
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
 * 18-Aug-2002 : Fixed a bug where a reportProperty is not read correctly.
 *
 */

package com.jrefinery.report.function;

import com.jrefinery.report.event.ReportEvent;
import com.jrefinery.report.states.ReportState;

/**
 * A report function that returns a property that has been set for a report.
 * <P>
 * There are some properties that are defined for all reports: "report.name" and "report.date".
 * <P>
 * You can add arbitrary properties to a report using the setProperty method.
 * <P>
 * ReportProperties are now retrieved from the report state.
 *
 * @author DG
 */
public class ReportPropertyFunction extends AbstractFunction
{

  /** The function value. */
  private Object value;

  /** Literal text for the 'reportProperty' property. */
  public static final String REPORTPROPERTY_PROPERTY = "reportProperty";

  /**
   * Default constructor (intended for use by the SAX handler only).
   */
  public ReportPropertyFunction()
  {
  }

  /**
   * Constructs a new function.
   *
   * @param name  the function name.
   * @param propertyName  the property name.
   */
  public ReportPropertyFunction(String name, String propertyName)
  {
    setName(name);
    setField(propertyName);
  }

  /**
   * Updates the property value of this function.
   *
   * @param event  the event.
   */
  public void reportStarted(ReportEvent event)
  {
    ReportState state = event.getState();
    value = state.getProperty(getField());
  }

  /**
   * Updates the property value of this function.
   *
   * @param event  the event.
   */
  public void reportFinished(ReportEvent event)
  {
    ReportState state = event.getState();
    value = state.getProperty(getField());
  }

  /**
   * Updates the property value of this function.
   *
   * @param event  the event.
   */
  public void pageStarted(ReportEvent event)
  {
    ReportState state = event.getState();
    value = state.getProperty(getField());
  }

  /**
   * Updates the property value of this function.
   *
   * @param event  the event.
   */
  public void pageFinished(ReportEvent event)
  {
    ReportState state = event.getState();
    value = state.getProperty(getField());
  }

  /**
   * Updates the property value of this function.
   *
   * @param event  the event.
   */
  public void groupStarted(ReportEvent event)
  {
    ReportState state = event.getState();
    value = state.getProperty(getField());
  }

  /**
   * Updates the property value of this function.
   *
   * @param event  the event.
   */
  public void groupFinished(ReportEvent event)
  {
    ReportState state = event.getState();
    value = state.getProperty(getField());
  }

  /**
   * Updates the property value of this function.
   *
   * @param event  the event.
   */
  public void itemsAdvanced(ReportEvent event)
  {
    ReportState state = event.getState();
    value = state.getProperty(getField());
  }

  /**
   * Updates the property value of this function.
   *
   * @param event  the event.
   */
  public void itemsStarted(ReportEvent event)
  {
    ReportState state = event.getState();
    value = state.getProperty(getField());
  }

  /**
   * Updates the property value of this function.
   *
   * @param event  the event.
   */
  public void itemsFinished(ReportEvent event)
  {
    ReportState state = event.getState();
    value = state.getProperty(getField());
  }

  /**
   * Returns the function's value.
   *
   * @return the function value.
   */
  public Object getValue()
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
  public String getField()
  {
    return getProperty(REPORTPROPERTY_PROPERTY);
  }

  /**
   * Sets the name of the report property that the function accesses.
   * <P>
   * I recommend renaming this method setReportPropertyName() (DG);
   *
   * @param field The report property name.
   */
  public void setField(String field)
  {
    if (field == null)
    {
      throw new NullPointerException();
    }
    setProperty(REPORTPROPERTY_PROPERTY, field);
  }

  /**
   * Initialises the function.
   *
   * @throws FunctionInitializeException if the function is not fully initialised.
   */
  public void initialize() throws FunctionInitializeException
  {
    super.initialize();
    String fieldProp = getProperty(REPORTPROPERTY_PROPERTY);
    if (fieldProp == null)
    {
      throw new FunctionInitializeException("No Such Property : reportProperty");
    }
    setField(fieldProp);
  }

}
