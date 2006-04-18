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
 * SinglePassReportProcessor.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id$
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.report.flow;

import org.jfree.report.DataSourceException;
import org.jfree.report.JFreeReport;
import org.jfree.report.ReportDataFactoryException;

/**
 * The abstract report processor implements a single-pass report processing
 * schema. This is suitable for most raw exports and the streaming-liblayout
 * export.
 *
 * @author Thomas Morgner
 */
public abstract class SinglePassReportProcessor implements ReportProcessor
{
  public SinglePassReportProcessor()
  {
  }


  protected FlowControler createFlowControler(ReportJob job)
          throws DataSourceException
  {
    return new DefaultFlowControler(job);
  }

  protected abstract ReportTarget createReportTarget (ReportJob job);

  /**
   * Bootstraps the local report processing. This way of executing the report
   * must be supported by *all* report processor implementations. It should
   * fully process the complete report.
   *
   * @param job
   * @throws ReportDataFactoryException
   */
  public void processReport (ReportJob job) throws ReportDataFactoryException,
          DataSourceException
  {
    // set up the scene
    final JFreeReport report = job.getReport();
    final LayoutControler layoutControler = new DefaultLayoutControler();

    // we have the data and we have our position inside the report.
    // lets generate something ...
    LayoutPosition position = new LayoutPosition
            (createFlowControler(job), report);
    final ReportTarget target = createReportTarget(job);
    while (position.getNode() != null)
    {
      position = layoutControler.process(target, position);
    }
  }
}
