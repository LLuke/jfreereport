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
 * ConfigStorage.java
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
 * 14.07.2003 : Initial version
 *  
 */

package org.jfree.report.modules.misc.configstore.base;

import java.util.Properties;

// A valid configuration path does not contain dots, semicolons or colons.
// A valid path obeys to the same rules as java identifiers ..
public interface ConfigStorage
{
  public void storeProperties (String configPath, Properties properties)
      throws ConfigStoreException;

  /**
   * Loads the properties from the given path, specifying the given properties
   * as default.
   *
   * @param configPath
   * @return
   */
  public Properties loadProperties (String configPath, Properties defaults)
      throws ConfigStoreException;

  /**
   * Tests, whether some configuration data exists for the given configuration.
   * @param configPath the configuration path to the property storage.
   * @return true, if there are properties under this path, false otherwise.
   */
  public boolean existsProperties (String configPath);
}
