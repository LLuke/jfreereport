/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
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
 * ----------------
 * TableWriter.java
 * ----------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: TableWriter.java,v 1.19 2003/05/02 12:40:37 taqua Exp $
 *
 * Changes
 * -------
 * 24-Jan-2003 : Initial version
 * 17-Feb-2003 : Documentation
 * 24-Feb-2003 : Fixed Checkstyle issues (DG);
 * 
 */
package com.jrefinery.report.targets.table;

import java.awt.geom.Rectangle2D;
import java.text.MessageFormat;
import java.util.ResourceBundle;

import com.jrefinery.report.Band;
import com.jrefinery.report.Group;
import com.jrefinery.report.event.ReportEvent;
import com.jrefinery.report.function.AbstractFunction;
import com.jrefinery.report.states.ReportState;
import com.jrefinery.report.targets.base.bandlayout.BandLayoutManagerUtil;
import com.jrefinery.report.targets.base.layout.DefaultLayoutSupport;
import com.jrefinery.report.targets.base.layout.LayoutSupport;
import com.jrefinery.report.targets.style.BandStyleSheet;

/**
 * The TableWriter is the content creation function used to collect the cell data.
 * After the layouting is done, the layouted bands are forwarded to the TableProducer.
 * The virtual page has an unlimited size, only when a manual pagebreak is encountered,
 * a new page is started.
 * <p>
 * This can be used to f.i. to create separate sheets in Excel-Workbooks, the detailed
 * semantics depend on concrete implementation of the TableProducer.
 * <p>
 * This writer is not thread-safe.
 * 
 * @author Thomas Morgner
 */
public class TableWriter extends AbstractFunction
{
  /**
   * The SheetName-function property, defines the name of an StringFunction
   * that creates the sheet names.
   */
  public static final String SHEET_NAME_FUNCTION_PROPERTY =
      "com.jrefinery.report.targets.table.TableWriter.SheetNameFunction";

  /** The base class for localised resources. */
  public static final String BASE_RESOURCE_CLASS =
      "com.jrefinery.report.resources.JFreeReportResources";

  /** The current event, stored on every call to one of the ReportListener methods. */
  private ReportEvent currentEvent;
  
  /** The table producer used to create the layout. */
  private TableProducer producer;
  
  /** the cursor pointing to the current position on the sheet. */
  private TableWriterCursor cursor;

  /** the maximum width, required for the BandLayout. */
  private float maxWidth;
  
  /** the dependency level for this function, usually -1. */
  private int depLevel;
  
  /** A flag indicating whether the writer is currently handling the end of an page. */
  private boolean inEndPage;
  
  /** A flag indicating whether the current page is empty. */
  private boolean isPageEmpty;
  
  /** A flag that indicates that the current pagebreak will be the last one. */
  private boolean isLastPageBreak;

  /** Locale-specific resources. */
  private ResourceBundle resources;

  /**
   * Creates a new TableWriter. The dependency level is set to -1 and the maxwidth
   * is defined to be 1000.
   */
  public TableWriter()
  {
    setDependencyLevel(-1);
    setMaxWidth(1000);
  }

  /**
   * Gets the name of the SheetName function. The sheetname function defines the
   * names of the generated sheets.
   *
   * @return the name of the sheet name function, or null, if that name is not known yet.
   */
  private String getSheetNameFunction()
  {
    if (getCurrentEvent() == null)
    {
      return null;
    }
    return getCurrentEvent().getReport().getReportConfiguration()
        .getConfigProperty(SHEET_NAME_FUNCTION_PROPERTY);
  }

  /**
   * Returns true, if the tablewriter is currently handling the end of the current page.
   *
   * @return true, if the end of the page is currently handled.
   */
  private boolean isInEndPage()
  {
    return inEndPage;
  }

  /**
   * Gets the cursor for this writer. The cursor marks the current position in the
   * current sheet.
   *
   * @return the cursor.
   */
  private TableWriterCursor getCursor()
  {
    return cursor;
  }

  /**
   * Sets the cursor for the writer.
   * @param cursor the new cursor.
   */
  private void setCursor(TableWriterCursor cursor)
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
  private LayoutSupport getLayoutSupport()
  {
    return DefaultLayoutSupport.getDefaultInstance();
  }

  /**
   * Return the current expression value.
   * <P>
   * The value depends (obviously) on the expression implementation.
   *
   * @return the value of the function.
   */
  public Object getValue()
  {
    return this;
  }

  /**
   * Gets the maximum width available for a root band during the layouting process.
   *
   * @return the maximum width for a root band.
   */
  public float getMaxWidth()
  {
    return maxWidth;
  }

  /**
   * Defines the maximum width available for a root band during the layouting process.
   *
   * @param width the maximum width for a root band.
   */
  public void setMaxWidth(float width)
  {
    maxWidth = width;
  }

  /**
   * Perform the layout of a band. The height of the band is calculated according to the contents
   * of the band.  The width of the band will always span the complete printable width.
   *
   * @param band  the band.
   *
   * @return the dimensions of the band.
   */
  private Rectangle2D doLayout(Band band)
  {
    // in this layouter the width of a band is always the full page width.
    // the height is not limited ...
    float width = getMaxWidth();
    float height = Short.MAX_VALUE;

    Rectangle2D bounds = BandLayoutManagerUtil.doLayout(band,
                                          getLayoutSupport(),
                                          width,
                                          height);
    getCurrentEvent().getState().fireLayoutCompleteEvent(band);
    return bounds;
  }

  /**
   * Forwards the given band to the TableProducer. This will create the content
   * and will add the TableCellData object to the grid.
   *
   * @see TableProducer#processBand
   * @param bounds the bounds of the band, defines the position of the printed band within
   * the sheet.
   * @param band the band that should be printed.
   */
  private void doPrint (Rectangle2D bounds, Band band)
  {
    int cellCount = producer.getCellCount();

    // now print the band ...
    producer.processBand(bounds, band);
    getCursor().advance((float) bounds.getHeight());
    if (cellCount < producer.getCellCount())
    {
      // something was printed ...
      isPageEmpty = false;
    }
  }


  /**
   * Ends the current page. Fires the PageFinished event.
   */
  private void endPage ()
  {
    if (inEndPage == true)
    {
      throw new IllegalStateException ("Already in startPage or endPage");
    }
    inEndPage = true;

    ReportEvent currentEvent = getCurrentEvent();
    ReportState cEventState = getCurrentEvent().getState();
    cEventState.firePageFinishedEvent();
    cEventState.nextPage();
    setCurrentEvent(currentEvent);
    inEndPage = false;
  }

  /**
   * Starts a new page. Fires the PageStarted event.
   */
  public void startPage ()
  {
    if (inEndPage == true)
    {
      throw new IllegalStateException ("Already in startPage or endPage");
    }
    inEndPage = true;

    ReportEvent currentEvent = getCurrentEvent();
    ReportState cEventState = getCurrentEvent().getState();
    cEventState.firePageStartedEvent();
    setCurrentEvent(currentEvent);
    inEndPage = false;
    isPageEmpty = true;
  }

  /**
   * Performs the band layout and prints the band.
   *
   * @param b  the band.
   */
  protected void print(Band b)
  {
    if (!isInEndPage() && (isPageEmpty == false) 
        && b.getStyle().getBooleanStyleProperty(BandStyleSheet.PAGEBREAK_BEFORE) == true)
    {
      endPage();
      startPage();
    }

    float y = getCursor().getY();
    // don't save the state if the current page is currently being finished
    // or restarted; PageHeader and PageFooter are printed out of order and
    // do not influence the reporting state

    Rectangle2D bounds = doLayout(b);
    bounds.setRect(0, y, bounds.getWidth(), bounds.getHeight());
    doPrint(bounds, b);

    if (!isInEndPage() && (isPageEmpty == false) 
        && b.getStyle().getBooleanStyleProperty(BandStyleSheet.PAGEBREAK_AFTER) == true)
    {
      endPage();
      startPage();
    }
  }

  /**
   * The dependency level defines the level of execution for this function. Higher dependency 
   * functions are executed before lower dependency functions. For ordinary functions and 
   * expressions, the range for dependencies is defined to start from 0 (lowest dependency 
   * possible to 2^31 (upper limit of int).
   * <p>
   * PageLayouter functions override the default behaviour an place them self at depency level -1,
   * an so before any userdefined function.
   *
   * @return the level.
   */
  public int getDependencyLevel()
  {
    return depLevel;
  }

  /**
   * Overrides the depency level. Should be lower than any other function depency.
   * @param deplevel the new depency level.
   */
  public void setDependencyLevel(int deplevel)
  {
    this.depLevel = deplevel;
  }

  /**
   * Receives notification that the report has started.
   *
   * @param event  the event.
   */
  public void reportStarted(ReportEvent event)
  {
    setCurrentEvent(event);

    producer.open();

    startPage();
    print(event.getReport().getReportHeader());
  }

  /**
   * Receives notification that the report has finished.
   *
   * @param event  the event.
   */
  public void reportFinished(ReportEvent event)
  {
    isLastPageBreak = true;
    setCurrentEvent(event);
    print(event.getReport().getReportFooter());
    endPage();
    producer.close();
  }

  /**
   * Retrieves the resources for this PreviewFrame. If the resources are not initialized,
   * they get loaded on the first call to this method.
   *
   * @return this frames ResourceBundle.
   */
  public ResourceBundle getResources()
  {
    if (resources == null)
    {
      resources = ResourceBundle.getBundle(BASE_RESOURCE_CLASS);
    }
    return resources;
  }

  /**
   * Prints the PageHeader and all repeating group headers.
   *
   * @param event  the event.
   */
  public void pageStarted(ReportEvent event)
  {
    setCurrentEvent(event);
    // a new page has started, so reset the cursor ...
    setCursor(new TableWriterCursor());

    // is paginated ...
     Object[] params = new Object[]{
       new Integer(event.getState().getCurrentPage())
     };
     String sheetName =
         MessageFormat.format(
             getResources().getString("tabletarget.page"),
             params
         );

    if (getSheetNameFunction() != null)
    {
      sheetName = String.valueOf(getDataRow().get(getSheetNameFunction()));
    }
    producer.beginPage(sheetName);

    Band b = event.getReport().getPageHeader();
    if (event.getState().getCurrentPage() == 1)
    {
      if (b.getStyle().getBooleanStyleProperty(BandStyleSheet.DISPLAY_ON_FIRSTPAGE) == true)
      {
        print(b);
      }
    }
    else if (isLastPageBreak)
    {
      if (b.getStyle().getBooleanStyleProperty(BandStyleSheet.DISPLAY_ON_LASTPAGE) == true)
      {
        print(b);
      }
    }
    else
    {
      print(b);
    }

    /**
     * Repeating group header are only printed while ItemElements are
     * processed.
     */
    for (int gidx = event.getState().getCurrentGroupIndex(); gidx >= 0; gidx--)
    {
      Group g = event.getReport().getGroup(gidx);
      if (g.getHeader().getStyle().getBooleanStyleProperty(BandStyleSheet.REPEAT_HEADER))
      {
        print(g.getHeader());
      }
    }
  }

  /**
   * Prints the page footer.
   *
   * @param event  the event.
   */
  public void pageFinished(ReportEvent event)
  {
    setCurrentEvent(event);
    Band b = event.getReport().getPageFooter();
    if (event.getState().getCurrentPage() == 1)
    {
      if (b.getStyle().getBooleanStyleProperty(BandStyleSheet.DISPLAY_ON_FIRSTPAGE) == true)
      {
        print(b);
      }
    }
    else
    if (isLastPageBreak)
    {
      if (b.getStyle().getBooleanStyleProperty(BandStyleSheet.DISPLAY_ON_LASTPAGE) == true)
      {
        print(b);
      }
    }
    else
    {
      print(b);
    }

    producer.endPage();
  }

  /**
   * Prints the group header for the current group.
   *
   * @param event  the event.
   */
  public void groupStarted(ReportEvent event)
  {
    setCurrentEvent(event);
    int gidx = event.getState().getCurrentGroupIndex();
    Group g = event.getReport().getGroup(gidx);
    Band b = g.getHeader();
    print(b);
  }

  /**
   * Prints the group footer for the current group.
   *
   * @param event  the event.
   */
  public void groupFinished(ReportEvent event)
  {
    setCurrentEvent(event);
    int gidx = event.getState().getCurrentGroupIndex();
    Group g = event.getReport().getGroup(gidx);
    Band b = g.getFooter();
    print(b);
  }

  /**
   * Prints the itemband.
   *
   * @param event  the event.
   */
  public void itemsAdvanced(ReportEvent event)
  {
    setCurrentEvent(event);
    print(event.getReport().getItemBand());
  }

  /**
   * Handles the start of the item processing.
   * <P>
   * The next events will be itemsAdvanced events until the itemsFinished event is raised.
   *
   * @param event The event.
   */
  public void itemsStarted(ReportEvent event)
  {
    setCurrentEvent(event);
  }

  /**
   * Handles the end of the item processing.
   * <P>
   * The itemBand is finished, the report starts to close open groups.
   *
   * @param event The event.
   */
  public void itemsFinished(ReportEvent event)
  {
    // this event does nothing
    setCurrentEvent(event);
  }

  /**
   * Returns the current event, which has been updated at the start of every
   * ReportListener method.
   *
   * @return the current event.
   */
  public ReportEvent getCurrentEvent()
  {
    return currentEvent;
  }

  /**
   * Defines the current event, which must be updated at the start of every
   * ReportListener method.
   *
   * @param currentEvent the current event.
   */
  public void setCurrentEvent(ReportEvent currentEvent)
  {
    this.currentEvent = currentEvent;
  }

  /**
   * Gets the TableProducer.
   *
   * @return the table producer that should be used to create the TableCellData.
   */
  public TableProducer getProducer()
  {
    return producer;
  }

  /**
   * Sets the TableProducer, that should be used to create the TableCellData.
   *
   * @param producer the table producer that should be used to create the TableCellData.
   */
  public void setProducer(TableProducer producer)
  {
    this.producer = producer;
  }
}
