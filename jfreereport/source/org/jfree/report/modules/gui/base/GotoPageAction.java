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
 * -------------------
 * GotoPageAction.java
 * -------------------
 * (C)opyright 2002, 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: GotoPageAction.java,v 1.7 2005/01/25 00:01:16 taqua Exp $
 *
 * Changes
 * -------
 * 31-May-2002 : Initial version
 * 04-Jun-2002 : Documentation
 * 09-Jun-2002 : Removed the action command, actions connect directly to their source.
 * 29-Aug-2002 : Downport to JDK 1.2.2
 * 04-Dec-2002 : Added ActionDowngrade reference to resolve ambiguity (compile error) (DG);
 */

package org.jfree.report.modules.gui.base;

import org.jfree.report.util.ImageUtils;
import org.jfree.ui.action.AbstractActionDowngrade;
import org.jfree.ui.action.ActionDowngrade;
import org.jfree.util.ResourceBundleSupport;

/**
 * The GotoPageAction is used to direclty jump to a page. A simple dialog opens to ask the
 * user for the page. This is the abstract base for the action doing the localisation
 * specific initialisation.
 *
 * @author Thomas Morgner
 */
public abstract class GotoPageAction extends AbstractActionDowngrade
{
  /**
   * Constructs a new action.
   *
   * @param resources Localised resources for the action.
   */
  protected GotoPageAction (final ResourceBundleSupport resources)
  {
    this.putValue(NAME, resources.getString("action.gotopage.name"));
    this.putValue(SHORT_DESCRIPTION, resources.getString("action.gotopage.description"));
    this.putValue(ActionDowngrade.MNEMONIC_KEY,
            resources.getMnemonic("action.gotopage.mnemonic"));
    this.putValue(ActionDowngrade.ACCELERATOR_KEY,
            resources.getKeyStroke("action.gotopage.accelerator"));
    this.putValue(SMALL_ICON, ImageUtils.createTransparentIcon(16, 16));
    this.putValue("ICON24", ImageUtils.createTransparentIcon(24, 24));
  }

}