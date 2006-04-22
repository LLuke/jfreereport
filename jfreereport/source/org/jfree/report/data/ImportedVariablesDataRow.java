/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2006, by Object Refinery Limited, Pentaho Corporation and Contributors.
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
 * ImportedVariablesDataRow.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: ImportedVariablesDataRow.java,v 1.1 2006/04/18 11:49:11 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.report.data;

import org.jfree.report.DataRow;
import org.jfree.report.DataSourceException;

/**
 * Creation-Date: 06.03.2006, 18:15:06
 *
 * @author Thomas Morgner
 */
public class ImportedVariablesDataRow extends StaticDataRow
{
  private String[] outerNames;
  private String[] innerNames;

  public ImportedVariablesDataRow(final GlobalMasterRow innerRow)
          throws DataSourceException
  {
    final DataRow globalView = innerRow.getGlobalView();
    final int cols = globalView.getColumnCount();
    this.outerNames = new String[cols];
    this.innerNames = outerNames;
    final Object[] values = new Object[outerNames.length];
    for (int i = 0; i < outerNames.length; i++)
    {
      outerNames[i] = globalView.getColumnName(i);
      values[i] = globalView.get(i);
    }
    setData(outerNames, values);
  }

  public ImportedVariablesDataRow(final GlobalMasterRow innerRow,
                                  final String[] outerNames,
                                  final String[] innerNames)
          throws DataSourceException
  {
    if (outerNames.length != innerNames.length)
    {
      throw new IllegalArgumentException();
    }

    this.outerNames = (String[]) outerNames.clone();
    this.innerNames = (String[]) innerNames.clone();
    final Object[] values = new Object[outerNames.length];
    final DataRow globalView = innerRow.getGlobalView();
    for (int i = 0; i < innerNames.length; i++)
    {
      String name = innerNames[i];
      values[i] = globalView.get(name);
    }
    setData(outerNames, values);
  }

  protected ImportedVariablesDataRow(final ImportedVariablesDataRow dataRow)
  {
    super(dataRow);
    outerNames = dataRow.outerNames;
    innerNames = dataRow.innerNames;
  }

  public ImportedVariablesDataRow advance (final GlobalMasterRow innerRow)
          throws DataSourceException
  {
    final DataRow globalView = innerRow.getGlobalView();
    final Object[] values = new Object[outerNames.length];
    for (int i = 0; i < innerNames.length; i++)
    {
      String name = innerNames[i];
      values[i] = globalView.get(name);
    }
    ImportedVariablesDataRow idr = new ImportedVariablesDataRow(this);
    idr.setData(outerNames, values);
    return idr;
  }
}
