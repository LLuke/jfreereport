/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
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
 * ConfigTreeModuleNode.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: ConfigTreeModuleNode.java,v 1.1 2003/08/30 15:05:00 taqua Exp $
 *
 * Changes 
 * -------------------------
 * 28.08.2003 : Initial version
 *  
 */

package org.jfree.report.modules.gui.config.model;

import java.util.ArrayList;

import org.jfree.report.modules.Module;
import org.jfree.report.util.ReportConfiguration;

public class ConfigTreeModuleNode extends AbstractConfigTreeNode
{
  private String configurationPrefix;
  private Module module;
  private ReportConfiguration configuration;
  private ArrayList assignedKeys;

  public ConfigTreeModuleNode(Module module, ReportConfiguration config)
  {
    super(module.getName());
    this.assignedKeys = new ArrayList();
    this.configuration = config;
    this.module = module;
    configurationPrefix = ModuleNodeFactory.getPackage(this.module.getClass());
  }

  public Module getModule()
  {
    return module;
  }

  public ReportConfiguration getConfiguration()
  {
    return configuration;
  }

  public String getConfigurationPrefix()
  {
    return configurationPrefix;
  }

  public String toString ()
  {
    return getConfigurationPrefix();
  }

  /**
   * Returns true if the receiver is a leaf.
   */
  public boolean isLeaf()
  {
    return true;
  }

  /**
   * Returns true if the receiver allows children.
   */
  public boolean getAllowsChildren()
  {
    return false;
  }

  public void addAssignedKey (ConfigDescriptionEntry key)
  {
    if (key == null)
    {
      throw new NullPointerException();
    }
    if (assignedKeys.contains(key) == false)
    {
      assignedKeys.add(key);
    }
  }

  public void removeAssignedKey (ConfigDescriptionEntry key)
  {
    assignedKeys.remove(key);
  }

  public ConfigDescriptionEntry[] getAssignedKeys ()
  {
    return (ConfigDescriptionEntry[]) assignedKeys.toArray
        (new ConfigDescriptionEntry[assignedKeys.size()]);
  }
}
