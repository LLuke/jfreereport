/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
 *
 * (C) Copyright 2000-2003, by Simba Management Limited and Contributors.
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
 * DataSourceWriter.java
 * ---------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: DataSourceWriter.java,v 1.4 2003/02/21 11:31:13 mungady Exp $
 *
 * Changes
 * -------
 * 20-Feb-2003 : Added standard header and Javadocs (DG);
 *
 */

package com.jrefinery.report.io.ext.writer;

import com.jrefinery.report.filter.DataSource;
import com.jrefinery.report.io.ext.DataSourceHandler;
import com.jrefinery.report.io.ext.factory.datasource.DataSourceCollector;
import com.jrefinery.report.io.ext.factory.objects.ObjectDescription;

import java.io.IOException;
import java.io.Writer;

/**
 * A data-source writer.
 * 
 * @author Thomas Morgner.
 */
public class DataSourceWriter extends ObjectWriter
{
  /** The data-source. */
  private DataSourceCollector dataSourceCollector;

  /**
   * Creates a new writer.
   * 
   * @param reportWriter  the report writer.
   * @param baseObject  the base object.
   * @param objectDescription the object description.
   */
  public DataSourceWriter(ReportWriter reportWriter, Object baseObject, 
                          ObjectDescription objectDescription)
  {
    super(reportWriter, baseObject, objectDescription);
    dataSourceCollector = getReportWriter().getDataSourceCollector();
  }

  /**
   * Writes a parameter.
   * 
   * @param writer  the writer.
   * @param name  the name.
   * 
   * @throws IOException if there is an I/O problem.
   * @throws ReportWriterException if the report definition could not be written.
   */
  protected void writeParameter(Writer writer, String name)
      throws IOException, ReportWriterException
  {
    if (name.equals("dataSource"))
    {
      DataSource ds = (DataSource) getObjectDescription().getParameter(name);
      ObjectDescription dsDesc = getParameterDescription(name);
      String dsname = dataSourceCollector.getDataSourceName(dsDesc);

      if (dsname == null)
      {
        throw new ReportWriterException("The datasource type is not registered: " 
                                        + ds.getClass());
      }

      writeTag(writer, DataSourceHandler.DATASOURCE_TAG, "type", dsname, OPEN);

      DataSourceWriter dsWriter =
          new DataSourceWriter(getReportWriter(), ds, dsDesc);
      dsWriter.write(writer);

      writeCloseTag(writer, DataSourceHandler.DATASOURCE_TAG);

    }
    else
    {
      super.writeParameter(writer, name);
    }
  }
}
