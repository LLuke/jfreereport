/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
 *
 * (C) Copyright 2000-2002, by Simba Management Limited and Contributors.
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
 * -----------------
 * SaveAsAction.java
 * -----------------
 * (C)opyright 2002, by Simba Management Limited and Contributors.
 *
 * Original Author:  David Gilbert (for Simba Management Limited);
 * Contributor(s):   Thomas Morgner;
 *
 * $Id: SaveAsAction.java,v 1.15 2003/06/23 16:08:27 taqua Exp $
 *
 * Changes
 * -------
 * 07-May-2002 : Version 1 (DG);
 * 10-May-2002 : Removed actionhandling from class. Specific handling is implemented based on
 *               target environment. (TM)
 * 16-May-2002 : Load images from jar (JS)
 * 09-Jun-2002 : Removed the action command, actions connect directly to their source.
 * 29-Aug-2002 : Downport to JDK 1.2.2
 * 04-Dec-2002 : Added ActionDowngrade reference to resolve ambiguity (compile error) (DG);
 */

package com.jrefinery.report.action;

import java.util.ResourceBundle;

import com.jrefinery.report.util.AbstractActionDowngrade;
import com.jrefinery.report.util.ActionDowngrade;

/**
 * Save (to PDF) action for a print preview frame.
 *
 * @deprecated Export modules are now defined by plugin-interfaces.
 * @author David Gilbert
 */
public abstract class SaveAsAction extends AbstractActionDowngrade
{

  /**
   * Constructs a new action.
   *
   * @param resources  localised resources.
   */
  public SaveAsAction(ResourceBundle resources)
  {
    this.putValue(NAME, resources.getString("action.save-as.name"));
    this.putValue(SHORT_DESCRIPTION, resources.getString("action.save-as.description"));
    this.putValue(ActionDowngrade.MNEMONIC_KEY,
        resources.getObject("action.save-as.mnemonic"));
    this.putValue(ActionDowngrade.ACCELERATOR_KEY,
        resources.getObject("action.save-as.accelerator"));
    this.putValue(SMALL_ICON, resources.getObject("action.save-as.small-icon"));
    this.putValue("ICON24", resources.getObject("action.save-as.icon"));
  }
}
