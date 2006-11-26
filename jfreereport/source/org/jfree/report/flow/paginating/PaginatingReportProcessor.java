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
 * $Id: PaginatingReportProcessor.java,v 1.7 2006/11/24 17:12:12 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.report.flow.paginating;

import org.jfree.layouting.ChainingLayoutProcess;
import org.jfree.layouting.DefaultLayoutProcess;
import org.jfree.layouting.LayoutProcess;
import org.jfree.layouting.StateException;
import org.jfree.layouting.output.pageable.PageableOutputProcessor;
import org.jfree.layouting.util.IntList;
import org.jfree.report.DataSourceException;
import org.jfree.report.ReportDataFactoryException;
import org.jfree.report.ReportProcessingException;
import org.jfree.report.flow.AbstractReportProcessor;
import org.jfree.report.flow.FlowController;
import org.jfree.report.flow.LibLayoutReportTarget;
import org.jfree.report.flow.ReportContext;
import org.jfree.report.flow.ReportJob;
import org.jfree.report.flow.ReportTargetState;
import org.jfree.report.flow.layoutprocessor.LayoutController;
import org.jfree.report.flow.layoutprocessor.LayoutControllerFactory;
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
  private PageStateList stateList;
  private IntList physicalMapping;
  private IntList logicalMapping;

  public PaginatingReportProcessor(final PageableOutputProcessor outputProcessor)
  {
    this.outputProcessor = outputProcessor;
  }

  public PageableOutputProcessor getOutputProcessor()
  {
    return outputProcessor;
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
    prepareReportProcessing(job);

    // third, generate the content.

    // Have a look at the content ..
//    DisplayAllInterceptor dia = new DisplayAllInterceptor();
//    GraphicsOutputProcessor gop = (GraphicsOutputProcessor) outputProcessor;
//    gop.setInterceptor(dia);

//    processReportRun(job, createTarget(job));
//
//    // Using the interceptor to get a specific page without state management.
//    DisplayInterceptor di =
//        new DisplayInterceptor(outputProcessor.getLogicalPage(1));
//    gop.setInterceptor(di);
//
//    processReportRun(job, createTarget(job));
//
//    try
//    {
//      // Using the state management.
//      final PageState state = (PageState) stateList.get(1);
//      final ReportTargetState o = state.getTargetState();
//      final LayoutPosition position = state.getLayoutPosition();
//
//      final LibLayoutReportTarget target =
//          (LibLayoutReportTarget) o.restore(outputProcessor);
//      gop.setInterceptor(dia);
//      continueFromPos(position, target);
//    }
//    catch (StateException e)
//    {
//      e.printStackTrace();
//    }
//
  }

  protected void prepareReportProcessing(final ReportJob job)
      throws ReportDataFactoryException, DataSourceException, ReportProcessingException
  {
    if (job == null)
    {
      throw new NullPointerException();
    }

    long start = System.currentTimeMillis();
    // first, compute the globals
    processReportRun(job, createTarget(job));
    if (outputProcessor.isGlobalStateComputed() == false)
    {
      throw new ReportProcessingException
          ("Pagination has not yet been finished.");
    }

    // second, paginate
    processPaginationRun(job, createTarget(job));
    if (outputProcessor.isPaginationFinished() == false)
    {
      throw new ReportProcessingException
          ("Pagination has not yet been finished.");
    }

    if (outputProcessor.isContentGeneratable() == false)
    {
      throw new ReportProcessingException
          ("Illegal State.");
    }

    long end = System.currentTimeMillis();
    System.out.println("Pagination-Time: " + (end - start));
  }

  protected PageStateList processPaginationRun(final ReportJob job,
                                               final LibLayoutReportTarget target)
      throws ReportDataFactoryException,
      DataSourceException, ReportProcessingException
  {
    synchronized (job)
    {
      stateList = new PageStateList(this);
      physicalMapping = new IntList(40);
      logicalMapping = new IntList(20);

      final ReportContext context = createReportContext(job, target);
      final LayoutControllerFactory layoutFactory =
          context.getLayoutControllerFactory();

      // we have the data and we have our position inside the report.
      // lets generate something ...
      final FlowController flowController = createFlowControler(context, job);

      LayoutController layoutController =
          layoutFactory.create(flowController, job.getReport(), null);

      try
      {
        stateList.add(new PageState(target.saveState(), layoutController,
            outputProcessor.getPageCursor()));
        int logPageCount = outputProcessor.getLogicalPageCount();
        int physPageCount = outputProcessor.getPhysicalPageCount();

        while (layoutController.isAdvanceable())
        {
          layoutController = layoutController.advance(target);
          target.commit();

          // check whether a pagebreak has been encountered.
          if (target.isPagebreakEncountered())
          {
            // So we hit a pagebreak. Store the state for later reuse.
            // A single state can refer to more than one physical page.

            int newLogPageCount = outputProcessor.getLogicalPageCount();
            int newPhysPageCount = outputProcessor.getPhysicalPageCount();

            int result = stateList.size() - 1;
            for (; physPageCount < newPhysPageCount; physPageCount++)
            {
              physicalMapping.add(result);
            }

            for (; logPageCount < newLogPageCount; logPageCount++)
            {
              logicalMapping.add(result);
            }

            stateList.add(new PageState(target.saveState(), layoutController,
                outputProcessor.getPageCursor()));
            target.resetPagebreakFlag();
          }
        }

        // And when we reached the end, add the remaining pages ..
        int newLogPageCount = outputProcessor.getLogicalPageCount();
        int newPhysPageCount = outputProcessor.getPhysicalPageCount();

        int result = stateList.size() - 1;
        for (; physPageCount < newPhysPageCount; physPageCount++)
        {
          physicalMapping.add(result);
        }

        for (; logPageCount < newLogPageCount; logPageCount++)
        {
          logicalMapping.add(result);
        }
      }
      catch (StateException e)
      {
        throw new ReportProcessingException("Argh, Unable to save the state!");
      }

      Log.debug("After pagination we have " + stateList.size() + " states");
      return stateList;
    }
  }

  public boolean isPaginated()
  {
    return outputProcessor.isPaginationFinished();
  }

  protected PageState getLogicalPageState (int page)
  {
    return stateList.get(logicalMapping.get(page));
  }

  protected PageState getPhysicalPageState (int page)
  {
    return stateList.get(physicalMapping.get(page));
  }

  public PageState processPage(PageState previousState)
      throws StateException, ReportProcessingException,
      ReportDataFactoryException, DataSourceException
  {
    final ReportTargetState targetState = previousState.getTargetState();
    final LibLayoutReportTarget target =
        (LibLayoutReportTarget) targetState.restore(outputProcessor);
    outputProcessor.setPageCursor(previousState.getPageCursor());

    LayoutController position = previousState.getLayoutController();

    // we have the data and we have our position inside the report.
    // lets generate something ...

    while (position.isAdvanceable())
    {
      position = position.advance(target);
      target.commit();

      // check whether a pagebreak has been encountered.
      if (target.isPagebreakEncountered())
      {
        // So we hit a pagebreak. Store the state for later reuse.
        // A single state can refer to more than one physical page.
        final PageState state = new PageState
            (target.saveState(), position, outputProcessor.getPageCursor());
        target.resetPagebreakFlag();
        return state;
      }
    }
    // reached the finish state .. this is bad!
    return null;
  }
}
