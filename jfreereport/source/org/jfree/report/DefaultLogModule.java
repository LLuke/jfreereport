/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2003, by Object Refinery Limited and Contributors.
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
 * DefaultLogModule.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Object Refinery Limited);
 *
 * $Id: DefaultLogModule.java,v 1.2 2003/09/08 18:11:48 taqua Exp $
 *
 * Changes
 * -------------------------
 * 11-Jul-2003 : Initial version
 *
 */

package org.jfree.report;

import java.io.InputStream;

import org.jfree.report.modules.AbstractModule;
import org.jfree.report.modules.ModuleInitializeException;
import org.jfree.report.util.Log;
import org.jfree.report.util.ReportConfiguration;
import org.jfree.report.util.SystemOutLogTarget;

/**
 * The module definition for the System.out-Logging. This is the
 * default log implementation and is provided to insert the logging
 * initialisation in the module loading process.
 *
 * @author Thomas Morgner
 */
public class DefaultLogModule extends AbstractModule
{
  /**
   * DefaultConstructor. Loads the module specification.
   * 
   * @throws ModuleInitializeException if an error occured.
   */
  public DefaultLogModule() throws ModuleInitializeException
  {
    final InputStream in = getClass().getResourceAsStream
        ("logmodule.properties");
    if (in == null)
    {
      throw new ModuleInitializeException
          ("File 'logmodule.properties' not found in JFreeReport package.");
    }
    loadModuleInfo(in);
  }

  /**
   * Initalizes the module. This method initializes the logging system,
   * if the System.out logtarget is selected.
   *
   * @see org.jfree.report.modules.Module#initialize()
   * @throws ModuleInitializeException if an error occured.
   */
  public void initialize() throws ModuleInitializeException
  {
    if (ReportConfiguration.getGlobalConfig().isDisableLogging())
    {
      return;
    }
    if (ReportConfiguration.getGlobalConfig().getLogTarget().equals
        (SystemOutLogTarget.class.getName()))
    {
      Log.getJFreeReportLog().addTarget(new SystemOutLogTarget());
      Log.getJFreeReportLog().init();
      Log.info("System.out log target started ... previous log messages could have been ignored.");
    }
  }
}
