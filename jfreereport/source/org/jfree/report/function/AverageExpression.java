/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2004, by Object Refinery Limited and Contributors.
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
 * AverageExpression.java
 * ----------------------
 * (C)opyright 2004, by Object Refinery Limited.
 *
 * Original Author:  David Gilbert (for Object Refinery Limited);
 * Contributor(s):   -;
 *
 * $Id: AverageExpression.java,v 1.5 2006/01/18 22:49:46 taqua Exp $
 *
 * Changes
 * -------
 * 19-Mar-2004 : Version 1 (DG);
 * 
 */

package org.jfree.report.function;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * An expression that takes values from one or more fields and returns the average of
 * them.
 *
 * @deprecated this has been replaced by the ColumnAverageExpression. 
 */
public class AverageExpression extends AbstractExpression implements Serializable
{
  /**
   * An ordered list containing the fieldnames used in the expression.
   */
  private ArrayList fieldList;

  /**
   * Creates a new expression.  The fields used by the expression are defined using
   * properties named '0', '1', ... 'N'.  These fields should contain {@link Number}
   * instances.
   */
  public AverageExpression ()
  {
    this.fieldList = new ArrayList();
  }

  /**
   * Returns the average of the values.
   *
   * @return a BigDecimal instance.
   */
  public Object getValue ()
  {
    final Number[] values = collectValues();
    BigDecimal total = new BigDecimal(0.0);
    int count = 0;
    for (int i = 0; i < values.length; i++)
    {
      final Number n = values[i];
      if (n != null)
      {
        total = total.add(new BigDecimal(n.toString()));
        count++;
      }
    }
    if (count > 0)
    {
      return total.divide(new BigDecimal(count), BigDecimal.ROUND_HALF_UP);
    }
    return new BigDecimal(0.0);
  }

  /**
   * collects the values of all fields defined in the fieldList.
   *
   * @return an Objectarray containing all defined values from the datarow
   */
  private Number[] collectValues ()
  {
    final Number[] retval = new Number[this.fieldList.size()];
    for (int i = 0; i < this.fieldList.size(); i++)
    {
      final String field = (String) this.fieldList.get(i);
      retval[i] = (Number) getDataRow().get(field);
    }
    return retval;
  }

  /**
   * Clones the expression.
   *
   * @return A copy of this expression.
   *
   * @throws CloneNotSupportedException this should never happen.
   */
  public Object clone ()
          throws CloneNotSupportedException
  {
    final AverageExpression ae = (AverageExpression) super.clone();
    ae.fieldList = (ArrayList) fieldList.clone();
    return ae;
  }

  /**
   * Returns the defined fields as array.
   *
   * @return the fields
   */
  public String[] getField ()
  {
    return (String[]) fieldList.toArray(new String[fieldList.size()]);
  }

  public void setField (final String[] fields)
  {
    this.fieldList.clear();
    this.fieldList.addAll(Arrays.asList(fields));
  }

  /**
   * @param idx
   * @return the field at the given index
   *
   * @throws IndexOutOfBoundsException if the index is invalid.
   */
  public String getField (final int idx)
  {
    return (String) this.fieldList.get(idx);
  }

  public void setField (final int idx, final String object)
  {
    if (this.fieldList.size() == idx)
    {
      this.fieldList.add(object);
    }
    else
    {
      this.fieldList.set(idx, object);
    }
  }
}
