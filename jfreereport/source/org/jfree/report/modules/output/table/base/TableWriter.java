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
 * ----------------
 * TableWriter.java
 * ----------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Object Refinery Limited);
 *
 * $Id: TableWriter.java,v 1.20 2005/03/03 14:42:38 taqua Exp $
 *
 * Changes
 * -------
 * 24-Jan-2003 : Initial version
 * 17-Feb-2003 : Documentation
 * 24-Feb-2003 : Fixed Checkstyle issues (DG);
 *
 */
package org.jfree.report.modules.output.table.base;

import org.jfree.report.Band;
import org.jfree.report.ReportProcessingException;
import org.jfree.report.content.ContentCreationException;
import org.jfree.report.event.PageEventListener;
import org.jfree.report.event.ReportEvent;
import org.jfree.report.function.AbstractFunction;
import org.jfree.report.function.Expression;
import org.jfree.report.function.FunctionProcessingException;
import org.jfree.report.layout.BandLayoutManagerUtil;
import org.jfree.report.layout.DefaultLayoutSupport;
import org.jfree.report.layout.LayoutSupport;
import org.jfree.report.modules.output.meta.MetaBand;
import org.jfree.report.modules.output.meta.MetaBandProducer;
import org.jfree.report.modules.output.support.pagelayout.SimplePageLayoutDelegate;
import org.jfree.report.modules.output.support.pagelayout.SimplePageLayoutWorker;
import org.jfree.report.states.ReportState;
import org.jfree.report.style.BandStyleKeys;
import org.jfree.report.util.Log;
import org.jfree.report.util.geom.StrictBounds;

/**
 * The TableWriter is the content creation function used to collect the cell data. After
 * the layouting is done, the layouted bands are forwarded to the TableProducer. The
 * virtual page has an unlimited size, only when a manual pagebreak is encountered, a new
 * page is started.
 * <p/>
 * This can be used to f.i. to create separate sheets in Excel-Workbooks, the detailed
 * semantics depend on concrete implementation of the TableProducer.
 * <p/>
 * This writer is not thread-safe.
 *
 * @author Thomas Morgner
 */
public strictfp class TableWriter
        extends AbstractFunction
        implements PageEventListener, SimplePageLayoutWorker
{
  /**
   * A constant defining the tablewriters default function level.
   */
  public static final int OUTPUT_LEVEL = -1;

  /**
   * The current event, stored on every call to one of the ReportListener methods.
   */
  private ReportEvent currentEvent;

  /**
   * the cursor pointing to the current position on the sheet.
   */
  private TableWriterCursor cursor;

  /**
   * the maximum width, required for the BandLayout. (as internal value)
   */
  private long maxWidth;

  /**
   * the dependency level for this function, usually -1.
   */
  private int depLevel;

  /**
   * A flag indicating whether the writer is currently handling the end of an page.
   */
  private boolean inEndPage;

  /**
   * The layout delegate used to perform the page layout.
   */
  private SimplePageLayoutDelegate delegate;

  /**
   * The table creator instances are used to create the content and to compute the
   * layout.
   */
  private TableCreator tableCreator;

  /**
   * The metaBand producer translates bands into an report state independent form.
   */
  private MetaBandProducer metaBandProducer;

  private LayoutSupport layoutSupport;

  /**
   * Creates a new TableWriter. The dependency level is set to -1 and the maxwidth is
   * defined to be 1000.
   */
  public TableWriter (final MetaBandProducer metaBandProducer)
  {
    setDependencyLevel(OUTPUT_LEVEL);
    this.delegate = new SimplePageLayoutDelegate(this);
    this.metaBandProducer = metaBandProducer;
    this.layoutSupport = new DefaultLayoutSupport();
  }

  /**
   * Clones the function. <P> Be aware, this does not create a deep copy. If you have
   * complex strucures contained in objects, you have to override this function.
   *
   * @return a clone of this function.
   *
   * @throws CloneNotSupportedException this should never happen.
   */
  public Object clone ()
          throws CloneNotSupportedException
  {
    final TableWriter clone = (TableWriter) super.clone();
    clone.delegate = (SimplePageLayoutDelegate) delegate.clone();
    clone.delegate.setWorker(clone);
    // the metaproducer is !Not! cloned
    return clone;
  }

  /**
   * Return a completly separated copy of this function. The copy does no longer share any
   * changeable objects with the original function.
   *
   * @return a copy of this function.
   */
  public Expression getInstance ()
  {
    final TableWriter tw = (TableWriter) super.getInstance();
    tw.delegate = new SimplePageLayoutDelegate(tw);
    return tw;
  }

  /**
   * Checks, whether the current page is empty. An page is empty if it does not contain
   * printed content. An empty page may have spooled content.
   *
   * @return true, if the page is empty, false otherwise.
   */
  public boolean isPageEmpty ()
  {
    if (tableCreator == null)
    {
      throw new IllegalStateException("tableCreator is null." + toString());
    }
    return tableCreator.isEmpty();
  }

  /**
   * Returns the position of the first content. As this writer does not limit the height
   * of the bands, this method returns 0.
   *
   * @return the first content position.
   */
  public long getTopContentPosition ()
  {
    return 0;
  }

  /**
   * This writer does not limit the height of an band and therefore does not implement
   * that feature.
   *
   * @param topPosition the first usable position to print content.
   */
  public void setTopPageContentPosition (final long topPosition)
  {
  }

  /**
   * Returns the reserved size for the current page. This size is not used when performing
   * a layout. This is usually used to preserve the pagefooters space.  As this writer
   * does not limit the height of the bands, this method returns 0.
   *
   * @return the reserved page height.
   */
  public long getReservedSpace ()
  {
    return 0;
  }

  /**
   * Defines the reserved size for the current page. This size is not used when performing
   * a layout.
   * <p/>
   * This method does nothing.
   *
   * @param reserved the reserved page height.
   */
  public void setReservedSpace (final long reserved)
  {
  }

  /**
   * Returns the current cursor position. It is assumed, that the cursor goes from top to
   * down, columns are not used.
   *
   * @return the cursor position.
   */
  public long getCursorPosition ()
  {
    return getCursor().getY();
  }

  /**
   * Reinitialize the cursor of the layout worker. Called when a new page is started.
   */
  public void resetCursor ()
  {
    setCursor(new TableWriterCursor());
  }

  /**
   * Checks, whether the page has ended. Once a page that is completly filled, only the
   * page footer will be printed and a page break will be done after that.
   * <p/>
   * As this target has no notion of pages, the virtual page does never end automaticly.
   *
   * @return true, if the page is finished, false otherwise.
   */
  public boolean isPageEnded ()
  {
    return false;
  }

  /**
   * Prints the given band at the bottom of the page.
   * <p/>
   * As we don't use the idea of pages here, this call is mapped to the print method.
   *
   * @param band the band.
   * @return true, if the band was printed successfully, false otherwise.
   *
   * @throws ReportProcessingException if an exception occured while processing the band.
   */
  public boolean printBottom (final Band band)
          throws ReportProcessingException
  {
    return print(band, false, false);
  }

  /**
   * Prints the given band at the current cursor position.
   *
   * @param band                  the band to be printed.
   * @param spoolBand             a flag defining whether the content should be spooled.
   * @param handlePagebreakBefore a flag defining whether the worker should check for the
   *                              'pagebreak-before' flag.
   * @return true, if the band was printed, false otherwise.
   *
   * @throws ReportProcessingException if an exception occured while processing the band.
   */
  public boolean print (final Band band, final boolean spoolBand,
                        final boolean handlePagebreakBefore)
          throws ReportProcessingException
  {
    if (!isInEndPage() && handlePagebreakBefore
            && band.getStyle().getBooleanStyleProperty(BandStyleKeys.PAGEBREAK_BEFORE) == true)
    {
      if (isPageEmpty() == false)
      {
        endPage();
        startPage();
      }
      else
      {
        Log.debug("Page is empty; so no break");
      }
    }

    final long y = getCursor().getY();
    // don't save the state if the current page is currently being finished
    // or restarted; PageHeader and PageFooter are printed out of order and
    // do not influence the reporting state

    final StrictBounds bounds = doLayout(band);
    bounds.setRect(0, y, bounds.getWidth(), bounds.getHeight());
    try
    {
      doPrint(bounds, band);
    }
    catch (ContentCreationException e)
    {
      throw new ReportProcessingException("Failed to create content", e);
    }

    if (!isInEndPage() &&
            band.getStyle().getBooleanStyleProperty(BandStyleKeys.PAGEBREAK_AFTER) == true)
    {
      if (isPageEmpty() == false)
      {
        endPage();
        startPage();
      }
      else
      {
        Log.debug("Empty Page, ignore!");
      }
    }
    return true;
  }

  /**
   * Returns true, if the tablewriter is currently handling the end of the current page.
   *
   * @return true, if the end of the page is currently handled.
   */
  private boolean isInEndPage ()
  {
    return inEndPage;
  }

  /**
   * Gets the cursor for this writer. The cursor marks the current position in the current
   * sheet.
   *
   * @return the cursor.
   */
  private TableWriterCursor getCursor ()
  {
    return cursor;
  }

  /**
   * Sets the cursor for the writer.
   *
   * @param cursor the new cursor.
   */
  private void setCursor (final TableWriterCursor cursor)
  {
    this.cursor = cursor;
  }

  /**
   * Creates an output target that mimics a real output target, but produces no output.
   * This is used by the reporting engine when it makes its first pass through the report,
   * calculating page boundaries etc.  The second pass will use a real output target.
   *
   * @return a dummy output target.
   */
  public LayoutSupport getLayoutSupport ()
  {
    return layoutSupport;
  }

  /**
   * Return the current expression value. <P> The value depends (obviously) on the
   * expression implementation.
   *
   * @return the value of the function.
   */
  public Object getValue ()
  {
    return this;
  }

  /**
   * Gets the maximum width available for a root band during the layouting process. The
   * value specifies the maximum page width in the internal unit.
   *
   * @return the maximum width for a root band.
   */
  public long getMaxWidth ()
  {
    return maxWidth;
  }

  /**
   * Defines the maximum width available for a root band during the layouting process. The
   * value specifies the maximum page width in the internal unit.
   *
   * @param width the maximum width for a root band.
   */
  public void setMaxWidth (final long width)
  {
    maxWidth = width;
  }

  /**
   * Perform the layout of a band. The height of the band is calculated according to the
   * contents of the band.  The width of the band will always span the complete printable
   * width.
   *
   * @param band the band.
   * @return the dimensions of the band.
   */
  private StrictBounds doLayout (final Band band)
  {
    // in this layouter the width of a band is always the full page width.
    // the height is not limited ...
    final long width = getMaxWidth();
    final long height = Integer.MAX_VALUE;

    final StrictBounds bounds = BandLayoutManagerUtil.doLayout(band,
            getLayoutSupport(),
            width,
            height);
    getCurrentEvent().getState().fireLayoutCompleteEvent
            (band, getCurrentEvent().getType());
    return bounds;
  }

  /**
   * Forwards the given band to the TableProducer. This will create the content and will
   * add the TableCellData object to the grid.
   *
   * @param bounds the bounds of the band, defines the position of the printed band within
   *               the sheet.
   * @param band   the band that should be printed.
   */
  private void doPrint (final StrictBounds bounds, final Band band)
          throws ContentCreationException, ReportProcessingException
  {

    final MetaBand metaBand = metaBandProducer.createBand(band, false);
    if (metaBand == null)
    {
      // still move the cursor ..
      getCursor().advance(bounds.getHeight());
      return;
    }
    tableCreator.processBand(metaBand);
    getCursor().advance(bounds.getHeight());
    // fine grained flushing ... change that?
    getCurrentEvent().getState().fireOutputCompleteEvent
            (band, getCurrentEvent().getType());
    tableCreator.flush();
  }

  /**
   * Ends the current page. Fires the PageFinished event.
   */
  private void endPage ()
          throws ReportProcessingException
  {
    if (inEndPage == true)
    {
      throw new IllegalStateException("Already in startPage or endPage");
    }
    inEndPage = true;

    final ReportEvent currentEvent = getCurrentEvent();
    final ReportState cEventState = getCurrentEvent().getState();
    cEventState.firePageFinishedEvent();
    if (cEventState.isErrorOccured())
    {
      throw new ReportProcessingException
              ("An error occured while processing the page start - aborting");
    }
    cEventState.nextPage();
    setCurrentEvent(currentEvent);
    inEndPage = false;
  }

  /**
   * Starts a new page. Fires the PageStarted event.
   */
  private void startPage ()
          throws ReportProcessingException
  {
    if (inEndPage == true)
    {
      throw new IllegalStateException("Already in startPage or endPage");
    }
    inEndPage = true;

    final ReportEvent currentEvent = getCurrentEvent();
    final ReportState cEventState = currentEvent.getState();
    cEventState.firePageStartedEvent(currentEvent.getType());
    if (cEventState.isErrorOccured())
    {
      throw new ReportProcessingException
              ("An error occured while processing the page start - aborting");
    }
    setCurrentEvent(currentEvent);
    inEndPage = false;
  }

  /**
   * The dependency level defines the level of execution for this function. Higher
   * dependency functions are executed before lower dependency functions. For ordinary
   * functions and expressions, the range for dependencies is defined to start from 0
   * (lowest dependency possible to 2^31 (upper limit of int).
   * <p/>
   * PageLayouter functions override the default behaviour an place them self at depency
   * level -1, an so before any userdefined function.
   *
   * @return the level.
   */
  public int getDependencyLevel ()
  {
    return depLevel;
  }

  /**
   * Overrides the depency level. Should be lower than any other function depency.
   *
   * @param deplevel the new depency level.
   */
  public void setDependencyLevel (final int deplevel)
  {
    this.depLevel = deplevel;
  }

  /**
   * Receives notification that the report has started.
   *
   * @param event the event.
   */
  public void reportStarted (final ReportEvent event)
  {
    try
    {
      setCurrentEvent(event);
      tableCreator.open(event.getReport());
      startPage();
      delegate.reportStarted(event);
      clearCurrentEvent();
    }
    catch (ReportProcessingException e)
    {
      throw new FunctionProcessingException("TableWriter", e);
    }
  }

  /**
   * Receives notification that the report has finished.
   *
   * @param event the event.
   */
  public void reportFinished (final ReportEvent event)
  {
    try
    {
      setCurrentEvent(event);
      delegate.reportFinished(event);
      endPage();
      tableCreator.close();
      clearCurrentEvent();
    }
    catch (ReportProcessingException e)
    {
      throw new FunctionProcessingException("TableWriter", e);
    }
  }

  /**
   * Prints the PageHeader and all repeating group headers.
   *
   * @param event the event.
   */
  public void pageStarted (final ReportEvent event)
  {
    try
    {
      setCurrentEvent(event);
      tableCreator.beginTable(event.getReport());
      delegate.pageStarted(event);
      clearCurrentEvent();
    }
    catch (ReportProcessingException e)
    {
      throw new FunctionProcessingException("TableWriter", e);
    }
  }

  /**
   * Prints the page footer.
   *
   * @param event the event.
   */
  public void pageFinished (final ReportEvent event)
  {
    try
    {
      setCurrentEvent(event);
      delegate.pageFinished(event);
      tableCreator.endTable();
      clearCurrentEvent();
    }
    catch (ReportProcessingException e)
    {
      throw new FunctionProcessingException("TableWriter", e);
    }

  }

  /**
   * Prints the group header for the current group.
   *
   * @param event the event.
   */
  public void groupStarted (final ReportEvent event)
  {
    setCurrentEvent(event);
    delegate.groupStarted(event);
    clearCurrentEvent();
  }

  /**
   * Prints the group footer for the current group.
   *
   * @param event the event.
   */
  public void groupFinished (final ReportEvent event)
  {
    setCurrentEvent(event);
    delegate.groupFinished(event);
    clearCurrentEvent();
  }

  /**
   * Prints the itemband.
   *
   * @param event the event.
   */
  public void itemsAdvanced (final ReportEvent event)
  {
    setCurrentEvent(event);
    delegate.itemsAdvanced(event);
    clearCurrentEvent();
  }

  /**
   * Handles the start of the item processing. <P> The next events will be itemsAdvanced
   * events until the itemsFinished event is raised.
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
   * Handles the end of the item processing. <P> The itemBand is finished, the report
   * starts to close open groups.
   *
   * @param event The event.
   */
  public void itemsFinished (final ReportEvent event)
  {
    // this event does nothing
    setCurrentEvent(event);
    delegate.itemsFinished(event);
    clearCurrentEvent();
  }

  /**
   * Returns the current event, which has been updated at the start of every
   * ReportListener method.
   *
   * @return the current event.
   */
  public ReportEvent getCurrentEvent ()
  {
    return currentEvent;
  }

  /**
   * Defines the current event, which must be updated at the start of every ReportListener
   * method.
   *
   * @param currentEvent the current event.
   */
  public void setCurrentEvent (final ReportEvent currentEvent)
  {
    this.currentEvent = currentEvent;
  }

  /**
   * Clear the current event after the event was fully processed.
   */
  private void clearCurrentEvent ()
  {
    currentEvent = null;
  }

  /**
   * Gets the TableProducer.
   *
   * @return the table producer that should be used to create the TableCellData.
   */
  public TableCreator getTableCreator ()
  {
    return tableCreator;
  }

  /**
   * Sets the TableProducer, that should be used to create the TableCellData.
   *
   * @param producer the table producer that should be used to create the TableCellData.
   */
  public void setTableCreator (final TableCreator producer)
  {
    if (producer == null)
    {
      throw new NullPointerException("TableCreator given must not be null.");
    }
    this.tableCreator = producer;
  }

  /**
   * Receives notification that a page was canceled by the ReportProcessor. This method is
   * called, when a page was removed from the report after it was generated.
   *
   * @param event The event.
   */
  public void pageCanceled (final ReportEvent event)
  {
    // we are fairly sure, that our table report processor does not invoke this
    // method :)
  }

  /**
   * Receives notification that report generation initializes the current run. <P> The
   * event carries a ReportState.Started state.  Use this to initialize the report.
   *
   * @param event The event.
   */
  public void reportInitialized (final ReportEvent event)
  {
    if (getMaxWidth() == 0)
    {
      throw new IllegalStateException("Assert: TableWriter function was not initialized properly");
    }
  }

  public boolean isWatermarkSupported ()
  {
    return false;
  }

  public boolean printWatermark (final Band watermark)
          throws ReportProcessingException
  {
    throw new ReportProcessingException("Watermark printing is not supported for table targets.");
  }

  /**
   * This event is fired, whenever an automatic pagebreak has been detected and the report
   * state had been reverted to the previous state.
   *
   * @param event
   */
  public void pageRolledBack (final ReportEvent event)
  {
    // this method is left empty, we dont handle rollback events.
    // TableProcessors do not generate that event type ...
  }
}
