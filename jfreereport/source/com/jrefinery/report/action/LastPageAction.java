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
 * 29-May-2002 : Initial version
 * 05-Jun-2002 : Documentation update.
 * 09-Jun-2002 : Removed the action command, actions connect directly to their source.
 */

package com.jrefinery.report.action;

import com.jrefinery.report.JFreeReportConstants;

import javax.swing.AbstractAction;
import javax.swing.Action;
import java.util.ResourceBundle;

/**
 * Creates a new LastPageAction used by the PreviewPane to directly jump to the last
 * page of the report. This abstract class is used for initializing the default locales,
 * the actual work is done in an internal subclass in PreviewFrame.
 *
 * @author js
 */
public abstract class LastPageAction extends AbstractAction
{
  /**
   * Constructs a new action.
   */
  public LastPageAction (ResourceBundle resources)
  {
    putValue (Action.NAME, resources.getString ("action.lastpage.name"));
    putValue (Action.SHORT_DESCRIPTION, resources.getString ("action.lastpage.description"));
    putValue (Action.MNEMONIC_KEY, resources.getObject ("action.lastpage.mnemonic"));
    putValue (Action.ACCELERATOR_KEY, resources.getObject ("action.lastpage.accelerator"));
    putValue (Action.SMALL_ICON, resources.getObject ("action.lastpage.small-icon"));
    putValue ("ICON24", resources.getObject ("action.lastpage.icon"));
  }
}
