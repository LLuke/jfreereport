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
 * ------------------------
 * TableCellBackground.java
 * ------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: TableCellBackground.java,v 1.8 2003/06/29 16:59:29 taqua Exp $
 *
 * Changes
 * -------
 * 27-Jan-2003 : Initial version
 * 24-Feb-2003 : Fixed Checkstyle issues (DG);
 *
 */
package org.jfree.report.modules.output.table.base;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

/**
 * Encapsulates all TableCellBackground informations, such as borders and background color.
 * <p>
 * The TableCellBackground contains the format information for the table cells.
 * Background information is used to format the tablecells in the {@link TableWriter}.
 * CellBackgrounds can be shared for multiple cells.
 *
 * @author Thomas Morgner
 */
public class TableCellBackground extends TableCellData
{
  /** The top border's size. */
  private float borderSizeTop;

  /** The bottom border's size. */
  private float borderSizeBottom;

  /** The left border's size. */
  private float borderSizeLeft;

  /** The right border's size. */
  private float borderSizeRight;

  /** The top border's color. */
  private Color colorTop;

  /** The left border's color. */
  private Color colorLeft;

  /** The bottom border's color. */
  private Color colorBottom;

  /** The right border's color. */
  private Color colorRight;

  /** The cell background color. */
  private Color color;

  /**
   * Creates a table cell background with the given bounds, no borders and the specified
   * color as background. If the color is <code>null</code>, no background is set.
   *
   * @param outerBounds  the background cell size
   * @param color  the background color, <code>null</code> for no background.
   */
  public TableCellBackground(final Rectangle2D outerBounds, final Color color)
  {
    super(outerBounds);
    this.color = color;
  }

  /**
   * Returns <code>true</code>, as this is a cell background definition.
   *
   * @return always true, this is a data cell.
   *
   * @see TableCellData#isBackground
   */
  public boolean isBackground()
  {
    return true;
  }

  /**
   * Gets the background color for this cell, or <code>null</code> if this cell has no background.
   *
   * @return the background color or <code>null</code>.
   */
  public Color getColor()
  {
    return color;
  }

  /**
   * Defines the top border. If color is null or the size is 0, then no top border
   * is drawn.
   *
   * @param color the color of the top border.
   * @param size the line width of the top border.
   */
  public void setBorderTop(final Color color, final float size)
  {
    colorTop = color;
    borderSizeTop = size;
  }

  /**
   * Defines the left border. If color is null or the size is 0, then no left border
   * is drawn.
   *
   * @param color the color of the left border.
   * @param size the line width of the left border.
   */
  public void setBorderLeft(final Color color, final float size)
  {
    colorLeft = color;
    borderSizeLeft = size;
  }

  /**
   * Defines the bottom border. If color is null or the size is 0, then no bottom border
   * is drawn.
   *
   * @param color the color of the bottom border.
   * @param size the line width of the bottom border.
   */
  public void setBorderBottom(final Color color, final float size)
  {
    colorBottom = color;
    borderSizeBottom = size;
  }

  /**
   * Defines the right border. If color is null or the size is 0, then no right border
   * is drawn.
   *
   * @param color the color of the right border.
   * @param size the line width of the right border.
   */
  public void setBorderRight(final Color color, final float size)
  {
    colorRight = color;
    borderSizeRight = size;
  }

  /**
   * Returns the line width of the top border.
   *
   * @return the  line width of the top border.
   */
  public float getBorderSizeTop()
  {
    return borderSizeTop;
  }

  /**
   * Returns the line width of the bottom border.
   *
   * @return the  line width of the bottom border.
   */
  public float getBorderSizeBottom()
  {
    return borderSizeBottom;
  }

  /**
   * Returns the line width of the left border.
   *
   * @return the  line width of the left border.
   */
  public float getBorderSizeLeft()
  {
    return borderSizeLeft;
  }

  /**
   * Returns the line width of the right border.
   *
   * @return the  line width of the right border.
   */
  public float getBorderSizeRight()
  {
    return borderSizeRight;
  }

  /**
   * Returns the line color of the top border.
   *
   * @return the  color of the top border.
   */
  public Color getColorTop()
  {
    return colorTop;
  }

  /**
   * Returns the line color of the left border.
   *
   * @return the  color of the left border.
   */
  public Color getColorLeft()
  {
    return colorLeft;
  }

  /**
   * Returns the line color of the bottom border.
   *
   * @return the  color of the bottom border.
   */
  public Color getColorBottom()
  {
    return colorBottom;
  }

  /**
   * Returns the line color of the right border.
   *
   * @return the  color of the right border.
   */
  public Color getColorRight()
  {
    return colorRight;
  }

  /**
   * Merges this background with the given background and returns the
   * result. The given background is considered to be overlayed by this
   * background.
   *
   * @param background the other background cell
   * @return a union of the background informations.
   */
  public TableCellBackground merge(final TableCellBackground background)
  {
    Color color = getColor();
    if (color == null)
    {
      color = background.getColor();
    }
    else
    {
      if (background.getColor() != null)
      {
        color = addColor(color, background.getColor());
      }
    }

    final TableCellBackground merged = new TableCellBackground(getBounds(), color);
    mergeBorders(background, merged);
    return merged;
  }

  /**
   * Merges the borders of the <code>background</code> cell and stores the result in
   * the <code>merged</code> cell.
   *
   * @param background the background cell that should be merged with this cell.
   * @param merged the resulting cell background.
   */
  private void mergeBorders(final TableCellBackground background, final TableCellBackground merged)
  {
    if (getColorTop() == null || getBorderSizeTop() == 0)
    {
      merged.setBorderTop(background.getColorTop(), background.getBorderSizeTop());
    }
    else
    {
      merged.setBorderTop(getColorTop(), getBorderSizeTop());
    }

    if (getColorBottom() == null || getBorderSizeBottom() == 0)
    {
      merged.setBorderBottom(background.getColorBottom(), background.getBorderSizeBottom());
    }
    else
    {
      merged.setBorderBottom(getColorBottom(), getBorderSizeBottom());
    }

    if (getColorLeft() == null || getBorderSizeLeft() == 0)
    {
      merged.setBorderLeft(background.getColorLeft(), background.getBorderSizeLeft());
    }
    else
    {
      merged.setBorderLeft(getColorLeft(), getBorderSizeLeft());
    }

    if (getColorRight() == null || getBorderSizeRight() == 0)
    {
      merged.setBorderRight(background.getColorRight(), background.getBorderSizeRight());
    }
    else
    {
      merged.setBorderRight(getColorRight(), getBorderSizeRight());
    }
  }

  /**
   * Adds two colors, the result is the mixed color of the base color and the paint color.
   *
   * @param base the base color
   * @param paint the overlay color
   * @return the merged colors.
   */
  private Color addColor(final Color base, final Color paint)
  {
    final BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
    final Graphics g = img.getGraphics();

    g.setColor(Color.white);
    g.drawRect(0, 0, 1, 1);

    g.setColor(base);
    g.drawRect(0, 0, 1, 1);
    g.setColor(paint);
    g.drawRect(0, 0, 1, 1);

    return new Color(img.getRGB(0, 0), true);
  }

  /**
   * Returns an String representation of this table cell background.
   *
   * @return the string representation of this cell background.
   */
  public String toString()
  {
    final StringBuffer b = new StringBuffer();
    b.append("TableCellBackground={color=");
    b.append(color);
    b.append(", colorTop=");
    b.append(colorTop);
    b.append(", widthTop=");
    b.append(borderSizeTop);
    b.append(", colorLeft=");
    b.append(colorLeft);
    b.append(", widthLeft=");
    b.append(borderSizeLeft);
    b.append(", colorBottom=");
    b.append(colorBottom);
    b.append(", widthBottom=");
    b.append(borderSizeBottom);
    b.append(", colorRight=");
    b.append(colorRight);
    b.append(", widthRight=");
    b.append(borderSizeRight);
    b.append("}");
    return b.toString();
  }

  /**
   * Tests this object for equality with another object.
   *
   * @param o  the other object.
   *
   * @return A boolean.
   */
  public boolean equals(final Object o)
  {
    if (this == o)
    {
      return true;
    }
    if (!(o instanceof TableCellBackground))
    {
      return false;
    }

    final TableCellBackground tableCellBackground = (TableCellBackground) o;

    if (borderSizeBottom != tableCellBackground.borderSizeBottom)
    {
      return false;
    }
    if (borderSizeLeft != tableCellBackground.borderSizeLeft)
    {
      return false;
    }
    if (borderSizeRight != tableCellBackground.borderSizeRight)
    {
      return false;
    }
    if (borderSizeTop != tableCellBackground.borderSizeTop)
    {
      return false;
    }
    if (color != null ? !color.equals(tableCellBackground.color)
        : tableCellBackground.color != null)
    {
      return false;
    }
    if (colorBottom != null ? !colorBottom.equals(tableCellBackground.colorBottom)
        : tableCellBackground.colorBottom != null)
    {
      return false;
    }
    if (colorLeft != null ? !colorLeft.equals(tableCellBackground.colorLeft)
        : tableCellBackground.colorLeft != null)
    {
      return false;
    }
    if (colorRight != null ? !colorRight.equals(tableCellBackground.colorRight)
        : tableCellBackground.colorRight != null)
    {
      return false;
    }
    if (colorTop != null ? !colorTop.equals(tableCellBackground.colorTop)
        : tableCellBackground.colorTop != null)
    {
      return false;
    }
    return true;
  }

  /**
   * Returns a hash code for this object.
   *
   * @return A hash code.
   */
  public int hashCode()
  {
    int result;
    result = Float.floatToIntBits(borderSizeTop);
    result = 29 * result + Float.floatToIntBits(borderSizeBottom);
    result = 29 * result + Float.floatToIntBits(borderSizeLeft);
    result = 29 * result + Float.floatToIntBits(borderSizeRight);
    result = 29 * result + (colorTop != null ? colorTop.hashCode() : 0);
    result = 29 * result + (colorLeft != null ? colorLeft.hashCode() : 0);
    result = 29 * result + (colorBottom != null ? colorBottom.hashCode() : 0);
    result = 29 * result + (colorRight != null ? colorRight.hashCode() : 0);
    result = 29 * result + (color != null ? color.hashCode() : 0);
    return result;
  }
}
