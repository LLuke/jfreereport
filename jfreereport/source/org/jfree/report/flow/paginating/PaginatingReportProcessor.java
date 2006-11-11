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
 * $Id: PaginatingReportProcessor.java,v 1.3 2006/07/11 13:24:40 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.report.flow.paginating;

import java.util.ArrayList;

import org.jfree.layouting.DefaultLayoutProcess;
import org.jfree.layouting.LayoutProcess;
import org.jfree.layouting.StateException;
import org.jfree.layouting.ChainingLayoutProcess;
import org.jfree.layouting.output.pageable.PageableOutputProcessor;
import org.jfree.layouting.output.pageable.graphics.DisplayAllInterceptor;
import org.jfree.layouting.output.pageable.graphics.DisplayInterceptor;
import org.jfree.layouting.output.pageable.graphics.GraphicsOutputProcessor;
import org.jfree.report.DataSourceException;
import org.jfree.report.ReportDataFactoryException;
import org.jfree.report.ReportProcessingException;
import org.jfree.report.flow.AbstractReportProcessor;
import org.jfree.report.flow.DefaultLayoutControler;
import org.jfree.report.flow.FlowControler;
import org.jfree.report.flow.LayoutControler;
import org.jfree.report.flow.LayoutPosition;
import org.jfree.report.flow.LibLayoutReportTarget;
import org.jfree.report.flow.ReportJob;
import org.jfree.report.flow.ReportTargetState;
import org.jfree.resourceloader.ResourceKey;
import org.jfree.resourceloader.ResourceManager;
import org.jfree.util.Log;

/**
 * Paginating report processors are multi-pass processors.
 * <p/>
 * This is written to use LibLayout. It will never work with other report
 * targets.
 *
 * @author Thomas Morgner
 */
public class PaginatingReportProcessor extends AbstractReportProcessor
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

  protected LibLayoutReportTarget createTarget(ReportJob job)
  {
    if (outputProcessor == null)
    {
      throw new IllegalStateException("OutputProcessor is invalid.");
    }

    final LayoutProcess layoutProcess =
        new ChainingLayoutProcess(new DefaultLayoutProcess(outputProcessor));
    final ResourceManager resourceManager = job.getReport().getResourceManager();
    final ResourceKey resourceKey = job.getReport().getBaseResource();

    return new LibLayoutReportTarget
        (job, resourceKey, resourceManager, layoutProcess);
  }

  public void processReport(ReportJob job)
      throws ReportDataFactoryException,
      DataSourceException, ReportProcessingException
  {
    if (job == null)
    {
      throw new NullPointerException();
    }

    // first, compute the globals
    processReportRun(job, createTarget(job));
    // second, paginate
    final ArrayList stateList = processPaginationRun(job, createTarget(job));
    if (outputProcessor.isPaginationFinished() == false)
    {
      throw new ReportProcessingException
          ("Pagination has not yet been finished.");
    }

    Log.debug("Generated: " + outputProcessor.getLogicalPageCount() + " logical pages");
    Log.debug("Generated: " + outputProcessor.getPhysicalPageCount() + " physical pages");
    // third, generate the content.

    // Have a look at the content ..
    DisplayAllInterceptor dia = new DisplayAllInterceptor();
    GraphicsOutputProcessor gop = (GraphicsOutputProcessor) outputProcessor;
//    gop.setInterceptor(dia);

//    processReportRun(job, createTarget(job));
//
//    // Using the interceptor to get a specific page without state management.
    DisplayInterceptor di =
        new DisplayInterceptor(outputProcessor.getLogicalPage(1));
    gop.setInterceptor(di);

    processReportRun(job, createTarget(job));



    try
    {
      // Using the state management.
      final PageState state = (PageState) stateList.get(1);
      final ReportTargetState o = state.getTargetState();
      final LayoutPosition position = state.getLayoutPosition();

      final LibLayoutReportTarget target =
          (LibLayoutReportTarget) o.restore(outputProcessor);
      gop.setInterceptor(dia);
      continueFromPos(position, target);
    }
    catch (StateException e)
    {
      e.printStackTrace();
    }

  }


  protected void continueFromPos (LayoutPosition position,
                                  final LibLayoutReportTarget target)
      throws ReportDataFactoryException,
      DataSourceException, ReportProcessingException
  {
    ReportJob job = target.getReportJob();
    synchronized (job)
    {
      // set up the scene
      final LayoutControler layoutControler = new DefaultLayoutControler();

      // we have the data and we have our position inside the report.
      // lets generate something ...
      while (position.isFinalPosition() == false)
      {
        position = layoutControler.process(target, position);
        target.commit();
      }
    }
  }



  protected ArrayList processPaginationRun(final ReportJob job,
                                      final LibLayoutReportTarget target)
      throws ReportDataFactoryException,
      DataSourceException, ReportProcessingException
  {
    synchronized (job)
    {
      final ArrayList states = new ArrayList();

      // set up the scene
      final LayoutControler layoutControler = new DefaultLayoutControler();

      // we have the data and we have our position inside the report.
      // lets generate something ...
      final FlowControler flowControler = createFlowControler(job);
      LayoutPosition position = layoutControler.createInitialPosition
          (flowControler, job.getReport());
      try
      {
        states.add(new PageState(target.saveState(), position));

        while (position.isFinalPosition() == false)
        {
          position = layoutControler.process(target, position);
          target.commit();

          // check whether a pagebreak has been encountered.
          if (target.isPagebreakEncountered())
          {
            // So we hit a pagebreak. Store the state for later reuse.
            Log.debug ("*********************************************************");
            Log.debug ("************* SAVED A PAGE-STATE ************************");
            Log.debug ("*********************************************************");

            states.add(new PageState(target.saveState(), position));
            target.resetPagebreakFlag();
          }
        }
      }
      catch (StateException e)
      {
        throw new ReportProcessingException("Blah!");
      }

      Log.debug ("After pagination we have " + states.size() + " states");
      return states;
    }
  }


}
