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
 * AbstractModuleInfo.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: DefaultModuleInfo.java,v 1.1 2003/07/07 22:44:05 taqua Exp $
 *
 * Changes
 * -------------------------
 * 05.07.2003 : Initial version
 *
 */

package org.jfree.report.modules;

public class DefaultModuleInfo implements ModuleInfo
{
  private String moduleClass;
  private String majorVersion;
  private String minorVersion;
  private String patchLevel;

  public DefaultModuleInfo()
  {
  }

  public DefaultModuleInfo(String moduleClass, String majorVersion,
                           String minorVersion, String patchLevel)
  {
    this.moduleClass = moduleClass;
    this.majorVersion = majorVersion;
    this.minorVersion = minorVersion;
    this.patchLevel = patchLevel;
  }

  public String getModuleClass()
  {
    return moduleClass;
  }

  protected void setModuleClass(String moduleClass)
  {
    this.moduleClass = moduleClass;
  }

  public String getMajorVersion()
  {
    return majorVersion;
  }

  protected void setMajorVersion(String majorVersion)
  {
    this.majorVersion = majorVersion;
  }

  public String getMinorVersion()
  {
    return minorVersion;
  }

  protected void setMinorVersion(String minorVersion)
  {
    this.minorVersion = minorVersion;
  }

  public String getPatchLevel()
  {
    return patchLevel;
  }

  protected void setPatchLevel(String patchLevel)
  {
    this.patchLevel = patchLevel;
  }

  /**
   * Two moduleinfos are equal,if they have the same module class.
   * @param o
   * @return
   */
  public boolean equals(Object o)
  {
    if (this == o) return true;
    if (!(o instanceof DefaultModuleInfo)) return false;

    final ModuleInfo defaultModuleInfo = (ModuleInfo) o;

    if (!moduleClass.equals(defaultModuleInfo.getModuleClass()))
    {
      return false;
    }
    return true;
  }

  public int hashCode()
  {
    int result;
    result = moduleClass.hashCode();
    return result;
  }

  public String toString ()
  {
    StringBuffer buffer = new StringBuffer();
    buffer.append(getClass().getName());
    buffer.append("={ModuleClass=");
    buffer.append(getModuleClass());
    if (getMajorVersion() != null)
    {
      buffer.append("; Version=");
      buffer.append(getMajorVersion());
      if (getMinorVersion() != null)
      {
        buffer.append("-");
        buffer.append(getMinorVersion());
        if (getPatchLevel() != null)
        {
          buffer.append("_");
          buffer.append(getPatchLevel());
        }
      }
    }
    buffer.append("}");
    return buffer.toString();
  }
}
