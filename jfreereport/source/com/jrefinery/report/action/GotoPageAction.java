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
 * GotoPageAction.java
 * -------------------
 * (C)opyright 2002, by Simba Management Limited.
 *
 * Changes
 * -------------------
 * 31-May-2002 : Initial version
 * 04-Jun-2002 : Documentation
 * 09-Jun-2002 : Removed the action command, actions connect directly to their source.
 * 29-Aug-2002 : Downport to JDK 1.2.2
 */
package com.jrefinery.report.action;

import com.jrefinery.report.util.AbstractActionDowngrade;
import java.util.ResourceBundle;

/**
 * The GotoPageAction is used to direclty jump to a page. A simple dialog opens to
 * ask the user for the page. This is the abstract base for the action doing the
 * localisation specific initialisation.
 *
 * @author ??
 */
public abstract class GotoPageAction extends AbstractActionDowngrade
{
  /**
   * Constructs a new action.
   *
   * @param resources Localised resources for the action.
   */
  public GotoPageAction (ResourceBundle resources)
  {
    this.putValue (NAME, resources.getString ("action.gotopage.name"));
    this.putValue (SHORT_DESCRIPTION, resources.getString ("action.gotopage.description"));
    this.putValue (MNEMONIC_KEY, resources.getObject ("action.gotopage.mnemonic"));
    this.putValue (ACCELERATOR_KEY, resources.getObject ("action.gotopage.accelerator"));
  }

}