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
 * --------------------
 * ItemMinFunction.java
 * --------------------
 * (C)opyright 2000-2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: ItemMinFunction.java,v 1.6 2005/02/04 19:22:54 taqua Exp $
 *
 * Changes
 * -------
 * 18-Feb-2002 : Version 1, contributed by Thomas Morgner (DG);
 * 24-Apr-2002 : Changed the implementation to reflect the changes in Function and
 *               AbstractFunction
 * 10-May-2002 : Applied the ReportEvent interface
 * 23-Jun-2002 : Documentation
 * 17-Jul-2002 : Handle empty data source without a crashing
 * 18-Jul-2002 : Handle out-of-bounds dataquery to the tablemodel
 * 21-Jul-2002 : Corrected the out-of-bounds constraint
 * 17-Feb-2005 : Use java.util.Comparable interface instead of number for the compared
 *               values.
 */

package org.jfree.report.function;

import java.io.Serializable;

import org.jfree.report.Group;
import org.jfree.report.event.ReportEvent;
import org.jfree.report.util.Log;

/**
 * A report function that calculates the minimum value of one field (column) from the TableModel.
 * The function can be used in two ways:
 * <ul>
 * <li>to calculate a minimum value for the entire report;</li>
 * <li>to calculate a minimum value within a particular group;</li>
 * </ul>
 * This function expects its input values to be either java.lang.Number instances or Strings
 * that can be parsed to java.lang.Number instances using a java.text.DecimalFormat.
 * <p>
 * The function undestands two parameters, the <code>field</code> parameter is required and
 * denotes the name of an ItemBand-field which gets summed up.
 * <p>
 * The parameter <code>group</code> denotes the name of a group. When this group is started,
 * the counter gets reseted to null.
 *
 * @author Thomas Morgner
 */
public class ItemMinFunction extends AbstractFunction implements Serializable
{
  /** The minimum value. */
  private transient Comparable min;
  private String group;
  private String field;

  /**
   * Constructs an unnamed function. Make sure to set a Name or function initialisation
   * will fail.
   */
  public ItemMinFunction()
  {
    min = null;
  }

  /**
   * Constructs a named function.
   * <P>
   * The field must be defined before using the function.
   *
   * @param name The function name.
   */
  public ItemMinFunction(final String name)
  {
    this();
    setName(name);
  }

  /**
   * Receives notification that a new report is about to start.
   * <P>
   * Does nothing.
   *
   * @param event Information about the event.
   *
   */
  public void reportInitialized(final ReportEvent event)
  {
    this.min = null;
  }

  /**
   * Receives notification that a new group is about to start.  If this is the group defined for
   * the function, then the minimum value is reset to zero.
   *
   * @param event Information about the event.
   */
  public void groupStarted(final ReportEvent event)
  {
    final String mygroup = getGroup();
    if (mygroup == null)
    {
      return;
    }

    final Group group = event.getReport().getGroup(event.getState().getCurrentGroupIndex());
    if (getGroup().equals(group.getName()))
    {
      this.min = null;
    }
  }

  /**
   * Returns the group name.
   *
   * @return The group name.
   */
  public String getGroup()
  {
    return group;
  }

  /**
   * Sets the group name.
   * <P>
   * If a group is defined, the minimum value is reset to zero at the start of every instance of
   * this group.
   *
   * @param name  the group name (null permitted).
   */
  public void setGroup(final String name)
  {
    this.group = name;
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
    return field;
  }

  /**
   * Sets the field name for the function.
   * <P>
   * The field name corresponds to a column name in the report's TableModel.
   *
   * @param field  the field name (null not permitted).
   */
  public void setField(final String field)
  {
    this.field = field;
  }

  /**
   * Receives notification that a row of data is being processed.  Reads the data from the field
   * defined for this function and calculates the minimum value.
   *
   * @param event Information about the event.
   */
  public void itemsAdvanced(final ReportEvent event)
  {
    final Object fieldValue = event.getDataRow().get(getField());
    if (fieldValue instanceof Comparable == false)
    {
      return;
    }
    try
    {
      final Comparable compare = (Comparable) fieldValue;
      if (min == null)
      {
        min = compare;
      }
      else if (min.compareTo(compare) > 0)
      {
        min = compare;
      }
    }
    catch (Exception e)
    {
      Log.error("ItemMinFunction.advanceItems(): problem adding number.");
    }
  }

  /**
   * Returns the function value, in this case the running total of a specific column in the
   * report's TableModel.
   *
   * @return The function value.
   */
  public Object getValue()
  {
    return min;
  }


  /**
   * Return a completly separated copy of this function. The copy does no
   * longer share any changeable objects with the original function.
   *
   * @return a copy of this function.
   */
  public Expression getInstance()
  {
    final ItemMinFunction function = (ItemMinFunction) super.getInstance();
    function.min = null;
    return function;
  }


}
