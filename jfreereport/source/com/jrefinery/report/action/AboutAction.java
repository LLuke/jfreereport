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
 * AboutAction.java
 * ----------------
 * (C)opyright 2002, by Simba Management Limited.
 *
 * Original Author:  David Gilbert (for Simba Management Limited);
 * Contributor(s):   -;
 *
 * $Id: AboutAction.java,v 1.5 2002/06/09 14:46:06 taqua Exp $
 *
 * Changes
 * -------
 * 07-May-2002 : Version 1 (DG);
 * 10-May-2002 : Removed actionhandling from class. Specific handling is implemented based on
 *               target environment. (TM)
 * 16-May-2002 : Load images from jar and simplified (JS)
 * 09-Jun-2002 : Removed the action command, actions connect directly to their source.
 */

package com.jrefinery.report.action;

import javax.swing.AbstractAction;
import javax.swing.Action;
import java.util.ResourceBundle;

/**
 * The About action is used to show some information about the current programm, to which
 * the printpreview belongs to.
 * <p>
 * This abstract class handles the locales specific initialisation.
 */

public abstract class AboutAction extends AbstractAction
{

  /**
   * Constructs a new action.
   */

  public AboutAction (ResourceBundle resources)
  {
    putValue (Action.NAME, resources.getString ("action.about.name"));
    putValue (Action.SHORT_DESCRIPTION, resources.getString ("action.about.description"));
    putValue (Action.MNEMONIC_KEY, resources.getObject ("action.about.mnemonic"));
    putValue (Action.SMALL_ICON, resources.getObject ("action.about.small-icon"));
    putValue ("ICON24", resources.getObject ("action.about.icon"));
  }
}
