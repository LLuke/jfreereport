/**
 * Date: Jan 25, 2003
 * Time: 1:46:36 PM
 *
 * $Id$
 */
package com.jrefinery.report.targets.table.excel;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;

import java.awt.Color;

public class ExcelBackgroundCellStyle implements ExcelCellStyle
{
  private ExcelBackgroundCellStyle parent;
  private short borderTop= HSSFCellStyle.BORDER_NONE;
  private short borderLeft = HSSFCellStyle.BORDER_NONE;
  private short borderRight = HSSFCellStyle.BORDER_NONE;
  private short borderBottom = HSSFCellStyle.BORDER_NONE;
  private Color color;

  public ExcelBackgroundCellStyle()
  {
  }

  public ExcelBackgroundCellStyle getParent()
  {
    return parent;
  }

  public void setParent(ExcelBackgroundCellStyle parent)
  {
    this.parent = parent;
  }

  public short getBorderTop()
  {
    return borderTop;
  }

  public void setBorderTop(short borderTop)
  {
    this.borderTop = borderTop;
  }

  public short getBorderLeft()
  {
    return borderLeft;
  }

  public void setBorderLeft(short borderLeft)
  {
    this.borderLeft = borderLeft;
  }

  public short getBorderRight()
  {
    return borderRight;
  }

  public void setBorderRight(short borderRight)
  {
    this.borderRight = borderRight;
  }

  public short getBorderBottom()
  {
    return borderBottom;
  }

  public void setBorderBottom(short borderBottom)
  {
    this.borderBottom = borderBottom;
  }

  public Color getColor()
  {
    return color;
  }

  public void setColor(Color color)
  {
    this.color = color;
  }

  public boolean isBackgroundStyle()
  {
    return true;
  }

  public boolean equals(Object o)
  {
    if (this == o) return true;
    if (!(o instanceof ExcelBackgroundCellStyle)) return false;

    final ExcelBackgroundCellStyle style = (ExcelBackgroundCellStyle) o;

    if (borderBottom != style.borderBottom) return false;
    if (borderLeft != style.borderLeft) return false;
    if (borderRight != style.borderRight) return false;
    if (borderTop != style.borderTop) return false;
    if (color != null ? !color.equals(style.color) : style.color != null) return false;
    if (parent != null ? !parent.equals(style.parent) : style.parent != null) return false;

    return true;
  }

  public int hashCode()
  {
    int result;
    result = (parent != null ? parent.hashCode() : 0);
    result = 29 * result + (int) borderTop;
    result = 29 * result + (int) borderLeft;
    result = 29 * result + (int) borderRight;
    result = 29 * result + (int) borderBottom;
    result = 29 * result + (color != null ? color.hashCode() : 0);
    return result;
  }
}
