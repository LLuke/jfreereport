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
 * ---------------
 * ReportPane.java
 * ---------------
 * (C)opyright 2000-2003, by Object Refinery Limited.
 *
 * Original Author:  David Gilbert (for Object Refinery Limited);
 * Contributor(s):   Thomas Morgner;
 *
 * $Id: ReportPane.java,v 1.19 2005/02/23 19:31:46 taqua Exp $
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

package org.jfree.report.modules.gui.base;

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
import java.util.ArrayList;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.jfree.report.JFreeReport;
import org.jfree.report.PageDefinition;
import org.jfree.report.ReportProcessingException;
import org.jfree.report.event.RepaginationListener;
import org.jfree.report.event.RepaginationState;
import org.jfree.report.modules.output.pageable.base.OutputTarget;
import org.jfree.report.modules.output.pageable.base.OutputTargetException;
import org.jfree.report.modules.output.pageable.base.PageProcess;
import org.jfree.report.modules.output.pageable.base.PageableReportProcessor;
import org.jfree.report.modules.output.pageable.graphics.G2OutputTarget;
import org.jfree.report.util.Log;

/**
 * A panel used to display one page of a report. Works in tandem with a ReportPreviewFrame
 * to display a report.
 * <p/>
 * The panel uses several properties to inform its listeners of state changes.
 *
 * @author David Gilbert
 * @author Thomas Morgner
 */
public class ReportPane extends JComponent
        implements Printable, Pageable, RepaginationListener
{
  /**
   * Literal text for the 'paginated' property.
   */
  public static final String PAGINATED_PROPERTY = "Paginated";

  /**
   * Literal text for the 'NumberOfPages' property.
   */
  public static final String NUMBER_OF_PAGES_PROPERTY = "NumberOfPages";

  /**
   * Literal text for the 'pagenumber' property.
   */
  public static final String PAGENUMBER_PROPERTY = "PageNumber";

  /**
   * Literal text for the 'zoomfactor' property.
   */
  public static final String ZOOMFACTOR_PROPERTY = "ZoomFactor";

  /**
   * Literal text for the 'error' property.
   */
  public static final String ERROR_PROPERTY = "Error";

  /**
   * Literal text for the 'borderPainted' property.
   */
  public static final String BORDER_PROPERTY = "BorderPainted";

  public static final String PRINTING_PROPERTY = "printing";
  public static final String PAGINATING_PROPERTY = "paginating";

  /**
   * The size of the paper border.
   */
  private static final float PAPERBORDER_PIXEL = 6f;

  /**
   * The page cache contains one complete page. For performance reasons, I buffer the
   * rendered image and discard it when the page is changed or zooming.
   */
  private BufferedImage graphCache;

  /**
   * The current page number.
   */
  private int pageNumber;

  /**
   * The current zoom-factor.
   */
  private float zoomFactor;

  /**
   * The number of pages required for the report given the current page format.
   */
  private int pageCount;

  /**
   * A flag to indicate whether the border is painted or not.
   */
  private boolean borderPainted;

  /**
   * The last error that occurred while printing or painting the report.
   */
  private Exception error;

  /**
   * The report.
   */
  private JFreeReport report;

  protected static final int PAGINATE_IDLE = 0;
  protected static final int PAGINATE_PAGINATING = 1;
  protected static final int PAGINATE_PRINTING = 2;

  private class PropertyChangeRunnable implements Runnable
  {
    private String property;
    private Object oldValue;
    private Object newValue;

    public PropertyChangeRunnable (final String property,
                                   final Object oldValue,
                                   final Object newValue)
    {
      this.property = property;
      this.oldValue = oldValue;
      this.newValue = newValue;
    }

    /**
     * When an object implementing interface <code>Runnable</code> is used to create a
     * thread, starting the thread causes the object's <code>run</code> method to be
     * called in that separately executing thread.
     * <p/>
     * The general contract of the method <code>run</code> is that it may take any action
     * whatsoever.
     *
     * @see Thread#run()
     */
    public void run ()
    {
      ReportPane.this.firePropertyChange(this.property, this.oldValue, this.newValue);
    }
  }

  /**
   * A simple class performing the locking for huge paginating runs.
   */
  private static class PaginateLock
  {
    /**
     * The 'paginate' state.
     */
    private int paginateState;

    public PaginateLock ()
    {
      paginateState = PAGINATE_IDLE;
    }

    public synchronized boolean isIdle ()
    {
      return paginateState == PAGINATE_IDLE;
    }

    /**
     * Returns the 'paginate' state.
     *
     * @return the 'paginate' state.
     */
    public synchronized boolean isPaginating ()
    {
      return paginateState == PAGINATE_PAGINATING;
    }

    /**
     * Returns the 'paginate' state.
     *
     * @return the 'paginate' state.
     */
    public synchronized boolean isPrinting ()
    {
      return paginateState == PAGINATE_PRINTING;
    }

    /**
     * Sets the 'paginate' state.
     *
     * @param p the flag.
     */
    public synchronized void setPaginateState (final int p)
    {
      paginateState = p;
    }
  }

  /**
   * The local paginate lock instance.
   */
  private final PaginateLock paginateLock = new PaginateLock();

  /**
   * The report processor.
   */
  private PageableReportProcessor processor;

  private PageProcess pageProcess;

  /**
   * The repagination listeners.
   */
  private ArrayList repaginationListeners;
  /**
   * The registered repagination listeners as object array to gain performance.
   */
  private transient Object[] repaginationListenersCache;

  /**
   * Creates a report pane to display the specified report.
   *
   * @param report the report to display within the pane.
   * @throws ReportProcessingException if there was a problem processing the report.
   */
  public ReportPane (final JFreeReport report)
          throws ReportProcessingException
  {
    if (report == null)
    {
      throw new NullPointerException("Report is null.");
    }
    this.repaginationListeners = new ArrayList();
    this.report = report;
    setDoubleBuffered(true);
    processor = new PageableReportProcessor(report);
    processor.addRepaginationListener(this);

    borderPainted = false;
    pageNumber = 1;
    setZoomFactor(1.0f);
  }

  /**
   * Returns true if this component is completely opaque. This component is not opaque
   * while the report is repaginated.
   *
   * @return always false, as this component may refuse to draw anything if the report is
   *         currently being paginated.
   */
  public boolean isOpaque ()
  {
    return false;
  }

  /**
   * Returns whether the processor should check the threads interrupted state. If this is
   * set to true and the thread was interrupted, then the report processing is aborted.
   *
   * @return true, if the processor should check the current thread state, false
   *         otherwise.
   */
  public boolean isHandleInterruptedState ()
  {
    return processor.isHandleInterruptedState();
  }

  /**
   * Returns the report lock for the pagination. Use this to synchronize the various
   * operations.
   *
   * @return the report lock
   */
  public Object getReportLock ()
  {
    return paginateLock;
  }

  /**
   * Defines, whether the processor should check the threads interrupted state. If this is
   * set to true and the thread was interrupted, then the report processing is aborted.
   *
   * @param handleInterruptedState true, if the processor should check the current thread
   *                               state, false otherwise.
   */
  public void setHandleInterruptedState (final boolean handleInterruptedState)
  {
    processor.setHandleInterruptedState(handleInterruptedState);
  }

  /**
   * Returns the pageFormat for this Pageable Object. Currently no different pageformats
   * are supported within a single report, so this returns alway the same as
   * getPageFormat()
   *
   * @param page the page number.
   * @return the page format.
   */
  public PageFormat getPageFormat (final int page)
  {
    final JFreeReport report = getReport();
    final PageDefinition pdef = report.getPageDefinition();
    return pdef.getPageFormat(page % pdef.getPageCount());
  }

  /**
   * Returns a self-reference (this), as this object implements its own
   * Printable-Interface.
   *
   * @param page the page number.
   * @return this object (as an instance of Printable).
   */
  public Printable getPrintable (final int page)
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
    return (pageProcess != null);
  }

  /**
   * Returns the currently displayed page.
   *
   * @return the current page shown.
   */
  public int getPageNumber ()
  {
    return this.pageNumber;
  }

  /**
   * Gets the number of pages in the report. If the report is not yet paginated, the
   * pagination is done.
   *
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
  private void setCurrentPageCount (final int pc)
  {
    final int oldpc = pageCount;
    pageCount = pc;
    fireSynchronousPropertyChange(NUMBER_OF_PAGES_PROPERTY,
            new Integer(oldpc), new Integer(pc));
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
  public void setBorderPainted (final boolean b)
  {
    final boolean oldval = isBorderPainted();
    borderPainted = b;
    revalidate();
    firePropertyChange(BORDER_PROPERTY, oldval, b);
  }

  /**
   * Sets the page number to be displayed. If the page number is negaitive it is corrected
   * to 0. A page 0 is never printed.
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
      final int oldpage = pageNumber;
      pageNumber = page;
      graphCache = null;
      this.repaint();
      fireSynchronousPropertyChange(PAGENUMBER_PROPERTY,
              new Integer(oldpage), new Integer(page));
    }
  }

  /**
   * Sets the zoom factor for the pane.
   *
   * @param factor The new factor;
   */
  public void setZoomFactor (final float factor)
  {
    final double oldzoom = zoomFactor;
    zoomFactor = factor;
    graphCache = null;

    final PageFormat pageFormat = getCurrentPageFormat();
    final int w = (int) ((pageFormat.getWidth() + PAPERBORDER_PIXEL) * zoomFactor);
    final int h = (int) ((pageFormat.getHeight() + PAPERBORDER_PIXEL) * zoomFactor);
    super.setSize(w, h);
    firePropertyChange(ZOOMFACTOR_PROPERTY, new Double(oldzoom), new Double(factor));
  }

  protected PageFormat getCurrentPageFormat ()
  {

    final PageDefinition pdef = getReport().getPageDefinition();
    final int pageNumber;
    if (getPageNumber() == 0)
    {
      pageNumber = 0;
    }
    else
    {
      pageNumber = ((getPageNumber() - 1) % pdef.getPageCount());
    }
    return getPageFormat(pageNumber);
  }

  /**
   * Gets the currently set zoom factor.
   *
   * @return the zoom factor.
   */
  public float getZoomFactory ()
  {
    return zoomFactor;
  }

  /**
   * Returns the preferred size of the pane - used by the layout manager.
   *
   * @return The preferred size of the pane;
   */
  public Dimension getPreferredSize ()
  {
    return this.getSize();
  }

  /**
   * Returns the minimum size of the pane - used by the layout manager.
   *
   * @return The minimum size of the pane;
   */
  public Dimension getMinimumSize ()
  {
    return this.getSize();
  }

  /**
   * Paints the component, which means drawing the current page of the report.
   *
   * @param g the graphics device.
   */
  public void paintComponent (final Graphics g)
  {
    if (paginateLock.isPaginating())
    {
      // we do nothing if the report is currently paginated.
      // try again once the pagination is complete.
      return;
    }
    try
    {
      final Graphics2D g2org = (Graphics2D) g;

      if (graphCache == null)
      {
        final PageFormat pageFormat = getCurrentPageFormat();

        final Dimension size = getSize();

        final float outerX = 0;
        final float outerY = 0;

        final float realouterW = (float) size.getWidth() - 1;
        final float realouterH = (float) size.getHeight() - 1;

        final float outerW = (float) pageFormat.getWidth();
        final float outerH = (float) pageFormat.getHeight();

        final float innerX = (float) pageFormat.getImageableX();
        final float innerY = (float) pageFormat.getImageableY();
        final float innerW = (float) pageFormat.getImageableWidth();
        final float innerH = (float) pageFormat.getImageableHeight();

        //double paperBorder = paperBorderPixel * zoomFactor;

        final Graphics2D g2;
        if (realouterW > 1500 || realouterH > 1500)
        {
          graphCache = null;
          g2 = g2org;
        }
        else
        {
          graphCache =
          g2org.getDeviceConfiguration().createCompatibleImage((int) (realouterW),
                  (int) (realouterH));
          g2 = graphCache.createGraphics();
        }


        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        /** Prepare background **/
        Rectangle2D pageArea = new Rectangle2D.Float(outerX, outerY, realouterW, realouterH);
        g2.setPaint(getBackground());
        g2.fill(pageArea);
        g2.transform(AffineTransform.getScaleInstance(zoomFactor, zoomFactor));

        /** Prepare background **/
        pageArea = new Rectangle2D.Float(outerX, outerY, outerW - 2, outerH - 2);

        g2.setPaint(Color.white);
        g2.fill(pageArea);

        /** Generate the page ... */
        performGeneratePage(g2);

        /**
         * The border around the printable area is painted when the corresponding property is
         * set to true.
         */
        final Rectangle2D printingArea = new Rectangle2D.Float(innerX, innerY, innerW, innerH);

        /** Paint Page Shadow */
        final Rectangle2D southborder =
                new Rectangle2D.Float(outerX + PAPERBORDER_PIXEL - 2,
                        outerY + outerH - 2,
                        outerW,
                        PAPERBORDER_PIXEL);

        g2.setPaint(UIManager.getColor("controlShadow"));

        g2.fill(southborder);
        final Rectangle2D eastborder =
                new Rectangle2D.Float(outerW - 2,
                        outerY - 2 + PAPERBORDER_PIXEL,
                        PAPERBORDER_PIXEL,
                        outerH);

        g2.fill(eastborder);
        final Rectangle2D transPageArea;
        if (zoomFactor > 1.49)
        {
          transPageArea = new Rectangle2D.Float(outerX, outerY, outerW - 1, outerH - 1);
        }
        else
        {
          transPageArea = new Rectangle2D.Float(outerX, outerY, outerW - 2, outerH - 2);
        }

        g2.setPaint(Color.black);
        g2.draw(transPageArea);
        if (isBorderPainted())
        {
          g2.setPaint(Color.gray);
          g2.draw(printingArea);
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
        g2org.drawImage(graphCache,
                new AffineTransformOp(g2org.getDeviceConfiguration().getDefaultTransform(),
                        g2org.getRenderingHints()),
                0,
                0);
      }
      else
      {
        super.paintComponent(g);
      }
    }
    catch (Exception e)
    {
      setError(e);
      super.paintComponent(g);
    }
  }

  /**
   * Generates the page and draws that page on the given Graphics2D object.
   *
   * @param g2 the target graphics.
   * @throws OutputTargetException if an error occured.
   */
  private void performGeneratePage (final Graphics2D g2)
          throws OutputTargetException
  {
    try
    {
      synchronized (paginateLock)
      {
        if (isPrinting())
        {
          // we cannot print and paint at the same time.
          // and printing is much more important ...
          return;
        }
        repaginate();
      }
    }
    catch (ReportProcessingException rpe)
    {
      Log.error("Repaginate failed: ", rpe);
      setError(rpe);
    }
    synchronized (paginateLock)
    {
      final G2OutputTarget target = new G2OutputTarget(g2);
      target.open();
      getProcessor().setOutputTarget(target);

      final int pageNumber = getPageNumber();
      if (pageNumber > 0)
      {
        try
        {
          getProcessor().processPage(getPageProcess(), pageNumber - 1, true);
        }
        catch (ReportProcessingException rpe)
        {
          Log.error("Repaginate failed: ", rpe);
          setError(rpe);
        }
      }
      else
      {
        Log.error("PageNumber is invalid after repaginating: " + pageNumber);
      }
      target.close();
      getProcessor().setOutputTarget(null);
    }
  }

  /**
   * Supports the Printable interface by drawing a report on a single page.
   *
   * @param g         the graphics device.
   * @param pf        the page format.
   * @param pageIndex the page index.
   * @return PAGE_EXISTS if the page is rendered successfully or NO_SUCH_PAGE if
   *         <code>pageIndex</code> specifies a non-existent page.
   */
  public int print (final Graphics g, final PageFormat pf, final int pageIndex)
  {
    try
    {
      final Graphics2D g2 = (Graphics2D) g;
      final G2OutputTarget target = new G2OutputTarget(g2);

      synchronized (paginateLock)
      {
        getProcessor().setOutputTarget(target);
        target.open();
        repaginate();
        setPrinting(true);
      }

      if (pageIndex > pageCount - 1)
      {
        return NO_SUCH_PAGE;
      }

      getProcessor().processPage(getPageProcess(), pageIndex, true);
      getProcessor().setOutputTarget(null);
      target.close();
    }
    catch (OutputTargetException oe)
    {
      Log.error("Report generated an error", oe);
      setError(oe);
    }
    catch (ReportProcessingException rpe)
    {
      Log.error("Report generated an error", rpe);
      setError(rpe);
    }
    finally
    {
      synchronized (paginateLock)
      {
        setPrinting(true);
      }
    }
    return PAGE_EXISTS;
  }

  protected PageProcess getPageProcess ()
  {
    return pageProcess;
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
   * <p/>
   * This function synchronizes on the paginateLock. While a ReportPane is paginating, no
   * other pane may print.
   *
   * @throws ReportProcessingException if there is a problem processing the report.
   */
  public void repaginate ()
          throws ReportProcessingException
  {
    synchronized (paginateLock)
    {
      if (isPaginated())
      {
        // Is already done
        return;
      }
      if (isPaginating() || isPrinting())
      {
        throw new IllegalStateException("Already paginating or printing.");
      }

      setPaginated(false);
      setPaginating(true);
    }

    boolean addedOutputTarget = false;
    if (getProcessor().getOutputTarget() == null)
    {
      try
      {
        final OutputTarget target = new G2OutputTarget(G2OutputTarget.createEmptyGraphics());
        target.open();
        getProcessor().setOutputTarget(target);
      }
      catch (OutputTargetException oe)
      {
        // does not happen when using the dummy target, but just in case
        Log.error("Unable to repaginate: Error", oe);
      }
      addedOutputTarget = true;
    }

    try
    {
      processor.repaginate();
      final int pageCount = processor.getPageCount();
      setCurrentPageCount(pageCount);
      if (pageCount > 0)
      {
        setPageNumber(1);
      }
      else
      {
        setPageNumber(0);
      }

      synchronized (paginateLock)
      {
        setPaginated(true);
      }
    }
    finally
    {
      if (addedOutputTarget)
      {
        getProcessor().setOutputTarget(null);
      }
      synchronized (paginateLock)
      {
        setPaginating(false);
      }
    }
  }

  public void setPrinting (final boolean printing)
  {
    final boolean oldPrinting = isPrinting();
    if (isPaginating())
    {
      throw new IllegalStateException("Is currently paginating.");
    }
    synchronized (paginateLock)
    {
      if (printing)
      {
        paginateLock.setPaginateState(PAGINATE_PRINTING);
      }
      else
      {
        paginateLock.setPaginateState(PAGINATE_IDLE);
      }
    }
    firePropertyChange(PRINTING_PROPERTY, oldPrinting, printing);
  }

  protected void setPaginating (final boolean paginating)
  {
    final boolean oldPaginating = isPaginating();
    synchronized (paginateLock)
    {
      if (paginating)
      {
        paginateLock.setPaginateState(PAGINATE_PAGINATING);
      }
      else
      {
        paginateLock.setPaginateState(PAGINATE_IDLE);
      }
    }
    firePropertyChange(PAGINATING_PROPERTY, oldPaginating, paginating);
  }

  public boolean isPrinting ()
  {
    return paginateLock.isPrinting();
  }

  /**
   * sets the last error occurred. The error can be cleared with the clearError()
   * function.
   *
   * @param error the error.
   */
  public void setError (final Exception error)
  {
    final Exception oldError = this.error;
    this.error = error;
    firePropertyChange(ERROR_PROPERTY, oldError, error);
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
    setError(null);
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
  protected PageableReportProcessor getProcessor ()
  {
    return processor;
  }

  /**
   * Free some of the used memory. It is the duty of the caller to interrupt a possible
   * pagination process.
   */
  public void dispose ()
  {
    // Log.debug ("Dispose the report pane ...");
    // clean up a little bit
    // this is safe, the report is repaginated if needed
    setPaginated(false);
    // is regenerated on next repaint
    graphCache = null;
  }

  protected void setPaginated (final boolean paginated)
  {
    if (paginated == isPaginated())
    {
      return;
    }
    final boolean oldPaginated = isPaginated();
    if (paginated == true)
    {
      this.pageProcess = processor.createPageProcess();
    }
    else
    {
      this.pageProcess = null;
      processor.resetPagination();
    }
    firePropertyChange(PAGINATED_PROPERTY, oldPaginated, paginated);
  }

  /**
   * Receives notification of a repagination update. This method is public as an
   * implementation side effect.
   *
   * @param state the state.
   */
  public void repaginationUpdate (final RepaginationState state)
  {
    if (repaginationListenersCache == null)
    {
      repaginationListenersCache = repaginationListeners.toArray();
    }

    for (int i = 0; i < repaginationListenersCache.length; i++)
    {
      final RepaginationListener l = (RepaginationListener) repaginationListenersCache[i];
      l.repaginationUpdate(state);
    }
  }

  /**
   * Adds a repagination listener to this component. The listener will be informed about
   * the pagination progress.
   *
   * @param listener the listener to be added.
   */
  public void addRepaginationListener (final RepaginationListener listener)
  {
    if (listener == null)
    {
      throw new NullPointerException("Listener must not be null.");
    }
    repaginationListenersCache = null;
    repaginationListeners.add(listener);
  }

  /**
   * Removes the specified repagination listener from this component.
   *
   * @param listener the listener to be removed.
   */
  public void removeRepaginationListener (final RepaginationListener listener)
  {
    if (listener == null)
    {
      throw new NullPointerException("Listener must not be null.");
    }
    repaginationListenersCache = null;
    repaginationListeners.remove(listener);
  }

  protected void fireSynchronousPropertyChange (final String propertyName,
                                                final Object oldValue,
                                                final Object newValue)
  {
    final PropertyChangeRunnable runnable =
            new PropertyChangeRunnable(propertyName, oldValue, newValue);
    if (SwingUtilities.isEventDispatchThread())
    {
      runnable.run();
    }
    else
    {
      try
      {
        SwingUtilities.invokeAndWait(runnable);
      }
      catch (Exception e)
      {
        Log.error("Unable to propagate PropertyChange.", e);
      }
    }
  }

}
