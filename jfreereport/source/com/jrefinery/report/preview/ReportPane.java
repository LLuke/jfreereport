/**
 * =============================================================
 * JFreeReport - a Java report printing API;
 * =========================================
 * Version 0.50;
 * (C) Copyright 2000, Simba Management Limited;
 * Contact: David Gilbert (david.gilbert@bigfoot.com);
 *
 * This library is free software; you can redistribute it and/or modify it under the terms
 * of the GNU Lesser General Public License as published by the Free Software Foundation;
 * either version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this library;
 * if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston,
 * MA 02111-1307, USA.
 *
 * ReportHeader.java
 * -----------------
 * (C)opyright 2000-2002, by Simba Management Limited.
 *
 * Original Author:  David Gilbert (for Simba Management Limited);
 * Contributor(s):   -;
 *
 * $Id: ReportPane.java,v 1.4 2002/05/18 16:23:51 taqua Exp $
 * Changes (from 8-Feb-2002)
 * -------------------------
 * 08-Feb-2002 : Updated code to work with latest version of the JCommon class library (DG);
 * 18-Feb-2002 : Multiple changes with introduction of XML format for report definition (DG);
 * 18-Apr-2002 : Caching painted graphics for performance reasons if the cached graphic is not
 *               greater than 1500 pixels. Provides propertyChangeEvents for zoom and paginating.
 * 10-May-2002 : Updated code to work with last changes in report processing.
 * 20-May-2002 : Adjusted to catch ReportProcessingException on processPage.
 */

package com.jrefinery.report.preview;

import com.jrefinery.report.JFreeReport;
import com.jrefinery.report.ReportProcessingException;
import com.jrefinery.report.ReportProcessor;
import com.jrefinery.report.ReportState;
import com.jrefinery.report.targets.G2OutputTarget;
import com.jrefinery.report.targets.OutputTarget;
import com.jrefinery.report.util.Log;

import javax.swing.JComponent;
import javax.swing.UIManager;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;

/**
 * A panel used to display one page of a report. Works in tandem with a ReportPreviewFrame
 * to display a report.
 *
 */
public class ReportPane extends JComponent implements Printable
{
  public static final String PAGINATED_PROPERTY = "paginated";
  public static final String PAGECOUNT_PROPERTY = "pagecount";
  public static final String PAGENUMBER_PROPERTY = "pagenumber";
  public static final String ZOOMFACTOR_PROPERTY = "zoomfactor";
  public static final String ERROR_PROPERTY = "error";

  private double PaperBorderPixel = 6;

  /**
   * For performance reasons, I buffer the rendered image and
   * discard it when the page is changed or zooming
   */
  private BufferedImage graphCache;

  /** The report currently being displayed in the pane; */
  protected JFreeReport report;

  /** The page format corresponding to the current printer setup; */
  //	private PageFormat pageFormat;

  /** The current page number; */
  private int pageNumber;

  /** The current zoom-factor; */
  private double zoomFactor;

  /** A flag that indicates whether or not the report is paginated. */
  private boolean paginated;

  /** The number of pages required for the report given the current page format. */
  private int pageCount;

  /** Storage for end-of-page state information. */
  protected List pageStates;

  protected PropertyChangeSupport propsupp;

  /** The output target for drawing to the screen.*/
  protected G2OutputTarget target;

  private Exception error;

  /**
   * Standard constructor - builds a report pane to display the specified report.
   * @param report The report to display within the pane;
   * @param pageFormat The current page format (contains information about paper size etc);
   *
   */
  public ReportPane (JFreeReport report, G2OutputTarget target)
  {
    propsupp = new PropertyChangeSupport (this);
    this.target = target;
    this.report = report;
    pageNumber = 1;
    setZoomFactor (1.0);
    paginated = false;
    pageStates = new ArrayList ();
  }

  /**
   * Returns the page format.
   * @return The current page format;
   *
   */

  public PageFormat getPageFormat ()
  {
    return getOutputTarget ().getPageFormat ();
  }

  /**
   * Sets the page format.
   * @param pageFormat The new page format;
   *
   */
  public void setPageFormat (PageFormat pageFormat)
  {
    if (pageFormat == null)
      throw new NullPointerException ("PageFormat must not be null");

    getOutputTarget ().setPageFormat (pageFormat);
    paginated = false;
    int w = (int) (pageFormat.getWidth () * zoomFactor);
    int h = (int) (pageFormat.getHeight () * zoomFactor);
    graphCache = null;
    setSize (w, h);
  }

  /**
   * Returns the current page number.
   *
   */
  public int getPageNumber ()
  {
    return this.pageNumber;
  }

  public int getCurrentPageCount ()
  {
    return this.pageCount;
  }

  private void setCurrentPageCount (int pc)
  {
    int oldpc = pageCount;
    pageCount = pc;
    propsupp.firePropertyChange (PAGECOUNT_PROPERTY, oldpc, pc);
  }

  /**
   * Sets the page number to be displayed.
   * What happens when the page number is out of range?-Nothing!
   * @param page The new page number;
   *
   */
  public void setPageNumber (int page)
  {
    if (page > pageCount)
      return;

    if (page < 1)
    {
      page = 1;
    }

    if (page <= pageCount)
    {
      int oldpage = pageNumber;
      pageNumber = page;
      graphCache = null;
      this.repaint ();
      propsupp.firePropertyChange (PAGENUMBER_PROPERTY, oldpage, page);
    }
  }

  /**
   * Sets the zoom factor for the pane.
   * @param factor The new factor;
   *
   */
  public void setZoomFactor (double factor)
  {

    double oldzoom = factor;
    zoomFactor = factor;

    PageFormat pageFormat = getPageFormat ();
    int w = (int) ((pageFormat.getWidth () + PaperBorderPixel) * zoomFactor);
    int h = (int) ((pageFormat.getHeight () + PaperBorderPixel) * zoomFactor);
    graphCache = null;
    setSize (w, h);
    propsupp.firePropertyChange (ZOOMFACTOR_PROPERTY, new Double (oldzoom), new Double (factor));
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
   * @param g The graphics device;
   *
   */
  public void paintComponent (Graphics g)
  {
    Graphics2D g2org = (Graphics2D) g;

    if (graphCache == null)
    {
      PageFormat pageFormat = getPageFormat ();

      Dimension size = getSize ();
      Insets insets = getInsets ();

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

      double paperBorder = PaperBorderPixel * zoomFactor;

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

      target.setGraphics2D (g2);
      try
      {
        if (!paginated)
        {
          repaginate (g2);
        }
      }
      catch (ReportProcessingException rpe)
      {
        Log.error ("Repaginate failed: ", rpe);
        setError (rpe);
      }

      g2.setRenderingHint (
              RenderingHints.KEY_TEXT_ANTIALIASING,
              RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
      //g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
      //g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

      /** Prepare background **/
      Rectangle2D pageArea = new Rectangle2D.Double (outerX, outerY, realouterW, realouterH);
      g2.setPaint (getBackground ());
      g2.fill (pageArea);
      g2.transform (AffineTransform.getScaleInstance (zoomFactor, zoomFactor));

      /** Prepare background **/
      pageArea = new Rectangle2D.Double (outerX, outerY, outerW - 2, outerH - 2);

      g2.setPaint (Color.white);
      g2.fill (pageArea);
      Rectangle2D printingArea = new Rectangle2D.Float (innerX, innerY, innerW, innerH);
      g2.setPaint (Color.lightGray);
      g2.draw (printingArea);
      ReportState state = (ReportState) this.pageStates.get (pageNumber - 1);
      try
      {
        ReportState s2 = report.processPage (target, state, true);
      }
      catch (ReportProcessingException rpe)
      {
        Log.error ("Repaginate failed: ", rpe);
        setError (rpe);
      }

      /** Paint Page Shadow */
      Rectangle2D southborder =
              new Rectangle2D.Double (
                      outerX + PaperBorderPixel - 2,
                      outerY + outerH - 2,
                      outerW,
                      PaperBorderPixel);

      g2.setPaint (UIManager.getColor ("controlShadow"));

      g2.fill (southborder);
      Rectangle2D eastborder =
              new Rectangle2D.Double (
                      outerW - 2,
                      outerY - 2 + PaperBorderPixel,
                      PaperBorderPixel,
                      outerH);
      g2.fill (eastborder);

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
      g2.setPaint (Color.gray);
      g2.draw (printingArea);
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

  /** Supports the Printable interface by drawing a report on a single page. */
  public int print (Graphics g, PageFormat pf, int pageIndex)
  {
    Graphics2D g2 = (Graphics2D) g;

    try
    {
      if (!paginated)
      {
        repaginate (g2);
      }

      if (pageIndex > pageCount - 1)
        return NO_SUCH_PAGE;

      ReportState state = (ReportState) this.pageStates.get (pageIndex);
      G2OutputTarget target = new G2OutputTarget (g2, pf);
      report.processPage (target, state, true);
    }
    catch (ReportProcessingException rpe)
    {
      //Log.error ("Report generated an error", rpe);

      rpe.printStackTrace ();
      setError (rpe);
    }
    return PAGE_EXISTS;
  }

  /** Processes the entire report and records the state at the end of every page. */
  protected void repaginate (Graphics2D g2) throws ReportProcessingException
  {
    pageStates.clear ();
    ReportState state = new ReportState.Start (report);
    ReportProcessor prc = new ReportProcessor (getOutputTarget (), false, report.getPageFooter ());
    state = state.advance (prc);

    pageStates.add (state);
    state = report.processPage (target, state, false);
    while (!state.isFinish ())
    {
      pageStates.add (state);
      ReportState oldstate = state;
      state = report.processPage (target, state, false);

      if (!state.isProceeding (oldstate))
      {
        throw new ReportProcessingException ("State did not proceed, bailing out!");
      }
    }

    setCurrentPageCount (state.getCurrentPage () - 1);
    boolean oldpagination = paginated;
    paginated = true;
    propsupp.firePropertyChange (PAGINATED_PROPERTY, oldpagination, paginated);
  }

  public void addPropertyChangeListener (PropertyChangeListener l)
  {
    propsupp.addPropertyChangeListener (l);
  }

  public void addPropertyChangeListener (String propertyname, PropertyChangeListener l)
  {
    propsupp.addPropertyChangeListener (propertyname, l);
  }

  public void removePropertyChangeListener (PropertyChangeListener l)
  {
    propsupp.removePropertyChangeListener (l);
  }

  public void removePropertyChangeListener (String propertyname, PropertyChangeListener l)
  {
    propsupp.removePropertyChangeListener (propertyname, l);
  }

  private void setError (Exception error)
  {
    Exception oldError = error;
    this.error = error;
    propsupp.firePropertyChange (ERROR_PROPERTY, oldError, error);
  }

  public boolean hasError ()
  {
    return (error != null);
  }

  public JFreeReport getReport ()
  {
    return report;
  }

  public OutputTarget getOutputTarget ()
  {
    return target;
  }

  public void clearError ()
  {
    Exception oldError = error;
    error = null;
    propsupp.firePropertyChange (ERROR_PROPERTY, oldError, null);
  }

  public Exception getError ()
  {
    return error;
  }
}