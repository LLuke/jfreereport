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
 * $Id: SimplePageLayouter.java,v 1.1 2002/12/02 17:57:06 taqua Exp $
 *
 * Changes
 * -------
 */
package com.jrefinery.report.targets.pageable.pagelayout;

import com.jrefinery.report.Band;
import com.jrefinery.report.Group;
import com.jrefinery.report.ReportProcessingException;
import com.jrefinery.report.util.Log;
import com.jrefinery.report.event.ReportEvent;
import com.jrefinery.report.function.FunctionProcessingException;
import com.jrefinery.report.states.ReportState;
import com.jrefinery.report.targets.pageable.bandlayout.BandLayoutManager;
import com.jrefinery.report.targets.pageable.bandlayout.BandLayoutManagerUtil;
import com.jrefinery.report.targets.pageable.OutputTargetException;
import com.jrefinery.report.targets.pageable.Spool;
import com.jrefinery.report.targets.pageable.LogicalPage;
import com.jrefinery.report.targets.pageable.pagelayout.PageLayouter;
import com.jrefinery.report.targets.pageable.pagelayout.SimplePageLayoutCursor;
import com.jrefinery.report.targets.style.BandStyleSheet;
import com.jrefinery.report.targets.style.ElementStyleSheet;

import java.awt.geom.Dimension2D;
import java.awt.geom.Rectangle2D;

/**
 * A simple page layouter.  This class replicates the 'old' behaviour of JFreeReport, 
 * simple and straightforward.
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
  
  /** A flag that indicates if ??. */
  private boolean isLastPageBreak;

  protected static class SimpleLayoutManagerState extends PageLayouter.LayoutManagerState
  {
    private Band band;
    private boolean isManualPagebreak;

    /**
     * Band can be null if there is no band to be printed on the next page
     * @param band
     */
    public SimpleLayoutManagerState(Band band, boolean manualBreak)
    {
      this.band = band;
      this.isManualPagebreak = manualBreak;
    }

    public Band getBand()
    {
      return band;
    }

    public boolean isManualPagebreak()
    {
      return isManualPagebreak;
    }

    public void setManualPagebreak(boolean manualPagebreak)
    {
      isManualPagebreak = manualPagebreak;
    }
  }

  private SimplePageLayoutCursor cursor;
  private SimpleLayoutManagerState state;
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
   * Layout and draw the header
   *
   * @param event Information about the event.
   */
  public void reportStarted(ReportEvent event)
  {
    isInItemGroup = false;
    try
    {
      setCurrentEvent(event);
      startPage(event.getState());
      printBand(getReport().getReportHeader());
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
   * Maps the pageStarted-method to the legacy function startPage (int).
   *
   * @param event Information about the event.
   */
  public void pageStarted(ReportEvent event)
  {
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
            Log.debug ("Repeating GroupHeader found at group " + gidx);
            print(g.getHeader(), true);
            break;
          }
          else
          {
            gidx--;
          }
        }
      }
      // to do: do not print on last page ... how to get the information when the last page is reached?
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
   * Receives notification that a page has ended.
   * <P>
   * Maps the pageFinished-method to the legacy function endPage (int).
   *
   * @param event Information about the event.
   */
  public void pageFinished(ReportEvent event)
  {
    try
    {
      getCursor().setReservedSpace(0);
      Band b = getReport().getPageFooter();
      if (isLastPageBreak)
      {
        if (b.getStyle().getBooleanStyleProperty(BandStyleSheet.DISPLAY_ON_FIRSTPAGE) == true)
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
      throw new FunctionProcessingException("ReportFinished failed", e);
    }
  }

  /**
   * Receives notification that the report has finished.
   * <P>
   * Maps the reportFinished-method to the legacy function endReport ().
   *
   * @param event Information about the event.
   */
  public void reportFinished(ReportEvent event)
  {
    try
    {
      setCurrentEvent(event);
      Band b = getReport().getReportFooter();
      printBand(b);

      // force the last pagebreak ...
      isLastPageBreak = true;
      createSaveState(null, true);
      endPage(ENDPAGE_FORCED);
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

  private void printBand (Band b) throws ReportProcessingException
  {
    if (b.getStyle().getBooleanStyleProperty(BandStyleSheet.PAGEBREAK_BEFORE) == true)
    {
      createSaveState(b, true);
      if (endPage(ENDPAGE_REQUESTED) == false)
      {
        print(b, false);
      }
    }
    else
    {
      print(b, false);
    }
  }

  /**
   * Receives notification that a group has started.
   * <P>
   * Maps the groupStarted-method to the legacy function startGroup (int).
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
      throw new FunctionProcessingException("ReportFinished failed", e);
    }
  }

  /**
   * Receives notification that a group has finished.
   * <P>
   * Maps the groupFinished-method to the legacy function endGroup (int).
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
      throw new FunctionProcessingException("ReportFinished failed", e);
    }
  }


  /**
   * Receives notification that a row of data is being processed.
   * <P>
   * Maps the itemsAdvanced-method to the legacy function advanceItems (int).
   *
   * @param event Information about the event.
   */
  public void itemsAdvanced(ReportEvent event)
  {
    try
    {
      setCurrentEvent(event);
      setCurrentEvent(event);
      Band b = getReport().getItemBand();
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
  }


  protected void print(Band b, boolean spool)
      throws ReportProcessingException
  {
    float y = getCursor().getY();
    // don't save the state if the current page is currently beeing finished
    // or restarted; PageHeader and PageFooter are printed out of order and
    // do not influence the reporting state

    Rectangle2D bounds = doLayout(b);
    bounds.setRect(0, y, bounds.getWidth(), bounds.getHeight());
    doPrint(bounds, b, spool);
  }

  protected void printBottom (Band b)
    throws ReportProcessingException
  {
    // don't save the state if the current page is currently beeing finished
    // or restarted; PageHeader and PageFooter are printed out of order and
    // do not influence the reporting state

    Rectangle2D bounds = doLayout(b);
    bounds.setRect(0, getCursor().getPageBottomReserved() - bounds.getHeight(), bounds.getWidth(), bounds.getHeight());
    doPrint(bounds, b, false);
  }

  protected Rectangle2D doLayout(Band band)
  {
    BandLayoutManager lm = BandLayoutManagerUtil.getLayoutManager(band, getLogicalPage().getOutputTarget());
    Dimension2D fdim = lm.preferredLayoutSize(band);

    // in this layouter the width of a band is always the full page width
    float width = (float) getLogicalPage().getWidth();
    // the height is defined by the band's requirements
    float height = (float) fdim.getHeight();
    Rectangle2D bounds = new Rectangle2D.Float(0, 0, width, height);
    band.getStyle().setStyleProperty(ElementStyleSheet.BOUNDS, bounds);
    lm.doLayout(band);
    return bounds;
  }

  protected void doPrint(Rectangle2D bounds, Band band, boolean spool)
    throws ReportProcessingException
  {
    if ((spooledBand != null) && (spool == false))
    {
      getLogicalPage().replaySpool (spooledBand);
    }
    try
    {
      float height = (float) bounds.getHeight();
      // handle the end of the page
      if (isFinishingPage())
      {
        getLogicalPage().addBand(bounds, band);
        cursor.advance(height);
      }
      // handle a automatic pagebreak in case there is not enough space here ...
      else if ((isSpaceFor(height) == false) && (isPageEnded() == false))
      {
        createSaveState(band, false);
        endPage(ENDPAGE_FORCED);
      }
      else if (isPageEnded()) // page has ended before, but that band should be printed
      {
        createSaveState(band, true);
      }
      else
      {
        if (spool)
        {
          Spool newSpool = getLogicalPage().spoolBand(bounds, band);
          if (spooledBand == null)
            spooledBand = newSpool;
          else
            spooledBand.merge (newSpool);

          cursor.advance(height);
        }
        else
        {
          getLogicalPage().addBand(bounds, band);
          cursor.advance(height);
          if (band.getStyle().getBooleanStyleProperty(BandStyleSheet.PAGEBREAK_AFTER) == true)
          {
            createSaveState(null, true);
            endPage(ENDPAGE_REQUESTED);
          }
        }
      }
    }
    catch (ReportProcessingException rpe)
    {
      throw rpe;
    }
    catch (OutputTargetException ote)
    {
      ote.printStackTrace();
      throw new FunctionProcessingException("Failed to print");
    }
  }

  /**
   * Perform doLayout for the pageFooter to guess the height.
   */
  public boolean isSpaceFor(float height)
  {
    Band b = getReport().getPageFooter();
    Rectangle2D rect = doLayout(b);
    getCursor().setReservedSpace((float) rect.getHeight());
    return getCursor().isSpaceFor(height);
  }

  protected SimplePageLayoutCursor getCursor()
  {
    if (cursor == null) throw new IllegalStateException("No cursor, no OutputTarget: " + hashCode());
    return cursor;
  }

  protected void setCursor(SimplePageLayoutCursor cursor)
  {
    if (cursor == null) throw new NullPointerException();
    this.cursor = cursor;
  }

  protected void createSaveState(Band b, boolean manualBreak)
  {
    state = new SimpleLayoutManagerState(b, manualBreak);
  }

  protected PageLayouter.LayoutManagerState saveCurrentState()
  {
    if (state == null) throw new NullPointerException();
    return state;
  }

  public void restoreSaveState(ReportState anchestor)
      throws ReportProcessingException
  {
    SimpleLayoutManagerState state = (SimpleLayoutManagerState) getLayoutManagerState();
    // reset the report finished flag...
    isLastPageBreak = false;
    if (state == null)
    {
//      Log.debug ("SimpleLayouManagerState is null, first page?");
//      Log.debug ("LogicalPage: " + getLogicalPage().isEmpty() + " && " + getLogicalPage().isClosed());
      if (anchestor.getCurrentPage() != 1) throw new IllegalStateException();
      return; // no state yet, maybe the first state?
    }
    startPage(anchestor);
    // if there was a pagebreak_after_print, there is no band to print for now
    if (state.getBand() != null)
    {
      print(state.getBand(), false);
    }
    clearSaveState();
  }

  protected void clearSaveState()
  {
    super.clearSaveState();
    state = null;
  }

  public void setLogicalPage(LogicalPage logicalPage)
  {
    super.setLogicalPage(logicalPage);
    setCursor(new SimplePageLayoutCursor((float) getLogicalPage().getHeight()));
  }

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
   * A detector whether the last pagebreak was a manual pagebreak or an automatic one
   *
   * @return
   */
  public boolean isManualPageBreak()
  {
    return false;
  }

  public Object clone () throws CloneNotSupportedException
  {
    SimplePageLayouter sl = (SimplePageLayouter) super.clone();
    if (spooledBand != null) sl.spooledBand = (Spool) spooledBand.clone();
    return sl;
  }
}
