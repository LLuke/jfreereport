/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
 *
 * (C) Copyright 2000-2003, by Simba Management Limited and Contributors.
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
 * ------------------------------
 * CountDistinctFunction.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: CountDistinctFunction.java,v 1.1 2003/05/16 19:29:50 taqua Exp $
 *
 * Changes
 * -------------------------
 * 16-05-2003 : Initial version
 *
 */

package com.jrefinery.report.function;

import java.util.HashSet;

import com.jrefinery.report.event.ReportEvent;

/**
 * Counts the distinct occurences of an certain value of an column.
 * This functionality is similiar to the SQL distinct() function.
 *
 * @author Thomas Morgner
 */
public class CountDistinctFunction extends AbstractFunction
{
  /** Literal text for the 'group' property. */
  public static final String GROUP_PROPERTY = "group";

  /** Literal text for the 'field' property. */
  public static final String FIELD_PROPERTY = "field";

  /** The collected values for the current group. */
  private HashSet values;

  /**
   * DefaultConstructor.
   */
  public CountDistinctFunction()
  {
    values = new HashSet();
  }

  /**
   * Returns the group name.
   *
   * @return The group name.
   */
  public String getGroup()
  {
    return getProperty(GROUP_PROPERTY);
  }

  /**
   * Sets the group name.
   * <P>
   * If a group is defined, the running total is reset to zero at the start of every instance of
   * this group.
   *
   * @param name  the group name (null permitted).
   */
  public void setGroup(String name)
  {
    setProperty(GROUP_PROPERTY, name);
  }

  /**
   * Returns the field used by the function.
   * <P>
   * The field name corresponds to a column name in the report's TableModel.
   *
   * @return The field name.
   */
  public String getField()
  {
    return getProperty(FIELD_PROPERTY);
  }

  /**
   * Sets the field name for the function.
   * <P>
   * The field name corresponds to a column name in the report's TableModel.
   *
   * @param field  the field name (null not permitted).
   */
  public void setField(String field)
  {
    if (field == null)
    {
      throw new NullPointerException();
    }
    setProperty(FIELD_PROPERTY, field);
  }

  /**
   * Receives notification that report generation initializes the current run.
   * <P>
   * The event carries a ReportState.Started state.  Use this to initialize the report.
   *
   * @param event The event.
   */
  public void reportInitialized(ReportEvent event)
  {
    if (FunctionUtilities.isDefinedPrepareRunLevel(this, event) == false)
    {
      return;
    }
    values.clear();
  }

  /**
   * Receives notification that a group has started.
   *
   * @param event  the event.
   */
  public void groupStarted(ReportEvent event)
  {
    if (FunctionUtilities.isDefinedPrepareRunLevel(this, event) == false)
    {
      return;
    }
    if (FunctionUtilities.isDefinedGroup(getGroup(), event) == false)
    {
      return;
    }

    values.clear();
  }

  /**
   * Receives notification that a row of data is being processed.
   *
   * @param event  the event.
   */
  public void itemsAdvanced(ReportEvent event)
  {
    if (FunctionUtilities.isDefinedPrepareRunLevel(this, event) == false)
    {
      return;
    }

    Object o = event.getDataRow().get(getField());
    values.add(o);
  }

  /**
   * Return the number of distint values for the given column.
   *
   * @return the value of the function.
   */
  public Object getValue()
  {
    return new Integer (values.size());
  }
}
