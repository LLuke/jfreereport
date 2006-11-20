/**
 * ===========================================
 * LibLayout : a free Java layouting library
 * ===========================================
 *
 * Project Info:  http://jfreereport.pentaho.org/liblayout/
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
 * $Id$
 * ------------
 * (C) Copyright 2006, by Pentaho Corperation.
 */

package org.jfree.layouting.modules.output.graphics;

import org.jfree.layouting.output.pageable.LogicalPageKey;
import org.jfree.layouting.output.pageable.PhysicalPageKey;

/**
 * Creation-Date: 10.11.2006, 20:41:29
 *
 * @author Thomas Morgner
 */
public class QueryPhysicalPageInterceptor implements GraphicsContentInterceptor
{
  private PageDrawable drawable;
  private PhysicalPageKey pageKey;

  public QueryPhysicalPageInterceptor(final PhysicalPageKey pageKey)
  {
    this.pageKey = pageKey;
  }

  public boolean isLogicalPageAccepted(LogicalPageKey key)
  {
    return false;
  }

  public void processLogicalPage(LogicalPageKey key, PageDrawable page)
  {
  }

  public boolean isPhysicalPageAccepted(PhysicalPageKey key)
  {
    return pageKey.equals(key);
  }

  public void processPhysicalPage(PhysicalPageKey key, PageDrawable page)
  {
    this.drawable = page;
  }

  public boolean isMoreContentNeeded()
  {
    return drawable == null;
  }

  public PageDrawable getDrawable()
  {
    return drawable;
  }
}