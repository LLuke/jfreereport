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
 * -----------------
 * ExportAction.java
 * -----------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: $
 *
 * Changes
 * --------
 * 25-Feb-2003 : Added standard header and Javadocs (DG);
 *
 */
package com.jrefinery.report.preview;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import com.jrefinery.report.JFreeReport;
import com.jrefinery.report.util.ActionDowngrade;

/**
 * An export action that works with any class that implements the {@link ExportPlugin} interface.
 * 
 * @author Thomas Morgner. 
 */
public class ExportAction extends AbstractAction implements ActionDowngrade
{
  /** The export plug-in. */
  private ExportPlugin plugin;
  
  /** The report. */ 
  private JFreeReport report;
  
  /**
   * Defines an <code>Action</code> object with a default description string and default icon.
   * 
   * @param plugin  the export plug-in.
   */
  public ExportAction(ExportPlugin plugin)
  {
    if (plugin == null)
    {
      throw new NullPointerException();
    }

    this.plugin = plugin;
    if (plugin.getAcceleratorKey() != null)
    {
      putValue(ActionDowngrade.ACCELERATOR_KEY, plugin.getAcceleratorKey());
    }
    if (plugin.getDisplayName() != null)
    {
      putValue(ExportAction.NAME, plugin.getDisplayName());
    }
    if (plugin.getSmallIcon() != null)
    {
      putValue(ExportAction.SMALL_ICON, plugin.getSmallIcon());
    }
    if (plugin.getLargeIcon() != null)
    {
      putValue("ICON24", plugin.getLargeIcon());
    }
    if (plugin.getMnemonicKey() != null)
    {
      putValue(ActionDowngrade.MNEMONIC_KEY, plugin.getMnemonicKey());
    }
    if (plugin.getShortDescription() != null)
    {
      putValue(ExportAction.SHORT_DESCRIPTION, plugin.getShortDescription());
    }
  }

  /**
   * Returns the report.
   * 
   * @return The report.
   */
  public JFreeReport getReport()
  {
    return report;
  }

  /**
   * Sets the report.
   * 
   * @param report  the report.
   */
  public void setReport(JFreeReport report)
  {
    this.report = report;
  }

  /**
   * Exports the current report using the installed export plug-in.
   * 
   * @param e  the event.
   */
  public void actionPerformed(ActionEvent e)
  {
    plugin.performExport(report);
  }
}
