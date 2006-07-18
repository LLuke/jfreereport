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
 * TrailingEdgeAlignment.java
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
package org.jfree.layouting.renderer.model.alignment;

import org.jfree.layouting.renderer.model.RenderNode;

/**
 * The class (despite its name) aligns the element on the leading edge.
 *
 * @author Thomas Morgner
 */
public class TrailingEdgeAlignment implements Alignment
{
  public TrailingEdgeAlignment()
  {
  }

  /**
   * Tries to align the element. If the given box overflows, do not perform any
   * alignment and return the box unchanged. (This is not yet implemented)
   *
   * @param box
   * @return
   */
  public boolean align(int axis, RenderNode box, long totalWidth)
  {
    // that one is simple. Just take the computed X value, and the computed
    // width and off you go.
    final long start = box.getPosition(axis);
    final long width = box.getDimension(axis);
    final long correction = totalWidth - width;
    box.setPosition(axis, start + correction);
    box.validate();
    return false;
  }
}
