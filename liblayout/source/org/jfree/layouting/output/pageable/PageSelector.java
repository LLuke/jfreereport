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
 * PageSelector.java
 * ---------
 *
 * Original Author:  Thomas Morgner;
 * Contributors: -;
 *
 * $Id: PageSelector.java,v 1.1 2006/02/12 21:40:17 taqua Exp $
 *
 * Changes
 * -------------------------
 * 03.01.2006 : Initial version
 */
package org.jfree.layouting.output.pageable;

import java.io.Serializable;

import org.jfree.layouting.input.style.keys.page.PageSize;

/**
 * A page selector describes a physical page format. It is up to the pageable
 * output target to return a suitable page definition.
 *
 * @author Thomas Morgner
 */
public class PageSelector implements Serializable
{
  private PageSize pageSize;
  private int pageSpanHorizontal;
  private int pageSpanVertical;
  private String name;

  public PageSelector()
  {
  }

  public PageSize getPageSize()
  {
    return pageSize;
  }

  public void setPageSize(final PageSize pageSize)
  {
    this.pageSize = pageSize;
  }

  public int getPageSpanHorizontal()
  {
    return pageSpanHorizontal;
  }

  public void setPageSpanHorizontal(final int pageSpanHorizontal)
  {
    this.pageSpanHorizontal = pageSpanHorizontal;
  }

  public int getPageSpanVertical()
  {
    return pageSpanVertical;
  }

  public void setPageSpanVertical(final int pageSpanVertical)
  {
    this.pageSpanVertical = pageSpanVertical;
  }

  public String getName()
  {
    return name;
  }

  public void setName(final String name)
  {
    this.name = name;
  }
}
