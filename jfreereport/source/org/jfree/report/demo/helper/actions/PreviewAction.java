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
 * ------------------
 * PreviewAction.java
 * ------------------
 * (C)opyright 2002, by Simba Management Limited and Contributors.
 *
 * Original Author:  David Gilbert (for Simba Management Limited);
 * Contributor(s):   Thomas Morgner;
 *
 * $Id: PreviewAction.java,v 1.1 2005/08/29 17:46:10 taqua Exp $
 *
 * Changes
 * -------
 * 07-May-2002 : Version 1 (DG);
 * 05-Jun-2002 : Documentation.
 * 04-Dec-2002 : Added ActionDowngrade reference to resolve ambiguity (compile error) (DG);
 *
 */

package org.jfree.report.demo.helper.actions;

import javax.swing.Action;

import org.jfree.ui.action.AbstractActionDowngrade;
import org.jfree.ui.action.ActionDowngrade;
import org.jfree.util.ResourceBundleSupport;
import org.jfree.report.modules.gui.base.Skin;
import org.jfree.report.modules.gui.base.SkinLoader;

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
   * @param resources localised resources.
   */
  public PreviewAction (final ResourceBundleSupport resources)
  {
    Skin skin = SkinLoader.loadSkin();
    this.putValue(Action.NAME, resources.getString("action.print-preview.name"));
    this.putValue(Action.SHORT_DESCRIPTION, resources.getString("action.print-preview.description"));
    this.putValue(ActionDowngrade.MNEMONIC_KEY,
            resources.getMnemonic("action.print-preview.mnemonic"));
    this.putValue(ActionDowngrade.ACCELERATOR_KEY,
            resources.getKeyStroke("action.print-preview.accelerator"));
    this.putValue(Action.SMALL_ICON, skin.getIcon("action.print-preview.small-icon", true, false));
    this.putValue("ICON24", skin.getIcon("action.print-preview.icon", true, true));
  }
}
