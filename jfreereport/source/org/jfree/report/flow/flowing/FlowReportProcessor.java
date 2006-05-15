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
 * FlowReportProcessor.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: FlowReportProcessor.java,v 1.2 2006/04/21 17:31:23 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.report.flow.flowing;

import org.jfree.layouting.DefaultFlowGenerationLayoutProcess;
import org.jfree.layouting.DefaultFlowPreparationLayoutProcess;
import org.jfree.layouting.FlowGenerationLayoutProcess;
import org.jfree.layouting.FlowPreparationLayoutProcess;
import org.jfree.layouting.output.flowing.FlowingOutputProcessor;
import org.jfree.report.DataSourceException;
import org.jfree.report.ReportDataFactoryException;
import org.jfree.report.ReportProcessingException;
import org.jfree.report.flow.DefaultFlowControler;
import org.jfree.report.flow.DefaultLayoutControler;
import org.jfree.report.flow.FlowControler;
import org.jfree.report.flow.LayoutControler;
import org.jfree.report.flow.LayoutPosition;
import org.jfree.report.flow.LibLayoutReportTarget;
import org.jfree.report.flow.ReportJob;
import org.jfree.report.flow.ReportProcessor;
import org.jfree.report.flow.ReportTarget;
import org.jfree.resourceloader.ResourceKey;
import org.jfree.resourceloader.ResourceManager;

/**
 * Creation-Date: 02.04.2006, 15:11:55
 *
 * @author Thomas Morgner
 */
public class FlowReportProcessor implements ReportProcessor
{
  private FlowingOutputProcessor outputProcessor;

  public FlowReportProcessor()
  {
  }

  public FlowingOutputProcessor getOutputProcessor()
  {
    return outputProcessor;
  }

  public void setOutputProcessor(final FlowingOutputProcessor outputProcessor)
  {
    this.outputProcessor = outputProcessor;
  }

  protected FlowControler createFlowControler(ReportJob job)
          throws DataSourceException
  {
    return new DefaultFlowControler(job);
  }

  protected ReportTarget createPrepareTarget(final ReportJob job)
  {
    if (outputProcessor == null)
    {
      throw new IllegalStateException(
              "OutputProcessor is invalid.");
    }
    final FlowPreparationLayoutProcess layoutProcess =
            new DefaultFlowPreparationLayoutProcess(outputProcessor);
    final ResourceManager resourceManager = job.getReport().getResourceManager();
    final ResourceKey resourceKey = job.getReport().getBaseResource();

    return new LibLayoutReportTarget
            (job, resourceKey, resourceManager, layoutProcess.getInputFeed());
  }

  protected ReportTarget createGenerateTarget(final ReportJob job)
  {
    if (outputProcessor == null)
    {
      throw new IllegalStateException(
              "OutputProcessor is invalid.");
    }
    final FlowGenerationLayoutProcess layoutProcess =
            new DefaultFlowGenerationLayoutProcess(outputProcessor);
    final ResourceManager resourceManager = job.getReport().getResourceManager();
    final ResourceKey resourceKey = job.getReport().getBaseResource();

    return new LibLayoutReportTarget
            (job, resourceKey, resourceManager, layoutProcess.getInputFeed());
  }


  /**
   * Bootstraps the local report processing. This way of executing the report
   * must be supported by *all* report processor implementations. It should
   * fully process the complete report.
   *
   * @param job
   * @throws ReportDataFactoryException
   */
  public void processReport (final ReportJob job)
          throws ReportDataFactoryException, DataSourceException,
          ReportProcessingException
  {
    if (job == null)
    {
      throw new NullPointerException();
    }

    synchronized (job)
    {
      prepareReport(job);
      generateReport(job);
    }
  }


  protected void prepareReport (final ReportJob job)
          throws ReportDataFactoryException, DataSourceException,
          ReportProcessingException
  {
    synchronized (job)
    {
      // set up the scene
      final LayoutControler layoutControler = new DefaultLayoutControler();

      // we have the data and we have our position inside the report.
      // lets prepare the layout ...
      final FlowControler flowControler = createFlowControler(job);
      LayoutPosition position = layoutControler.createInitialPosition
              (flowControler, job.getReport());
      final ReportTarget prepareTarget = createPrepareTarget(job);
      while (position.isFinalPosition())
      {
        position = layoutControler.process(prepareTarget, position);
      }
    }
  }

  protected void generateReport (final ReportJob job)
          throws ReportDataFactoryException, DataSourceException,
          ReportProcessingException
  {
    synchronized (job)
    {
      // set up the scene
      final LayoutControler layoutControler = new DefaultLayoutControler();

      // we have the data and we have our position inside the report.
      // lets prepare the layout ...
      final FlowControler flowControler = createFlowControler(job);
      LayoutPosition position = layoutControler.createInitialPosition
              (flowControler, job.getReport());
      final ReportTarget generateTarget = createGenerateTarget(job);
      while (position.isFinalPosition())
      {
        position = layoutControler.process(generateTarget, position);
      }
    }
  }


}
