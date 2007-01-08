/**
 * ===========================================
 * LibLayout : a free Java layouting library
 * ===========================================
 *
 * Project Info:  http://jfreereport.pentaho.org/liblayout/
 *
 * (C) Copyright 2000-2005, by Pentaho Corporation and Contributors.
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
 * $Id: TableLayoutInfo.java,v 1.2 2006/12/03 18:58:09 taqua Exp $
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 */
package org.jfree.layouting.renderer.model.table;

import org.jfree.layouting.renderer.border.RenderLength;

/**
 * Creation-Date: 09.09.2006, 14:50:21
 *
 * @author Thomas Morgner
 */
public class TableLayoutInfo
{
  private RenderLength borderSpacing;
  private RenderLength rowSpacing;

  private boolean displayEmptyCells;
  private boolean collapsingBorderModel;
  private boolean autoLayout;

  public TableLayoutInfo()
  {
  }

  public RenderLength getBorderSpacing()
  {
    return borderSpacing;
  }

  public void setBorderSpacing(final RenderLength borderSpacing)
  {
    this.borderSpacing = borderSpacing;
  }

  public RenderLength getRowSpacing()
  {
    return rowSpacing;
  }

  public void setRowSpacing(final RenderLength rowSpacing)
  {
    this.rowSpacing = rowSpacing;
  }

  public boolean isDisplayEmptyCells()
  {
    return displayEmptyCells;
  }

  public void setDisplayEmptyCells(final boolean displayEmptyCells)
  {
    this.displayEmptyCells = displayEmptyCells;
  }

  public boolean isCollapsingBorderModel()
  {
    return collapsingBorderModel;
  }

  public void setCollapsingBorderModel(final boolean collapsingBorderModel)
  {
    this.collapsingBorderModel = collapsingBorderModel;
  }

  public boolean isAutoLayout()
  {
    return autoLayout;
  }

  public void setAutoLayout(final boolean autoLayout)
  {
    this.autoLayout = autoLayout;
  }
}
