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
 * ModuleTestSuite.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: ModuleTestSuite.java,v 1.3 2003/11/01 19:57:02 taqua Exp $
 *
 * Changes 
 * -------------------------
 * 12.09.2003 : Initial version
 *  
 */

package org.jfree.report.ext.junit.base.basic.modules;

import junit.framework.Test;
import junit.framework.TestSuite;
import org.jfree.report.ext.junit.base.basic.modules.table.SimpleLayoutTest;
import org.jfree.report.ext.junit.base.basic.modules.table.TableCellBackgroundTest;

public class ModuleTestSuite extends TestSuite
{
  public ModuleTestSuite(final String s)
  {
    super(s);
    addTestSuite(PackageStateSortTest.class);
    addTestSuite(SimpleLayoutTest.class);
    addTestSuite(TableCellBackgroundTest.class);
  }

  /**
   * Dummmy method to silence the checkstyle test.
   */
  public void dummy()
  {
  }

  public static Test suite()
  {
    return new ModuleTestSuite(ModuleTestSuite.class.getName());
  }

}
