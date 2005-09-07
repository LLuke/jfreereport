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
 * $Id: DefaultLogModule.java,v 1.9 2005/09/06 11:40:19 taqua Exp $
 *
 * Changes
 * -------------------------
 * 11-Jul-2003 : Initial version
 *
 */

package org.jfree.report;

import java.io.InputStream;

import org.jfree.base.log.DefaultLog;
import org.jfree.base.modules.AbstractModule;
import org.jfree.base.modules.ModuleInitializeException;
import org.jfree.base.modules.SubSystem;
import org.jfree.report.util.SystemOutLogTarget;
import org.jfree.util.Log;
import org.jfree.util.ObjectUtilities;

/**
 * The module definition for the System.out-Logging. This is the default log
 * implementation and is provided to insert the logging initialisation in the module
 * loading process.
 *
 * @author Thomas Morgner
 */
public class DefaultLogModule extends AbstractModule
{
  /**
   * The 'disable logging' property key.
   */
  public static final String DISABLE_LOGGING_KEY
          = "org.jfree.report.NoDefaultDebug";

  /**
   * The 'log target' property key.
   */
  public static final String LOGTARGET_KEY = "org.jfree.report.LogTarget";

  /**
   * DefaultConstructor. Loads the module specification from a file called
   * 'logmodule.properties'.
   *
   * @throws ModuleInitializeException if an error occured.
   */
  public DefaultLogModule ()
          throws ModuleInitializeException
  {
    final InputStream in = ObjectUtilities.getResourceRelativeAsStream
            ("logmodule.properties", DefaultLogModule.class);
    if (in == null)
    {
      throw new ModuleInitializeException
              ("File 'logmodule.properties' not found in JFreeReport package.");
    }
    loadModuleInfo(in);
  }

  /**
   * Initializes the module. If the defined LogTarget from the configuration is the
   * class name of the SystemOutLogTarget, then the SystemLogTarget is defined as
   * log system.
   * <p>
   * If logging is disabled, no further initialization is done. 
   *
   * @param subSystem the subSystem.
   * @throws org.jfree.base.modules.ModuleInitializeException
   *          if an error ocurred while initializing the module.
   */
  public void initialize (final SubSystem subSystem)
          throws ModuleInitializeException
  {
    if (Log.getInstance() instanceof DefaultLog)
    {
      DefaultLog log = (DefaultLog) Log.getInstance();
      log.init();
    }
    if (subSystem.getExtendedConfig().getBoolProperty(DISABLE_LOGGING_KEY, false))
    {
      return;
    }
    if (subSystem.getGlobalConfig().getConfigProperty(LOGTARGET_KEY).equals
            (SystemOutLogTarget.class.getName()))
    {

      Log.getInstance().addTarget(new SystemOutLogTarget());
      Log.info("System.out log target started ... previous log messages could have been ignored.");
    }
  }
}
