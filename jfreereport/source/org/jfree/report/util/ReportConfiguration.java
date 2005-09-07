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
 * ------------------------
 * ReportConfiguration.java
 * ------------------------
 * (C)opyright 2002-2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: ReportConfiguration.java,v 1.18 2005/08/08 15:36:38 taqua Exp $
 *
 * Changes
 * -------
 * 06-Nov-2002 : Initial release
 * 12-Nov-2002 : Added Javadoc comments (DG);
 * 29-Nov-2002 : Fixed bugs reported by CheckStyle (DG)
 * 05-Dec-2002 : Documentation
 * 07-Sep-2005 : Removed all global functionality. This class represents a
 *               local configuration of a single JFreeReport instance.
 */

package org.jfree.report.util;

import org.jfree.base.config.HierarchicalConfiguration;
import org.jfree.report.JFreeReportBoot;
import org.jfree.util.Configuration;

/**
 * The local configuration for a JFreeReport instance.
 * <p/>
 * The global configuration can be accessed using <code>JFreeReportBoot.getInstance().getGlobalConfig()</code>.
 *
 * @author Thomas Morgner
 */
public class ReportConfiguration extends HierarchicalConfiguration
{
  /**
   * Creates a new report configuration.
   *
   * @param globalConfig the global configuration.
   */
  public ReportConfiguration (final Configuration globalConfig)
  {
    super(globalConfig);
  }

  protected boolean isParentSaved ()
  {
    return false;
  }

  protected void configurationLoaded ()
  {
    if (isParentSaved() == false)
    {
      setParentConfig(JFreeReportBoot.getInstance().getGlobalConfig());
    }
  }
}
