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
 * DefaultPage.java
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
package org.jfree.layouting.renderer.page;

/**
 * Creation-Date: 15.06.2006, 16:07:21
 *
 * @author Thomas Morgner
 */
public class DefaultPage implements Page
{
  private PageArea[] pageAreas;
  private long width;
  private long height;

  public DefaultPage(final long width, final long height)
  {
    this.width = width;
    this.height = height;
    this.pageAreas = new PageArea[PageAreaType.getMaxIndex()];
    for (int i = 0; i < pageAreas.length; i++)
    {
      pageAreas[i] = new DefaultPageArea();
    }
    alignPageAreas();
  }

  public PageArea getPageArea(PageAreaType type)
  {
    return pageAreas[type.getIndex()];
  }

  /**
   * Returns the physical width of the paper.
   *
   * @return the physical width.
   */
  public long getWidth()
  {
    return width;
  }

  /**
   * Returns the physical height of the paper.
   *
   * @return the physical height.
   */
  public long getHeight()
  {
    return height;
  }

  public void alignPageAreas()
  {
    // update the page area sizes ..
  }
}
