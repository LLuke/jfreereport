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
 * LibFontBoot.java
 * ---------
 *
 * Original Author:  Thomas Morgner;
 * Contributors: -;
 *
 * $Id: LibFontBoot.java,v 1.4 2006/03/17 20:19:16 taqua Exp $
 *
 * Changes
 * -------------------------
 * 2005-11-07 : Initial version
 */
package org.jfree.fonts;

import org.jfree.base.AbstractBoot;
import org.jfree.base.BootableProjectInfo;
import org.jfree.util.Configuration;
import org.jfree.util.Log;

/**
 * Creation-Date: 06.11.2005, 18:25:11
 *
 * @author Thomas Morgner
 */
public class LibFontBoot extends AbstractBoot
{
  private static LibFontBoot instance;

  public static synchronized LibFontBoot getInstance()
  {
    if (instance == null)
    {
      instance = new LibFontBoot();
    }
    return instance;
  }

  private LibFontBoot()
  {
  }

  protected Configuration loadConfiguration()
  {
    return createDefaultHierarchicalConfiguration
            ("/org/jfree/fonts/libfont.properties",
             "/libfont.properties", true);

  }

  protected void performBoot()
  {
//    Log.debug ("LibFonts ..");
  }

  protected BootableProjectInfo getProjectInfo()
  {
    return LibFontInfo.getInstance();
  }
}
