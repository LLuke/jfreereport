/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
 *
 * (C) Copyright 2000-2003, by Simba Management Limited and Contributors.
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
 * ------------------------------
 * AbstractExportPlugin.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id$
 *
 * Changes 
 * -------------------------
 * 18.06.2003 : Initial version
 *  
 */

package com.jrefinery.report.preview;

public abstract class AbstractExportPlugin implements ExportPlugin
{
  /** The backend to perform post or preprocessing. */
  private PreviewProxyBase base;
  private PreviewProxy proxy;

  public AbstractExportPlugin()
  {
  }

  /**
   * Returns true if the action is separated, and false otherwise.
   *
   * @return A boolean.
   */
  public boolean isSeparated()
  {
    return false;
  }

  /**
   * Returns an error description for the last operation.
   *
   * @return returns a error description.
   */
  public String getFailureDescription()
  {
    return getDisplayName() + " failed on export.";
  }

  /**
   * Returns true, when this export plugin is used to configure the report or an other
   * plugin.
   *
   * @return true if this is a control plugin, false otherwise.
   */
  public boolean isControlPlugin()
  {
    return false;
  }

  /**
   * Initializes the plugin to work with the given PreviewProxy.
   *
   * @param proxy
   */
  public void init(PreviewProxy proxy)
  {
    if (proxy == null)
    {
      throw new NullPointerException("Proxy must not be null.");
    }
    if (proxy.getBase() == null)
    {
      throw new NullPointerException("Proxy must be initialized.");
    }
    this.base = proxy.getBase();
    this.proxy = proxy;
  }

  public PreviewProxyBase getBase()
  {
    return base;
  }

  public PreviewProxy getProxy()
  {
    return proxy;
  }

  /**
   * Returns true if the action should be added to the toolbar, and false otherwise.
   *
   * @return A boolean.
   */
  public boolean isAddToToolbar()
  {
    return false;
  }
}
