/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
 *
 * (C) Copyright 2000-2003, by Simba Management Limited and Contributors.
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
 * ExportToExcelAction.java
 * -----------------
 * (C)opyright 2003, by Heiko Evermann and Contributors.
 *
 * Original Author:  Heiko Evermann (for Hawesko GmbH & Co KG);
 * Contributor(s):
 *
 * $Id: ExportToCSVAction.java,v 1.4 2003/06/27 14:25:16 taqua Exp $
 *
 * Changes
 * -------
 * 02-Jan-2003 : inial Version
 */
package com.jrefinery.report.action;

import java.util.ResourceBundle;

import com.jrefinery.report.util.AbstractActionDowngrade;
import com.jrefinery.report.util.ActionDowngrade;

/**
 * Export to CSV action for a print preview frame.
 *
 * @deprecated Export modules are now defined by plugin-interfaces.
 * @author David Gilbert
 */
public abstract class ExportToCSVAction extends AbstractActionDowngrade
{

  /**
   * Constructs a new action.
   *
   * @param resources  localised resources.
   */
  public ExportToCSVAction(final ResourceBundle resources)
  {
    this.putValue(NAME, resources.getString("action.export-to-csv.name"));
    this.putValue(SHORT_DESCRIPTION, resources.getString("action.export-to-csv.description"));
    this.putValue(ActionDowngrade.MNEMONIC_KEY,
        resources.getObject("action.export-to-csv.mnemonic"));
    this.putValue(ActionDowngrade.ACCELERATOR_KEY,
        resources.getObject("action.export-to-csv.accelerator"));
    this.putValue(SMALL_ICON, resources.getObject("action.export-to-csv.small-icon"));
    this.putValue("ICON24", resources.getObject("action.export-to-csv.icon"));
  }
}
