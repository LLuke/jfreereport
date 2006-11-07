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
 * BoxShifter.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: BoxShifter.java,v 1.3 2006/10/31 11:14:12 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.layouting.renderer.process;

import org.jfree.layouting.renderer.model.RenderBox;
import org.jfree.layouting.renderer.model.RenderNode;

/**
 * By keeping the shifting in a separate class, we can optimize it later without
 * having to touch the other code. Remember: Recursive calls can be evil in
 * complex documents..
 *
 * @author Thomas Morgner
 */
public class BoxShifter
{
  public BoxShifter()
  {
  }

  public void shiftBox(RenderBox box, long amount)
  {
    if (amount == 0)
    {
      return;
    }
    if (amount < 0)
    {
      throw new IllegalArgumentException("Cannot shift upwards: " + amount);
    }

    box.setY(box.getY() + amount);
    shiftBoxInternal(box, amount);
  }

  public void shiftBoxUnchecked(RenderBox box, long amount)
  {
    if (amount == 0)
    {
      return;
    }
    
    box.setY(box.getY() + amount);
    shiftBoxInternal(box, amount);
  }

  private void shiftBoxInternal(final RenderBox box, final long amount)
  {
    RenderNode node = box.getFirstChild();
    while (node != null)
    {
      node.setY(node.getY() + amount);
      if (node instanceof RenderBox)
      {
        shiftBoxInternal((RenderBox) node, amount);
      }
      node = node.getNext();
    }
  }


  public void extendParents(RenderBox box, long amount)
  {
    RenderBox parent = box.getParent();
    while (parent != null)
    {
      parent.setHeight(parent.getHeight() + amount);
      parent = parent.getParent();
    }
  }
}
