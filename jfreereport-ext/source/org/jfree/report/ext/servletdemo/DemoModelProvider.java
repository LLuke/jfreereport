/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
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
 * DemoModelProvider.java
 * -------------------------
 * (C)opyright 2002, by Simba Management Limited and Contributors.
 *
 * Original Author:  David Gilbert (for Simba Management Limited);
 * Contributor(s):   Thomas Morgner;
 *
 * $Id: DemoModelProvider.java,v 1.2 2003/07/23 16:06:25 taqua Exp $
 *
 * Changes
 * -------
 * 04-Mar-2003 : Initial version
 *
 */
package org.jfree.report.ext.servletdemo;

import java.net.URL;
import javax.swing.table.TableModel;

import org.jfree.report.demo.SwingIconsDemoTableModel;

/**
 * Implements a TableModelProvider to create and return a tablemodel
 * for the JFreeReport servlet demo. This creates a IconTableModel
 * as used in the Swing version of the JFreeReport demo.
 *
 * @author Thomas Morgner
 */
public class DemoModelProvider implements TableModelProvider
{
  /** The URL of the JLF-Zip file containing the Icons used in the model. */
  private URL base;
  /** The tablemodel for the report. */
  private TableModel model;

  /**
   * Creates a model based on the icons from the given ZIP file (read from
   * the URL).
   *
   * @param base the URL pointing to the JLF-Icons zip file.
   */
  public DemoModelProvider(final URL base)
  {
    this.base = base;
  }

  /**
   * Returns the model, creating one if necessary.
   *
   * @return the IconTableModel.
   */
  public TableModel getModel()
  {
    if (model == null)
    {
      model = new SwingIconsDemoTableModel(base);
    }
    return model;
  }
}
