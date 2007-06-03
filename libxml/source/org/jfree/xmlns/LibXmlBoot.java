/**
 * =========================================
 * LibXML : a free Java layouting library
 * =========================================
 *
 * Project Info:  http://reporting.pentaho.org/libxml/
 *
 * (C) Copyright 2006, by Object Refinery Ltd, Pentaho Corporation and Contributors.
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
 *
 * ------------
 * $Id: LibXmlBoot.java,v 1.3 2007/04/01 13:46:34 taqua Exp $
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 */

package org.jfree.xmlns;

import org.jfree.base.AbstractBoot;
import org.jfree.base.BootableProjectInfo;
import org.jfree.util.Configuration;

/**
 * The LibXmlBoot class is used to initialize the library before it is
 * first used. This loads all configurations and initializes all factories.
 *
 * Without booting, basic services like logging and the global configuration
 * will not be availble.
 *
 * @author Thomas Morgner
 */
public class LibXmlBoot extends AbstractBoot
{
  private static LibXmlBoot singleton;

  /**
   * Returns the singleton instance of the boot-class.
   *
   * @return the singleton booter.
   */
  public static synchronized LibXmlBoot getInstance()
  {
    if (singleton == null)
    {
      singleton = new LibXmlBoot();
    }
    return singleton;
  }

  /**
   * Private constructor prevents object creation.
   */
  private LibXmlBoot ()
  {
  }

  /**
   * Returns the project info.
   *
   * @return The project info.
   */
  protected BootableProjectInfo getProjectInfo ()
  {
    return LibXmlInfo.getInstance();
  }

  /**
   * Loads the configuration.
   *
   * @return The configuration.
   */
  protected Configuration loadConfiguration ()
  {
    return createDefaultHierarchicalConfiguration
            ("/org/jfree/xmlns/libxml.properties",
             "/libxml.properties", true);
  }

  /**
   * Performs the boot.
   */
  protected void performBoot ()
  {
  }
}
