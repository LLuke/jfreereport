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
 * Java14ConfigModule.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: Java14ConfigModule.java,v 1.4 2005/01/31 17:16:38 taqua Exp $
 *
 * Changes
 * -------------------------
 * 23.07.2003 : Initial version
 *
 */

package org.jfree.report.ext.modules.java14config;

import org.jfree.base.modules.AbstractModule;
import org.jfree.base.modules.ModuleInitializeException;
import org.jfree.base.modules.SubSystem;
import org.jfree.report.util.ReportConfiguration;
import org.jfree.util.Log;

/**
 * The module definition for the Java1.4 configuration target support module.
 * 
 * @author Thomas Morgner
 */
public class Java14ConfigModule extends AbstractModule
{
  /** The class name of the storage module. */
  private static final String JAVA14_CONFIG_STORE_CLASS =
      "org.jfree.report.ext.modules.java14config.Java14ConfigStorage";
  /** The class name of the initializer class. */
  private static final String JAVA14_CONFIG_STORE_INITIALIZER =
      "org.jfree.report.ext.modules.java14config.Java14ConfigModuleInitializer";

  /**
   * DefaultConstructor. Loads the module specification.
   * @throws ModuleInitializeException if an error occured.
   */
  public Java14ConfigModule() throws ModuleInitializeException
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
    final String value = ReportConfiguration.getGlobalConfig().getConfigProperty
        ("org.jfree.report.ConfigStore", "<not defined>");
    if (value.equals(JAVA14_CONFIG_STORE_CLASS) == false)
    {
      Log.debug ("Java 1.4 Config module not active.");
      return;
    }
    // this will result in an caught exception if JDK 1.4 is not available.
    performExternalInitialize(JAVA14_CONFIG_STORE_INITIALIZER);
  }
}
