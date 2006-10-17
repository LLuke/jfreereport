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
 * IndexedRenderBox.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: IndexedRenderBox.java,v 1.3 2006/07/29 18:57:13 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.layouting.renderer.model;

import java.util.HashMap;

/**
 * A renderbox, that maintains the build-order and allows to replace elements
 * by their name.
 *
 * @author Thomas Morgner
 */
public class IndexedRenderBox extends BlockRenderBox
{
  private HashMap childs;

  public IndexedRenderBox(final BoxDefinition boxDefinition)
  {
    super(boxDefinition);
    childs = new HashMap();
  }

  public RenderNode getElement (String name)
  {
    return (RenderNode) childs.get(name);
  }

  public void setElement (String name, RenderNode child)
  {
    RenderNode node = (RenderNode) childs.get(name);
    if (node == null)
    {
      // oh, we have no such child at all. Nice! Add as usual.
      super.addChild(child);
      childs.put (name, child);
    }
    else
    {
      // remove the old one and put this one in ..
      // this preserves the order of the childs ..
      replaceChild(node, child);
      childs.put (name, child);
    }
  }
}
