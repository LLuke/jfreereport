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
 * NodeAlignContext.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: NodeAlignContext.java,v 1.1 2006/10/17 17:31:57 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.layouting.renderer.process.valign;

import org.jfree.layouting.renderer.model.RenderNode;

/**
 * A generic align context for images and other nodes. (Renderable-Content
 * should have been aligned by the parent.
 *
 * @author Thomas Morgner
 */
public class NodeAlignContext extends AlignContext
{
  private long shift;

  public NodeAlignContext(RenderNode node)
  {
    super(node);
  }

  public long getBaselineDistance(int baseline)
  {
    return 0;
  }

  public void shift(final long delta)
  {
    this.shift += delta;
  }

  public long getAfterEdge()
  {
    return shift;
  }

  public long getBeforeEdge()
  {
    return shift;
  }
}
