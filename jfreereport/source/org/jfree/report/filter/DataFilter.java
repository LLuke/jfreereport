/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2002, by Object Refinery Limited and Contributors.
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
 * DataFilter.java
 * ---------------
 * (C)opyright 2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Object Refinery Limited);
 *
 * $Id: DataFilter.java,v 1.3 2004/05/07 08:24:42 mungady Exp $
 *
 * Changes
 * -------
 * 20-May-2002 : Initial version
 * 06-Jun-2002 : Updated source header (DG);
 *
 */

package org.jfree.report.filter;

/**
 * A data filter is a combined input-/out target. Use filters to perform stateless data
 * transformation. If you need to access the report state to perform a task, use functions
 * instead.
 *
 * @author Thomas Morgner
 */
public interface DataFilter extends DataSource, DataTarget
{
}
