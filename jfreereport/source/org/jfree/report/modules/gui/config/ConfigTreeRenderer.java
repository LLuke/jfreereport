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
 * ConfigTreeRenderer.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: ConfigTreeRenderer.java,v 1.2 2003/09/08 18:11:48 taqua Exp $
 *
 * Changes
 * -------------------------
 * 30.08.2003 : Initial version
 *
 */

package org.jfree.report.modules.gui.config;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

import org.jfree.report.modules.gui.config.model.ConfigTreeModuleNode;
import org.jfree.report.modules.gui.config.model.ConfigTreeRootNode;
import org.jfree.report.modules.gui.config.model.ConfigTreeSectionNode;

/**
 * Implements a config tree renderer that fixes some AWT-Graphics problems
 * in conjunction with the clipping. It seems that the AWT-Graphics ignores
 * the clipping bounds for some primitive operations. Clipping is done
 * if the operations are performed on the Graphics2D level.
 * 
 * @see org.jfree.report.modules.gui.config.BugFixProxyGraphics2D
 * @author Thomas Morgner
 */
public class ConfigTreeRenderer extends DefaultTreeCellRenderer
{
  /**
   * DefaultConstructor.
   */
  public ConfigTreeRenderer()
  {
    setDoubleBuffered(false);
  }

  /**
   * Configures the renderer based on the passed in components.
   * The value is set from messaging the tree with
   * <code>convertValueToText</code>, which ultimately invokes
   * <code>toString</code> on <code>value</code>.
   * The foreground color is set based on the selection and the icon
   * is set based on on leaf and expanded.
   * 
   * @param tree the tree that renders the node.
   * @param value the tree node
   * @param sel whether the node is selected.
   * @param expanded whether the node is expanded
   * @param leaf whether the node is a leaf
   * @param row the row number of the node in the tree.
   * @param hasFocus whether the node has the input focus
   * @return the renderer component.
   */
  public Component getTreeCellRendererComponent(final JTree tree, final Object value,
                                                final boolean sel,
                                                final boolean expanded,
                                                final boolean leaf, final int row,
                                                final boolean hasFocus)
  {
    if (value instanceof ConfigTreeRootNode)
    {
      return super.getTreeCellRendererComponent(tree, "<Root>",
          sel, expanded, leaf, row, hasFocus);
    }
    else if (value instanceof ConfigTreeSectionNode)
    {
      final ConfigTreeSectionNode node = (ConfigTreeSectionNode) value;
      return super.getTreeCellRendererComponent(tree, node.getName(),
          sel, expanded, leaf, row, hasFocus);
    }
    else if (value instanceof ConfigTreeModuleNode)
    {
      final ConfigTreeModuleNode node = (ConfigTreeModuleNode) value;
      final StringBuffer text = new StringBuffer();
      text.append(node.getModule().getName());
      text.append(" - ");
      text.append(node.getModule().getMajorVersion());
      text.append(".");
      text.append(node.getModule().getMinorVersion());
      text.append("-");
      text.append(node.getModule().getPatchLevel());
      return super.getTreeCellRendererComponent(tree, text.toString(),
          sel, expanded, leaf, row, hasFocus);
    }
    return super.getTreeCellRendererComponent(tree, value,
        sel, expanded, leaf, row, hasFocus);
  }

  /**
   * Paints the value.  The background is filled based on selected.
   * The TreeCellRenderer or Swing or something else has a bug inside so
   * that the clipping of the graphics is not done correctly. If a rectangle
   * is painted with Graphics.fillRect(int, int, int, int) the graphics
   * is totally messed up.
   *
   * @param g the graphics.
   */
  public void paint(final Graphics g)
  {
    super.paint(new BugFixProxyGraphics2D((Graphics2D) g));
  }
}
