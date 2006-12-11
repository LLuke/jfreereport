/**
 * ===========================================
 * JFreeReport : a free Java reporting library
 * ===========================================
 *
 * Project Info:  http://jfreereport.pentaho.org/
 *
 * (C) Copyright 2006, by Pentaho Corporation and Contributors.
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
 * JFreeReportDemoInfo.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 */

package org.jfree.report.demo;

import java.util.Arrays;

import org.jfree.JCommon;
import org.jfree.report.JFreeReportInfo;
import org.jfree.ui.about.Contributor;
import org.jfree.ui.about.ProjectInfo;
import org.jfree.xmlns.LibXmlBoot;

/**
 * Creation-Date: 02.12.2006, 19:20:18
 *
 * @author Thomas Morgner
 */
public class JFreeReportDemoInfo extends ProjectInfo
{
  private static JFreeReportDemoInfo info;

  /**
   * Constructs an empty project info object.
   */
  public JFreeReportDemoInfo ()
  {
    setName("JFreeReport-Demo");
    setVersion("0.9.0-01");
    setInfo("http://jfreereport.pentaho.org");
    setCopyright ("(C)opyright 2006, by Thomas Morgner, " +
            "Object Refinery Limited and Contributors");

    setContributors(Arrays.asList(
        new Contributor[]
        {
          new Contributor("Thomas Morgner", "taqua@users.sourceforge.net"),
        }
    ));

    addLibrary(JCommon.INFO);
    addLibrary(JFreeReportInfo.getInstance());

    setBootClass(LibXmlBoot.class.getName());
    setAutoBoot(true);
  }


  public static synchronized ProjectInfo getInstance()
  {
    if (info == null)
    {
      info = new JFreeReportDemoInfo();
    }
    return info;
  }
}
