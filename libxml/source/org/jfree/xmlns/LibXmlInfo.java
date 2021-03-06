/**
 * =========================================
 * LibXML : a free Java layouting library
 * =========================================
 *
 * Project Info:  http://reporting.pentaho.org/libxml/
 *
 * (C) Copyright 2006, by Object Refinery Ltd, Pentaho Corporation and Contributors.
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
 * $Id: LibXmlInfo.java,v 1.11 2007/06/03 17:07:08 taqua Exp $
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 */

package org.jfree.xmlns;

import java.util.Arrays;

import org.jfree.JCommon;
import org.jfree.resourceloader.LibLoaderInfo;
import org.jfree.ui.about.Contributor;
import org.jfree.ui.about.ProjectInfo;

/**
 * The LibXmlInfo class contains all dependency information and some
 * common information like version, license and contributors about the
 * library itself.
 *
 * @author Thomas Morgner
 */
public class LibXmlInfo extends ProjectInfo
{
  private static LibXmlInfo info;

  /**
   * Constructs an empty project info object.
   */
  private LibXmlInfo ()
  {
    setName("LibXML");
    setVersion("0.9.6");
    setInfo("http://reporting.pentaho.org/libxml/");
    setCopyright ("(C)opyright 2007, by Object Refinery Limited, Pentaho Corporation and Contributors");

    setContributors(Arrays.asList(
        new Contributor[]
        {
          new Contributor("Peter Becker", "-"),
          new Contributor("Thomas Morgner", "taqua@users.sourceforge.net"),
          new Contributor("Michael D'Amourr", "mdamour1976@users.sourceforge.net"),
        }
    ));

    addLibrary(JCommon.INFO);
    addLibrary(LibLoaderInfo.getInstance());

    setBootClass(LibXmlBoot.class.getName());
    setAutoBoot(true);
  }

  /**
   * Returns the singleton instance of the boot-class.
   *
   * @return the singleton booter.
   */
  public static synchronized ProjectInfo getInstance()
  {
    if (info == null)
    {
      info = new LibXmlInfo();
    }
    return info;
  }
}
