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
 * PaginatingReportProcessor.java
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
package org.jfree.report.flow.paginating;

import org.jfree.layouting.DefaultPageGenerationLayoutProcess;
import org.jfree.layouting.DefaultPagePreparationLayoutProcess;
import org.jfree.layouting.PageGenerationLayoutProcess;
import org.jfree.layouting.PagePreparationLayoutProcess;
import org.jfree.layouting.output.pageable.PageableOutputProcessor;
import org.jfree.report.DataSourceException;
import org.jfree.report.ReportDataFactoryException;
import org.jfree.report.flow.DefaultFlowControler;
import org.jfree.report.flow.FlowControler;
import org.jfree.report.flow.LibLayoutReportTarget;
import org.jfree.report.flow.ReportJob;
import org.jfree.report.flow.ReportProcessor;
import org.jfree.report.flow.ReportTarget;
import org.jfree.resourceloader.ResourceManager;
import org.jfree.resourceloader.ResourceKey;

/**
 * Creation-Date: 02.04.2006, 15:11:55
 *
 * @author Thomas Morgner
 */
public class PaginatingReportProcessor implements ReportProcessor
{
  private PageableOutputProcessor outputProcessor;

  public PaginatingReportProcessor()
  {
  }

  public PageableOutputProcessor getOutputProcessor()
  {
    return outputProcessor;
  }

  public void setOutputProcessor(final PageableOutputProcessor outputProcessor)
  {
    this.outputProcessor = outputProcessor;
  }

  protected FlowControler createFlowControler(ReportJob job)
          throws DataSourceException
  {
    return new DefaultFlowControler(job);
  }

  protected ReportTarget createPrepareTarget(ReportJob job)
  {
    if (outputProcessor == null)
    {
      throw new IllegalStateException(
              "OutputProcessor is invalid.");
    }
    final PagePreparationLayoutProcess layoutProcess =
            new DefaultPagePreparationLayoutProcess(outputProcessor);
    final ResourceManager resourceManager = job.getReport().getResourceManager();
    final ResourceKey resourceKey = job.getReport().getBaseResource();

    return new LibLayoutReportTarget
            (resourceKey, resourceManager, layoutProcess.getInputFeed());
  }

  protected ReportTarget createGenerateTarget(ReportJob job)
  {
    if (outputProcessor == null)
    {
      throw new IllegalStateException(
              "OutputProcessor is invalid.");
    }
    final PageGenerationLayoutProcess layoutProcess =
            new DefaultPageGenerationLayoutProcess(outputProcessor, null, null);
    final ResourceManager resourceManager = job.getReport().getResourceManager();
    final ResourceKey resourceKey = job.getReport().getBaseResource();

    return new LibLayoutReportTarget
            (resourceKey, resourceManager, layoutProcess.getInputFeed());
  }

  public void processReport(ReportJob job)
          throws ReportDataFactoryException, DataSourceException
  {
    if (job == null)
    {
      throw new NullPointerException();
    }

    synchronized (job)
    {
      //prepareReport(job);
      //generateReport(job);
    }
  }


}
