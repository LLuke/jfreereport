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
 * SimpleParserModule.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: SimpleParserModule.java,v 1.4 2003/08/24 15:08:21 taqua Exp $
 *
 * Changes
 * -------------------------
 * 10.07.2003 : Initial version
 *
 */

package org.jfree.report.modules.parser.simple;

import org.jfree.report.modules.AbstractModule;
import org.jfree.report.modules.ModuleInitializeException;

/**
 * The Module specification for the simple parser module. This module handles the
 * simple report definition format.
 *
 * @author Thomas Morgner
 */
public class SimpleParserModule extends AbstractModule
{
  /**
   * Loads the module information from the module.properties file.
   *
   * @throws ModuleInitializeException if loading the specifications failed.
   */
  public SimpleParserModule() throws ModuleInitializeException
  {
    loadModuleInfo();
  }

  /**
   * Initializes the module.
   *
   * @throws ModuleInitializeException if initialisation fails.
   */
  public void initialize() throws ModuleInitializeException
  {
    performExternalInitialize(SimpleParserModuleInit.class.getName());
  }
}
