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
 * $Id$
 *
 * Changes 
 * -------------------------
 * 14.07.2003 : Initial version
 *  
 */

package org.jfree.report.modules.misc.configstore.base;

public class ConfigFactory
{
  public static final String CONFIG_TARGET_KEY = "org.jfree.report.ConfigStore";

  private static ConfigFactory factory;
  private ConfigStorage storage;

  public static ConfigFactory getInstance()
  {
    if (factory == null)
    {
      factory = new ConfigFactory();
      factory.defineStorage(new NullConfigStorage());
    }
    return factory;
  }

  public void defineStorage (ConfigStorage storage)
  {
    if (storage == null)
    {
      throw new NullPointerException();
    }
    this.storage = storage;
  }

  public ConfigStorage getStorage ()
  {
    return storage;
  }

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
