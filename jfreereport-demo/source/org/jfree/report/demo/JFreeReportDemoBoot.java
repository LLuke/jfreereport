/**
 * ===========================================
 * JFreeReport : a free Java reporting library
 * ===========================================
 *
 * Project Info:  http://reporting.pentaho.org/
 *
 * (C) Copyright 2006-2007, by Pentaho Corporation and Contributors.
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
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * ------------
 * JFreeReportDemoBoot.java
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */

package org.jfree.report.demo;

import org.jfree.base.AbstractBoot;
import org.jfree.base.BootableProjectInfo;
import org.jfree.base.config.ModifiableConfiguration;
import org.jfree.xmlns.LibXmlInfo;
import org.jfree.util.Configuration;

/**
 * Creation-Date: 02.12.2006, 19:20:26
 *
 * @author Thomas Morgner
 */
public class JFreeReportDemoBoot extends AbstractBoot
{
  private static JFreeReportDemoBoot singleton;

  public static synchronized JFreeReportDemoBoot getInstance()
  {
    if (singleton == null)
    {
      singleton = new JFreeReportDemoBoot();
    }
    return singleton;
  }

  private JFreeReportDemoBoot ()
  {
  }

  /**
   * Returns the project info.
   *
   * @return The project info.
   */
  protected BootableProjectInfo getProjectInfo ()
  {
    return JFreeReportDemoInfo.getInstance();
  }

  /**
   * Loads the configuration.
   *
   * @return The configuration.
   */
  protected Configuration loadConfiguration ()
  {
    return createDefaultHierarchicalConfiguration
            ("/org/jfree/report/demo/demo.properties",
             "/jfreereport-demo.properties", true);
  }

  /**
   * Returns the current global configuration as modifiable instance. This
   * is exactly the same as casting the global configuration into a
   * ModifableConfiguration instance.
   * <p/>
   * This is a convinience function, as all programmers are lazy.
   *
   * @return the global config as modifiable configuration.
   */
  public ModifiableConfiguration getEditableConfig()
  {
    return (ModifiableConfiguration) getGlobalConfig();
  }

  /**
   * Performs the boot.
   */
  protected void performBoot ()
  {
  }
}
