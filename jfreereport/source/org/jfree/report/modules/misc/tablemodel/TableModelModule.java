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
 * TableModelModule.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: TableModelModule.java,v 1.1 2003/07/10 20:06:00 taqua Exp $
 *
 * Changes 
 * -------------------------
 * 10-Jul-2003 : Initial version
 *  
 */

package org.jfree.report.modules.misc.tablemodel;

import org.jfree.report.modules.AbstractModule;
import org.jfree.report.modules.ModuleInitializeException;

/**
 * The module definition for the table model utility classes module.
 * 
 * @author Thomas Morgner
 */
public class TableModelModule extends AbstractModule
{
  /** 
   * DefaultConstructor. Loads the module specification.
   * @throws ModuleInitializeException if an error occured.
   */
  public TableModelModule() throws ModuleInitializeException
  {
    loadModuleInfo();
  }

  /**
   * Initalizes the module. This method is empty. 
   * @see org.jfree.report.modules.Module#initialize()
   * 
   * @throws ModuleInitializeException if an error occured.
   */
  public void initialize() throws ModuleInitializeException
  {
  }
}
