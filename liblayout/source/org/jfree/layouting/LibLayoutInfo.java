/**
 * ===========================================
 * LibLayout : a free Java layouting library
 * ===========================================
 *
 * Project Info:  http://jfreereport.pentaho.org/liblayout/
 *
 * (C) Copyright 2000-2005, by Pentaho Corporation and Contributors.
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
 * $Id: LibLayoutInfo.java,v 1.5 2006/12/03 18:57:49 taqua Exp $
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 */

package org.jfree.layouting;

import java.util.Arrays;

import org.jfree.JCommon;
import org.jfree.fonts.LibFontInfo;
import org.jfree.repository.LibRepositoryInfo;
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
    setVersion("0.2.1");
    setInfo("http://jfreereport.pentaho.org/liblayout/");
    setCopyright ("(C)opyright 2006, by Pentaho Corporation and Contributors");

    setContributors(Arrays.asList(
        new Contributor[]
        {
          new Contributor("Thomas Morgner", "taqua@users.sourceforge.net"),
        }
    ));

    addLibrary(JCommon.INFO);
    addLibrary(LibLoaderInfo.getInstance());
    addLibrary(LibRepositoryInfo.getInstance());
    addLibrary(LibFontInfo.getInstance());

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
