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
 * -------------------
 * LastPageAction.java
 * -------------------
 * Original Author:  Joerg Schoemer;
 * 
 */

package com.jrefinery.report.action;

import com.jrefinery.report.JFreeReportConstants;

import java.util.ResourceBundle;

import javax.swing.AbstractAction;
import javax.swing.Action;

/**
 * @author js
 *
 */
public abstract class LastPageAction extends AbstractAction
{

  public LastPageAction(ResourceBundle resources)
  {
    putValue(Action.NAME, resources.getString("action.lastpage.name"));
    putValue(Action.SHORT_DESCRIPTION, resources.getString("action.lastpage.description"));
    putValue(Action.MNEMONIC_KEY, resources.getObject("action.lastpage.mnemonic"));
    putValue(Action.ACCELERATOR_KEY, resources.getObject("action.lastpage.accelerator"));
    putValue(Action.SMALL_ICON, resources.getObject("action.lastpage.small-icon"));
    putValue("ICON24", resources.getObject("action.lastpage.icon"));

    putValue(Action.ACTION_COMMAND_KEY, JFreeReportConstants.LASTPAGE_COMMAND);
  }
}
