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
 * ConfigFactory.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: ConfigFactory.java,v 1.2 2003/07/23 16:02:20 taqua Exp $
 *
 * Changes 
 * -------------------------
 * 14-Jul-2003 : Initial version
 *  
 */

package org.jfree.report.modules.misc.configstore.base;

/**
 * The config factory is used to access the currently active config storage 
 * implementation. The implementation itself allows to read or store a set
 * of properties stored under a certain path.
 * 
 * @author Thomas Morgner
 */
public final class ConfigFactory
{
  /** 
   * The selector configuration key that defines the active config storage
   * implementation
   */
  public static final String CONFIG_TARGET_KEY = "org.jfree.report.ConfigStore";

  /** The singleton instance of the config factory. */
  private static ConfigFactory factory;
  /** The user storage is used to store user dependend settings. */
  private ConfigStorage userStorage;
  /** The system storage is used to store system wide settings. */
  private ConfigStorage systemStorage;

  /**
   * Returns the singleton instance of the config factory.
   * 
   * @return the config factory
   */
  public static ConfigFactory getInstance()
  {
    if (factory == null)
    {
      factory = new ConfigFactory();
      factory.defineSystemStorage(new NullConfigStorage());
      factory.defineUserStorage(new NullConfigStorage());
    }
    return factory;
  }

  /**
   * DefaultConstructor.
   */
  private ConfigFactory ()
  {
  }
  
  /**
   * Defines the user storage implementation that should be used.
   * This method should only be called by the module initialization
   * methods.
   * 
   * @param storage the user settings storage implementation.
   */
  public void defineUserStorage (ConfigStorage storage)
  {
    if (storage == null)
    {
      throw new NullPointerException();
    }
    this.userStorage  = storage;
  }

  /**
   * Defines the system storage implementation that should be used.
   * This method should only be called by the module initialization
   * methods.
   * 
   * @param storage the system settings storage implementation.
   */
  public void defineSystemStorage (ConfigStorage storage)
  {
    if (storage == null)
    {
      throw new NullPointerException();
    }
    this.systemStorage = storage;
  }

  /**
   * Returns the user settings storage implementation used in the config subsystem.
   * 
   * @return the user settingsstorage provider.
   */
  public ConfigStorage getUserStorage ()
  {
    return systemStorage;
  }

  /**
   * Returns the system settings storage implementation used in the config subsystem.
   * 
   * @return the system settings storage provider.
   */
  public ConfigStorage getSystemStorage ()
  {
    return userStorage;
  }

  /**
   * Checks, whether the given string denotes a valid config storage path.
   * Such an path must not contain whitespaces or non-alphanumeric characters.
   * 
   * @param path the path that should be tested.
   * @return true, if the path is valid, false otherwise.
   */
  public static boolean isValidPath (String path)
  {
    char[] data = path.toCharArray();
    for (int i = 0; i < data.length; i++)
    {
      if (Character.isJavaIdentifierPart(data[i]) == false)
      {
        return false;
      }
    }
    return true;
  }
}
