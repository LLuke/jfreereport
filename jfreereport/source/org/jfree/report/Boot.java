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
 * Boot.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id$
 *
 * Changes 
 * -------------------------
 * 01-Sep-2003 : Initial version
 *  
 */

package org.jfree.report;

import org.jfree.report.modules.PackageManager;
import org.jfree.report.util.CSVTokenizer;
import org.jfree.report.util.Log;

/**
 * An utility class to safely boot and initialize the JFreeReport library.
 * This class should be called before using the JFreeReport classes, to
 * make sure that all subsystems are initialized correctly and in the correct
 * order.
 */
public final class Boot
{
  private static boolean bootInProgress;
  private static boolean bootDone;

  public static boolean isBootInProgress()
  {
    return bootInProgress;
  }

  public static boolean isBootDone()
  {
    return bootDone;
  }

  public static void start()
  {
    if (isBootInProgress() || isBootDone())
    {
      return;
    }
    bootInProgress = true;
    PackageManager mgr = PackageManager.getInstance();

    mgr.addModule(JFreeReportCoreModule.class.getName());
    mgr.addModule(DefaultLogModule.class.getName());
    mgr.load("org.jfree.report.modules.");
    mgr.load("org.jfree.report.ext.modules.");
    try
    {
      String bootModules = System.getProperty("org.jfree.report.boot.Modules");
      if (bootModules != null)
      {
        CSVTokenizer csvToken = new CSVTokenizer(bootModules, ",");
        while (csvToken.hasMoreTokens())
        {
          String token = csvToken.nextToken();
          mgr.load(token);
        }
      }
    }
    catch (SecurityException se)
    {
      Log.info ("Security settings forbid to check the system properties for extension modules.");
    }
    catch (Exception se)
    {
      Log.error ("An error occured while checking the system properties for extension modules.", se);
    }

    mgr.initializeModules();
    bootDone = true;
  }
}

