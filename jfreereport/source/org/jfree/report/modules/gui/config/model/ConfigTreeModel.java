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
 * ConfigTreeModel.java
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

import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import org.jfree.report.modules.Module;
import org.jfree.report.modules.PackageManager;
import org.jfree.report.util.ReportConfiguration;

public class ConfigTreeModel implements TreeModel
{
  private ConfigTreeRootNode root;
  private ConfigTreeSectionNode globalSection;
  private ConfigTreeSectionNode localSection;

  public ConfigTreeModel(ReportConfiguration config)
  {
    this.root = new ConfigTreeRootNode("<root>");
    this.globalSection = new ConfigTreeSectionNode("Global configuration");
    this.localSection = new ConfigTreeSectionNode("Local configuration");

    root.add(globalSection);
    root.add(localSection);

    PackageManager pm = PackageManager.getInstance();
    Module[] mods = pm.getActiveModules();

    for (int i = 0; i < mods.length; i++)
    {
      globalSection.add (new ConfigTreeModuleNode(mods[i], config));
      localSection.add (new ConfigTreeModuleNode(mods[i], config));
    }
  }

  /**
   * Returns the root of the tree.  Returns <code>null</code>
   * only if the tree has no nodes.
   *
   * @return  the root of the tree
   */
  public Object getRoot()
  {
    return root;
  }

  /**
   * Returns the child of <code>parent</code> at index <code>index</code>
   * in the parent's
   * child array.  <code>parent</code> must be a node previously obtained
   * from this data source. This should not return <code>null</code>
   * if <code>index</code>
   * is a valid index for <code>parent</code> (that is <code>index >= 0 &&
   * index < getChildCount(parent</code>)).
   *
   * @param   parent  a node in the tree, obtained from this data source
   * @return  the child of <code>parent</code> at index <code>index</code>
   */
  public Object getChild(Object parent, int index)
  {
    TreeNode node = (TreeNode) parent;
    return node.getChildAt(index);
  }

  /**
   * Returns the number of children of <code>parent</code>.
   * Returns 0 if the node
   * is a leaf or if it has no children.  <code>parent</code> must be a node
   * previously obtained from this data source.
   *
   * @param   parent  a node in the tree, obtained from this data source
   * @return  the number of children of the node <code>parent</code>
   */
  public int getChildCount(Object parent)
  {
    TreeNode node = (TreeNode) parent;
    return node.getChildCount();
  }

  /**
   * Returns <code>true</code> if <code>node</code> is a leaf.
   * It is possible for this method to return <code>false</code>
   * even if <code>node</code> has no children.
   * A directory in a filesystem, for example,
   * may contain no files; the node representing
   * the directory is not a leaf, but it also has no children.
   *
   * @param   node  a node in the tree, obtained from this data source
   * @return  true if <code>node</code> is a leaf
   */
  public boolean isLeaf(Object node)
  {
    TreeNode tnode = (TreeNode) node;
    return tnode.isLeaf();
  }

  /**
   * Messaged when the user has altered the value for the item identified
   * by <code>path</code> to <code>newValue</code>.
   * If <code>newValue</code> signifies a truly new value
   * the model should post a <code>treeNodesChanged</code> event.
   *
   * @param path path to the node that the user has altered
   * @param newValue the new value from the TreeCellEditor
   */
  public void valueForPathChanged(TreePath path, Object newValue)
  {
  }

  /**
   * Returns the index of child in parent.  If <code>parent</code>
   * is <code>null</code> or <code>child</code> is <code>null</code>,
   * returns -1.
   *
   * @param parent a note in the tree, obtained from this data source
   * @param child the node we are interested in
   * @return the index of the child in the parent, or -1 if either
   *    <code>child</code> or <code>parent</code> are <code>null</code>
   */
  public int getIndexOfChild(Object parent, Object child)
  {
    TreeNode node = (TreeNode) parent;
    TreeNode childNode = (TreeNode) child;
    return node.getIndex(childNode);
  }

  /**
   * Adds a listener for the <code>TreeModelEvent</code>
   * posted after the tree changes.
   *
   * @param   l       the listener to add
   * @see     #removeTreeModelListener
   */
  public void addTreeModelListener(TreeModelListener l)
  {
  }

  /**
   * Removes a listener previously added with
   * <code>addTreeModelListener</code>.
   *
   * @see     #addTreeModelListener
   * @param   l       the listener to remove
   */
  public void removeTreeModelListener(TreeModelListener l)
  {
  }
}
