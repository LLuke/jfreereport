/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
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
 * $Id: TableCellBackground.java,v 1.9 2003/10/11 15:50:17 taqua Exp $
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
public strictfp class TableCellBackground extends TableCellData implements Cloneable
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
   * @param cellBounds the bounds of the cell for which to form the background.
   * @return a union of the background informations.
   */
  public TableCellBackground merge(final TableCellBackground background,
                                   final Rectangle2D cellBounds)
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

//    Log.debug ("This: " + this);
//    Log.debug ("  BG: " + background);
//    Log.debug ("    CellBounds: " + cellBounds);
    if (isBottomBorderDefinition(cellBounds, this))
    {
      final TableCellBackground merged = background.createMergedInstance(cellBounds);
      merged.color = color;
      merged.mergeBottomBorder(this);
//      Log.debug ("Bottom:This: " + merged);
      return merged;
    }
    if (isBottomBorderDefinition(cellBounds, background))
    {
      final TableCellBackground merged = createMergedInstance(cellBounds);
      merged.color = color;
      merged.mergeBottomBorder(background);
//      Log.debug ("Bottom:Bg: " + merged);
      return merged;
    }
    if (isRightBorderDefinition(cellBounds, this))
    {
      final TableCellBackground merged = background.createMergedInstance(cellBounds);
      merged.color = color;
      merged.mergeRightBorder(this);
//      Log.debug ("Right:This: " + merged);
      return merged;
    }
    if (isRightBorderDefinition(cellBounds, background))
    {
      final TableCellBackground merged = createMergedInstance(cellBounds);
      merged.color = color;
      merged.mergeRightBorder(background);
//      Log.debug ("Right:Bg: " + merged);
      return merged;
    }

    // the merged area is dominated by the background ...
    if (cellBounds.equals(getBounds()))
    {
      final TableCellBackground merged = createMergedInstance(cellBounds);
      merged.color = color;
      merged.mergeAllBorders(background);
//      Log.debug ("Bounds:Equal:This: " + merged);
      return merged;
    }

    // the merged area is dominated by the background ...
    if (cellBounds.equals(background.getBounds()))
    {
      final TableCellBackground merged = background.createMergedInstance(cellBounds);
      merged.color = color;
      merged.mergeAllBorders(this);
//      Log.debug ("Bottom:Equal:Bg: " + merged);
      return merged;
    }

    // we don't copy the borders, just the plain background color
    // as the borders won't fit ...
    // our background should define the cells background ...
    if (getBounds().contains(cellBounds))
    {
      final TableCellBackground merged = createMergedInstance(cellBounds);
      merged.color = color;
//      Log.debug ("None:This: " + merged);
      return merged;
    }
    else
    {
      final TableCellBackground merged = background.createMergedInstance(cellBounds);
      merged.color = color;
//      Log.debug ("None:Bg: " + merged);
      return merged;
    }
    // create an unmerged instance in any other case, as the bounds are not releated
    // in a useable way.
  }

  /**
   * Checks, whether the given background definition defines a bottom border of the
   * unified background.
   *
   * @param bounds the unified bounds.
   * @param background the background definition which should be checked.
   * @return true, if the background definition defines a bottom border, false otherwise.
   */
  private boolean isBottomBorderDefinition (Rectangle2D bounds, TableCellBackground background)
  {
    final Rectangle2D bgBounds = background.getBounds();
    return ((bgBounds.getHeight() == 0) &&
        (bgBounds.getY() == (bounds.getHeight() + bounds.getY())));
  }

  /**
   * The background defines a bottom border for the cell.
   *
   * @param background the background
   */
  private void mergeBottomBorder (final TableCellBackground background)
  {
    // map the background's top border to the bottom side
    if (getColorBottom() == null || getBorderSizeBottom() == 0)
    {
      setBorderBottom(background.getColorTop(), background.getBorderSizeTop());
    }
  }

  /**
   * Checks, whether the given background definition defines a right border of the
   * unified background.
   *
   * @param bounds the unified bounds.
   * @param background the background definition which should be checked.
   * @return true, if the background definition defines a right border, false otherwise.
   */
  private boolean isRightBorderDefinition (Rectangle2D bounds, TableCellBackground background)
  {
    final Rectangle2D bgBounds = background.getBounds();
    return ((bgBounds.getWidth() == 0) &&
        (bgBounds.getX() == (bounds.getX() + bounds.getWidth())));
  }

  /**
   * Merges the left border of the background definition with the right border
   * of this one.
   * 
   * @param background the other background definition.
   */
  private void mergeRightBorder (final TableCellBackground background)
  {
    // map the background's left border to the right side
    if (getColorRight() == null || getBorderSizeRight() == 0)
    {
      setBorderRight(background.getColorLeft(), background.getBorderSizeLeft());
    }
  }

  /**
   * Merges the borders of the <code>background</code> cell and stores the result in
   * the <code>merged</code> cell. This should only be called, if the bounds of both
   * background definitions are equal, or the result may be undefined.
   *
   * @param background the background cell that should be merged with this cell.
   */
  private void mergeAllBorders(final TableCellBackground background)
  {

    if (getColorTop() == null || getBorderSizeTop() == 0)
    {
      setBorderTop(background.getColorTop(), background.getBorderSizeTop());
    }

    if (getColorLeft() == null || getBorderSizeLeft() == 0)
    {
      setBorderLeft(background.getColorLeft(), background.getBorderSizeLeft());
    }

    if (getColorRight() == null || getBorderSizeRight() == 0)
    {
      setBorderRight(background.getColorRight(), background.getBorderSizeRight());
    }

    if (getColorBottom() == null || getBorderSizeBottom() == 0)
    {
      setBorderBottom(background.getColorBottom(), background.getBorderSizeBottom());
    }
  }

  /**
   * Creates an merged instance of this background for the given bounds.
   * 
   * @param bounds the new bounds of the merged background
   * @return a copy of this background with new bounds.
   */
  protected TableCellBackground createMergedInstance (Rectangle2D bounds)
  {
    try
    {
      TableCellBackground bg = (TableCellBackground) clone();
      bg.setBounds(bounds);
      return bg;
    }
    catch (CloneNotSupportedException cne)
    {
      // should not happen
      throw new IllegalStateException("Clone caused an unexpected error.");
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
    b.append("TableCellBackground={bounds=");
    b.append(getBounds());
    b.append(", color=");
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
