/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2003, by Object Refinery Limited and Contributors.
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
 * ZoomInAction.java
 * -----------------
 * (C)opyright 2002, 2003, by Object Refinery Limited and Contributors.
 *
 * Original Author:  David Gilbert (for Object Refinery Limited);
 * Contributor(s):   Thomas Morgner;
 *
 * $Id: ZoomInAction.java,v 1.3 2004/03/16 15:09:24 taqua Exp $
 *
 * Changes
 * -------
 * 09-Jun-2002 : Removed the action command, actions connect directly to their source.
 * 29-Aug-2002 : Downport to JDK 1.2.2
 * 04-Dec-2002 : Added ActionDowngrade reference to resolve ambiguity (compile error) (DG);
 *
 */
package org.jfree.report.modules.gui.base;

import java.util.ResourceBundle;

import org.jfree.report.modules.gui.base.components.AbstractActionDowngrade;
import org.jfree.report.modules.gui.base.components.ActionDowngrade;

/**
 * The zoomIn action is used to increase the ZoomFactor of the report. Zooming
 * can be increased in predefined steps from 25% to 400%. This abstract base class
 * implements the locales specific initialisations, actual work is done in an internal
 * subclass in PreviewFrame.
 *
 * @author David Gilbert
 */
public abstract class ZoomInAction extends AbstractActionDowngrade
{
  /**
   * Constructs a new action.
   *
   * @param resources  localised resources.
   */
  protected ZoomInAction(final ResourceBundle resources)
  {
    putValue(NAME, resources.getString("action.zoomIn.name"));
    putValue(SHORT_DESCRIPTION, resources.getString("action.zoomIn.description"));
    putValue(ActionDowngrade.MNEMONIC_KEY,
        ResourceBundleUtils.createMnemonic(resources.getString("action.zoomIn.mnemonic")));
    putValue(ActionDowngrade.ACCELERATOR_KEY,
        ResourceBundleUtils.createMenuKeystroke(resources.getString("action.zoomIn.accelerator")));
    putValue(SMALL_ICON,
        ResourceBundleUtils.getIcon(resources.getString("action.zoomIn.small-icon")));
    putValue("ICON24",
        ResourceBundleUtils.getIcon(resources.getString("action.zoomIn.icon")));
  }

}
