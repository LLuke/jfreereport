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
 * ModuleInfo.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Object Refinery Limited);
 *
 * $Id: ModuleInfo.java,v 1.4 2003/08/25 14:29:29 taqua Exp $
 *
 * Changes
 * -------------------------
 * 05-Jul-2003 : Initial version
 *
 */

package org.jfree.report.modules;

/**
 * The module info describes an required module and the module version.
 * This description is used by the package manager to locate base modules.
 *
 * @author Thomas Morgner
 */
public interface ModuleInfo
{
  /**
   * Returns the module class of the desired base module.
   *
   * @return the module class.
   */
  public String getModuleClass();

  /**
   * Returns the major version of the base module. The string should
   * contain a compareable character sequence so that higher versions
   * of the module are considered greater than lower versions.
   *
   * @return the major version of the module.
   */
  public String getMajorVersion();

  /**
   * Returns the minor version of the base module. The string should
   * contain a compareable character sequence so that higher versions
   * of the module are considered greater than lower versions.
   *
   * @return the minor version of the module.
   */
  public String getMinorVersion();

  /**
   * Returns the patchlevel version of the base module. The patch level
   * should be used to mark bugfixes. The string should
   * contain a compareable character sequence so that higher versions
   * of the module are considered greater than lower versions.
   *
   * @return the major version of the module.
   */
  public String getPatchLevel();
}
