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
 * ------------------------------
 * PageFormatDefinition.java
 * ------------------------------
 * (C)opyright 2004, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: PageDefinition.java,v 1.1 2004/03/16 15:34:26 taqua Exp $
 *
 * Changes 
 * -------------------------
 * 14.02.2004 : Initial version
 *  
 */

package org.jfree.report;

import java.awt.geom.Rectangle2D;
import java.awt.print.PageFormat;
import java.io.Serializable;

public interface PageDefinition extends Cloneable, Serializable
{
  public float getWidth();
  public float getHeight();

  public int getPageCount();

  /**
   * Returns all page positions as array.
   * @see PageDefinition#getPagePosition(int)
   * @return the collected page positions
   */
  public Rectangle2D[] getPagePositions();

  /**
   * Describes the internal position of the given page within
   * the logical page. The logical page does not include any
   * page margins, the printable area for a page starts at
   * (0,0).
   *
   * @param pos
   * @return
   */
  public Rectangle2D getPagePosition (int pos);

  /**
   * Returns the page format for the given page number.
   * The page format contains local coordinates - that means
   * that the point (0,0) denotes the upper left corner of
   * this returned page format and not global coordinates.
   *
   * @param pos the position of the pageformat within the page
   * @return the given pageformat.
   */
  public PageFormat getPageFormat(int pos);

  public Object clone () throws CloneNotSupportedException;
}
