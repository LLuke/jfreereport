/**
 * ===========================================
 * JFreeReport : a free Java reporting library
 * ===========================================
 *
 * Project Info:  http://www.jfree.org/liblayout/
 *
 * (C) Copyright 2006, by Pentaho Corperation and Contributors.
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
 * Java14ConfigStorage.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corperation.
 */

package org.jfree.report.ext.modules.misc.java14config;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.Properties;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import org.jfree.base.config.HierarchicalConfiguration;
import org.jfree.base.config.ModifiableConfiguration;
import org.jfree.report.modules.preferences.base.ConfigFactory;
import org.jfree.report.modules.preferences.base.ConfigStorage;
import org.jfree.report.modules.preferences.base.ConfigStoreException;
import org.jfree.util.Configuration;

/**
 * A configuration storage provider which stores the entries using the
 * JDK 1.4 configuration API.
 *
 * @author Thomas Morgner
 */
public class Java14ConfigStorage implements ConfigStorage
{
  /** The preferences node used to store the configuration. */
  private Preferences base;

  /**
   * Creates a new storage, which uses the given preferences
   * node as base for all operations.
   *
   * @param base the base node.
   */
  public Java14ConfigStorage(Preferences base)
  {
    this.base = base;
  }

  /**
   * Stores the given properties on the defined path.
   *
   * @param configPath the path on where to store the properties.
   * @param properties the properties which should be stored.
   * @throws org.jfree.report.modules.preferences.base.ConfigStoreException
   *          if an error occured.
   */
  public void store(String configPath, Configuration config)
      throws ConfigStoreException
  {
    if (ConfigFactory.isValidPath(configPath) == false)
    {
      throw new IllegalArgumentException("The give path is not valid.");
    }

    try
    {
      final Enumeration keys = config.getConfigProperties();
      Preferences pref = base.node(configPath);
      pref.clear();
      while (keys.hasMoreElements())
      {
        String key = (String) keys.nextElement();
        String value = config.getConfigProperty(key);
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
   * @param configPath the configuration path from where to read the
   *                   properties.
   * @param defaults   the property set that acts as fallback to provide default
   *                   values.
   * @return the loaded properties
   * @throws org.jfree.report.modules.preferences.base.ConfigStoreException
   *          if an error occured.
   */
  public Configuration load(String configPath, Configuration defaults)
      throws ConfigStoreException
  {
    if (ConfigFactory.isValidPath(configPath) == false)
    {
      throw new IllegalArgumentException("The give path is not valid.");
    }

    try
    {
      final Properties props = new Properties();
      final Preferences pref = base.node(configPath);
      final String [] keysArray = pref.keys();
      for (int i = 0; i < keysArray.length; i++)
      {
        String key = keysArray[i];
        String value = pref.get(key, null);
        if (value != null)
        {
          props.setProperty(key,value);
        }
      }

      final ModifiableConfiguration config = new HierarchicalConfiguration(defaults);
      final Iterator keys = props.keySet().iterator();
      while (keys.hasNext())
      {
        String key = (String) keys.next();
        config.setConfigProperty(key, props.getProperty(key));
      }
      return config;
    }
    catch (BackingStoreException be)
    {
      throw new ConfigStoreException("Failed to load config" + configPath, be);
    }
  }

  /**
   * Tests, whether some configuration data exists for the given configuration.
   *
   * @param configPath the configuration path to the property storage.
   * @return true, if there are properties under this path, false otherwise.
   */
  public boolean isAvailable(String configPath)
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
