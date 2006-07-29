/**
 * ===========================================
 * LibLayout : a free Java layouting library
 * ===========================================
 *
 * Project Info:  http://www.jfree.org/liblayout/
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2005, by Object Refinery Limited and Contributors.
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
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * ------------
 * PrintSplitResult.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id$
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.layouting.renderer.model.page;

/**
 * Creation-Date: 28.07.2006, 18:08:46
 *
 * @author Thomas Morgner
 */
public class PrintSplitResult
{
  private LogicalPageBox pageBox;
  private PhysicalPageBox[] physicalPages;

  public PrintSplitResult(final LogicalPageBox pageBox,
                          final PhysicalPageBox[] physicalPages)
  {
    this.pageBox = pageBox;
    this.physicalPages = physicalPages;
  }

  public LogicalPageBox getPageBox()
  {
    return pageBox;
  }

  public PhysicalPageBox[] getPhysicalPages()
  {
    return physicalPages;
  }
}
