/**
 * ===========================================
 * LibLayout : a free Java layouting library
 * ===========================================
 *
 * Project Info:  http://www.jfree.org/liblayout/
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2005, by Object Refinery Limited and Contributors.
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
 * BorderSpecification.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id$
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.layouting.model.border;

import org.jfree.layouting.input.style.values.CSSColorValue;
import org.jfree.layouting.input.style.keys.border.BorderStyle;

/**
 * Creation-Date: 14.12.2005, 23:59:13
 *
 * @author Thomas Morgner
 */
public class BorderSpecification
{
  private CSSColorValue topColor;
  private CSSColorValue bottomColor;
  private CSSColorValue leftColor;
  private CSSColorValue rightColor;
  private CSSColorValue breakColor;

  private long topWidth;
  private long bottomWidth;
  private long leftWidth;
  private long rightWidth;
  private long breakWidth;

  private BorderStyle topStyle;
  private BorderStyle bottomStyle;
  private BorderStyle leftStyle;
  private BorderStyle rightStyle;
  private BorderStyle breakStyle;

  private long topLeftHorizontalRadius;
  private long topLeftVerticalRadius;
  private long bottomLeftHorizontalRadius;
  private long bottomLeftVerticalRadius;
  private long topRightHorizontalRadius;
  private long topRightVerticalRadius;
  private long bottomRightHorizontalRadius;
  private long bottomRightVerticalRadius;



  public BorderSpecification()
  {
  }

  public CSSColorValue getTopColor()
  {
    return topColor;
  }

  public void setTopColor(final CSSColorValue topColor)
  {
    this.topColor = topColor;
  }

  public CSSColorValue getBottomColor()
  {
    return bottomColor;
  }

  public void setBottomColor(final CSSColorValue bottomColor)
  {
    this.bottomColor = bottomColor;
  }

  public CSSColorValue getLeftColor()
  {
    return leftColor;
  }

  public void setLeftColor(final CSSColorValue leftColor)
  {
    this.leftColor = leftColor;
  }

  public CSSColorValue getRightColor()
  {
    return rightColor;
  }

  public void setRightColor(final CSSColorValue rightColor)
  {
    this.rightColor = rightColor;
  }

  public long getTopWidth()
  {
    return topWidth;
  }

  public void setTopWidth(final long topWidth)
  {
    this.topWidth = topWidth;
  }

  public long getBottomWidth()
  {
    return bottomWidth;
  }

  public void setBottomWidth(final long bottomWidth)
  {
    this.bottomWidth = bottomWidth;
  }

  public long getLeftWidth()
  {
    return leftWidth;
  }

  public void setLeftWidth(final long leftWidth)
  {
    this.leftWidth = leftWidth;
  }

  public long getRightWidth()
  {
    return rightWidth;
  }

  public void setRightWidth(final long rightWidth)
  {
    this.rightWidth = rightWidth;
  }

  public BorderStyle getTopStyle()
  {
    return topStyle;
  }

  public void setTopStyle(final BorderStyle topStyle)
  {
    this.topStyle = topStyle;
  }

  public BorderStyle getBottomStyle()
  {
    return bottomStyle;
  }

  public void setBottomStyle(final BorderStyle bottomStyle)
  {
    this.bottomStyle = bottomStyle;
  }

  public BorderStyle getLeftStyle()
  {
    return leftStyle;
  }

  public void setLeftStyle(final BorderStyle leftStyle)
  {
    this.leftStyle = leftStyle;
  }

  public BorderStyle getRightStyle()
  {
    return rightStyle;
  }

  public void setRightStyle(final BorderStyle rightStyle)
  {
    this.rightStyle = rightStyle;
  }

  public long getTopLeftHorizontalRadius()
  {
    return topLeftHorizontalRadius;
  }

  public void setTopLeftHorizontalRadius(final long topLeftHorizontalRadius)
  {
    this.topLeftHorizontalRadius = topLeftHorizontalRadius;
  }

  public long getTopLeftVerticalRadius()
  {
    return topLeftVerticalRadius;
  }

  public void setTopLeftVerticalRadius(final long topLeftVerticalRadius)
  {
    this.topLeftVerticalRadius = topLeftVerticalRadius;
  }

  public long getBottomLeftHorizontalRadius()
  {
    return bottomLeftHorizontalRadius;
  }

  public void setBottomLeftHorizontalRadius(final long bottomLeftHorizontalRadius)
  {
    this.bottomLeftHorizontalRadius = bottomLeftHorizontalRadius;
  }

  public long getBottomLeftVerticalRadius()
  {
    return bottomLeftVerticalRadius;
  }

  public void setBottomLeftVerticalRadius(final long bottomLeftVerticalRadius)
  {
    this.bottomLeftVerticalRadius = bottomLeftVerticalRadius;
  }

  public long getTopRightHorizontalRadius()
  {
    return topRightHorizontalRadius;
  }

  public void setTopRightHorizontalRadius(final long topRightHorizontalRadius)
  {
    this.topRightHorizontalRadius = topRightHorizontalRadius;
  }

  public long getTopRightVerticalRadius()
  {
    return topRightVerticalRadius;
  }

  public void setTopRightVerticalRadius(final long topRightVerticalRadius)
  {
    this.topRightVerticalRadius = topRightVerticalRadius;
  }

  public long getBottomRightHorizontalRadius()
  {
    return bottomRightHorizontalRadius;
  }

  public void setBottomRightHorizontalRadius(final long bottomRightHorizontalRadius)
  {
    this.bottomRightHorizontalRadius = bottomRightHorizontalRadius;
  }

  public long getBottomRightVerticalRadius()
  {
    return bottomRightVerticalRadius;
  }

  public void setBottomRightVerticalRadius(final long bottomRightVerticalRadius)
  {
    this.bottomRightVerticalRadius = bottomRightVerticalRadius;
  }

  public CSSColorValue getBreakColor()
  {
    return breakColor;
  }

  public void setBreakColor(final CSSColorValue breakColor)
  {
    this.breakColor = breakColor;
  }

  public long getBreakWidth()
  {
    return breakWidth;
  }

  public void setBreakWidth(final long breakWidth)
  {
    this.breakWidth = breakWidth;
  }

  public BorderStyle getBreakStyle()
  {
    return breakStyle;
  }

  public void setBreakStyle(final BorderStyle breakStyle)
  {
    this.breakStyle = breakStyle;
  }
}
