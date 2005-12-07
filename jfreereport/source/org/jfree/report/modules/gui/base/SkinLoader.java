/**
 * ========================================
 * <libname> : a free Java <foobar> library
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
 * SkinLoader.java
 * ---------
 *
 * Original Author:  Thomas Morgner;
 * Contributors: -;
 *
 * $Id: Anchor.java,v 1.3 2005/02/23 21:04:29 taqua Exp $
 *
 * Changes
 * -------------------------
 * 07.12.2005 : Initial version
 */
package org.jfree.report.modules.gui.base;

import org.jfree.report.JFreeReportBoot;
import org.jfree.util.ObjectUtilities;

/**
 * Creation-Date: 07.12.2005, 21:54:38
 *
 * @author Thomas Morgner
 */
public class SkinLoader
{
  private SkinLoader()
  {
  }

  public static Skin loadSkin ()
  {
    final String skinClass =
            JFreeReportBoot.getInstance().getGlobalConfig().getConfigProperty
            ("org.jfree.report.modules.gui.base.Skin");
    if (skinClass != null)
    {
      Object maybeSkin =
              ObjectUtilities.loadAndInstantiate(skinClass, SkinLoader.class);
      if (maybeSkin instanceof Skin)
      {
        return (Skin) maybeSkin;
      }
    }

    return new ResourceBundleSkin();
  }
}
