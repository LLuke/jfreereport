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
 * RightAlignmentProcessor.java
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
package org.jfree.layouting.renderer.process;

import org.jfree.layouting.renderer.model.RenderNode;

/**
 * Right alignment strategy. Not working yet, as this is unimplemented right now.
 *
 * @author Thomas Morgner
 */
public class RightAlignmentProcessor extends AbstractAlignmentProcessor
{
  private int pageSegment;
  private long position;

  public RightAlignmentProcessor()
  {
  }

  public int getPageSegment()
  {
    return pageSegment;
  }

  public void setPageSegment(final int pageSegment)
  {
    this.pageSegment = pageSegment;
  }

  public long getPosition()
  {
    return position;
  }

  public void setPosition(final long position)
  {
    this.position = position;
  }

  /**
   * Handle the next input chunk.
   *
   * @param start the start index
   * @param count the number of elements in the sequence
   * @return true, if processing should be finished, false if more elements are
   *         needed for the line.
   */
  protected int handleElement(int start, int count)
  {
    final int endIndex = start + count;

    return 0;
  }

  public RenderNode next()
  {
    position = getStartOfLine();
    pageSegment = 0;

    final RenderNode retval = super.next();

    position = 0;
    pageSegment = 0;

    return retval;
  }

}
