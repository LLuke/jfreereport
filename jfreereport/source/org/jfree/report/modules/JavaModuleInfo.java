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
 * JavaModuleInfo.java
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
 * 05.07.2003 : Initial version
 *  
 */

package org.jfree.report.modules;

public class JavaModuleInfo extends DefaultModuleInfo
{
  public JavaModuleInfo()
  {
    setModuleClass("java.lang.System");
    setupJavaVersion();
  }

  private void setupJavaVersion ()
  {
    String jVersion = System.getProperty("java.version");
    // something like 1.4.1_02
    // major version is 1
    // minor version is 4
    // patch level is 1_02
    String major = "";
    String minor = "";
    String patch = "";
    int majorpos = jVersion.indexOf('.');
    if (majorpos != -1)
    {
      major = jVersion.substring(0, majorpos);
      int minorpos = jVersion.indexOf(majorpos, '.');
      if (minorpos != -1)
      {
        minor = jVersion.substring(majorpos, minorpos);
        patch = jVersion.substring(minorpos);
      }
    }
    setMajorVersion(major);
    setMinorVersion(minor);
    setPatchLevel(patch);
  }
}
