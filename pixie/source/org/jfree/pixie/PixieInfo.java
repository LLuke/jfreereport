/**
 * ========================================
 * Pixie : a free Java vector image library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/pixie/
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2005, by Object Refinery Limited and Contributors.
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
 * PixieInfo.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: PixieInfo.java,v 1.6 2006/05/15 09:25:05 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.pixie;

import java.util.Arrays;

import org.jfree.JCommon;
import org.jfree.ui.about.Contributor;
import org.jfree.ui.about.Licences;
import org.jfree.ui.about.ProjectInfo;

public class PixieInfo extends ProjectInfo
{
  private static PixieInfo singleton;

  /**
   * Returns the single instance of this class.
   *
   * @return The single instance of information about the JCommon library.
   */
  public static PixieInfo getInstance ()
  {
    if (singleton == null)
    {
      singleton = new PixieInfo();
    }
    return singleton;
  }

  /**
   * Creates a new instance. (Must be public so that we can instantiate
   * the library-info using Class.newInstance(..)).
   */
  public PixieInfo ()
  {
    setName("Pixie");
    setVersion("0.8.6");
    setInfo("http://www.jfree.org/jfreereport/pixie/");
    setCopyright ("(C)opyright 2000-2006, by Pentaho Corporation, Object Refinery Limited and Contributors");

    setLicenceName("LGPL");
    setLicenceText(Licences.getInstance().getLGPL());

    setContributors(Arrays.asList(new Contributor[]
    {
      new Contributor("David Gilbert", "david.gilbert@object-refinery.com"),
      new Contributor("Thomas Morgner", "taqua@users.sourceforge.net"),
      new Contributor("David R. Harris", "-"),
    }));

    addLibrary(JCommon.INFO);
  }
}
