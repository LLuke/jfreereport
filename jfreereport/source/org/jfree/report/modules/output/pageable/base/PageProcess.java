/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
 *
 * (C) Copyright 2000-2004, by Object Refinery Limited and Contributors.
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
 * ------------------------------
 * PageProcess.java
 * ------------------------------
 * (C)opyright 2004, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Object Refinery Limited);
 *
 * $Id: PageProcess.java,v 1.9 2005/08/08 15:36:32 taqua Exp $
 *
 * Changes 
 * -------------------------
 * 15.04.2004 : Initial version
 *  
 */

package org.jfree.report.modules.output.pageable.base;

import java.lang.ref.WeakReference;

import org.jfree.report.PageDefinition;
import org.jfree.report.ReportEventException;
import org.jfree.report.ReportInterruptedException;
import org.jfree.report.ReportProcessingException;
import org.jfree.report.modules.output.meta.MetaPage;
import org.jfree.report.modules.output.pageable.base.pagelayout.PageLayouter;
import org.jfree.report.states.ReportState;
import org.jfree.report.states.StartState;
import org.jfree.util.Log;

public class PageProcess
{
  private static class CacheInformation
  {
    private ReportState initialState;
    private ReportState finalState;

    public CacheInformation (final ReportState initialState,
                             final ReportState finalState)
    {
      this.initialState = initialState;
      this.finalState = finalState;
    }

    public ReportState getInitialState ()
    {
      return initialState;
    }

    public ReportState getFinalState ()
    {
      return finalState;
    }
  }

  private boolean handleInterruptedState;
  private OutputTarget outputTarget;
  private PageDefinition pageDefinition;
  private MetaPage metaPage;
  private WeakReference lastRecentlyUsedState;

  public PageProcess (final OutputTarget target,
                      final PageDefinition page,
                      final boolean handleInterrupt)
  {
    this.handleInterruptedState = handleInterrupt;
    this.outputTarget = target;
    this.pageDefinition = page;
  }

  public boolean isHandleInterruptedState ()
  {
    return handleInterruptedState;
  }

  /**
   * Checks, whether the current thread is interrupted.
   *
   * @throws org.jfree.report.ReportInterruptedException
   *          if the thread is interrupted to abort the report processing.
   */
  private void checkInterrupted ()
          throws ReportInterruptedException
  {
    if (isHandleInterruptedState() && Thread.interrupted())
    {
      throw new ReportInterruptedException("Current thread is interrupted. Returning.");
    }
  }

  public final synchronized ReportState processPage (final ReportState currPage,
                                                     final boolean failOnError)
          throws ReportProcessingException
  {
    if (currPage == null)
    {
      throw new NullPointerException("State != null");
    }
    // if a finish state is set to be processed, crash to make sure that FinishStates
    // are caught outside, we won't handle them here
    if (currPage.isFinish())
    {
      throw new IllegalArgumentException("No finish state for processpage allowed: ");
    }

    if (lastRecentlyUsedState != null)
    {
      final CacheInformation ci = (CacheInformation) lastRecentlyUsedState.get();
      if (ci != null && ci.getInitialState() == currPage)
      {
        return ci.getFinalState();
      }
    }

    ReportState state = null;
    PageLayouter lm = null;
    metaPage = null;
    try
    {
      checkInterrupted();

      try
      {
        state = (ReportState) currPage.clone();
      }
      catch (CloneNotSupportedException cne)
      {
        throw new ReportProcessingException("Clone not supported by ReportState?!");
      }
      lm = (PageLayouter) state.getDataRow().get(PageableReportProcessor.LAYOUTMANAGER_NAME);
      lm.setLogicalPage(new AlignedLogicalPage(outputTarget, pageDefinition));
      lm.restoreSaveState(state);

      // check, whether this is a StartState instance. If so, advance it, so
      // that the reportInitialized() event is always fired before any other event.
      // (even the page events!)
      // the reportInitialized event does never generate any output.
      state = ensureReportInitialized(state);
      lm.restartPage(state);
      // docmark: page spanning bands will affect this badly designed code.
      // this code will definitly be affected by the Band-intenal-pagebreak code
      // to this is non-fatal. the next redesign is planed here :)

      // The state restoration must not finish the current page, except if the whole
      // report processing will be finished.
      if (lm.isPageEnded())
      {
        state = state.advance();
        if (state.isFinish() == false)
        {
          throw new ReportProcessingException("State finished page during restore");
        }
      }
      else
      {
        // Do some real work.  The report header and footer, and the page headers and footers are
        // just decorations, as far as the report state is concerned.  The state only changes in
        // the following code...

        // this loop advances the report state until the next page gets started or
        // the end of the reporting is reached.
        // note: Dont test the end of the page, this gives no hint whether there will
        // be a next page ...
        boolean autoPageBreak = false;
        ReportState.PageBreakSaveState oldState = null;

        while ((lm.isPageEnded() == false) &&
               (state.isFinish() == false) && autoPageBreak == false)

        {
          final PageLayouter org = (PageLayouter) state.getDataRow().get(PageableReportProcessor.LAYOUTMANAGER_NAME);
          oldState = state.createPageProgressCopy();
          lm = (PageLayouter) state.getDataRow().get(PageableReportProcessor.LAYOUTMANAGER_NAME);
          state = state.advance();
          if (failOnError)
          {
            if (state.isErrorOccured() == true)
            {
              throw new ReportEventException("Failed to dispatch an event.", state.getErrors());
            }
          }
          else
          {
            if (state.isErrorOccured() == true)
            {
              Log.error("Failed to dispatch an event.",
                      new ReportEventException("Failed to dispatch an event.", state.getErrors()));
            }
          }
          lm = (PageLayouter) state.getDataRow().get(PageableReportProcessor.LAYOUTMANAGER_NAME);
          if (org != lm)
          {
            // assertation check, the pagelayouter must not and should not change
            // during the processing.
            throw new IllegalStateException("Lost the layout manager");
          }
          // we detected an break ... undo the last event ..
          autoPageBreak = lm.isAutomaticPagebreak();
          checkInterrupted();
        }

        if (autoPageBreak)
        {
          // perform a rollback
          state = oldState.restorePageProgressCopy();
          // due to the cloning involved in the restore process, we have a new instance
          lm = (PageLayouter) state.getDataRow().get(PageableReportProcessor.LAYOUTMANAGER_NAME);
          lm.finishPageAfterRestore(state);
        }
//        else
//        {
//          // perform a commit ...
//
//        }
      }


      metaPage = lm.getMetaPage();
    }
    catch (CloneNotSupportedException cne)
    {
      throw new ReportProcessingException("Unable to create temporary state object.");
    }
    finally
    {
      // clear the logical page reference, so that no memleak is created...
      if (lm != null)
      {
        lm.clearLogicalPage();
      }
    }
    lastRecentlyUsedState =
    new WeakReference(new CacheInformation(currPage, state));
    return state;
  }

  private ReportState ensureReportInitialized (final ReportState state)
          throws ReportProcessingException
  {
    if (state instanceof StartState)
    {
      return state.advance();
    }
    return state;
  }

  public MetaPage getMetaPage ()
  {
    return metaPage;
  }

  public synchronized void clear ()
  {
    metaPage = null;
    lastRecentlyUsedState = null;
  }

  public OutputTarget getOutputTarget ()
  {
    return outputTarget;
  }

  public PageDefinition getPageDefinition ()
  {
    return pageDefinition;
  }
}
