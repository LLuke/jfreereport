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
 * -----------------------
 * PreviousPageAction.java
 * -----------------------
 * (C)opyright 2000-2002, by Simba Management Limited.
 *
 * $Id: PreviousPageAction.java,v 1.11 2002/11/07 21:45:27 taqua Exp $
 *
 * Changes
 * -------
 * 09-Jun-2002 : Removed the action command, actions connect directly to their source.
 * 29-Aug-2002 : Downport to JDK 1.2.2
 */
package com.jrefinery.report.action;

import com.jrefinery.report.util.AbstractActionDowngrade;

import java.util.ResourceBundle;

/**
 * Creates a new PreviousPageAction used by the PreviewPane to directly jump to the previous
 * page of the report. This abstract class is used for initializing the default locales,
 * the actual work is done in an internal subclass in PreviewFrame.
 *
 * @author ??
 */
public abstract class PreviousPageAction extends AbstractActionDowngrade
{
  /**
   * Constructs a new action.
   *
   * @param resources  localised resources.
   */
  public PreviousPageAction (ResourceBundle resources)
  {
    putValue (NAME, resources.getString ("action.back.name"));
    putValue (SHORT_DESCRIPTION, resources.getString ("action.back.description"));
    putValue (MNEMONIC_KEY, resources.getObject ("action.back.mnemonic"));
    putValue (ACCELERATOR_KEY, resources.getObject ("action.back.accelerator"));
    putValue (SMALL_ICON, resources.getObject ("action.back.small-icon"));
    putValue ("ICON24", resources.getObject ("action.back.icon"));
  }

}
