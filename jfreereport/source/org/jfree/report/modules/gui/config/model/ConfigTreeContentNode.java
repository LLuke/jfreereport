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
 * ConfigTreeContentNode.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id$
 *
 * Changes 
 * -------------------------
 * 28.08.2003 : Initial version
 *  
 */

package org.jfree.report.modules.gui.config.model;

import java.util.Collections;
import java.util.Enumeration;
import javax.swing.tree.TreeNode;

public class ConfigTreeContentNode implements ConfigTreeNode
{
  private TreeNode parent;
  private ConfigDescriptionEntry configDescriptionEntry;

  public ConfigTreeContentNode(ConfigDescriptionEntry configDescriptionEntry)
  {
    this.configDescriptionEntry = configDescriptionEntry;
  }

  public String getName()
  {
    return null;
  }

  public void setParent(TreeNode parent)
  {
    this.parent = parent;
  }

  /**
   * Returns the child <code>TreeNode</code> at index
   * <code>childIndex</code>.
   */
  public TreeNode getChildAt(int childIndex)
  {
    return null;
  }

  /**
   * Returns the number of children <code>TreeNode</code>s the receiver
   * contains.
   */
  public int getChildCount()
  {
    return 0;
  }

  /**
   * Returns the parent <code>TreeNode</code> of the receiver.
   */
  public TreeNode getParent()
  {
    return parent;
  }

  /**
   * Returns the index of <code>node</code> in the receivers children.
   * If the receiver does not contain <code>node</code>, -1 will be
   * returned.
   */
  public int getIndex(TreeNode node)
  {
    return -1;
  }

  /**
   * Returns true if the receiver allows children.
   */
  public boolean getAllowsChildren()
  {
    return false;
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
    return Collections.enumeration(Collections.EMPTY_LIST);
  }
}
