package org.jfree.report.modules.misc.bsf;

import org.jfree.report.DataRow;

/**
 * Creation-Date: 12.09.2005, 19:21:02
 *
 * @author Thomas Morgner
 */
public class DataRowWrapper implements DataRow
{
  private DataRow parent;

  public DataRowWrapper()
  {
  }

  public DataRow getParent()
  {
    return parent;
  }

  public void setParent(final DataRow parent)
  {
    this.parent = parent;
  }

  public Object get(int col)
  {
    if (parent == null)
    {
      return null;
    }
    return parent.get(col);
  }

  public Object get(String col) throws IllegalStateException
  {
    if (parent == null)
    {
      return null;
    }
    return parent.get(col);
  }

  public String getColumnName(int col)
  {
    if (parent == null)
    {
      return null;
    }
    return parent.getColumnName(col);
  }

  public int findColumn(String name)
  {
    if (parent == null)
    {
      return -1;
    }
    return parent.findColumn(name);
  }

  public int getColumnCount()
  {
    if (parent == null)
    {
      return 0;
    }
    return parent.getColumnCount();
  }
}
