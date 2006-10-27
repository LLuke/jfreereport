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
 * FinishedRenderNode.java
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
package org.jfree.layouting.renderer.model;

/**
 * A box replacement. It has a predefined width and height and does not change
 * those. It is a placeholder for all already printed content.
 * <p/>
 * If you see this node inside an inline box, you can be sure you've shot
 * yourself in the foot.
 *
 * @author Thomas Morgner
 */
public class FinishedRenderNode extends PlaceholderRenderNode
{
  private long layoutedWidth;
  private long layoutedHeight;

  public FinishedRenderNode(final long layoutedWidth, final long layoutedHeight)
  {
    this.layoutedWidth = layoutedWidth;
    this.layoutedHeight = layoutedHeight;
  }

  public boolean isEmpty()
  {
    return false;
  }

  public long getLayoutedWidth()
  {
    return layoutedWidth;
  }

  public long getLayoutedHeight()
  {
    return layoutedHeight;
  }
}
