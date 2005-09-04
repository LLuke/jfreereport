/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
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
 * ------------------------
 * TableCellBackground.java
 * ------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Object Refinery Limited);
 *
 * $Id: TableCellBackground.java,v 1.23 2005/04/09 17:43:13 taqua Exp $
 *
 * Changes
 * -------
 * 27-Jan-2003 : Initial version
 * 24-Feb-2003 : Fixed Checkstyle issues (DG);
 *
 */
package org.jfree.report.modules.output.table.base;

import java.awt.Color;
import java.util.ArrayList;

import org.jfree.report.Anchor;
import org.jfree.report.content.Content;
import org.jfree.report.modules.output.meta.MetaElement;
import org.jfree.report.style.ElementStyleSheet;
import org.jfree.report.util.geom.StrictBounds;

/**
 * Encapsulates all TableCellBackground informations, such as borders and
 * background color.
 * <p/>
 * The TableCellBackground contains the format information for the table cells.
 * Background information is used to format the tablecells in the {@link
 * TableWriter}. CellBackgrounds can be shared for multiple cells.
 *
 * @author Thomas Morgner
 */
public strictfp class TableCellBackground
        extends MetaElement implements Cloneable
{
  private ArrayList anchors;

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
//
//  private StrictBounds cellBounds;

  /** The cell background color. */
  private Color color;
  private static final Anchor[] EMPTY_ANCHOR_ARRAY = new Anchor[0];

  public TableCellBackground(final Content elementContent,
                             final ElementStyleSheet style,
                             final Color color)
  {
    super(elementContent, style);
    this.color = color;
  }

  /**
   * Gets the background color for this cell, or <code>null</code> if this cell
   * has no background.
   *
   * @return the background color or <code>null</code>.
   */
  public Color getColor()
  {
    return color;
  }

  protected void setColor(final Color color)
  {
    this.color = color;
  }

  /**
   * Defines the top border. If color is null or the size is 0, then no top
   * border is drawn.
   *
   * @param color the color of the top border.
   * @param size  the line width of the top border.
   */
  public void setBorderTop(final Color color, final float size)
  {
    if (size == 0)
    {
      colorTop = null;
      borderSizeTop = 0;
    }
    else
    {
      colorTop = color;
      borderSizeTop = size;
    }
  }

  /**
   * Defines the left border. If color is null or the size is 0, then no left
   * border is drawn.
   *
   * @param color the color of the left border.
   * @param size  the line width of the left border.
   */
  public void setBorderLeft(final Color color, final float size)
  {
    if (size == 0)
    {
      colorLeft = null;
      borderSizeLeft = 0;
    }
    else
    {
      colorLeft = color;
      borderSizeLeft = size;
    }
  }

  /**
   * Defines the bottom border. If color is null or the size is 0, then no
   * bottom border is drawn.
   *
   * @param color the color of the bottom border.
   * @param size  the line width of the bottom border.
   */
  public void setBorderBottom(final Color color, final float size)
  {
    if (size == 0)
    {
      colorBottom = null;
      borderSizeBottom = 0;
    }
    else
    {
      colorBottom = color;
      borderSizeBottom = size;
    }
  }

  /**
   * Defines the right border. If color is null or the size is 0, then no right
   * border is drawn.
   *
   * @param color the color of the right border.
   * @param size  the line width of the right border.
   */
  public void setBorderRight(final Color color, final float size)
  {
    if (size == 0)
    {
      colorRight = null;
      borderSizeRight = 0;
    }
    else
    {
      colorRight = color;
      borderSizeRight = size;
    }
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
   * Merges this background with the given background and returns the result.
   * The given background is considered to be overlayed by this background.
   * <p/>
   * This function returns a <code>this</code> reference if the background was
   * not merged.
   * <p/>
   * Merging always implicates normalizing. Merging is assumed to happen when
   * new elements get added to an already existing background definition.
   * Therefore only the given new background is normalized - the existing
   * definition must already be normalized.
   * <p>
   * In this method, we assume, that the given background definition will
   * overlay <code>this</code> background. Therefore the borders and colors
   * of the background are considered more significant than this ones.
   *
   *
   * @param background the other background cell
   * @return a union of the background informations.
   */
  public TableCellBackground merge(final TableCellBackground background,
                                   final StrictBounds cellBounds)
  {
    final StrictBounds bounds = getBounds();
    final StrictBounds backgroundBounds = background.getBounds();

    final boolean thisIsALine =
            (bounds.getHeight() == 0 || bounds.getWidth() == 0);
    final boolean otherIsALine =
            (backgroundBounds.getHeight() == 0 ||
             backgroundBounds.getWidth() == 0);

    // merge the borders: CellBounds are most likely totally wrong.
    // we correct that here.
    final TableCellBackground merged = background.normalize(cellBounds);

    // If the given cell does not fully overlap this cell or
    // the this cell does not fully overlap the given cell
    // then we preserve the other's color nothing ...
    final Color orgColor = getColor();
    if (otherIsALine == false && thisIsALine == false)
    {
      // create the unified color for both backgrounds ..
      if (orgColor == null)
      {
        merged.setColor(background.getColor());
      }
      else if (background.getColor() != null)
      {
        final Color color = addColor(orgColor, background.getColor());
        merged.setColor(color);
      }
      else
      {
        merged.setColor(orgColor);
        // do nothing ..
      }
    }
    else if (thisIsALine == false)
    {
      merged.setColor(orgColor);
    }
    else
    {
      // do nothing ..
    }

    // anchors get updated if the top-left corner of the to be merged cell
    // is contained in the new cell
    if (cellBounds.contains(backgroundBounds.getX(), backgroundBounds.getY()))
    {
      if (background.anchors != null)
      {
        if (merged.anchors == null)
        {
          merged.anchors = new ArrayList(background.anchors);
        }
        else
        {
          merged.anchors.addAll(background.anchors);
        }
      }
    }

    // copy the borders of the other background to the merged instance
    if (merged.getColorTop() == null)
    {
      // the thing does not have a border ..
      merged.setBorderTop(getColorTop(), getBorderSizeTop());
    }
    if (merged.getColorLeft() == null)
    {
      // the thing does not have a border ..
      merged.setBorderLeft(getColorLeft(), getBorderSizeLeft());
    }

    return merged;
  }

  /**
   * Creates an splitted instance of this background for the given bounds. This
   * method updates the borders to fit the new bounds. If the old border
   * position is no longer valid, the border is removed. It does not add any
   * new information.
   *
   * @return a copy of this background with new bounds.
   */
  public TableCellBackground createSplittedInstance(final StrictBounds bounds)
  {
    try
    {
      final StrictBounds originalBounds = getBounds();
      final TableCellBackground bg = (TableCellBackground) clone();
      bg.setBounds((StrictBounds) bounds.clone());

      final long orgX2 = originalBounds.getX() + originalBounds.getWidth();
      final long orgY2 = originalBounds.getY() + originalBounds.getHeight();
      if ((bounds.getX() > orgX2 || bounds.getY() > orgY2) ||
              (bounds.getX() == orgX2 && bounds.getY() == orgY2))
      {
        // this cell is out of bounds ..
        bg.setBorderTop(null, 0);
        bg.setBorderBottom(null, 0);
        bg.setBorderLeft(null, 0);
        bg.setBorderRight(null, 0);
        bg.setColor(null);
      }
      else
      {
        if (bg.getTopBorderPos() != getTopBorderPos())
        {
          bg.setBorderTop(null, 0);
        }
        if (bg.getLeftBorderPos() != getLeftBorderPos())
        {
          bg.setBorderLeft(null, 0);
        }
        bg.setBorderRight(null, 0);
        bg.setBorderBottom(null, 0);
      }
      return bg;
    }
    catch (CloneNotSupportedException cne)
    {
      // should not happen
      throw new IllegalStateException("Clone caused an unexpected error.");
    }
  }


  /**
   * Creates an merged instance of this background for the given bounds. This
   * method updates the borders to fit the new bounds. If the old border
   * position is no longer valid, the border is removed.
   *
   * @return a copy of this background with new bounds.
   */
  public TableCellBackground normalize(final StrictBounds bounds)
  {
    try
    {
      final StrictBounds originalBounds = getBounds();
      final TableCellBackground bg = (TableCellBackground) clone();
      bg.setBounds((StrictBounds) bounds.clone());

      final long orgX2 = originalBounds.getX() + originalBounds.getWidth();
      final long orgY2 = originalBounds.getY() + originalBounds.getHeight();
      if ((bounds.getX() > orgX2 || bounds.getY() > orgY2) ||
              (bounds.getX() == orgX2 && bounds.getY() == orgY2))
      {
        // this cell is out of bounds ..
        bg.setBorderTop(null, 0);
        bg.setBorderBottom(null, 0);
        bg.setBorderLeft(null, 0);
        bg.setBorderRight(null, 0);
        bg.setColor(null);
      }
      else if (bounds.getX() == orgX2)
      {
        // this is a right border .. make it a left border definition ..
        bg.setBorderTop(null, 0);
        bg.setBorderBottom(null, 0);
        if (bg.getColorLeft() == null)
        {
          // only copy the right border, if there is no left border
          bg.setBorderLeft(getColorRight(), getBorderSizeRight());
        }
        bg.setBorderRight(null, 0);
        bg.setColor(null);
      }
      else if (bounds.getY() == orgY2)
      {
        // this is a bottom border element .. make it a top border definition ..
        if (bg.getColorTop() == null)
        {
          bg.setBorderTop(getColorBottom(), getBorderSizeBottom());
        }
        bg.setBorderBottom(null, 0);
        bg.setBorderLeft(null, 0);
        bg.setBorderRight(null, 0);
        bg.setColor(null);
      }
      else
      {
        if (bg.getTopBorderPos() != getTopBorderPos())
        {
          bg.setBorderTop(null, 0);
        }
        if (bg.getLeftBorderPos() != getLeftBorderPos())
        {
          bg.setBorderLeft(null, 0);
        }
        bg.setBorderRight(null, 0);
        bg.setBorderBottom(null, 0);
      }
      return bg;
    }
    catch (CloneNotSupportedException cne)
    {
      // should not happen
      throw new IllegalStateException("Clone caused an unexpected error.");
    }
  }

//
//  /**
//   * Updates the border positions (the effective bounds of this cell) after
//   * a layout-split-operation.
//   *
//   * @param bounds the new bounds.
//   */
//  public void updateBorderPositions (final StrictBounds bounds)
//  {
//    this.cellBounds.setRect(bounds.getX(), bounds.getY(), bounds.getWidth(), bounds.getHeight());
//  }

  /**
   * Adds two colors, the result is the mixed color of the base color and the
   * paint color.
   *
   * @param base  the base color
   * @param paint the overlay color
   * @return the merged colors.
   */
  private static Color addColor(final Color base, final Color paint)
  {
    if (paint.getAlpha() == 255)
    {
      return paint;
    }

    final float baseAlpha = (base.getAlpha() / 255f);
    final float paintAlpha = (paint.getAlpha() / 255f);
    final float effectiveAlpha = 1.0f - baseAlpha * paintAlpha;

    final double deltaAlpha = 1.0 - effectiveAlpha;
    final int red = (int)
            (base.getRed() * deltaAlpha + paint.getRed() * effectiveAlpha);
    final int green = (int)
            (base.getGreen() * deltaAlpha + paint.getGreen() * effectiveAlpha);
    final int blue = (int)
            (base.getBlue() * deltaAlpha + paint.getBlue() * effectiveAlpha);
    return new Color (red, green, blue, effectiveAlpha * 255);
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
   * Tests this object for equality with another object. This onyl compares the
   * background properties - not the elements.
   *
   * @param o the other object.
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
    if (colorBottom != null ? !colorBottom.equals(
            tableCellBackground.colorBottom)
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
    int result = Float.floatToIntBits(borderSizeTop);
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

  /**
   * Creates and returns a copy of this object.  The precise meaning of "copy"
   * may depend on the class of the object.
   *
   * @return a clone of this instance.
   * @throws CloneNotSupportedException if the object's class does not support
   *                                    the <code>Cloneable</code> interface.
   *                                    Subclasses that override the
   *                                    <code>clone</code> method can also throw
   *                                    this exception to indicate that an
   *                                    instance cannot be cloned.
   * @see Cloneable
   */
  public Object clone()
          throws CloneNotSupportedException
  {
    final TableCellBackground tb = (TableCellBackground) super.clone();
    if (anchors != null)
    {
      tb.anchors = (ArrayList) anchors.clone();
    }
    return tb;
  }

  public Anchor[] getAnchors()
  {
    if (anchors == null)
    {
      return EMPTY_ANCHOR_ARRAY;
    }
    return (Anchor[]) anchors.toArray(new Anchor[anchors.size()]);
  }

  public void addAnchor(final Anchor anchor)
  {
    if (anchor == null)
    {
      throw new NullPointerException();
    }
    if (anchors == null)
    {
      anchors = new ArrayList();
    }
    anchors.add(anchor);
  }

  public long getBottomBorderPos()
  {
    final StrictBounds bounds = getBounds();
    return bounds.getY() + bounds.getHeight();
  }

  public long getLeftBorderPos()
  {
    final StrictBounds cellBounds = getBounds();
    return cellBounds.getX();
  }

  public long getRightBorderPos()
  {
    final StrictBounds cellBounds = getBounds();
    return cellBounds.getX() + cellBounds.getWidth();
  }

  public long getTopBorderPos()
  {
    final StrictBounds cellBounds = getBounds();
    return cellBounds.getY();
  }
}
