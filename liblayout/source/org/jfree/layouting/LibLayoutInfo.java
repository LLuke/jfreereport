/**
 * ===========================================
 * LibLayout : a free Java layouting library
 * ===========================================
 *
 * Project Info:  http://www.jfree.org/liblayout/
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2005, by Object Refinery Limited and Contributors.
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
 * LibLayoutInfo.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id$
 *
 * Changes
 * -------
 *
 *
 */

package org.jfree.layouting;

import java.util.Arrays;

import org.jfree.JCommon;
import org.jfree.resourceloader.LibLoaderInfo;
import org.jfree.ui.about.Contributor;
import org.jfree.ui.about.ProjectInfo;

public class LibLayoutInfo extends ProjectInfo
{

  private static LibLayoutInfo info;

  /**
   * Constructs an empty project info object.
   */
  private LibLayoutInfo ()
  {
    setName("LibLayout");
    setVersion("pre-alpha-01");
    setInfo("http://www.jfree.org/jfreereport/index.html");
    setCopyright ("(C)opyright 2000-2004, by Thomas Morgner, " +
            "Object Refinery Limited and Contributors");

    setContributors(Arrays.asList(
        new Contributor[]
        {
          new Contributor("Thomas Morgner", "taqua@users.sourceforge.net"),
        }
    ));

    addLibrary(JCommon.INFO);
    addLibrary(LibLoaderInfo.getInstance());

    setBootClass(LibLayoutBoot.class.getName());
    setAutoBoot(true);
  }

  public static ProjectInfo getInstance()
  {
    if (info == null)
    {
      info = new LibLayoutInfo();
    }
    return info;
  }
}
