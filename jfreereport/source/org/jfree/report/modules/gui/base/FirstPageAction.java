/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2002, by Object Refinery Limited and Contributors.
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
 * --------------------
 * FirstPageAction.java
 * --------------------
 * (C)opyright 2002, by Object Refinery Limited and Contributors.
 *
 * Original Author:  Joerg Schoemer;
 * Contributor(s):   David Gilbert (for Object Refinery Limited);
 *                   Thomas Morgner;
 *
 * $Id: FirstPageAction.java,v 1.3 2004/03/16 15:09:23 taqua Exp $
 *
 * Changes
 * -------
 * 29-May-2002 : Initial version
 * 05-Jun-2002 : These comments and documentation
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
 * Creates a new FirstPageAction used by the PreviewPane to directly jump to the first
 * page of the report. This abstract class is used for initializing the default locales,
 * the actual work is done in an internal subclass in PreviewFrame.
 *
 * @author JS
 */
public abstract class FirstPageAction extends AbstractActionDowngrade
{

  /**
   * Constructs a new action.
   *
   * @param resources  localised resources.
   */
  protected FirstPageAction(final ResourceBundle resources)
  {
    putValue(NAME, resources.getString("action.firstpage.name"));
    putValue(SHORT_DESCRIPTION, resources.getString("action.firstpage.description"));
    putValue(ActionDowngrade.MNEMONIC_KEY,
        ResourceBundleUtils.createMnemonic(resources.getString("action.firstpage.mnemonic")));
    putValue(ActionDowngrade.ACCELERATOR_KEY,
        ResourceBundleUtils.createMenuKeystroke(resources.getString("action.firstpage.accelerator")));
    putValue(SMALL_ICON,
        ResourceBundleUtils.getIcon(resources.getString("action.firstpage.small-icon")));
    putValue("ICON24",
        ResourceBundleUtils.getIcon(resources.getString("action.firstpage.icon")));
  }
}
