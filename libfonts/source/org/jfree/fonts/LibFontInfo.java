/**
 * ========================================
 * libLayout : a free Java font reading library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/liblayout/
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2005, by Object Refinery Limited and Contributors.
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
 * ---------
 * LibFontInfo.java
 * ---------
 *
 * Original Author:  Thomas Morgner;
 * Contributors: -;
 *
 * $Id: LibFontInfo.java,v 1.2 2005/11/09 21:24:12 taqua Exp $
 *
 * Changes
 * -------------------------
 * 2005-11-07 : Initial version
 */
package org.jfree.fonts;

import org.jfree.ui.about.ProjectInfo;
import org.jfree.JCommon;

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

  private LibFontInfo()
  {
    setName("LibFont");
    setVersion("0.1.1");
    addLibrary(JCommon.INFO);
    addDependency(JCommon.INFO);
  }
}
