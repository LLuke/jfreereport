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
 * ConfigTreeRootNode.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Object Refinery Limited);
 *
 * $Id: ConfigTreeRootNode.java,v 1.3 2003/11/07 18:33:52 taqua Exp $
 *
 * Changes 
 * -------------------------
 * 28-Aug-2003 : Initial version
 *  
 */

package org.jfree.report.modules.gui.config.model;

import javax.swing.tree.TreeNode;

/**
 * The root node contains the local and the global node and is the
 * main entry point into the tree.
 * 
 * @author Thomas Morgner
 */
public class ConfigTreeRootNode extends AbstractConfigTreeNode
{
  /**
   * Creates a new root node with the given name.
   * 
   * @param name the name of the node.
   */
  public ConfigTreeRootNode(final String name)
  {
    super(name);
  }

  /**
   * Returns the parent <code>TreeNode</code> of the receiver.
   * 
   * @return always null, as the root node never has a parent. 
   */
  public TreeNode getParent()
  {
    return null;
  }

}
