/**
 * =============================================================
 * JFreeReport : an open source reporting class library for Java
 * =============================================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
 * Project Lead:  David Gilbert (david.gilbert@object-refinery.com)
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
 * -----------------------
 * NextPageAction.java
 * -----------------------
 * (C)opyright 2000-2002, by Simba Management Limited.
 *
 * Changes
 * -------
 * 09-Jun-2002 : Removed the action command, actions connect directly to their source.
 */
package com.jrefinery.report.action;

import com.jrefinery.report.JFreeReportConstants;

import javax.swing.AbstractAction;
import javax.swing.Action;
import java.util.ResourceBundle;

public abstract class NextPageAction extends AbstractAction
{
  /**
   * Constructs a new action.
   */
  public NextPageAction (ResourceBundle resources)
  {
    putValue(Action.NAME, resources.getString("action.forward.name"));
    putValue(Action.SHORT_DESCRIPTION, resources.getString("action.forward.description"));
    putValue(Action.ACCELERATOR_KEY, resources.getObject("action.forward.accelerator"));
    putValue(Action.MNEMONIC_KEY, resources.getObject("action.forward.mnemonic"));
    putValue(Action.SMALL_ICON, resources.getObject("action.forward.small-icon"));
    putValue("ICON24", resources.getObject("action.forward.icon"));
  }
}
