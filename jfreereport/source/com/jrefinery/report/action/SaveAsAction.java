/**
 * =============================================================
 * JFreeReport : an open source reporting class library for Java
 * =============================================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
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
 * (C)opyright 2002, by Simba Management Limited.
 *
 * Original Author:  David Gilbert (for Simba Management Limited);
 * Contributor(s):   -;
 *
 * $Id: SaveAsAction.java,v 1.2 2002/05/14 21:35:02 taqua Exp $
 *
 * Changes
 * -------
 * 07-May-2002 : Version 1 (DG);
 * 10-May-2002 : Removed actionhandling from class. Specific handling is implemented based on
 *               target environment. (TM)
 * 16-May-2002 : Load images from jar (JS)
 *
 */

package com.jrefinery.report.action;

import java.util.ResourceBundle;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.KeyStroke;

import com.jrefinery.report.JFreeReportConstants;

/**
 * Save (to PDF) action for a print preview frame.
 */
public abstract class SaveAsAction extends AbstractAction
{

  /**
   * Constructs a new action.
   */
  public SaveAsAction(ResourceBundle resources)
  {

    String name = resources.getString("action.save-as.name");
    this.putValue(Action.NAME, name);

    String description = resources.getString("action.save-as.description");
    this.putValue(Action.SHORT_DESCRIPTION, description);

    Integer mnemonic = (Integer) resources.getObject("action.save-as.mnemonic");
    this.putValue(Action.MNEMONIC_KEY, mnemonic);

    KeyStroke accelerator = (KeyStroke) resources.getObject("action.save-as.accelerator");
    this.putValue(Action.ACCELERATOR_KEY, accelerator);

    ImageIcon icon16 = (ImageIcon) resources.getObject("action.save-as.small-icon");
    this.putValue(Action.SMALL_ICON, icon16);

    ImageIcon icon24 = (ImageIcon) resources.getObject("action.save-as.icon");
    this.putValue("ICON24", icon24);

    this.putValue(Action.ACTION_COMMAND_KEY, JFreeReportConstants.SAVE_AS_COMMAND);
  }
}