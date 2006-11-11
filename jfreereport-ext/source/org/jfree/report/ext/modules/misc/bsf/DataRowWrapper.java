/**
 * ===========================================
 * JFreeReport : a free Java reporting library
 * ===========================================
 *
 * Project Info:  http://www.jfree.org/liblayout/
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
 * DataRowWrapper.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corperation.
 */

package org.jfree.report.ext.modules.misc.bsf;

import org.jfree.report.DataFlags;
import org.jfree.report.DataRow;
import org.jfree.report.DataSourceException;

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

  public Object get(int col) throws DataSourceException
  {
    if (parent == null)
    {
      return null;
    }
    return parent.get(col);
  }

  public Object get(String col) throws DataSourceException
  {
    if (parent == null)
    {
      return null;
    }
    return parent.get(col);
  }

  public String getColumnName(int col) throws DataSourceException
  {
    if (parent == null)
    {
      return null;
    }
    return parent.getColumnName(col);
  }

  public int getColumnCount() throws DataSourceException
  {
    if (parent == null)
    {
      return 0;
    }
    return parent.getColumnCount();
  }

  public DataFlags getFlags(String col) throws DataSourceException
  {
    return parent.getFlags(col);
  }

  public DataFlags getFlags(int col) throws DataSourceException
  {
    return parent.getFlags(col);
  }
}
