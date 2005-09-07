package org.jfree.report.demo.helper;

import java.util.ArrayList;
import java.util.Enumeration;
import javax.swing.tree.TreeNode;

import org.jfree.report.util.ArrayEnumeration;

/**
 * Creation-Date: 27.08.2005, 13:34:49
 *
 * @author: Thomas Morgner
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
