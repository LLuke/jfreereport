/**
 * Date: Jan 24, 2003
 * Time: 11:38:01 PM
 *
 * $Id$
 */
package com.jrefinery.report.ext.demo;

import com.jrefinery.report.ReportProcessingException;
import com.jrefinery.report.targets.table.TableProcessor;

public abstract class AbstractTableReportServletWorker extends AbstractReportServletWorker
{
  private TableProcessor tableProcessor;

  public AbstractTableReportServletWorker()
  {
    super(null);
  }

  public TableProcessor getTableProcessor()
  {
    return tableProcessor;
  }

  public void setTableProcessor(TableProcessor tableProcessor)
  {
    this.tableProcessor = tableProcessor;
  }

  public void processReport ()
    throws ReportProcessingException
  {
    tableProcessor.processReport();
  }

}
