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
 * PageableBaseModule.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Object Refinery Limited);
 *
 * $Id: PlainTextPageableModule.java,v 1.6 2005/01/25 00:11:21 taqua Exp $
 *
 * Changes
 * -------------------------
 * 06-Jul-2003 : Initial version
 *
 */

package org.jfree.report.modules.output.pageable.plaintext;

import org.jfree.base.modules.AbstractModule;
import org.jfree.base.modules.ModuleInitializeException;
import org.jfree.base.modules.SubSystem;

/**
 * The module definition for the plain text pagable export module.
 *
 * @author Thomas Morgner
 */
public class PlainTextPageableModule extends AbstractModule
{
  /**
   * DefaultConstructor. Loads the module specification.
   *
   * @throws org.jfree.base.modules.ModuleInitializeException
   *          if an error occured.
   */
  public PlainTextPageableModule ()
          throws ModuleInitializeException
  {
    loadModuleInfo();
  }

  /**
   * Initalizes the module. This method is empty.
   *
   * @throws ModuleInitializeException if an error occured.
   * @see org.jfree.base.modules.Module#initialize(org.jfree.base.modules.SubSystem)
   */
  public void initialize (final SubSystem subSystem)
          throws ModuleInitializeException
  {
  }
}
