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
 * $Id: Java14PrintExportTask.java,v 1.2 2003/10/19 11:29:56 taqua Exp $
 *
 * Changes 
 * -------------------------
 * 20.09.2003 : Initial version
 *  
 */

package org.jfree.report.ext.modules.java14print;

import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.SimpleDoc;
import javax.print.attribute.PrintRequestAttributeSet;

import org.jfree.report.modules.gui.base.ExportTask;
import org.jfree.report.modules.gui.base.ReportPane;
import org.jfree.report.modules.gui.base.ReportProgressDialog;

public class Java14PrintExportTask extends ExportTask
{
  private PrintService service;
  private ReportPane pageable;
  private PrintRequestAttributeSet attributes;
  private ReportProgressDialog progressDialog;

  public Java14PrintExportTask(ReportProgressDialog progressDialog,
                               PrintService service, ReportPane pageable,
                               PrintRequestAttributeSet attributes)
  {
    this.service = service;
    this.pageable = pageable;
    this.attributes = attributes;
    this.progressDialog = progressDialog;
  }

  /**
   * Performs the export to the java1.4 print system.
   */
  protected void performExport()
  {
    try
    {
      progressDialog.setModal(false);
      progressDialog.setVisible(true);
      DocPrintJob job = service.createPrintJob();
      SimpleDoc document = new SimpleDoc
        (pageable, DocFlavor.SERVICE_FORMATTED.PAGEABLE, null);
      synchronized (pageable.getReportLock())
      {
        pageable.setPrinting(true);
        job.print(document, attributes);
        pageable.setPrinting(false);
      }
      setTaskDone();
    }
    catch (PrintException pe)
    {
      setTaskFailed(pe);
    }
    finally
    {
      progressDialog.setVisible(false);
    }
  }
}
