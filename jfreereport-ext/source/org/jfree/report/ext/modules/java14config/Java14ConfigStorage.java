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
 * Java14ConfigStorage.java
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
 * 23.07.2003 : Initial version
 *  
 */

package org.jfree.report.ext.modules.java14config;

import java.util.Enumeration;
import java.util.Properties;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import org.jfree.report.modules.misc.configstore.base.ConfigFactory;
import org.jfree.report.modules.misc.configstore.base.ConfigStorage;
import org.jfree.report.modules.misc.configstore.base.ConfigStoreException;

public class Java14ConfigStorage implements ConfigStorage
{
  private Preferences base;

  public Java14ConfigStorage(Preferences base)
  {
    this.base = base;
  }

  public void storeProperties(String configPath, Properties properties)
      throws ConfigStoreException
  {
    if (ConfigFactory.isValidPath(configPath) == false)
    {
      throw new IllegalArgumentException("The give path is not valid.");
    }

    try
    {
      Enumeration enum = properties.keys();
      Preferences pref = base.node(configPath);
      pref.clear();
      while (enum.hasMoreElements())
      {
        String key = (String) enum.nextElement();
        String value = properties.getProperty(key);
        if (value != null)
        {
          pref.put(key,value);
        }
      }
      pref.sync();
    }
    catch (BackingStoreException be)
    {
      throw new ConfigStoreException("Failed to store config" + configPath, be);
    }
  }

  /**
   * Loads the properties from the given path, specifying the given properties
   * as default.
   *
   * @param configPath
   * @return
   */
  public Properties loadProperties(String configPath, Properties defaults)
      throws ConfigStoreException
  {
    if (ConfigFactory.isValidPath(configPath) == false)
    {
      throw new IllegalArgumentException("The give path is not valid.");
    }

    try
    {
      Properties props = new Properties();
      Preferences pref = base.node(configPath);
      String [] keys = pref.keys();
      for (int i = 0; i < keys.length; i++)
      {
        String key = keys[i];
        String value = pref.get(key, null);
        if (value != null)
        {
          props.setProperty(key,value);
        }
      }
      return props;
    }
    catch (BackingStoreException be)
    {
      throw new ConfigStoreException("Failed to load config" + configPath, be);
    }
  }

  /**
   * Tests, whether some configuration data exists for the given configuration.
   * @param configPath the configuration path to the property storage.
   * @return true, if there are properties under this path, false otherwise.
   */
  public boolean existsProperties(String configPath)
  {
    if (ConfigFactory.isValidPath(configPath) == false)
    {
      throw new IllegalArgumentException("The give path is not valid.");
    }

    try
    {
      return base.nodeExists(configPath);
    }
    catch (BackingStoreException bse)
    {
      return false;
    }
  }
}
