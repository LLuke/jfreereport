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
 * ------------------
 * PreviewAction.java
 * ------------------
 * (C)opyright 2002, by Simba Management Limited.
 *
 * Original Author:  David Gilbert (for Simba Management Limited);
 * Contributor(s):   -;
 *
 * $Id: PreviewAction.java,v 1.8 2002/09/13 15:38:07 mungady Exp $
 *
 * Changes
 * -------
 * 07-May-2002 : Version 1 (DG);
 * 05-Jun-2002 : Documentation.
 */

package com.jrefinery.report.demo;

import com.jrefinery.report.util.AbstractActionDowngrade;
import java.util.ResourceBundle;

/**
 * The preview action invokes the parsing and processing of the currently selected sample
 * report. The actual work is done in the JFreeReportDemos method attemptPreview ()
 *
 * @author DG
 */
public abstract class PreviewAction extends AbstractActionDowngrade
{
  /**
   * Constructs a new preview action.
   *
   * @param resources  localised resources.
   */
  public PreviewAction (ResourceBundle resources)
  {
    this.putValue (NAME, resources.getString ("action.print-preview.name"));
    this.putValue (SHORT_DESCRIPTION, resources.getString ("action.print-preview.description"));
    this.putValue (MNEMONIC_KEY, resources.getObject ("action.print-preview.mnemonic"));
    this.putValue (ACCELERATOR_KEY, resources.getObject ("action.print-preview.accelerator"));
    this.putValue (SMALL_ICON, resources.getObject ("action.print-preview.small-icon"));
    this.putValue ("ICON24", resources.getObject ("action.print-preview.icon"));
  }
}
