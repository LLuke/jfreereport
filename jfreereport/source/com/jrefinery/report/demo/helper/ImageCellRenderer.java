/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
 *
 * (C) Copyright 2000-2002, by Simba Management Limited and Contributors.
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
 * ----------------------
 * ImageCellRenderer.java
 * ----------------------
 * (C)opyright 2002, by Simba Management Limited.
 *
 * Original Author:  David Gilbert (for Simba Management Limited);
 * Contributor(s):   -;
 *
 * $Id: ImageCellRenderer.java,v 1.1 2003/06/19 18:46:35 taqua Exp $
 *
 * Changes
 * -------
 * 15-Jul-2002 : Version 1 (DG);
 *
 */

package com.jrefinery.report.demo.helper;

import java.awt.Component;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

/**
 * A table cell renderer that draws an image in a table cell.
 * <P>
 * This class will be moved to the JCommon class library.
 *
 * @author David Gilbert
 */
public class ImageCellRenderer extends DefaultTableCellRenderer implements TableCellRenderer
{

  /** The icon. */
  private ImageIcon icon = new ImageIcon();

  /**
   * Constructs a new renderer.
   */
  public ImageCellRenderer()
  {
    super();
    setHorizontalAlignment(JLabel.CENTER);
    setVerticalAlignment(JLabel.CENTER);
    setIcon(icon);
  }

  /**
   * Returns itself as the renderer. Supports the TableCellRenderer interface.
   *
   * @param table The table.
   * @param value The data to be rendered.
   * @param isSelected A boolean that indicates whether or not the cell is selected.
   * @param hasFocus A boolean that indicates whether or not the cell has the focus.
   * @param row The (zero-based) row index.
   * @param column The (zero-based) column index.
   * @return The component that can render the contents of the cell.
   */
  public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                 boolean hasFocus, int row, int column)
  {

    setFont(null);
    icon.setImage((Image) value);
    if (isSelected)
    {
      setBackground(table.getSelectionBackground());
    }
    else
    {
      setBackground(null);
    }
    return this;
  }

}
