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
 * --------------------------------
 * PhysicalOperationsCollector.java
 * --------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner (taquera@sherito.org);
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: PhysicalOperationsCollector.java,v 1.2 2003/04/09 15:52:54 mungady Exp $
 *
 * Changes
 * -------
 * 04.04.2003 : Initial version
 */
package com.jrefinery.report.targets.pageable.operations;

/**
 * A collector of {@link PhysicalOperation} objects.
 *
 * @author Thomas Morgner.
 */
public interface PhysicalOperationsCollector
{
  /**
   * Adds a {@link PhysicalOperation} to the collection.
   *
   * @param p  the operation.
   */
  public void addOperation(PhysicalOperation p);
}
