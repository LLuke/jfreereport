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
 * ConfigTreeNode.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Object Refinery Limited);
 *
 * $Id: ConfigTreeNode.java,v 1.2 2003/09/11 22:17:09 taqua Exp $
 *
 * Changes 
 * -------------------------
 * 28-Aug-2003 : Initial version
 *  
 */

package org.jfree.report.modules.gui.config.model;

import javax.swing.tree.TreeNode;

/**
 * The config tree is the base class for all nodes in the ConfigTreeModel.
 * 
 * @author Thomas Morgner
 */
public interface ConfigTreeNode extends TreeNode
{
  /**
   * Returns the name of the node.
   * 
   * @return the name of the node.
   */
  public String getName();
  
  /**
   * Defines the parent of this node; this replaces all previously defined
   * parents.
   * 
   * @param parent the new parent node. 
   */
  public void setParent (TreeNode parent);
}
