/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
 *
 * (C) Copyright 2000-2003, by Simba Management Limited and Contributors.
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
 * SimplePageLayoutDelegate.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: SimplePageLayoutDelegate.java,v 1.1 2003/10/01 20:41:41 taqua Exp $
 *
 * Changes
 * -------------------------
 * 26.09.2003 : Initial version
 *
 */

package org.jfree.report.modules.output.support.pagelayout;

import java.io.Serializable;

import org.jfree.report.Band;
import org.jfree.report.Group;
import org.jfree.report.ReportDefinition;
import org.jfree.report.JFreeReport;
import org.jfree.report.event.PageEventListener;
import org.jfree.report.event.ReportEvent;
import org.jfree.report.event.ReportListener;
import org.jfree.report.function.FunctionProcessingException;
import org.jfree.report.style.BandStyleSheet;

/**
 * The simple page layout delegate encasulates all required tasks to perform
 * a plain top-to-bottom pagelayouting. It gets fed with report states and 
 * will perform all necessary steps to prepare the content generation. The
 * final printing is done in the simple page layout worker (in most cases
 * the report processor function).
 * 
 * @author Thomas Morgner
 */
public class SimplePageLayoutDelegate implements
    PageEventListener, ReportListener, Cloneable, Serializable
{
  /** The pagelayout worker which performs the final printing. */ 
  private SimplePageLayoutWorker worker;

  /** small carrier class to transport the maximum page number for this report. */
  private static class PageCarrier
  {
    /** stores the last page number of the report processing. */
    private int maxPages;
  }

  /** the page carrier for this pagelayouter contains the number of the last page. */
  private PageCarrier pageCarrier;

  /** A flag indicating whether the next pagebreak will be the last one. */
  private boolean lastPagebreak;
  /** The current group index (used to select the correct group header and footer).*/
  private int currentEffectiveGroupIndex;

  /**
   * DefaultConstructor. A worker needs to be assigned to use this delegate.
   *
   * @param worker the worker.
   */
  public SimplePageLayoutDelegate(SimplePageLayoutWorker worker)
  {
    pageCarrier = new PageCarrier();
    setWorker(worker);
  }


  /**
   * Creates and returns a copy of this object.
   *
   * @return     a clone of this instance.
   * @exception  CloneNotSupportedException  if the object's class does not
   *               support the <code>Cloneable</code> interface. Subclasses
   *               that override the <code>clone</code> method can also
   *               throw this exception to indicate that an instance cannot
   *               be cloned.
   * @exception  OutOfMemoryError            if there is not enough memory.
   * @see        Cloneable
   */
  public Object clone() throws CloneNotSupportedException
  {
    return super.clone();
  }

  /**
   * Returns the simple page layout worker for this delegate.
   * 
   * @return the worker.
   */
  public SimplePageLayoutWorker getWorker()
  {
    return worker;
  }

  /**
   * Defines the simple page layout worker for this delegate.
   * 
   * @param worker the worker.
   * @throws NullPointerException if the given worker is null.
   */
  public void setWorker(SimplePageLayoutWorker worker)
  {
    if (worker == null)
    {
      throw new NullPointerException("The given worker is null.");
    }
    this.worker = worker;
  }

  /**
   * Defines the currently effective group index. This index is used for
   * the repeating group headers feature.
   * 
   * @return the current group index.
   */
  protected int getCurrentEffectiveGroupIndex()
  {
    return currentEffectiveGroupIndex;
  }

  /**
   * Defines the currently effective group index. 
   * 
   * @param currentEffectiveGroupIndex the current group index.
   */
  protected void setCurrentEffectiveGroupIndex(int currentEffectiveGroupIndex)
  {
    this.currentEffectiveGroupIndex = currentEffectiveGroupIndex;
  }

  /**
   * Checks, whether the next pagebreak will be the last one.
   * After that pagebreak, the report processing is completed.
   * 
   * @return true, if the last pagebreak has been reached, 
   * false otherwise
   */
  protected boolean isLastPagebreak()
  {
    return lastPagebreak;
  }

  /**
   * Defines, whether the next pagebreak will be the last one.
   * After that pagebreak, the report processing is completed.
   * 
   * @param lastPagebreak set to true, if the last pagebreak has been reached, 
   * false otherwise
   */
  protected void setLastPagebreak(boolean lastPagebreak)
  {
    this.lastPagebreak = lastPagebreak;
  }

  /**
   * Returns the number of pages in the report (as currently known).
   * This property is filled during the pagination process and is later
   * used to support the surpression of the pageheader/footer on the last page. 
   * 
   * @return the number of pages in the report.
   */
  protected int getMaxPage()
  {
    return pageCarrier.maxPages;
  }

  /**
   * Defines the number of pages in the report (as currently known).
   * This property is filled during the pagination process and is later
   * used to support the surpression of the pageheader/footer on the last page. 
   * 
   * @param maxPage the number of pages in the report.
   */
  protected void setMaxPage(int maxPage)
  {
    this.pageCarrier.maxPages = maxPage;
  }

  /**
   * Receives notification that a page has started.
   * <P>
   * This prints the PageHeader. If this is the first page, the header is not
   * printed if the pageheader style-flag DISPLAY_ON_FIRSTPAGE is set to false.
   * If this event is known to be the last pageStarted event, the DISPLAY_ON_LASTPAGE
   * is evaluated and the header is printed only if this flag is set to TRUE.
   * <p>
   * If there is an active repeating GroupHeader, print the last one. The GroupHeader
   * is searched for the current group and all parent groups, starting at the
   * current group and ascending to the parents. The first goupheader that has the
   * StyleFlag REPEAT_HEADER set to TRUE is printed.
   * <p>
   * The PageHeader and the repeating GroupHeader are spooled until the first real
   * content is printed. This way, the LogicalPage remains empty until an other band
   * is printed.
   *
   * @param event Information about the event.
   */
  public void pageStarted(final ReportEvent event)
  {
    // activating this state after the page has ended is invalid.
    if (worker.isPageEnded())
    {
      throw new IllegalStateException();
    }
    try
    {
      // a new page has started, so reset the cursor ...
      // setCursor(new SimplePageLayoutCursor(getLogicalPage().getHeight()));
      worker.resetCursor();

      final ReportDefinition report = event.getReport();
      final Band b = report.getPageHeader();
      if (event.getState().getCurrentPage() == 1)
      {
        if (b.getStyle().getBooleanStyleProperty(BandStyleSheet.DISPLAY_ON_FIRSTPAGE) == true)
        {
          worker.print(b, SimplePageLayoutWorker.BAND_SPOOLED, 
              SimplePageLayoutWorker.PAGEBREAK_BEFORE_IGNORED);
        }
      }
      else if (event.getState().getCurrentPage() == getMaxPage())
      {
        if (b.getStyle().getBooleanStyleProperty(BandStyleSheet.DISPLAY_ON_LASTPAGE) == true)
        {
          worker.print(b, SimplePageLayoutWorker.BAND_SPOOLED, 
              SimplePageLayoutWorker.PAGEBREAK_BEFORE_IGNORED);
        }
      }
      else if (isLastPagebreak())
      {
        if (b.getStyle().getBooleanStyleProperty(BandStyleSheet.DISPLAY_ON_LASTPAGE) == true)
        {
          worker.print(b, SimplePageLayoutWorker.BAND_SPOOLED, 
              SimplePageLayoutWorker.PAGEBREAK_BEFORE_IGNORED);
        }
      }
      else
      {
        worker.print(b, SimplePageLayoutWorker.BAND_SPOOLED, 
            SimplePageLayoutWorker.PAGEBREAK_BEFORE_IGNORED);
      }

      /**
       * Repeating group header are only printed while ItemElements are
       * processed.
       */
      // was currentEffectiveGroupIndex - 1
      for (int gidx = 0; gidx < getCurrentEffectiveGroupIndex(); gidx++)
      {
        final Group g = report.getGroup(gidx);
        if (g.getHeader().getStyle().getBooleanStyleProperty(BandStyleSheet.REPEAT_HEADER))
        {
          worker.print(g.getHeader(), SimplePageLayoutWorker.BAND_SPOOLED, 
            SimplePageLayoutWorker.PAGEBREAK_BEFORE_IGNORED);
        }
      }

      // mark the current position to calculate the maxBand-Height
      worker.setTopPageContentPosition(worker.getCursorPosition());
    }
    catch (FunctionProcessingException fe)
    {
      throw fe;
    }
    catch (Exception e)
    {
      throw new FunctionProcessingException("PageStarted failed", e);
    }
  }

  /**
   * Receives notification that a page is completed.
   *
   * @param event The event.
   */
  public void pageFinished(ReportEvent event)
  {
    try
    {

      worker.setReservedSpace(0);
      final Band b = event.getReport().getPageFooter();
      if (event.getState().getCurrentPage() == 1)
      {
        if (b.getStyle().getBooleanStyleProperty(BandStyleSheet.DISPLAY_ON_FIRSTPAGE) == true)
        {
          worker.printBottom(b);
        }
      }
      else if (isLastPagebreak())
      {
        if (b.getStyle().getBooleanStyleProperty(BandStyleSheet.DISPLAY_ON_LASTPAGE) == true)
        {
          worker.printBottom(b);
        }
      }
      else
      {
        worker.printBottom(b);
      }
    }
    catch (FunctionProcessingException fe)
    {
      throw fe;
    }
    catch (Exception e)
    {
      throw new FunctionProcessingException("PageFinished failed", e);
    }
  }

  /**
   * Receives notification that a page was canceled by the ReportProcessor.
   * This method is called, when a page was removed from the report after
   * it was generated.
   *
   * @param event The event.
   */
  public void pageCanceled(ReportEvent event)
  {
    // this method is left empty, we dont handle canceled pages.
  }

  /**
   * Receives notification that report generation initializes the current run.
   * <P>
   * The event carries a ReportState.Started state.  Use this to initialize the report.
   *
   * @param event The event.
   */
  public void reportInitialized(ReportEvent event)
  {
    // we don't handle the report initialized event.
  }

  /**
   * Receives notification that report generation has started.
   * <P>
   * The event carries a ReportState.Started state.
   * Use this to prepare the report header.
   *
   * @param event The event.
   */
  public void reportStarted(ReportEvent event)
  {
    // activating this state after the page has ended is invalid.
    if (worker.isPageEnded())
    {
      throw new IllegalStateException("Assertation: ReportStarted cannot have ended page");
    }
    try
    {
      currentEffectiveGroupIndex = -1;
      worker.print(event.getReport().getReportHeader(), 
        SimplePageLayoutWorker.BAND_PRINTED, SimplePageLayoutWorker.PAGEBREAK_BEFORE_HANDLED);
    }
    catch (FunctionProcessingException fe)
    {
      throw fe;
    }
    catch (Exception e)
    {
      throw new FunctionProcessingException("ReportStarted failed", e);
    }
  }

  /**
   * Receives notification that report generation has finished (the last record is read and all
   * groups are closed).
   *
   * @param event The event.
   */
  public void reportFinished(ReportEvent event)
  {
    // activating this state after the page has ended is invalid.
    if (worker.isPageEnded())
    {
      throw new IllegalStateException("AssertationFailed: Page is closed.");
    }
    try
    {
      currentEffectiveGroupIndex -= 1;

      final Object prepareRun =
          event.getState().getProperty(JFreeReport.REPORT_PREPARERUN_PROPERTY,
              Boolean.FALSE);
      if (prepareRun.equals(Boolean.TRUE))
      {
        setMaxPage(event.getState().getCurrentPage());
      }

      // force that this last pagebreak ...
      setLastPagebreak(true);

      final Band b = event.getReport().getReportFooter();
      worker.print(b, SimplePageLayoutWorker.BAND_PRINTED, 
        SimplePageLayoutWorker.PAGEBREAK_BEFORE_HANDLED);
    }
    catch (FunctionProcessingException fe)
    {
      throw fe;
    }
    catch (Exception e)
    {
      throw new FunctionProcessingException("ReportFinished failed", e);
    }
  }

  /**
   * Receives notification that report generation has completed, the report footer was printed,
   * no more output is done. This is a helper event to shut down the output service.
   *
   * @param event The event.
   */
  public void reportDone(ReportEvent event)
  {
    // this event is not handled by this implementation.
    // the pagelayouter must make sure, that the report footer is really printed.
  }

  /**
   * Receives notification that a new group has started.
   * <P>
   * The group can be determined by the report state's getCurrentGroup() function.
   *
   * @param event The event.
   */
  public void groupStarted(ReportEvent event)
  {
    // activating this state after the page has ended is invalid.
    if (worker.isPageEnded())
    {
      throw new IllegalStateException("AssertationFailed: Page is closed.");
    }
    try
    {
      currentEffectiveGroupIndex += 1;

      final int gidx = event.getState().getCurrentGroupIndex();
      final Group g = event.getReport().getGroup(gidx);
      final Band b = g.getHeader();
      worker.print(b, SimplePageLayoutWorker.BAND_PRINTED, 
        SimplePageLayoutWorker.PAGEBREAK_BEFORE_HANDLED);
    }
    catch (FunctionProcessingException fe)
    {
      throw fe;
    }
    catch (Exception e)
    {
      throw new FunctionProcessingException("GroupStarted failed", e);
    }
  }

  /**
   * Receives notification that a group is finished.
   * <P>
   * The group can be determined by the report state's getCurrentGroup() function.
   *
   * @param event The event.
   */
  public void groupFinished(ReportEvent event)
  {
    // activating this state after the page has ended is invalid.
    if (worker.isPageEnded())
    {
      throw new IllegalStateException("AssertationFailed: Page is closed.");
    }
    try
    {
      currentEffectiveGroupIndex -= 1;

      final int gidx = event.getState().getCurrentGroupIndex();
      final Group g = event.getReport().getGroup(gidx);
      final Band b = g.getFooter();
      worker.print(b, SimplePageLayoutWorker.BAND_PRINTED, 
        SimplePageLayoutWorker.PAGEBREAK_BEFORE_HANDLED);
    }
    catch (FunctionProcessingException fe)
    {
      throw fe;
    }
    catch (Exception e)
    {
      throw new FunctionProcessingException("GroupFinished failed", e);
    }
  }

  /**
   * Receives notification that a group of item bands is about to be processed.
   * <P>
   * The next events will be itemsAdvanced events until the itemsFinished event is raised.
   *
   * @param event The event.
   */
  public void itemsStarted(ReportEvent event)
  {
    // activating this state after the page has ended is invalid.
    if (worker.isPageEnded())
    {
      throw new IllegalStateException("AssertationFailed: Page is closed.");
    }
    currentEffectiveGroupIndex += 1;
  }

  /**
   * Receives notification that a group of item bands has been completed.
   * <P>
   * The itemBand is finished, the report starts to close open groups.
   *
   * @param event The event.
   */
  public void itemsFinished(ReportEvent event)
  {
    // activating this state after the page has ended is invalid.
    if (worker.isPageEnded())
    {
      throw new IllegalStateException("AssertationFailed: Page is closed.");
    }
    currentEffectiveGroupIndex -= 1;
  }

  /**
   * Receives notification that a new row has been read.
   * <P>
   * This event is raised before an ItemBand is printed.
   *
   * @param event The event.
   */
  public void itemsAdvanced(ReportEvent event)
  {
    // activating this state after the page has ended is invalid.
    if (worker.isPageEnded())
    {
      throw new IllegalStateException("AssertationFailed: Page is closed.");
    }

    try
    {
      worker.print(event.getReport().getItemBand(), 
        SimplePageLayoutWorker.BAND_PRINTED, SimplePageLayoutWorker.PAGEBREAK_BEFORE_HANDLED);
    }
    catch (FunctionProcessingException fe)
    {
      throw fe;
    }
    catch (Exception e)
    {
      throw new FunctionProcessingException("ItemsAdvanced failed", e);
    }
  }
}
