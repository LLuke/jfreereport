/**
 * =========================================
 * LibLayout : a free Java layouting library
 * =========================================
 *
 * Project Info:  http://www.jfree.org/liblayout/
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2006, by Pentaho Corperation and Contributors.
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
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * ------------
 * DefaultDataTable.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corperation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: DefaultDataTable.java,v 1.1 2006/11/05 14:29:39 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.formula.lvalues;

import org.jfree.formula.EvaluationException;
import org.jfree.formula.FormulaContext;
import org.jfree.formula.typing.Type;
import org.jfree.formula.typing.coretypes.DataTableType;
import org.jfree.util.ObjectTable;

/**
 * Creation-Date: 05.11.2006, 13:34:01
 *
 * @author Thomas Morgner
 */
public class DefaultDataTable extends ObjectTable implements DataTable
{
  private transient Boolean constant;

  /**
   * Creates a new table.
   */
  public DefaultDataTable()
  {
  }

  public String getColumnName(int column)
  {
    StringBuffer result = new StringBuffer();
    for (; column >= 0; column = column / 26 - 1)
    {
      final int colChar = (char) (column % 26) + 'A';
      result.append(colChar);
    }
    return result.toString();
  }

  /**
   * Sets the object for a cell in the table.  The table is expanded if
   * necessary.
   *
   * @param row    the row index (zero-based).
   * @param column the column index (zero-based).
   * @param object the object.
   */
  public void setObject(final int row, final int column, final LValue object)
  {
    super.setObject(row, column, object);
  }

  public LValue getValueAt(int row, int column)
  {
    return (LValue) getObject(row, column);
  }

  public void initialize(FormulaContext context) throws EvaluationException
  {
    final int rows = getRowCount();
    final int cols = getColumnCount();
    for (int row = 0; row < rows; row++)
    {
      for (int col = 0; col < cols; col++)
      {
        LValue value = getValueAt(row, col);
        value.initialize(context);
      }
    }
  }

  public TypeValuePair evaluate() throws EvaluationException
  {
    return new TypeValuePair(DataTableType.TYPE, this);
  }

  public Object clone() throws CloneNotSupportedException
  {
    final DefaultDataTable table = (DefaultDataTable) super.clone();
    final Object[][] data = getData();
    final Object[][] targetData = (Object[][]) data.clone();
    for (int i = 0; i < targetData.length; i++)
    {
      final Object[] objects = targetData[i];
      if (objects == null)
      {
        continue;
      }

      targetData[i] = (Object[]) objects.clone();
      for (int j = 0; j < objects.length; j++)
      {
        final LValue object = (LValue) objects[j];
        if (object == null)
        {
          continue;
        }
        objects[j] = object.clone();
      }
    }

    table.setData(targetData, getColumnCount());
    return table;
  }

  /**
   * Querying the value type is only valid *after* the value has been
   * evaluated.
   *
   * @return
   */
  public Type getValueType()
  {
    return DataTableType.TYPE;
  }

  /**
   * Returns any dependent lvalues (parameters and operands, mostly).
   *
   * @return
   */
  public LValue[] getChildValues()
  {
    // too expensive ...
    return new LValue[0];
  }

  /**
   * Checks, whether the LValue is constant. Constant lvalues always return the
   * same value.
   *
   * @return
   */
  public boolean isConstant()
  {
    if (constant == null)
    {
      if (computeConstantValue())
      {
        constant = Boolean.TRUE;
      }
      else
      {
        constant = Boolean.FALSE;
      }
    }

    return Boolean.TRUE.equals(constant);
  }

  private boolean computeConstantValue()
  {
    final int rows = getRowCount();
    final int cols = getColumnCount();
    for (int row = 0; row < rows; row++)
    {
      for (int col = 0; col < cols; col++)
      {
        LValue value = getValueAt(row, col);
        if (value.isConstant() == false)
        {
          return false;
        }
      }
    }
    return true;
  }
}
