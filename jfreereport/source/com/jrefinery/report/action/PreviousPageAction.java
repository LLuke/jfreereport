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
 * PreviousPageAction.java
 * -----------------------
 * (C)opyright 2000-2002, by Simba Management Limited.
 *
 */
package com.jrefinery.report.action;

import com.jrefinery.report.JFreeReportConstants;

import javax.swing.Action;
import javax.swing.AbstractAction;
import java.util.ResourceBundle;

public abstract class PreviousPageAction extends AbstractAction
{
  public PreviousPageAction (ResourceBundle resources)
  {
    putValue(Action.NAME, resources.getString("action.back.name"));
    putValue(Action.SHORT_DESCRIPTION, resources.getString("action.back.description"));
    putValue(Action.MNEMONIC_KEY, resources.getObject("action.back.mnemonic"));
    putValue(Action.ACCELERATOR_KEY, resources.getObject("action.back.accelerator"));
    putValue(Action.SMALL_ICON, resources.getObject("action.back.small-icon"));
    putValue("ICON24", resources.getObject("action.back.icon"));
    putValue(Action.ACTION_COMMAND_KEY, JFreeReportConstants.PREVPAGE_COMMAND);
  }

}
