/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
 *
 * (C) Copyright 2000-2003, by Object Refinery Limited and Contributors.
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
 * Contributor(s):   David Gilbert (for Object Refinery Limited);
 *
 * $Id: ConfigTreeModel.java,v 1.6 2003/11/07 18:33:52 taqua Exp $
 *
 * Changes 
 * -------------------------
 * 28.08.2003 : Initial version
 *  
 */

package org.jfree.report.modules.gui.config.model;

import java.io.InputStream;
import java.util.ArrayList;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import org.jfree.report.util.ReportConfiguration;

/**
 * Provides a tree model view for an report configuration. The configuration
 * will be separated into a local and a global part. The local nodes are read
 * from the report's configuration instance, while the global nodes are always
 * read from the global report configuration instance.
 * 
 * @author Thomas Morgner
 */
public class ConfigTreeModel implements TreeModel
{
  /** The root node for the tree model. */
  private final ConfigTreeRootNode root;
  /** The section containing the global tree nodes. */
  private final ConfigTreeSectionNode globalSection;
  /** The section containing the report-local tree nodes. */
  private final ConfigTreeSectionNode localSection;
  /** The factory used to update the tree model. */
  private ModuleNodeFactory nodeFactory;
  /** A list of model listeners. */
  private final ArrayList listeners;

  /**
   * Creates a new tree model from the given specifications. These
   * specifications contain the config description entry definitions
   * used to describe the report configuration keys.
   * 
   * @param specs the specifications.
   * @throws ConfigTreeModelException if an error occured while reading
   * the sprecifications and building the model.
   */
  public ConfigTreeModel(final InputStream specs) throws ConfigTreeModelException
  {
    this.root = new ConfigTreeRootNode("<root>");
    this.globalSection = new ConfigTreeSectionNode("Global configuration");
    this.localSection = new ConfigTreeSectionNode("Local configuration");
    this.listeners = new ArrayList();

    root.add(globalSection);
    root.add(localSection);

    try
    {
      nodeFactory = new ModuleNodeFactory(specs);
    }
    catch (Exception ioe)
    {
      throw new ConfigTreeModelException ("Failed to initialize the model.", ioe);
    }
  }

  /**
   * Initializes the tree from the given report configuration.
   * 
   * @param config the report configuration from where to read the values.
   * @throws ConfigTreeModelException if an error occured while creating the model.
   */
  public void init (final ReportConfiguration config)
    throws ConfigTreeModelException
  {
    globalSection.reset();
    localSection.reset();
    nodeFactory.init(config);
    ArrayList list = nodeFactory.getGlobalNodes();
    for (int i = 0; i < list.size(); i++)
    {
      globalSection.add ((ConfigTreeNode) list.get(i));
    }
    list = nodeFactory.getLocalNodes();
    for (int i = 0; i < list.size(); i++)
    {
      localSection.add ((ConfigTreeNode) list.get(i));
    }

    fireTreeModelChanged();
  }

  /**
   * Informs all listeners, that the tree's contents have changed.
   */
  private void fireTreeModelChanged ()
  {
    for (int i = 0; i < listeners.size(); i++)
    {
      final TreeModelListener l = (TreeModelListener) listeners.get(i);
      l.treeStructureChanged(new TreeModelEvent(this, new TreePath(root)));
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
   * @param   index the index from where to read the child.
   * @return  the child of <code>parent</code> at index <code>index</code>
   */
  public Object getChild(final Object parent, final int index)
  {
    final TreeNode node = (TreeNode) parent;
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
  public int getChildCount(final Object parent)
  {
    final TreeNode node = (TreeNode) parent;
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
  public boolean isLeaf(final Object node)
  {
    final TreeNode tnode = (TreeNode) node;
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
  public void valueForPathChanged(final TreePath path, final Object newValue)
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
  public int getIndexOfChild(final Object parent, final Object child)
  {
    final TreeNode node = (TreeNode) parent;
    final TreeNode childNode = (TreeNode) child;
    return node.getIndex(childNode);
  }

  /**
   * Adds a listener for the <code>TreeModelEvent</code>
   * posted after the tree changes.
   *
   * @param   l       the listener to add
   * @see     #removeTreeModelListener
   */
  public void addTreeModelListener(final TreeModelListener l)
  {
    if (l == null)
    {
      throw new NullPointerException();
    }
    listeners.add (l);
  }

  /**
   * Removes a listener previously added with
   * <code>addTreeModelListener</code>.
   *
   * @see     #addTreeModelListener
   * @param   l       the listener to remove
   */
  public void removeTreeModelListener(final TreeModelListener l)
  {
    listeners.remove(l);
  }

  /**
   * Returns the entry for the given key or null, if the key has no
   * metadata.
   * @param key the name of the key
   * @return the entry or null if not found.
   */
  public ConfigDescriptionEntry getEntryForKey (final String key)
  {
    return nodeFactory.getEntryForKey(key);
  }
}
