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
 * ---------------
 * ReportPane.java
 * ---------------
 * (C)opyright 2000-2002, by Simba Management Limited.
 *
 * Original Author:  David Gilbert (for Simba Management Limited);
 * Contributor(s):   Thomas Morgner;
 *
 * $Id: ReportPane.java,v 1.37 2003/02/02 22:46:43 taqua Exp $
 *
 * Changes (from 8-Feb-2002)
 * -------------------------
 * 08-Feb-2002 : Updated code to work with latest version of the JCommon class library (DG);
 * 18-Feb-2002 : Multiple changes with introduction of XML format for report definition (DG);
 * 18-Apr-2002 : Caching painted graphics for performance reasons if the cached graphic is not
 *               greater than 1500 pixels. Provides propertyChangeEvents for zoom and paginating.
 * 10-May-2002 : Updated code to work with last changes in report processing.
 * 20-May-2002 : Adjusted to catch ReportProcessingException on processPage.
 * 26-May-2002 : Changed Repagination bahaviour. Implemented the Pageable-interface
 * 08-Jun-2002 : Documentation
 * 22-Aug-2002 : BugFix: Errors while processing the report did not trigger a PropertyChangeEvent
 * 01-Sep-2002 : BugFix: ZoomFactorProperty did not trigger a PropertyChangeEvent
 */

package com.jrefinery.report.preview;

import com.jrefinery.report.JFreeReport;
import com.jrefinery.report.ReportProcessingException;
import com.jrefinery.report.function.FunctionInitializeException;
import com.jrefinery.report.states.ReportState;
import com.jrefinery.report.targets.pageable.OutputTarget;
import com.jrefinery.report.targets.pageable.OutputTargetException;
import com.jrefinery.report.targets.pageable.PageableReportProcessor;
import com.jrefinery.report.targets.pageable.ReportStateList;
import com.jrefinery.report.targets.pageable.output.DummyOutputTarget;
import com.jrefinery.report.targets.pageable.output.G2OutputTarget;
import com.jrefinery.report.util.Log;

import javax.swing.JComponent;
import javax.swing.UIManager;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Pageable;
import java.awt.print.Printable;

/**
 * A panel used to display one page of a report. Works in tandem with a ReportPreviewFrame
 * to display a report.
 * <p>
 * The panel uses several properties to inform its listeners of state changes.
 *
 * @author David Gilbert
 * @author Thomas Morgner
 */
public class ReportPane extends JComponent implements Printable, Pageable
{
  /** Literal text for the 'paginated' property. */
  public static final String PAGINATED_PROPERTY = "paginated";

  /** Literal text for the 'NumberOfPages' property. */
  public static final String NUMBER_OF_PAGES_PROPERTY = "NumberOfPages";

  /** Literal text for the 'pagenumber' property. */
  public static final String PAGENUMBER_PROPERTY = "pagenumber";

  /** Literal text for the 'zoomfactor' property. */
  public static final String ZOOMFACTOR_PROPERTY = "zoomfactor";

  /** Literal text for the 'error' property. */
  public static final String ERROR_PROPERTY = "error";

  /** Literal text for the 'borderPainted' property. */
  public static final String BORDER_PROPERTY = "borderPainted";

  /** The size of the paper border. */
  private static final double PAPERBORDER_PIXEL = 6;

  /**
   * For performance reasons, I buffer the rendered image and
   * discard it when the page is changed or zooming
   */
  private BufferedImage graphCache;

  /** The current page number. */
  private int pageNumber;

  /** The current zoom-factor. */
  private double zoomFactor;

  /** The number of pages required for the report given the current page format. */
  private int pageCount;

  /** Storage for end-of-page state information. */
  private ReportStateList pageStateList;

  /** A flag to indicate whether the border is painted or not */
  private boolean borderPainted;

  /** The last error that occurred while printing or painting the report */
  private Exception error;

  /** The report. */
  private JFreeReport report;

  /**
   * A simple class performing the locking for huge paginating runs.
   */
  private static class PaginateLock
  {
    /** The 'paginate' state. */
    private boolean paginateState;

    /**
     * Returns the 'paginate' state.
     *
     * @return the 'paginate' state.
     */
    public boolean isPaginating ()
    {
      return paginateState;
    }

    /**
     * Sets the 'paginate' state.
     *
     * @param p  the flag.
     */
    public void setPaginating (boolean p)
    {
      paginateState = p;
    }
  }

  /** The local paginate lock instance */
  private PaginateLock paginateLock = new PaginateLock ();

  /** The report processor. */
  private PageableReportProcessor processor;

  /**
   * Creates a report pane to display the specified report.
   *
   * @param report  the report to display within the pane.
   *
   * @throws ReportProcessingException if there was a problem processing the report.
   */
  public ReportPane (JFreeReport report) throws ReportProcessingException
  {
    this.report = report;
    setDoubleBuffered (true);
    try
    {
      processor = new PageableReportProcessor(report);
    }
    catch (FunctionInitializeException fe)
    {
      throw new ReportProcessingException("unable to create the PageableReportProcessor", fe);
    }

    borderPainted = false;
    pageNumber = 1;
    setZoomFactor (1.0);
  }

  public boolean isHandleInterruptedState()
  {
    return processor.isHandleInterruptedState();
  }

  public void setHandleInterruptedState(boolean handleInterruptedState)
  {
    processor.setHandleInterruptedState (handleInterruptedState);
  }

  /**
   * Returns the current pageStates as a list.
   *
   * @return the report state list.
   */
  protected ReportStateList getPageStateList ()
  {
    return pageStateList;
  }

  /**
   * Sets the current page state list after the report has been repaginated.
   * Fires the paginating PropertyChangeEvent after the report is paginated.
   *
   * @param list  the report state list.
   */
  protected void setPageStateList (ReportStateList list)
  {
    ReportStateList oldList = pageStateList;
    if (oldList != null)
    {
      oldList.clear ();
    }

    this.pageStateList = list;
    firePropertyChange (PAGINATED_PROPERTY, oldList == null, list == null);
  }

  /**
   * Returns the page format.
   *
   * @return The current page format;
   */
  public PageFormat getPageFormat ()
  {
    return report.getDefaultPageFormat();
  }

  /**
   * Returns the pageFormat for this Pageable Object. Currently no different pageformats
   * are supported within a single report, so this returns alway the same as getPageFormat()
   *
   * @param page  the page number.
   *
   * @return the page format.
   */
  public PageFormat getPageFormat (int page)
  {
    return getPageFormat ();
  }

  /**
   * Returns a self-reference (this), as this object implements its own Printable-Interface
   *
   * @param page  the page number.
   *
   * @return this object (as an instance of Printable).
   */
  public Printable getPrintable (int page)
  {
    return this;
  }

  /**
   * Returns true, if this report has a pagestate list.
   *
   * @return true if the report has been paginated.
   */
  protected boolean isPaginated ()
  {
    return pageStateList != null;
  }

  /**
   * Sets the page format.
   *
   * @param pageFormat The new page format;
   *
   */
  public void setPageFormat (PageFormat pageFormat)
  {
    if (pageFormat == null)
    {
      throw new NullPointerException ("PageFormat must not be null");
    }

    this.report.setDefaultPageFormat(pageFormat);
    setPageStateList (null);
    graphCache = null;

    int w = (int) ((pageFormat.getWidth () + PAPERBORDER_PIXEL) * zoomFactor);
    int h = (int) ((pageFormat.getHeight () + PAPERBORDER_PIXEL) * zoomFactor);
    super.setSize (w, h);
  }

  /**
   * @return the current page shown
   */
  public int getPageNumber ()
  {
    return this.pageNumber;
  }

  /**
   * @return the page count for the current page settings.
   */
  public int getNumberOfPages ()
  {
    if (isPaginating())
    {
      // we are currently in the process of repagination, dont try to
      // repaginate ...
      return pageCount;
    }

    if (isPaginated() == false)
    {
      try
      {
        repaginate();
      }
      catch (Exception e)
      {
        this.pageCount = 0;
      }
    }
    return this.pageCount;
  }

  /**
   * defines the page count for the current page settings. This is set after the report
   * has been repaginated.
   *
   * @param pc the new pagecount.
   */
  private void setCurrentPageCount (int pc)
  {
    int oldpc = pageCount;
    pageCount = pc;
    firePropertyChange (NUMBER_OF_PAGES_PROPERTY, oldpc, pc);
  }

  /**
   * checkes whether a black border is painted around the printable page area.
   *
   * @return a flag indicating whether to paint the border.
   */
  public boolean isBorderPainted ()
  {
    return borderPainted;
  }

  /**
   * Defines whether a black border is painted around the printable page area.
   *
   * @param b a flag indicating whether to paint the border.
   */
  public void setBorderPainted (boolean b)
  {
    boolean oldval = isBorderPainted ();
    borderPainted = b;
    revalidate ();
    firePropertyChange (BORDER_PROPERTY, oldval, b);
  }

  /**
   * Sets the page number to be displayed.
   * If the page number is negaitive it is corrected to 0. A page 0 is never printed.
   *
   * @param page The new page number;
   */
  public void setPageNumber (int page)
  {
    if (page > getNumberOfPages())
    {
      return;
    }

    if (page < 1)
    {
      page = 0;
    }

    if (page <= getNumberOfPages())
    {
      int oldpage = pageNumber;
      pageNumber = page;
      graphCache = null;
      this.repaint ();
      firePropertyChange (PAGENUMBER_PROPERTY, oldpage, page);
    }
  }

  /**
   * Sets the zoom factor for the pane.
   * @param factor The new factor;
   *
   */
  public void setZoomFactor (double factor)
  {
    double oldzoom = zoomFactor;
    zoomFactor = factor;
    graphCache = null;

    PageFormat pageFormat = getPageFormat ();
    int w = (int) ((pageFormat.getWidth () + PAPERBORDER_PIXEL) * zoomFactor);
    int h = (int) ((pageFormat.getHeight () + PAPERBORDER_PIXEL) * zoomFactor);
    super.setSize (w, h);
    firePropertyChange (ZOOMFACTOR_PROPERTY, new Double (oldzoom), new Double (factor));
  }

  /**
   * Returns the preferred size of the pane - used by the layout manager.
   * @return The preferred size of the pane;
   *
   */
  public Dimension getPreferredSize ()
  {
    return this.getSize ();
  }

  /**
   * Returns the minimum size of the pane - used by the layout manager.
   * @return The minimum size of the pane;
   *
   */
  public Dimension getMinimumSize ()
  {
    return this.getSize ();
  }

  /**
   * Paints the component, which means drawing the current page of the report.
   *
   * @param g  the graphics device.
   *
   */
  public void paintComponent (Graphics g)
  {
    try
    {
      Graphics2D g2org = (Graphics2D) g;

      if (graphCache == null)
      {
        PageFormat pageFormat = getPageFormat ();

        Dimension size = getSize ();

        double outerX = 0;
        double outerY = 0;

        double realouterW = size.getWidth () - 1;
        double realouterH = size.getHeight () - 1;

        double outerW = pageFormat.getWidth ();
        double outerH = pageFormat.getHeight ();

        float innerX = (float) pageFormat.getImageableX ();
        float innerY = (float) pageFormat.getImageableY ();
        float innerW = (float) pageFormat.getImageableWidth ();
        float innerH = (float) pageFormat.getImageableHeight ();

        //double paperBorder = paperBorderPixel * zoomFactor;

        Graphics2D g2 = null;
        if (realouterW > 1500 || realouterH > 1500)
        {
          graphCache = null;
          g2 = g2org;
        }
        else
        {
          graphCache =
                  g2org.getDeviceConfiguration ().createCompatibleImage (
                          (int) (realouterW),
                          (int) (realouterH));
          g2 = graphCache.createGraphics ();
        }


        g2.setRenderingHint (
                RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        /** Prepare background **/
        Rectangle2D pageArea = new Rectangle2D.Double (outerX, outerY, realouterW, realouterH);
        g2.setPaint (getBackground ());
        g2.fill (pageArea);
        g2.transform (AffineTransform.getScaleInstance (zoomFactor, zoomFactor));

        /** Prepare background **/
        pageArea = new Rectangle2D.Double (outerX, outerY, outerW - 2, outerH - 2);

        g2.setPaint (Color.white);
        g2.fill (pageArea);

        /**
         * The border around the printable area is painted when the corresponding property is
         * set to true.
         */
        Rectangle2D printingArea = new Rectangle2D.Float (innerX, innerY, innerW, innerH);
        G2OutputTarget target = new G2OutputTarget(g2, getPageFormat());
        target.open();
        getProcessor().setOutputTarget(target);

        try
        {
          if (!isPaginated ())
          {
            repaginate ();
          }
        }
        catch (ReportProcessingException rpe)
        {
          Log.error ("Repaginate failed: ", rpe);
          setError (rpe);
        }

        int pageNumber = getPageNumber ();
        if (pageNumber > 0)
        {
          ReportState state = getPageStateList ().get (pageNumber - 1);
          try
          {
            getProcessor().processPage (state, target);
          }
          catch (ReportProcessingException rpe)
          {
            Log.error ("Repaginate failed: ", rpe);
            setError (rpe);
          }
        }
        else
        {
          Log.error ("PageNumber is invalid after repaginating: " + pageNumber);
        }

        getProcessor().setOutputTarget(null);
        target.close();

        /** Paint Page Shadow */
        Rectangle2D southborder =
                new Rectangle2D.Double (
                        outerX + PAPERBORDER_PIXEL - 2,
                        outerY + outerH - 2,
                        outerW,
                        PAPERBORDER_PIXEL);

        g2.setPaint (UIManager.getColor ("controlShadow"));

        g2.fill (southborder);
        Rectangle2D eastborder =
                new Rectangle2D.Double (
                        outerW - 2,
                        outerY - 2 + PAPERBORDER_PIXEL,
                        PAPERBORDER_PIXEL,
                        outerH);
        g2.fill (eastborder);
/*
        // Above Title
        Rectangle2D unprintArea =
                new Rectangle2D.Double (outerX, outerY, outerW - 2, (innerY - outerY));
        g2.setPaint (Color.white);
        g2.fill (unprintArea);

        // Under Footer
        unprintArea =
                new Rectangle2D.Double (
                        outerX,
                        innerY + innerH,
                        outerW - 2,
                        (outerH - innerY - innerH) - 2);
        g2.fill (unprintArea);

        // Left
        unprintArea = new Rectangle2D.Double (outerX, innerY, innerX, (innerH));
        g2.fill (unprintArea);

        // Right
        unprintArea =
                new Rectangle2D.Double (
                        (innerX + innerW),
                        innerY,
                        outerW - (innerX + innerW) - 2,
                        (innerH));
        g2.fill (unprintArea);
*/
        Rectangle2D transPageArea = null;
        if (zoomFactor > 1.49)
        {
          transPageArea = new Rectangle2D.Double (outerX, outerY, outerW - 1, outerH - 1);
        }
        else
        {
          transPageArea = new Rectangle2D.Double (outerX, outerY, outerW - 2, outerH - 2);
        }

        g2.setPaint (Color.black);
        g2.draw (transPageArea);
        if (isBorderPainted ())
        {
          g2.setPaint (Color.gray);
          g2.draw (printingArea);
        }

        // Dispose the temporary graphics object, it is no longer needed
        // if graphcache is null, the image is not cached, so do not dispose...
        if (graphCache != null)
        {
          g2.dispose();
        }
      }

      if (graphCache != null)
      {
        g2org.drawImage (
                graphCache,
                new AffineTransformOp (
                        g2org.getDeviceConfiguration ().getDefaultTransform (),
                        g2org.getRenderingHints ()),
                0,
                0);
      }
      else
      {
        super.paintComponent (g);
      }
    }
    catch (Exception e)
    {
      setError(e);
      super.paintComponent(g);
    }
  }

  /**
   * Supports the Printable interface by drawing a report on a single page.
   *
   * @param g  the graphics device.
   * @param pf  the page format.
   * @param pageIndex  the page index.
   *
   * @return PAGE_EXISTS if the page is rendered successfully
   *         or NO_SUCH_PAGE if <code>pageIndex</code> specifies a non-existent page.
   */
  public int print (Graphics g, PageFormat pf, int pageIndex)
  {
    Graphics2D g2 = (Graphics2D) g;

    try
    {
      G2OutputTarget target = new G2OutputTarget (g2, pf);
      getProcessor().setOutputTarget(target);
      target.open();
      if (!isPaginated ())
      {
        repaginate ();
      }

      if (pageIndex > pageCount - 1)
      {
        return NO_SUCH_PAGE;
      }

      ReportState state = getPageStateList ().get (pageIndex);
      getProcessor().processPage (state, target);
      getProcessor().setOutputTarget(null);
      target.close();
    }
    catch (OutputTargetException oe)
    {
      Log.error ("Report generated an error", oe);
      setError (oe);
    }
    catch (ReportProcessingException rpe)
    {
      Log.error ("Report generated an error", rpe);
      setError (rpe);
    }
    return PAGE_EXISTS;
  }

  /**
   * Returns true, if this ReportPane is currently paginating.
   *
   * @return true if the report pane is paginating.
   */
  public boolean isPaginating ()
  {
    return paginateLock.isPaginating();
  }

  /**
   * Repaginates this report according to the OutputTarget given.
   *
   * This function synchronizes on the paginateLock. While a ReportPane is paginating,
   * no other pane may print.
   *
   * @throws ReportProcessingException if there is a problem processing the report.
   */
  public void repaginate ()
          throws ReportProcessingException
  {
//    Log.error ("Why repaginate?", new Exception());
    if (isPaginated ())
    {
      // Is already done
      return;
    }
    if (isPaginating ())
    {
      throw new IllegalStateException ("Already paginating");
    }

    boolean addedOutputTarget = false;

    synchronized (paginateLock)
    {
      if (getProcessor().getOutputTarget() == null)
      {
        try
        {
          OutputTarget target = new DummyOutputTarget(new G2OutputTarget(G2OutputTarget.createEmptyGraphics(),
                                                                         getPageFormat()));
          target.open();
          getProcessor().setOutputTarget(target);
        }
        catch (OutputTargetException oe)
        {
          // does not happen when using the dummy target
        }
        addedOutputTarget = true;
      }

      paginateLock.setPaginating(true);
      setPageStateList (null);

      try
      {
        ReportStateList list = processor.repaginate ();
        int pageCount = 0;
        int pageNr = 0;
        if (list.size() > 0)
        {
          Number i = (Number) (list.get(0)).getProperty (JFreeReport.REPORT_PAGECOUNT_PROPERTY);
          if (i != null)
          {
            pageCount = i.intValue();
            pageNr = 1;
          }
        }
        setCurrentPageCount (pageCount);
        setPageNumber (pageNr);
        setPageStateList (list);
      }
      finally
      {
        if (addedOutputTarget)
        {
          getProcessor().setOutputTarget(null);
        }
        paginateLock.setPaginating (false);
      }
    }
  }

  /**
   * sets the last error occurred. The error can be cleared with the clearError() function.
   *
   * @param error  the error.
   */
  public void setError (Exception error)
  {
    Exception oldError = this.error;
    this.error = error;
    firePropertyChange (ERROR_PROPERTY, oldError, error);
  }

  /**
   * Checks whether an error occurred since the last call to clearError().
   *
   * @return a flag indicating whether or not here is an error.
   */
  public boolean hasError ()
  {
    return (error != null);
  }

  /**
   * Returns the report for this ReportPane.
   *
   * @return the report.
   */
  public JFreeReport getReport ()
  {
    return report;
  }

  /**
   * Clears the error state.
   */
  public void clearError ()
  {
    setError (null);
  }

  /**
   * Queries the error state for this ReportPane.
   *
   * @return the last error.
   */
  public Exception getError ()
  {
    return error;
  }

  /**
   * Returns the report processor.
   *
   * @return the report processor.
   */
  public PageableReportProcessor getProcessor()
  {
    return processor;
  }

  /** Free some of the used memory. */
  public void dispose ()
  {
    // clean up a little bit
    // this is safe, the report is repaginated if needed
    setPageStateList(null);
    // is regenerated on next repaint
    graphCache = null;
  }

  public static void main (String [] args) throws Exception
  {
    ReportPane pane = new ReportPane (new JFreeReport());
    Log.debug ("" + pane.getNumberOfPages());
  }
}
