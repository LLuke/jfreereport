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
 * ----------------
 * CloseAction.java
 * ----------------
 * (C)opyright 2002, by Simba Management Limited.
 *
 * Original Author:  David Gilbert (for Simba Management Limited);
 * Contributor(s):   -;
 *
 * $Id: CloseAction.java,v 1.2 2002/05/14 21:35:02 taqua Exp $
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

import com.jrefinery.report.JFreeReportConstants;

/**
 * An action for closing the print preview frame.
 */
public abstract class CloseAction extends AbstractAction
{

  /**
   * Constructs a new action.
   *
   * @param resources Localised resources for the action.
   */
  public CloseAction(ResourceBundle resources)
  {
    String name = resources.getString("action.close.name");
    this.putValue(Action.NAME, name);

    String description = resources.getString("action.close.description");
    this.putValue(Action.SHORT_DESCRIPTION, description);

    Integer mnemonic = (Integer) resources.getObject("action.close.mnemonic");
    this.putValue(Action.MNEMONIC_KEY, mnemonic);

    this.putValue(Action.ACTION_COMMAND_KEY, JFreeReportConstants.CLOSE_COMMAND);

  }
}