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
 * ZoomInAction.java
 * -----------------
 * (C)opyright 2000-2002, by Simba Management Limited.
 *
 * $Id$
 *
 * Changes
 * -------
 * 09-Jun-2002 : Removed the action command, actions connect directly to their source.
 * 29-Aug-2002 : Downport to JDK 1.2.2
 *
 */
package com.jrefinery.report.action;

import com.jrefinery.report.util.AbstractActionDowngrade;
import java.util.ResourceBundle;

/**
 * The zoomIn action is used to increase the ZoomFactor of the report. Zooming
 * can be increased in predefined steps from 25% to 400%. This abstract base class
 * implements the locales specific initialisations, actual work is done in an internal
 * subclass in PreviewFrame.
 *
 * @author ??
 */
public abstract class ZoomInAction extends AbstractActionDowngrade
{
  /**
   * Constructs a new action.
   *
   * @param resources  localised resources.
   */
  public ZoomInAction (ResourceBundle resources)
  {
    putValue (NAME, resources.getString ("action.zoomIn.name"));
    putValue (SHORT_DESCRIPTION, resources.getString ("action.zoomIn.description"));
    putValue (MNEMONIC_KEY, resources.getObject ("action.zoomIn.mnemonic"));
    putValue (ACCELERATOR_KEY, resources.getObject ("action.zoomIn.accelerator"));
    putValue (SMALL_ICON, resources.getObject ("action.zoomIn.small-icon"));
    putValue ("ICON24", resources.getObject ("action.zoomIn.icon"));
  }

}
