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
 * -----------------------
 * PreviousPageAction.java
 * -----------------------
 * (C)opyright 2002, 2003, by Simba Management Limited and Contributors.
 *
 * Original Author:  David Gilbert (for Simba Management Limited);
 * Contributor(s):   Thomas Morgner;
 *
 * $Id: PreviousPageAction.java,v 1.7 2005/09/21 14:52:05 taqua Exp $
 *
 * Changes
 * -------
 * 09-Jun-2002 : Removed the action command, actions connect directly to their source.
 * 29-Aug-2002 : Downport to JDK 1.2.2
 * 04-Dec-2002 : Added ActionDowngrade reference to resolve ambiguity (compile error) (DG);
 */

package org.jfree.report.modules.gui.base;

import org.jfree.ui.action.AbstractActionDowngrade;
import org.jfree.ui.action.ActionDowngrade;
import org.jfree.util.ResourceBundleSupport;

/**
 * Creates a new PreviousPageAction used by the PreviewPane to directly jump to the
 * previous page of the report. This abstract class is used for initializing the default
 * locales, the actual work is done in an internal subclass in PreviewFrame.
 *
 * @author David Gilbert
 */
public abstract class PreviousPageAction extends AbstractActionDowngrade
{
  /**
   * Constructs a new action.
   *
   * @param resources localised resources.
   */
  protected PreviousPageAction (final ResourceBundleSupport resources,
                                final Skin skin)
  {
    putValue(NAME, resources.getString("action.back.name"));
    putValue(SHORT_DESCRIPTION, resources.getString("action.back.description"));
    putValue(ActionDowngrade.MNEMONIC_KEY,
            resources.getMnemonic("action.back.mnemonic"));
    putValue(ActionDowngrade.ACCELERATOR_KEY,
            resources.getKeyStroke("action.back.accelerator", 0));
    putValue(SMALL_ICON, skin.getIcon("action.back.small-icon", true, false));
    putValue("ICON24", skin.getIcon("action.back.icon", true, true));
  }

}
