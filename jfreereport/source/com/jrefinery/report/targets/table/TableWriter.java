/**
 * Date: Jan 14, 2003
 * Time: 2:32:12 PM
 *
 * $Id: TableWriter.java,v 1.3 2003/01/29 03:13:04 taqua Exp $
 */
package com.jrefinery.report.targets.table;

import com.jrefinery.report.Band;
import com.jrefinery.report.Group;
import com.jrefinery.report.ReportProcessingException;
import com.jrefinery.report.event.ReportEvent;
import com.jrefinery.report.function.AbstractFunction;
import com.jrefinery.report.function.FunctionProcessingException;
import com.jrefinery.report.states.ReportState;
import com.jrefinery.report.targets.DefaultLayoutSupport;
import com.jrefinery.report.targets.FloatDimension;
import com.jrefinery.report.targets.LayoutSupport;
import com.jrefinery.report.targets.base.bandlayout.BandLayoutManager;
import com.jrefinery.report.targets.base.bandlayout.BandLayoutManagerUtil;
import com.jrefinery.report.targets.style.BandStyleSheet;
import com.jrefinery.report.targets.style.ElementStyleSheet;
import com.jrefinery.report.util.Log;

import java.awt.geom.Dimension2D;
import java.awt.geom.Rectangle2D;

public class TableWriter extends AbstractFunction
{
  public static final String SHEET_NAME_FUNCTION_PROPERTY =
      "com.jrefinery.report.targets.table.TableWriter.SheetNameFunction";

  private LayoutSupport layoutSupport;
  private ReportEvent currentEvent;
  private TableProducer producer;
  private TableWriterCursor cursor;

  private float maxWidth;
  private int depLevel;
  private boolean inEndPage;
  private boolean isPageEmpty;


  /** A flag that indicates if the report state is currently inside the item group. */
  private boolean isInItemGroup;

  /** A flag that indicates that the current pagebreak will be the last one. */
  private boolean isLastPageBreak;

  public TableWriter()
  {
    setDependencyLevel(-1);
    setMaxWidth(1000);
  }

  public String getSheetNameFunction()
  {
    if (getCurrentEvent() == null)
      return null;
    return getCurrentEvent().getReport().getReportConfiguration().getConfigProperty(SHEET_NAME_FUNCTION_PROPERTY);
  }

  public boolean isInEndPage()
  {
    return inEndPage;
  }

  public TableWriterCursor getCursor()
  {
    return cursor;
  }

  public void setCursor(TableWriterCursor cursor)
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
    if (layoutSupport == null)
    {
      layoutSupport = new DefaultLayoutSupport();
    }
    return layoutSupport;
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

  public float getMaxWidth()
  {
    return maxWidth;
  }

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
  protected Rectangle2D doLayout(Band band)
  {
    BandLayoutManager lm
        = BandLayoutManagerUtil.getLayoutManager(band, getLayoutSupport());

    // in this layouter the width of a band is always the full page width.
    // the height is not limited ...
    float width = getMaxWidth();
    Dimension2D fdim = lm.preferredLayoutSize(band, new FloatDimension(width, Short.MAX_VALUE));

    // the height is now defined by the band's requirements
    float height = (float) fdim.getHeight();
    Rectangle2D bounds = new Rectangle2D.Float(0, 0, width, height);
    band.getStyle().setStyleProperty(ElementStyleSheet.BOUNDS, bounds);
    lm.doLayout(band);
    return bounds;
  }

  protected void doPrint (Rectangle2D bounds, Band band)
  {
    int cellCount = producer.getCellCount();

    // now print the band ...
    producer.processBand(bounds, band);
    getCursor().advance((float) bounds.getHeight());
    if (cellCount < producer.getCellCount())
    {
      isPageEmpty = false;
    }
  }


  public void endPage ()
  {
    Log.debug ("EndPage;" + isPageEmpty + " + " + getCurrentEvent().getState());

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
   * Prints a band.
   *
   * @param b  the band.
   *
   * @throws com.jrefinery.report.ReportProcessingException if the printing failed
   */
  protected void print(Band b)
      throws ReportProcessingException
  {
    if (!isInEndPage() && (isPageEmpty == false) &&
        b.getStyle().getBooleanStyleProperty(BandStyleSheet.PAGEBREAK_BEFORE) == true)
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

    if (!isInEndPage() && (isPageEmpty == false) &&
        b.getStyle().getBooleanStyleProperty(BandStyleSheet.PAGEBREAK_AFTER) == true)
    {
      Log.debug  ("PAGEBREAK AFTER PRINT");
      endPage();
      startPage();
    }
  }

  /**
   * The dependency level defines the level of execution for this function. Higher dependency functions
   * are executed before lower dependency functions. For ordinary functions and expressions,
   * the range for dependencies is defined to start from 0 (lowest dependency possible)
   * to 2^31 (upper limit of int).
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
    try
    {
      setCurrentEvent(event);
      Log.debug ("CurrentEvent: " + event.getState());

      producer.open();

      startPage();
      print(event.getReport().getReportHeader());
    }
    catch (Exception e)
    {
      throw new FunctionProcessingException("ReportHeader Failed", e);
    }
  }

  /**
   * Receives notification that the report has finished.
   *
   * @param event  the event.
   */
  public void reportFinished(ReportEvent event)
  {
    try
    {
      isLastPageBreak = true;
      setCurrentEvent(event);
      print(event.getReport().getReportFooter());
      endPage();
      producer.close();
    }
    catch (Exception e)
    {
      throw new FunctionProcessingException("ReportFooter Failed", e);
    }
  }

  /**
   * Receives notification that a page has started.
   *
   * @param event  the event.
   */
  public void pageStarted(ReportEvent event)
  {
    setCurrentEvent(event);
    try
    {
      // a new page has started, so reset the cursor ...
      setCursor(new TableWriterCursor());

      String sheetName = null;
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
      if (isInItemGroup)
      {
        int gidx = event.getState().getCurrentGroupIndex();
        while (gidx >= 0)
        {
          Group g = null;
          if (gidx >= 0)
          {
            g = event.getReport().getGroup(gidx);
          }
          if (g.getHeader().getStyle().getBooleanStyleProperty(BandStyleSheet.REPEAT_HEADER))
          {
            print(g.getHeader());
            break;
          }
          else
          {
            gidx--;
          }
        }
      }
    }
    catch (Exception e)
    {
      throw new FunctionProcessingException("PageHeader", e);
    }
  }

  /**
   * Receives notification that a page has ended.
   *
   * @param event  the event.
   */
  public void pageFinished(ReportEvent event)
  {
    setCurrentEvent(event);
    try
    {
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
   * Receives notification that a group has started.
   *
   * @param event  the event.
   */
  public void groupStarted(ReportEvent event)
  {
    try
    {
      setCurrentEvent(event);
      int gidx = event.getState().getCurrentGroupIndex();
      Group g = event.getReport().getGroup(gidx);
      Band b = g.getHeader();
      print(b);
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
   *
   * @param event  the event.
   */
  public void groupFinished(ReportEvent event)
  {
    try
    {
      setCurrentEvent(event);
      int gidx = event.getState().getCurrentGroupIndex();
      Group g = event.getReport().getGroup(gidx);
      Band b = g.getFooter();
      print(b);
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
   *
   * @param event  the event.
   */
  public void itemsAdvanced(ReportEvent event)
  {
    try
    {
      setCurrentEvent(event);
      print(event.getReport().getItemBand());
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
   * Receives notification that a group of item bands is about to be processed.
   * <P>
   * The next events will be itemsAdvanced events until the itemsFinished event is raised.
   *
   * @param event The event.
   */
  public void itemsStarted(ReportEvent event)
  {
    setCurrentEvent(event);
    isInItemGroup = true;
    // this event does nothing
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
    // this event does nothing
    setCurrentEvent(event);
    isInItemGroup = false;
  }

  public ReportEvent getCurrentEvent()
  {
    return currentEvent;
  }

  public void setCurrentEvent(ReportEvent currentEvent)
  {
    this.currentEvent = currentEvent;
  }

  public TableProducer getProducer()
  {
    return producer;
  }

  public void setProducer(TableProducer producer)
  {
    this.producer = producer;
  }
}
