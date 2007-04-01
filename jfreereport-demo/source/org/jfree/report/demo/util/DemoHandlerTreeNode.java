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
 * DemoHandlerTreeNode.java
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.report.demo.util;

import java.util.Enumeration;
import javax.swing.tree.TreeNode;

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
