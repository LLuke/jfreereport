/**
 * Date: Feb 2, 2003
 * Time: 6:28:35 PM
 *
 * $Id$
 */
package com.jrefinery.report.preview;

import com.jrefinery.report.util.ActionDowngrade;
import com.jrefinery.report.JFreeReport;

import javax.swing.AbstractAction;
import java.awt.event.ActionEvent;

public class ExportAction extends AbstractAction implements ActionDowngrade
{
  private ExportPlugin plugin;
  private JFreeReport report;
  /**
   * Defines an <code>Action</code> object with a default
   * description string and default icon.
   */
  public ExportAction(ExportPlugin plugin)
  {
    if (plugin == null)
      throw new NullPointerException();

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

  public JFreeReport getReport()
  {
    return report;
  }

  public void setReport(JFreeReport report)
  {
    this.report = report;
  }

  /**
   * Invoked when an action occurs.
   */
  public void actionPerformed(ActionEvent e)
  {
    plugin.performExport(report);
  }
}
