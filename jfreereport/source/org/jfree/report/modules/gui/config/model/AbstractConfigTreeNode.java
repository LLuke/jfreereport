/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
 *
 * (C) Copyright 2000-2003, by Simba Management Limited and Contributors.
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
 * ------------------------------
 * AbstractConfigTreeNode.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: AbstractConfigTreeNode.java,v 1.1 2003/08/30 15:05:00 taqua Exp $
 *
 * Changes 
 * -------------------------
 * 28.08.2003 : Initial version
 *  
 */

package org.jfree.report.modules.gui.config.model;

import java.util.Enumeration;
import java.util.Collections;
import java.util.ArrayList;
import javax.swing.tree.TreeNode;

import org.jfree.report.modules.gui.config.model.ConfigTreeNode;

public abstract class AbstractConfigTreeNode implements ConfigTreeNode
{
  private ArrayList childs;
  private String name;
  private TreeNode parent;

  public AbstractConfigTreeNode(String name)
  {
    childs = new ArrayList();
    this.name = name;
  }

  public void add (ConfigTreeNode node)
  {
    if (node == null)
    {
      throw new NullPointerException();
    }
    if (childs.contains(node) == false)
    {
      childs.add (node);
      node.setParent(this);
    }
  }

  protected void reset ()
  {
    childs.clear();
  }
  
  /**
   * Returns the child <code>TreeNode</code> at index
   * <code>childIndex</code>.
   */
  public TreeNode getChildAt(int childIndex)
  {
    return (TreeNode) childs.get(childIndex);
  }

  /**
   * Returns the number of children <code>TreeNode</code>s the receiver
   * contains.
   */
  public int getChildCount()
  {
    return childs.size();
  }

  /**
   * Returns true if the receiver allows children.
   */
  public boolean getAllowsChildren()
  {
    return true;
  }

  /**
   * Returns the index of <code>node</code> in the receivers children.
   * If the receiver does not contain <code>node</code>, -1 will be
   * returned.
   */
  public int getIndex(TreeNode node)
  {
    return childs.indexOf(node);
  }

  /**
   * Returns true if the receiver is a leaf.
   */
  public boolean isLeaf()
  {
    return false;
  }

  /**
   * Returns the children of the receiver as an <code>Enumeration</code>.
   */
  public Enumeration children()
  {
    return Collections.enumeration(childs);
  }

  public String getName()
  {
    return name;
  }

  public TreeNode getParent()
  {
    return parent;
  }

  public void setParent(TreeNode parent)
  {
    this.parent = parent;
  }
}
