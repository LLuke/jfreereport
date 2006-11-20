/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://jfreereport.pentaho.org/
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
 * $Id: AbstractReportProcessor.java,v 1.1 2006/11/11 20:41:14 taqua Exp $
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 */

package org.jfree.report.flow;

import org.jfree.report.ReportDataFactoryException;
import org.jfree.report.DataSourceException;
import org.jfree.report.ReportProcessingException;

/**
 * Creation-Date: 10.11.2006, 16:07:26
 *
 * @author Thomas Morgner
 */
public abstract class AbstractReportProcessor implements ReportProcessor
{
  public AbstractReportProcessor()
  {
  }

  protected void processReportRun(final ReportJob job, final ReportTarget target)
      throws ReportDataFactoryException,
      DataSourceException, ReportProcessingException
  {
    synchronized (job)
    {
      // set up the scene
      final LayoutController layoutController = new DefaultLayoutController();

      // we have the data and we have our position inside the report.
      // lets generate something ...
      final FlowController flowController = createFlowControler(job, target);
      LayoutPosition position = layoutController.createInitialPosition
              (flowController, job.getReport());
      while (position.isFinalPosition() == false)
      {
        position = layoutController.process(target, position);
        target.commit();
      }
    }
  }

  protected FlowController createFlowControler(ReportJob job, ReportTarget target)
          throws DataSourceException
  {
    return new DefaultFlowController(job, target.getExportDescriptor());
  }

}
