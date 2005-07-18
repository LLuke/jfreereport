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
 * PackageConfiguration.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Object Refinery Limited);
 *
 * $Id: PackageConfiguration.java,v 1.6 2005/02/23 21:06:05 taqua Exp $
 *
 * Changes
 * -------------------------
 * 09.07.2003 : Initial version
 *
 */

package org.jfree.report.util;

/**
 * The PackageConfiguration handles the module level configuration.
 *
 * @author Thomas Morgner
 * @deprecated This class is no longer used. Package level configuration has been moved
 * to JCommon.
 */
public class PackageConfiguration extends PropertyFileReportConfiguration
{
  /**
   * DefaultConstructor. Creates a new package configuration.
   */
  public PackageConfiguration ()
  {
  }

  /**
   * The new configuartion will be inserted into the list of report configuration, so that
   * this configuration has the given report configuration instance as parent.
   *
   * @param config the new report configuration.
   */
  public void insertConfiguration (final ReportConfiguration config)
  {
    super.insertConfiguration(config);
  }
}
