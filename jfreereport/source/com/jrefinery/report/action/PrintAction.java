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
 * PrintAction.java
 * ----------------
 * (C)opyright 2002, by Simba Management Limited.
 *
 * Original Author:  David Gilbert (for Simba Management Limited);
 * Contributor(s):   -;
 *
 * $Id: PrintAction.java,v 1.4 2002/05/26 20:05:12 taqua Exp $
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
 * Print action for a print preview frame.
 */
public abstract class PrintAction extends AbstractAction implements Runnable
{

  /**
   * Constructs a new action.
   */
  public PrintAction(ResourceBundle resources)
  {
    this.putValue(Action.NAME, resources.getString("action.print.name"));
    this.putValue(Action.SHORT_DESCRIPTION, resources.getString("action.print.description"));
    this.putValue(Action.MNEMONIC_KEY, resources.getObject("action.print.mnemonic"));
    this.putValue(Action.ACCELERATOR_KEY, resources.getObject("action.print.accelerator"));
    this.putValue(Action.SMALL_ICON, resources.getObject("action.print.small-icon"));
    this.putValue("ICON24", resources.getObject("action.print.icon"));
    this.putValue(Action.ACTION_COMMAND_KEY, JFreeReportConstants.PRINT_COMMAND);
  }
}
