/**
 * Date: Jan 22, 2003
 * Time: 5:02:13 PM
 *
 * $Id: DataSourceWriter.java,v 1.2 2003/01/23 18:07:46 taqua Exp $
 */
package com.jrefinery.report.io.ext.writer;

import com.jrefinery.report.filter.DataSource;
import com.jrefinery.report.io.ext.DataSourceHandler;
import com.jrefinery.report.io.ext.factory.datasource.DataSourceCollector;
import com.jrefinery.report.io.ext.factory.objects.ObjectDescription;

import java.io.IOException;
import java.io.Writer;

public class DataSourceWriter extends ObjectWriter
{
  private DataSourceCollector dataSourceCollector;

  public DataSourceWriter(ReportWriter reportWriter, Object baseObject, ObjectDescription objectDescription)
  {
    super(reportWriter, baseObject, objectDescription);
    dataSourceCollector = getReportWriter().getDataSourceCollector();
  }

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
        throw new ReportWriterException("The datasource type is not registered: " +
                                        ds.getClass());
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
