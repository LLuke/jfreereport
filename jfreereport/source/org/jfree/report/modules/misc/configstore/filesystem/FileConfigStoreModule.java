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
 * FileConfigStoreModule.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Object Refinery Limited);
 *
 * $Id: FileConfigStoreModule.java,v 1.5 2004/05/07 14:29:49 mungady Exp $
 *
 * Changes
 * -------------------------
 * 14-Jul-2003 : Initial version
 *
 */

package org.jfree.report.modules.misc.configstore.filesystem;

import org.jfree.report.util.ReportConfiguration;
import org.jfree.base.modules.AbstractModule;
import org.jfree.base.modules.ModuleInitializeException;
import org.jfree.base.modules.SubSystem;

/**
 * The module definition for the filesystem config storage module.
 * This module provides an configuration store implementation that
 * saves all properties to an configurable directory on the filesystem.
 *
 * @author Thomas Morgner
 */
public class FileConfigStoreModule extends AbstractModule
{
  /**
   * DefaultConstructor. Loads the module specification.
   * @throws ModuleInitializeException if an error occured.
   */
  public FileConfigStoreModule() throws ModuleInitializeException
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
    if (value.equals(FileConfigStorage.class.getName()))
    {
      performExternalInitialize(FileConfigStoreModuleInitializer.class.getName());
    }
  }
}
