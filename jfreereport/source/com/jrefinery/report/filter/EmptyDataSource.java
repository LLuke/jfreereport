/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
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
 * --------------------
 * EmptyDataSource.java
 * --------------------
 * (C)opyright 2002, by Simba Management Limited and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: EmptyDataSource.java,v 1.9 2003/04/05 18:57:10 taqua Exp $
 *
 * Changes
 * -------
 * 20-May-2002 : Initial version
 * 06-Jun-2002 : Updated source header (DG);
 * 03-Jul-2002 : Implemented the Cloneable functionality
 */

package com.jrefinery.report.filter;

/**
 * A data source that always returns null.
 *
 * @author Thomas Morgner
 */
public final class EmptyDataSource implements DataSource
{

  /**
   * Returns the value for the data source (always null in this case).
   *
   * @return always null.
   */
  public Object getValue()
  {
    return null;
  }

  /**
   * Clones the data source.
   *
   * @return a clone.
   *
   * @throws CloneNotSupportedException this should never happen.
   */
  public Object clone() throws CloneNotSupportedException
  {
    return this;
  }

}
