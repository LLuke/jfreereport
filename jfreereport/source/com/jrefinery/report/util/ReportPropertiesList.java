/**
 * ----------------------
 * ReportPropertiesList.java
 * ----------------------
 * 
 * ChangeLog
 * ---------
 */
package com.jrefinery.report.util;

import java.util.ArrayList;
import java.util.Enumeration;

public class ReportPropertiesList
{
  private ReportProperties base;
  private ArrayList columns;

  public ReportPropertiesList(ReportProperties base)
  {
    if (base == null) throw new NullPointerException();
    this.base = base;
    this.columns = new ArrayList();

    Enumeration enum = base.keys();
    while (enum.hasMoreElements())
    {
      String key = (String) enum.nextElement();
      if (base.isMarked(key))
      {
        columns.add (key);
      }
    }
  }

  public int getColumnCount ()
  {
    return columns.size();
  }

  public String getColumnName (int column)
  {
    return (String) columns.get (column);
  }

  public Object get(int column)
  {
    return (base.get(getColumnName(column)));
  }
}
