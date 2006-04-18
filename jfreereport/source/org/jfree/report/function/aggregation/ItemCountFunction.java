/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/
 * Project Lead:  Thomas Morgner;
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
 * ----------------------
 * ItemCountFunction.java
 * ----------------------
 * (C)opyright 2000-2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: ItemCountFunction.java,v 1.8 2005/02/23 21:04:47 taqua Exp $
 *
 * Changes
 * -------
 * 18-Feb-2002 : Version 1, contributed by Thomas Morgner (DG);
 * 24-Apr-2002 : Changed the implementation to reflect the changes in Function and
 *               AbstractFunction
 * 10-May-2002 : Applied the ReportEvent interface.
 * 05-Jun-2002 : Updated Javadoc comments (DG);
 * 18-Jul-2002 : Added system-out message ...
 * 20-Jul-2002 : removed the message leftover from last commit
 * 08-Aug-2002 : Imports cleaned
 * 20-Aug-2002 : Moved function configuration into function properties, removed local field "group"
 */

package org.jfree.report.function.aggregation;

import java.io.Serializable;

import org.jfree.report.DataSourceException;
import org.jfree.report.util.IntegerCache;
import org.jfree.report.function.AbstractFunction;
import org.jfree.report.function.Function;

/**
 * A report function that counts items in a report.  If the "group" property is set, the
 * item count is reset to zero whenever the group changes.
 *
 * @author Thomas Morgner
 */
public class ItemCountFunction extends AbstractFunction implements Serializable
{
  /**
   * The item count.
   */
  private transient int count;

  /**
   * Constructs an unnamed function. <P> This constructor is intended for use by the SAX
   * handler class only.
   */
  public ItemCountFunction ()
  {
    count = 1;
  }

  /**
   * Constructs an item count report function.
   *
   * @param name The name of the function.
   * @throws NullPointerException if the name is null
   */
  public ItemCountFunction (final String name)
  {
    this();
    setName(name);
  }

  /**
   * Returns the current count value.
   *
   * @return the current count value.
   */
  protected int getCount ()
  {
    return count;
  }

  /**
   * Defines the current count value.
   *
   * @param count the current count value.
   */
  protected void setCount (final int count)
  {
    this.count = count;
  }

  /**
   * The advance method is a signal for the function to update its internal
   * state.
   */
//  public void advance() throws DataSourceException
//  {
//    setCount(getCount() + 1);
//  }


  /**
   * When the advance method is called, the function is asked to perform the
   * next step of its computation.
   * <p/>
   * The original function must not be altered during that step (or more
   * correctly, calling advance on the original expression again must not return
   * a different result).
   *
   * @return a copy of the function containing the new state.
   */
  public Function advance() throws DataSourceException
  {
    try
    {
      ItemCountFunction function = (ItemCountFunction) clone();
      function.setCount(getCount() + 1);
      return function;
    }
    catch (CloneNotSupportedException e)
    {
      throw new DataSourceException("Unable to clone the ItemCountFunction.");
    }

  }

  /**
   * Returns the number of items counted (so far) by the function.  This is either the
   * number of items in the report, or the group (if a group has been defined for the
   * function).
   *
   * @return The item count.
   */
  public Object getValue ()
  {
    return IntegerCache.getInteger(getCount());
  }

}
