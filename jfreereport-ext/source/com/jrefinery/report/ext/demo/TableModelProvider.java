/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
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
 * ---------------------
 * ReportDefinition.java
 * ---------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 *
 * $Id: TableModelProvider.java,v 1.1 2003/03/05 14:55:51 taqua Exp $
 *
 * Changes
 * -------
 * 04-Mar-2003 : Initial version.
 *
 */
package com.jrefinery.report.ext.demo;

import javax.swing.table.TableModel;

/**
 * The tablemodel provider is used to supply the tablemodel to the generated
 * report.
 */
public interface TableModelProvider
{
  /**
   * Returns the tablemodel for the generated report. 
   *
   * @return the model.
   */
  public TableModel getModel();
}
