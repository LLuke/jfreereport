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
 * --------------------
 * ItemSumFunction.java
 * --------------------
 * (C)opyright 2000-2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: ItemSumFunction.java,v 1.1.1.1 2002/04/25 17:02:33 taqua Exp $
 *
 * Changes
 * -------
 * 18-Feb-2002 : Version 1, contributed by Thomas Morgner (DG);
 * 24-Apr-2002 : Changed the implementation to reflect the changes in Function and
 *               AbstractFunction
 * 10-May-2002 : Applied the ReportEvent interface
 *
 */

package com.jrefinery.report.function;

import com.jrefinery.report.Group;
import com.jrefinery.report.JFreeReport;
import com.jrefinery.report.event.ReportEvent;

import javax.swing.table.TableModel;
import java.math.BigDecimal;

/**
 * A report function that calculates the sum of a field within a group.
 */
public class ItemSumFunction extends AbstractFunction
{

  private String group;
  private String field;

  /** Initial value. */
  private static final BigDecimal ZERO = new BigDecimal (0.0);

  /** The sum. */
  private BigDecimal sum;

  /**
   * Constructs an unnamed function.
   * <P>
   * This constructor is intended for use by the SAX handler class only.
   */
  public ItemSumFunction ()
  {
  }

  /**
   * Constructs a named function that sums the values of a field.  The sum resets to zero at
   * the beginning of each instance of a specific group.
   *
   * @param name The function name.
   * @param group The report group.
   * @param field The field.
   */
  public ItemSumFunction (String name)
  {
    setName (name);
    this.sum = ZERO;
  }

  /**
   * Initialises the function when it is first created.  The function is also given a chance
   * to perform initialisation every time a report starts (see the startReport() method).
   */
  public void initialise ()
  {
    this.sum = ZERO;
  }

  /**
   * Receives notification that a new report is about to start.
   */
  public void startReport (JFreeReport report)
  {
    this.sum = ZERO;
  }

  /**
   * Receives notification that a new group is about to start.
   */
  public void startGroup (Group group)
  {
    if (this.group != null)
    {
      if (this.group.equals (group.getName ()))
      {
        this.sum = ZERO;
      }
    }
  }

  public String getGroup ()
  {
    return group;
  }

  public void setGroup (String group)
  {
    this.group = group;
    setProperty ("group", group);
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
    setProperty ("field", field);
  }

  public void itemsAdvanced (ReportEvent event)
  {
    TableModel data = event.getReport().getData ();
    int row = event.getState().getCurrentDataItem();

    Object fieldValue = null;
    for (int c = 0; c < data.getColumnCount (); c++)
    {
      if (this.field.equals (data.getColumnName (c)))
      {
        fieldValue = data.getValueAt (row, c);
      }
    }

    if (fieldValue != null)
    {
      if (fieldValue instanceof Number)
      {
        Number n = (Number) fieldValue;
        try
        {
          sum = sum.add (new BigDecimal (n.toString ()));
        }
        catch (Exception e)
        {
          System.err.println ("ItemSumFunction.advanceItems(): problem adding number.");
        }
      }

    }

  }

  /**
   * Returns the function value.
   */
  public Object getValue ()
  {
    return sum;
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
      System.err.println ("ItemSumFunction: clone not supported");
    }

    return result;

  }

  public void initialize ()
    throws FunctionInitializeException
  {
    String fieldProp = getProperty ("field");
    if (fieldProp == null)
    {
      throw new FunctionInitializeException("No Such Property : field");
    }
    setField (fieldProp);
    setGroup (getProperty ("group"));
  }

}
