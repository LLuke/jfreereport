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
 * ResourceBundleSkin.java
 * ---------
 *
 * Original Author:  Thomas Morgner;
 * Contributors: -;
 *
 * $Id: ResourceBundleSkin.java,v 1.1 2005/12/07 22:21:11 taqua Exp $
 *
 * Changes
 * -------------------------
 * 07.12.2005 : Initial version
 */
package org.jfree.report.modules.gui.base;

import java.util.ResourceBundle;
import javax.swing.Icon;

import org.jfree.util.ResourceBundleSupport;
import org.jfree.report.JFreeReportBoot;

/**
 * Creation-Date: 07.12.2005, 20:07:45
 *
 * @author Thomas Morgner
 */
public class ResourceBundleSkin implements Skin
{
  private ResourceBundleSupport resourceBundleSupport;


  public ResourceBundleSkin()
  {
    final String resourceBundle =
            JFreeReportBoot.getInstance().getGlobalConfig().getConfigProperty
                    ("org.jfree.report.modules.gui.base.SkinResourceBundle");
    if (resourceBundle == null)
    {
      throw new IllegalStateException("JFreeReportBoot has not been started. Initialize the system first.");
    }
    
    final ResourceBundle iconResourceBundle =
            ResourceBundle.getBundle(resourceBundle);
    resourceBundleSupport = new ResourceBundleSupport(iconResourceBundle);
  }

  public Icon getIcon(String id, boolean scale, boolean size)
  {
    if (scale)
    {
      return resourceBundleSupport.getIcon(id, size);
    }
    else
    {
      return resourceBundleSupport.getIcon(id);
    }
  }

  public ResourceBundle getResourceBundle(String id)
  {
    return ResourceBundle.getBundle(id);
  }
}
