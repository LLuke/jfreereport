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
 * ------------------------------
 * NullConfigStorage.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: NullConfigStorage.java,v 1.3 2003/08/22 20:27:20 taqua Exp $
 *
 * Changes 
 * -------------------------
 * 14-Jul-2003 : Initial version
 *  
 */

package org.jfree.report.modules.misc.configstore.base;

import java.util.Properties;

/**
 * An empty default implementation. This config storare will not store
 * any values and will provide no read access to stored properties by 
 * denying their existence.
 * 
 * @author Thomas Morgner
 */
public class NullConfigStorage implements ConfigStorage
{
  /**
   * DefaultConstructor.
   */
  public NullConfigStorage()
  {
  }

  /**
   * This method does nothing.
   *  
   * @see org.jfree.report.modules.misc.configstore.base.ConfigStorage#storeProperties
   * (java.lang.String, java.util.Properties)
   * 
   * @param configPath this parameter is not used.
   * @param properties this parameter is not used. 
   */
  public void storeProperties(String configPath, Properties properties)
  {
  }

  /**
   * Loads the properties from the given path, specifying the given properties
   * as default.
   * <p>
   * This implementation will always throw and ConfigStoreException as the
   * specified resource is not available.
   *
   * @param configPath the configuration path from where to read the properties.
   * @param defaults the property set that acts as fallback to provide default
   * values. 
   * @return the loaded properties
   * @throws ConfigStoreException always throws this exception as the specified
   * resource will be not available.
   */
  public Properties loadProperties(String configPath, Properties defaults)
    throws ConfigStoreException
  {
    throw new ConfigStoreException("This configuration path is not available.");
  }

  /**
   * Tests, whether some configuration data exists for the given configuration.
   * <p>
   * This method returns always false and denies the existence of any resource.
   * 
   * @param configPath the configuration path to the property storage.
   * @return always false as this implementation does not store anything.
   */
  public boolean existsProperties(String configPath)
  {
    return false;
  }
}
