/**
 * ===========================================
 * JFreeReport : a free Java reporting library
 * ===========================================
 *
 * Project Info:  http://reporting.pentaho.org/
 *
 * (C) Copyright 2006-2007, by Pentaho Corporation and Contributors.
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
 * DemoSelectorTreeNode.java
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.report.demo.util;

import java.util.ArrayList;
import java.util.Enumeration;
import javax.swing.tree.TreeNode;

/**
 * A DemoSelectorTreeNode encapsulates a DemoSelector and makes it
 * accessible from within a JTree.
 *
 * @author Thomas Morgner
 */
public class DemoSelectorTreeNode implements TreeNode
{
  private TreeNode parent;
  private DemoSelector selector;
  private TreeNode[] childs;

  public DemoSelectorTreeNode(final TreeNode parent,
                              final DemoSelector selector)
  {
    this.parent = parent;
    this.selector = selector;

    final ArrayList nodes = new ArrayList();
    final DemoSelector[] selectors = selector.getChilds();
    for (int i = 0; i < selectors.length; i++)
    {
      DemoSelector demoSelector = selectors[i];
      nodes.add(new DemoSelectorTreeNode(this, demoSelector));
    }
    final DemoHandler[] handlers = selector.getDemos();
    for (int i = 0; i < handlers.length; i++)
    {
      DemoHandler handler = handlers[i];
      nodes.add(new DemoHandlerTreeNode(this, handler));
    }
    this.childs = (TreeNode[]) nodes.toArray(new TreeNode[nodes.size()]);
  }

  public DemoSelector getSelector()
  {
    return selector;
  }

  public TreeNode getChildAt(int childIndex)
  {
    return childs[childIndex];
  }

  public int getChildCount()
  {
    return childs.length;
  }

  public TreeNode getParent()
  {
    return parent;
  }

  public int getIndex(TreeNode node)
  {
    for (int i = 0; i < childs.length; i++)
    {
      TreeNode child = childs[i];
      if (node == child)
      {
        return i;
      }
    }
    return -1;
  }

  public boolean getAllowsChildren()
  {
    return true;
  }

  public boolean isLeaf()
  {
    return false;
  }

  public Enumeration children()
  {
    return new ArrayEnumeration(childs);
  }

  public String toString()
  {
    return selector.getName();
  }
}
