/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2006, by Object Refinery Limited, Pentaho Corporation and Contributors.
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
 * ConfigStoreBaseModule.java
 * ------------------------------
 * (C) Copyright 2000-2006, by Object Refinery Limited, Pentaho Corporation and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Object Refinery Limited);
 *
 * $Id: ConfigStoreBaseModule.java,v 1.8 2005/09/19 15:38:47 taqua Exp $
 *
 * Changes
 * -------------------------
 * 14-Jul-2003 : Initial version
 *
 */

package org.jfree.report.modules.preferences.base;

import org.jfree.base.modules.AbstractModule;
import org.jfree.base.modules.ModuleInitializeException;
import org.jfree.base.modules.SubSystem;


/**
 * The module definition for the config store system.
 *
 * @author Thomas Morgner
 */
public class ConfigStoreBaseModule extends AbstractModule
{
  /**
   * DefaultConstructor. Loads the module specification.
   *
   * @throws ModuleInitializeException if an error occured.
   */
  public ConfigStoreBaseModule ()
          throws ModuleInitializeException
  {
    loadModuleInfo();
  }

  /**
   * Initalizes the module. This method is empty.
   *
   * @throws ModuleInitializeException if an error occured.
   * @see org.jfree.base.modules.Module#initialize()
   */
  public void initialize ()
          throws ModuleInitializeException
  {
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
  }
}