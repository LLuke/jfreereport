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
 * $Id: ConfigDescriptionEntry.java,v 1.1 2003/08/27 20:19:53 taqua Exp $
 *
 * Changes
 * -------------------------
 * 26.08.2003 : Initial version
 *
 */

package org.jfree.report.modules.gui.config;

public abstract class ConfigDescriptionEntry
{
  private String description;
  private String keyName;
  private boolean global;

  public ConfigDescriptionEntry(String keyName)
  {
    if (keyName == null)
    {
      throw new NullPointerException();
    }
    this.keyName = keyName;
  }

  public String getKeyName()
  {
    return keyName;
  }

  public String getDescription()
  {
    return description;
  }

  public void setDescription(String description)
  {
    this.description = description;
  }

  public boolean isGlobal()
  {
    return global;
  }

  public void setGlobal(boolean global)
  {
    this.global = global;
  }

  public boolean equals(Object o)
  {
    if (this == o) return true;
    if (!(o instanceof ConfigDescriptionEntry)) return false;

    final ConfigDescriptionEntry configDescriptionEntry = (ConfigDescriptionEntry) o;

    if (!keyName.equals(configDescriptionEntry.keyName)) return false;

    return true;
  }

  public int hashCode()
  {
    return keyName.hashCode();
  }
}
