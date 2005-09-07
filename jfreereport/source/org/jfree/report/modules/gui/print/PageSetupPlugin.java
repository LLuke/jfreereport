/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
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
 * PageSetupPlugin.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: PageSetupPlugin.java,v 1.15 2005/08/08 15:36:32 taqua Exp $
 *
 * Changes
 * -------------------------
 * 13.06.2003 : Initial version
 *
 */

package org.jfree.report.modules.gui.print;

import java.awt.print.PageFormat;
import java.awt.print.PrinterJob;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.Icon;
import javax.swing.KeyStroke;

import org.jfree.report.JFreeReport;
import org.jfree.report.JFreeReportBoot;
import org.jfree.report.ReportProcessingException;
import org.jfree.report.modules.gui.base.AbstractExportPlugin;
import org.jfree.report.modules.gui.base.PreviewProxy;
import org.jfree.report.modules.gui.base.PreviewProxyBase;
import org.jfree.report.modules.gui.base.ReportPane;
import org.jfree.report.util.PageFormatFactory;
import org.jfree.util.Log;
import org.jfree.util.ResourceBundleSupport;

/**
 * An export control plugin that handles the setup of page format objects for the report.
 *
 * @author Thomas Morgner
 */
public class PageSetupPlugin extends AbstractExportPlugin
{
  private class RepaginationListener implements PropertyChangeListener
  {
    public RepaginationListener ()
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
      if (ReportPane.PRINTING_PROPERTY.equals(evt.getPropertyName()) ||
              ReportPane.PAGINATING_PROPERTY.equals(evt.getPropertyName()))
      {
        final ReportPane reportPane = getReportPane();
        setEnabled((reportPane.isPrinting() == false) &&
                (reportPane.isPaginating() == false));
      }
      else if (PreviewProxyBase.REPORT_PANE_PROPERTY.equals(evt.getPropertyName()))
      {
        updateReportPane();
      }
    }
  }

  /**
   * Localised resources.
   */
  private ResourceBundleSupport resources;

  private ReportPane reportPane;
  private RepaginationListener repaginationListener;

  /**
   * Default Constructor.
   */
  public PageSetupPlugin ()
  {
    resources = new ResourceBundleSupport(PrintingPlugin.BASE_RESOURCE_CLASS);
    repaginationListener = new RepaginationListener();
  }

  /**
   * Initializes the plugin to work with the given PreviewProxy.
   *
   * @param proxy the preview proxy that created this plugin.
   * @throws NullPointerException if the proxy or the proxy's basecomponent is null.
   */
  public void init (final PreviewProxy proxy)
  {
    super.init(proxy);
    reportPane = proxy.getBase().getReportPane();
    reportPane.addPropertyChangeListener(repaginationListener);
  }

  /**
   * Returns true, when this export plugin is used to configure the report or an other
   * plugin.
   *
   * @return true if this is a control plugin, false otherwise.
   */
  public boolean isControlPlugin ()
  {
    return true;
  }

  /**
   * Exports a report.
   *
   * @param report the report.
   * @return A boolean.
   */
  public boolean performExport (final JFreeReport report)
  {
    final PrinterJob pj = PrinterJob.getPrinterJob();
    final PageFormat original = report.getPageDefinition().getPageFormat(0);
    final PageFormat pf = pj.validatePage(pj.pageDialog(original));
    if (PageFormatFactory.isEqual(pf, original))
    {
      return false;
    }
    else
    {
      try
      {
        getBase().updatePageFormat(pf);
      }
      catch (ReportProcessingException rpe)
      {
        Log.warn("Invalid pageformat update");
        return false;
      }
      return true;
    }
  }

  /**
   * Returns the display name for the export action.
   *
   * @return The display name.
   */
  public String getDisplayName ()
  {
    return (resources.getString("action.page-setup.name"));
  }

  /**
   * Returns the short description for the export action.
   *
   * @return The short description.
   */
  public String getShortDescription ()
  {
    return (resources.getString("action.page-setup.description"));
  }

  /**
   * Returns the small icon for the export action.
   *
   * @return The icon.
   */
  public Icon getSmallIcon ()
  {
    return resources.getIcon("action.page-setup.small-icon");
  }

  /**
   * Returns the large icon for the export action.
   *
   * @return The icon.
   */
  public Icon getLargeIcon ()
  {
    return resources.getIcon("action.page-setup.icon");
  }

  /**
   * Returns the accelerator key for the export action.
   *
   * @return The accelerator key.
   */
  public KeyStroke getAcceleratorKey ()
  {
    return null;
  }

  /**
   * Returns the mnemonic key code.
   *
   * @return The code.
   */
  public Integer getMnemonicKey ()
  {
    return resources.getMnemonic("action.page-setup.mnemonic");
  }


  /**
   * Returns the resourcebundle to be used to translate strings into localized content.
   *
   * @return the resourcebundle for the localisation.
   */
  protected ResourceBundleSupport getResources ()
  {
    return resources;
  }


  /**
   * Returns true if the action should be added to the toolbar, and false otherwise.
   *
   * @return true, if the plugin should be added to the toolbar, false otherwise.
   */
  public boolean isAddToToolbar ()
  {
    return JFreeReportBoot.getInstance().getGlobalConfig().getConfigProperty
            ("org.jfree.report.modules.gui.print.pagesetup.AddToToolbar", "false").equals("true");
  }

  /**
   * Returns true if the action is separated, and false otherwise. A separated action
   * starts a new action group and will be spearated from previous actions on the menu and
   * toolbar.
   *
   * @return true, if the action should be separated from previous actions, false
   *         otherwise.
   */
  public boolean isSeparated ()
  {
    return JFreeReportBoot.getInstance().getGlobalConfig().getConfigProperty
            ("org.jfree.report.modules.gui.print.pagesetup.Separated", "false").equals("true");
  }

  protected void updateReportPane ()
  {
    reportPane.removePropertyChangeListener(repaginationListener);
    reportPane = getBase().getReportPane();
    reportPane.addPropertyChangeListener(repaginationListener);
    setEnabled((reportPane.isPrinting() == false) &&
            (reportPane.isPaginating() == false));
  }
  
  protected ReportPane getReportPane()
  {
    return reportPane;
  }
}
