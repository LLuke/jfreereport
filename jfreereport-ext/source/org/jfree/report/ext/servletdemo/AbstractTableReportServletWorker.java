/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
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
 * -----------------------
 * AbstractTableReportServletWorker.java
 * -----------------------
 *
 * (C)opyright 2002, by Simba Management Limited and Contributors.
 *
 * Original Author:  David Gilbert (for Simba Management Limited);
 * Contributor(s):   Thomas Morgner;
 *
 * $Id: AbstractTableReportServletWorker.java,v 1.1 2003/07/08 14:21:48 taqua Exp $
 *
 * Changes
 * -------
 * 24-Jan-2003: Initial version
 */
package org.jfree.report.ext.servletdemo;

import org.jfree.report.ReportProcessingException;
import org.jfree.report.modules.output.table.base.TableProcessor;

/**
 * The report servlet worker provides the infrastructure needed to process the
 * report with a table output target. The worker handles the output processing and
 * provides structures to initializes the report.
 * 
 * @author Thomas Morgner
 */
public abstract class AbstractTableReportServletWorker extends AbstractReportServletWorker
{
  /** the tableprocessor that is used for outputing the report. */
  private TableProcessor tableProcessor;

  /**
   * Creates a new table servlet worker. Table based report don't use sessions for
   * the report processing.
   */
  public AbstractTableReportServletWorker()
  {
    super(null);
  }

  /**
   * Gets the used tableprocessor.
   *
   * @return the table processor.
   */
  public TableProcessor getTableProcessor()
  {
    return tableProcessor;
  }

  /**
   * Defines the table processor, that should be used in that servlet worker.
   *
   * @param tableProcessor the processor.
   */
  public void setTableProcessor(final TableProcessor tableProcessor)
  {
    this.tableProcessor = tableProcessor;
  }

  /**
   * Processes the report and generates the content.
   *
   * @throws ReportProcessingException if something went wrong during the report processing.
   */
  public void processReport ()
    throws ReportProcessingException
  {
    tableProcessor.processReport();
  }

}
