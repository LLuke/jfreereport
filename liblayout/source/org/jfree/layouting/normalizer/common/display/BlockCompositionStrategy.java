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
 * BlockCompositionStrategy.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: BlockCompositionStrategy.java,v 1.1 2006/04/17 21:04:27 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.layouting.normalizer.common.display;

import org.jfree.layouting.input.style.keys.box.DisplayRole;
import org.jfree.layouting.normalizer.common.display.CompositionStrategy;

/**
 * Describes the composition and normalization behaviour of block elements To make the
 * later layouting easier, we always construct artificial line boxes, even if they would
 * not be needed according to the CSS description.
 *
 * @author Thomas Morgner
 */
public class BlockCompositionStrategy implements CompositionStrategy
{
  private ContentBox lineBox;

  public BlockCompositionStrategy ()
  {
  }

  public CompositionStrategy getInstance ()
  {
    return new BlockCompositionStrategy();
  }

  public void addElement (ContentBox parent, ContentNode node)
  {
    if (node.getDisplayRole() == DisplayRole.INLINE)
    {
      if (lineBox == null)
      {
        lineBox = new ContentLine();
        lineBox.setParent(parent);
        parent.addElementInternal(lineBox);
      }
      lineBox.add(node);
    }
    else
    {
      if (lineBox != null)
      {
        lineBox.markFinished();
      }
      lineBox = null;
      parent.addElementInternal(node);
    }
  }
}
