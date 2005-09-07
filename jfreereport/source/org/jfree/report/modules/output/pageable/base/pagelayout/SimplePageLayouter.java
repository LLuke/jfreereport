/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2003, by Object Refinery Limited and Contributors.
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
 * Contributor(s):   David Gilbert (for Object Refinery Limited);
 *
 * $Id: SimplePageLayouter.java,v 1.28 2005/08/12 12:09:42 taqua Exp $
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

import org.jfree.report.Band;
import org.jfree.report.Group;
import org.jfree.report.ReportDefinition;
import org.jfree.report.ReportProcessingException;
import org.jfree.report.content.ContentCreationException;
import org.jfree.report.event.PrepareEventListener;
import org.jfree.report.event.ReportEvent;
import org.jfree.report.function.Expression;
import org.jfree.report.function.FunctionProcessingException;
import org.jfree.report.layout.AbstractBandLayoutManager;
import org.jfree.report.layout.BandLayoutManagerUtil;
import org.jfree.report.modules.output.meta.MetaBand;
import org.jfree.report.modules.output.pageable.base.LogicalPage;
import org.jfree.report.modules.output.pageable.base.operations.AligningMetaBandProducer;
import org.jfree.report.modules.output.support.pagelayout.SimplePageLayoutDelegate;
import org.jfree.report.modules.output.support.pagelayout.SimplePageLayoutWorker;
import org.jfree.report.states.ReportState;
import org.jfree.report.style.BandStyleKeys;
import org.jfree.report.util.geom.StrictBounds;
import org.jfree.report.util.geom.StrictGeomUtility;

/**
 * A simple page layouter.  This class replicates the 'old' behaviour of JFreeReport,
 * simple and straightforward.
 * <p/>
 * Layout Constraints used: <ul> <li>PageHeader, PageFooter: BandStyleSheet.DISPLAY_ON_FIRST_PAGE
 * <p>Defines whether a PageHeader or ~footer should be printed on the first page.</p>
 * <li>PageHeader, PageFooter: BandStyleSheet.DISPLAY_ON_LAST_PAGE <p>Defines whether a
 * PageHeader or ~footer should be printed on the last page. A warning: For the PageHeader
 * this works only if the ReportFooter has a pagebreak before printing. </p>
 * <li>GroupHeader: BandStyleSheet.REPEAT_HEADER <p>Defines whether this GroupHeader
 * should be repeated on every page as long as this group is active</p> <li>All Bands:
 * BandStyleSheet.PAGEBREAK_BEFORE, BandStyleSheet.PAGEBREAK_AFTER <p>defines whether to
 * start a new page before the band is printed or after the band is printed. This request
 * is ignored, if the current page is empty (not counting the PageHeader and the repeating
 * GroupHeader).</p> </ul>
 *
 * @author Thomas Morgner
 */
public strictfp class SimplePageLayouter extends PageLayouter
        implements PrepareEventListener, SimplePageLayoutWorker
{
  /**
   * Represents the current state of the page layouter.
   */
  protected static class SimpleLayoutManagerState
          extends PageLayouter.LayoutManagerState
  {
    /**
     * The band.
     */
    private final Object bandID;

    /**
     * Creates a new state.  The band can be <code>null</code> if there is no band to be
     * printed on the next page.
     *
     * @param bandID the band.
     */
    public SimpleLayoutManagerState (final Object bandID)
    {
      this.bandID = bandID;
    }

    /**
     * Returns the band.
     *
     * @return the band.
     */
    public Object getBandID ()
    {
      return bandID;
    }

    /**
     * Returns a string describing the object.
     *
     * @return The string.
     */
    public String toString ()
    {
      return "State={" + bandID + "}";
    }
  }

  public static final String WATERMARK_PRINTED_KEY =
          "org.jfree.report.modules.pageable.base.WatermarkPrinted";

  private static final long POSITION_UNDEFINED = -1;
  /**
   * A flag value indicating that the current page should be finished, regardless of the
   * current state or fill level.
   */
  private static final boolean ENDPAGE_FORCED = true;

  /**
   * A flag value indicating, that the current page should be finished, if it contains
   * printed content. Spooled content is not considered to be printed.
   */
  private static final boolean ENDPAGE_REQUESTED = false;

  /**
   * A flag that indicates that the current pagebreak will be the last one.
   */
  private boolean isLastPageBreak;

  /**
   * A flag which indicates that a new page should be started before the next band is
   * printed. Bool-or this flag with PageBreakBefore ...
   */
  private boolean startNewPage;

  /**
   * The cursor used by the layouter.
   */
  private SimplePageLayoutCursor cursor;

  /**
   * The current state.
   */
  private SimpleLayoutManagerState state;
  /**
   * The delegate which is used to perform the layouting.
   */
  private SimplePageLayoutDelegate delegate;

  /**
   * Creates a new page layouter.
   */
  public SimplePageLayouter ()
  {
    setName("Layout");
    delegate = new SimplePageLayoutDelegate(this);
  }

  /**
   * Reinitialize the cursor of the layout worker. Called when a new page is started.
   */
  public void resetCursor ()
  {
    final LogicalPage lp = getLogicalPage();
    final long internalVerticalAlignmentBorder =
            lp.getLayoutSupport().getInternalVerticalAlignmentBorder();
    setCursor(new SimplePageLayoutCursor
            (AbstractBandLayoutManager.alignDown
            (StrictGeomUtility.toInternalValue(lp.getHeight()),
                    internalVerticalAlignmentBorder)));
  }

  /**
   * Returns the current position of the cursor.
   *
   * @return the cursor position.
   */
  public long getCursorPosition ()
  {
    if (getCursor() == null)
    {
      throw new IllegalStateException("Cursor is not initialized.");
    }
    return getCursor().getY();
  }

  /**
   * Defines the reserved space on the current page.
   *
   * @param reserved the reserved space.
   */
  public void setReservedSpace (final long reserved)
  {
    if (getCursor() == null)
    {
      throw new IllegalStateException("Cursor is not initialized.");
    }
    getCursor().setReservedSpace(reserved);
  }

  /**
   * Returns the reserved space on the current page.
   *
   * @return the reserved space.
   */
  public long getReservedSpace ()
  {
    if (getCursor() == null)
    {
      throw new IllegalStateException("Cursor is not initialized.");
    }
    return getCursor().getReservedSpace();
  }

  /**
   * Marks the position of the first content after the page header. This can be used to
   * limit the maximum size of bands so that they do not exceed the available page space.
   * <p/>
   * This feature will be obsolete when bands can span multiple pages.
   *
   * @param topContentPosition the first content position.
   */
  public void setTopPageContentPosition (final long topContentPosition)
  {
    if (getCursor() == null)
    {
      throw new IllegalStateException("Cursor is not initialized.");
    }
    getCursor().setPageTop(topContentPosition);
  }

  /**
   * Returns the position of the first content.
   *
   * @return the first content position.
   */
  public long getTopContentPosition ()
  {
    if (getCursor() == null)
    {
      throw new IllegalStateException("Cursor is not initialized.");
    }
    return getCursor().getPageTop();
  }

  /**
   * Checks, whether the current page is empty.
   *
   * @return true, if the current page is empty, false otherwise.
   */
  public boolean isPageEmpty ()
  {
    return isGeneratedPageEmpty();
  }

  /**
   * Receives notification that the report has started. Also invokes the start of the
   * first page ... <P> Layout and draw the report header after the PageStartEvent was
   * fired.
   *
   * @param event the event.
   */
  public void reportStarted (final ReportEvent event)
  {
    // activating this state after the page has ended is invalid.
    if (isPageEnded())
    {
      throw new IllegalStateException();
    }
    setCurrentEvent(event);
    try
    {
      delegate.reportStarted(event);
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
   * Receives notification that a group of item bands has been completed. <P> The itemBand
   * is finished, the report starts to close open groups.
   *
   * @param event The event.
   */
  public void itemsFinished (final ReportEvent event)
  {
    setCurrentEvent(event);
    delegate.itemsFinished(event);
    clearCurrentEvent();
  }

  /**
   * Receives notification that report generation has completed, the report footer was
   * printed, no more output is done. This is a helper event to shut down the output
   * service.
   *
   * @param event The event.
   */
  public void reportDone (final ReportEvent event)
  {
    // activating this state after the page has ended is invalid.
    if (isPageEnded())
    {
      throw new IllegalStateException("AssertationFailed: Page ended.");
    }
    try
    {
      setCurrentEvent(event);
      restartPage(event.getState());
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
   * Receives notification that a group of item bands is about to be processed. <P> The
   * next events will be itemsAdvanced events until the itemsFinished event is raised.
   *
   * @param event The event.
   */
  public void itemsStarted (final ReportEvent event)
  {
    setCurrentEvent(event);
    delegate.itemsStarted(event);
    clearCurrentEvent();
  }

  /**
   * Receives notification that a page has started. <P> This prints the PageHeader. If
   * this is the first page, the header is not printed if the pageheader style-flag
   * DISPLAY_ON_FIRSTPAGE is set to false. If this event is known to be the last
   * pageStarted event, the DISPLAY_ON_LASTPAGE is evaluated and the header is printed
   * only if this flag is set to TRUE.
   * <p/>
   * If there is an active repeating GroupHeader, print the last one. The GroupHeader is
   * searched for the current group and all parent groups, starting at the current group
   * and ascending to the parents. The first goupheader that has the StyleFlag
   * REPEAT_HEADER set to TRUE is printed.
   * <p/>
   * The PageHeader and the repeating GroupHeader are spooled until the first real content
   * is printed. This way, the LogicalPage remains empty until an other band is printed.
   *
   * @param event Information about the event.
   */
  public void pageStarted (final ReportEvent event)
  {
    // activating this state after the page has ended is invalid.
    if (isPageEnded())
    {
      throw new IllegalStateException();
    }
    setCurrentEvent(event);
    try
    {
      delegate.pageStarted(event);
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
   * <p/>
   * This prints the PageFooter. If this is the first page, the footer is not printed if
   * the pagefooter style-flag DISPLAY_ON_FIRSTPAGE is set to false. If this event is
   * known to be the last pageFinished event, the DISPLAY_ON_LASTPAGE is evaluated and the
   * footer is printed only if this flag is set to TRUE.
   * <p/>
   *
   * @param event the report event.
   */
  public void pageFinished (final ReportEvent event)
  {
    setCurrentEvent(event);
    try
    {
      delegate.pageFinished(event);
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
   * Receives notification that the report has finished. <P> Prints the ReportFooter and
   * forces the last pagebreak.
   *
   * @param event Information about the event.
   */
  public void reportFinished (final ReportEvent event)
  {
    // activating this state after the page has ended is invalid.
    if (isPageEnded())
    {
      throw new IllegalStateException();
    }

    setCurrentEvent(event);
    try
    {
      delegate.reportFinished(event);
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
   * Receives notification that a group has started. <P> Prints the GroupHeader
   *
   * @param event Information about the event.
   */
  public void groupStarted (final ReportEvent event)
  {
    // activating this state after the page has ended is invalid.
    if (isPageEnded())
    {
      throw new IllegalStateException();
    }
    setCurrentEvent(event);
    try
    {
      delegate.groupStarted(event);
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
   * Receives notification that a group has finished. <P> Prints the GroupFooter.
   *
   * @param event Information about the event.
   */
  public void groupFinished (final ReportEvent event)
  {
    // activating this state after the page has ended is invalid.
    if (isPageEnded())
    {
      throw new IllegalStateException("AssertationFailed: Page is closed.");
    }
    setCurrentEvent(event);
    try
    {
      delegate.groupFinished(event);
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
   * Receives notification that a row of data is being processed. <P> prints the
   * ItemBand.
   *
   * @param event Information about the event.
   */
  public void itemsAdvanced (final ReportEvent event)
  {
    // activating this state after the page has ended is invalid.
    if (isPageEnded())
    {
      throw new IllegalStateException();
    }

    setCurrentEvent(event);
    try
    {
      delegate.itemsAdvanced(event);
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
   * @param b               the band.
   * @param spool           a flag that controls whether or not to spool.
   * @param handlePagebreak a flag that controls whether to handle the 'pagebreak-before
   *                        constraint.
   * @return true, if the band was printed, and false if the printing is delayed until a
   *         new page gets started.
   *
   * @throws ReportProcessingException if the printing failed
   */
  public boolean print (final Band b, final boolean spool, final boolean handlePagebreak)
          throws ReportProcessingException
  {
    // create a safe state, if the page ended. This does not print the
    // band. we just return.
    if (isPageEnded())
    {
      createSaveState(b);
      setStartNewPage(true);
      return false;
    }

    final Float f = (Float)
            b.getStyle().getStyleProperty(BandStyleKeys.FIXED_POSITION);
    final long position;
    if (f == null)
    {
      position = POSITION_UNDEFINED;
    }
    else
    {
      position = StrictGeomUtility.toInternalValue(f.floatValue());
      if ((position >= getCursor().getPageBottom()) || (position < 0))
      {
        throw new IndexOutOfBoundsException("Given fixed position is invalid");
      }
    }

    if ((handlePagebreak &&
            b.getStyle().getBooleanStyleProperty(BandStyleKeys.PAGEBREAK_BEFORE) == true) ||
            ((position != -1) && (position < getCursorPosition())))
    {
      // don't save the state if the current page is currently being finished
      // or restarted; PageHeader and PageFooter are printed out of order and
      // do not influence the reporting state

      // out-of-order prints do not accept pagebreaks, so we can be sure that
      // this is no pageheader or page footer or a band from there.
      createSaveState(b);

      if (endPage(ENDPAGE_REQUESTED) == true)
      {
        // a pagebreak was requested and granted, printing is delayed
        setStartNewPage(true);
        return false;
      }
    }

    final long y;
    if (position == POSITION_UNDEFINED)
    {
      y = getCursor().getY();
    }
    else
    {
      y = position;
    }
    // don't save the state if the current page is currently being finished
    // or restarted; PageHeader and PageFooter are printed out of order and
    // do not influence the reporting state

    final StrictBounds bounds = doLayout(b, true);
    bounds.setRect(0, y, bounds.getWidth(), bounds.getHeight());
    final boolean retval = doPrint(bounds, b, spool, false, position);
    return retval;
  }

  /**
   * Prints a band at the bottom of the page.
   *
   * @param b the band.
   * @return true, if the band was printed, and false if the printing is delayed until a
   *         new page gets started.
   *
   * @throws ReportProcessingException if the printing failed
   */
  public boolean printBottom (final Band b)
          throws ReportProcessingException
  {
    // don't save the state if the current page is currently beeing finished
    // or restarted; PageHeader and PageFooter are printed out of order and
    // do not influence the reporting state

    // if there is nothing printed, then ignore everything ...
    // the spooling is now slightly different ...
    final boolean spool = true;
    final StrictBounds bounds = doLayout(b, true);
    bounds.setRect(0, getCursor().getPageBottomReserved() - bounds.getHeight(),
            bounds.getWidth(), bounds.getHeight());
    return doPrint(bounds, b, spool, false, -1);
  }

  /**
   * Perform the layout of a band. The height of the band is calculated according to the
   * contents of the band.  The width of the band will always span the complete printable
   * width.
   *
   * @param band      the band.
   * @param fireEvent a flag to control whether or not a report event is fired.
   * @return the dimensions of the band.
   */
  protected StrictBounds doLayout (final Band band, final boolean fireEvent)
  {
    final long width = StrictGeomUtility.toInternalValue(getLogicalPage().getWidth());
    final long height = getCursor().getPageBottomReserved() - getCursor().getPageTop();
    final StrictBounds bounds = BandLayoutManagerUtil.doLayout(band,
            getLogicalPage().getLayoutSupport(), width, height);

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
   * @param bounds the bounds of the band within the logical page
   * @param band   the band that should be printed. The internal band layouting is already
   *               done, all Elements contain a valid BOUNDS property.
   * @param spool  a flag that controls whether to print the contents directly or to cache
   *               the printing operation for later usage.
   * @return true, if the band was printed, and false if the printing is delayed until a
   *         new page gets started.
   *
   * @throws ReportProcessingException if the printing caused an detectable error while
   *                                   printing the band
   */
  protected boolean doPrint (final StrictBounds bounds, final Band band,
                             final boolean spool, final boolean watermark,
                             final long position)
          throws ReportProcessingException
  {
    try
    {
      final long height = bounds.getHeight();
      // handle the end of the page
      if (isFinishingPage())
      {
        final ReportEvent event = getCurrentEvent();
        clearCurrentEvent();
        event.getState().fireOutputCompleteEvent(band, event.getType());
        setCurrentEvent(event);

        final AligningMetaBandProducer producer = new AligningMetaBandProducer
                (getLogicalPage().getLayoutSupport());
        final MetaBand rootBand = producer.createBand(band, spool);
        if (rootBand != null)
        {
          //Log.error(rootBand.getBounds());
          addRootMetaBand(rootBand);
        }
        cursor.advance(height);
        return true;
      }
      // handle a automatic pagebreak in case there is not enough space here ...
      else if ((watermark == false) && (isPageEnded() == false) && (isSpaceFor(height) == false))
      {
        setAutomaticPagebreak(true);
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
        final ReportEvent event = getCurrentEvent();
        clearCurrentEvent();
        event.getState().fireOutputCompleteEvent(band, event.getType());
        setCurrentEvent(event);

        final AligningMetaBandProducer producer = new AligningMetaBandProducer
                (getLogicalPage().getLayoutSupport());
        final MetaBand metaBand = producer.createBand(band, spool);
        if (metaBand != null)
        {
          addRootMetaBand(metaBand);
        }
        cursor.advance(height);
        if (band.getStyle().getBooleanStyleProperty(BandStyleKeys.PAGEBREAK_AFTER) == true)
        {
          createSaveState(null);
          endPage(ENDPAGE_REQUESTED);
        }
        return true;
      }
    }
    catch (ReportProcessingException rpe)
    {
      throw rpe;
    }
    catch (ContentCreationException ote)
    {
      throw new FunctionProcessingException("Failed to print", ote);
    }
  }

  /**
   * Determines whether or not there is space remaining on the page for a band of the
   * specified height.  Perform layouting for the pageFooter to guess the height.
   *
   * @param height the height (in Java2D user space units).
   * @return true or false.
   */
  public boolean isSpaceFor (final long height)
  {
    if (isLastPageBreak && (getReport().getPageFooter().isDisplayOnLastPage() == false))
    {
      getCursor().setReservedSpace(0);
    }
    else
    {
      final Band b = getReport().getPageFooter();
      // perform layout, but do not fire the event, as we don't print the band ...
      final StrictBounds rect = doLayout(b, false);
      getCursor().setReservedSpace(rect.getHeight());
    }
    return getCursor().isSpaceFor(height);
  }

  /**
   * Returns the cursor.
   *
   * @return the cursor.
   *
   * @throws IllegalStateException if a cursor is requested but no OutputTarget is set.
   */
  protected SimplePageLayoutCursor getCursor ()
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
   * @param cursor the cursor (null not permitted).
   * @throws NullPointerException if the given cursor is null
   */
  protected void setCursor (final SimplePageLayoutCursor cursor)
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
   * @param b the band.
   */
  protected void createSaveState (final Band b)
  {
    if (b == null)
    {
      state = new SimpleLayoutManagerState(null);
    }
    else
    {
      state = new SimpleLayoutManagerState(b.getTreeLock());
    }
  }

  /**
   * Returns the current state. The state was previously recorded using the
   * <code>createSaveState(Band b)</code> method.
   *
   * @return the current state, never null
   */
  protected PageLayouter.LayoutManagerState saveCurrentState ()
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
   * @param anchestor the ancestor state.
   * @throws ReportProcessingException if the printing failed or a pagebreak is requested
   *                                   while the page is restored.
   * @throws IllegalStateException     if there is no SavedState but this is not the first
   *                                   page.
   */
  public void restoreSaveState (final ReportState anchestor)
          throws ReportProcessingException
  {
    super.restoreSaveState(anchestor);
    isLastPageBreak = false;
  }

  /**
   * Handles the restarting of the page. Fires the pageStarted event and prints the
   * pageheader. Restarting the page is done once after the PageLayouterState was
   * restored.
   *
   * @throws ReportProcessingException if restarting the page failed.
   * @param reportState
   */
  public void restartPage (final ReportState reportState)
          throws ReportProcessingException
  {
    if (isPageRestartDone() || isRestartingPage() || isFinishingPage())
    {
//      throw new IllegalStateException ("SimplePageLayouter: restartPage() is already running ..");
      return;
    }
    startPage(reportState);

    if (this.state == null)
    {
      // no state saved, break  ...
      return;
    }

    setRestartingPage(true);
    boolean pagebreakAfter = false;
    // if there was a pagebreak_after_print, there is no band to print for now
    if (this.state.getBandID() != null)
    {
      final ReportDefinition impl = reportState.getReport();
      final Band band = getBandForID(impl, this.state.getBandID());
      pagebreakAfter = band.getStyle().getBooleanStyleProperty
              (BandStyleKeys.PAGEBREAK_AFTER);
      band.getStyle().setBooleanStyleProperty
              (BandStyleKeys.PAGEBREAK_AFTER, false);

      setCurrentEvent(new ReportEvent (reportState, reportState.getEventCode()));
      print(band, pagebreakAfter, PAGEBREAK_BEFORE_IGNORED);
      band.getStyle().setBooleanStyleProperty
              (BandStyleKeys.PAGEBREAK_AFTER, pagebreakAfter);
      clearCurrentEvent();
    }
    clearSaveState();
    setRestartingPage(false);

    // ugly fix for the reportd-problem ..
    if (pagebreakAfter)
    {
      createSaveState(null);
      endPage(false);
    }
  }

  private Band getBandForID (final ReportDefinition def,
                             final Object id)
  {
    if (def.getReportHeader().getTreeLock() == id)
    {
      return def.getReportHeader();
    }
    if (def.getReportFooter().getTreeLock() == id)
    {
      return def.getReportFooter();
    }
    if (def.getPageHeader().getTreeLock() == id)
    {
      return def.getPageHeader();
    }
    if (def.getPageFooter().getTreeLock() == id)
    {
      return def.getPageFooter();
    }
    if (def.getItemBand().getTreeLock() == id)
    {
      return def.getItemBand();
    }
    for (int i = 0; i < def.getGroupCount(); i++)
    {
      final Group g = def.getGroup(i);
      if (g.getHeader().getTreeLock() == id)
      {
        return g.getHeader();
      }
      if (g.getFooter().getTreeLock() == id)
      {
        return g.getFooter();
      }
    }
    return null;
  }

  /**
   * Clears the layout state.
   */
  protected void clearSaveState ()
  {
    super.clearSaveState();
    state = null;
  }

  /**
   * Sets the logical page and adjust the cursor.
   *
   * @param logicalPage the logical page.
   */
  public void setLogicalPage (final LogicalPage logicalPage)
  {
    super.setLogicalPage(logicalPage);
    final long internalVerticalAlignmentBorder =
            logicalPage.getLayoutSupport().getInternalVerticalAlignmentBorder();
    setCursor(new SimplePageLayoutCursor
            (AbstractBandLayoutManager.alignDown
            (StrictGeomUtility.toInternalValue(logicalPage.getHeight()),
                    internalVerticalAlignmentBorder)));
  }

  /**
   * Ends the page.
   *
   * @param force set to true, to skip the test whether the logical page is empty and to
   *              enforce an pagebreak. This is a requirement when an completly empty
   *              report (no bands or elements printed) should be finished.
   * @return true if the pageBreak is done, false otherwise.
   *
   * @throws ReportProcessingException if finishing the page failed.
   */
  protected boolean endPage (final boolean force)
          throws ReportProcessingException
  {
    if (isGeneratedPageEmpty() == false || force)
    {
      super.endPage();
      return true;
    }
    else
    {
      return false;
    }
  }

  /**
   * Returns true, if the PageLayouter has successfully started a new page. The start of
   * the new page is delayed, until the first content is printed.
   *
   * @return true, if a new page has already been started, false otherwise.
   */
  public boolean isNewPageStarted ()
  {
    return startNewPage;
  }

  /**
   * Defines whether the PageLayouter has successfully started a new page. The start of
   * the new page is delayed, until the first content is printed, this flag is used to
   * keep track of the page initialization state.
   *
   * @param startNewPage true, if a new page has already been started, false otherwise.
   */
  public void setStartNewPage (final boolean startNewPage)
  {
    this.startNewPage = startNewPage;
  }

  /**
   * Return a completly separated copy of this function. The copy does no longer share any
   * changeable objects with the original function.
   *
   * @return a copy of this function.
   */
  public Expression getInstance ()
  {
    final SimplePageLayouter sl = (SimplePageLayouter) super.getInstance();
    sl.delegate = new SimplePageLayoutDelegate(sl);
    sl.cursor = null;
    sl.isLastPageBreak = false;
    sl.state = null;
    return sl;
  }

  /**
   * Clones the layouter.
   *
   * @return the clone.
   *
   * @throws CloneNotSupportedException if there is a problem cloning.
   */
  public Object clone ()
          throws CloneNotSupportedException
  {
    final SimplePageLayouter sl = (SimplePageLayouter) super.clone();
    sl.delegate = (SimplePageLayoutDelegate) delegate.clone();
    sl.delegate.setWorker(sl);
    return sl;
  }

  /**
   * Receives notification of a prepare event.
   *
   * @param event the event.
   */
  public void prepareEvent (final ReportEvent event)
  {
    // docmark
//    setCurrentEvent(event);
//    try
//    {
//      restartPage();
//    }
//    catch (Exception e)
//    {
//      throw new FunctionProcessingException("prepareEvent", e);
//    }
//    finally
//    {
//      clearCurrentEvent();
//    }
  }

  public boolean isWatermarkSupported ()
  {
    return getReport().getReportConfiguration().getConfigProperty
            (WATERMARK_PRINTED_KEY, "true").equals("true");
  }

  public boolean printWatermark (final Band watermark)
          throws ReportProcessingException
  {
    final LogicalPage logPage = getLogicalPage();
    final long width = StrictGeomUtility.toInternalValue(logPage.getWidth());
    final long height = StrictGeomUtility.toInternalValue(logPage.getHeight());
    final StrictBounds bounds = BandLayoutManagerUtil.doFixedLayout
            (watermark, logPage.getLayoutSupport(), width, height);
    final boolean retval = doPrint(bounds, watermark, true, true, -1);
    return retval;
  }

  public void finishPageAfterRestore (final ReportState state)
          throws ReportProcessingException
  {
    createSaveState(null);
    super.finishPageAfterRestore(state);
  }

  /**
   * This event is fired, whenever an automatic pagebreak has been detected and the report
   * state had been reverted to the previous state.
   *
   * @param event
   */
  public void pageRolledBack (final ReportEvent event)
  {
    // we do nothing here ..
  }
}
