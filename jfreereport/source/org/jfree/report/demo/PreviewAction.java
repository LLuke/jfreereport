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
 * ------------------
 * PreviewAction.java
 * ------------------
 * (C)opyright 2002, by Object Refinery Limited and Contributors.
 *
 * Original Author:  David Gilbert (for Object Refinery Limited);
 * Contributor(s):   Thomas Morgner;
 *
 * $Id: PreviewAction.java,v 1.3 2004/03/16 15:09:22 taqua Exp $
 *
 * Changes
 * -------
 * 07-May-2002 : Version 1 (DG);
 * 05-Jun-2002 : Documentation.
 * 04-Dec-2002 : Added ActionDowngrade reference to resolve ambiguity (compile error) (DG);
 *
 */

package org.jfree.report.demo;

import java.util.ResourceBundle;

import org.jfree.report.modules.gui.base.components.AbstractActionDowngrade;
import org.jfree.report.modules.gui.base.components.ActionDowngrade;
import org.jfree.report.modules.gui.base.ResourceBundleUtils;

/**
 * The preview action invokes the parsing and processing of the currently selected sample
 * report. The actual work is done in the JFreeReportDemos method attemptPreview ()
 *
 * @author David Gilbert
 */
public abstract class PreviewAction extends AbstractActionDowngrade
{
  /**
   * Constructs a new preview action.
   *
   * @param resources  localised resources.
   */
  public PreviewAction(final ResourceBundle resources)
  {
    this.putValue(NAME, resources.getString("action.print-preview.name"));
    this.putValue(SHORT_DESCRIPTION, resources.getString("action.print-preview.description"));
    this.putValue(ActionDowngrade.MNEMONIC_KEY,
        ResourceBundleUtils.createMnemonic(resources.getString("action.print-preview.mnemonic")));
    this.putValue(ActionDowngrade.ACCELERATOR_KEY,
        ResourceBundleUtils.createMenuKeystroke(resources.getString("action.print-preview.accelerator")));
    this.putValue(SMALL_ICON,
        ResourceBundleUtils.getIcon(resources.getString("action.print-preview.small-icon")));
    this.putValue("ICON24",
        ResourceBundleUtils.getIcon(resources.getString("action.print-preview.icon")));
  }
}
