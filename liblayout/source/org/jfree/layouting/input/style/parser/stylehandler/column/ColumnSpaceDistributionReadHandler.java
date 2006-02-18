/**
 * ========================================
 * <libname> : a free Java <foobar> library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/liblayout/
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2005, by Object Refinery Limited and Contributors.
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
 * ---------
 * ColumnSpaceDistributionReadHandler.java
 * ---------
 *
 * Original Author:  Thomas Morgner;
 * Contributors: -;
 *
 * $Id: ColumnSpaceDistributionReadHandler.java,v 1.1 2006/02/12 21:57:20 taqua Exp $
 *
 * Changes
 * -------------------------
 * 08.12.2005 : Initial version
 */
package org.jfree.layouting.input.style.parser.stylehandler.column;

import org.jfree.layouting.input.style.parser.stylehandler.OneOfConstantsReadHandler;
import org.jfree.layouting.input.style.keys.column.ColumnSpaceDistribution;

/**
 * Creation-Date: 08.12.2005, 17:36:20
 *
 * @author Thomas Morgner
 */
public class ColumnSpaceDistributionReadHandler extends OneOfConstantsReadHandler
{
  public ColumnSpaceDistributionReadHandler()
  {
    super(false);
    addValue(ColumnSpaceDistribution.BETWEEN);
    addValue(ColumnSpaceDistribution.END);
    addValue(ColumnSpaceDistribution.INNER);
    addValue(ColumnSpaceDistribution.OUTER);
    addValue(ColumnSpaceDistribution.START);
  }
}