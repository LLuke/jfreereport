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
 * $Id$
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

public class ConfigTreeRenderer extends DefaultTreeCellRenderer
{
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
   */
  public Component getTreeCellRendererComponent(JTree tree, Object value,
                                                boolean sel,
                                                boolean expanded,
                                                boolean leaf, int row,
                                                boolean hasFocus)
  {
    if (value instanceof ConfigTreeRootNode)
    {
      return super.getTreeCellRendererComponent(tree, "<Root>",
          sel, expanded, leaf, row, hasFocus);
    }
    else if (value instanceof ConfigTreeSectionNode)
    {
      ConfigTreeSectionNode node = (ConfigTreeSectionNode) value;
      return super.getTreeCellRendererComponent(tree, node.getName(),
          sel, expanded, leaf, row, hasFocus);
    }
    else if (value instanceof ConfigTreeModuleNode)
    {
      ConfigTreeModuleNode node = (ConfigTreeModuleNode) value;
      StringBuffer text = new StringBuffer();
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
  public void paint(Graphics g)
  {
    super.paint(new BugFixProxyGraphics2D((Graphics2D) g));
  }
}
