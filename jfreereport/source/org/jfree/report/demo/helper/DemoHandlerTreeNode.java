package org.jfree.report.demo.helper;

import java.util.Enumeration;
import javax.swing.tree.TreeNode;

import org.jfree.report.util.ArrayEnumeration;

/**
 * Creation-Date: 27.08.2005, 13:40:16
 *
 * @author: Thomas Morgner
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
