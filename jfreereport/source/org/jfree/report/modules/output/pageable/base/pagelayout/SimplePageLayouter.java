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
 * -----------------------
 * SimplePageLayouter.java
 * -----------------------
 * (C)opyright 2002, 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: SimplePageLayouter.java,v 1.2 2003/07/14 20:16:05 taqua Exp $
 *
 * Changes
 * -------
 * 05-Dec-2002 : Updated Javadocs (DG);
 * 07-Dec-2002 : Removed manual-pagebreak flag, was a left-over. PageFinished
 *               did not query the DISPLAY_ON_FIRSTPAGE flag.
 * 12-Dec-2002 : Layouting Fix    I: Layouting failed for non-absolute band elements if no
 *               absolute positioned elements were defined.
 * 18-Dec-2002 : Layouting Fix  II: PageFooter on own page failed
 * 07-Jan-2003 : Layouting Fix III: Limited max band height to the available space of a single page
 * 08-Jan-2003 : Layouting Fix IIa: PageFooter fix fix
 * 27-Jan-2003 : BugFix: printing empty bands caused a commit spooled operations
 * 01-Feb-2003 : Layouting moved into BandLayoutManagerUtil (Common code)
 */

package org.jfree.report.modules.output.pageable.base.pagelayout;

import java.awt.geom.Rectangle2D;

import org.jfree.report.Band;
import org.jfree.report.Group;
import org.jfree.report.JFreeReportConstants;
import org.jfree.report.ReportProcessingException;
import org.jfree.report.modules.output.pageable.base.Spool;
import org.jfree.report.modules.output.pageable.base.LogicalPage;
import org.jfree.report.modules.output.pageable.base.OutputTargetException;
import org.jfree.report.event.PrepareEventListener;
import org.jfree.report.event.ReportEvent;
import org.jfree.report.function.Expression;
import org.jfree.report.function.FunctionProcessingException;
import org.jfree.report.layout.BandLayoutManagerUtil;
import org.jfree.report.states.ReportState;
import org.jfree.report.style.BandStyleSheet;

/**
 * A simple page layouter.  This class replicates the 'old' behaviour of JFreeReport,
 * simple and straightforward.
 * <p>
 * Layout Constraints used:
 * <ul>
 * <li>PageHeader, PageFooter: BandStyleSheet.DISPLAY_ON_FIRST_PAGE
 * <p>Defines whether a PageHeader or ~footer should be printed on the first page.</p>
 * <li>PageHeader, PageFooter: BandStyleSheet.DISPLAY_ON_LAST_PAGE
 * <p>Defines whether a PageHeader or ~footer should be printed on the last page.
 * A warning: For the PageHeader this works only if the ReportFooter has a pagebreak
 * before printing.
 * </p>
 * <li>GroupHeader: BandStyleSheet.REPEAT_HEADER
 * <p>Defines whether this GroupHeader should be repeated on every page as long as this
 * group is active</p>
 * <li>All Bands: BandStyleSheet.PAGEBREAK_BEFORE, BandStyleSheet.PAGEBREAK_AFTER
 * <p>defines whether to start a new page before the band is printed or after the
 * band is printed. This request is ignored, if the current page is empty (not counting
 * the PageHeader and the repeating GroupHeader).</p>
 * </ul>
 *
 * @author Thomas Morgner
 */
public class SimplePageLayouter extends PageLayouter implements PrepareEventListener
{
  /**
   * Represents the current state of the page layouter.
   */
  protected static class SimpleLayoutManagerState extends PageLayouter.LayoutManagerState
  {
    /** The band. */
    private Band band;

    /**
     * Creates a new state.  The band can be <code>null</code> if there is no band to be printed
     * on the next page.
     *
     * @param band  the band.
     */
    public SimpleLayoutManagerState(final Band band)
    {
      this.band = band;
    }

    /**
     * Returns the band.
     *
     * @return the band.
     */
    public Band getBand()
    {
      return band;
    }

    /**
     * Returns a string describing the object.
     *
     * @return The string.
     */
    public String toString()
    {
      return "State={" + band + "}";
    }
  }

  /** small carrier class to transport the maximum page number for this report. */
  private static class PageCarrier
  {
    /** stores the last page number of the report processing. */
    public int maxPages;
  }

  /** the page carrier for this pagelayouter contains the number of the last page. */
  private PageCarrier pageCarrier;

  /** A useful constant. */
  private static final boolean ENDPAGE_FORCED = true;

  /** A useful constant. */
  private static final boolean ENDPAGE_REQUESTED = false;

  /** A flag that indicates that the current pagebreak will be the last one. */
  private boolean isLastPageBreak;

  /**
   * A flag which indicates that a new page should be started before the next band
   * is printed. Bool-or this flag with PageBreakBefore ...
   */
  private boolean startNewPage;

  /** The cursor used by the layouter. */
  private SimplePageLayoutCursor cursor;

  /** The current state. */
  private SimpleLayoutManagerState state;

  /** The spool. */
  private Spool spooledBand;

  /** The current state for repeating group headers. */
  private int currentEffectiveGroupIndex;

  /**
   * Creates a new page layouter.
   */
  public SimplePageLayouter()
  {
    setName("Layout");
    currentEffectiveGroupIndex = -1;
    pageCarrier = new PageCarrier();
  }

  /**
   * Return a completly separated copy of this function. The copy does no
   * longer share any changeable objects with the original function.
   *
   * @return a copy of this function.
   */
  public Expression getInstance()
  {
    final SimplePageLayouter pl = (SimplePageLayouter) super.getInstance();
    pl.pageCarrier = new PageCarrier();
    return pl;
  }

  /**
   * Returns the highest pagenumber found during the repagination process.
   *
   * @return the highest page number.
   */
  public int getMaxPage()
  {
    return pageCarrier.maxPages;
  }

  /**
   * Defines the highest pagenumber found during the repagination process.
   *
   * @param maxPage the highest page number.
   */
  protected void setMaxPage(final int maxPage)
  {
    pageCarrier.maxPages = maxPage;
  }

  /**
   * Receives notification that the report has started. Also invokes the
   * start of the first page ...
   * <P>
   * Layout and draw the report header after the PageStartEvent was fired.
   *
   * @param event  the event.
   */
  public void reportStarted(final ReportEvent event)
  {
    // activating this state after the page has ended is invalid.
    if (isPageEnded())
    {
      throw new IllegalStateException();
    }
    setCurrentEvent(event);
    if (getCurrentEvent() == null)
    {
      throw new NullPointerException("getCurrentEvent() returned null");
    }
    try
    {
      currentEffectiveGroupIndex = -1;
      printBand(getReport().getReportHeader());
    }
    catch (FunctionProcessingException fe)
    {
      throw fe;
    }
    catch (Exception e)
    {
      throw new FunctionProcessingException("ReportStarted failed", e);
    }
    finally
    {
      clearCurrentEvent();
    }
  }

  /**
   * Receives notification that a group of item bands has been completed.
   * <P>
   * The itemBand is finished, the report starts to close open groups.
   *
   * @param event The event.
   */
  public void itemsFinished(final ReportEvent event)
  {
    // activating this state after the page has ended is invalid.
    if (isPageEnded())
    {
      throw new IllegalStateException();
    }
    currentEffectiveGroupIndex -= 1;
  }

  /**
   * Receives notification that report generation has completed, the report footer was printed,
   * no more output is done. This is a helper event to shut down the output service.
   *
   * @param event The event.
   */
  public void reportDone(final ReportEvent event)
  {
    // activating this state after the page has ended is invalid.
    if (isPageEnded())
    {
      throw new IllegalStateException();
    }
    try
    {
      setCurrentEvent(event);
      restartPage();
      createSaveState(null);
      saveCurrentState();

      endPage(ENDPAGE_FORCED);
    }
    catch (Exception e)
    {
      throw new FunctionProcessingException("ReportDone", e);
    }
    finally
    {
      clearCurrentEvent();
    }
  }

  /**
   * Receives notification that a group of item bands is about to be processed.
   * <P>
   * The next events will be itemsAdvanced events until the itemsFinished event is raised.
   *
   * @param event The event.
   */
  public void itemsStarted(final ReportEvent event)
  {
    // activating this state after the page has ended is invalid.
    if (isPageEnded())
    {
      throw new IllegalStateException();
    }
    currentEffectiveGroupIndex += 1;
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
    if (isPageEnded())
    {
      throw new IllegalStateException();
    }
    setCurrentEvent(event);
    try
    {
      // a new page has started, so reset the cursor ...
      setCursor(new SimplePageLayoutCursor(getLogicalPage().getHeight()));

      final Band b = getReport().getPageHeader();
      if (event.getState().getCurrentPage() == 1)
      {
        if (b.getStyle().getBooleanStyleProperty(BandStyleSheet.DISPLAY_ON_FIRSTPAGE) == true)
        {
          print(b, true);
        }
      }
      else if (event.getState().getCurrentPage() == getMaxPage())
      {
        if (b.getStyle().getBooleanStyleProperty(BandStyleSheet.DISPLAY_ON_LASTPAGE) == true)
        {
          print(b, true);
        }
      }
      else if (isLastPageBreak)
      {
        if (b.getStyle().getBooleanStyleProperty(BandStyleSheet.DISPLAY_ON_LASTPAGE) == true)
        {
          print(b, true);
        }
      }
      else
      {
        print(b, true);
      }

      /**
       * Repeating group header are only printed while ItemElements are
       * processed.
       */
      // was currentEffectiveGroupIndex - 1
      for (int gidx = 0; gidx < currentEffectiveGroupIndex; gidx++)
      {
        final Group g = getReport().getGroup(gidx);
        if (g.getHeader().getStyle().getBooleanStyleProperty(BandStyleSheet.REPEAT_HEADER))
        {
          print(g.getHeader(), true);
        }
      }

      // mark the current position to calculate the maxBand-Height
      getCursor().setPageTop(getCursor().getY());

      if (getLogicalPage().isEmpty() == false)
      {
        throw new IllegalStateException("Not empty after pagestart");
      }
    }
    catch (FunctionProcessingException fe)
    {
      throw fe;
    }
    catch (Exception e)
    {
      throw new FunctionProcessingException("PageStarted failed", e);
    }
    finally
    {
      clearCurrentEvent();
    }
  }

  /**
   * Receives notification that a page has ended.
   * <p>
   * This prints the PageFooter. If this is the first page, the footer is not
   * printed if the pagefooter style-flag DISPLAY_ON_FIRSTPAGE is set to false.
   * If this event is known to be the last pageFinished event, the DISPLAY_ON_LASTPAGE
   * is evaluated and the footer is printed only if this flag is set to TRUE.
   * <p>
   *
   * @param event  the report event.
   */
  public void pageFinished(final ReportEvent event)
  {
    setCurrentEvent(event);
    try
    {

      getCursor().setReservedSpace(0);
      final Band b = getReport().getPageFooter();
      if (event.getState().getCurrentPage() == 1)
      {
        if (b.getStyle().getBooleanStyleProperty(BandStyleSheet.DISPLAY_ON_FIRSTPAGE) == true)
        {
          printBottom(b);
        }
      }
      else if (isLastPageBreak)
      {
        if (b.getStyle().getBooleanStyleProperty(BandStyleSheet.DISPLAY_ON_LASTPAGE) == true)
        {
          printBottom(b);
        }
      }
      else
      {
        printBottom(b);
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
    finally
    {
      clearCurrentEvent();
    }
  }

  /**
   * Receives notification that the report has finished.
   * <P>
   * Prints the ReportFooter and forces the last pagebreak.
   *
   * @param event Information about the event.
   */
  public void reportFinished(final ReportEvent event)
  {
    // activating this state after the page has ended is invalid.
    if (isPageEnded())
    {
      throw new IllegalStateException();
    }
    try
    {
      setCurrentEvent(event);
      currentEffectiveGroupIndex -= 1;

      final Object prepareRun =
          event.getState().getProperty(JFreeReportConstants.REPORT_PREPARERUN_PROPERTY,
              Boolean.FALSE);
      if (prepareRun.equals(Boolean.TRUE))
      {
        setMaxPage(event.getState().getCurrentPage());
      }

      // force that this last pagebreak ...
      isLastPageBreak = true;

      final Band b = getReport().getReportFooter();
      printBand(b);
    }
    catch (FunctionProcessingException fe)
    {
      throw fe;
    }
    catch (Exception e)
    {
      throw new FunctionProcessingException("ReportFinished failed", e);
    }
    finally
    {
      clearCurrentEvent();
    }
  }

  /**
   * Receives notification that a group has started.
   * <P>
   * Prints the GroupHeader
   *
   * @param event Information about the event.
   */
  public void groupStarted(final ReportEvent event)
  {
    // activating this state after the page has ended is invalid.
    if (isPageEnded())
    {
      throw new IllegalStateException();
    }
    try
    {
      setCurrentEvent(event);
      currentEffectiveGroupIndex += 1;

      final int gidx = event.getState().getCurrentGroupIndex();
      final Group g = getReport().getGroup(gidx);
      final Band b = g.getHeader();
      printBand(b);
    }
    catch (FunctionProcessingException fe)
    {
      throw fe;
    }
    catch (Exception e)
    {
      throw new FunctionProcessingException("GroupStarted failed", e);
    }
    finally
    {
      clearCurrentEvent();
    }
  }

  /**
   * Receives notification that a group has finished.
   * <P>
   * Prints the GroupFooter.
   *
   * @param event Information about the event.
   */
  public void groupFinished(final ReportEvent event)
  {
    // activating this state after the page has ended is invalid.
    if (isPageEnded())
    {
      throw new IllegalStateException();
    }
    try
    {
      setCurrentEvent(event);
      currentEffectiveGroupIndex -= 1;

      final int gidx = event.getState().getCurrentGroupIndex();
      final Group g = getReport().getGroup(gidx);
      final Band b = g.getFooter();
      printBand(b);
    }
    catch (FunctionProcessingException fe)
    {
      throw fe;
    }
    catch (Exception e)
    {
      throw new FunctionProcessingException("GroupFinished failed", e);
    }
    finally
    {
      clearCurrentEvent();
    }
  }

  /**
   * Receives notification that a row of data is being processed.
   * <P>
   * prints the ItemBand.
   *
   * @param event Information about the event.
   */
  public void itemsAdvanced(final ReportEvent event)
  {
    // activating this state after the page has ended is invalid.
    if (isPageEnded())
    {
      throw new IllegalStateException();
    }

    try
    {
      setCurrentEvent(event);

      printBand(getReport().getItemBand());
    }
    catch (FunctionProcessingException fe)
    {
      throw fe;
    }
    catch (Exception e)
    {
      throw new FunctionProcessingException("ItemsAdvanced failed", e);
    }
    finally
    {
      clearCurrentEvent();
    }
  }

  /**
   * Prints a band.
   *
   * @param b  the band.
   *
   * @throws ReportProcessingException if the printing or spooling of the band failed.
   * @return true, if the band was printed, false if the printing was delayed to the next page
   */
  private boolean printBand(final Band b) throws ReportProcessingException
  {
    if (isPageEnded())
    {
      createSaveState(b);
      setStartNewPage(true);
      return false;
    }
    if (b.getStyle().getBooleanStyleProperty(BandStyleSheet.PAGEBREAK_BEFORE) == true)
    {
      createSaveState(b);

      if (endPage(ENDPAGE_REQUESTED) == false)
      {
        // no pagebreak was done, the band was printed to an already empty page
        return print(b, false);
      }
      // a pagebreak was requested, printing is delayed
      setStartNewPage(true);
      return false;
    }
    else
    {
      return print(b, false);
    }
  }

  /**
   * Prints a band.
   *
   * @param b  the band.
   * @param spool  a flag that controls whether or not to spool.
   * @return true, if the band was printed, and false if the printing is delayed
   * until a new page gets started.
   *
   * @throws ReportProcessingException if the printing failed
   */
  private boolean print(final Band b, final boolean spool)
      throws ReportProcessingException
  {
    final float y = getCursor().getY();
    // don't save the state if the current page is currently being finished
    // or restarted; PageHeader and PageFooter are printed out of order and
    // do not influence the reporting state

    final Rectangle2D bounds = doLayout(b, true);
    bounds.setRect(0, y, bounds.getWidth(), bounds.getHeight());
    final boolean retval = doPrint(bounds, b, spool);
    return retval;
  }

  /**
   * Prints a band at the bottom of the page.
   *
   * @param b  the band.
   * @return true, if the band was printed, and false if the printing is delayed
   * until a new page gets started.
   *
   * @throws ReportProcessingException if the printing failed
   */
  private boolean printBottom(final Band b)
      throws ReportProcessingException
  {
    // don't save the state if the current page is currently beeing finished
    // or restarted; PageHeader and PageFooter are printed out of order and
    // do not influence the reporting state

    // if there is nothing printed, then ignore everything ...
    final boolean spool = getLogicalPage().isEmpty();
    final Rectangle2D bounds = doLayout(b, true);
    bounds.setRect(0, getCursor().getPageBottomReserved() - bounds.getHeight(),
        bounds.getWidth(), bounds.getHeight());
    return doPrint(bounds, b, spool);
  }

  /**
   * Perform the layout of a band. The height of the band is calculated according to the contents
   * of the band.  The width of the band will always span the complete printable width.
   *
   * @param band  the band.
   * @param fireEvent  a flag to control whether or not a report event is fired.
   *
   * @return the dimensions of the band.
   */
  protected Rectangle2D doLayout(final Band band, final boolean fireEvent)
  {
    final float width = getLogicalPage().getWidth();
    final float height = getCursor().getPageBottomReserved() - getCursor().getPageTop();
    final Rectangle2D bounds = BandLayoutManagerUtil.doLayout(band,
        getLogicalPage().getOutputTarget(),
        width,
        height);
    if (fireEvent == true)
    {
      final ReportEvent event = getCurrentEvent();
      clearCurrentEvent();
      event.getState().fireLayoutCompleteEvent(band, event.getType());
      setCurrentEvent(event);
    }
    return bounds;
  }

  /**
   * Prints a band.
   *
   * @param bounds  the bounds of the band within the logical page
   * @param band  the band that should be printed. The internal band layouting is
   * already done, all Elements contain a valid BOUNDS property.
   * @param spool  a flag that controls whether to print the contents directly
   * or to cache the printing operation for later usage.
   * @return true, if the band was printed, and false if the printing is delayed
   * until a new page gets started.
   * @see LogicalPage#spoolBand
   *
   * @throws ReportProcessingException if the printing caused an detectable error
   * while printing the band
   */
  protected boolean doPrint(final Rectangle2D bounds, final Band band, final boolean spool)
      throws ReportProcessingException
  {
    try
    {
      final float height = (float) bounds.getHeight();
      // handle the end of the page
      if (isFinishingPage())
      {
        if (spool)
        {
          final Spool newSpool = getLogicalPage().spoolBand(bounds, band);
          if (spooledBand == null)
          {
            spooledBand = newSpool;
          }
          else
          {
            spooledBand.merge(newSpool);
          }
        }
        else
        {
          final Spool newSpool = getLogicalPage().spoolBand(bounds, band);
          if (newSpool.isEmpty() == false)
          {
            if (spooledBand != null)
            {
              getLogicalPage().replaySpool(spooledBand);
              spooledBand = null;
            }
/*
            PhysicalOperation [] pop = newSpool.getOperations();
            Log.debug ("--->" + band.getClass());
            for (int i = 0; i < pop.length; i++)
            {
              Log.debug (pop[i]);
            }
*/
            getLogicalPage().replaySpool(newSpool);
          }
        }
        cursor.advance(height);
        return true;
      }
      // handle a automatic pagebreak in case there is not enough space here ...
      else if ((isSpaceFor(height) == false) && (isPageEnded() == false))
      {
        if ((spooledBand != null) && (spool == false))
        {
          getLogicalPage().replaySpool(spooledBand);
          spooledBand = null;
        }

        createSaveState(band);
        endPage(ENDPAGE_FORCED);
        return false;
      }
      else if (isPageEnded())
      {
        // page has ended before, that band should be printed on the next page
        createSaveState(band);
        return false;
      }
      else
      {
        if (spool)
        {
          final Spool newSpool = getLogicalPage().spoolBand(bounds, band);
          if (spooledBand == null)
          {
            spooledBand = newSpool;
          }
          else
          {
            spooledBand.merge(newSpool);
          }

          cursor.advance(height);
          return true;
        }
        else
        {
          final Spool newSpool = getLogicalPage().spoolBand(bounds, band);
          if (newSpool.isEmpty() == false)
          {
            if (spooledBand != null)
            {
              getLogicalPage().replaySpool(spooledBand);
              spooledBand = null;
            }
/*
            PhysicalOperation [] pop = newSpool.getOperations();
            Log.debug ("--->" + band.getClass());
            for (int i = 0; i < pop.length; i++)
            {
              Log.debug (pop[i]);
            }
*/
            getLogicalPage().replaySpool(newSpool);
          }

          cursor.advance(height);
          if (band.getStyle().getBooleanStyleProperty(BandStyleSheet.PAGEBREAK_AFTER) == true)
          {
            createSaveState(null);
            endPage(ENDPAGE_REQUESTED);
          }
          return true;
        }
      }
    }
    catch (ReportProcessingException rpe)
    {
      throw rpe;
    }
    catch (OutputTargetException ote)
    {
      throw new FunctionProcessingException("Failed to print", ote);
    }
  }

  /**
   * Determines whether or not there is space remaining on the page for a band of the specified
   * height.  Perform layouting for the pageFooter to guess the height.
   *
   * @param height  the height (in Java2D user space units).
   *
   * @return true or false.
   */
  public boolean isSpaceFor(final float height)
  {
    if (isLastPageBreak && (getReport().getPageFooter().isDisplayOnLastPage() == false))
    {
      getCursor().setReservedSpace(0);
    }
    else
    {
      final Band b = getReport().getPageFooter();
      // perform layout, but do not fire the event, as we don't print the band ...
      final Rectangle2D rect = doLayout(b, false);
      getCursor().setReservedSpace((float) rect.getHeight());
    }
    return getCursor().isSpaceFor(height);
  }

  /**
   * Returns the cursor.
   *
   * @return the cursor.
   * @throws IllegalStateException if a cursor is requested but no OutputTarget is set.
   */
  protected SimplePageLayoutCursor getCursor()
  {
    if (cursor == null)
    {
      throw new IllegalStateException("No cursor, no OutputTarget: " + hashCode());
    }
    return cursor;
  }

  /**
   * Sets the cursor.
   *
   * @param cursor  the cursor (null not permitted).
   * @throws NullPointerException if the given cursor is null
   */
  protected void setCursor(final SimplePageLayoutCursor cursor)
  {
    if (cursor == null)
    {
      throw new NullPointerException("SimplePageLayouter.setCursor(...): cursor is null.");
    }
    this.cursor = cursor;
  }

  /**
   * Records state information.
   *
   * @param b  the band.
   */
  protected void createSaveState(final Band b)
  {
    state = new SimpleLayoutManagerState(b);
  }

  /**
   * Returns the current state. The state was previously recorded using the
   * <code>createSaveState(Band b)</code> method.
   *
   * @return the current state, never null
   */
  protected PageLayouter.LayoutManagerState saveCurrentState()
  {
    if (state == null)
    {
      throw new NullPointerException();
    }
    return state;
  }

  /**
   * Restores the state.
   *
   * @param anchestor  the ancestor state.
   *
   * @throws ReportProcessingException if the printing failed or a pagebreak is
   * requested while the page is restored.
   * @throws IllegalStateException if there is no SavedState but this is not the
   * first page.
   */
  public void restoreSaveState(final ReportState anchestor)
      throws ReportProcessingException
  {
    super.restoreSaveState(anchestor);
    isLastPageBreak = false;
  }

  /**
   * Handles the restarting of the page. Fires the pageStarted event and prints
   * the pageheader. Restarting the page is done once after the PageLayouterState
   * was restored.
   *
   * @throws ReportProcessingException if restarting the page failed.
   */
  public void restartPage() throws ReportProcessingException
  {
    if (isPageRestartDone() || isRestartingPage() || isFinishingPage())
    {
      return;
    }
    startPage();

    if (state == null)
    {
      // no state saved, break  ...
      return;
    }

    setRestartingPage(true);
    // if there was a pagebreak_after_print, there is no band to print for now
    if (state.getBand() != null)
    {
      try
      {
        final Band band = (Band) state.getBand().clone();
        // update the dataRow to the current dataRow instance...
        getCurrentEvent().getState().updateDataRow(band);
        print(band, false);
      }
      catch (CloneNotSupportedException cne)
      {
        throw new ReportProcessingException
            ("Unable to initialize the new page. Clone failed.", cne);
      }
    }
    clearSaveState();
    setRestartingPage(false);
  }

  /**
   * Clears the layout state.
   */
  protected void clearSaveState()
  {
    super.clearSaveState();
    state = null;
  }

  /**
   * Sets the logical page and adjust the cursor.
   *
   * @param logicalPage  the logical page.
   */
  public void setLogicalPage(final LogicalPage logicalPage)
  {
    super.setLogicalPage(logicalPage);
    setCursor(new SimplePageLayoutCursor(getLogicalPage().getHeight()));
  }

  /**
   * Ends the page.
   *
   * @param force set to true, to skip the test whether the logical page is empty and
   * to enforce an pagebreak. This is a requirement when an completly empty report
   * (no bands or elements printed) should be finished.
   *
   * @return true if the pageBreak is done, false otherwise.
   *
   * @throws ReportProcessingException if finishing the page failed.
   */
  protected boolean endPage(final boolean force) throws ReportProcessingException
  {
    if (getLogicalPage().isEmpty() == false || force)
    {
      if (spooledBand != null)
      {
        // getLogicalPage().replaySpool(spooledBand);
        // Log.warn ("Spool contained data, this data is lost now ...!");
        spooledBand = null;
      }
      super.endPage();
      return true;
    }
    else
    {
      return false;
    }
  }

  /**
   * Returns true, if the PageLayouter has successfully started a new page. The
   * start of the new page is delayed, until the first content is printed.
   *
   * @return true, if a new page has already been started, false otherwise.
   */
  public boolean isNewPageStarted()
  {
    return startNewPage;
  }

  /**
   * Defines whether the PageLayouter has successfully started a new page. The
   * start of the new page is delayed, until the first content is printed, this
   * flag is used to keep track of the page initialization state.
   *
   * @param startNewPage true, if a new page has already been started, false otherwise.
   */
  public void setStartNewPage(final boolean startNewPage)
  {
    this.startNewPage = startNewPage;
  }

  /**
   * Clones the layouter.
   *
   * @return the clone.
   *
   * @throws CloneNotSupportedException if there is a problem cloning.
   */
  public Object clone() throws CloneNotSupportedException
  {
    final SimplePageLayouter sl = (SimplePageLayouter) super.clone();
    if (spooledBand != null)
    {
      sl.spooledBand = (Spool) spooledBand.clone();
    }
    return sl;
  }

  /**
   * Receives notification of a prepare event.
   *
   * @param event  the event.
   */
  public void prepareEvent(final ReportEvent event)
  {
    try
    {
      setCurrentEvent(event);
      restartPage();
    }
    catch (Exception e)
    {
      throw new FunctionProcessingException("prepareEvent", e);
    }
    finally
    {
      clearCurrentEvent();
    }
  }
}
