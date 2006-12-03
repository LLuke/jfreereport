/**
 * ===========================================
 * LibFonts : a free Java font reading library
 * ===========================================
 *
 * Project Info:  http://jfreereport.pentaho.org/libfonts/
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
 * $Id$
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 */
package org.jfree.fonts;

import java.util.Arrays;

import org.jfree.ui.about.ProjectInfo;
import org.jfree.ui.about.Licences;
import org.jfree.ui.about.Contributor;
import org.jfree.JCommon;
import org.jfree.resourceloader.LibLoaderInfo;

/**
 * Creation-Date: 06.11.2005, 18:24:57
 *
 * @author Thomas Morgner
 */
public class LibFontInfo extends ProjectInfo
{
  private static LibFontInfo instance;

  public static synchronized LibFontInfo getInstance()
  {
    if (instance == null)
    {
      instance = new LibFontInfo();
    }
    return instance;
  }

  public LibFontInfo()
  {
    setName("LibFonts");
    setVersion("0.2.2");

    setLicenceName("LGPL");
    setLicenceText(Licences.getInstance().getLGPL());

    setInfo("http://jfreereport.pentaho.org/libfonts/");
    setCopyright ("(C)opyright 2006, by Pentaho Corporation and Contributors");
    setLicenceText(Licences.getInstance().getLGPL());

    setContributors(Arrays.asList(new Contributor[]
    {
      new Contributor("Thomas Morgner", "taqua@users.sourceforge.net"),
    }));

    setBootClass("org.jfree.fonts.LibFontBoot");
    setAutoBoot(true);

    addLibrary(JCommon.INFO);
    addLibrary(LibLoaderInfo.getInstance());
  }
}
