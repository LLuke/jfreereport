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
 * $Id: Java14LogModule.java,v 1.7 2005/09/19 13:34:25 taqua Exp $
 *
 * Changes 
 * -------------------------
 * 11.07.2003 : Initial version
 *  
 */

package org.jfree.report.ext.modules.java14log;

import org.jfree.base.log.LogConfiguration;
import org.jfree.base.modules.AbstractModule;
import org.jfree.base.modules.ModuleInitializeException;
import org.jfree.base.modules.SubSystem;
import org.jfree.base.BaseBoot;
import org.jfree.util.Configuration;
import org.jfree.util.Log;

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
   * Initializes the module. Use this method to perform all initial setup operations. This
   * method is called only once in a modules lifetime. If the initializing cannot be
   * completed, throw a ModuleInitializeException to indicate the error,. The module will
   * not be available to the system.
   *
   * @param subSystem the subSystem.
   * @throws org.jfree.base.modules.ModuleInitializeException
   *          if an error ocurred while initializing the module.
   */
  public void initialize (final SubSystem subSystem)
          throws ModuleInitializeException
  {
    if (BaseBoot.getConfiguration().getConfigProperty
            (LogConfiguration.DISABLE_LOGGING, "false").equals("false"))
    {
      return;
    }

    final Configuration config = BaseBoot.getConfiguration();
    if (Java14LogTarget.class.getName().equals
            (config.getConfigProperty(LogConfiguration.LOGTARGET)))
    {
      Log.getInstance().addTarget(new Java14LogTarget());
      if ("true".equals(subSystem.getGlobalConfig().getConfigProperty
              ("org.jfree.base.LogAutoInit")))
      {
        Log.getInstance().init();
      }
      Log.info ("Java1.4 log target started ... previous log messages could have been ignored.");
    }
  }
}
