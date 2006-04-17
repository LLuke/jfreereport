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
 * $Id$
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.layouting.normalizer.common;

import org.jfree.layouting.input.style.keys.box.DisplayRole;

/**
 * Creation-Date: 02.04.2006, 17:11:13
 *
 * @author Thomas Morgner
 */
public class BlockCompositionStrategy implements CompositionStrategy
{
  private boolean separateInlines;
  private ContentBox lineBox;

  public BlockCompositionStrategy()
  {
  }

  public CompositionStrategy getInstance()
  {
    return new BlockCompositionStrategy();
  }

  public void addElement(ContentBox parent, ContentNode node)
  {
    System.out.println ("Node: " + node + " " + node.getDisplayRole());
    if (node.getDisplayRole() == DisplayRole.INLINE)
    {
      if (separateInlines == false)
      {
        parent.addElementInternal(node);
        return;
      }

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
      if (separateInlines == false)
      {
        // A block element has been added; copy the content into a linebox.
        if (parent.isEmpty() == false)
        {
          lineBox = new ContentLine();
          lineBox.setParent(parent);
          final ContentNode[] elements = parent.getElements();
          for (int i = 0; i < elements.length; i++)
          {
            lineBox.add(elements[i]);
          }
          parent.clear();
          parent.addElementInternal(lineBox);
        }
        parent.setLayoutingStrategy(new BlockLayoutingStrategy());
        separateInlines = true;
      }
      lineBox = null;
      parent.addElementInternal(node);
    }
  }
}
