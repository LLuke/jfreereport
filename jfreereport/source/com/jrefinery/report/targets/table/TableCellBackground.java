/**
 * Date: Jan 27, 2003
 * Time: 2:19:42 PM
 *
 * $Id$
 */
package com.jrefinery.report.targets.table;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class TableCellBackground extends TableCellData
{
  private float borderSizeTop;
  private float borderSizeBottom;
  private float borderSizeLeft;
  private float borderSizeRight;
  private Color colorTop;
  private Color colorLeft;
  private Color colorBottom;
  private Color colorRight;
  private Color color;

  public TableCellBackground(Rectangle2D outerBounds, Color color)
  {
    super(outerBounds);
    this.color = color;
  }

  public boolean isBackground()
  {
    return true;
  }

  public Color getColor()
  {
    return color;
  }

  public void setBorderTop (Color color, float size)
  {
    colorTop = color;
    borderSizeTop = size;
  }

  public void setBorderLeft (Color color, float size)
  {
    colorLeft = color;
    borderSizeLeft = size;
  }

  public void setBorderBottom (Color color, float size)
  {
    colorBottom = color;
    borderSizeBottom = size;
  }

  public void setBorderRight (Color color, float size)
  {
    colorRight = color;
    borderSizeRight = size;
  }

  public float getBorderSizeTop()
  {
    return borderSizeTop;
  }

  public float getBorderSizeBottom()
  {
    return borderSizeBottom;
  }

  public float getBorderSizeLeft()
  {
    return borderSizeLeft;
  }

  public float getBorderSizeRight()
  {
    return borderSizeRight;
  }

  public Color getColorTop()
  {
    return colorTop;
  }

  public Color getColorLeft()
  {
    return colorLeft;
  }

  public Color getColorBottom()
  {
    return colorBottom;
  }

  public Color getColorRight()
  {
    return colorRight;
  }

  /**
   * Merges this background with the given background and returns the
   * result. The given background is considered to be overlayed by this
   * background.
   *
   * @param background
   * @return
   */
  public TableCellBackground merge (TableCellBackground background)
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

    TableCellBackground merged = new TableCellBackground(getBounds(), color);
    mergeBorders(background, merged);
    return merged;
  }

  private void mergeBorders  (TableCellBackground background, TableCellBackground merged)
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

  public Color addColor (Color base, Color c)
  {
    BufferedImage img = new BufferedImage (1,1,BufferedImage.TYPE_INT_ARGB);
    Graphics g = img.getGraphics();

    g.setColor(Color.white);
    g.drawRect(0,0,1,1);

    g.setColor(base);
    g.drawRect(0,0,1,1);
    g.setColor(c);
    g.drawRect(0,0,1,1);

    return new Color (img.getRGB(0,0), true);
  }

  public String toString ()
  {
    StringBuffer b = new StringBuffer();
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
}
