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
 * -------------------------------
 * DefaultReportConfiguration.java
 * -------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: DefaultReportConfiguration.java,v 1.1 2003/07/07 22:44:09 taqua Exp $
 *
 * Changes
 * -------
 * 14-Jan-2003 : Initial version
 *
 */
package org.jfree.report.util;

/**
 * Create the implemented default report configuration. This configuration should
 * be set as root and defines the implementation defaults.
 *
 * @author Thomas Morgner
 */
public class DefaultReportConfiguration extends ReportConfiguration
{
  /**
   * Creates the default report configuration for JFreeReport.
   */
  public DefaultReportConfiguration()
  {
    this.getConfiguration().put(DISABLE_LOGGING, DISABLE_LOGGING_DEFAULT);
    this.getConfiguration().put(LOGLEVEL, LOGLEVEL_DEFAULT);
  }
}
