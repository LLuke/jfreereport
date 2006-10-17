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
 * AlignContext.java
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
package org.jfree.layouting.renderer.process.valign;

import org.jfree.layouting.renderer.model.RenderNode;

/**
 * To position an element inside an box, we need the following data:
 * <p/>
 * (1) Offset. The distance between the parent's top-edge and the child's top
 * edge.
 * <p/>
 * (2) Dominant baseline. The childs alignment point is defined by that one.
 * <p/>
 * (3) Ascent. The distance from the baseline to the top edge.
 * <p/>
 * (4) descent. The distance from the baseline to the bottom edge.
 *
 * @author Thomas Morgner
 */
public abstract class AlignContext
{
  private int dominantBaseline;
  private RenderNode node;

  protected AlignContext(final RenderNode node)
  {
    this.node = node;
  }

  public RenderNode getNode()
  {
    return node;
  }

  public abstract void shift (final long delta);

  public abstract long getAfterEdge();
  public abstract long getBeforeEdge();

  public void setDominantBaseline(final int dominantBaseline)
  {
    this.dominantBaseline = dominantBaseline;
  }

  public int getDominantBaseline()
  {
    return dominantBaseline;
  }

  public abstract long getBaselineDistance (int baseline);
}
