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
 * Java14LogModule.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: Java14LogModule.java,v 1.2 2003/08/20 19:24:58 taqua Exp $
 *
 * Changes 
 * -------------------------
 * 11.07.2003 : Initial version
 *  
 */

package org.jfree.report.ext.modules.java14log;

import org.jfree.report.modules.AbstractModule;
import org.jfree.report.modules.ModuleInitializeException;
import org.jfree.report.util.Log;
import org.jfree.report.util.ReportConfiguration;

/**
 * The module definition for the Java1.4 log target support module.
 * 
 * @author Thomas Morgner
 */
public class Java14LogModule extends AbstractModule
{
  /** 
   * DefaultConstructor. Loads the module specification.
   * @throws ModuleInitializeException if an error occured.
   */
  public Java14LogModule() throws ModuleInitializeException
  {
    loadModuleInfo();
  }

  /**
   * Initalizes the module. This method is empty. 
   * @see org.jfree.report.modules.Module#initialize()
   * 
   * @throws ModuleInitializeException if an error occured.
   */
  public void initialize() throws ModuleInitializeException
  {
    if (ReportConfiguration.getGlobalConfig().isDisableLogging())
    {
      return;
    }
    if (ReportConfiguration.getGlobalConfig().getConfigProperty
        (ReportConfiguration.LOGTARGET, "").equals
        (Java14LogTarget.class.getName()))
    {
      Log.getJFreeReportLog().addTarget(new Java14LogTarget());
      Log.getJFreeReportLog().init();
      Log.info ("Java 1.4 log target started ... previous log messages could have been ignored.");
    }
  }
}
