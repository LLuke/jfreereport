/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
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
 * JFreeReportCoreModule.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: JFreeReportCoreModule.java,v 1.2 2003/09/02 15:05:05 taqua Exp $
 *
 * Changes 
 * -------------------------
 * 28.08.2003 : Initial version
 *  
 */

package org.jfree.report;

import java.io.InputStream;

import org.jfree.report.modules.AbstractModule;
import org.jfree.report.modules.ModuleInitializeException;

/**
 * The CoreModule is used to represent the base classes of JFreeReport
 * in a PackageManager-compatible way. Modules may request a certain
 * core-version to be present by referencing to this module.
 * 
 * @author Thomas Morgner
 */
public class JFreeReportCoreModule extends AbstractModule
{
  /**
   * Creates a new module definition based on the 'coremodule.properties'
   * file of this package.
   * 
   * @throws ModuleInitializeException if the file could not be loaded.
   */
  public JFreeReportCoreModule() throws ModuleInitializeException
  {
    final InputStream in = getClass().getResourceAsStream
        ("coremodule.properties");
    if (in == null)
    {
      throw new ModuleInitializeException
          ("File 'coremodule.properties' not found in JFreeReport package.");
    }
    loadModuleInfo(in);
  }

  /**
   * Initializes the module. This method does nothing.
   *
   * @throws ModuleInitializeException if an error ocurred while initializing the module.
   */
  public void initialize() throws ModuleInitializeException
  {
    // this does nothing ...
  }
}
