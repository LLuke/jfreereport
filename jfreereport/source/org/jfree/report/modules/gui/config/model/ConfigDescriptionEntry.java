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
 * ConfigDescriptionEntry.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: ConfigDescriptionEntry.java,v 1.2 2003/09/08 18:11:49 taqua Exp $
 *
 * Changes
 * -------------------------
 * 26-Aug-2003 : Initial version
 *
 */

package org.jfree.report.modules.gui.config.model;

/**
 * A config description entry provides a declaration of a single
 * report configuration key and speicifes rules for the values of that
 * key.
 * 
 * @author Thomas Morgner
 */
public abstract class ConfigDescriptionEntry
{
  /** A description of the given key. */
  private String description;
  /** The fully qualified name of the key. */
  private String keyName;
  /** a flag defining whether this is a boot time key. */
  private boolean global;

  /**
   * Creates a new config description entry with the given name.
   * 
   * @param keyName the name of the entry.
   */
  public ConfigDescriptionEntry(String keyName)
  {
    if (keyName == null)
    {
      throw new NullPointerException();
    }
    this.keyName = keyName;
  }

  /**
   * Returns the full key name of the configuration description.
   * @return the key name.
   */
  public String getKeyName()
  {
    return keyName;
  }

  /**
   * Returns the descrption of the configuration entry.
   *  
   * @return the key description.
   */
  public String getDescription()
  {
    return description;
  }

  /**
   * Defines the descrption of the configuration entry.
   *  
   * @param description the key description.
   */
  public void setDescription(String description)
  {
    this.description = description;
  }

  /**
   * Returns, whether the key is a global key. Global keys are read
   * from the global report configuration and specifying them in the
   * report local configuration is useless.
   *  
   * @return true, if the key is global, false otherwise.
   */
  public boolean isGlobal()
  {
    return global;
  }

  /**
   * Defines, whether the key is a global key. Global keys are read
   * from the global report configuration and specifying them in the
   * report local configuration is useless.
   *  
   * @param global set to true, if the key is global, false otherwise.
   */
  public void setGlobal(boolean global)
  {
    this.global = global;
  }

  /**
   * Checks, whether the given object is equal to this config description
   * entry. The object will be equal, if it is also an config description
   * entry with the same name as this entry.
   *  
   * @see java.lang.Object#equals(java.lang.Object)
   * 
   * @param o the other object.
   * @return true, if the config entry is equal to the given object, false otherwise.
   */
  public boolean equals(Object o)
  {
    if (this == o)
    { 
      return true;
    }
    if (!(o instanceof ConfigDescriptionEntry))
    { 
      return false;
    }

    final ConfigDescriptionEntry configDescriptionEntry = (ConfigDescriptionEntry) o;

    if (!keyName.equals(configDescriptionEntry.keyName))
    { 
      return false;
    }

    return true;
  }

  /**
   * Computes an hashcode for this object. 
   * @see java.lang.Object#hashCode()
   * 
   * @return the hashcode.
   */
  public int hashCode()
  {
    return keyName.hashCode();
  }
}
