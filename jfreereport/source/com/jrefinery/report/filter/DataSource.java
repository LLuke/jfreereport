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
 * ---------------
 * DataSource.java
 * ---------------
 * (C)opyright 2002, by Simba Management Limited and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: DataSource.java,v 1.4 2002/07/03 18:49:48 taqua Exp $
 *
 * Changes
 * -------
 * 20-May-2002 : Initial version
 * 06-Jun-2002 : Updated Javadoc comments (DG);
 * 03-Jul-2002 : Cloneable and Serializable added
 */
package com.jrefinery.report.filter;

import java.io.Serializable;

/**
 * A DataSource is a producer in the data chain. Common Sources are StaticSources (predefined
 * data), ReportDataSources (data filled from the reports data set) or FunctionDataSource (the
 * data is filled by querying an assigned function).
 * <p>
 * All DataSources have to support the Cloneable interface so that a report can be completley cloned
 * with all assigned filters and DataSources. Reports are cloned before they are processed to remove the
 * sideeffect when having multiple report processors working on the same object.
 */
public interface DataSource extends Serializable, Cloneable
{
  /**
   * Returns the current value for the data source.
   *
   * @return The value.
   */
  public Object getValue ();

  /**
   * Clones this datasource.
   *
   * @returns a clone of the datasource.
   */
  public Object clone () throws CloneNotSupportedException;

}
