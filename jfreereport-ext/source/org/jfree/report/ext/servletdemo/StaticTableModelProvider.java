/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
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
 * -------------------------
 * StaticTableModelProvider.java
 * -------------------------
 * (C)opyright 2002, by Simba Management Limited and Contributors.
 *
 * Original Author:  David Gilbert (for Simba Management Limited);
 * Contributor(s):   Thomas Morgner;
 *
 * $Id: StaticTableModelProvider.java,v 1.1 2003/07/08 14:21:48 taqua Exp $
 *
 * Changes
 * -------
 * 04-Mar-2003 : Initial version
 *
 */
package org.jfree.report.ext.servletdemo;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

/**
 * Implements a TableModel provider for a given TableModel. The model
 * was created somewhere else and is returned to the reporting servlets
 * when requested.
 *
 * @author Thoams Morgner
 */
public class StaticTableModelProvider implements TableModelProvider
{
  /** The tablemodel returned by this provider. */
  private TableModel model;

  /**
   * Default constructor. A tablemodel must be set later.
   */
  public StaticTableModelProvider()
  {
  }

  /**
   * Creates a new StaticTableModelProvier for the given tablemodel.
   *
   * @param model the model that should be used as datasource in the reporting.
   */
  public StaticTableModelProvider(final TableModel model)
  {
    this.model = model;
  }

  /**
   * Returns the tablemodel of this provider. The tablemodel must been set
   * either with the constructor or with the setModel method.
   *
   * @return the defined model or an empty DefaultTableModel if none was defined.
   */
  public TableModel getModel()
  {
    if (model == null)
    {
      return new DefaultTableModel();
    }
    return model;
  }

  /**
   * Sets the model that should be returned by this model provider.
   *
   * @param model the tablemodel that should be used in the reporting.
   */
  public void setModel(final TableModel model)
  {
    this.model = model;
  }
}
