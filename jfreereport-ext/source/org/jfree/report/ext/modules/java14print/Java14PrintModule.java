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
 * Java14PrintModule.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: Java14PrintModule.java,v 1.4 2003/11/01 19:57:04 taqua Exp $
 *
 * Changes 
 * -------------------------
 * 23.07.2003 : Initial version
 *  
 */

package org.jfree.report.ext.modules.java14print;

import org.jfree.report.modules.gui.base.ExportPluginFactory;
import org.jfree.report.util.ReportConfiguration;
import org.jfree.base.modules.AbstractModule;
import org.jfree.base.modules.ModuleInitializeException;
import org.jfree.base.modules.SubSystem;

/**
 * The module definition for the Java 1.4 printing support module.
 * 
 * @author Thomas Morgner
 */
public class Java14PrintModule extends AbstractModule
{
  /** The printing export plugin preference key. */ 
  private static final String PRINT_ORDER_KEY = 
    "org.jfree.report.ext.modules.java14print.Order";
  
  /** The printing export plugin enable key. */
  private static final String PRINT_ENABLE_KEY = 
    "org.jfree.report.ext.modules.java14print.Enable";

  /** 
   * DefaultConstructor. Loads the module specification.
   * @throws ModuleInitializeException if an error occured.
   */
  public Java14PrintModule() throws ModuleInitializeException
  {
    loadModuleInfo();
  }

  /**
   * Initializes the module. Use this method to perform all initial setup operations. This
   * method is called only once in a modules lifetime. If the initializing cannot be
   * completed, throw a ModuleInitializeException to indicate the error,. The module will
   * not be available to the system.
   *
   * @param subSystem the subSystem.
   * @throws org.jfree.base.modules.ModuleInitializeException
   *          if an error ocurred while initializing the module.
   */
  public void initialize (SubSystem subSystem)
          throws ModuleInitializeException
  {
    String printOrder = ReportConfiguration.getGlobalConfig().getConfigProperty
        (PRINT_ORDER_KEY, "0");

    ExportPluginFactory.getInstance().registerPlugin
        (Java14PrintingPlugin.class, printOrder, PRINT_ENABLE_KEY);
  }

}
