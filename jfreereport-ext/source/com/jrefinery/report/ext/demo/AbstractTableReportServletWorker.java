/**
 * =============================================================
 * JFreeReport : an open source reporting class library for Java
 * =============================================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport;
 * Project Lead:  David Gilbert (david.gilbert@jrefinery.com);
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
 * $Id: AbstractTableReportServletWorker.java,v 1.1 2003/01/25 02:56:17 taqua Exp $
 *
 * Changes
 * -------
 * 24-Jan-2003: Initial version
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
