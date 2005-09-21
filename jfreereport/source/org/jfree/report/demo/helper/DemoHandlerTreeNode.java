/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
 *
 * (C) Copyright 2000-2004, by Simba Management Limited and Contributors.
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
 * DemoHandlerTreeNode.java
 * ------------------------------
 * (C)opyright 2004, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: Treatment.java,v 1.2 2005/01/25 01:13:55 taqua Exp $
 *
 * Changes
 * -------------------------
 * 27-Aug-2005 : Initial version
 *
 */
package org.jfree.report.demo.helper;

import java.util.Enumeration;
import javax.swing.tree.TreeNode;

import org.jfree.report.util.ArrayEnumeration;

/**
 * The DemoHandlerTreeNode is used to build the tree component to select a single
 * demo from within a CompoundDemoFrame.
 *
 * @author Thomas Morgner
 */
public class DemoHandlerTreeNode implements TreeNode
{
  private TreeNode parent;
  private DemoHandler handler;

  public DemoHandlerTreeNode(final TreeNode parent, final DemoHandler handler)
  {
    this.parent = parent;
    this.handler = handler;
  }

  public DemoHandler getHandler()
  {
    return handler;
  }

  public TreeNode getChildAt(int childIndex)
  {
    return null;
  }

  public int getChildCount()
  {
    return 0;
  }

  public TreeNode getParent()
  {
    return parent;
  }

  public int getIndex(TreeNode node)
  {
    return -1;
  }

  public boolean getAllowsChildren()
  {
    return false;
  }

  public boolean isLeaf()
  {
    return true;
  }

  public Enumeration children()
  {
    return new ArrayEnumeration (new Object[0]);
  }

  public String toString()
  {
    return handler.getDemoName();
  }
}
