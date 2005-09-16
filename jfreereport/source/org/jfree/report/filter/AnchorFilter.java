/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2005, by Object Refinery Limited and Contributors.
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
 * AnchorFilter.java
 * ------------
 * (C) Copyright 2002-2005, by Object Refinery Limited.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: AnchorFilter.java,v 1.2 2005/03/03 22:59:59 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.report.filter;

import org.jfree.report.Anchor;

public class AnchorFilter implements DataFilter
{
  private DataSource dataSource;

  public AnchorFilter()
  {
  }

  /**
   * Clones this <code>DataSource</code>.
   *
   * @return the clone.
   * @throws CloneNotSupportedException this should never happen.
   */
  public Object clone()
          throws CloneNotSupportedException
  {
    final AnchorFilter af = (AnchorFilter) super.clone();
    if (dataSource == null)
    {
      af.dataSource = null;
    }
    else
    {
      af.dataSource = (DataSource) dataSource.clone();
    }
    return af;
  }

  /**
   * Returns the current value for the data source.
   *
   * @return the value.
   */
  public Object getValue()
  {
    if (dataSource == null)
    {
      return null;
    }
    final Object value = dataSource.getValue();
    if (value == null)
    {
      return null;
    }
    if (value instanceof Anchor)
    {
      return value;
    }
    return new Anchor(String.valueOf(value));
  }

  /**
   * Returns the assigned DataSource for this Target.
   *
   * @return The datasource.
   */
  public DataSource getDataSource()
  {
    return dataSource;
  }

  /**
   * Assigns a DataSource for this Target.
   *
   * @param ds The data source.
   */
  public void setDataSource(final DataSource ds)
  {
    this.dataSource = ds;
  }
}
