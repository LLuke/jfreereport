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
 * $Id: AbstractReportProcessor.java,v 1.5 2006/12/03 20:24:09 taqua Exp $
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 */

package org.jfree.report.flow;

import org.jfree.formula.DefaultFormulaContext;
import org.jfree.report.DataSourceException;
import org.jfree.report.ReportDataFactoryException;
import org.jfree.report.ReportProcessingException;
import org.jfree.report.data.ReportContextImpl;
import org.jfree.report.flow.layoutprocessor.DefaultLayoutControllerFactory;
import org.jfree.report.flow.layoutprocessor.LayoutController;
import org.jfree.report.flow.layoutprocessor.LayoutControllerFactory;

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

  protected void processReportRun
      (final ReportJob job, final ReportTarget target)
      throws ReportDataFactoryException,
      DataSourceException, ReportProcessingException
  {
    synchronized (job)
    {
      final ReportContext context = createReportContext(job, target);
      final LayoutControllerFactory layoutFactory =
          context.getLayoutControllerFactory();
      // we have the data and we have our position inside the report.
      // lets generate something ...
      final FlowController flowController = createFlowControler(context, job);

      LayoutController layoutController =
          layoutFactory.create(flowController, job.getReportStructureRoot(), null);

      while (layoutController.isAdvanceable())
      {
        layoutController = layoutController.advance(target);
        target.commit();
      }
    }
  }

  protected ReportContext createReportContext (final ReportJob job,
                                               final ReportTarget target)
  {
    ReportContextImpl context = new ReportContextImpl();
    context.setExportDescriptor(target.getExportDescriptor());
    final DefaultLayoutControllerFactory lcf = new DefaultLayoutControllerFactory();
    lcf.initialize(job);
    context.setLayoutControllerFactory(lcf);

    final DefaultFormulaContext formulaContext = new DefaultFormulaContext();
    context.setFormulaContext(formulaContext);
    context.setResourceBundleFactory(job.getResourceBundleFactory());
    return context;
  }

  protected FlowController createFlowControler(final ReportContext context,
                                               final ReportJob job)
          throws DataSourceException
  {
    return new DefaultFlowController(context, job);
  }

}
