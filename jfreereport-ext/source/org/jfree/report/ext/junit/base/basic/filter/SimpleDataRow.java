package org.jfree.report.ext.junit.base.basic.filter;

import java.util.ArrayList;

import org.jfree.report.DataRow;

public class SimpleDataRow implements DataRow
{
  private ArrayList names;
  private ArrayList values;

  public SimpleDataRow ()
  {
    names = new ArrayList();
    values = new ArrayList();
  }

  /**
   * Returns the column position of the column, expression or function with the given name
   * or -1 if the given name does not exist in this DataRow.
   *
   * @param name the item name.
   * @return the item index.
   */
  public int findColumn (String name)
  {
    return names.indexOf(name);
  }

  /**
   * Returns the value of the expression or column in the tablemodel using the given
   * column number as index. For functions and expressions, the <code>getValue()</code>
   * method is called and for columns from the tablemodel the tablemodel method
   * <code>getValueAt(row, column)</code> gets called.
   *
   * @param col the item index.
   * @return the value.
   *
   * @throws IllegalStateException if the datarow detected a deadlock.
   */
  public Object get (int col)
  {
    return values.get(col);
  }

  /**
   * Returns the value of the function, expression or column using its specific name. The
   * given name is translated into a valid column number and the the column is queried.
   * For functions and expressions, the <code>getValue()</code> method is called and for
   * columns from the tablemodel the tablemodel method <code>getValueAt(row,
   * column)</code> gets called.
   *
   * @param col the item index.
   * @return the value.
   *
   * @throws IllegalStateException if the datarow detected a deadlock.
   */
  public Object get (String col)
          throws IllegalStateException
  {
    // todo implement me
    int index = names.indexOf(col);
    if (index == -1)
    {
      return null;
    }
    return values.get(index);
  }

  /**
   * Returns the number of columns, expressions and functions and marked ReportProperties
   * in the report.
   *
   * @return the item count.
   */
  public int getColumnCount ()
  {
    return values.size();
  }

  /**
   * Returns the name of the column, expression or function. For columns from the
   * tablemodel, the tablemodels <code>getColumnName</code> method is called. For
   * functions, expressions and report properties the assigned name is returned.
   *
   * @param col the item index.
   * @return the name.
   */
  public String getColumnName (int col)
  {
    return (String) names.get(col);
  }

  public void add (String name, Object value)
  {
    names.add(name);
    values.add(value);
  }
}
