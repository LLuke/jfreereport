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
 * ---------------------
 * ReportPropertiesList.java
 * ---------------------
 * (C)opyright 2000-2002, by Simba Management Limited.
 *
 * $Id$
 *
 * Changes
 * -------
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
