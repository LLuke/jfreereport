/**
 * ===========================================================
 * LibRepository : a free Java content repository access layer
 * ===========================================================
 *
 * Project Info:  http://jfreereport.pentaho.org/librepository/
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
 * LibRepositoryInfo.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 */

package org.jfree.repository;

import java.util.Arrays;

import org.jfree.ui.about.ProjectInfo;
import org.jfree.ui.about.Licences;
import org.jfree.ui.about.Contributor;
import org.jfree.JCommon;

/**
 * Creation-Date: 13.11.2006, 11:27:20
 *
 * @author Thomas Morgner
 */
public class LibRepositoryInfo extends ProjectInfo
{
  private static LibRepositoryInfo instance;

  public static synchronized LibRepositoryInfo getInstance()
  {
    if (instance == null)
    {
      instance = new LibRepositoryInfo();
    }
    return instance;
  }

  public LibRepositoryInfo()
  {
    setName("LibRepository");
    setVersion("0.1.0");

    setLicenceName("LGPL");
    setLicenceText(Licences.getInstance().getLGPL());

    setInfo("http://jfreereport.pentaho.org/librepository/");
    setCopyright ("(C)opyright 2006, by Pentaho Corporation and Contributors");
    setLicenceText(Licences.getInstance().getLGPL());

    setContributors(Arrays.asList(new Contributor[]
    {
      new Contributor("Thomas Morgner", "taqua@users.sourceforge.net"),
    }));

    setBootClass(LibRepositoryBoot.class.getName());
    setAutoBoot(true);

    addLibrary(JCommon.INFO);
  }
}
