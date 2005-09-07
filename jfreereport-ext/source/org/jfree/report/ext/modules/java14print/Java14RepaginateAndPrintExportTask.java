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
 * ------------------------------
 * Java14PrintExportTask.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: Java14PrintExportTask.java,v 1.3 2003/12/21 23:49:23 taqua Exp $
 *
 * Changes 
 * -------------------------
 * 20.09.2003 : Initial version
 *  
 */

package org.jfree.report.ext.modules.java14print;

import java.awt.print.PageFormat;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.SimpleDoc;
import javax.print.attribute.PrintRequestAttributeSet;

import org.jfree.report.ReportProcessingException;
import org.jfree.report.modules.gui.base.ExportTask;
import org.jfree.report.modules.gui.base.PreviewProxyBase;
import org.jfree.report.modules.gui.base.ReportPane;
import org.jfree.report.modules.gui.base.ReportProgressDialog;
import org.jfree.util.Log;

public class Java14RepaginateAndPrintExportTask extends ExportTask
{

  /**
   * A listener that waits for the repagination to be finished.
   */
  private class ReportPaneUpdateListener implements PropertyChangeListener
  {
    public ReportPaneUpdateListener()
    {
    }

    /**
     * This method gets called when a bound property is changed.
     *
     * @param evt A PropertyChangeEvent object describing the event source and the
     *            property that has changed.
     */
    public void propertyChange (final PropertyChangeEvent evt)
    {
      if (PreviewProxyBase.REPORT_PANE_PROPERTY.equals(evt.getPropertyName()))
      {
        // when printing, this method must be executed on a thread other
        // than the current worker - or weird things may happen.
        Log.debug ("Got a property change ...");
        synchronized(Java14RepaginateAndPrintExportTask.this)
        {
          Log.debug ("Got a property change (lock aquired)...");
          reportPaneUpdated = true;
          Java14RepaginateAndPrintExportTask.this.notifyAll();
        }
      }
    }
  }

  /**
   * A listener that waits for the repagination to be finished.
   */
  private class RepaginationCompleteListener implements PropertyChangeListener
  {
    private ReportPane reportPane;

    public RepaginationCompleteListener(final ReportPane reportPane)
    {
      if (reportPane == null)
      {
        throw new NullPointerException("ReportPane cannot be null.");
      }
      this.reportPane = reportPane;
    }

    public ReportPane getReportPane()
    {
      return reportPane;
    }

    /**
     * This method gets called when a bound property is changed.
     *
     * @param evt A PropertyChangeEvent object describing the event source and the
     *            property that has changed.
     */
    public void propertyChange (final PropertyChangeEvent evt)
    {
      if (ReportPane.PAGINATED_PROPERTY.equals(evt.getPropertyName()))
      {
        // when printing, this method must be executed on a thread other
        // than the current worker - or weird things may happen.
        Log.debug ("Got a property change (Paginated) ...");
        synchronized(reportPane.getReportLock())
        {
          Log.debug ("Got a property change (lock aquired)...");
          paginated = true;
          reportPane.getReportLock().notifyAll();
        }
      }
    }
  }

  private PrintService service;
  private PreviewProxyBase proxyBase;
  private PrintRequestAttributeSet attributes;
  private ReportProgressDialog progressDialog;
  private boolean reportPaneUpdated;
  private boolean paginated;

  public Java14RepaginateAndPrintExportTask
          (final ReportProgressDialog progressDialog,
           final PrintService service,
           final PreviewProxyBase proxyBase,
           final PrintRequestAttributeSet attributes)
  {
    this.service = service;
    this.proxyBase = proxyBase;
    this.attributes = attributes;
    this.progressDialog = progressDialog;
  }

  /**
   * Performs the export to the java1.4 print system.
   */
  protected void performExport()
  {
    RepaginationCompleteListener repaginateListener = null;
    final ReportPaneUpdateListener updateListener =
            new ReportPaneUpdateListener();
    try
    {
      progressDialog.setModal(false);
      progressDialog.setVisible(true);
      proxyBase.setLockInterface(true);
      proxyBase.addPropertyChangeListener
              (PreviewProxyBase.REPORT_PANE_PROPERTY, updateListener);
      // from attributes ..
      final PageFormat pageFormat = new PageFormat();
      synchronized(this)
      {
        Log.debug ("Updated the proxy base.");
        proxyBase.updatePageFormat(pageFormat);
        while (reportPaneUpdated == false)
        {
          try
          {
            Log.debug ("Waiting ...");
            wait();
          }
          catch (InterruptedException e)
          {
            Log.debug ("Interrupted while waiting for repagination.");

          }
        }
      }
      Log.debug ("proxybase update complete.");

      // now, as the report pane has been updated, we can safely wait for the
      // repagination ..
      final ReportPane reportPane = proxyBase.getReportPane();
      repaginateListener = new RepaginationCompleteListener(reportPane);
      reportPane.addPropertyChangeListener
              (ReportPane.PAGINATED_PROPERTY, repaginateListener);

      synchronized(reportPane.getReportLock())
      {
        Log.debug ("Waiting for repagination complete event.");
        while (reportPane.isPaginated() == false)
        {
          try
          {
            Log.debug ("Still Waiting ...");
            reportPane.getReportLock().wait();
          }
          catch (InterruptedException e)
          {
            Log.debug ("Interrupted while waiting for repagination.");
          }
        }

        Log.debug ("Repagination complete.");
        Log.debug ("Printing now ..");

        reportPane.setPrinting(true);
      }

      DocPrintJob job = service.createPrintJob();
      SimpleDoc document = new SimpleDoc
        (reportPane, DocFlavor.SERVICE_FORMATTED.PAGEABLE, null);

      job.print(document, attributes);
      synchronized(reportPane.getReportLock())
      {
        reportPane.setPrinting(false);
      }

      Log.debug ("Printing complete ..");
      setTaskDone();
    }
    catch (PrintException pe)
    {
      setTaskFailed(pe);
    }
    catch (ReportProcessingException pe)
    {
      setTaskFailed(pe);
    }
    finally
    {
      proxyBase.removePropertyChangeListener(updateListener);
      if (repaginateListener != null)
      {
        repaginateListener.getReportPane().removePropertyChangeListener
                (repaginateListener);
      }
      progressDialog.setVisible(false);
    }
  }
}
