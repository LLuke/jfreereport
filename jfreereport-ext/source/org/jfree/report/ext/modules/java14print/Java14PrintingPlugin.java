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
 * Java14PrintingPlugin.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id$
 *
 * Changes 
 * -------------------------
 * 23.07.2003 : Initial version
 *  
 */

package org.jfree.report.ext.modules.java14print;

import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.ServiceUI;
import javax.print.SimpleDoc;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;

import org.jfree.report.JFreeReport;
import org.jfree.report.modules.gui.print.PrintingPlugin;

public class Java14PrintingPlugin extends PrintingPlugin
{
  private String failureDescription;

  public Java14PrintingPlugin()
  {
  }


  public String getFailureDescription()
  {
    return failureDescription;
  }

  public void setFailureDescription(String failureDescription)
  {
    this.failureDescription = failureDescription;
  }

  /**
   * Exports a report.
   *
   * @param report  the report.
   *
   * @return A boolean.
   */
  public boolean performExport(JFreeReport report)
  {

    PrintService[] services = PrintServiceLookup.lookupPrintServices(
                               DocFlavor.SERVICE_FORMATTED.PAGEABLE, null);
    if (services.length == 0)
    {
      setFailureDescription("Unable to find a matching print service implementation.");
      return false;
    }
    PrintRequestAttributeSet attributes = new HashPrintRequestAttributeSet();
    PrintService service =  ServiceUI.printDialog
        (null, 50, 50, services, null, null, attributes);
    if (service == null)
    {
      setFailureDescription("Print job cancelled.");
      return true;
    }

    try
    {
      DocPrintJob job = service.createPrintJob();
      SimpleDoc document = new SimpleDoc(getBase().getPageable(), DocFlavor.SERVICE_FORMATTED.PAGEABLE, null);
      job.print(document, attributes);
      return true;
    }
    catch (PrintException pe)
    {
      setFailureDescription(pe.getMessage());
      return false;
    }
  }

}
