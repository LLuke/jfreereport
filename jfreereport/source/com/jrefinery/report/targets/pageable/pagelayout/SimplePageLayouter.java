/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
 *
 * (C) Copyright 2000-2002, by Simba Management Limited and Contributors.
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
 * (C)opyright 2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: SimplePageLayouter.java,v 1.12 2002/12/18 20:46:41 taqua Exp $
 *
 * Changes
 * -------
 * 05-Dec-2002 : Updated Javadocs (DG);
 * 07-Dec-2002 : Removed manual-pagebreak flag, was a left-over. PageFinished
 *               did not query the DISPLAY_ON_FIRSTPAGE flag.
 */

package com.jrefinery.report.targets.pageable.pagelayout;

import com.jrefinery.report.Band;
import com.jrefinery.report.Group;
import com.jrefinery.report.ReportProcessingException;
import com.jrefinery.report.util.Log;
import com.jrefinery.report.event.ReportEvent;
import com.jrefinery.report.function.FunctionProcessingException;
import com.jrefinery.report.states.ReportState;
import com.jrefinery.report.states.PostReportFooterState;
import com.jrefinery.report.targets.pageable.bandlayout.BandLayoutManager;
import com.jrefinery.report.targets.pageable.bandlayout.BandLayoutManagerUtil;
import com.jrefinery.report.targets.pageable.OutputTargetException;
import com.jrefinery.report.targets.pageable.Spool;
import com.jrefinery.report.targets.pageable.LogicalPage;
import com.jrefinery.report.targets.style.BandStyleSheet;
import com.jrefinery.report.targets.style.ElementStyleSheet;
import com.jrefinery.report.targets.FloatDimension;

import java.awt.geom.Dimension2D;
import java.awt.geom.Rectangle2D;

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
public class SimplePageLayouter extends PageLayouter
{
  /** A useful constant. */
  private boolean ENDPAGE_FORCED = true;

  /** A useful constant. */
  private boolean ENDPAGE_REQUESTED = false;

  /** A flag that indicates if the report state is currently inside the item group. */
  private boolean isInItemGroup;

  /** A flag that indicates that the current pagebreak will be the last one. */
  private boolean isLastPageBreak;

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
    public SimpleLayoutManagerState(Band band)
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
  }

  /** The cursor used by the layouter. */
  private SimplePageLayoutCursor cursor;

  /** The current state. */
  private SimpleLayoutManagerState state;

  /** The spool. */
  private Spool spooledBand;

  /**
   * Creates a new page layouter.
   */
  public SimplePageLayouter()
  {
    setName("Layout");
  }

  /**
   * Receives notification that the report has started. Also invokes the
   * start of the first page ...
   * <P>
   * Layout and draw the report header after the PageStartEvent was fired.
   *
   * @param event  the event.
   */
  public void reportStarted(ReportEvent event)
  {
    isInItemGroup = false;
    setCurrentEvent(event);
    try
    {
      startPage(event.getState());
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
    isInItemGroup = false;
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
    isInItemGroup = true;
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
  public void pageStarted(ReportEvent event)
  {
    setCurrentEvent(event);
    try
    {
      // a new page has started, so reset the cursor ...
      setCursor(new SimplePageLayoutCursor((float) getLogicalPage().getHeight()));

      Band b = getReport().getPageHeader();
      if (event.getState().getCurrentPage() == 1)
      {
        if (b.getStyle().getBooleanStyleProperty(BandStyleSheet.DISPLAY_ON_FIRSTPAGE) == true)
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
      if (isInItemGroup)
      {
        int gidx = event.getState().getCurrentGroupIndex();
        while (gidx >= 0)
        {
          Group g = null;
          if (gidx >= 0)
          {
            g = getReport().getGroup(gidx);
          }
          if (g.getHeader().getStyle().getBooleanStyleProperty(BandStyleSheet.REPEAT_HEADER))
          {
            print(g.getHeader(), true);
            break;
          }
          else
          {
            gidx--;
          }
        }
      }
      // todo: do not print on last page ... how to get the information when the last page is
      // reached for all events?


      // mark the current position to calculate the maxBand-Height
      getCursor().setPageTop(getCursor().getY());
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
  public void pageFinished(ReportEvent event)
  {
    setCurrentEvent(event);
    try
    {
      getCursor().setReservedSpace(0);
      Band b = getReport().getPageFooter();
      if (event.getState().getCurrentPage() == 1)
      {
        if (b.getStyle().getBooleanStyleProperty(BandStyleSheet.DISPLAY_ON_FIRSTPAGE) == true)
        {
          printBottom(b);
        }
      }
      else
      if (isLastPageBreak)
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
  }

  /**
   * Receives notification that the report has finished.
   * <P>
   * Prints the ReportFooter and forces the last pagebreak.
   *
   * @param event Information about the event.
   */
  public void reportFinished(ReportEvent event)
  {
    try
    {
      setCurrentEvent(event);
      // force the last pagebreak ...
      isLastPageBreak = true;

      Band b = getReport().getReportFooter();

      // if the band was printed on that page without PAGEBREAK_BEFORE
      // the final pagebreak is forced later ..
      if (printBand(b) && isPageEnded() == false)
      {
        createSaveState(null);
        endPage(ENDPAGE_FORCED);
      }
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
   * Receives notification that a group has started.
   * <P>
   * Prints the GroupHeader
   *
   * @param event Information about the event.
   */
  public void groupStarted(ReportEvent event)
  {
    try
    {
      setCurrentEvent(event);
      int gidx = event.getState().getCurrentGroupIndex();
      Group g = getReport().getGroup(gidx);
      Band b = g.getHeader();
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
  }

  /**
   * Receives notification that a group has finished.
   * <P>
   * Prints the GroupFooter.
   *
   * @param event Information about the event.
   */
  public void groupFinished(ReportEvent event)
  {
    try
    {
      setCurrentEvent(event);
      int gidx = event.getState().getCurrentGroupIndex();
      Group g = getReport().getGroup(gidx);
      Band b = g.getFooter();
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
  }


  /**
   * Receives notification that a row of data is being processed.
   * <P>
   * prints the ItemBand.
   *
   * @param event Information about the event.
   */
  public void itemsAdvanced(ReportEvent event)
  {
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
  }

  /**
   * Prints a band.
   *
   * @param b  the band.
   *
   * @throws ReportProcessingException if the printing or spooling of the band failed.
   * @return true, if the band was printed, false if the printing was delayed to the next page
   */
  private boolean printBand (Band b) throws ReportProcessingException
  {
    if (b.getStyle().getBooleanStyleProperty(BandStyleSheet.PAGEBREAK_BEFORE) == true)
    {
      createSaveState(b);
      if (endPage(ENDPAGE_REQUESTED) == false)
      {
        // no pagebreak was done, the band was printed to an already empty page
        return print(b, false);
      }
      // a pagebreak was requested, printing is delayed
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
   *
   * @throws ReportProcessingException if the printing failed
   */
  protected boolean print(Band b, boolean spool)
      throws ReportProcessingException
  {
    float y = getCursor().getY();
    // don't save the state if the current page is currently being finished
    // or restarted; PageHeader and PageFooter are printed out of order and
    // do not influence the reporting state

    Rectangle2D bounds = doLayout(b);
    bounds.setRect(0, y, bounds.getWidth(), bounds.getHeight());
    return doPrint(bounds, b, spool);
  }

  /**
   * Prints a band at the bottom of the page.
   *
   * @param b  the band.
   *
   * @throws ReportProcessingException if the printing failed
   */
  protected boolean printBottom (Band b)
    throws ReportProcessingException
  {
    // don't save the state if the current page is currently beeing finished
    // or restarted; PageHeader and PageFooter are printed out of order and
    // do not influence the reporting state

    Rectangle2D bounds = doLayout(b);
    bounds.setRect(0, getCursor().getPageBottomReserved() - bounds.getHeight(),
                   bounds.getWidth(), bounds.getHeight());
    return doPrint(bounds, b, false);
  }

  /**
   * Perform the layout of a band. The height of the band is calculated according to the contents
   * of the band.  The width of the band will always span the complete printable width.
   *
   * @param band  the band.
   *
   * @return the dimensions of the band.
   */
  protected Rectangle2D doLayout(Band band)
  {
    BandLayoutManager lm
        = BandLayoutManagerUtil.getLayoutManager(band, getLogicalPage().getOutputTarget());
    // in this layouter the width of a band is always the full page width
    float width = (float) getLogicalPage().getWidth();
    float height = getCursor().getPageBottomReserved() - getCursor().getPageTop();
    Dimension2D fdim = lm.preferredLayoutSize(band, new FloatDimension(width, height));

    // the height is defined by the band's requirements
    height = (float) fdim.getHeight();
    Rectangle2D bounds = new Rectangle2D.Float(0, 0, width, height);
    band.getStyle().setStyleProperty(ElementStyleSheet.BOUNDS, bounds);
    lm.doLayout(band);
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
   * @see com.jrefinery.report.targets.pageable.LogicalPage#spoolBand
   *
   * @throws ReportProcessingException if the printing caused an detectable error
   * while printing the band
   */
  protected boolean doPrint(Rectangle2D bounds, Band band, boolean spool)
    throws ReportProcessingException
  {
    if ((spooledBand != null) && (spool == false))
    {
      getLogicalPage().replaySpool (spooledBand);
      spooledBand = null;
    }
    try
    {
      float height = (float) bounds.getHeight();
      // handle the end of the page
      if (isFinishingPage())
      {
        getLogicalPage().addBand(bounds, band);
        cursor.advance(height);
        return true;
      }
      // handle a automatic pagebreak in case there is not enough space here ...
      else if ((isSpaceFor(height) == false) && (isPageEnded() == false))
      {
        createSaveState(band);
        endPage(ENDPAGE_FORCED);
        return false;
      }
      else if (isPageEnded()) // page has ended before, but that band should be printed
      {
        createSaveState(band);
        return false;
      }
      else
      {
        if (spool)
        {
          Spool newSpool = getLogicalPage().spoolBand(bounds, band);
          if (spooledBand == null)
          {
            spooledBand = newSpool;
          }
          else
          {
            Log.debug ("There is a band already spooled");
            spooledBand.merge (newSpool);
          }

          cursor.advance(height);
          return true;
        }
        else
        {
          getLogicalPage().addBand(bounds, band);
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
  public boolean isSpaceFor(float height)
  {
    Band b = getReport().getPageFooter();
    Rectangle2D rect = doLayout(b);
    getCursor().setReservedSpace((float) rect.getHeight());
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
  protected void setCursor(SimplePageLayoutCursor cursor)
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
  protected void createSaveState(Band b)
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
  public void restoreSaveState(ReportState anchestor)
      throws ReportProcessingException
  {
    SimpleLayoutManagerState state = (SimpleLayoutManagerState) getLayoutManagerState();
    // reset the report finished flag...
    isLastPageBreak = false;
    if (state == null)
    {
      if (anchestor.getCurrentPage() != 1)
      {
        throw new IllegalStateException("State is null, but this is not the first page");
      }
      return; // no state yet, maybe the first state?
    }

    Log.debug ("State: " + anchestor.getCurrentPage() + " " + anchestor.getCurrentDataItem() + " " + anchestor.getDataRow());
    startPage(anchestor);

    // if there was a pagebreak_after_print, there is no band to print for now
    if (state.getBand() != null)
    {
      print(state.getBand(), false);
    }
    clearSaveState();
    // this is the last valid state for the reporting,
    // force the last pagebreak if the reportfooter was printed here.
    if (anchestor instanceof PostReportFooterState && state.getBand() != null)
    {
      createSaveState(null);
      endPage(ENDPAGE_FORCED);
    }
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
  public void setLogicalPage(LogicalPage logicalPage)
  {
    super.setLogicalPage(logicalPage);
    setCursor(new SimplePageLayoutCursor((float) getLogicalPage().getHeight()));
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
   * @throws ReportProcessingException ??.
   */
  protected boolean endPage(boolean force) throws ReportProcessingException
  {
    if (getLogicalPage().isEmpty() == false || force)
    {
      if (spooledBand != null)
      {
        getLogicalPage().replaySpool(spooledBand);
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
   * Clones the layouter.
   *
   * @return the clone.
   *
   * @throws CloneNotSupportedException if there is a problem cloning.
   */
  public Object clone () throws CloneNotSupportedException
  {
    SimplePageLayouter sl = (SimplePageLayouter) super.clone();
    if (spooledBand != null)
    {
      sl.spooledBand = (Spool) spooledBand.clone();
    }
    return sl;
  }
}
