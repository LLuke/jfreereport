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
 * PackageStateSortTest.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: PackageStateSortTest.java,v 1.1 2003/09/12 17:51:05 taqua Exp $
 *
 * Changes 
 * -------------------------
 * 01.09.2003 : Initial version
 *  
 */

package org.jfree.report.ext.junit.base.basic.modules;

import java.util.ArrayList;

import junit.framework.TestCase;
import org.jfree.base.modules.Module;
import org.jfree.base.modules.PackageSorter;
import org.jfree.base.modules.PackageState;
import org.jfree.report.JFreeReportBoot;
import org.jfree.report.modules.misc.configstore.filesystem.FileConfigStoreModule;
import org.jfree.report.modules.output.support.itext.BaseFontModule;
import org.jfree.report.util.Log;

public class PackageStateSortTest extends TestCase
{
  public PackageStateSortTest (final String s)
  {
    super(s);
  }

  public void testAll () throws Exception
  {
    final ArrayList states = new ArrayList();
    JFreeReportBoot.getInstance().start();
    final Module[] mods = JFreeReportBoot.getInstance().getPackageManager().getAllModules();
    int basefontPos = 0;
    int fileConfigPos = 0;

    for (int i = 0; i < mods.length; i++)
    {
      states.add (new PackageState(mods[i]));
    }

    PackageSorter.sort(states);

    for (int i = 0; i < states.size(); i++)
    {
      final PackageState state = (PackageState) states.get(i);

      if (state.getModule().getClass().equals(BaseFontModule.class))
      {
        Log.debug ("BaseFont: " + i);
        basefontPos = i;
      }
      if (state.getModule().getClass().equals(FileConfigStoreModule.class))
      {
        Log.debug ("File: " + i);
        fileConfigPos = i;
      }
    }

    assertTrue(fileConfigPos < basefontPos);
  }

}
