/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
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
 * ----------------
 * AboutAction.java
 * ----------------
 * (C)opyright 2002, by Simba Management Limited and Contributors.
 *
 * Original Author:  David Gilbert (for Simba Management Limited);
 * Contributor(s):   Thomas Morgner;
 *
 * $Id: AboutAction.java,v 1.2 2003/08/24 15:08:18 taqua Exp $
 *
 * Changes
 * -------
 * 07-May-2002 : Version 1 (DG);
 * 10-May-2002 : Removed actionhandling from class. Specific handling is implemented based on
 *               target environment. (TM)
 * 16-May-2002 : Load images from jar and simplified (JS)
 * 09-Jun-2002 : Removed the action command, actions connect directly to their source.
 * 29-Aug-2002 : Downport to JDK 1.2.2
 * 04-Dec-2002 : Added ActionDowngrade reference to resolve ambiguity (compile error) (DG);
 */

package org.jfree.report.modules.gui.base;

import org.jfree.ui.action.AbstractActionDowngrade;
import org.jfree.ui.action.ActionDowngrade;
import org.jfree.util.ResourceBundleSupport;

/**
 * The About action is used to show some information about the current programm, to which
 * the printpreview belongs to.
 * <p>
 * This abstract class handles the locales specific initialisation.
 *
 * @author David Gilbert
 */

public abstract class AboutAction extends AbstractActionDowngrade
{

  /**
   * Constructs a new action.
   *
   * @param resources  localised resources.
   */
  protected AboutAction(final ResourceBundleSupport resources)
  {
    putValue(NAME, resources.getString("action.about.name"));
    putValue(SHORT_DESCRIPTION, resources.getString("action.about.description"));
    putValue(ActionDowngrade.MNEMONIC_KEY,
        resources.getMnemonic("action.about.mnemonic"));
    putValue(SMALL_ICON, resources.getIcon("action.about.small-icon", false));
    putValue("ICON24", resources.getIcon("action.about.icon", true));
  }
}
