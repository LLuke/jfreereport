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
 * PreviewBase.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: AWTPrintingGUIModule.java,v 1.6 2003/08/24 15:08:19 taqua Exp $
 *
 * Changes
 * -------------------------
 * 06.07.2003 : Initial version
 *
 */

package org.jfree.report.modules.gui.print;

import org.jfree.report.modules.AbstractModule;
import org.jfree.report.modules.ModuleInitializeException;
import org.jfree.report.modules.gui.base.ExportPluginFactory;
import org.jfree.report.util.ReportConfiguration;

/**
 * The module definition for the AWT printing export gui module.
 * The module contains 2 export plugins, the page setup plugin
 * and the printing plugin.
 *
 * @author Thomas Morgner
 */
public class AWTPrintingGUIModule extends AbstractModule
{
  /** The printing export plugin preference key. */
  private static final String PRINT_ORDER_KEY =
      "org.jfree.report.modules.gui.print.Order";
  /** The printing export plugin enable key. */
  private static final String PRINT_ENABLE_KEY =
      "org.jfree.report.modules.gui.print.Enable";
  /** The pagesetup export plugin preference key. */
  private static final String PAGESETUP_ORDER_KEY =
      "org.jfree.report.modules.gui.print.pagesetup.Order";
  /** The pagesetup export plugin enable key. */
  private static final String PAGESETUP_ENABLE_KEY =
      "org.jfree.report.modules.gui.print.pagesetup.Enable";

  /**
   * DefaultConstructor. Loads the module specification.
   * @throws ModuleInitializeException if an error occured.
   */
  public AWTPrintingGUIModule() throws ModuleInitializeException
  {
    loadModuleInfo();
  }

  /**
   * Initalizes the module and registes it with the export plugin factory.
   * @see org.jfree.report.modules.Module#initialize()
   *
   * @throws ModuleInitializeException if an error occured.
   */
  public void initialize() throws ModuleInitializeException
  {
    final String printOrder = ReportConfiguration.getGlobalConfig().getConfigProperty
        (PRINT_ORDER_KEY, "0");

    ExportPluginFactory.getInstance().registerPlugin
        (PrintingPlugin.class, printOrder, PRINT_ENABLE_KEY);

    final String pageSetupOrder = ReportConfiguration.getGlobalConfig().getConfigProperty
        (PAGESETUP_ORDER_KEY, "0");

    ExportPluginFactory.getInstance().registerPlugin
        (PageSetupPlugin.class, pageSetupOrder, PAGESETUP_ENABLE_KEY);
  }
}
