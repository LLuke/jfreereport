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
 * PackageManager.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: PackageManager.java,v 1.2 2003/06/27 14:25:26 taqua Exp $
 *
 * Changes
 * -------------------------
 * 26-Jun-2003 : Initial version
 *
 */

package org.jfree.report.util;

/**
 * This class will help to manage the extension classes of JFreeReport 0.8.4 and
 * later...
 *
 * @author Thomas Morgner
 */
public class PackageManager
{
  /** A singleton instance of the package manager. */
  private static PackageManager singleton;

  /**
   * Returns the singleton instance of the package manager.
   *
   * @return the package manager.
   */
  public static PackageManager getInstance()
  {
    if (singleton == null)
    {
      singleton = new PackageManager();
    }
    return singleton;
  }

  /**
   * DefaultConstructor.
   */
  private PackageManager()
  {
  }
}
