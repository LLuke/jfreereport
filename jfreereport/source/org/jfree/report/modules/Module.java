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
 * Module.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: Module.java,v 1.4 2003/08/24 15:08:18 taqua Exp $
 *
 * Changes
 * -------------------------
 * 05-Jul-2003 : Initial version
 *
 */

package org.jfree.report.modules;

/**
 * The module interface describes a JFreeReport-module. Modules are loaded
 * and managed by the package manager.
 *
 * @author Thomas Morgner
 */
public interface Module extends ModuleInfo
{
  /**
   * Returns an array of all required modules. If one of these modules is missing
   * or cannot be initialized, the module itself will be not available.
   *
   * @return an array of the required modules.
   */
  public ModuleInfo[] getRequiredModules();

  /**
   * Returns an array of optional modules. Missing or invalid modules are non fatal
   * and will not harm the module itself.
   *
   * @return an array of optional module specifications.
   */
  public ModuleInfo[] getOptionalModules();

  /**
   * Initializes the module. Use this method to perform all initial setup operations.
   * This method is called only once in a modules lifetime. If the initializing cannot
   * be completed, throw a ModuleInitializeException to indicate the error,. The module
   * will not be available to the system.
   *
   * @throws ModuleInitializeException if an error ocurred while initializing the module.
   */
  public void initialize() throws ModuleInitializeException;

  /**
   * Configures the module. This should load the default settings of the module.
   */
  public void configure();

  /**
   * Returns a short description of the modules functionality.
   *
   * @return a module description.
   */
  public String getDescription();

  /**
   * Returns the name of the module producer.
   *
   * @return the producer name
   */
  public String getProducer();

  /**
   * Returns the module name. This name should be a short descriptive handle of the
   * module.
   *
   * @return the module name
   */
  public String getName();
}
