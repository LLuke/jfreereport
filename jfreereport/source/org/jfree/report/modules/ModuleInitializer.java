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
 * ModuleInitializer.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: ModuleInitializer.java,v 1.2 2003/08/19 13:37:23 taqua Exp $
 *
 * Changes 
 * -------------------------
 * 14-Jul-2003 : Initial version
 *  
 */

package org.jfree.report.modules;

/**
 * The module initializer is used to separate the initialization process from
 * the module definition. An invalid classpath setup or an missing base module 
 * may throw an ClassCastException if the module class references this missing
 * resource. Separating them is the best way to make sure that the classloader
 * does not interrupt the module loading process.
 * 
 * @author Thomas Morgner
 */
public interface ModuleInitializer
{
  /**
   * Performs the initalization of the module.
   * 
   * @throws ModuleInitializeException if an error occurs which prevents the module
   * from being usable.
   */
  public void performInit () throws ModuleInitializeException;
}
