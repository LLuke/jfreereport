/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
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
 * $Id: DataSourceWriter.java,v 1.15 2003/06/27 14:25:19 taqua Exp $
 *
 * Changes
 * -------
 * 20-Feb-2003 : Added standard header and Javadocs (DG);
 *
 */

package com.jrefinery.report.io.ext.writer;

import java.io.IOException;
import java.io.Writer;

import com.jrefinery.report.filter.DataSource;
import com.jrefinery.report.io.ext.DataSourceHandler;
import com.jrefinery.report.io.ext.factory.datasource.DataSourceCollector;
import org.jfree.xml.factory.objects.ObjectDescription;

/**
 * A data-source writer. Writes datasources and templates.
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
   * @param indent the current indention level.
   */
  public DataSourceWriter(final ReportWriter reportWriter, final DataSource baseObject,
                          final ObjectDescription objectDescription, final int indent)
  {
    super(reportWriter, baseObject, objectDescription, indent);
    if (DataSource.class.isAssignableFrom(objectDescription.getObjectClass()) == false)
    {
      throw new IllegalArgumentException("Expect a datasource description, but got "
          + objectDescription.getObjectClass());
    }
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
  protected void writeParameter(final Writer writer, final String name)
      throws IOException, ReportWriterException
  {
    if (name.equals("dataSource"))
    {
      final DataSource ds = (DataSource) getObjectDescription().getParameter(name);
      final ObjectDescription dsDesc = getParameterDescription(name);
      final String dsname = dataSourceCollector.getDataSourceName(dsDesc);

      if (dsname == null)
      {
        throw new ReportWriterException("The datasource type is not registered: "
            + ds.getClass());
      }

      writeTag(writer, DataSourceHandler.DATASOURCE_TAG, "type", dsname, OPEN);

      final DataSourceWriter dsWriter =
          new DataSourceWriter(getReportWriter(), ds, dsDesc, getIndentLevel());
      dsWriter.write(writer);

      writeCloseTag(writer, DataSourceHandler.DATASOURCE_TAG);

    }
    else
    {
      super.writeParameter(writer, name);
    }
  }
}
