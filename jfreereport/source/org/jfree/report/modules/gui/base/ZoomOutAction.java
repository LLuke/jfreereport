/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
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
 * ------------------
 * ZoomOutAction.java
 * ------------------
 * (C)opyright 2002, 2003, by Simba Management Limited and Contributors.
 *
 * Original Author:  David Gilbert (for Simba Management Limited);
 * Contributor(s):   Thomas Morgner;
 *
 * $Id: ZoomOutAction.java,v 1.1 2003/07/07 22:44:05 taqua Exp $
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
 * The zoomOut action is used to decrease the ZoomFactor of the report. Zooming
 * can be increased in predefined steps from 25% to 400%. This abstract base class
 * implements the locales specific initialisations, actual work is done in an internal
 * subclass in PreviewFrame.
 *
 * @author David Gilbert
 */
public abstract class ZoomOutAction extends AbstractActionDowngrade
{
  /**
   * Constructs a new action.
   *
   * @param resources  localised resources.
   */
  protected ZoomOutAction(final ResourceBundle resources)
  {
    putValue(NAME, resources.getString("action.zoomOut.name"));
    putValue(SHORT_DESCRIPTION, resources.getString("action.zoomOut.description"));
    putValue(ActionDowngrade.MNEMONIC_KEY,
        resources.getObject("action.zoomOut.mnemonic"));
    putValue(ActionDowngrade.ACCELERATOR_KEY,
        resources.getObject("action.zoomOut.accelerator"));
    putValue(SMALL_ICON, resources.getObject("action.zoomOut.small-icon"));
    putValue("ICON24", resources.getObject("action.zoomOut.icon"));
  }

}
