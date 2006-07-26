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
 * BoxDefinition.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: DefaultBoxDefinition.java,v 1.3 2006/07/18 14:40:28 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.layouting.renderer.model;

import org.jfree.layouting.input.style.values.CSSColorValue;
import org.jfree.layouting.renderer.border.Border;
import org.jfree.layouting.renderer.border.RenderLength;

/**
 * Describes the margins, paddings, borders and sizes of a box. (This does not
 * define or describe the *actual* value used for the rendering, it describes
 * the stylesheet's computed values.)
 *
 * @author Thomas Morgner
 */
public class DefaultBoxDefinition implements BoxDefinition
{
  private RenderLength marginTop;
  private RenderLength marginBottom;
  private RenderLength marginLeft;
  private RenderLength marginRight;

  private RenderLength paddingTop;
  private RenderLength paddingLeft;
  private RenderLength paddingBottom;
  private RenderLength paddingRight;

  private Border border;

  private RenderLength minimumWidth;
  private RenderLength minimumHeight;
  private RenderLength maximumWidth;
  private RenderLength maximumHeight;
  private RenderLength preferredWidth;
  private RenderLength preferredHeight;

  private CSSColorValue backgroundColor;

  public DefaultBoxDefinition()
  {
  }

  public Border getBorder()
  {
    return border;
  }

  public void setBorder(final Border border)
  {
    this.border = border;
  }

  public RenderLength getMarginTop()
  {
    return marginTop;
  }

  public void setMarginTop(final RenderLength marginTop)
  {
    this.marginTop = marginTop;
  }

  public RenderLength getMarginBottom()
  {
    return marginBottom;
  }

  public void setMarginBottom(final RenderLength marginBottom)
  {
    this.marginBottom = marginBottom;
  }

  public RenderLength getMarginLeft()
  {
    return marginLeft;
  }

  public void setMarginLeft(final RenderLength marginLeft)
  {
    this.marginLeft = marginLeft;
  }

  public RenderLength getMarginRight()
  {
    return marginRight;
  }

  public void setMarginRight(final RenderLength marginRight)
  {
    this.marginRight = marginRight;
  }

  public RenderLength getPaddingTop()
  {
    return paddingTop;
  }

  public void setPaddingTop(final RenderLength paddingTop)
  {
    this.paddingTop = paddingTop;
  }

  public RenderLength getPaddingLeft()
  {
    return paddingLeft;
  }

  public void setPaddingLeft(final RenderLength paddingLeft)
  {
    this.paddingLeft = paddingLeft;
  }

  public RenderLength getPaddingBottom()
  {
    return paddingBottom;
  }

  public void setPaddingBottom(final RenderLength paddingBottom)
  {
    this.paddingBottom = paddingBottom;
  }

  public RenderLength getPaddingRight()
  {
    return paddingRight;
  }

  public void setPaddingRight(final RenderLength paddingRight)
  {
    this.paddingRight = paddingRight;
  }

  public RenderLength getMinimumWidth()
  {
    return minimumWidth;
  }

  public void setMinimumWidth(final RenderLength minimumWidth)
  {
    this.minimumWidth = minimumWidth;
  }

  public RenderLength getMinimumHeight()
  {
    return minimumHeight;
  }

  public void setMinimumHeight(final RenderLength minimumHeight)
  {
    this.minimumHeight = minimumHeight;
  }

  public RenderLength getMaximumWidth()
  {
    return maximumWidth;
  }

  public void setMaximumWidth(final RenderLength maximumWidth)
  {
    this.maximumWidth = maximumWidth;
  }

  public RenderLength getMaximumHeight()
  {
    return maximumHeight;
  }

  public void setMaximumHeight(final RenderLength maximumHeight)
  {
    this.maximumHeight = maximumHeight;
  }

  public RenderLength getPreferredWidth()
  {
    return preferredWidth;
  }

  public void setPreferredWidth(final RenderLength preferredWidth)
  {
    this.preferredWidth = preferredWidth;
  }

  public RenderLength getPreferredHeight()
  {
    return preferredHeight;
  }

  public void setPreferredHeight(final RenderLength preferredHeight)
  {
    this.preferredHeight = preferredHeight;
  }

  /**
   * Split the box definition for the given major axis. A horizontal axis will
   * perform vertical splits (resulting in a left and right box definition) and
   * a given vertical axis will split the box into a top and bottom box.
   *  
   * @param axis
   * @return
   */
  public BoxDefinition[] split(int axis)
  {
    if (axis == RenderNode.HORIZONTAL_AXIS)
    {
      return splitVertically();
    }
    return splitHorizontally();
  }

  public BoxDefinition[] splitVertically()
  {
    final Border[] borders = border.splitVertically(null);
    final DefaultBoxDefinition first = new DefaultBoxDefinition();
    first.marginTop = marginTop;
    first.marginLeft = marginLeft;
    first.marginBottom = marginBottom;
    first.marginRight = RenderLength.EMPTY;
    first.paddingBottom = paddingBottom;
    first.paddingTop = paddingTop;
    first.paddingLeft = paddingLeft;
    first.paddingRight = RenderLength.EMPTY;
    first.border = borders[0];
    first.preferredHeight = null;
    first.preferredWidth = null;
    first.minimumHeight = null;
    first.minimumWidth = null;
    first.maximumHeight = null;
    first.maximumWidth = null;

    final DefaultBoxDefinition second = new DefaultBoxDefinition();
    second.marginTop = marginTop;
    second.marginLeft = RenderLength.EMPTY;
    second.marginBottom = marginBottom;
    second.marginRight = marginRight;
    second.paddingBottom = paddingBottom;
    second.paddingTop = paddingTop;
    second.paddingLeft = RenderLength.EMPTY;
    second.paddingRight = paddingRight;
    second.border = borders[1];
    second.preferredHeight = null;
    second.preferredWidth = null;
    second.minimumHeight = null;
    second.minimumWidth = null;
    second.maximumHeight = null;
    second.maximumWidth = null;

    final BoxDefinition[] boxes = new BoxDefinition[2];
    boxes[0] = first;
    boxes[1] = second;
    return boxes;
  }

  public BoxDefinition[] splitHorizontally()
  {
    final Border[] borders = border.splitHorizontally(null);

    final DefaultBoxDefinition first = new DefaultBoxDefinition();
    first.marginTop = marginTop;
    first.marginLeft = marginLeft;
    first.marginBottom = RenderLength.EMPTY;
    first.marginRight = marginRight;
    first.paddingBottom = RenderLength.EMPTY;
    first.paddingTop = paddingTop;
    first.paddingLeft = paddingLeft;
    first.paddingRight = paddingRight;
    first.border = borders[0];
    first.preferredHeight = null;
    first.preferredWidth = null;
    first.minimumHeight = null;
    first.minimumWidth = null;
    first.maximumHeight = null;
    first.maximumWidth = null;

    final DefaultBoxDefinition second = new DefaultBoxDefinition();
    second.marginTop = RenderLength.EMPTY;
    second.marginLeft = marginLeft;
    second.marginBottom = marginBottom;
    second.marginRight = marginRight;
    second.paddingBottom = paddingBottom;
    second.paddingTop = RenderLength.EMPTY;
    second.paddingLeft = paddingLeft;
    second.paddingRight = paddingRight;
    second.border = borders[1];
    second.preferredHeight = null;
    second.preferredWidth = null;
    second.minimumHeight = null;
    second.minimumWidth = null;
    second.maximumHeight = null;
    second.maximumWidth = null;

    final BoxDefinition[] boxes = new BoxDefinition[2];
    boxes[0] = first;
    boxes[1] = second;
    return boxes;
  }

  public CSSColorValue getBackgroundColor()
  {
    return backgroundColor;
  }

  public void setBackgroundColor(final CSSColorValue backgroundColor)
  {
    this.backgroundColor = backgroundColor;
  }

  public boolean isEmpty()
  {
    if (paddingTop.getValue() != 0)
    {
      return false;
    }
    if (paddingLeft.getValue() != 0)
    {
      return false;
    }
    if (paddingBottom.getValue() != 0)
    {
      return false;
    }
    if (paddingRight.getValue() != 0)
    {
      return false;
    }
    return border.isEmpty();
  }
}
