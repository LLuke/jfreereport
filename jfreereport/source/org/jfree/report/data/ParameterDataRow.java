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
 * ParameterDataRow.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: ParameterDataRow.java,v 1.1 2006/04/18 11:49:11 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.report.data;

import org.jfree.report.DataRow;
import org.jfree.report.DataSourceException;
import org.jfree.report.structure.SubReport;
import org.jfree.report.util.ReportParameters;

/**
 * This is the first datarow in each report. It holds the values of all declared
 * input parameters. This datarow does not advance and does not keep track of
 * any changes, as parameters are considered read-only once the reporting has
 * started.
 *
 * @author Thomas Morgner
 */
public class ParameterDataRow extends StaticDataRow
{
  public ParameterDataRow(final ReportParameters parameters)
  {
    final String[] names = parameters.keys();
    final Object[] values = new Object[parameters.size()];

    for (int i = 0; i < names.length; i++)
    {
      final String key = names[i];
      values[i] = parameters.get(key);
    }
    setData(names, values);
  }

  public ParameterDataRow(final SubReport report, final DataRow dataRow)
          throws DataSourceException
  {
    final String[] outerNames = report.getInputParameters();
    final String[] innerNames = new String[outerNames.length];
    final Object[] values = new Object[outerNames.length];
    for (int i = 0; i < outerNames.length; i++)
    {
      String name = outerNames[i];
      innerNames[i] = report.getInnerParameter(name);
      values[i] = dataRow.get(name);
    }
    setData(innerNames, values);
  }
}
