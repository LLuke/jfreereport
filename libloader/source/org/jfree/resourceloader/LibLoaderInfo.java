/**
 * ================================================
 * LibLoader : a free Java resource loading library
 * ================================================
 *
 * Project Info:  http://jfreereport.pentaho.org/libloader/
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
 *
 * ------------
 * $Id: LibLoaderInfo.java,v 1.8 2006/12/11 12:29:09 taqua Exp $
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 */

package org.jfree.resourceloader;

import java.util.Arrays;

import org.jfree.JCommon;
import org.jfree.base.Library;
import org.jfree.ui.about.Contributor;
import org.jfree.ui.about.ProjectInfo;
import org.jfree.ui.about.Licences;

public class LibLoaderInfo extends ProjectInfo
{
  private static LibLoaderInfo instance;

  public static LibLoaderInfo getInstance ()
  {
    if (instance == null)
    {
      instance = new LibLoaderInfo();
    }
    return instance;
  }

  /**
   * Constructs an empty project info object.
   */
  public LibLoaderInfo ()
  {
    setName("LibLoader");
    setVersion("0.2.2");

    setLicenceName("LGPL");
    setLicenceText(Licences.getInstance().getLGPL());

    setInfo("http://jfreereport.pentaho.org/libloader/");
    setCopyright ("(C)opyright 2006, by Pentaho Corporation and Contributors");
    setLicenceText(Licences.getInstance().getLGPL());

    setContributors(Arrays.asList(new Contributor[]
    {
      new Contributor("Thomas Morgner", "taqua@users.sourceforge.net"),
    }));


    setBootClass(LibLoaderBoot.class.getName());
    setAutoBoot(true);

    addLibrary(JCommon.INFO);
    addOptionalLibrary("org.jfree.pixie.PixieInfo");
    addOptionalLibrary(
        new Library(
            "OSCache", "2.3", "The OpenSymphony Software License", "http://www.opensymphony.com/oscache/license.action"
        )
    );
    addOptionalLibrary(
        new Library(
            "EHCache", "1.2rc1", "Apache Licence 2.0", "http://ehcache.sourceforge.net/"
        )
    );

  }
}
