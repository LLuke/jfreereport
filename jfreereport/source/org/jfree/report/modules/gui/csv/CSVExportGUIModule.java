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
 * CSVExportGUIModule.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: CSVExportGUIModule.java,v 1.4 2003/08/19 13:37:23 taqua Exp $
 *
 * Changes 
 * -------------------------
 * 06-Jul-2003 : Initial version
 *  
 */

package org.jfree.report.modules.gui.csv;

import org.jfree.report.modules.AbstractModule;
import org.jfree.report.modules.ModuleInitializeException;
import org.jfree.report.modules.gui.base.ExportPluginFactory;
import org.jfree.report.util.ReportConfiguration;

/**
 * The module definition for the CSV export gui module.
 * 
 * @author Thomas Morgner
 */
public class CSVExportGUIModule extends AbstractModule
{
  /** The export plugin preference key. */ 
  private static final String ORDER_KEY = "org.jfree.report.modules.gui.csv.Order";
  /** The export plugin enable key. */
  private static final String ENABLE_KEY = "org.jfree.report.modules.gui.csv.Enable";

  /** 
   * DefaultConstructor. Loads the module specification.
   * @throws ModuleInitializeException if an error occured.
   */
  public CSVExportGUIModule() throws ModuleInitializeException
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
    String order = ReportConfiguration.getGlobalConfig().getConfigProperty
        (ORDER_KEY, "0");

    ExportPluginFactory.getInstance().registerPlugin(CSVExportPlugin.class, order, ENABLE_KEY);
  }
}
